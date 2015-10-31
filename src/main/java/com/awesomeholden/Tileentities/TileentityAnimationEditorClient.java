package com.awesomeholden.Tileentities;

import com.awesomeholden.Main;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.packets.SendOnTileentityAnimatedDeleted;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileentityAnimationEditorClient extends TileEntity{
	
	public AnimationControllerClient controller;
			
	public TileentityAnimationEditorClient(){}
	
	public TileentityAnimationEditorClient(AnimationControllerClient c){
		controller = c;
		int[] p = c.coords;
		//System.out.println("TileentityAnimationEditor constructor coords: "+p[0]+','+p[1]+','+p[2]+','+p[3]+','+p[4]+','+p[5]);
		markDirty();
		
		//c.theControlled.addAll(ClientProxy.controllerAssignmentCache);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
	}
	
	@Override
	public void onChunkUnload(){
		/*Main.network.sendToServer(new SendOnTileentityAnimatedDeleted(controller.coords));*/
		ClientProxy.deleteAnimationController(controller.coords);
	}

}
