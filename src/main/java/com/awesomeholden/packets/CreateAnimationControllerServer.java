package com.awesomeholden.packets;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class CreateAnimationControllerServer implements IMessage{
	
	public int[] coords;
	public String player;
	
	public CreateAnimationControllerServer(){}
	
	public CreateAnimationControllerServer(int[] coords,String player){
		this.coords = coords;
		this.player = player;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		coords = new int[6];
		for(int i=0;i<6;i++)
			coords[i] = buf.readInt();
		
		player = ByteBufUtils.readUTF8String(buf);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		for(int i=0;i<6;i++)
			buf.writeInt(coords[i]);
		
		ByteBufUtils.writeUTF8String(buf,player);
		
	}
	
	public static class Handler implements IMessageHandler<CreateAnimationControllerServer, IMessage> {
	    
	    @Override
	    public IMessage onMessage(CreateAnimationControllerServer message, MessageContext ctx) {
	    		
	    	
	    	
	    	if(ServerProxy.controllerCoordsAssigmentCache.size() > 0){
	    		AnimationControllerServer c = ServerProxy.controllerCoordsAssigmentCache.get(0);
	    		int[] coords = c.coords;
	    			c.coords = message.coords;
	    			
	    		c.dimension = ServerProxy.getPlayer(message.player).dimension;
	    		
	    		System.out.println("C COORDS: "+Arrays.toString(c.coords));
	    		
	    		WorldServer w = MinecraftServer.getServer().worldServers[c.dimension];

	    		TileentityAnimationEditorServer e2 = ServerProxy.getEditor(c);
	    		
	    		int[] cc = c.coords;
	    		
	    		if((!(Math.abs(cc[0]-cc[3])<20 && Math.abs(cc[1]-cc[4])<20 && Math.abs(cc[2]-cc[5])<20)) || (cc[0]==0 && cc[1]==0 && cc[2]==0 && cc[3]==0 && cc[4]==0 && cc[5]==0)){
	    			System.out.println("TO BIG OR 000000");
	    			
	    			ServerProxy.deleteAnimationController(cc);
					
					w.setBlock(e2.xCoord, e2.yCoord, e2.zCoord, Blocks.air);
					
					Main.network.sendToAllAround(new RemoveEditorClient(e2.xCoord,e2.yCoord,e2.zCoord), new TargetPoint(c.dimension,e2.xCoord,e2.yCoord,e2.zCoord, 120));
	    		}else{
	    			    				
	    		for(int i=0;i<w.loadedTileEntityList.size();i++){
	    			if(!(w.loadedTileEntityList.get(i) instanceof TileentityAnimationEditorServer))
	    					continue;
	    			
	    			TileentityAnimationEditorServer e = (TileentityAnimationEditorServer) w.loadedTileEntityList.get(i);
	    			
	    			int[] cb = e.controller.coords;
	    			
	    			System.out.println("CC: "+Arrays.toString(cc));
	    			
	    			if(( ( ( (cc[0]>=cb[0] && cc[0]<=cb[3]) || (cb[0]>=cc[0] && cb[0]<=cc[3]) ) && ( (cc[1]>=cb[1] && cc[1]<=cb[4]) || (cb[1]>=cc[1] && cb[1]<=cc[4]) ) && ( (cc[2]>=cb[2] && cc[2]<=cb[5]) || (cb[2]>=cc[2] && cb[2]<=cc[5]) ) ) && e.controller != c && c.dimension == e.controller.dimension)){
	    				
	    				System.out.println("OTHEr DELETE");
	        						        					
	        					ServerProxy.deleteAnimationController(cc);
	        					
	        					w.setBlock(e2.xCoord, e2.yCoord, e2.zCoord, Blocks.air);
	        					
	        					Main.network.sendToAllAround(new RemoveEditorClient(e2.xCoord,e2.yCoord,e2.zCoord), c.genTargetPoint());
	        					
	        					break;
	        			}
	    			}
	    		}
	    		
						
	    	}/*else{
	    		AnimationControllerServer c = new AnimationControllerServer();
	    		c.coords = new int[]{message.p1, message.p2, message.p3, message.p4, message.p5, message.p6};
	    	}*/
	    		    	
			    ServerProxy.controllerCoordsAssigmentCache.clear();

	    	return null; // no response in this case 
	    	
	    }

	}


}
