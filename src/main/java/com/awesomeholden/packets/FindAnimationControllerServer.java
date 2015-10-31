package com.awesomeholden.packets;

import com.awesomeholden.Main;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class FindAnimationControllerServer implements IMessage{
	
	public int x;
	public int y;
	public int z;
	
	public FindAnimationControllerServer(){}
	
	public FindAnimationControllerServer(int x,int y,int z){
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
	
	public static class Handler implements IMessageHandler<FindAnimationControllerServer,IMessage>{

		@Override
		public IMessage onMessage(FindAnimationControllerServer message,MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationControllerFromEditor(message.x, message.y, message.z);
			
			if(c != null){
				Main.network.sendToAll(new SetAnimationControllerClientCoords(message.x,message.y,message.z,c.coords));
			}else{
				System.out.println("C ++ NULL");
			}
			return null;
		}
	}

}
