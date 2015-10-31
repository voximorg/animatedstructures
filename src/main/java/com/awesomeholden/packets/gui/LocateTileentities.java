package com.awesomeholden.packets.gui;

import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class LocateTileentities implements IMessage{
	
	public int[] coords;
	
	public LocateTileentities(){}
	
	public LocateTileentities(int[] coords){
		this.coords = coords;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
	}
	
	public static class Handler implements IMessageHandler<LocateTileentities, IMessage>{

		@Override
		public IMessage onMessage(LocateTileentities message, MessageContext ctx) {
			//ServerProxy.getAnimationController(message.coords).
			return null;
		}
		
	}

}
