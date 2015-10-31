package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DeleteAnimationControllers implements IMessage{

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	public static class Handler implements IMessageHandler<DeleteAnimationControllers,IMessage>{

		@Override
		public IMessage onMessage(DeleteAnimationControllers message,MessageContext ctx) {
			ServerProxy.AnimationControllers.clear();
			return null;
		}
		
	}

}
