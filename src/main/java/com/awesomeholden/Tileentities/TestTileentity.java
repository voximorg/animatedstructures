package com.awesomeholden.Tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TestTileentity extends TileEntity{
	
	public TestTileentity(){
		markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt){
		super.readFromNBT(nbt);
		System.out.println("READING TO NBT!\nREADING TO NBT!\nREADING TO NBT!\nREADING TO NBT!\nREADING TO NBT!\n");
	}

}
