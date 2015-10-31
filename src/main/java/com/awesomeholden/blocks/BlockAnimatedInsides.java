package com.awesomeholden.blocks;

import com.awesomeholden.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockAnimatedInsides extends Block{
	
	public BlockAnimatedInsides(){
		super(Material.iron);
		this.setBlockName("Animated Insides");
		this.setBlockTextureName("animatedstructures:animated_insides");
		this.setHardness(0.5f);
		this.setCreativeTab(Tabs.Tab);
	}

}
