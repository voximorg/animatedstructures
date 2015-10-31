package com.awesomeholden.guis.animationeditorguiwidges;

import org.lwjgl.opengl.GL11;

import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;

import net.minecraft.util.ResourceLocation;

public class TexturedButton extends Button{
	
	public ResourceLocation tex;

	public TexturedButton(float[] coordsTop, float[] coordsBottom, String text,ResourceLocation tex) {
		super(coordsTop, coordsBottom, text);
		this.tex = tex;
	}
	
	@Override
	public void update(){
		AnimationEditorGui.textureManager.bindTexture(tex);
		GL11.glBegin(GL11.GL_QUADS);
		Main.draw(coordsBottom[0], coordsTop[1]);
		Main.draw(coordsTop[0], coordsTop[1]);
		Main.draw(coordsTop[0], coordsBottom[1]);
		Main.draw(coordsBottom[0], coordsBottom[1]);
		GL11.glEnd();
		
		super.doo();
	}

}
