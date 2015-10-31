package com.awesomeholden.Tileentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.awesomeholden.Main;
import com.awesomeholden.ServerLoop;
import com.awesomeholden.blocks.BlockAnimated;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.packets.AddTextureToClients;
import com.awesomeholden.packets.DeleteTileentityAnimated;
import com.awesomeholden.packets.SendOnTileentityAnimatedDeleted;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.CommonProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.FMLCommonHandler;

public class TileentityAnimatedServer extends TileEntity{
	
	public static List<ResourceLocation> textures = new ArrayList<ResourceLocation>();
							
	//public static IIcon icon = register.registerIcon(Main.MODID+":Icon");
		
	/*@Override
	public void readFromNBT(NBTTagCompound nbt) {
	  super.readFromNBT(nbt);
	  //active = nbt.getBoolean("active");
	  //fuse = nbt.getInteger("fuse");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
	  super.writeToNBT(nbt);
	  nbt.setString("texture",texture.getResourceDomain()+texture.getResourcePath());
	  //nbt.setBoolean("active", active);
	  //nbt.setInteger("fuse", fuse);
	}*/
		
	public void realConstructor(){ //called in block class when added. Have to do this because minecraft takes some time to set Coords of tileentity.
		//System.out.println("IS CLIENT: "+FMLCommonHandler.instance().getEffectiveSide().isClient()+" also: "+isAdded);
		
		AnimationControllerServer c = ServerProxy.coordsInController(xCoord, yCoord, zCoord);
		
		if(c != null)
			ServerProxy.addTileentityToController(this, c);
		
		for(int i=0;i<ServerProxy.AnimationControllers.size();i++)
			ServerProxy.AnimationControllers.get(i).TileentitiesToBeAdded++;
		
	}
	
	public static int getTextureByString(String location){
		for(int ph=0;ph<textures.size();ph++){
			ResourceLocation c = textures.get(ph);
			if((c.getResourceDomain()+':'+c.getResourcePath()).equals(location)) return ph;
		}
		return -1;
	}
	
	public static void addTextureToAll(String location){
		if(getTextureByString(location) == -1){
			TileentityAnimatedServer.textures.add(new ResourceLocation(location));
			ServerLoop.textureData.markDirty();
			Main.network.sendToAll(new AddTextureToClients(location));
		}
	}
	
	@Override
	public void onChunkUnload(){
		/*System.out.println("blockDestroyed!");
		ServerProxy.deleteTileentityAnimated(xCoord, yCoord, zCoord);
		Main.network.sendToAll(new DeleteTileentityAnimated(xCoord,yCoord,zCoord));*/
	}
	
}
