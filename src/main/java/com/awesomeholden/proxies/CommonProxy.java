package com.awesomeholden.proxies;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.ServerLoop;
import com.awesomeholden.Tileentities.CreateTileentities;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.items.MItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommonProxy {
		
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		CreateTileentities MTileentities = new CreateTileentities();
		CreateBlocks MBlocks = new CreateBlocks();
		MItems Mitems = new MItems();

    }

	@EventHandler
    public void init(FMLInitializationEvent e) {
		
		FMLCommonHandler.instance().bus().register(ClientLoop.instance);
    	MinecraftForge.EVENT_BUS.register(ClientLoop.instance);
    	
    	FMLCommonHandler.instance().bus().register(ServerLoop.instance);
    	MinecraftForge.EVENT_BUS.register(ServerLoop.instance);
		
    }

	@EventHandler
    public void postInit(FMLPostInitializationEvent e) {

    }
}
