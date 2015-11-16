package com.awesomeholden.blocks;

import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.Tabs;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.packets.CallTileentityAnimatedRealConstructor;
import com.awesomeholden.packets.DeleteTileentityAnimated;
import com.awesomeholden.packets.SendCallTileentityAnimatedConstructor;
import com.awesomeholden.packets.SendOnTileentityAnimatedDeleted;
import com.awesomeholden.proxies.ClientProxy;
import com.awesomeholden.proxies.ServerProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnimated extends Block implements ITileEntityProvider{

	public BlockAnimated() {
		super(Material.anvil);
		//needsRandomTick = true;
		this.isBlockContainer = true;
		this.setBlockName("Animated Block");
		this.setCreativeTab(Tabs.Tab);
		
		setHardness(0.7f);
		
		this.setHarvestLevel("pickaxe", 2);
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
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		TileEntity tileEntity;
		if(world.isRemote){
			tileEntity = new TileentityAnimatedClient();
		}else{
			tileEntity = new TileentityAnimatedServer();
		}
		return tileEntity;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		/*for(int ph=0;ph<world.loadedTileEntityList.size();ph++){
			if(   ((TileEntity)(world.loadedTileEntityList.get(ph))).xCoord == x   ){
				//
			}
		}*/
		
		if(ServerProxy.coordsInController(x, y, z) == null){
			world.setBlock(x, y, z, Blocks.air);
			return;
		}
				
		TileentityAnimatedServer tea = (TileentityAnimatedServer)world.getTileEntity(x, y, z);
		
		tea.realConstructor(world);
		Main.network.sendToAllAround(new CallTileentityAnimatedRealConstructor(x,y,z),new TargetPoint(world.provider.dimensionId, x, y, z, 80));
	}
	
	public int onBlockPlacedBy(World world, int x, int y, int z, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_){
		
		/*System.out.println("BLOCK PLACED BY: "+world.isRemote);
		
		if(world.isRemote)
			return 0;
		
		System.out.println("BLOCK PlACED BY");
		
		AnimationControllerServer c = ServerProxy.coordsInController(x, y, z);
				
		if(c == null){
			//world.setBlock(x, y, z, Blocks.air);
			return 0;
		}
		
		ServerProxy.updateControllerFrames(t, c);*/
		
		return 0;
	}
	
	/*@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int notused0, float notused1, float notused2, float notused3){
		return true;
	}*/
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_)
    {
		/*System.out.println("blockDestroyed!");
		if(Main.deleteTileentityAnimated((TileentityAnimated)world.getTileEntity(x, y, z))){
			System.out.println("COULD DELETE BLOCK THE FOLLOWING IS BULLSHIT!");
		}
		else{
			System.out.println("Could not delete TileentityAnimated from CommonProxy.AnimationControllers this is a memory/diskspace leak. Please tell me (The mod author) because this should never happen.");
		}*/
		if(FMLCommonHandler.instance().getEffectiveSide().isClient()){
    		ClientProxy.deleteTileentityAnimated(x,y,z);
    	}else{
    		ServerProxy.deleteTileentityAnimated(x,y,z);
    	}
		//System.out.println("breakBlock: "+FMLCommonHandler.instance().getEffectiveSide().isClient());
    }
	
	@Override
	public void onBlockPreDestroy(World p_149725_1_, int x, int y, int z, int p_149725_5_) {
		ClientProxy.deleteTileentityAnimated(x,y,z);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world,int x,int y,int z,int other){
		if(world.isRemote){
			ClientProxy.deleteTileentityAnimated(x, y, z);
		}else{
			ServerProxy.deleteTileentityAnimated(x, y, z);
		}
	}
	
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		
		//did not call super
		
	}
	
	/*@Override
	public int tickRate(World world){
		return 10;
	}
	
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_, int p_149674_4_, Random p_149674_5_){
	}
	
	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		System.out.println(((Block) blockRegistry.getObject("torch")).getUnlocalizedName());
		return Item.getItemFromBlock((Block) blockRegistry.getObject("torch"));
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return 1;
	}*/
	
}
