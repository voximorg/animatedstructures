package com.awesomeholden.guis.animationeditorguiwidges;

import java.util.Collections;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;
import com.awesomeholden.guis.AnimationEditorGui;

public class EditBlockGuiWidget {
/*	
	public AnimationEditorGui fullGui;
	public TileentityAnimatedClient controlled;
	public Minecraft mc = Minecraft.getMinecraft();
	
	public TextBoxInput textBox;
	public TextBox locationBox;
	
	public EditBlockGuiWidget(AnimationEditorGui f,TileentityAnimatedClient c){
		fullGui = f;
		controlled = c;
		locationBox = new TextBox(new float[]{-0.8f,-0.4f},new float[]{-0.6f,-0.44f},"location:");
		String path = "none";
		ResourceLocation location = (ResourceLocation) TileentityAnimatedServer.textures.get(controlled.frames.get(fullGui.currentFrame));
		path = location.getResourceDomain()+location.getResourcePath();
		textBox = new TextBoxInput(new float[]{-0.8f,-0.4f},new float[]{-0.6f,-0.44f},path,"");
	}
	
	public void render(){
		//System.out.println("wasPressed: "+Keyboard.getEventCharacter());
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(0, 0, 0.5f, 0.5f);
			float[] s = fullGui.fullSize;
			float right = -(s[0]-(s[0]-fullGui.s[0]));
			GL11.glVertex2f(right,0);
			GL11.glVertex2f(-s[0],0);
			GL11.glVertex2f(-s[0],-s[1]);
			GL11.glVertex2f(right,-s[1]);
			
			float subLeft = -s[0]+0.005f;
			float subRight = subLeft+((-right)/5);
			float subBottom = -s[1]/5;
			GL11.glColor3f(0,0,0);
			GL11.glVertex2f(subRight,-0.005f);
			GL11.glVertex2f(subLeft,-0.005f);
			GL11.glVertex2f(subLeft, subBottom);
			GL11.glVertex2f(subRight,subBottom);
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		locationBox.updateCoords(new float[]{-s[0],subBottom-0.01f},new float[]{right-0.005f,subBottom-0.05f});
		locationBox.render();
		/*if(textBox.entered){
			
		}*/
		/*if(textBox.entered){
			if(!TileentityAnimatedServer.textures.containsKey(textBox.text)){
				TileentityAnimatedServer.textures.put(textBox.text, new ResourceLocation(textBox.text));
			}
			if(!controlled.doFrames.get(fullGui.currentFrame)){
				controlled.frames.add("");
				controlled.doFrames.set(fullGui.currentFrame, true);
				System.out.println("currentFrame: "+fullGui.currentFrame);
				System.out.println("frames.size(): "+controlled.frames.size());
				System.out.println("list index: "+controlled.doFrames.get(fullGui.currentFrame));
			}
			controlled.frames.set(fullGui.currentFrame, textBox.text);
			textBox.entered = false;
		}
		textBox.updateCoords(new float[]{subLeft, locationBox.coordsBottom[1]-0.02f},new float[]{locationBox.coordsBottom[0],locationBox.coordsBottom[1]-0.06f});
		textBox.render();
		
		GL11.glColor3f(1,1,1);
						
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fullGui.textureManager.bindTexture(controlled.texture);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1, 1, 1);
			Main.draw(subRight-0.01f,-0.015f);
			Main.draw(subLeft+0.01f,-0.015f);
			Main.draw(subLeft+0.01f, subBottom+0.01f);
			Main.draw(subRight-0.01f,subBottom+0.01f);
		GL11.glEnd();
		
	}
*/
}