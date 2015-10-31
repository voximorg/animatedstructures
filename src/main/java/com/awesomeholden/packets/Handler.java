package com.awesomeholden.packets;

import net.minecraft.client.multiplayer.WorldClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Handler implements IMessageHandler<Send, IMessage> {
    
    @Override
    public IMessage onMessage(Send message, MessageContext ctx) {
        //System.out.println(String.format("Received %s from %s", message.text, ctx.getServerHandler().playerEntity.getDisplayName()));
        System.out.println(message.text);
    	return null; // no response in this case
    }

}
