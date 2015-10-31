package com.awesomeholden.packets;

import net.minecraft.client.Minecraft;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandleOnTileentityAnimatedDeleted implements IMessageHandler<SendOnTileentityAnimatedDeleted, IMessage> {
    
    @Override
    public IMessage onMessage(SendOnTileentityAnimatedDeleted message, MessageContext ctx) {
        //System.out.println(String.format("Received %s from %s", message.text, ctx.getServerHandler().playerEntity.getDisplayName()));
    	ServerProxy.deleteAnimationController(message.coords);
    	return null; // no response in this case
    }

}
