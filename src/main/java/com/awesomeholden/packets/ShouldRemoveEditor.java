package com.awesomeholden.packets;

import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ShouldRemoveEditor implements IMessage{
	
	public int x;
	public int y;
	public int z;
	public int dimension;
	
	public ShouldRemoveEditor(){}
	
	public ShouldRemoveEditor(int x,int y,int z,int dimension){
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		dimension = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(dimension);
	}
	
	
	public static class Handler implements IMessageHandler<ShouldRemoveEditor,IMessage>{

		@Override
		public IMessage onMessage(ShouldRemoveEditor message, MessageContext ctx) {
			WorldServer w = MinecraftServer.getServer().worldServers[message.dimension];
			
			TileentityAnimationEditorServer c = (TileentityAnimationEditorServer)w.getTileEntity(message.x, message.y, message.z);
			for(int i=0;i<w.loadedTileEntityList.size();i++){
				if(w.loadedTileEntityList.get(i) instanceof TileentityAnimationEditorServer){
					TileentityAnimationEditorServer c2 = (TileentityAnimationEditorServer) w.loadedTileEntityList.get(i);
					
					int[] cc = c.controller.coords;
					if(ServerProxy.coordsInController(cc[0], cc[1], cc[2]) != null 
			    			|| ServerProxy.coordsInController(cc[3], cc[4], cc[5]) != null){
						ServerProxy.AnimationControllers.remove(Main.indexOf(ServerProxy.AnimationControllers,(Object) c.controller));
						w.setBlock(message.x,message.y,message.z,Blocks.air);
						System.out.println("SERVER DID IT");
						return null;
					}
				}
			}

			return null;
		}
		
	}

}
