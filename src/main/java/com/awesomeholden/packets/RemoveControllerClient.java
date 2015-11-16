package com.awesomeholden.packets;

import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RemoveControllerClient implements IMessage{
	
	public int[] coords;
	
	public RemoveControllerClient(){}
	
	public RemoveControllerClient(int[] coords){
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
	
	public static class Handler implements IMessageHandler<RemoveControllerClient,IMessage>{

		@Override
		public IMessage onMessage(RemoveControllerClient message, MessageContext ctx) {
			ClientProxy.deleteAnimationController(message.coords);
			return null;
		}
		
	}

}
