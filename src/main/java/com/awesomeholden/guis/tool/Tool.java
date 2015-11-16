package com.awesomeholden.guis.tool;

import org.lwjgl.opengl.GL11;

import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;

import net.minecraft.util.ResourceLocation;

public class Tool {
	
	public static float width = 0.1f;
	
	public static float height = AnimationEditorGui.xToY(width); //add this to a loop
	
	public static float cursorWidth = 0.04f;
	
	public static float cursorHeight = AnimationEditorGui.xToY(cursorWidth);
	
	public ResourceLocation icon;
	
	public ResourceLocation cursorIcon;
	
	public void onEvent(int event){}
	
	public void draw(){
		AnimationEditorGui.textureManager.bindTexture(icon);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(width, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0, -height);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(width, -height);
		GL11.glEnd();
	}
	
	public void drawAsCursor(){
		AnimationEditorGui.textureManager.bindTexture(cursorIcon);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(cursorWidth,cursorHeight);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(0, cursorHeight);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(cursorWidth, 0);
		GL11.glEnd();
	}
	
}
