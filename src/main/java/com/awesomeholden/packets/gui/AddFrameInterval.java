package com.awesomeholden.packets.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class AddFrameInterval implements IMessage{
	
	public int[] coords;
	public int value;
	
	public AddFrameInterval(){}
	
	public AddFrameInterval(int[] coords,int value){
		this.coords = coords;
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		value = buf.readInt();
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(value);
		for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
	}
	
	
	public static class Handler implements IMessageHandler<AddFrameInterval,IMessage>{

		@Override
		public IMessage onMessage(AddFrameInterval message, MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
			c.frameIntervals.add(message.value);
			c.framesInfo.add(new HashMap<Integer,List<Integer>>());
			
			System.out.println("CURRENT FRAME: "+(c.framesInfo.size()-1));
			
			HashMap<Integer,List<Integer>> list = c.framesInfo.get(c.framesInfo.size()-1);
			for(Entry<Integer,List<Integer>> e : list.entrySet()){
				System.out.println("TEXTURE: "+e.getKey());
				System.out.println("SIZE: "+e.getValue().size());
			}
			return null;
		}
		
	}

}
