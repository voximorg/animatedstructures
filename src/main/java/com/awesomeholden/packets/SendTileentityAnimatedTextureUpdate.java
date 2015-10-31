package com.awesomeholden.packets;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class SendTileentityAnimatedTextureUpdate implements IMessage{
	
	int texture;
	int controlledIndex;
	
	int coords00;
	int coords01;
	int coords02;
	int coords10;
	int coords11;
	int coords12;
	
	public SendTileentityAnimatedTextureUpdate(){};
	
	public SendTileentityAnimatedTextureUpdate(int texture,int controlledIndex,int[] cc){
		this.texture = texture;
		this.controlledIndex = controlledIndex;
		
		coords00 = cc[0];
		coords01 = cc[1];
		coords02 = cc[2];
		coords10 = cc[3];
		coords11 = cc[4];
		coords12 = cc[5];
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		texture = buf.readInt();
		controlledIndex = buf.readInt();
		coords00 = buf.readInt();
		coords01 = buf.readInt();
		coords02 = buf.readInt();
		coords10 = buf.readInt();
		coords11 = buf.readInt();
		coords12 = buf.readInt();
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(texture);
		buf.writeInt(controlledIndex);
		buf.writeInt(coords00);
		buf.writeInt(coords01);
		buf.writeInt(coords02);
		buf.writeInt(coords10);
		buf.writeInt(coords11);
		buf.writeInt(coords12);
	}

}
