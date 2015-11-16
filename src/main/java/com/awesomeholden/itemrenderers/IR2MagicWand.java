package com.awesomeholden.itemrenderers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL44;

import com.awesomeholden.ClientLoop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class IR2MagicWand implements IItemRenderer{
	
	public static IR2MagicWand instance = new IR2MagicWand();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ResourceLocation stick = new ResourceLocation("animatedstructures:textures/magic_wand_stick.png");
	
	float cache = 10;
	float ss = 0.1f; //stick size
	float ssy = 4.5f;
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
				
		GL11.glPushMatrix();
				
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16); //lol destroyed java
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX,buffer);
				
		float x = buffer.get(12);
		float y = buffer.get(13);
		float z = buffer.get(14);
		
		boolean bool = true;
		
		/*GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();*/
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON){
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			GL11.glTranslatef(x+6,y+2,z-11);
		}else if(type == ItemRenderType.INVENTORY){
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			GL11.glTranslatef(x+6,y+5,z-11);
			
			GL11.glRotatef(160,0,0,1);
			GL11.glRotatef(45, 1, 0, 0);
			
			bool = false;
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glColor3f(0.1f, 0.9f, 0.1f);
			//top
			GL11.glVertex3f(1, 1, 1);
			GL11.glVertex3f(-1, 1, 1);
			GL11.glVertex3f(-1, 1, -1);
			GL11.glVertex3f(1, 1, -1);
		
			GL11.glColor3f(1, 1, 1);
			//top
			GL11.glTexCoord2f(1, -1);
			GL11.glVertex3f(-1, 1, 1);
			GL11.glTexCoord2f(0, -1);
			GL11.glVertex3f(-1, 1, -1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(1, 1, -1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(1, 1, 1);
			
			GL11.glEnd();
			
		}else if(type == ItemRenderType.EQUIPPED){
			GL11.glTranslatef(-1f, 5.6f, 1.4f);
												
			GL11.glRotatef(45, 1, 0, 0);
			
		}
		
		
				
		/*System.out.println("BUFFER START");
		for(int i=0;i<16;i++){
			System.out.println("I: "+i+" : "+buffer.get(i));
		}
		System.out.println("BUFFER END");*/
		
		//GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		//GL11.glTranslatef(cache,0,-10);
		cache+=0.01;
		/*GL11.glTranslatef(10, 0, -10);
		GL11.glRotatef(315, 0, 1, 0);
		GL11.glTranslatef(-10, 0, 10);*/
		
		GL11.glScalef(2, 2, 2);
		
		GL11.glRotatef(15f, 0, 0, 1);
		GL11.glRotatef(332.5f, 1, 0, 0);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		TextureManager texMan = Minecraft.getMinecraft().renderEngine;
		
		texMan.bindTexture(stick);
		
		GL11.glBegin(GL11.GL_QUADS);
		//stick
		//front
		GL11.glColor3f(0.75f,0,0.75f);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-ss,-ssy,ss);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(ss,-ssy,ss);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(ss,-1,ss);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(-ss,-1,ss);
		
		//left
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-ss,-ssy,-ss);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-ss,-ssy,ss);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(-ss, -1, ss);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(-ss, -1, -ss);
		
		//back
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(ss, -ssy, -ss);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-ss, -ssy, -ss);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(-ss, -1, -ss);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(ss,-1,-ss);
		
		//right
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(ss,-ssy,ss);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(ss, -ssy, -ss);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(ss, -1, -ss);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(ss, -1, ss);
		
		GL11.glEnd();
		
		
		
		//GL11.glRotatef(180, 1, 0, 0);
		//GL11.glRotatef(315, 0, 0, 1);
		
		GL11.glRotatef(ClientLoop.degrees, 0, 1, 0);
				
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(0.1f, 0.9f, 0.1f);
		
		//front
		GL11.glVertex3f(-1, -1, 1);
		GL11.glVertex3f(1, -1, 1);
		GL11.glVertex3f(1, 1, 1);
		GL11.glVertex3f(-1, 1, 1);
				
		//left
		GL11.glVertex3f(-1, -1, -1);
		GL11.glVertex3f(-1, -1, 1);
		GL11.glVertex3f(-1, 1, 1);
		GL11.glVertex3f(-1, 1, -1);
		
		//back
		GL11.glVertex3f(1, -1, -1);
		GL11.glVertex3f(-1, -1, -1);
		GL11.glVertex3f(-1, 1, -1);
		GL11.glVertex3f(1, 1, -1);
		
		//right
		GL11.glVertex3f(1, -1, 1);
		GL11.glVertex3f(1, -1, -1);
		GL11.glVertex3f(1, 1, -1);
		GL11.glVertex3f(1,1,1);
		
		//bottom
		GL11.glVertex3f(1, -1, 1);
		GL11.glVertex3f(-1, -1, 1);
		GL11.glVertex3f(-1, -1, -1);
		GL11.glVertex3f(1, -1, -1);
		
		GL11.glEnd();

		
		
		
		
		
		texMan.bindTexture(ClientLoop.aBlockTex);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1,1,1);
		
		if(bool){
			
			GL11.glVertex3f(1, 1, 1);
			GL11.glVertex3f(-1, 1, 1);
			GL11.glVertex3f(-1, 1, -1);
			GL11.glVertex3f(1, 1, -1);
			
			//top
			GL11.glTexCoord2f(0, -1);
			GL11.glVertex3f(-1, 1, -1);
			GL11.glTexCoord2f(1, -1);
			GL11.glVertex3f(-1, 1, 1);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(1, 1, 1);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(1, 1, -1);
			
		}
		
		//front
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(-1, -1, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3f(1, 1, 1);
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3f(-1, 1, 1);
				
		//left
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, -1, -1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(-1, -1, 1);
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3f(-1, 1, 1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3f(-1, 1, -1);
		
		//back
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, -1, -1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, -1, -1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3f(-1, 1, -1);
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3f(1, 1, -1);
		
		//right
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(1, -1, -1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3f(1, 1, -1);
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3f(1,1,1);
		
		//bottom
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3f(-1, -1, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-1, -1, -1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, -1, -1);
		
		GL11.glEnd();
		//GL11.glTranslatef(-cache,0,10);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

}
