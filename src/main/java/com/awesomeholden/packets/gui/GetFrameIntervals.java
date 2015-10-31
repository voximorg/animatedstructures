package com.awesomeholden.packets.gui;

import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GetFrameIntervals implements IMessage{
	
	public int[] coords;
	
	public GetFrameIntervals(){};
	
	public GetFrameIntervals(int[] coords){
		this.coords = coords;
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int ph=0;ph<6;ph++){
			int c = buf.readInt();
			coords[ph] = c;
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int ph=0;ph<6;ph++){
			buf.writeInt(coords[ph]);
		}
	}
	
	public static class Handler implements IMessageHandler<GetFrameIntervals, IMessage>{

		@Override
		public IMessage onMessage(GetFrameIntervals message, MessageContext ctx) {
			return new GetFrameIntervalsResponse(ServerProxy.getAnimationController(message.coords).frameIntervals);
		}
		
	}

}
