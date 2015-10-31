package com.awesomeholden.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import com.awesomeholden.Tabs;

public class BlockBone extends Block{
	
	public BlockBone(){
		super(Material.iron);
		this.setBlockName("Bone Block");
		this.setBlockTextureName("animatedstructures:bone");
		this.setHardness(0.5f);
		this.setCreativeTab(Tabs.Tab);
	}

}
