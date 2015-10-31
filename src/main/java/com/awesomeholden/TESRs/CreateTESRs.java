package com.awesomeholden.TESRs;

import com.awesomeholden.TESRs.TESRAnimated;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;

import cpw.mods.fml.client.registry.ClientRegistry;

public class CreateTESRs {
	public CreateTESRs(){
		ClientRegistry.bindTileEntitySpecialRenderer(TileentityAnimatedClient.class, new TESRAnimated());
		ClientRegistry.bindTileEntitySpecialRenderer(TileentityAnimationEditorClient.class, new TESReditor());
	}

}
