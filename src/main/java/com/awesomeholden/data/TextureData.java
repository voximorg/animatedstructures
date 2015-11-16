package com.awesomeholden.data;

import java.util.List;

import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSavedData;

public class TextureData extends WorldSavedData{
	
	public TextureData(String string) {
		super(string);
		markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		List<ResourceLocation> t = TileentityAnimatedServer.textures;
		for(int i=0;i<nbt.getInteger("texture number");i++){
			t.add(new ResourceLocation(nbt.getString("t"+i)));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int s = TileentityAnimatedServer.textures.size();
		nbt.setInteger("texture number",s);
		for(int i=0;i<s;i++){
			ResourceLocation c = TileentityAnimatedServer.textures.get(i);
			nbt.setString("t"+i,c.getResourceDomain()+':'+c.getResourcePath());
		}
	}

}
