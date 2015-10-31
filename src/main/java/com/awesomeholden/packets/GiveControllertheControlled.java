package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GiveControllertheControlled implements IMessage{
	
	public int[] coords;
	
	public GiveControllertheControlled(){}
	
	public GiveControllertheControlled(int[] coords){
		this.coords = coords;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
	}
	
	public static class Handler implements IMessageHandler<GiveControllertheControlled,IMessage>{

		@Override
		public IMessage onMessage(GiveControllertheControlled message,MessageContext ctx) {
			//ClientProxy.getAnimationController(message.coords).;
			return null;
		}
		
	}

}
