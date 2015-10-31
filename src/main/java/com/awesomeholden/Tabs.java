package com.awesomeholden;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Tabs {
	
	private static class ClassTab extends CreativeTabs{
	
		public ClassTab() {
			super("AnimatedStructures/Items");
		}

		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return Items.bone;
		}
	}

	public static CreativeTabs Tab = new ClassTab();

}
