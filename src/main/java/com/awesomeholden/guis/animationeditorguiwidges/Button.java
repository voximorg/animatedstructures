package com.awesomeholden.guis.animationeditorguiwidges;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.proxies.ClientProxy;

public class Button {
	
	public static float[] color = new float[]{0.5f,0,0,0.5f};
	
	public TextBox textBox;
	
	public float[] coordsTop;
	public float[] coordsBottom;
	
	public boolean pressed = false;
	
	public float textHeight = 0.04f;
	
	public Button(float[] coordsTop,float[] coordsBottom,String text){
		this.coordsTop = coordsTop;
		this.coordsBottom = coordsBottom;
		textBox = new TextBox(coordsTop, coordsBottom, text);
		textBox.updateCoords(new float[]{coordsTop[0], (float) (coordsTop[1]-((textBox.size[1]-textHeight)/2))}, new float[]{0,0});
		textBox.updateCoords(textBox.coordsTop, new float[]{coordsBottom[0],textBox.coordsTop[1]-textHeight});
		textBox.alignment = 1;
	}
	
	public void draw(){
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(color[0],color[1],color[2],color[3]);
		GL11.glVertex2f(coordsBottom[0], coordsTop[1]);
		GL11.glVertex2f(coordsTop[0], coordsTop[1]);
		GL11.glVertex2f(coordsTop[0], coordsBottom[1]);
		GL11.glVertex2f(coordsBottom[0], coordsBottom[1]);
		GL11.glEnd();
	}
	
	public void doo(){
		float cache = TextBox.color[3];
		TextBox.color[3] = 0;
		textBox.render();
		TextBox.color[3] = cache;
		if(textBox.active) textBox.active = false;
		textBox.updateSpot();
		
		float[] mp = ClientProxy.mousePos;
		if(mp[0]>=coordsTop[0] && mp[1]<=coordsTop[1] && mp[0]<=coordsBottom[0] && mp[1]>=coordsBottom[1]){
			color[3] = 0.75f;
			if(Mouse.isButtonDown(1) && !wasPressed) pressed = true;
				
			wasPressed = Mouse.isButtonDown(1);
		}
	}
	
	boolean wasPressed = false;
	public void update(){
		draw();
		
		doo();
	}

}
