package com.awesomeholden;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.items.MItems;
import com.awesomeholden.packets.AddTextureToClients;
import com.awesomeholden.packets.CallTileentityAnimatedRealConstructor;
import com.awesomeholden.packets.FindAnimationControllerServer;
import com.awesomeholden.packets.GivePlayerTextures;
import com.awesomeholden.packets.HandleCallTileentityAnimatedConstructor;
import com.awesomeholden.packets.HandleOnTileentityAnimatedDeleted;
import com.awesomeholden.packets.HandleTileentityAnimatedTextureUpdate;
import com.awesomeholden.packets.RefreshAnimation;
import com.awesomeholden.packets.RemoveControllerClient;
import com.awesomeholden.packets.RemoveEditorClient;
import com.awesomeholden.packets.SendCallTileentityAnimatedConstructor;
import com.awesomeholden.packets.CreateAnimationControllerServer;
import com.awesomeholden.packets.SendOnTileentityAnimatedDeleted;
import com.awesomeholden.packets.SendTileentityAnimatedTextureUpdate;
import com.awesomeholden.packets.SetAnimationControllerClientCoords;
import com.awesomeholden.packets.SetCoordsOnClient;
import com.awesomeholden.packets.ShouldDestroyController;
import com.awesomeholden.packets.ShouldRemoveEditor;
import com.awesomeholden.packets.UpdateControllerClientTextures;
import com.awesomeholden.packets.gui.AddFrameInterval;
import com.awesomeholden.packets.gui.DeleteFrame;
import com.awesomeholden.packets.gui.Erase;
import com.awesomeholden.packets.gui.GetFrameIntervals;
import com.awesomeholden.packets.gui.GetFrameIntervalsResponse;
import com.awesomeholden.packets.gui.GetFrameByLayerAndFrame;
import com.awesomeholden.packets.gui.SaveControllerToItem;
import com.awesomeholden.packets.gui.SetTexture;
import com.awesomeholden.packets.gui.SetFrameInterval;
import com.awesomeholden.proxies.CommonProxy;
import com.google.common.collect.BiMap;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.ChatComponentText;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = Main.MODID, version = Main.VERSION)



//@NetworkMod( channels = {""}, clientSideRequired = true, serverSideRequired = true )

public class Main
{
	
	public static String workingDir = "";
	
	public static int indexOf(List l,Object o){
		for(int i=0;i<l.size();i++){
			if(l.get(i) == o)
				return i;
		}
		return -1;
	}
	
	public static int randRange(int range){
		return (int) (Math.random()*range);
	}
	
	//this is a helper function for opengl draws a block with texture
	
	public static boolean compareArrays(int[] a0,int[] a1){
		for(int ph=0;ph<a0.length;ph++){
			if(a0[ph] != a1[ph]){
				return false;
			}
		}
		return true;
	}
	
	private static byte drawPlaceHolder = 0;
	public static void draw(float x,float y,float z){
		switch(drawPlaceHolder){
			case 0:
				GL11.glTexCoord3f(0, -1,0);
				drawPlaceHolder++;
				break;
			case 1:
				GL11.glTexCoord3f(1, -1,0);
				drawPlaceHolder++;
				break;
			case 2:
				GL11.glTexCoord3f(1, 0, 0);
				drawPlaceHolder++;
				break;
			case 3:
				GL11.glTexCoord3f(0, 0, 0);
				drawPlaceHolder = 0;
				break;
		}
		
		GL11.glVertex3f(x,y,z);
	}
	
	public static void draw(float x,float y){
		switch(drawPlaceHolder){
			case 0:
				GL11.glTexCoord2f(0, -1);
				drawPlaceHolder++;
				break;
			case 1:
				GL11.glTexCoord2f(1, -1);
				drawPlaceHolder++;
				break;
			case 2:
				GL11.glTexCoord2f(1, 0);
				drawPlaceHolder++;
				break;
			case 3:
				GL11.glTexCoord2f(0, 0);
				drawPlaceHolder = 0;
				break;
		}
		
		GL11.glVertex2f(x,y);
	}
	
	public static void drawString(String string,double[] pos,double[] size,double spaceSize,String blacklist){
		double f = (size[0]/string.length());
		double s = f-spaceSize;
		double y = pos[1]-size[1];
		
		double left = pos[0];
		double halfY = pos[1]-(size[1]/2);
		double right = left+s;
		double mid = left+(s/2);
		for(int ph=0;ph<string.length();ph++){
			string = string.toLowerCase();
			if(blacklist.indexOf(string.charAt(ph)) != 1){
			GL11.glBegin(GL11.GL_LINE_STRIP);
			if(string.charAt(ph) == 'a'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right,halfY);
				
			}else if(string.charAt(ph) == 'b'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left,y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == 'c'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == 'd'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
			}else if(string.charAt(ph) == 'e'){
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == 'f'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == 'g'){
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == 'h'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == 'i'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == 'j'){
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == 'k'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == 'l'){
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(mid, y);
			}else if(string.charAt(ph) == 'm'){
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == 'n'){
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
			}else if(string.charAt(ph) == 'o'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
			}else if(string.charAt(ph) == 'p'){
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == 'q'){
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
			}else if(string.charAt(ph) == 'r'){
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == 's'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == 't'){
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
			}else if(string.charAt(ph) == 'u'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
			}else if(string.charAt(ph) == 'v'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(left, pos[1]);
			}else if(string.charAt(ph) == 'w'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
			}else if(string.charAt(ph) == 'x'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(mid, halfY);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, pos[1]);
			}else if(string.charAt(ph) == 'y'){
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(mid, halfY);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(mid, halfY);
				GL11.glVertex2d(left, pos[1]);
			}else if(string.charAt(ph) == 'z'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == '/'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == ':'){
				GL11.glEnd();
				/*GL11.glEnable( GL11.GL_POINT_SIZE );
				GL11.glPointSize(0.005f);*/
				GL11.glBegin(GL11.GL_POINTS);
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_LOOP);
			}else if(string.charAt(ph) == '.'){
				GL11.glEnd();
				/*GL11.glEnable( GL11.GL_POINT_SIZE );
				GL11.glPointSize(0.005f);*/
				GL11.glBegin(GL11.GL_POINTS);
				GL11.glVertex2d(mid, y);
				GL11.glEnd();
				GL11.glBegin(GL11.GL_LINE_LOOP);
			}else if(string.charAt(ph) == '0'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
			}else if(string.charAt(ph) == '1'){
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(mid, pos[1]);
				GL11.glVertex2d(mid, y);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == '2'){
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
			}else if(string.charAt(ph) == '3'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(mid, halfY);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == '4'){
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
			}else if(string.charAt(ph) == '5'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == '6'){
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, halfY);
				GL11.glVertex2d(left, halfY);
			}else if(string.charAt(ph) == '7'){
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, y);
			}else if(string.charAt(ph) == '8'){
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, y);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
			}else if(string.charAt(ph) == '9'){
				GL11.glVertex2d(right, y);
				GL11.glVertex2d(right, pos[1]);
				GL11.glVertex2d(left, pos[1]);
				GL11.glVertex2d(left, halfY);
				GL11.glVertex2d(right, halfY);
			}
			GL11.glEnd();
			//System.out.println("HEHEHEHE: "+left+','+right+','+f);
			left+=f;
			right = left+s;
			mid = left+(s/2);
			}
		}
	}
	
	public static int[] toIntArray(List<Integer> list){
		int[] ret = new int[list.size()];
		for(int i = 0;i < ret.length;i++)
		ret[i] = list.get(i);
		return ret;
	}
	
	public static List<Integer> toIntList(int[] array){
		List<Integer> list = new ArrayList<Integer>();
		for(int ph=0;ph<array.length;ph++) list.add(array[ph]);
		return list;
	}
	
	public static List<Byte> byteToByte(byte[] bytes){
		List<Byte> r = new ArrayList<Byte>();
		for(int i=0;i<bytes.length;i++){
			r.add(bytes[i]);
		}
		return r;
	}
	
	public static byte[] byteToByte(List<Byte> ls){
		byte[] ar = new byte[ls.size()];
		
		for(int i=0;i<ar.length;i++)
			ar[i] = ls.get(i);
		
		return ar;
	}
	
	
	
    public static final String MODID = "AnimatedStructures";
    public static final String VERSION = "1.0";
    
    @SidedProxy(clientSide="com.awesomeholden.proxies.ClientProxy", serverSide="com.awesomeholden.proxies.ServerProxy")
    public static CommonProxy proxy = new CommonProxy();
    
    @Instance
    public static Main instance = new Main();
    
    public static SimpleNetworkWrapper network;
    
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) throws IOException{
		String[] stuff = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().split(File.separator);
		
		for(int i=0;i<stuff.length-4;i++)
			workingDir+=(File.separator+stuff[i]);
		
		//workingDir = workingDir.substring(0, workingDir.length()-1);
		
		if(!new File(workingDir).exists())
			workingDir = workingDir.split(":")[1];
		
		System.out.println("WORKING DIR: "+workingDir);
		
		/*URL url = null;
		try {
			url = new URL("jar:file:"+Main.workingDir+"!/assets/animatedstructures/textures/blocks");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream is = null;
		try{
			is = url.openStream();
		}catch(FileNotFoundException e2){}*/
				
			network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
			
		proxy.preInit(e);

		// network.registerMessage(Handler.class, Send.class, 0, Side.CLIENT);
		network.registerMessage(HandleOnTileentityAnimatedDeleted.class,SendOnTileentityAnimatedDeleted.class, 1, Side.SERVER);
		network.registerMessage(HandleCallTileentityAnimatedConstructor.class,SendCallTileentityAnimatedConstructor.class, 2, Side.CLIENT);
		network.registerMessage(HandleTileentityAnimatedTextureUpdate.class,SendTileentityAnimatedTextureUpdate.class, 3, Side.CLIENT);
		network.registerMessage(CreateAnimationControllerServer.Handler.class,CreateAnimationControllerServer.class, 4, Side.SERVER);
		network.registerMessage(GetFrameIntervals.Handler.class,GetFrameIntervals.class, 5, Side.SERVER);
		network.registerMessage(GetFrameIntervalsResponse.Handler.class,GetFrameIntervalsResponse.class, 6, Side.CLIENT);
		network.registerMessage(AddFrameInterval.Handler.class,AddFrameInterval.class, 7, Side.SERVER);
		network.registerMessage(SetFrameInterval.Handler.class,SetFrameInterval.class, 8, Side.SERVER);
		network.registerMessage(SetTexture.Handler.class, SetTexture.class, 9,Side.SERVER);
		network.registerMessage(SetTexture.Response.Handler.class,SetTexture.Response.class, 10, Side.CLIENT);
		network.registerMessage(AddTextureToClients.Handler.class,AddTextureToClients.class, 11, Side.CLIENT);
		network.registerMessage(GetFrameByLayerAndFrame.Handler.class,GetFrameByLayerAndFrame.class, 12, Side.SERVER);
		network.registerMessage(GetFrameByLayerAndFrame.Response.Handler.class,GetFrameByLayerAndFrame.Response.class, 13, Side.CLIENT);
		network.registerMessage(RemoveControllerClient.Handler.class,RemoveControllerClient.class,14,Side.CLIENT);
		//network.registerMessage(SaveControllerToItem.Handler.class,SaveControllerToItem.class, 14, Side.SERVER);
		network.registerMessage(CallTileentityAnimatedRealConstructor.Handler.class,CallTileentityAnimatedRealConstructor.class, 15, Side.CLIENT);
		network.registerMessage(SetCoordsOnClient.Handler.class,SetCoordsOnClient.class, 16, Side.CLIENT);
		//network.registerMessage(WorldUnload.Handler.class, WorldUnload.class,17, Side.CLIENT);
		network.registerMessage(GivePlayerTextures.Handler.class,GivePlayerTextures.class, 18, Side.CLIENT);
		// network.registerMessage(DeleteAnimationControllers.Handler.class,
		// DeleteAnimationControllers.class, 19, Side.SERVER);
		network.registerMessage(SetAnimationControllerClientCoords.Handler.class,SetAnimationControllerClientCoords.class, 20, Side.CLIENT);
		network.registerMessage(FindAnimationControllerServer.Handler.class,FindAnimationControllerServer.class, 21, Side.SERVER);
		network.registerMessage(UpdateControllerClientTextures.Handler.class,UpdateControllerClientTextures.class, 22, Side.CLIENT);
		network.registerMessage(ShouldRemoveEditor.Handler.class,ShouldRemoveEditor.class, 23, Side.SERVER);
		network.registerMessage(RemoveEditorClient.Handler.class,RemoveEditorClient.class, 24, Side.CLIENT);
		//network.registerMessage(DropGlass.Handler.class, DropGlass.class, 25,Side.SERVER);
		network.registerMessage(ShouldDestroyController.Handler.class, ShouldDestroyController.class, 26, Side.CLIENT);
		//network.registerMessage(RemoveEditorServer.Handler.class, RemoveEditorServer.class, 27, Side.SERVER);
		network.registerMessage(Erase.Handler.class, Erase.class, 28, Side.SERVER);
		network.registerMessage(RefreshAnimation.Handler.class, RefreshAnimation.class, 29, Side.SERVER);
		network.registerMessage(DeleteFrame.Handler.class, DeleteFrame.class, 30, Side.SERVER);
		
		
		
		
		

		GameRegistry.addShapelessRecipe(new ItemStack(MItems.rawLed),
				new Object[] { new ItemStack(MItems.redShard), new ItemStack(MItems.greenShard),
						new ItemStack(MItems.blueShard) });
				
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.animatedInsides),new Object[]{"SRS","IGI","SRS",'S',new ItemStack(Blocks.stone),'R',new ItemStack(Items.redstone),'G',new ItemStack(Blocks.gold_block),'I',new ItemStack(Items.iron_ingot)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.ledBlock),new Object[]{"###","# #","###",'#',new ItemStack(MItems.led)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.Animated),new Object[]{"BBB","BFB","BBB",'B',new ItemStack(CreateBlocks.ledBlock),'F',new ItemStack(CreateBlocks.animatedInsides)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.enderBrain),new Object[]{"###","#E#","###",'#',new ItemStack(MItems.enderBrain),'E',new ItemStack(Items.ender_eye)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.bone),new Object[]{"###","###","###",'#',new ItemStack(Items.bone)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.controllerInsides),new Object[]{"DDD","DGD","DDD",'D',new ItemStack(Items.diamond),'G',new ItemStack(Items.ghast_tear)});
		GameRegistry.addRecipe(new ItemStack(CreateBlocks.AnimationEditor),new Object[]{"OEO","WCW","OBO",'O',new ItemStack(Blocks.obsidian),'E',new ItemStack(CreateBlocks.enderBrain),'W',new ItemStack(Blocks.wool),'C',new ItemStack(CreateBlocks.controllerInsides),'B',new ItemStack(CreateBlocks.bone)});
		GameRegistry.addRecipe(new ItemStack(MItems.magicWand),new Object[]{"A  "," S ","  S",'A',new ItemStack(CreateBlocks.Animated),'S',new ItemStack(Blocks.obsidian)});
		
		GameRegistry.addSmelting(MItems.rawLed, new ItemStack(MItems.led), 1.0F);

		/*
		 * field_150769_h =
		 * EnumConnectionState.func_150760_a(0).func_150753_a();
		 * 
		 * registerPacket(100,ModifiedC07PacketPlayerDigging.class);
		 */
		// network.registerMessage(GetTextures.Handler.class, GetTextures.class,
		// 22, Side.SERVER);
		// network.registerMessage(Hanlder.class, TestPacket.class, 23,
		// Side.CLIENT);

	}

	@EventHandler
    public void init(FMLInitializationEvent e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		/*Method m = EnumConnectionState.class.getDeclaredMethod("func_150751_a",int.class,Class.class);
		m.setAccessible(true);
		m.invoke(null, 100, ModifiedC07PacketPlayerDigging.class);*/
		proxy.init(e);

    }
	

	@EventHandler
    public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
    }
}
