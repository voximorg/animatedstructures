package com.awesomeholden.packets;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class SendOnTileentityAnimatedDeleted implements IMessage {
	
	public int[] coords;
	
	public SendOnTileentityAnimatedDeleted(){};
	
	public SendOnTileentityAnimatedDeleted(int[] coords){
		this.coords = coords;
		System.out.println("SEND ON TILEENTITY ANIMED DELETED CONSTRUCTOR");
	}

	@Override
    public void fromBytes(ByteBuf buf) {
		coords = new int[6];
        for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
    }

}
