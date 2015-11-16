package com.awesomeholden.packets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.awesomeholden.Main;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class RefreshAnimation implements IMessage{
	
	public int[] coords;
	
	public RefreshAnimation(){}
	
	public RefreshAnimation(int[] coords){
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
	
	public static class Handler implements IMessageHandler<RefreshAnimation,IMessage>{

		@Override
		public IMessage onMessage(RefreshAnimation message, MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
			c.tick = 0;
			
			HashMap<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
			List<Integer> list = new ArrayList<Integer>();
			
			for(int i=0;i<c.theControlled.size();i++){
				list.add(i);
			}
			
			map.put(0, list);
			
			Main.network.sendToAllAround(new UpdateControllerClientTextures(message.coords,map),c.genTargetPoint());
			return null;
		}
		
	}

}
