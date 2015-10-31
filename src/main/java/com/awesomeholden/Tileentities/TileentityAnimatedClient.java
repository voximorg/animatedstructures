package com.awesomeholden.Tileentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileentityAnimatedClient extends TileEntity{
		
	public ResourceLocation texture = new ResourceLocation("textures/blocks/diamond_block.png");
	
	public static List<ResourceLocation> textures = new ArrayList<ResourceLocation>();
		
	public static int getTextureByString(String location){
		for(int ph=0;ph<textures.size();ph++){
			ResourceLocation c = textures.get(ph);
			if((c.getResourceDomain()+':'+c.getResourcePath()).equals(location)) return ph;
		}
		return -1;
	}
	
	
	
	//@Override
	//public void createAndLoadEntity(NBTTagCompound nbt){}
	
	public void realConstructor(){ //called in block class when added. Have to do this because minecraft takes some time to set Coords of tileentity.
		//System.out.println("IS CLIENT: "+FMLCommonHandler.instance().getEffectiveSide().isClient()+" also: "+isAdded);
		
		AnimationControllerClient c = ClientProxy.coordsInController(xCoord, yCoord, zCoord);
		
		if(c != null)
			ClientProxy.addTileentityToController(this, c);
		
		for(int i=0;i<ServerProxy.AnimationControllers.size();i++)
			ServerProxy.AnimationControllers.get(i).TileentitiesToBeAdded++;
					
	}

}
