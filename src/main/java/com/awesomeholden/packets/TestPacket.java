package com.awesomeholden.packets;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TestPacket implements IMessage{
	
	public TestPacket(){
		System.out.println("Send");
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		System.out.println("THE TEST PACKET READS "+buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(20);
	}
	
	public static class Hanlder implements IMessageHandler<TestPacket, IMessage>{

		@Override
		public IMessage onMessage(TestPacket message, MessageContext ctx) {
			System.out.println("Recieved");
			return null;
		}
		
	}

}
