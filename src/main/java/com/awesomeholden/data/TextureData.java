package com.awesomeholden.data;

import java.util.List;

import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSavedData;

public class TextureData extends WorldSavedData{
	
	public static String theStuff;

	public TextureData(String string) {
		super(string);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		//theStuff = nbt.getString("ANIWHAT");
		List<ResourceLocation> t = TileentityAnimatedServer.textures;
		/*if(t.size()>0){
			t.clear();
		}*/
		for(int i=0;i<nbt.getInteger("texture number");i++){
			t.add(new ResourceLocation(nbt.getString("t"+i)));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		//nbt.setString("ANIWHAT","\n\n\n\nYOU READ tHIS RIGHT!\n\n\n\n");
		int s = TileentityAnimatedServer.textures.size();
		nbt.setInteger("texture number",s);
		for(int i=0;i<s;i++){
			ResourceLocation c = TileentityAnimatedServer.textures.get(i);
			nbt.setString("t"+i,c.getResourceDomain()+':'+c.getResourcePath());
		}
	}

}
