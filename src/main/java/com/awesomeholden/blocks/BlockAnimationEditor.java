package com.awesomeholden.blocks;

import java.util.Arrays;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.awesomeholden.Main;
import com.awesomeholden.Tabs;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorServer;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.packets.CreateAnimationControllerServer;
import com.awesomeholden.packets.ShouldRemoveEditor;
import com.awesomeholden.packets.TestPacket;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockAnimationEditor extends Block implements ITileEntityProvider{

	protected BlockAnimationEditor() {
		super(Material.iron);
		isBlockContainer = true;
		this.setBlockName("Animation Editor");
		this.setCreativeTab(Tabs.Tab);
		
		setHardness(1);
	}

	@Override
	//@SideOnly(Side.SERVER)
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
			
			//AnimationControllerClient c2 = ClientProxy.get
			
			AnimationControllerClient c = null;
			if(ClientProxy.outlineCacheMeta[0] == true && ClientProxy.outlineCacheMeta[1] == true){
				
				int[] a0 = new int[2]; int[] a1 = new int[2]; int[] a2 = new int[2];
				
				int[] o = ClientProxy.outlineCache;
				a0[0] = o[0]; a0[1] = o[3]; Arrays.sort(a0);
				a1[0] = o[1]; a1[1] = o[4]; Arrays.sort(a1);
				a2[0] = o[2]; a2[1] = o[5]; Arrays.sort(a2);
				
				int[] coords = new int[]{a0[0],a1[0],a2[0],a0[1],a1[1],a2[1]};
								
				c = new AnimationControllerClient(coords);
				Main.network.sendToServer(new CreateAnimationControllerServer(coords,Minecraft.getMinecraft().thePlayer.getDisplayName()));
				return new TileentityAnimationEditorClient(c);
			}
		}else{
			ServerProxy.controllerCoordsAssigmentCache.clear();
			
			AnimationControllerServer c;
			if(ServerProxy.controllerCoordsAssigmentCache.size() == 0){
				ServerProxy.controllerCoordsAssigmentCache.clear();
				c = new AnimationControllerServer();
			}else{
				c = ServerProxy.controllerCoordsAssigmentCache.get(0);
			}
			/*int[] o = ServerProxy.outlineCache;
			System.out.println("ADDED AN ANIMATIONCONTROLLER! "+o[0]+','+o[1]+','+o[2]+','+o[3]+','+o[4]+','+o[5]);*/
			ServerProxy.AnimationControllers.add(c);
			return new TileentityAnimationEditorServer(c);
		}
		/*}else{
			System.out.println("world is remote");
		}*/
		
		AnimationControllerClient c = new AnimationControllerClient();
		ClientProxy.controllerCoordsAssignmentCache.clear();
		ClientProxy.controllerCoordsAssignmentCache.add(c);
				
		return new TileentityAnimationEditorClient(c);
	}
	
	@Override
	public void onBlockPlacedBy(World world,int x,int y,int z,EntityLivingBase e,ItemStack itemstack){
	}
	
	@Override
	public int getRenderType(){
		return -1;
	}
	
	@Override
	public boolean hasTileEntity(){
		return true;
	}
	
	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(){
	    return false;
    }
	
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int notused0, float notused1, float notused2, float notused3){
		if(ClientProxy.gui == null && FMLCommonHandler.instance().getEffectiveSide().isClient()){
		ClientProxy.gui = new AnimationEditorGui((TileentityAnimationEditorClient) world.getTileEntity(x, y, z));
		//Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		}
		return blockConstructorCalled;
	}
	
	/*@Override
	public void onBlockAdded(World world,int x,int y,int z){
		a
	}*/


}
