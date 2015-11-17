package com.awesomeholden;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.TESRs.TESReditor;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.blocks.CreateBlocks;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.guis.animationeditorguiwidges.TextBox;
import com.awesomeholden.guis.animationeditorguiwidges.TextBoxInput;
import com.awesomeholden.itemrenderers.ItemRendererAnimation;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.CommonProxy;
import com.awesomeholden.proxies.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ClientLoop {
	
	public static ClientLoop instance = new ClientLoop();
	
	public static ResourceLocation aBlockTex = new ResourceLocation("textures/items/diamond.png");
	
	/*@SubscribeEvent
	public void drawBlockHightlight(DrawBlockHighlightEvent e){
		e.target.
		e.setCanceled(true);
	}*/
	
	public void function(){}
	
	public List<Method> ls = new ArrayList<Method>();
	
	public static AnimationControllerClient tryingToDestroy = null;
	
	@SubscribeEvent
	public void changeDimension(WorldEvent.Load e){
		
		ClientProxy.AnimationControllers.clear();
		
	}
		
	/*@SubscribeEvent
	public void input(InputEvent.KeyInputEvent e) {
		if(tryingToDestroy != null){
			if(Keyboard.getEventKey() == 121){
				Minecraft.getMinecraft().theWorld.loadedTileEntityList.remove(ClientProxy.getEditorFromCoords(tryingToDestroy.coords));
				ClientProxy.AnimationControllers.remove(tryingToDestroy);
				Main.network.sendToServer(new RemoveEditorServer(tryingToDestroy.coords));
			}else if(Keyboard.getEventKey() == 110){
				tryingToDestroy = null;
			}
		}
		/*if(Minecraft.getMinecraft().gameSettings.keyBindDrop.isPressed()){
			EntityClientPlayerMP p = Minecraft.getMinecraft().thePlayer;
			
			ItemStack stack = p.getCurrentEquippedItem();
			
			if(stack == null)
				return;
			
			Block b = Block.getBlockFromItem(stack.getItem());
			if(b == Blocks.stained_glass){
				int meta = stack.getItemDamage();
				
				if(meta == 13 || meta == 14 || meta == 11){
					Main.network.sendToServer(new DropGlass(p.getDisplayName()));
				}else{
					p.dropOneItem(GuiScreen.isCtrlKeyDown());
				}
			}else{
				p.dropOneItem(GuiScreen.isCtrlKeyDown());
			}
		}*/
	//}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		/*if(event.side.isClient()){
			i--;
			System.out.println("I: "+i);
		}else{
			i++;
			System.out.println("I: "+i);
		}*/
	}
	
	/*@SubscribeEvent
	public void onGuiIngameMenuQuit(GuiScreenEvent.ActionPerformedEvent event) {
		if (event.gui instanceof GuiIngameMenu && event.button.id == 1) {
			
			System.out.println("QUIT FROM SLSKFJ");
					
			ServerProxy.AnimationControllers.clear();
			ClientProxy.AnimationControllers.clear();
			
		}
			
		/*ServerProxy.AnimationControllers.clear();
		ClientProxy.AnimationControllers.clear();*/
		
		//TileentityAnimatedServer.textures.clear();
	//}
	
	public World world;
	
	@SubscribeEvent
	public void onRenderTick(TickEvent.RenderTickEvent event) {
				
		if(Minecraft.getMinecraft().getIntegratedServer() == null && ServerProxy.AnimationControllers.size() > 0){
						
			ServerProxy.AnimationControllers.clear();
			ClientProxy.AnimationControllers.clear();
			
			TileentityAnimatedClient.textures.clear();
			TileentityAnimatedServer.textures.clear();
		}
					
		//System.out.println(Main.currentTileEntityForGui);
		if(ClientProxy.gui != null){
			ClientProxy.gui.render();
		}
	}
	
	/*@SubscribeEvent
	public void playerClickBlock(PlayerEvent.BreakSpeed e){
		if("tile.log".equals(e.block.getUnlocalizedName())){
			if(e.entityPlayer.getHeldItem() == null){
				e.newSpeed = 0;
				return;
			}
			if(!(e.entityPlayer.getHeldItem().getItem() instanceof ItemAxe)){
				e.newSpeed = 0;
			}
		}
	}*/
	
	public static float degrees = 0;
	
	public static float y = 0;
	
	public static float yInc = 0.0025f;
	
	private List<ResourceLocation> textures = TileentityAnimatedClient.textures;
	
	private int tick = 0;
	private int prevTex = 0;
	
	int previousEvent;
	
	public static GuiScreen previousScreen;
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		
		//System.out.println("CLIENT SIZE: "+ClientProxy.AnimationControllers.size());
				
		if(event.phase == Phase.START && ClientProxy.gui != null){
			
			TextBox.tick++;
			TextBoxInput.deleteTick++;
			
			while(Keyboard.next()){
				if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
		        	ClientProxy.rightPressed = true;
		        }else{
		        	ClientProxy.rightPressed = false;
		        }if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
		        	ClientProxy.leftPressed = true;
		        }else{
		        	ClientProxy.leftPressed = false;
		        }
				
				eventChar = (byte) Keyboard.getEventCharacter();
				
				if(eventChar != prevEventChar)
					newEventChar = true;
				
				if(newEventChar){
					if(eventChar == 27){
						ClientProxy.gui = null;
						newEventChar = false;
					}
				}
				
				prevEventChar = eventChar;
			}
		
			while(Mouse.next()){
				
				if(Mouse.getEventButton() == AnimationEditorGui.button && (!Mouse.getEventButtonState()))
						leftUp = true;
				
				AnimationEditorGui.eventButton = Mouse.getEventButton();
				if(Mouse.getEventButton() > -1){
					AnimationEditorGui.newEventButton = true;
				}
				
				
			}
		}

						
		if(tick >= 10 && textures.size() != 0){
			
			TESReditor.texTrainIndex++;
			if(TESReditor.texTrainIndex == textures.size())
				TESReditor.texTrainIndex = 0;
			
			tick = 0;
			int tex = prevTex;
			int infiniteProtect = 0;
			while(true){
				tex++;
				if(tex >= textures.size())
					tex = 0;
				ResourceLocation r = textures.get(tex);
				if(r.getResourcePath().length()>15 && r.getResourcePath().substring(0, 15).equals("textures/blocks") && !(r.getResourceDomain()+":"+r.getResourcePath()).equals("animatedstructures:textures/transparent.png")){
					aBlockTex = textures.get(tex);
					prevTex = tex;
					break;
				}
				
				infiniteProtect++;
				if(infiniteProtect == textures.size())
					break;
			}
		}			
		tick++;
					
		y+=yInc;
		if(y>0.1f){
			yInc = -yInc;
		}else if(y<-0.1f){
			yInc = -yInc;
		}
		
		degrees++;
		if(degrees>360)
			degrees = 0;
		
		for(int i=0;i<ClientProxy.tryingToCallRealConstructor.size();i+=3){
			List<Integer> c = ClientProxy.tryingToCallRealConstructor;
			
			List<TileEntity> ls = Minecraft.getMinecraft().theWorld.loadedTileEntityList;
			for(int i2=0;i2<ls.size();i2++){
			
				try{
				TileEntity t = ls.get(i2);
				if(t.xCoord == c.get(i) && t.yCoord == c.get(i+1) && t.zCoord == c.get(i+2)){
				
				TileentityAnimatedClient c2 = (TileentityAnimatedClient) t;
				if(c2 != null){
					c2.realConstructor();
					c.remove(i);
					c.remove(i);
					c.remove(i);
				}
				}
				}catch(IndexOutOfBoundsException e){ break; }
			}
		}
				
		//System.out.println(GL11.glGetError());
		
		//System.out.println("ANIMATION CONTROLLERS CLIENT: "+ClientProxy.AnimationControllers.size());
		
		//System.out.println(Keyboard.KEY_ESCAPE);
		
		/*if(Minecraft.getMinecraft().thePlayer == null) return;
		
		ItemStack chest = Minecraft.getMinecraft().thePlayer.inventory.armorInventory[2];
		
		if(chest != null && chest.getItem() == Items.chainmail_chestplate && Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			EntityPlayer p = Minecraft.getMinecraft().thePlayer;
			p.setPositionAndUpdate(p.posX, p.posY+0.0001, p.posZ);
		}*/
		
		//Main.network.sendToServer(new TestPacket());
		//System.out.println("random: "+random);
		/*int count = 0;
		for(int ph=0;ph<ServerProxy.AnimationControllers.size();ph++){
			for(int ph2=0;ph2<ServerProxy.AnimationControllers.get(ph).theControlled.size();ph2++){
				count++;
			}
		}
		System.out.println("client count: "+count);*/
		
		//System.out.println("number: "+ClientProxy.AnimationControllers.size());
		float halfWidth = Display.getWidth();
		float halfHeight = Display.getHeight();
		if(Display.getHeight()>Display.getWidth()){
			ClientProxy.canvasWidth = 0.8f;
		}else{
			ClientProxy.canvasWidth = ((halfHeight*0.8f)/halfWidth);
		}
		halfWidth/=2;
		halfHeight/=2;
		
		if(Mouse.getX()<halfWidth){
			ClientProxy.mousePos[0] = -(halfWidth-Mouse.getX())/halfWidth;
		}else{
			ClientProxy.mousePos[0] = (Mouse.getX()-halfWidth)/halfWidth;
		}
		
		if(Mouse.getY()<halfHeight){
			ClientProxy.mousePos[1] = -(halfHeight-Mouse.getY())/halfHeight;
		}else{
			ClientProxy.mousePos[1] = (Mouse.getY()-halfHeight)/halfHeight;
		}

		
		//System.out.println(CommonProxy.AnimationControllers);
		
		/*if(ClientProxy.currentTileEntityForGui != null){
			Mouse.setGrabbed(false);
		}*/
	}
	
	/*private boolean wasDead = false;
	private List<ItemStack> keepItems = new ArrayList<ItemStack>();
	private List<Integer> keepIndexes = new ArrayList<Integer>();
	
	private List<ItemStack> currentKeepItems = new ArrayList<ItemStack>();
	private List<Integer> currentKeepIndexes = new ArrayList<Integer>();*/
	
	/*@SubscribeEvent(priority = EventPriority.LOW)
	public void onLivingUpdateEvent(LivingUpdateEvent event){
		if(event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) event.entity;
			if(player.isDead){
				System.out.println("playerDied!");
				for(int ph=0;ph<currentKeepIndexes.size();ph++){
					System.out.println(ph+" index");
					player.inventory.mainInventory[currentKeepIndexes.get(ph)] = currentKeepItems.get(ph);
				}
				keepItems.clear();
				keepIndexes.clear();
			}else{
				currentKeepItems = new ArrayList<ItemStack>(keepItems);
				currentKeepIndexes = new ArrayList<Integer>(keepIndexes);
				keepItems.clear();
				keepIndexes.clear();
				for(int ph=0;ph<player.inventory.mainInventory.length;ph++){
					//System.out.println("LOOPING THROUGH inventory");
					if(player.inventory.mainInventory[ph] != null && StringUtils.difference(player.inventory.mainInventory[ph].getUnlocalizedName(),CreateBlocks.blockAnimated.getUnlocalizedName()).length() == 0){
						System.out.println("OK?");
						keepItems.add(player.inventory.mainInventory[ph]);
						keepIndexes.add(ph);
						}
				}
			}
			System.out.println("finished");
		}
	}*/
	
	@SubscribeEvent
	public void preMouseEvent(MouseEvent e){
		if(ClientProxy.gui != null)
			e.setCanceled(true);
	}
	
	public static byte prevEventChar;
	
	public static byte eventChar;
	
	public static boolean newEventChar = false;
	
		
	public static boolean leftUp;
	
}
