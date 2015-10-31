package com.awesomeholden.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import scala.actors.threadpool.Arrays;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class UpdateControllerClientTextures implements IMessage{
	
	public int[] coords;
	
	public HashMap<Integer,List<Integer>> stuff;
	
	public UpdateControllerClientTextures(){}
	
	public UpdateControllerClientTextures(int[] coords,HashMap<Integer,List<Integer>> stuff){
		this.coords = coords;
		this.stuff = stuff;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
		
		stuff = new HashMap<Integer,List<Integer>>();
		
		int size = buf.readInt();
		for(int i=0;i<size;i++){
			List ls = new ArrayList<Integer>();
			stuff.put(buf.readInt(), ls);
			int size2 = buf.readInt();
			for(int i2=0;i2<size2;i2++){
				ls.add(buf.readInt());
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
		
		buf.writeInt(stuff.size());
		for(Entry< Integer, List<Integer> > e : stuff.entrySet()){
			buf.writeInt(e.getKey());
			//System.out.println("e: "+e);
			buf.writeInt(e.getValue().size());
			//System.out.println("SIZE: "+e.getValue().size());
			for(int i=0;i<e.getValue().size();i++){
				buf.writeInt(e.getValue().get(i));
				//System.out.println("GET I: "+e.getValue().get(i));
			}
		}
	}
	
	public static class Handler implements IMessageHandler<UpdateControllerClientTextures,IMessage>{

		@Override
		public IMessage onMessage(UpdateControllerClientTextures message,MessageContext ctx) {
			AnimationControllerClient c = ClientProxy.getAnimationController(message.coords);
			if(c != null){
				c.updateTextures(message.stuff);
			}else{
				List<TileEntity> ls = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
				for(int i=0;i<ls.size();i++){
					if(ls.get(i) instanceof TileentityAnimationEditorClient){
						//System.out.println("INSTANCE");
						TileentityAnimationEditorClient c2 =(TileentityAnimationEditorClient) ls.get(i);
						Main.network.sendToServer(new FindAnimationControllerServer(c2.xCoord,c2.yCoord,c2.zCoord));
					}
				}
				//}
			}
			return null;
		}
		
	}

}
