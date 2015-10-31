package com.awesomeholden.packets;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public class SendCallTileentityAnimatedConstructor implements IMessage {
	
	public int x;
	public int y;
	public int z;
	
	public SendCallTileentityAnimatedConstructor(){};
	
	public SendCallTileentityAnimatedConstructor(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt(); // this class is very useful in general for writing more complex objects
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

}
