package com.awesomeholden.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import scala.actors.threadpool.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

public class AnimationControllerClient {
		
	public int[] coords;
	
	public List<TileentityAnimatedClient> theControlled = new ArrayList<TileentityAnimatedClient>();
				
	public int TileentitiesToBeAdded = 10;
		
	public AnimationControllerClient(int[] coords){
		ClientProxy.AnimationControllers.add(this);
		this.coords = coords;
		boolean doo = true;
		for(int ph=0;ph<ClientProxy.controllerAssignmentCache.size();ph++){
			TileentityAnimatedClient c = ClientProxy.controllerAssignmentCache.get(ph);
			int id = ClientProxy.getTileentityAnimatedId(c);
			if(coords[0]<=c.xCoord && coords[1]<=c.yCoord && coords[2]<=c.zCoord && coords[3]>=c.xCoord && coords[4]>=c.yCoord && coords[5]>=c.zCoord){
				for(int ph2=0;ph2<theControlled.size();ph2++){
					if(id<ClientProxy.getTileentityAnimatedId(theControlled.get(ph2))){
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
	}
	
	public AnimationControllerClient(){}
	
	public boolean changing = true;
	
	public void updateTextures(HashMap<Integer,List<Integer>> map){
										
		for(Entry<Integer,List<Integer>> e : map.entrySet()){
			for(int i=0;i<e.getValue().size();i++){
				try{
					theControlled.get(e.getValue().get(i)).texture = TileentityAnimatedClient.textures.get(e.getKey());
				}catch(IndexOutOfBoundsException e1){}
			}
		}
		
		
		
		
		if(TileentitiesToBeAdded>0){
		TileentitiesToBeAdded--;
			
		World world = Minecraft.getMinecraft().theWorld;
		for(int ph=0;ph<world.loadedTileEntityList.size();ph++){
			TileentityAnimatedClient c;
			if(world.loadedTileEntityList.get(ph) instanceof TileentityAnimatedClient){
				c = (TileentityAnimatedClient) world.loadedTileEntityList.get(ph);
			}else{
				continue;
			}
			if(coords[0]<=c.xCoord && coords[1]<=c.yCoord && coords[2]<=c.zCoord && coords[3]>=c.xCoord && coords[4]>=c.yCoord && coords[5]>=c.zCoord)
				ClientProxy.addTileentityToController(c, this);
		}
		}
		
		
	}

}
