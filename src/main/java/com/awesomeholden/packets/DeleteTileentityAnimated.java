package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DeleteTileentityAnimated implements IMessage{
	
	public int x;
	public int y;
	public int z;
	
	public DeleteTileentityAnimated(){}
	
	public DeleteTileentityAnimated(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}
	
	public static class Handler implements IMessageHandler<DeleteTileentityAnimated,IMessage>{

		@Override
		public IMessage onMessage(DeleteTileentityAnimated message,MessageContext ctx) {
			ServerProxy.deleteTileentityAnimated(message.x, message.y, message.z);
			return null;
		}
		
	}

}
