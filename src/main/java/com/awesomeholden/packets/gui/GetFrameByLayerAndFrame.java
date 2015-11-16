package com.awesomeholden.packets.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GetFrameByLayerAndFrame implements IMessage{
	
	public int smallestY;
	public int currentFrame;
	public int layer;
	int[] coords;
	
	public GetFrameByLayerAndFrame(){}
	
	public GetFrameByLayerAndFrame(int smallestY,int currentFrame,int layer,int[] coords){
		this.smallestY = smallestY;
		this.currentFrame = currentFrame;
		this.layer = layer;
		this.coords = coords;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		smallestY = buf.readInt();
		currentFrame = buf.readInt();
		layer = buf.readInt();
		
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(smallestY);
		buf.writeInt(currentFrame);
		buf.writeInt(layer);
		for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
	}
	
	
	public static class Handler implements IMessageHandler<GetFrameByLayerAndFrame,IMessage>{

		@Override
		public IMessage onMessage(GetFrameByLayerAndFrame message, MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
						
			HashMap<Integer,List<Integer>> list = c.framesInfo.get(message.currentFrame);
			
			List<TileentityAnimatedServer> onLayer = new ArrayList<TileentityAnimatedServer>();
			for(int ph=0;ph<c.theControlled.size();ph++){
				if(c.theControlled.get(ph).yCoord-message.smallestY == message.layer)
					onLayer.add(c.theControlled.get(ph));
			}
						
			int[] ghostFrames = new int[onLayer.size()];
			
			for(int i=0;i<ghostFrames.length;i++) //for animatedstructures:textures/transparent.png because that is TileentityAnimatedClient.textures.get(0)
				ghostFrames[i] = -1;
			
			if(c.framesInfo.size()>1){
				for(int i=message.currentFrame;i>-1;i--){
					for(Entry<Integer,List<Integer>> e : c.framesInfo.get(i).entrySet()){
						List<Integer> ls = e.getValue();
						for(int i2=0;i2<ls.size();i2++){
							int current = ls.get(i2);
							if(c.theControlled.get(current).yCoord-message.smallestY != message.layer)
								continue;
							
							for(int i3=0;i3<onLayer.size();i3++){
								if(onLayer.get(i3) == c.theControlled.get(current) && ghostFrames[i3] == -1)
									ghostFrames[i3] = e.getKey();
							}
						}
					}
				}
			}
			
			for(int i=0;i<ghostFrames.length;i++)
				if(ghostFrames[i] == -1)
					ghostFrames[i] = 0;
			
			int[] frames = new int[onLayer.size()];
			
			for(int ph=0;ph<onLayer.size();ph++){
				boolean found = false;
				for(Entry<Integer,List<Integer>> e : list.entrySet()){
					for(int i=0;i<e.getValue().size();i++){
						if(e.getValue().get(i) == c.theControlled.indexOf(onLayer.get(ph))){
							frames[ph] = e.getKey();
							ghostFrames[ph] = e.getKey();
							found = true;
						}
					}
				}
				if(!found)
					frames[ph] = 0;
			}
			return new Response(frames,ghostFrames);
		}
		
	}
	
	
	public static class Response implements IMessage{
		
		public int[] frames;
		public int[] ghostFrames;
		
		public Response(){}
		
		public Response(int[] frames,int[] ghostFrames){
			this.frames = frames;
			this.ghostFrames = ghostFrames;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			int size = buf.readInt();
			frames = new int[size];
			
			if(size==0)
				frames = new int[1];
			
			ghostFrames = new int[size];
			for(int ph=0;ph<size;ph++){
				frames[ph] = buf.readInt();
				ghostFrames[ph] = buf.readInt();
			}
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(frames.length);
			for(int ph=0;ph<frames.length;ph++){
				buf.writeInt(frames[ph]);
				buf.writeInt(ghostFrames[ph]);
			}
		}
		
		
		public static class Handler implements IMessageHandler<Response,IMessage>{

			@Override
			public IMessage onMessage(Response message, MessageContext ctx) {
					
				if(ClientProxy.gui == null)
					return null;
				
				ClientProxy.gui.frames = new int[message.frames.length];
				ClientProxy.gui.ghostFrames = new int[message.frames.length];
				int[] frames = ClientProxy.gui.frames;
				for(int ph=0;ph<frames.length;ph++){
					frames[ph] = message.frames[ph];
					try{
						ClientProxy.gui.ghostFrames[ph] = message.ghostFrames[ph];
					}catch(IndexOutOfBoundsException e){}
				}
				
				return null;
			}
			
		}
		
	}

}
