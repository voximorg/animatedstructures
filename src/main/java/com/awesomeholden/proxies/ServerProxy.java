package com.awesomeholden.proxies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import scala.actors.threadpool.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.ServerLoop;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.packets.RefreshAnimation;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy{
	
	public static List<AnimationControllerServer> controllerCoordsAssigmentCache = new ArrayList<AnimationControllerServer>();
	
	public static List<TileentityAnimatedServer> controllerAssignmentCache = new ArrayList<TileentityAnimatedServer>();
	
	public static int[] outlineCache = new int[0];
	
	public static boolean blahFB = true;
	
	public static List<AnimationControllerServer> AnimationControllers = new ArrayList<AnimationControllerServer>();
	
	public static EntityPlayerMP getPlayer(String name){
		WorldServer[] worlds = MinecraftServer.getServer().worldServers;
		for(int i=0;i<worlds.length;i++){
			WorldServer world = worlds[i];
			for(int i2=0;i2<world.playerEntities.size();i2++){
				EntityPlayerMP player = ((EntityPlayerMP)world.playerEntities.get(i));
				if(player.getDisplayName().equals(name)){
					return player;
				}
			}
		}
		return null;
	}
	
	public static boolean deleteTileentityAnimated(int x,int y,int z){
		for(int i=0;i<AnimationControllers.size();i++){
			//List<TileentityAnimatedServer> le = AnimationControllers.get(controller).theControlled;
			AnimationControllerServer c = AnimationControllers.get(i);
			
			c.removeTileentity(x,y,z);
			/*for(int controlled=0;controlled<le.size();controlled++){
				TileentityAnimatedServer current = le.get(controlled);
				if(current.xCoord == x && current.yCoord == y && current.zCoord == z){
					System.out.println("BLOCK IS BEING REMOVED!");
					System.out.println("CONTROLLERS SIZE: "+ServerProxy.AnimationControllers.size());
					System.out.println("LE: "+le);
					System.out.println("LE>SIZE: "+le.size());
					AnimationControllers.get(controller).theControlled.remove(current);
					System.out.println("LE>SIZE: "+le.size());
					System.out.println("current: "+current);
					System.out.println("LE: "+le);
					return true;
				}
			}*/
		}
		return false;
	}
	
	public static void deleteAnimationController(int[] coords){
		AnimationControllers.remove(getAnimationController(coords));
	}
	
	public static TileentityAnimatedServer getTileentityAnimated(int x,int y,int z){
		for(int controller=0;controller<AnimationControllers.size();controller++){
			List<TileentityAnimatedServer> le = AnimationControllers.get(controller).theControlled;
			for(int controlled=0;controlled<le.size();controlled++){
				TileentityAnimatedServer current = le.get(controlled);
				if(current.xCoord == x && current.yCoord == y && current.zCoord == z){
					return current;
				}
			}
		}
		return null;
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
	
	public static AnimationControllerServer getAnimationController(int[] coords){
		for(int ph=0;ph<AnimationControllers.size();ph++){
			AnimationControllerServer c = AnimationControllers.get(ph);
			if(Main.compareArrays(coords, c.coords)){
				return c;
			}
		}
		return null;
	}
	
	public static TileentityAnimationEditorServer getEditor(AnimationControllerServer in){
		WorldServer w = MinecraftServer.getServer().worldServers[in.dimension];
		for(int i=0;i<w.loadedTileEntityList.size();i++){
			if(!(w.loadedTileEntityList.get(i) instanceof TileentityAnimationEditorServer))
				continue;
			
			TileentityAnimationEditorServer c = (TileentityAnimationEditorServer) w.loadedTileEntityList.get(i);
			if(c.controller == in){
				return c;
			}
		}
		
		return null;
	}
	
	public static int getTileentityAnimatedId(TileentityAnimatedServer c){
		int max = 20;
		return c.xCoord*max*max+c.yCoord*max+c.zCoord;
	}
	
	public static AnimationControllerServer getAnimationControllerFromEditor(int x,int y,int z){
		
		WorldServer[] ws = MinecraftServer.getServer().worldServers;
		for(int i=0;i<ws.length;i++){
			WorldServer w = ws[i];
			
			if(w.getTileEntity(x, y, z) != null){
				return ((TileentityAnimationEditorServer) w.getTileEntity(x, y, z)).controller;
			}
			/*for(int i2=0;i2<w.loadedTileEntityList.size();i2++){
				//System.out.println("ONE TILEENTITY!");
				if(w.loadedTileEntityList.get(i) instanceof TileentityAnimationEditorServer){
					System.out.println("RIGHT HERE INSTANCEOF TILEENTITYANIMATIONEDITORSERVER");
					TileentityAnimationEditorServer c = (TileentityAnimationEditorServer) w.loadedTileEntityList.get(i);
					if(x==c.xCoord && y==c.yCoord && z==c.zCoord){
						return c.controller;
					}
				}
			}*/
		}
		
		return null;
	}
	
	public static AnimationControllerServer ignore = null;
	public static AnimationControllerServer coordsInController(int x,int y,int z){
		for(int i=0;i<AnimationControllers.size();i++){
			int[] c = AnimationControllers.get(i).coords;
			if(c[0]<=x && c[1]<=y && c[2]<=z && c[3]>=x && c[4]>=y && c[5]>=z){
				return AnimationControllers.get(i);
			}
		}
		
		return null;
	}
	
	public static TileentityAnimationEditorServer getEditorFromCoords(int[] coords,int dimension){
		List<TileEntity> ls = MinecraftServer.getServer().worldServers[dimension].loadedTileEntityList;
		for(int i=0;i<ls.size();i++){
			if(ls.get(i) instanceof TileentityAnimationEditorServer){
				TileentityAnimationEditorServer c =(TileentityAnimationEditorServer) ls.get(i);
				if(c.controller.coords != null){
					if(Main.compareArrays(c.controller.coords, coords)){
						return c;
					}
				}
			}
		}
		return null;
	}
	
	public static void addTileentityToController(TileentityAnimatedServer t, AnimationControllerServer c){
		
		if(Main.indexOf(c.theControlled,t) > -1) return;
				
		int id = ServerProxy.getTileentityAnimatedId(t);
		for(int i=0;i<c.theControlled.size();i++){
			if(id<ServerProxy.getTileentityAnimatedId(c.theControlled.get(i))){
				c.theControlled.add(i,t);
				return;
			}
		}
		
		c.theControlled.add(t);
		
	}
	
	public static void updateControllerFrames(TileentityAnimatedServer t, AnimationControllerServer c){
		int index = -1;
		
		int id = ServerProxy.getTileentityAnimatedId(t);
		for(int i=0;i<c.theControlled.size();i++){
			if(id<ServerProxy.getTileentityAnimatedId(c.theControlled.get(i))){
				index = i;
				break;
			}
		}
		
		if(index == -1){
			c.theControlled.add(t);
			return;
		}
		
		for(int i=0;i<c.framesInfo.size();i++){
			for(Entry<Integer, List<Integer>> e : c.framesInfo.get(i).entrySet()){
				for(int i2=0;i2<e.getValue().size();i2++){
					if(e.getValue().get(i2) == index){
						return;
					}
				}
			}
		}
		
		for(int i=0;i<c.framesInfo.size();i++){
			for(Entry<Integer, List<Integer>> e : c.framesInfo.get(i).entrySet()){
				for(int i2=0;i2<e.getValue().size();i2++){
					if(e.getValue().get(i2) >= index){
						e.getValue().set(i2, e.getValue().get(i2)+1);
					}
				}
			}
		}
		
		Main.network.sendToAllAround(new RefreshAnimation(c.coords), c.genTargetPoint());
	}
	
	//public static TileentityAnimationEditor getEditorByCoords
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
       // MinecraftForge.EVENT_BUS.register(new BlockE());
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }
    
}
