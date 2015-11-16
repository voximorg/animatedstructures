package com.awesomeholden.packets.gui;

import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SetFrameInterval implements IMessage{
	
	int[] coords;
	int ph;
	int value;
	
	public SetFrameInterval(){}
	
	public SetFrameInterval(int[] coords,int ph,int value){
		this.coords = coords;
		this.ph = ph;
		this.value = value;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		value = buf.readInt();
		ph = buf.readInt();
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(value);
		buf.writeInt(ph);
		for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
		
	}
	
	
	public static class Handler implements IMessageHandler<SetFrameInterval, IMessage>{

		@Override
		public IMessage onMessage(SetFrameInterval message, MessageContext ctx) {			
			ServerProxy.getAnimationController(message.coords).frameIntervals.set(message.ph, message.value);
			
			return null;
		}
		
	}

}
