package com.awesomeholden.guis.animationeditorguiwidges;

import java.util.Arrays;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.proxies.ClientProxy;

import net.minecraft.util.ResourceLocation;

public class Scrollbar {
	
	public float[] coordsTop;
	public float[] coordsBottom;
	
	public float[] color;
	public float[] barColor;
	
	public int divisionVariable;
	
	public float divisionY;
	
	public float barY;
	
	public int barPos = 0;
	
	public boolean active;
	
	
	
	public Scrollbar(float[] coordsTop,float[] coordsBottom,float[] color,float[] barColor,int divisionVariable){
		this.coordsTop = coordsTop;
		this.coordsBottom = coordsBottom;
		this.color = color;
		this.barColor = barColor;
		this.divisionVariable = divisionVariable;
		
		float dif = coordsTop[1]-coordsBottom[1];
		
		barY = dif/divisionVariable;
		
		divisionY = dif-barY;
	}
	
	public void doo(){
		float[] mp = ClientProxy.mousePos;
		if(mp[0]>=coordsTop[0] && mp[1]<=coordsTop[1] && mp[0]<=coordsBottom[0] && mp[1]>=coordsBottom[1]){
			
			if(AnimationEditorGui.newEventButton && AnimationEditorGui.eventButton == AnimationEditorGui.button && Mouse.getEventButtonState())
				active = true;
			
		}
		
		if(ClientLoop.leftUp)
			active = false;
		
		else if(active){
					
			float dif = coordsTop[1]-mp[1];
		
			barPos = (int) (dif/barY);
			
			if(mp[1]<=coordsBottom[1])
				barPos = ((int) ((coordsTop[1]-coordsBottom[1])/barY))-1;
			else if(mp[1]>=coordsTop[1])
				barPos = 0;
		}
	}
	
	public void draw(){				
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		
		GL11.glVertex2f(coordsBottom[0], coordsTop[1]);
		GL11.glVertex2f(coordsTop[0], coordsTop[1]);
		GL11.glVertex2f(coordsTop[0], coordsBottom[1]);
		GL11.glVertex2f(coordsBottom[0], coordsBottom[1]);
		
		GL11.glColor4f(barColor[0], barColor[1], barColor[2], barColor[3]);
		
		float y = barPos*barY;
		GL11.glVertex2f(coordsBottom[0], coordsTop[1]-y);
		GL11.glVertex2f(coordsTop[0], coordsTop[1]-y);
		GL11.glVertex2f(coordsTop[0], coordsTop[1]-y-barY);
		GL11.glVertex2f(coordsBottom[0], coordsTop[1]-y-barY);
		GL11.glEnd();
	}
	public void update(){
		doo();
		draw();
	}
	
	public void updateCoords(float[] top,float[] bottom,int divisionVariable){
		coordsBottom = bottom;
		coordsTop = top;
		
		this.divisionVariable = divisionVariable;
		
		float dif = coordsTop[1]-coordsBottom[1];
		
		barY = dif/divisionVariable;
		
		divisionY = dif-barY;
	}

}
