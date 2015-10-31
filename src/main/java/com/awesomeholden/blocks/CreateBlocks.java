package com.awesomeholden.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.awesomeholden.blocks.BlockAnimated;

import cpw.mods.fml.common.registry.GameRegistry;

public class CreateBlocks {
	public static Block Animated = new BlockAnimated();
	public static Block AnimationEditor = new BlockAnimationEditor();
	
	//crafting
	public static Block ledBlock;
	public static Block animatedInsides;
	public static Block enderBrain;
	public static Block bone;
	public static Block controllerInsides;

	public CreateBlocks(){
		ledBlock = new BlockLED();
		animatedInsides = new BlockAnimatedInsides();
		enderBrain = new BlockEnderBrain();
		bone = new BlockBone();
		controllerInsides = new BlockControllerInsides();
		
		GameRegistry.registerBlock(Animated,"Animated Block");
		GameRegistry.registerBlock(AnimationEditor,"Animation Editor");
		GameRegistry.registerBlock(ledBlock,"LED Block");
		GameRegistry.registerBlock(animatedInsides, "Animated Insides");
		GameRegistry.registerBlock(enderBrain, "Ender Brain Block");
		GameRegistry.registerBlock(bone, "Bone Block");
		GameRegistry.registerBlock(controllerInsides, "Controller Insides");
	};

}
