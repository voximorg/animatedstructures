package com.awesomeholden.proxies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.awesomeholden.KeyBindings;
import com.awesomeholden.KeyInputHandler;
import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.ServerLoop;
import com.awesomeholden.TESRs.CreateTESRs;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.guis.animationeditorguiwidges.TextBox;
import com.awesomeholden.itemrenderers.IR2MagicWand;
import com.awesomeholden.itemrenderers.IReditor;
import com.awesomeholden.itemrenderers.ItemRendererAnimation;
import com.awesomeholden.items.MItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{
			
	public static List<AnimationControllerClient> AnimationControllers = new ArrayList<AnimationControllerClient>();
	
	public static List<TileentityAnimatedClient> controllerAssignmentCache = new ArrayList<TileentityAnimatedClient>();
	
	public static List<AnimationControllerClient> controllerCoordsAssignmentCache = new ArrayList<AnimationControllerClient>();
	
	public static List<TextBox> textBoxs = new ArrayList<TextBox>();
	
	public static int[] outlineCache = new int[]{0,0,0,0,0,0}; //Holds the outline player is filling
	public static boolean[] outlineCacheMeta = new boolean[]{true,true};
	
	public static float[] mousePos = new float[2];
	public static float canvasWidth;
	
	public static boolean rightPressed = false;
	public static boolean leftPressed = false;
		
	public static AnimationControllerClient getAnimationController(int[] coords){
		//System.out.println("HERE IS THE ANImATIONCONTROLLERS>SIZE: "+AnimationControllers.size());
		for(int ph=0;ph<AnimationControllers.size();ph++){
			//System.out.println("HERE WHAT: "+Arrays.toString(AnimationControllers.get(ph).coords)+" AND "+Arrays.toString(coords));
			if(Main.compareArrays(AnimationControllers.get(ph).coords,coords)){
				return AnimationControllers.get(ph);
			}
		}
		return null;
	}
	
	public static boolean deleteTileentityAnimated(int x,int y,int z){
		for(int controller=0;controller<AnimationControllers.size();controller++){
			List<TileentityAnimatedClient> le = AnimationControllers.get(controller).theControlled;
			for(int controlled=0;controlled<le.size();controlled++){
				TileentityAnimatedClient current = le.get(controlled);
				if(current.xCoord == x && current.yCoord == y && current.zCoord == z){
					le.remove(controlled);
					return true;
				}
			}
		}
		return false;
	}
	
	public static List<Integer> tryingToCallRealConstructor = new ArrayList<Integer>();
	public static void callTileentityAnimatedConstructor(int x,int y,int z){
		TileentityAnimatedClient current = (TileentityAnimatedClient) Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
		if(current == null){
			tryingToCallRealConstructor.add(x);
			tryingToCallRealConstructor.add(y);
			tryingToCallRealConstructor.add(z);
			return;
		}
		if(current.xCoord == x && current.yCoord == y && current.zCoord == z){
			//System.out.println("Dilectable!");
			current.realConstructor();
		}
	}
	
	public static int getTileentityAnimatedSize(){
		int count = 0;
		for(int ph=0;ph<AnimationControllers.size();ph++){
			for(int ph2=0;ph2<AnimationControllers.get(ph).theControlled.size();ph2++){
				count++;
			}
		}
		return count;
	}
	
	public static int getTileentityAnimatedId(TileentityAnimatedClient c){
		int max = AnimationEditorGui.max;
		return c.xCoord*max*max+c.yCoord*max+c.zCoord;
	}
	
	public static void deleteAnimationController(int[] coords){
		AnimationControllers.remove(getAnimationController(coords));
	}
	
	public static TileentityAnimatedClient getTileentityAnimated(int x,int y,int z){
		for(int controller=0;controller<AnimationControllers.size();controller++){
			List<TileentityAnimatedClient> le = AnimationControllers.get(controller).theControlled;
			for(int controlled=0;controlled<le.size();controlled++){
				TileentityAnimatedClient current = le.get(controlled);
				if(current.xCoord == x && current.yCoord == y && current.zCoord == z){
					return current;
				}
			}
		}
		return null;
	}
	
	public static TileentityAnimationEditorClient getEditorFromCoords(int[] coords){
		List<TileEntity> ls = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
		for(int i=0;i<ls.size();i++){
			if(ls.get(i) instanceof TileentityAnimationEditorClient){
				TileentityAnimationEditorClient c =(TileentityAnimationEditorClient) ls.get(i);
				if(c.controller.coords != null){
					if(Main.compareArrays(c.controller.coords, coords)){
						return c;
					}
				}
			}
		}
		return null;
	}
	
	public static AnimationControllerClient ignore = null;

	public static AnimationEditorGui gui;
	
	public static AnimationControllerClient coordsInController(int x,int y,int z){
		for(int i=0;i<AnimationControllers.size();i++){
			int[] c = AnimationControllers.get(i).coords;
			if(c[0]<=x && c[1]<=y && c[2]<=z && c[3]>=x && c[4]>=y && c[5]>=z && AnimationControllers.get(i) != ignore)
				return AnimationControllers.get(i);
		}
		
		return null;
	}
	
	public static void addTileentityToController(TileentityAnimatedClient t, AnimationControllerClient c){
		
		if(Main.indexOf(c.theControlled,t) > -1) return;
		
		int id = ClientProxy.getTileentityAnimatedId(t);
		for(int i=0;i<c.theControlled.size();i++){
			if(id<ClientProxy.getTileentityAnimatedId(c.theControlled.get(i))){
				c.theControlled.add(i,t);
				return;
			}
		}
		
		c.theControlled.add(t);
	}
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        CreateTESRs MTESRs = new CreateTESRs();
        FMLCommonHandler.instance().bus().register(new KeyInputHandler());
        KeyBindings.create();
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CreateBlocks.Animated), ItemRendererAnimation.instance);
        MinecraftForgeClient.registerItemRenderer(MItems.magicWand, IR2MagicWand.instance);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CreateBlocks.AnimationEditor), new IReditor());
        
        /*FMLCommonHandler.instance().bus().register(new ServerLoop());
        MinecraftForge.EVENT_BUS.register(new ServerLoop());*/
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
        /*animatedCache = new ThreadAnimatedCache();
        new Thread(animatedCache).start();*/
    }
}

