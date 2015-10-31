package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SetCoordsOnClient implements IMessage{
	
	public int[] coords;
	
	public SetCoordsOnClient(){}
	
	public SetCoordsOnClient(int[] coords){
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
	
	public static class Handler implements IMessageHandler<SetCoordsOnClient,IMessage>{

		@Override
		public IMessage onMessage(SetCoordsOnClient message, MessageContext ctx) {
			if(ClientProxy.controllerCoordsAssignmentCache.size()>0){
				ClientProxy.controllerCoordsAssignmentCache.get(0).coords = message.coords;
			}
			return null;
		}
		
	}

}
