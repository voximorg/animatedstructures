package com.awesomeholden.items;

import com.awesomeholden.Tabs;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;

public class MItems {
	
	public static Item magicWand;
	public static Item redShard;
	public static Item greenShard;
	public static Item blueShard;
	public static Item enderBrain;
	
	//crafting only
	public static Item rawLed;
	public static Item led;
	
	public MItems(){
		magicWand = new CMagicWand();
		redShard = new Item().setUnlocalizedName("Red Glass Shard").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:glass_shard_red");
		greenShard = new Item().setUnlocalizedName("Green Glass Shard").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:glass_shard_green");
		blueShard = new Item().setUnlocalizedName("Blue Glass Shard").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:glass_shard_blue");
		rawLed = new Item().setUnlocalizedName("Raw LED").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:raw_LED");
		led = new Item().setUnlocalizedName("LED").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:LED");
		enderBrain = new Item().setUnlocalizedName("Ender Brain").setCreativeTab(Tabs.Tab).setTextureName("animatedstructures:ender_brain");
		
		GameRegistry.registerItem(magicWand, "Magic Wand"); 
		GameRegistry.registerItem(redShard, "Red Glass Shard");
		GameRegistry.registerItem(greenShard, "Green Glass Shard");
		GameRegistry.registerItem(blueShard, "Blue Glass Shard");
		GameRegistry.registerItem(rawLed, "Raw LED");
		GameRegistry.registerItem(led, "LED");
		GameRegistry.registerItem(enderBrain, "Ender Brain");
	}

}
