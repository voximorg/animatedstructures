package com.awesomeholden.packets;

import net.minecraft.server.MinecraftServer;

import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RemoveEditorServer implements IMessage{
	
	public int[] coords;
	
	public RemoveEditorServer(){}
	
	public RemoveEditorServer(int[] coords){
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
	
	public static class Handler implements IMessageHandler<RemoveEditorServer,IMessage>{

		@Override
		public IMessage onMessage(RemoveEditorServer message, MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
			ServerProxy.AnimationControllers.remove(c);
			MinecraftServer.getServer().worldServers[c.dimension].loadedTileEntityList.remove(ServerProxy.getEditorFromCoords(message.coords, c.dimension));
			return null;
		}
		
	}

}
