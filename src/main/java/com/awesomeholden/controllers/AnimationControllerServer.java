package com.awesomeholden.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.glu.GLU;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.packets.SendTileentityAnimatedTextureUpdate;
import com.awesomeholden.packets.SetCoordsOnClient;
import com.awesomeholden.packets.UpdateControllerClientTextures;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;

public class AnimationControllerServer { //Pointers to objects inside of ServerProxy.AnimationControllers
	
	public int TileentitiesToBeAdded = 4;
	
	public int dimension = 0;
		
	public List<Integer> frameIntervals = new ArrayList<Integer>();
	
	public int[] coords;
	public int tick = 0;
	public List<TileentityAnimatedServer> theControlled = new ArrayList<TileentityAnimatedServer>();
	
	public List<HashMap<Integer,List<Integer>>> framesInfo = new ArrayList<HashMap<Integer,List<Integer>>>();
	
	public AnimationControllerServer(){
		coords = new int[]{0,0,0,0,0,0};
		ServerProxy.controllerCoordsAssigmentCache.add(this);
		
		frameIntervals.add(10);
		framesInfo.add(new HashMap<Integer,List<Integer>>());
	}
	
	private int frameIndex = 0;
	
	private boolean lock = true;
	
	public void onUpdate(){ //added to ServerLoop
				
		if(tick == frameIntervals.get(frameIndex)){
			
			if(TileentitiesToBeAdded>0){
				TileentitiesToBeAdded--;
								
				WorldServer world = MinecraftServer.getServer().worldServers[dimension];
				for(int ph=0;ph<world.loadedTileEntityList.size();ph++){
					TileentityAnimatedServer c;
					if(world.loadedTileEntityList.get(ph) instanceof TileentityAnimatedServer){
						c = (TileentityAnimatedServer) world.loadedTileEntityList.get(ph);
						if(coords[0]<=c.xCoord && coords[1]<=c.yCoord && coords[2]<=c.zCoord && coords[3]>=c.xCoord && coords[4]>=c.yCoord && coords[5]>=c.zCoord)
							ServerProxy.addTileentityToController(c, this);
					}else{
						continue;
					}
				}
			}
																					
			//System.out.println("CONTROLLERSERVER COORDS: "+Arrays.toString(coords)+" THECONTROLLED SIZE: "+theControlled.size());
			/*HashMap<Integer,List<Integer>> stuff = new HashMap<Integer,List<Integer>>();
			
			for(int i=0;i<theControlled.size();i++){
				TileentityAnimatedServer c = theControlled.get(i);
				
				try{
					int tex = c.frames.get(frameIndex);
				
				List<Integer> ls = stuff.get(tex);
				
				if(ls == null){
					ls = new ArrayList<Integer>();
					stuff.put(tex, ls);
				}
				
				if(frameIndex == 0){
					if(tex != c.frames.get(c.frames.size()-1))
						ls.add(i);
				}else{
					if(tex != c.frames.get(frameIndex-1))
						ls.add(i);
				}
				}catch(IndexOutOfBoundsException e){
					c.frames.clear();
					for(int i2=0;i2<frameIntervals.size();i2++)
						c.frames.add(0);
				}
			}*/
			
			if(coords != null)
				Main.network.sendToAllAround(new UpdateControllerClientTextures(coords,framesInfo.get(frameIndex)),new TargetPoint(dimension,(coords[0]+coords[3])/2,(coords[1]+coords[4])/2,(coords[2]+coords[5])/2,80));
				
			/*theControlled.clear();
			for(int i=0;i<MinecraftServer.getServer().worldServers[dimension].loadedTileEntityList.size();i++){
				if(!(MinecraftServer.getServer().worldServers[dimension].loadedTileEntityList.get(i) instanceof TileentityAnimatedServer))
					continue;
				
				TileentityAnimatedServer c = (TileentityAnimatedServer) MinecraftServer.getServer().worldServers[dimension].loadedTileEntityList.get(i);
				
				if(coords[0]<=c.xCoord && coords[3]>=c.xCoord && coords[1]<=c.yCoord && coords[4]>=c.yCoord && coords[2]<=c.zCoord && coords[5]>=c.zCoord)
					theControlled.add(c);
			}*/
			
			
			
			List<Integer> ls = new ArrayList<Integer>();
			List<TileentityAnimatedServer> ls2 = new ArrayList<TileentityAnimatedServer>();
			for(int i=0;i<theControlled.size();i++){
				int id = ServerProxy.getTileentityAnimatedId(theControlled.get(i));
				
				if(ls.indexOf(id)<0){
					ls.add(id);
					ls2.add(theControlled.get(i));
				}
			}
			theControlled = ls2;
		
			if(frameIndex+1 == frameIntervals.size()){
				frameIndex = 0;
			}else{
				frameIndex++;
			}
			
			tick = -1;
			
		}
		
		tick++;
	}
	
	public void onCoordsSet(){
		/*if(coords != null)
			Main.network.sendToAll(new SetCoordsOnClient());*/
		
		int i2 = ServerProxy.AnimationControllers.indexOf(this);
		for(int i=0;i<ServerProxy.AnimationControllers.size();i++){
			AnimationControllerServer c = ServerProxy.AnimationControllers.get(i);
			if(i != i2 && Main.compareArrays(coords, c.coords))
				ServerProxy.AnimationControllers.remove(i);
		}
		
		boolean doo = true;
		for(int ph=0;ph<ServerProxy.controllerAssignmentCache.size();ph++){
			TileentityAnimatedServer c = ServerProxy.controllerAssignmentCache.get(ph);
			int id = ServerProxy.getTileentityAnimatedId(c);
			if(coords[0]<=c.xCoord && coords[1]<=c.yCoord && coords[2]<=c.zCoord && coords[3]>=c.xCoord && coords[4]>=c.yCoord && coords[5]>=c.zCoord){
				for(int ph2=0;ph2<theControlled.size();ph2++){
					if(id<ServerProxy.getTileentityAnimatedId(theControlled.get(ph2))){
						theControlled.add(ph2,c);
						doo = false;
						break;
					}
				}
				if(doo){
					theControlled.add(c);
				}
				doo = true;
			}
		}
		
		for(int i=0;i<theControlled.size();i++){
			//theControlled.set(i,theControlled.get(theControlled.size()-(i+1)));
			System.out.println("CHECK THIS OUT: "+ServerProxy.getTileentityAnimatedId(theControlled.get(i)));
		}
	}
	
	public void removeTileentity(int x,int y,int z){
		List<TileentityAnimatedServer> n = new ArrayList<TileentityAnimatedServer>();
		for(int i=0;i<theControlled.size();i++){
			TileentityAnimatedServer c = theControlled.get(i);
			
			if(c.xCoord == x && c.yCoord == y && c.zCoord == z){
			}else{
				n.add(c);
			}
		}
		
		theControlled = n;
	}

}
