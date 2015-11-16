package com.awesomeholden.Tileentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.awesomeholden.Main;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.packets.SetAnimationControllerClientCoords;
import com.awesomeholden.packets.SetCoordsOnClient;
import com.awesomeholden.proxies.ServerProxy;

public class TileentityAnimationEditorServer extends TileEntity{
	
	public AnimationControllerServer controller;
	
	public TileentityAnimationEditorServer(){
		markDirty();
	}
	
	public TileentityAnimationEditorServer(AnimationControllerServer c){
		controller = c;
		markDirty();
	}
		
	@Override
	public void writeToNBT(NBTTagCompound nbt){
		super.writeToNBT(nbt);
		nbt.setIntArray("coords", controller.coords);
		nbt.setIntArray("frameIntervals",Main.toIntArray(controller.frameIntervals));

		nbt.setInteger("size", controller.framesInfo.size());
		
		for(int i=0;i<controller.framesInfo.size();i++){
			nbt.setInteger("size"+i, controller.framesInfo.get(i).size());
			for(int i2=0;i2<controller.framesInfo.get(i).size();i2++){
				nbt.setInteger("a"+i+":"+i2,(Integer) controller.framesInfo.get(i).keySet().toArray()[i2]);
				nbt.setIntArray("s"+i+":"+i2, Main.toIntArray((List<Integer>)controller.framesInfo.get(i).values().toArray()[i2]));
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		if(controller == null){
			controller = new AnimationControllerServer();
			ServerProxy.AnimationControllers.add(controller);
		}
		super.readFromNBT(nbt);
		controller.coords = nbt.getIntArray("coords");
		controller.frameIntervals = Main.toIntList(nbt.getIntArray("frameIntervals"));
		
		List<HashMap<Integer,List<Integer>>> list = new ArrayList<HashMap<Integer,List<Integer>>>();
		int size = nbt.getInteger("size");
		for(int i=0;i<size;i++){
			HashMap<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
			int size2 = nbt.getInteger("size"+i);
			for(int i2=0;i2<size2;i2++){
				map.put(nbt.getInteger("a"+i+":"+i2), Main.toIntList(nbt.getIntArray("s"+i+":"+i2)));
			}
			list.add(map);
		}
		
		controller.framesInfo = list;
				
		controller.onCoordsSet();
		
		Main.network.sendToAll(new SetAnimationControllerClientCoords(xCoord,yCoord,zCoord,controller.coords));
		
		//controller.theControlled.addAll(ServerProxy.controllerAssignmentCache);
		
		
	}

}
