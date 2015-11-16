package com.awesomeholden.packets;

import net.minecraft.util.ResourceLocation;

import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class AddTextureToClients implements IMessage{
	
	public String location;
	
	public AddTextureToClients(){}
	
	public AddTextureToClients(String location){
		this.location = location;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		location = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, location);
	}
	
	
	public static class Handler implements IMessageHandler<AddTextureToClients,IMessage>{

		@Override
		public IMessage onMessage(AddTextureToClients message,MessageContext ctx) {
			if(TileentityAnimatedClient.getTextureByString(message.location) == -1){
				TileentityAnimatedClient.textures.add(new ResourceLocation(message.location));
			}
			return null;
		}
		
	}

}
