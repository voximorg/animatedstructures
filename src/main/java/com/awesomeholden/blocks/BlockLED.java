package com.awesomeholden.blocks;

import com.awesomeholden.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockLED extends Block{
	
	public BlockLED(){
		super(Material.iron);
		setBlockName("LED Block");
		setBlockTextureName("animatedstructures:LED");
		setCreativeTab(Tabs.Tab);
		setHardness(0.5f);
	}

}
