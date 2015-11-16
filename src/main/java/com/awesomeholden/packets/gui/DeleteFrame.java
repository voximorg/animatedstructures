package com.awesomeholden.packets.gui;

import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DeleteFrame implements IMessage{
	
	public int[] coords;
	public int frame;
	
	public DeleteFrame(){}
	
	public DeleteFrame(int[] coords, int frame){
		this.coords = coords;
		this.frame = frame;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		frame = buf.readInt();
		
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(frame);
		
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
	}
	
	public static class Handler implements IMessageHandler<DeleteFrame,IMessage>{

		@Override
		public IMessage onMessage(DeleteFrame message, MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
			
			if(c.framesInfo.size()>1){
				c.frameIntervals.remove(message.frame);
				
				c.framesInfo.remove(message.frame);
				c.frameIndex = 0;
			}
			return null;
		}
		
	}
	
	

}
