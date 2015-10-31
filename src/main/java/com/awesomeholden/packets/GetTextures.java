package com.awesomeholden.packets;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GetTextures implements IMessage{
	

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}
	
	public static class Handler implements IMessageHandler<GetTextures,IMessage>{

		@Override
		public IMessage onMessage(GetTextures message, MessageContext ctx) {
			// TODO Auto-generated method stub
			return new TestPacket();
		}
		
	}

}
