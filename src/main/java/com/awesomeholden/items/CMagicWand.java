package com.awesomeholden.items;

import com.awesomeholden.Tabs;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.CommonProxy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CMagicWand extends Item{
	
	public boolean whichCoords = true;
	
	public CMagicWand(){
		setCreativeTab(Tabs.Tab);
		setUnlocalizedName("Magic Wand");
	}
	
	@Override
	public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int j0, float j1, float j2, float j3){
		if(world.isRemote){
		if(whichCoords/*ClientProxy.outlineCacheMeta[0] == true*/){
			ClientProxy.outlineCache[3] = x;
			ClientProxy.outlineCache[4] = y;
			ClientProxy.outlineCache[5] = z;
			//System.out.println("second stage :"+ClientProxy.outlineCache);
			ClientProxy.outlineCacheMeta[1] = true;
			whichCoords = false;
		}else if(!whichCoords/*ClientProxy.outlineCacheMeta[1] == true || (ClientProxy.outlineCacheMeta[0] == false && ClientProxy.outlineCacheMeta[1] == false)*/){
			ClientProxy.outlineCache[0] = x;
			ClientProxy.outlineCache[1] = y;
			ClientProxy.outlineCache[2] = z;
			//System.out.println("first stage :"+ClientProxy.outlineCache);
			ClientProxy.outlineCacheMeta[0] = true;
			whichCoords = true;
		}
		}

		return bFull3D;
		
	}

}
