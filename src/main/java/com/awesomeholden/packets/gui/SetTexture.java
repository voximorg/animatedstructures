package com.awesomeholden.packets.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SetTexture implements IMessage{
	
	public int TileentityIndex;
	public String location;
	public int[] coords;
	public int currentFrame;
	
	public SetTexture(){}
	
	public SetTexture(String location,int TileentityIndex,int[] coords,int currentFrame){
		this.location = location;
		this.TileentityIndex = TileentityIndex;
		this.coords = coords;
		this.currentFrame = currentFrame;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		TileentityIndex = buf.readInt();
		location = ByteBufUtils.readUTF8String(buf);
		currentFrame = buf.readInt();
		
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(TileentityIndex);
		ByteBufUtils.writeUTF8String(buf, location);
		buf.writeInt(currentFrame);
		
		for(int ph=0;ph<6;ph++){
			buf.writeInt(coords[ph]);
		}
	}
	
	
	public static class Handler implements IMessageHandler<SetTexture, IMessage>{
		
		public String removeExtension(String str){
			String ret = "";
			boolean doo = false;
			for(int i=str.length()-1;i>-1;i--){
				if(doo)
					ret+=str.charAt(i);
				
				if(((Character)str.charAt(i)).compareTo('.') == 0)
					doo = true;
			}
			
			String ret2 = "";
			for(int i=ret.length()-1;i>-1;i--)
				ret2+=ret.charAt(i);
			
			return ret2;
		}

		@Override
		public IMessage onMessage(SetTexture message, MessageContext ctx) {
			/*try{ //bad ass
				ResourceLocation r = new ResourceLocation(message.location);
				//new TextureManager(null).loadTexture(r, (ITextureObject)new SimpleTexture(r));
				
				((ITextureObject)new SimpleTexture(r)).loadTexture(Minecraft.getMinecraft().getResourceManager());
			}catch(IOException  e2){
				System.out.println("IOException");
				return null;
			}*/
						
			message.location = removeExtension(message.location);
			
			ResourceLocation r = new ResourceLocation(message.location);
									
			boolean found = false;
						
			if(r.getResourceDomain().equals("minecraft")){
				
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
							.getResourceAsStream("assets/animatedstructures/minecrafttextures"), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String thisLine;
				try {
					
					while ((thisLine = br.readLine()) != null) {
					    if((r.getResourcePath()+".png").equals("textures/"+thisLine)){
					    	found = true;
					    	break;
					    }
					 }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     
			}else{
			
			File[] files = new File(Main.workingDir).listFiles();
			
			for(int i=0;i<files.length;i++){
				
				URL url = null;
				try {
					url = new URL("jar:file:"+Main.workingDir+File.separator+files[i].getName()+"!/assets/"+r.getResourceDomain()+File.separator+r.getResourcePath()+".png");
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				InputStream is = null;
				try{
					is = url.openStream();
				}catch(FileNotFoundException e2){} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(is != null){
					found = true;
					break;
				}
				
			}
			}
			
			if(!found)
				return null;
			
			//Main.class.getResource(name)
			
			//System.out.println("WORKINF DIR: "+System.getProperty("user.dir"));
			
			TileentityAnimatedServer.addTextureToAll(message.location+".png");
			HashMap<Integer,List<Integer>> map = ServerProxy.getAnimationController(message.coords).framesInfo.get(message.currentFrame);
			
			HashMap<Integer,List<Integer>> nw = new HashMap<Integer,List<Integer>>();
			for(Entry<Integer,List<Integer>> e : map.entrySet()){
								
				List<Integer> tmp = new ArrayList<Integer>();
				for(int i=0;i<e.getValue().size();i++){
					if(e.getValue().get(i) != message.TileentityIndex){
						tmp.add(e.getValue().get(i));
					}
				}
				nw.put(e.getKey(),tmp);
			}
			
			List<HashMap<Integer,List<Integer>>> ls = ServerProxy.getAnimationController(message.coords).framesInfo;
			
			ls.set(message.currentFrame, nw);
			
			map = ServerProxy.getAnimationController(message.coords).framesInfo.get(message.currentFrame);
			
			List<Integer> tileentities = map.get(TileentityAnimatedServer.getTextureByString(message.location+".png"));
			
			if(tileentities == null){
				tileentities = new ArrayList<Integer>();
				map.put(TileentityAnimatedServer.getTextureByString(message.location+".png"), tileentities);
			}
			
			tileentities.add(message.TileentityIndex);
			
			return new SetTexture.Response(message.location+".png");
		}
		
	}
	
	
	public static class Response implements IMessage{
		
		public boolean isAdded = true;
		
		public Response(){}
		
		public Response(String location){
			if(TileentityAnimatedServer.getTextureByString(location) == -1) isAdded = false;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			isAdded = buf.readBoolean();
			
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeBoolean(isAdded);
			
		}
		
		
		public static class Handler implements IMessageHandler<Response,IMessage>{

			@Override
			public IMessage onMessage(Response message, MessageContext ctx) {
				AnimationEditorGui g = ClientProxy.gui;
				if(g != null){
					Main.network.sendToServer(new GetFrameByLayerAndFrame(g.smallestCoords[1],g.currentFrame,g.currentLayer,g.controller.coords));
				}
				//g.selectedBlock.frames.set(g.currentFrame, TileentityAnimatedServer.getTextureByString(g.textureBox.text));
				return null;
			}
			
		}
		
	}

}
