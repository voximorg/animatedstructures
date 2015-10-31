package com.awesomeholden.packets;

import net.minecraft.entity.player.EntityPlayerMP;

import com.awesomeholden.ServerLoop;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class DropGlass implements IMessage {
	
	public String playerName;
	
	public DropGlass(){}
	public DropGlass(String playerName){
		this.playerName = playerName;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		playerName = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, playerName);
	}
	
	public static class Handler implements IMessageHandler<DropGlass,IMessage>{

		@Override
		public IMessage onMessage(DropGlass message,MessageContext ctx) {
			EntityPlayerMP p = ServerProxy.getPlayer(message.playerName);
			System.out.println("PACKET P: "+p);
			System.out.println("FIRST PACKET 4");
		      ServerLoop.trackedItems.put(p.dropOneItem(false),0D);
			return null;
		}
	}

}
