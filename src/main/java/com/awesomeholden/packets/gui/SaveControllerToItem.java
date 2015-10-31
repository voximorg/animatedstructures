package com.awesomeholden.packets.gui;

import java.util.ArrayList;
import java.util.List;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.items.MItems;
import com.awesomeholden.proxies.ServerProxy;

import net.minecraft.block.BlockTorch;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SaveControllerToItem implements IMessage{

	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		
	}
	
	/*public static NBTTagCompound nbt = null;
	
	public int[] coords;
	public String name;
	
	public SaveControllerToItem(){}
	
	public SaveControllerToItem(int[] coords,String name){this.coords = coords; this.name = name;}

	@Override
	public void fromBytes(ByteBuf buf) {
		name = ByteBufUtils.readUTF8String(buf);
		coords = new int[6];
		for(int ph=0;ph<6;ph++) coords[ph] = buf.readInt();
	}
	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, name);
		System.out.println("NAME: "+name);
		for(int ph=0;ph<6;ph++) buf.writeInt(coords[ph]);
	}
	
	public static class Handler implements IMessageHandler<SaveControllerToItem,IMessage>{

		@Override
		public IMessage onMessage(SaveControllerToItem message,MessageContext ctx) {
			AnimationControllerServer c = ServerProxy.getAnimationController(message.coords);
			for(int ph=0;ph<MinecraftServer.getServer().worldServers.length;ph++) {
				List list = MinecraftServer.getServer().worldServers[ph].playerEntities;
				for(int ph2=0;ph2<list.size();ph2++){
					EntityPlayer player = ((EntityPlayer) list.get(ph2));
					if(message.name.equals(player.getDisplayName())){
						System.out.println("FOUND PLAYER!");
						ItemStack[] inv = player.inventory.mainInventory;
						for(int ph3=0;ph3<inv.length;ph3++){
							if(inv[ph3] == null) continue;
							if(inv[ph3].getItem().getUnlocalizedName().equals(MItems.Animation.getUnlocalizedName())){
								inv[ph3] = new ItemStack(MItems.Animation);
								System.out.println("ACACIA STAIRS!");
								inv[ph3].stackTagCompound = new NBTTagCompound();
								NBTTagCompound nbt = inv[ph3].stackTagCompound;
								inv[ph3].setStackDisplayName("Animation");
								inv[ph3].addEnchantment(Enchantment.unbreaking, 1);
								nbt.setIntArray("frameIntervals", Main.toIntArray(c.frameIntervals));
								int theControlledSize = c.theControlled.size();
								nbt.setInteger("theControlledSize", theControlledSize);
								for(int ph4=0;ph4<theControlledSize;ph4++){
									TileentityAnimatedServer c2 = c.theControlled.get(ph4);
									nbt.setIntArray("t"+ph4, new int[]{c2.xCoord-c.coords[0],c2.yCoord-c.coords[1],c2.zCoord-c.coords[2]});
									nbt.setIntArray("f"+ph4, Main.toIntArray(c2.frames));
								}
								return null;
							}
						}
					}
				}
			}
			return null;//new Response(message.coords);
		}
		
	}
	
	public static class Response implements IMessage{
		
		public int[] coords;
		public int[] frameIntervals;
		public int[] theControlledCoords;
		public int frameNumber;
		public List<int[]> theControlledFrames;
		
		public Response(int[] coords){
			this.coords = coords;
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			frameIntervals = new int[buf.readInt()];
			for(int ph=0;ph<frameIntervals.length;ph++) frameIntervals[ph] = buf.readInt();
			
			theControlledCoords = new int[buf.readInt()*3];
			theControlledFrames = new ArrayList<int[]>();
			for(int ph=0;ph<theControlledCoords.length;ph+=3){
				theControlledCoords[ph] = buf.readInt();
				theControlledCoords[ph+1] = buf.readInt();
				theControlledCoords[ph+2] = buf.readInt();
				theControlledFrames.add(new int[frameIntervals.length]);
				for(int ph2=0;ph2<frameIntervals.length;ph2++){
					theControlledFrames.get(ph)[ph2] = buf.readInt();
				}
			}
			
		}

		@Override
		public void toBytes(ByteBuf buf) {
			AnimationControllerServer c = ServerProxy.getAnimationController(coords);
			int s = c.frameIntervals.size();
			buf.writeInt(s);
			for(int ph=0;ph<s;ph++) buf.writeInt(frameIntervals[ph]);
			s = c.theControlled.size();
			buf.writeInt(s);
			int frameNumber = c.frameIntervals.size();
			for(int ph=0;ph<s;ph++){
				TileentityAnimatedServer c2 = c.theControlled.get(ph);
				buf.writeInt(c2.xCoord-c.coords[0]);
				buf.writeInt(c2.yCoord-c.coords[1]);
				buf.writeInt(c2.zCoord-c.coords[2]);
				for(int ph2=0;ph2<frameNumber;ph2++) buf.writeInt(c2.frames.get(ph2));
			}
		}
		
		public static class Handler implements IMessageHandler<Response,IMessage>{

			@Override
			public IMessage onMessage(Response message, MessageContext ctx) {
				nbt.setIntArray("FrameIntervals", message.frameIntervals);
				nbt.setIntArray("TheControlledCoords", message.theControlledCoords);
 				for(int ph=0;ph<message.theControlledFrames.size();ph++){
					nbt.setIntArray("f"+ph, message.theControlledFrames.get(ph));
				}
				return null;
			}
			
		}
		
	}*/

}
