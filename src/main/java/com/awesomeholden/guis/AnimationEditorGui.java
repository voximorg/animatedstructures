package com.awesomeholden.guis;

import java.util.List;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.controllers.AnimationControllerClient;
import com.awesomeholden.controllers.AnimationControllerServer;
import com.awesomeholden.guis.animationeditorguiwidges.BasicButton;
import com.awesomeholden.guis.animationeditorguiwidges.Scrollbar;
import com.awesomeholden.guis.animationeditorguiwidges.TextBox;
import com.awesomeholden.guis.animationeditorguiwidges.TextBoxInput;
import com.awesomeholden.guis.tool.Tool;
import com.awesomeholden.guis.tool.ToolEraser;
import com.awesomeholden.guis.tool.ToolPencil;
import com.awesomeholden.items.MItems;
import com.awesomeholden.packets.gui.AddFrameInterval;
import com.awesomeholden.packets.gui.DeleteFrame;
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
	
	public static int button = 0;
	
	public List<Tool> tools = new ArrayList<Tool>();
	
	public static int max = 20;
	
	public static AnimationControllerClient controller;
	
	public static List<Integer> frameIntervals;
		
	//public List<ArrayList<ArrayList<TileentityAnimated>>> organisedBlocks = new ArrayList<ArrayList<ArrayList<TileentityAnimated >  >  > ();
	
	public static int[] smallestCoords;
	public static int[] biggestCoords;
	public static float[] s = new float[]{ClientProxy.canvasWidth,0.8f}; //size of drawing area for blocks;
	public static float[] fullSize = new float[]{0.8f,0.8f};
	public static int blockNumber;
	public float xOffset;
	public float yFill;
	public float blockSizeX;
	public float blockSizeY;
	
	public static TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
	public static int[] frames = new int[0];
	public int[] ghostFrames = new int[0];
			
	//TileEntityRendererDispatcher ugly = new TileEntityRendererDispatcher();
	
	public static int currentLayer = 0;
	public static int currentFrame;
	
	public List<TextBoxInput> timelineBottoms; //I meant buttons...
	public float timelineBottomSizeX;
	public float timelineBottomSizeY;
	
	
	
	public static float textboxHeight = 0.04f;
	public static float[] selectedBlockSize;
	public static float[] selectedBlockPos = null;
	
	public static TextBoxInput currentTexTextbox = null;
	
	public ResourceLocation currentTex;
		
	public AnimationEditorGui(TileentityAnimationEditorClient tea){
		outofBox.alignment = 2;
		
		frameIntervals = new ArrayList<Integer>();
		timelineBottoms = new ArrayList<TextBoxInput>();
		
		currentFrame = 0;
		currentLayer = 0;
		
		frameBox.text = "frame: 0";
		
		
		
		frames = new int[0];
		ghostFrames = new int[0];
		
		controller = tea.controller;
		smallestCoords = getSmallestCoords();
		biggestCoords = getBiggestCoords();
		s = new float[]{ClientProxy.canvasWidth,0.8f}; //size from center
		blockNumber = 20;
		xOffset = (-s[0])+(s[0]*0.1f);
		yFill = s[1]*0.1f;
		blockSizeX = ( (s[0]*2) - (s[0]-(-xOffset) ) )/blockNumber;
		blockSizeY = ((s[1]*2)-yFill)/blockNumber;
		
		timelineBottomSizeX = (fullSize[0]-s[0])*0.75f;
		timelineBottomSizeY = fullSize[1]/16;
		float extraspace = 0.04f;
		
		offset = fullSize[0]-s[0]-timelineBottomSizeX;
		
		float cache = fullSize[1]-0.08f;
		
		
		
		
		selectedBlockSize = new float[]{(fullSize[0]-s[0])/4,((fullSize[0]-s[0])/4)*(s[1]/s[0])};
		selectedBlockPos = new float[]{(-fullSize[0])+(((fullSize[0]-s[0])-selectedBlockSize[0])/2),(-fullSize[1])+((fullSize[1]-s[1]-selectedBlockSize[1])/2)};
		currentTexTextbox = new TextBoxInput(new float[]{-fullSize[0],(-fullSize[1])+(textboxHeight*2)},new float[]{-s[0],(-fullSize[1])+textboxHeight},"minecraft:textures/items/apple.png","");
		currentTex = new ResourceLocation(currentTexTextbox.text);
		
		currentTool = new ToolPencil();
		 
		tools.add(currentTool);
		tools.add(new ToolEraser());
		
		Main.network.sendToServer(new GetFrameIntervals(controller.coords)); //this sets this.frameIntervals
		
		Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
				
	}
	
	public static float xToY(float x){
		return (s[1]/s[0])*x;
	}
	
	public void addTimelineBottoms(){	
		
		int size = frameIntervals.size();
		
			for(int ph=0;ph<size;ph++){
				timelineBottoms.add(new TextBoxInput(new float[]{s[0]+offset,cache},new float[]{fullSize[0]-offset,cache-timelineBottomSizeY},frameIntervals.get(ph).toString(),blacklist));
				timelineBottoms.get(ph).alignment = 1;
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
	
	public float[] mousePos;
	int previousSize = 0;
	float right = -(fullSize[0]-(fullSize[0]-s[0]));
	
	public static int eventButton = 0;
		
	public static boolean newEventButton = false;
	public void render(){		
		mousePos = ClientProxy.mousePos;
				
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
			GL11.glLoadIdentity();
			GL11.glOrtho(-1, 1, -1, 1, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT | GL11.GL_TEXTURE_BIT);
	    	GL11.glDisable(GL11.GL_LIGHTING);
	    	GL11.glDisable(GL11.GL_TEXTURE_2D);
	    	GL11.glEnable(GL11.GL_BLEND);
        	
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
				
				drawEditBlock();
				
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
	        
	        newEventButton = false;
	        
	        ClientLoop.leftUp = false;
	}
	
	public static TileentityAnimatedClient hoverAnimated;
	
	public void drawBlocks(){
		
		float[] coords = new float[2];
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		int onLayerPh = 0;
		boolean inAnimated = false;
		for(int ph=0;ph<controller.theControlled.size();ph++){
			TileentityAnimatedClient te = controller.theControlled.get(ph);
			//System.out.println("frames.length: "+frames.length);
			//System.out.println("onLayerPH: "+onLayerPh+" frames.length: "+frames.length+" currentLayer: "+currentLayer+" subtract: "+(te.yCoord-smallestCoords[1]));
			if(currentLayer != te.yCoord-smallestCoords[1] || onLayerPh == frames.length) continue;
			
			coords[0] = ((te.xCoord-smallestCoords[0])*blockSizeX)-s[0];
			coords[1] = s[1]-((te.zCoord-smallestCoords[2])*blockSizeY);
			
			//System.out.println("xCoord: "+Math.abs(te.xCoord)+" subtract: "+Math.abs(smallestCoords[0]));
			//System.out.println("zCoord: "+Math.abs(te.zCoord)+" subtract: "+Math.abs(smallestCoords[2]));
			
			//System.out.println("BEBE "+mousePos[0]+','+mousePos[1]);
			
			GL11.glColor4f(1,1,1,0.6f);
			textureManager.bindTexture((ResourceLocation) TileentityAnimatedClient.textures.get(ghostFrames[onLayerPh]));
			GL11.glBegin(GL11.GL_QUADS);
			Main.draw(coords[0]+blockSizeX,coords[1]);
			Main.draw(coords[0],coords[1]);
			Main.draw(coords[0], coords[1]-blockSizeY);
			Main.draw(coords[0]+blockSizeX, coords[1]-blockSizeY);
			GL11.glEnd();
			
			
			GL11.glColor4f(1,1,1,1);
			textureManager.bindTexture((ResourceLocation) TileentityAnimatedClient.textures.get(frames[onLayerPh]));
			GL11.glBegin(GL11.GL_QUADS);
			Main.draw(coords[0]+blockSizeX,coords[1]);
			Main.draw(coords[0],coords[1]);
			Main.draw(coords[0], coords[1]-blockSizeY);
			Main.draw(coords[0]+blockSizeX, coords[1]-blockSizeY);
			GL11.glEnd();
			
			if(mousePos[0]>=coords[0] && mousePos[0]<=coords[0]+blockSizeX && mousePos[1]<=coords[1] && mousePos[1]>=coords[1]-blockSizeY){
				
				hoverAnimated = te;
				inAnimated = true;
				
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
		
		if(!inAnimated)
			hoverAnimated = null;
		
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
	
	
	
	public void drawCursor(){		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		if(currentTool != null){
			GL11.glTranslatef(mousePos[0],mousePos[1],0);
			currentTool.drawAsCursor();
			GL11.glTranslatef(-mousePos[0],-mousePos[1],0);
			
			return;
		}
		
	}
	
		
	/*float timelineBottomSizeX1 = (fullSize[0]-s[0])*0.75f;
	float timelineBottomSizeY1 = fullSize[1]/16;*/
	public int viewPos = 0;

	public float extraspace = 0.04f;

	public float offset = fullSize[0]-s[0]-timelineBottomSizeX;
	public TextBoxInput frameBox = new TextBoxInput(new float[]{0,0},new float[]{-2,2},"frame: "+currentFrame,blacklist);
	public BasicButton buttonNewFrame = new BasicButton(new float[]{0,0},new float[]{0,0},new ResourceLocation("animatedstructures:textures/buttons/add_frame.png"));
	public BasicButton buttonDeleteFrame = new BasicButton(new float[]{0,0},new float[]{0,0}, new ResourceLocation("animatedstructures:textures/buttons/delete_frame.png"));
	public BasicButton buttonScrollDown = new BasicButton(new float[]{0,0},new float[]{0,0},new ResourceLocation("animatedstructures:textures/buttons/scroll_down.png"));
	public BasicButton buttonScrollUp = new BasicButton(new float[]{0,0},new float[]{0,0},new ResourceLocation("animatedstructures:textures/buttons/scroll_up.png"));
	
	public Scrollbar barTimeline = new Scrollbar(new float[]{0,0},new float[]{0,0},new float[]{0.07058823529f,0.69411764705f,0.90196078431f,1},new float[]{0.11372549019f,0.62745098039f,0.8f,1},0);
	public int prevBarPos = 0;
	float cache = fullSize[0]-0.08f;
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
					
					for(int i=0;i<timelineBottoms.size();i++)
						if(timelineBottoms.get(i).active)
							timelineBottoms.get(i).active = false;
					
					timelineBottoms.get(integer).active = true;
					
					int i = integer-5;
					
					if(i>-1){
						viewPos = i;
						barTimeline.barPos = i;
					}
					
					Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
				}
			}
			frameBox.entered = false;
		}
		frameBox.render();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		buttonNewFrame.updateCoords(new float[]{frameBox.coordsTop[0], -fullSize[1]+0.16f+0.06f},new float[]{frameBox.coordsBottom[0],-fullSize[1]});
		
		buttonNewFrame.update();
		
		if(buttonNewFrame.pressed){
			int i = frameIntervals.size()-5;
			if(i>(-1)){
				viewPos = i;
				barTimeline.barPos = i;
			}
						
			Main.network.sendToServer(new AddFrameInterval(controller.coords,10));
			
			frameIntervals.add(10);
			/*for(int ph=0;ph<controller.theControlled.size();ph++){
				TileentityAnimatedClient te = controller.theControlled.get(ph);
				//te.doFrames.add(false);
			}*/
						
			timelineBottoms.add(new TextBoxInput(new float[]{s[0]+offset,cache},new float[]{fullSize[0]-offset,cache-timelineBottomSizeY},Integer.toString(10),blacklist));
			
			timelineBottoms.get(timelineBottoms.size()-1).alignment = 1;
			cache-=timelineBottomSizeY;
			cache-=extraspace;
			
			buttonNewFrame.pressed = false;
		}
		
		float y = xToY(0.06f);
		
		buttonDeleteFrame.updateCoords(new float[]{buttonNewFrame.coordsTop[0]-0.06f,buttonNewFrame.coordsBottom[1]+y},new float[]{buttonNewFrame.coordsTop[0],buttonNewFrame.coordsBottom[1]});
		
		buttonDeleteFrame.update();
		
		if(buttonDeleteFrame.pressed){
			buttonDeleteFrame.pressed = false;
			
			if(frameIntervals.size()>1){
			for(int i=0;i<timelineBottoms.size();i++){
				TextBoxInput t = timelineBottoms.get(i);
				
				if(t.active){
					Main.network.sendToServer(new DeleteFrame(controller.coords,i));
										
					frameIntervals.remove(i);
					timelineBottoms.remove(i);
					
					currentFrame = 0;
					
					cache = fullSize[1]-0.08f;
					
					timelineBottoms.clear();
					
					addTimelineBottoms();
					
					Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
					break;
				}
			}
			}
			
		}
		
		
		
		buttonScrollUp.updateCoords(new float[]{fullSize[0]-0.04f, frameBox.coordsBottom[1]},new float[]{fullSize[0],frameBox.coordsBottom[1]-xToY(0.04f)});
		
		buttonScrollDown.updateCoords(new float[]{fullSize[0]-0.04f,buttonNewFrame.coordsTop[1]+xToY(0.04f)}, new float[]{fullSize[0],buttonNewFrame.coordsTop[1]});
		
		buttonScrollUp.update();
		buttonScrollDown.update();
		
		if(buttonScrollUp.pressed){
			if(viewPos>0){
				viewPos--;
				barTimeline.barPos--;
			}
			
			buttonScrollUp.pressed = false;
		}else if(buttonScrollDown.pressed){
			if(viewPos<timelineBottoms.size()){
				viewPos++;
				barTimeline.barPos++;
			}
			buttonScrollDown.pressed = false;
		}
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		barTimeline.updateCoords(new float[]{buttonScrollUp.coordsTop[0], buttonScrollUp.coordsBottom[1]},new float[]{buttonScrollDown.coordsBottom[0],buttonScrollDown.coordsTop[1]}, frameIntervals.size());
		
		barTimeline.update();
		
		if(barTimeline.barPos != prevBarPos){
			viewPos = barTimeline.barPos;
		}
			
			
		prevBarPos = barTimeline.barPos;
		
		
		float posY = frameBox.coordsBottom[1]-0.04f;
		float[] hold;
		float[] hold2;
		for(int ph=0;ph<timelineBottoms.size();ph++){
						
			if(ph<viewPos) continue;
			TextBoxInput tb = timelineBottoms.get(ph);
			
			
			hold = new float[]{tb.coordsTop[0],tb.coordsTop[1]};
			hold2 = new float[]{tb.coordsBottom[0],tb.coordsBottom[1]};
			//System.out.println("Coords1: "+tb.coordsTop+" , "+tb.coor)
			tb.coordsTop[1] += viewPos*(timelineBottomSizeY+extraspace);
			tb.coordsBottom[1] = tb.coordsTop[1]-timelineBottomSizeY;
			if(tb.coordsBottom[1]<buttonNewFrame.coordsTop[1]){
				tb.coordsTop = hold;
				tb.coordsBottom = hold2;
				break;
			}
			tb.updateSpot();
			tb.render();
			tb.coordsTop = hold;
			tb.coordsBottom = hold2;
			
			
			if(tb.entered){
								
				try{
					int cint = Integer.parseInt(tb.text);
					
					frameIntervals.set(ph, cint);
					
					Main.network.sendToServer(new SetFrameInterval(controller.coords, ph,cint));
										
				}catch(NumberFormatException e){ System.out.println("BAD exception"); }
				
				currentFrame = ph;
				
				frameBox.text = "frame: "+ph;
				
				Main.network.sendToServer(new GetFrameByLayerAndFrame(smallestCoords[1],currentFrame,currentLayer,controller.coords));
				tb.entered = false;
				
			}
		}
		
		
		
		/*GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(fullSize[0], -fullSize[1]+0.16);
		GL11.glVertex2d(s[0], -fullSize[1]+0.16);
		GL11.glVertex2d(s[0], -fullSize[1]);
		GL11.glVertex2d(fullSize[0], -fullSize[1]);
		GL11.glEnd();*/
		
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
		
	}
				
	public void drawEditBlock(){
		
		selectedBlockSize = new float[]{(fullSize[0]-s[0])/4,((fullSize[0]-s[0])/4)*(s[1]/s[0])};
		selectedBlockPos = new float[]{(-fullSize[0])+(((fullSize[0]-s[0])-selectedBlockSize[0])/2),currentTexTextbox.coordsTop[1]+selectedBlockSize[1]};
		
		currentTexTextbox.updateCoords(new float[]{-fullSize[0],(-fullSize[1])+(textboxHeight*2)},new float[]{-s[0],(-fullSize[1])+textboxHeight});
		currentTexTextbox.render();
		
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
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
			
	}
	
	//public Button buttonGetCopy = new Button(new float[]{-fullSize[0],fullSize[1]}, new float[]{-(fullSize[0])+0.15f,fullSize[1]-0.04f}, "get copy");
	
	boolean wasSelected = false;
	
	public Tool currentTool;
	
	public void drawTools(){
		
		Tool.height = AnimationEditorGui.xToY(Tool.width);
		Tool.cursorHeight = AnimationEditorGui.xToY(Tool.cursorWidth);
		
		if(currentTool != null && newEventButton)
			currentTool.onEvent(eventButton);
		
		float dif = Math.abs(fullSize[0]-s[0]);
		float posX = (dif-Tool.width)/2;
		
		float x = (-fullSize[0])+posX;
		float y = 0.6f;
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		float height = tools.size()*Tool.height;
		
		GL11.glColor4f(0, 0.5f, 0.5f, 0.5f);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x+Tool.width, y);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y-height);
		GL11.glVertex2f(x+Tool.width, y-height);
		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTranslatef(x, 0.6f, 0);
		GL11.glColor4f(1, 1, 1, 1);
		for(int i=0;i<tools.size();i++){
			
			Tool tool = tools.get(i);
						
			if(x<=mousePos[0] && x+Tool.width>=mousePos[0] && y>=mousePos[1] && y-Tool.height<=mousePos[1]){
				
				if(newEventButton && Mouse.getEventButtonState()){
					if(eventButton == button){
						currentTool = tool;
					}
				}
				
				GL11.glColor4f(1,1,1,0.6f);
			}else{
				GL11.glColor4f(1, 1, 1, 1);
			}
			
			tool.draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4f(1,1,1,1);
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glVertex2f(Tool.width, 0);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(0, -Tool.height);
			GL11.glVertex2f(Tool.width, -Tool.height);
			GL11.glEnd();
			GL11.glColor4f(1, 1, 1, 1);
						
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			
			GL11.glTranslatef(0,-Tool.height,0);
			y-=Tool.height;
		}
		GL11.glTranslatef(Math.abs(x), -(0.6f-height), 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
		
}
