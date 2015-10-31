package com.awesomeholden.packets;

import scala.actors.threadpool.Arrays;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CreateAnimationControllerServer implements IMessage{
	
	public int[] coords;
	public String player;
	
	public CreateAnimationControllerServer(){}
	
	public CreateAnimationControllerServer(int[] coords,String player){
		this.coords = coords;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
		
		player = ByteBufUtils.readUTF8String(buf);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
		
		ByteBufUtils.writeUTF8String(buf,player);
		
	}
	
	public static class Handler implements IMessageHandler<CreateAnimationControllerServer, IMessage> {
	    
	    @Override
	    public IMessage onMessage(CreateAnimationControllerServer message, MessageContext ctx) {
	    		
	    	
	    	
	    	AnimationControllerServer c;
	    	TileentityAnimationEditorServer c2 = null;
	    	if(ServerProxy.controllerCoordsAssigmentCache.size() > 0){
	    		c = ServerProxy.controllerCoordsAssigmentCache.get(0);
	    		int[] coords = c.coords;
	    		if(coords[0] == 0 && coords[1] == 0 && coords[2] == 0 && coords[3] == 0 && coords[4] == 0 && coords[5] == 0){
	    			c.coords = message.coords;
	    		}
	    		c.dimension = ServerProxy.getPlayer(message.player).dimension;
	    		
	    		WorldServer w = MinecraftServer.getServer().worldServers[ServerProxy.getPlayer(message.player).dimension];
				
	    				c2 = ServerProxy.getEditorFromCoords(c.coords,ServerProxy.getPlayer(message.player).dimension);
						int[] cc = c2.controller.coords;
						ServerProxy.ignore = c2.controller;
						if(ServerProxy.coordsInController(cc[0], cc[1], cc[2]) != null 
				    			|| ServerProxy.coordsInController(cc[3], cc[4], cc[5]) != null){
							ServerProxy.AnimationControllers.remove(Main.indexOf(ServerProxy.AnimationControllers,(Object) c2.controller));
							w.setBlock(c2.xCoord,c2.yCoord,c2.zCoord,Blocks.air);
				}
						
	    	}/*else{
	    		AnimationControllerServer c = new AnimationControllerServer();
	    		c.coords = new int[]{message.p1, message.p2, message.p3, message.p4, message.p5, message.p6};
	    	}*/
	    		    	
			    ServerProxy.controllerCoordsAssigmentCache.clear();

	    	return null; // no response in this case 
	    	
	    }

	}


}
