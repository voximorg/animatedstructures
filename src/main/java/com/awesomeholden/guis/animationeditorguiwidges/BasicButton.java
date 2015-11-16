package com.awesomeholden.guis.animationeditorguiwidges;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.proxies.ClientProxy;

public class BasicButton {
	
	public float[] coordsTop;
	public float[] coordsBottom;
	
	public boolean pressed = false;
	
	public float[] color = new float[]{1,1,1};
	
	public ResourceLocation texture;
	
	public BasicButton(float[] coordsTop,float[] coordsBottom,ResourceLocation texture){
		this.coordsTop = coordsTop;
		this.coordsBottom = coordsBottom;
		
		this.texture = texture;
	}
	
	public void draw(){
		AnimationEditorGui.textureManager.bindTexture(texture);
		GL11.glBegin(GL11.GL_QUADS);
		Main.draw(coordsBottom[0], coordsTop[1]);
		Main.draw(coordsTop[0], coordsTop[1]);
		Main.draw(coordsTop[0], coordsBottom[1]);
		Main.draw(coordsBottom[0], coordsBottom[1]);
		GL11.glEnd();
	}
	
	public void doo(){
		
		GL11.glColor3f(color[0], color[1], color[2]);
		
		float[] mp = ClientProxy.mousePos;
		if(mp[0]>=coordsTop[0] && mp[1]<=coordsTop[1] && mp[0]<=coordsBottom[0] && mp[1]>=coordsBottom[1]){
			GL11.glColor4f(1, 1, 1, 0.8f);
			if(AnimationEditorGui.newEventButton && AnimationEditorGui.eventButton == AnimationEditorGui.button && Mouse.getEventButtonState()) pressed = true;
				
		}
	}
	
	public void update(){
		doo();
		draw();
	}
	
	public void updateCoords(float[] top,float[] bottom){
		coordsBottom = bottom;
		coordsTop = top;
	}

}
