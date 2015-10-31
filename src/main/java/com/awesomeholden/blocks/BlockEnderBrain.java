package com.awesomeholden.blocks;

import com.awesomeholden.Tabs;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockEnderBrain extends Block{

	protected BlockEnderBrain() {
		super(Material.sponge);
		setBlockName("Ender Brain Block");
		setBlockTextureName("animatedstructures:ender_brain");
		setCreativeTab(Tabs.Tab);
		setHardness(0.2f);
	}

}
