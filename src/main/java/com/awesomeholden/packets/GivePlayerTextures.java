package com.awesomeholden.packets;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import net.minecraft.util.ResourceLocation;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class GivePlayerTextures implements IMessage{
	
	public GivePlayerTextures(){}

	@Override
	public void fromBytes(ByteBuf buf) {
		List<ResourceLocation> list = TileentityAnimatedClient.textures;
		list.clear();
		/*for(int ph=0;ph<buf.readInt();ph++){
			try{
				byte[] bytes = new byte[3];
				buf.readBytes(bytes);
				System.out.println("ADDED A RESOURCELOCAtION: "+ph+" string lenght: "+bytes.toString());
				list.add(new ResourceLocation(bytes.toString()));
			}catch(DecoderException e){
				return;
			}
		}*/
		
		
		int length = buf.readInt();
		
		byte[] bytes = new byte[length];
		
		/*for(int i=0;i<length;i++){
			System.out.print("READING BYTE: "+i);
			bytes[i] = buf.readByte();
		}*/
		
		buf.readBytes(bytes);
				
		//List<String> textures = new ArrayList<String>();
		
		String[] textures = new String[1];
		try {
			textures = new String(bytes,"UTF-8").split("\n");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0;i<textures.length;i++){
			String thing = textures[i];
			list.add(new ResourceLocation(textures[i]));
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		//buf.writeInt(list.size());
		
		//byte[] build = new byte[16];
		
		List<ResourceLocation> list = TileentityAnimatedServer.textures;
				
		List<Byte> bytes = new ArrayList<Byte>();
		for(int ph=0;ph<list.size();ph++)
			bytes.addAll(Main.byteToByte(((list.get(ph).getResourceDomain()+':'+list.get(ph).getResourcePath())+"\n").getBytes()));
		
		buf.writeInt(bytes.size());
		buf.writeBytes(Main.byteToByte(bytes));
		
		/*for(int i=0;i<bytes.size();i++){
			System.out.println("WRITE BYTE "+i);*/
			//buf.writeBytes(bytes/*bytes.get(i)*/);
		//}
	}
	
	public static class Handler implements IMessageHandler<GivePlayerTextures,IMessage>{

		@Override
		public IMessage onMessage(GivePlayerTextures message, MessageContext ctx) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

}
