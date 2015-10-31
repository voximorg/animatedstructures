package com.awesomeholden.packets;

import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CallTileentityAnimatedRealConstructor implements IMessage{
	
	public int x;
	public int y;
	public int z;
	
	public CallTileentityAnimatedRealConstructor(){}
	
	public CallTileentityAnimatedRealConstructor(int x,int y,int z){
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
	
	public static class Handler implements IMessageHandler<CallTileentityAnimatedRealConstructor,IMessage>{

		@Override
		public IMessage onMessage(CallTileentityAnimatedRealConstructor message,MessageContext ctx) {
			TileentityAnimatedClient c = ClientProxy.getTileentityAnimated(message.x, message.y, message.z);
			
			if(c == null){
				ClientProxy.tryingToCallRealConstructor.add(message.x);
				ClientProxy.tryingToCallRealConstructor.add(message.y);
				ClientProxy.tryingToCallRealConstructor.add(message.z);
			}else{
				c.realConstructor();
			}
			return null;
		}
		
	}

}
