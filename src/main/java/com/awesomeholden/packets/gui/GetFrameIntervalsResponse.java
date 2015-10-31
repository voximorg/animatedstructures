package com.awesomeholden.packets.gui;

import java.util.ArrayList;
import java.util.List;

import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GetFrameIntervalsResponse implements IMessage{
	
	public List<Integer> frameIntervals;
	
	public GetFrameIntervalsResponse(){}
	
	public GetFrameIntervalsResponse(List<Integer> frameIntervals){
		this.frameIntervals = frameIntervals;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		frameIntervals = new ArrayList<Integer>();
		int size = buf.readInt();
		for(int ph=0;ph<size;ph++){
			frameIntervals.add(buf.readInt());
		}
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//System.out.println("frameIntervals.size(): "+frameIntervals.size());
		buf.writeInt(frameIntervals.size());
		for(int ph=0;ph<frameIntervals.size();ph++){
			buf.writeInt(frameIntervals.get(ph));
		}
	}
	
	public static class Handler implements IMessageHandler<GetFrameIntervalsResponse, IMessage>{

		@Override
		public IMessage onMessage(GetFrameIntervalsResponse message, MessageContext ctx) {
			ClientProxy.gui.frameIntervals = message.frameIntervals;
			ClientProxy.gui.addTimelineBottoms();
			return null;
		}
	}

}
