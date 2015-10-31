package com.awesomeholden.guis;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import scala.actors.threadpool.Arrays;

import java.util.Collections;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.animationeditorguiwidges.Button;
import com.awesomeholden.guis.animationeditorguiwidges.EditBlockGuiWidget;
import com.awesomeholden.guis.animationeditorguiwidges.TextBox;
import com.awesomeholden.guis.animationeditorguiwidges.TextBoxInput;
import com.awesomeholden.guis.animationeditorguiwidges.TexturedButton;
import com.awesomeholden.items.MItems;
import com.awesomeholden.packets.gui.AddFrameInterval;
import com.awesomeholden.packets.gui.GetFrameIntervals;
import com.awesomeholden.packets.gui.GetFrameByLayerAndFrame;
import com.awesomeholden.packets.gui.SaveControllerToItem;
import com.awesomeholden.packets.gui.SetTexture;
import com.awesomeholden.packets.gui.SetFrameInterval;
import com.awesomeholden.proxies.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class AnimationEditorGui {
	
	public static int max = 20;
	
	public AnimationControllerClient controller;
	
	public List<Integer> frameIntervals = new ArrayList<Integer>();
	public boolean waitTillServerSetsFrameIntervals = true;
		
	//public List<ArrayList<ArrayList<TileentityAnimated>>> organisedBlocks = new ArrayList<ArrayList<ArrayList<TileentityAnimated >  >  > ();
	
	public int[] smallestCoords;
	public int[] biggestCoords;
	public int[] fullCanvasSize;
	public static float[] s = new float[]{ClientProxy.canvasWidth,0.8f}; //size of drawing area for blocks;
	public static float[] fullSize = new float[]{0.8f,0.8f};
	public static int blockNumber;
	public float xOffset;
	public float yFill;
	public float blockSizeX;
	public float blockSizeY;
	
	public static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	public int[] frames = new int[0];
			
	//TileEntityRendererDispatcher ugly = new TileEntityRendererDispatcher();
	
	public int currentLayer = 0;
	public int currentFrame = 0;
	
	public List<TextBoxInput> timelineBottoms = new ArrayList<TextBoxInput>();
	public float timelineBottomSizeX;
	public float timelineBottomSizeY;
	
	
	
	public static float textboxHeight = 0.04f;
	public static float[] selectedBlockSize;
	public static float[] selectedBlockPos = null;
	
	public static TextBoxInput currentTexTextbox = null;
	
	public ResourceLocation currentTex;
		
	public AnimationEditorGui(TileentityAnimationEditorClient tea){
		controller = tea.controller;
		smallestCoords = getSmallestCoords();
		biggestCoords = getBiggestCoords();
		fullCanvasSize = new int[]{biggestCoords[0]-smallestCoords[0],biggestCoords[1]-smallestCoords[1],biggestCoords[2]-smallestCoords[2]};
		s = new float[]{ClientProxy.canvasWidth,0.8f}; //size from center
		blockNumber = 20;
		xOffset = (-s[0])+(s[0]*0.1f);
		yFill = s[1]*0.1f;
		blockSizeX = ( (s[0]*2) - (s[0]-(-xOffset) ) )/blockNumber;
		blockSizeY = ((s[1]*2)-yFill)/blockNumber;
		
		Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
		
		timelineBottomSizeX = (fullSize[0]-s[0])*0.75f;
		timelineBottomSizeY = fullSize[1]/16;
		float extraspace = 0.04f;
		
		offset = fullSize[0]-s[0]-timelineBottomSizeX;
		
		float cache = fullSize[1]-0.08f;
		
		
		
		
		selectedBlockSize = new float[]{(fullSize[0]-s[0])/4,((fullSize[0]-s[0])/4)*(s[1]/s[0])};
		selectedBlockPos = new float[]{(-fullSize[0])+(((fullSize[0]-s[0])-selectedBlockSize[0])/2),(-fullSize[1])+((fullSize[1]-s[1]-selectedBlockSize[1])/2)};
		currentTexTextbox = new TextBoxInput(new float[]{-fullSize[0],(-fullSize[1])+(textboxHeight*2)},new float[]{-s[0],(-fullSize[1])+textboxHeight},"minecraft:textures/items/apple.png","");
		currentTex = new ResourceLocation(currentTexTextbox.text);
		
		Main.network.sendToServer(new GetFrameIntervals(controller.coords)); //this sets this.frameIntervals
				
	}
	
	public void addTimelineBottoms(){	
		
			for(int ph=1;ph<frameIntervals.size();ph++){
				timelineBottoms.add(new TextBoxInput(new float[]{s[0]+offset,cache},new float[]{fullSize[0]-offset,cache-timelineBottomSizeY},frameIntervals.get(ph).toString(),blacklist));
				timelineBottoms.get(ph-1).alignment = 1;
				cache-=timelineBottomSizeY;
				cache-=extraspace;
			}
		
		if(frameIntervals.size()>0){
			timelineBottoms.add(new TextBoxInput(new float[]{s[0]+offset,cache},new float[]{fullSize[0]-offset,cache-timelineBottomSizeY},frameIntervals.get(0).toString(),blacklist));
			timelineBottoms.get(timelineBottoms.size()-1).alignment = 1;
			cache-=timelineBottomSizeY;
			cache-=extraspace;
		}
		
	}
	
	public int[] getSmallestCoords(){
		int[] cache = new int[]{2147483647,2147483647,2147483647};
		int[] currentCache = new int[]{2147483647,2147483647,2147483647};
		for(int ph=0;ph<controller.theControlled.size();ph++){
			TileentityAnimatedClient te = controller.theControlled.get(ph);
			currentCache[0] = controller.theControlled.get(ph).xCoord;
			if(currentCache[0]<cache[0]) cache[0] = currentCache[0];
			currentCache[1] = controller.theControlled.get(ph).yCoord;
			if(currentCache[1]<cache[1]) cache[1] = currentCache[1];
			currentCache[2] = controller.theControlled.get(ph).zCoord;
			if(currentCache[2]<cache[2]) cache[2] = currentCache[2];
			
		}
		return cache;
	}
	
	public int[] getBiggestCoords(){
		int[] cache = new int[3];
		int[] currentCache = new int[3];
		for(int ph=0;ph<controller.theControlled.size();ph++){
			currentCache[0] = controller.theControlled.get(ph).xCoord;
			if(currentCache[0]>cache[0]) cache[0] = currentCache[0];
			currentCache[1] = controller.theControlled.get(ph).yCoord;
			if(currentCache[1]>cache[1]) cache[1] = currentCache[1];
			currentCache[2] = controller.theControlled.get(ph).zCoord;
			if(currentCache[2]>cache[2]) cache[2] = currentCache[2];
		}
		return cache;
	}
	
	public TileentityAnimatedClient lookUp(int x,int y,int z){
		for(int ph=0;ph<controller.theControlled.size();ph++){
			TileentityAnimatedClient t = controller.theControlled.get(ph);
			if(x == t.xCoord && y == t.yCoord && z == t.zCoord){
				return t;
			}
		}
		return null;
	}
	int previousSize = 0;
	float right = -(fullSize[0]-(fullSize[0]-s[0]));
	public void render(){
		if(controller.theControlled.size() != previousSize){
			smallestCoords = getSmallestCoords();
			biggestCoords = getBiggestCoords();
		}
		previousSize = controller.theControlled.size();
		Mouse.getDX(); //These stop minecraft from rotating the world around you.
		Mouse.getDY();
		s[0] = ClientProxy.canvasWidth;
		//xOffset = (-s[0])+(s[0]*0.1f);
		//yFill = s[1]*0.1f;
		blockSizeX = (s[0]*2)/blockNumber;
		blockSizeY = (s[1]*2)/blockNumber;
		//GL11.glPushMatrix();
		
			/*GL11.glDisable(GL11.GL_LIGHTING);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 0);*/
		
		/*GL11.glPushMatrix();
        	GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        	GL11.glDisable(GL11.GL_LIGHTING);
        	GL11.glDisable(GL11.GL_TEXTURE_2D);
        	GL11.glEnable(GL11.GL_BLEND);
        	OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 0);*/
					
	    //GL11.glPopMatrix();
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPushMatrix();
			GL11.glLoadIdentity(); //minecraft has it set so that x is just like y. So that blocks can be 1x1 and look like blocks not weird rectangles
			GL11.glOrtho(-1, 1, -1, 1, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);
	    	GL11.glDisable(GL11.GL_LIGHTING);
	    	GL11.glDisable(GL11.GL_TEXTURE_2D);
	    	GL11.glEnable(GL11.GL_BLEND); //Transparency
        	
	        	GL11.glColor4f(0,0,0,0.5f);
				
	        	GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(fullSize[0], fullSize[1]);
				GL11.glVertex2f(-fullSize[0], fullSize[1]);
				GL11.glVertex2f(-fullSize[0], -fullSize[1]);
				GL11.glVertex2f(fullSize[0], -fullSize[1]);
				GL11.glEnd();
        
				GL11.glColor4f(1,1,1,1);
				
				editLayers();
				drawTimeline();

				drawBlocks();
				
				if(selectedBlock != null){
					drawEditBlock();
				}
				
				drawTools();
				
				drawCursor();
				
				
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			GL11.glPopAttrib();
			
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        	GL11.glDisable(GL11.GL_LIGHTING);
        	GL11.glEnable(GL11.GL_BLEND); //Transparency
        	
        	GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			
			
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();
			GL11.glPopAttrib();
			
			
			GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_BLEND);
	}
	
	public int previousEventButton = 0;
	public void drawBlocks(){
		
		float[] coords = new float[2];
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		int onLayerPh = 0;
		for(int ph=0;ph<controller.theControlled.size();ph++){
			TileentityAnimatedClient te = controller.theControlled.get(ph);
			//System.out.println("frames.length: "+frames.length);
			//System.out.println("onLayerPH: "+onLayerPh+" frames.length: "+frames.length+" currentLayer: "+currentLayer+" subtract: "+(te.yCoord-smallestCoords[1]));
			if(currentLayer != te.yCoord-smallestCoords[1] || onLayerPh == frames.length) continue;
			
			coords[0] = ((te.xCoord-smallestCoords[0])*blockSizeX)-s[0];
			coords[1] = s[1]-((te.zCoord-smallestCoords[2])*blockSizeY);
			
			//System.out.println("xCoord: "+Math.abs(te.xCoord)+" subtract: "+Math.abs(smallestCoords[0]));
			//System.out.println("zCoord: "+Math.abs(te.zCoord)+" subtract: "+Math.abs(smallestCoords[2]));
			
			float[] mousePos = ClientProxy.mousePos;
			
			//System.out.println("BEBE "+mousePos[0]+','+mousePos[1]);
			
			GL11.glColor4f(1,1,1,1);
			textureManager.bindTexture((ResourceLocation) TileentityAnimatedClient.textures.get(frames[onLayerPh]));
			GL11.glBegin(GL11.GL_QUADS);
			Main.draw(coords[0]+blockSizeX,coords[1]);
			Main.draw(coords[0],coords[1]);
			Main.draw(coords[0], coords[1]-blockSizeY);
			Main.draw(coords[0]+blockSizeX, coords[1]-blockSizeY);
			GL11.glEnd();
			
			if(mousePos[0]>=coords[0] && mousePos[0]<=coords[0]+blockSizeX && mousePos[1]<=coords[1] && mousePos[1]>=coords[1]-blockSizeY){
				if(Mouse.getEventButton() == 1 && previousEventButton != 1){ //right click
															
					rightClickBlock(te);
					selectedBlock = te;
					String path = "none";
					//ResourceLocation location = TileentityAnimatedServer.textures.get(selectedBlock.frames.get(currentFrame));
					//path = location.getResourceDomain()+':'+location.getResourcePath();
					textureBox = new TextBoxInput(new float[]{-0.8f,-0.4f},new float[]{-0.6f,-0.44f},path,"");
					locationBox = new TextBox(new float[]{-0.8f,-0.4f},new float[]{-0.6f,-0.44f},"location:");
					/*if(selectedBlock == null){
						System.out.println("new EditBlockGuiWidget");
						selectedBlock = new EditBlockGuiWidget(this,te);
					}*//*else{
						System.out.println("removed EditBlockGuiWidget");
						selectedBlock = null;
					}*/
				}
				previousEventButton = Mouse.getEventButton();
				
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glBegin(GL11.GL_LINE_LOOP);
				GL11.glVertex2f(coords[0]+blockSizeX,coords[1]);
				GL11.glVertex2f(coords[0],coords[1]);
				GL11.glVertex2f(coords[0], coords[1]-blockSizeY);
				GL11.glVertex2f(coords[0]+blockSizeX, coords[1]-blockSizeY);
				GL11.glEnd();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
			
			onLayerPh++;                 
			//textureManager.bindTexture(texture);
			//System.out.println(texture.getResourcePath());
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	//private static ResourceLocation cursorTexture = new ResourceLocation(Main.MODID+":/textures/misc/cursor.png");
	public static String blacklist = "abcdefghijklmnopqrstuvwxyz"+"abcdefghijklmnopqrstuvwxyz".toUpperCase();
	
	public TextBoxInput currentLayerBox = new TextBoxInput(new float[]{0,0},new float[]{-2,2},"layer: "+currentLayer,blacklist);
	public TextBox outofBox = new TextBox(new float[]{0,0},new float[]{-2,2},"");
	
	public void editLayers(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float right = -(fullSize[0]-(fullSize[0]-s[0]));
		GL11.glColor4f(1,1,1,1);
		/*if(currentLayerBox.text.length() < 6){
			currentLayerBox.text = "layer: "+fullGui.currentLayer;
		}*/
		if(currentLayerBox.text.length() < 7){
			currentLayerBox.text = "layer: ";
		}
		currentLayerBox.updateCoords(new float[]{-fullSize[0], 0.09f},new float[]{right,0.05f});
		currentLayerBox.render();
		if(currentLayerBox.entered){
			String string = currentLayerBox.text.substring(7, currentLayerBox.text.length());
			if(string.length()>0) currentLayer = Integer.parseInt(string);
			Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
			currentLayerBox.entered = false;
		}
		outofBox.text = "/"+(biggestCoords[1]-smallestCoords[1]);
		outofBox.updateCoords(new float[]{-fullSize[0], 0.04f},new float[]{right,0});
		outofBox.render();
		
		
	}
	
	
	
	private static float mouseSize = 0.04f;
	private static float[] mouseColor = new float[]{1,0,0};
	private int colorIndex = 1;
	public void drawCursor(){		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float[] mousePos = ClientProxy.mousePos;
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(mouseColor[0],mouseColor[1],mouseColor[1],1);
		GL11.glVertex2f(mousePos[0]+mouseSize,mousePos[1]);
		GL11.glVertex2f(mousePos[0],mousePos[1]);
		GL11.glVertex2f(mousePos[0],mousePos[1]-mouseSize);
		GL11.glVertex2f(mousePos[0],mousePos[1]-mouseSize);
		GL11.glEnd();
		
		mouseColor[colorIndex-1]-=0.004;
		mouseColor[colorIndex]+=0.004;
		if(mouseColor[colorIndex]>=1){
			if(colorIndex==2){
				mouseColor[0] = 1;
				mouseColor[1] = 0;
				mouseColor[2] = 0;
				colorIndex = 1;
			}else{
				colorIndex++;
			}
		}
	}
	
		
	/*float timelineBottomSizeX1 = (fullSize[0]-s[0])*0.75f;
	float timelineBottomSizeY1 = fullSize[1]/16;*/
	public int viewPos = 0;

	public float extraspace = 0.04f;

	public float offset = fullSize[0]-s[0]-timelineBottomSizeX;
	public TextBoxInput frameBox = new TextBoxInput(new float[]{0,0},new float[]{-2,2},"frame: "+currentFrame,blacklist);
	float cache = fullSize[0]-0.08f;
	float[] mousePos;
	float mouseInScrollBarY=0;
	int previousEvent = -1;
	float previousMouseY = 0;
	boolean usedToBeDown = false;
	public void drawTimeline(){
		
		if(frameBox.text.length() < 7){
			frameBox.text = "frame: ";
		}
		frameBox.updateCoords(new float[]{s[0], fullSize[1]},new float[]{fullSize[0],fullSize[1]-0.04f});
		if(frameBox.entered){
			String string = frameBox.text.substring(7, frameBox.text.length());
			if(string.length()>0){
				int integer = Integer.parseInt(string);
				if(integer < frameIntervals.size()){
					currentFrame = integer;
					Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
				}
			}
			frameBox.entered = false;
		}
		frameBox.render();
		
		float posY = frameBox.coordsBottom[1]-0.04f;
		float[] hold;
		float[] hold2;
		cache = fullSize[0]-0.08f;
		for(int ph=0;ph<timelineBottoms.size();ph++){
			if(ph<viewPos) continue;
			TextBoxInput tb = timelineBottoms.get(ph);
			if(tb.entered){
				try{
					int cint = Integer.parseInt(tb.text);
					int i = ph+1;
					if(i==frameIntervals.size())
						i = 0;
					frameIntervals.set(ph, cint);
					Main.network.sendToServer(new SetFrameInterval(controller.coords, i,cint));
				}catch(NumberFormatException e){}
				tb.entered = false;
			}
			hold = new float[]{tb.coordsTop[0],tb.coordsTop[1]};
			hold2 = new float[]{tb.coordsBottom[0],tb.coordsBottom[1]};
			//System.out.println("Coords1: "+tb.coordsTop+" , "+tb.coor)
			tb.coordsTop[1] += viewPos*(timelineBottomSizeY+extraspace);
			tb.coordsBottom[1] = tb.coordsTop[1]-timelineBottomSizeY;
			if(tb.coordsBottom[1]<-fullSize[1]+0.16){
				tb.coordsTop = hold;
				tb.coordsBottom = hold2;
				break;
			}
			tb.updateSpot();
			tb.render();
			tb.coordsTop = hold;
			tb.coordsBottom = hold2;
		}
		
		
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(fullSize[0], -fullSize[1]+0.16);
		GL11.glVertex2d(s[0], -fullSize[1]+0.16);
		GL11.glVertex2d(s[0], -fullSize[1]);
		GL11.glVertex2d(fullSize[0], -fullSize[1]);
		
		mousePos = ClientProxy.mousePos;
		if(Mouse.getEventButton() == 1 && previousEvent != 1){
			
			if(mousePos[0]>=s[0] && mousePos[0]<=fullSize[0] && mousePos[1]<=-fullSize[1]+0.16 && mousePos[1]>=-fullSize[1]){
				Main.network.sendToServer(new AddFrameInterval(controller.coords,20));
			frameIntervals.add(20);
			/*for(int ph=0;ph<controller.theControlled.size();ph++){
				TileentityAnimatedClient te = controller.theControlled.get(ph);
				//te.doFrames.add(false);
			}*/
			timelineBottoms.add(new TextBoxInput(new float[]{s[0]+offset,timelineBottoms.get(timelineBottoms.size()-1).coordsBottom[1]-extraspace},new float[]{fullSize[0]-offset,timelineBottoms.get(timelineBottoms.size()-1).coordsBottom[1]-extraspace-timelineBottomSizeY},"20",blacklist));
			timelineBottoms.get(timelineBottoms.size()-1).alignment = 1;
			
			
			TextBoxInput tmp = timelineBottoms.get(timelineBottoms.size()-1);
			//System.out.println("ADDED: ["+tmp.coordsTop[0]+','+tmp.coordsTop[1]+"] , ["+tmp.coordsBottom[0]+','+tmp.coordsBottom[1]+']');
			}
		}
		previousEvent = Mouse.getEventButton();
		
		if(Mouse.isButtonDown(1) && !usedToBeDown){
			if(mousePos[0]>=fullSize[0]-0.04 && mousePos[0]<=fullSize[0] && mousePos[1]<=-fullSize[1]+0.16+0.06 && mousePos[1]>=-fullSize[1]+0.16){
				if(viewPos>0) viewPos--;
			}
			if(mousePos[0]>=fullSize[0]-0.04 && mousePos[0]<=fullSize[0] && mousePos[1]<=fullSize[1]-0.04 && mousePos[1]>=fullSize[1]-0.04-0.06){
				if(viewPos<timelineBottoms.size()) viewPos++;
			}
			usedToBeDown = true;
		}
		if(!Mouse.getEventButtonState()){
			usedToBeDown = false;
		}
		
		GL11.glColor3d(0.2,1,0.2);
		GL11.glVertex2d(fullSize[0], -fullSize[1]+0.16+0.06);
		GL11.glVertex2d(fullSize[0]-0.04, -fullSize[1]+0.16+0.06);
		GL11.glVertex2d(fullSize[0]-0.04, -fullSize[1]+0.16);
		GL11.glVertex2d(fullSize[0], -fullSize[1]+0.16);
		
		GL11.glVertex2d(fullSize[0], fullSize[1]-0.04);
		GL11.glVertex2d(fullSize[0]-0.04, fullSize[1]-0.04);
		GL11.glVertex2d(fullSize[0]-0.04, fullSize[1]-0.04-0.06);
		GL11.glVertex2d(fullSize[0], fullSize[1]-0.04-0.06);
		
		/*double tmppos = (fullSize[1]-0.16)/timelineBottoms.size();
		double tmppos2 = viewPos*tmppos;
		GL11.glVertex2d(fullSize[0], tmppos2);
		GL11.glVertex2d(fullSize[0]-0.16, tmppos2);
		GL11.glVertex2d(fullSize[0]-0.16, tmppos2-tmppos);
		GL11.glVertex2d(fullSize[0], tmppos2-tmppos);*/
		
		/*if(Mouse.isButtonDown(1)){
			if(mousePos[0]>=fullSize[0]-0.16 && mousePos[0]<=fullSize[0]){
				if(previousMouseY != mousePos[1]){
					float dif = previousMouseY-mousePos[1];
					//System.out.println("DIF "+dif);
					mouseInScrollBarY+=dif;
					viewPos = (int)(mouseInScrollBarY/(((fullSize[1]*2)-0.2)/timelineBottoms.size()));
					System.out.println("MOUSE: "+mouseInScrollBarY);
				}
			}
		}*/
		previousMouseY = mousePos[1];
		
		GL11.glEnd();
	}
	
	
	public TileentityAnimatedClient selectedBlock = null;
		
	public TextBoxInput textureBox;
	public TextBox locationBox;
	
	public void drawEditBlock(){
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0, 0, 0.5f, 0.5f);
		GL11.glVertex2f(-s[0],0);
		GL11.glVertex2f(-fullSize[0],0);
		GL11.glVertex2f(-fullSize[0],-fullSize[1]);
		GL11.glVertex2f(-s[0],-fullSize[1]);
		
		float subLeft = -fullSize[0]+0.005f;
		float subRight = subLeft+(s[0]/5);
		float subBottom = -((s[1]/5)+0.005f);
		GL11.glColor3f(0,0,0);
		GL11.glVertex2f(subRight,-0.005f);
		GL11.glVertex2f(subLeft,-0.005f);
		GL11.glVertex2f(subLeft, subBottom);
		GL11.glVertex2f(subRight,subBottom);
	
		GL11.glEnd();
		
		locationBox.updateCoords(new float[]{-s[0],subBottom-0.01f},new float[]{right-0.005f,subBottom-0.05f});
		locationBox.render();
		/*if(textBox.entered){
			
		}*/
		if(textureBox.entered){
			Main.network.sendToServer(new SetTexture(textureBox.text, controller.theControlled.indexOf(selectedBlock), controller.coords, currentFrame));
			textureBox.entered = false;
		}
		textureBox.updateCoords(new float[]{subLeft, locationBox.coordsBottom[1]-0.02f},new float[]{locationBox.coordsBottom[0],locationBox.coordsBottom[1]-0.06f});
		textureBox.render();
			
		GL11.glColor3f(1,1,1);
						
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		textureManager.bindTexture(selectedBlock.texture);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1, 1, 1);
			Main.draw(subRight-0.01f,-0.015f);
			Main.draw(subLeft+0.01f,-0.015f);
			Main.draw(subLeft+0.01f, subBottom+0.01f);
			Main.draw(subRight-0.01f,subBottom+0.01f);
		GL11.glEnd();
			
	}
	
	//public Button buttonGetCopy = new Button(new float[]{-fullSize[0],fullSize[1]}, new float[]{-(fullSize[0])+0.15f,fullSize[1]-0.04f}, "get copy");
	
	boolean wasSelected = false;
	
	public void drawTools(){
		
		selectedBlockSize = new float[]{(fullSize[0]-s[0])/4,((fullSize[0]-s[0])/4)*(s[1]/s[0])};
		selectedBlockPos = new float[]{(-fullSize[0])+(((fullSize[0]-s[0])-selectedBlockSize[0])/2),currentTexTextbox.coordsTop[1]+selectedBlockSize[1]};
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-s[0], 0);
		GL11.glVertex2f(-fullSize[0], 0);
		GL11.glVertex2f(-fullSize[0], -s[1]);
		GL11.glVertex2f(-s[0], -s[1]);
		GL11.glEnd();
		
		/*System.out.println("SELECTEDBLOCKPOS: "+Arrays.toString(selectedBlockPos));
		System.out.println("SELECTEDBLOCKSIZE: "+Arrays.toString(selectedBlockSize));
		System.out.println("DIVIDED: "+(s[1]/s[0]));
		System.out.println("ALSO DIVIDED: "+((fullSize[0]-s[0])/4));
		System.out.println("ALSO AGINA: "+((fullSize[0]-s[0])/4)*(s[1]/s[0]));*/
		/*System.out.println("FIRST: "+selectedBlockPos[0]+", "+selectedBlockPos[1]);
		System.out.println("SECOND: "+(selectedBlockPos[0]+selectedBlockSize[0])+", "+selectedBlockPos[1]);
		System.out.println("THIRD: "+(selectedBlockPos[0]+selectedBlockSize[0])+", "+(selectedBlockPos[1]-selectedBlockSize[1]));
		System.out.println("THIRD: "+selectedBlockPos[0]+", "+(selectedBlockPos[1]-selectedBlockSize[1]));*/
		
		currentTexTextbox.updateCoords(new float[]{-fullSize[0],(-fullSize[1])+(textboxHeight*2)},new float[]{-s[0],(-fullSize[1])+textboxHeight});
		currentTexTextbox.render();
		
		/*if(currentTexTextbox.active){
			if(!wasSelected){
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat("/"));
				wasSelected = true;
				currentTexTextbox.active = true;
			}
				
		}else{
			wasSelected = false;
		}*/
		
		if(currentTexTextbox.entered){
			//currentTex = new ResourceLocation(currentTexTextbox.text);
			currentTex = new ResourceLocation(currentTexTextbox.text);
			currentTexTextbox.entered = false;
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(1, 1, 1,1);
		
		try{
			textureManager.bindTexture(currentTex);
		}catch(ReportedException e){
			currentTex = new ResourceLocation("minecraft:textures");
			textureManager.bindTexture(currentTex);
		}
		GL11.glBegin(GL11.GL_QUADS);
		Main.draw(selectedBlockPos[0]+selectedBlockSize[0], selectedBlockPos[1]);
		Main.draw(selectedBlockPos[0], selectedBlockPos[1]);
		Main.draw(selectedBlockPos[0], selectedBlockPos[1]-selectedBlockSize[1]);
		Main.draw(selectedBlockPos[0]+selectedBlockSize[0], selectedBlockPos[1]-selectedBlockSize[1]);
		GL11.glEnd();
	}
	
	public void rightClickBlock(TileentityAnimatedClient te) {
			Main.network.sendToServer(new SetTexture(currentTexTextbox.text, controller.theControlled.indexOf(te), controller.coords, currentFrame));
	}
		
}
