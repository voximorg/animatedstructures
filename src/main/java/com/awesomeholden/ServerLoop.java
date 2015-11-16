package com.awesomeholden;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.data.TextureData;
import com.awesomeholden.items.MItems;
import com.awesomeholden.packets.GivePlayerTextures;
import com.awesomeholden.packets.ShouldDestroyController;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;


public class ServerLoop {
		
	public static ServerLoop instance = new ServerLoop();
	
	public static TextureData textureData;
		
	@SubscribeEvent
	public void unload(WorldEvent.Unload e){
		if(textureData != null)
			textureData.markDirty();
	}
	
	@SubscribeEvent
 	public void load(WorldEvent.Load event){
		
		/*List<ResourceLocation> cache0 = new ArrayList(TileentityAnimatedClient.textures);
		List<ResourceLocation> cache1 = new ArrayList(TileentityAnimatedServer.textures);
				
		TileentityAnimatedClient.textures.clear();
		TileentityAnimatedServer.textures.clear();*/
		
		
		//TextureData.theStuff = null;
		try{
		WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[0];
		
		TextureData data = ((TextureData) world.perWorldStorage.loadData(TextureData.class,"aniwhat"));
		
		if(data == null){
					
			//world.perWorldStorage.setData("aniwhat",new TextureData("aniwhat"));
			TileentityAnimatedServer.textures.add(new ResourceLocation("animatedstructures:textures/transparent.png"));
			TileentityAnimatedServer.textures.add(new ResourceLocation("minecraft:textures/blocks/beacon.png"));
			TileentityAnimatedServer.textures.add(new ResourceLocation("minecraft:textures/blocks/bedrock.png"));
			TileentityAnimatedServer.textures.add(new ResourceLocation("minecraft:textures/blocks/bookshelf.png"));
			
			textureData = new TextureData("aniwhat");
			world.perWorldStorage.setData("aniwhat",textureData);
		}else{
			textureData = data;
			
		}
		
		/*if(TileentityAnimatedClient.textures.size()==0) //incase of change of dimention
			TileentityAnimatedClient.textures.addAll(cache0);
		
		if(TileentityAnimatedServer.textures.size()==0)
			TileentityAnimatedServer.textures.addAll(cache1);*/
		
		}catch(NullPointerException e){
			
			System.out.println("NULL POINTER EXCEPTION :"+FMLCommonHandler.instance().getEffectiveSide().isClient());
			//client
		}
		/*try{
			Minecraft mc = Minecraft.getMinecraft();
			System.out.println("WORLD SAVE FOLDER: "+mc.mcDataDir.getPath()); 
		}catch(NoClassDefFoundError error){
			/*System.out.println("WORLD SAVE FOLDER: "+"./"+place);
			Minecraft.getMinecraft().theWorld.getW*/
		//}
		
	}
	
	/*@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onClientTick(RenderWorldLastEvent event) {
		for(int ph=0;ph<ServerProxy.AnimationControllers.size();ph++){
			System.out.println("looped");
			ServerProxy.AnimationControllers.get(ph).onUpdate();
		}
	}*/
		
	@SubscribeEvent
	public void joinEvent(PlayerEvent.PlayerLoggedInEvent e){
		//if((!didLoad2) && FMLCommonHandler.instance().getEffectiveSide().isServer()){
		
			Main.network.sendTo(new GivePlayerTextures(), (EntityPlayerMP)e.player);
		//}
		//}
		/*if(e.entity instanceof EntityPlayerMP){
			
			if(a>0) return;
			a++;
			System.out.println("PLAYER JOINED");
			if(e.entity == null){
				System.out.println("PLAYER IS NULL");
			}else{
				//System.out.println("PLAYER: "+e.entity);
				try{*/
					//Main.network.sendTo(new GivePlayerTextures(),(EntityPlayerMP)e.player);
				/*}catch(NullPointerException e1){
					System.out.println("OH NO GIVEPLAYERTEXTURES DIDNT WORK!");
				}
	
			}
		}*/
	}
	
	public static Map<EntityItem,Double> trackedItems = new HashMap<EntityItem,Double>();
	public List<EntityItem> removeThis = new ArrayList<EntityItem>();
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if(event.phase == Phase.END)
			return;
						
		for(int ph=0;ph<ServerProxy.AnimationControllers.size();ph++)
			ServerProxy.AnimationControllers.get(ph).onUpdate();
		
		
		removeThis.clear();
		
		for(Entry e : trackedItems.entrySet()){
			EntityItem b = (EntityItem) e.getKey();
			
			if(b.isDead){
				removeThis.add(b);
			}else{
				if(b.motionY == 0){
					if((Double)e.getValue()<-0.6){
						WorldServer world = MinecraftServer.getServer().worldServers[b.dimension];
						ItemStack stack = b.getEntityItem();
						for(int i=0;i<4*stack.stackSize;i++){
							Item item = null;
							switch(stack.getItemDamage()){
							case 11:
								item = MItems.blueShard;
								break;
							case 13:
								item = MItems.greenShard;
								break;
							case 14:
								item = MItems.redShard;
								break;
							}
							EntityItem p = new EntityItem(world,b.posX,b.posY,b.posZ,new ItemStack(item));
							p.motionX*=4;
							p.motionY*=4;
							p.motionZ*=4;
							world.spawnEntityInWorld(p);
						}
						world.removeEntity(b);
					}
					removeThis.add(b);
				}
				
				e.setValue(b.motionY);
			}
		}
		
		for(int i=0;i<removeThis.size();i++){
			trackedItems.remove(removeThis.get(i));
		}
		
		/*for(int ph=0;ph<MinecraftServer.getServer().worldServers.length;ph++) {
			List<EntityPlayerMP> list = MinecraftServer.getServer().worldServers[ph].playerEntities;
			for(int ph2=0;ph2<list.size();ph2++){
				ItemStack chest = list.get(ph2).inventory.armorInventory[2];
				
				if(chest != null && chest.getItem() == Items.chainmail_chestplate && Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
					EntityPlayer p = Minecraft.getMinecraft().thePlayer;
					p.setPositionAndUpdate(p.posX, p.posY+0.0001, p.posZ);
				}
			}
		}*/
		
	}
	
	@SubscribeEvent
	public void killEntity(LivingDeathEvent e){
		World w = e.entity.worldObj;
		if(w.isRemote)
			return;
		if(e.entity instanceof EntityEnderman){
			EntityItem i = new EntityItem(w,e.entity.posX,e.entity.posY,e.entity.posZ,new ItemStack(MItems.enderBrain));
			w.spawnEntityInWorld(i);
		}
	}
	
	@SubscribeEvent
	public void entityCreation(EntityEvent.EnteringChunk e){
		if(e.entity instanceof EntityItem){
			ItemStack stack = ((EntityItem)e.entity).getEntityItem();
			Block b = Block.getBlockFromItem(stack.getItem());
			if(b == Blocks.stained_glass){
				int meta = stack.getItemDamage();
								
				if(meta == 13 || meta == 14 || meta == 11){
				      ServerLoop.trackedItems.put((EntityItem) e.entity,0D);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void blockEvent(BlockEvent.PlaceEvent e){
		if(e.block == CreateBlocks.Animated){			
			TileentityAnimatedServer t = (TileentityAnimatedServer) e.world.getTileEntity(e.x, e.y, e.z);
			
			AnimationControllerServer c = ServerProxy.coordsInController(e.x, e.y, e.z);
						
			if(t == null)
				return;
			
			int index = -1;
			
			ServerProxy.addTileentityToController(t, c);
			
			
			int id = ServerProxy.getTileentityAnimatedId(t);
			for(int i=0;i<c.theControlled.size();i++){
				if(id==ServerProxy.getTileentityAnimatedId(c.theControlled.get(i))){
					index = i;
					
					break;
				}
			}
						
			for(int i=0;i<c.framesInfo.size();i++){
				for(Entry<Integer, List<Integer>> e2 : c.framesInfo.get(i).entrySet()){
					for(int i2=0;i2<e2.getValue().size();i2++){
						if(e2.getValue().get(i2) >= index){
							e2.getValue().set(i2, e2.getValue().get(i2)+1);
						}
					}
				}
			}
		}
	}
	
	/*@SubscribeEvent
	public void blockEvent(BlockEvent.BreakEvent event){
		if(event.block == CreateBlocks.AnimationEditor){
			System.out.println("BLOCK IS ANIMATION EDITOR");
			AnimationControllerServer c = ((TileentityAnimationEditorServer) event.world.getTileEntity(event.x, event.y, event.z)).controller;
			Main.network.sendToAllAround(new ShouldDestroyController(c.coords),new TargetPoint(c.dimension,(c.coords[0]+c.coords[3])/2,(c.coords[1]+c.coords[4])/2,(c.coords[2]+c.coords[5])/2,80));
			event.setCanceled(true);
		}
	}*/
	

}
