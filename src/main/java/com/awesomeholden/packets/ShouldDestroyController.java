package com.awesomeholden.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.util.ChatComponentText;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.proxies.ClientProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ShouldDestroyController implements IMessage{
	
	public int[] coords;
	
	public ShouldDestroyController(){}
	
	public ShouldDestroyController(int[] coords){
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
	
	public static class Handler implements IMessageHandler<ShouldDestroyController,IMessage>{

		@Override
		public IMessage onMessage(ShouldDestroyController message,MessageContext ctx) {
			AnimationControllerClient c = ClientProxy.getAnimationController(message.coords);
			
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			player.addChatMessage(new ChatComponentText("Are you sure? Press y or n."));
			
			ClientLoop.tryingToDestroy = c;
			
			return null;
		}
		
	}

}
