package com.awesomeholden.items;

import com.awesomeholden.Tabs;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class StructureStateStorageItem extends Item{
	
	public StructureStateStorageItem(){
		setCreativeTab(Tabs.Tab);
		setUnlocalizedName("StructureStateStorageItem");
		
		GameRegistry.registerItem(this, "StructureStateStorageItem");
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
	    itemStack.stackTagCompound = new NBTTagCompound();
	    itemStack.stackTagCompound.setBoolean("state", false);
	}

}
