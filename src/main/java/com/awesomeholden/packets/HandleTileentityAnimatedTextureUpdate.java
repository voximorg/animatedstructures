package com.awesomeholden.packets;

import java.util.List;

import scala.actors.threadpool.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.proxies.ClientProxy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandleTileentityAnimatedTextureUpdate implements IMessageHandler<SendTileentityAnimatedTextureUpdate, IMessage>{
	
	public static int h = 0;

	@Override
	public IMessage onMessage(SendTileentityAnimatedTextureUpdate message, MessageContext ctx) {
		
		/*try{
			System.out.println("COORDS CLIENT: "+Arrays.toString(ClientProxy.AnimationControllers.get(h).coords)+" H: "+h);
		}catch(IndexOutOfBoundsException e){}*/
		
		h++;
		if(h==ClientProxy.AnimationControllers.size())
			h = 0;
		
		AnimationControllerClient c = ClientProxy.getAnimationController(new int[]{message.coords00,message.coords01,message.coords02,message.coords10,message.coords11,message.coords12}
		);
		
		
		/*if(c != null){
			c.updateTextures(message.controlledIndex, message.texture);
		}else{
			System.out.println("COULD NOT FIND ANIMATIONCONTROLLERCLIENT");
			/*TileentityAnimationEditorClient c2 = ClientProxy.getEditorFromCoords(new int[]{message.coords00,message.coords01,message.coords02,message.coords10,message.coords11,message.coords12});
			if(c2 != null){*/
			/*List<TileEntity> ls = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
			for(int i=0;i<ls.size();i++){
				if(ls.get(i) instanceof TileentityAnimationEditorClient){
					//System.out.println("INSTANCE");
					TileentityAnimationEditorClient c2 =(TileentityAnimationEditorClient) ls.get(i);
					Main.network.sendToServer(new FindAnimationControllerServer(c2.xCoord,c2.yCoord,c2.zCoord));
				}
			}
			//}
		}*/
		
		return null;
	}

}
