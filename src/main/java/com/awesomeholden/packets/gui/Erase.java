package com.awesomeholden.packets.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.awesomeholden.Main;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Erase implements IMessage{
	
	public int[] coords;
	public int frame;
	public int index;
	
	public Erase(){}
	
	public Erase(int[] coords,int frame,int index){
		this.frame = frame;
		this.index = index;
		this.coords = coords;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		frame = buf.readInt();
		index = buf.readInt();
		
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(frame);
		buf.writeInt(index);
		
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
		
	}
	
	public static class Handler implements IMessageHandler<Erase,IMessage>{

		@Override
		public IMessage onMessage(Erase message, MessageContext ctx) {
			HashMap<Integer,List<Integer>> map = ServerProxy.getAnimationController(message.coords).framesInfo.get(message.frame);
			
			for(Entry<Integer,List<Integer>> e : map.entrySet()){
				for(int i=0;i<e.getValue().size();i++){
					if(e.getValue().get(i) == message.index){
						e.getValue().remove(i);
						
						return null;
					}
				}
			}
			return null;
		}
		
	}

}
