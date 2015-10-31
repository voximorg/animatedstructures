package com.awesomeholden.Tileentities;

import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import cpw.mods.fml.common.registry.GameRegistry;

public class CreateTileentities {
	public CreateTileentities(){
		GameRegistry.registerTileEntity(TileentityAnimatedServer.class,"AnimatedServer");
		GameRegistry.registerTileEntity(TileentityAnimationEditorClient.class, "AnimationEditorClient");
		GameRegistry.registerTileEntity(TileentityAnimationEditorServer.class, "AnimationEditorServer");
		GameRegistry.registerTileEntity(TileentityAnimatedClient.class, "AnimatedClient");
		GameRegistry.registerTileEntity(TestTileentity.class, "TEST");
	}
}
