package com.awesomeholden.packets;

import com.awesomeholden.Main;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.CommonProxy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandleCallTileentityAnimatedConstructor implements IMessageHandler<SendCallTileentityAnimatedConstructor, IMessage> {
    
    @Override
    public IMessage onMessage(SendCallTileentityAnimatedConstructor message, MessageContext ctx) {
    	ClientProxy.callTileentityAnimatedConstructor(message.x,message.y,message.z);
    	return null; // no response in this case
    }

}