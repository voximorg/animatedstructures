package com.awesomeholden.blocks;

import com.awesomeholden.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockControllerInsides extends Block{
	
	public BlockControllerInsides(){
		super(Material.iron);
		this.setBlockName("Controller Insides");
		this.setBlockTextureName("animatedstructures:controller_insides");
		this.setHardness(0.5f);
		this.setCreativeTab(Tabs.Tab);
	}

}
