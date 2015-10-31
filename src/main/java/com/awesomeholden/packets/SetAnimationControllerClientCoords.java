package com.awesomeholden.packets;

import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.proxies.ClientProxy;

import net.minecraft.client.Minecraft;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SetAnimationControllerClientCoords implements IMessage{
	
	public int editorX;
	public int editorY;
	public int editorZ;
	public int[] coords;
	
	public SetAnimationControllerClientCoords(){}
	
	public SetAnimationControllerClientCoords(int editorX,int editorY,int editorZ,int[] coords){
		this.editorX = editorX;
		this.editorY = editorY;
		this.editorZ = editorZ;
		this.coords = coords;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		editorX = buf.readInt();
		editorY = buf.readInt();
		editorZ = buf.readInt();
		
		coords = new int[6];
		for(int i=0;i<6;i++) coords[i] = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(editorX);
		buf.writeInt(editorY);
		buf.writeInt(editorZ);
		
		for(int i=0;i<6;i++) buf.writeInt(coords[i]);
	}
	
	
	public static class Handler implements IMessageHandler<SetAnimationControllerClientCoords,IMessage>{

		@Override
		public IMessage onMessage(SetAnimationControllerClientCoords message, MessageContext ctx) {
			//System.out.println("tHE GOLD MESSAGE WAS RECIEVED");
			TileentityAnimationEditorClient c = (TileentityAnimationEditorClient)
					Minecraft.getMinecraft().theWorld.getTileEntity(message.editorX, message.editorY, message.editorZ);
			
			if(c != null){
				c.controller.coords = message.coords;
				if(ClientProxy.AnimationControllers.indexOf(c.controller)<0)
					ClientProxy.AnimationControllers.add(c.controller);
				//System.out.println("THE GOLD C ISN'T NULL");
			}else{
				//System.out.println("THE GOLD C IS NULL");
			}
			return null;
		}
	}

}
