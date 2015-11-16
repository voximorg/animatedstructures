package com.awesomeholden;

import com.awesomeholden.blocks.CreateBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Tabs {

	public static CreativeTabs Tab = new CreativeTabs("Animated Structures") {
		
		@Override
		public Item getTabIconItem(){
			return Item.getItemFromBlock(CreateBlocks.Animated);
		}
		
	};
		

}
