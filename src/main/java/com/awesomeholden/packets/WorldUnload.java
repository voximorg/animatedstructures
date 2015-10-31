package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class WorldUnload implements IMessage{
	
	public WorldUnload(){}

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	public static class Handler implements IMessageHandler<WorldUnload,IMessage>{

		@Override
		public IMessage onMessage(WorldUnload message, MessageContext ctx) {
			ClientProxy.AnimationControllers.clear();
			return null;
		}
		
	}

}
