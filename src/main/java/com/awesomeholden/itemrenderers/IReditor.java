package com.awesomeholden.itemrenderers;

import org.lwjgl.opengl.GL11;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.TESRs.TESReditor;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.guis.AnimationEditorGui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class IReditor implements IItemRenderer{

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
	

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
		GL11.glPushMatrix();
		
		GL11.glDisable(GL11.GL_LIGHTING);
		
		TextureManager t = AnimationEditorGui.textureManager;
		
		GL11.glColor3f(1,1,1);
		
		if(type == ItemRenderType.INVENTORY){
			t.bindTexture(TESReditor.obsidian);
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(16, 0);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(0, 0);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(0, 16);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(16, 16);
			
			GL11.glEnd();
			
			t.bindTexture(ClientLoop.aBlockTex);
			
			GL11.glBegin(GL11.GL_QUADS);
			
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex2f(12, 4);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex2f(4, 4);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex2f(4, 12);
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex2f(12, 12);
			
			GL11.glEnd();
			
			GL11.glEnable(GL11.GL_LIGHTING);

		    GL11.glPopMatrix();
			
			
			
			return;
			
			
			/*GL11.glRotatef(180, 1, 0, 0);
			//GL11.glTranslatef(8, 0, 0);
			GL11.glRotatef(ClientLoop.degrees, 1, 0, 0);
			//GL11.glTranslatef(-8,0,0);
			GL11.glScalef(16, 16, 16);
			//GL11.glTranslatef(0, 0, -8);*/
		}else{
			GL11.glTranslatef(0, 1, 0);
		}
        
        t.bindTexture(TESReditor.obsidian);
        
        GL11.glTranslatef(0.5f, -0.5f, 0.5f);
        GL11.glRotatef(ClientLoop.degrees,0,1,0);
        GL11.glTranslatef(-0.5f, 0.5f, -0.5f);
		
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3d(0, 0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(1, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3d(1, -1, 0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3d(0, -1, 0);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(0, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(0,-1,0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, -1, 1);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(1, 0, 0);
		GL11.glTexCoord2d(0,0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1,-1,0);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2d(0,1);
		GL11.glVertex3f(0, -1, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, -1, 1);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, 0, 0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 0, 0);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, -1, 1);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(0, -1, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, -1, 0);

		GL11.glEnd();
		
		int cache = TESReditor.texTrainIndex;
		int side = 0;
		
		float mX = 1;
		float mZ = 0;
		
		GL11.glTranslatef(0, -1, 0);
				
		for(int i=0;i<TESReditor.texTrainSize;i++){
			if(i%4 == 0 && i != 0)
				side++;
			try{
				AnimationEditorGui.textureManager.bindTexture(TileentityAnimatedClient.textures.get(cache));
			}catch(IndexOutOfBoundsException e){
				AnimationEditorGui.textureManager.bindTexture(new ResourceLocation("minecraft:textures/items/apple.png"));
			}
			
			if(type == ItemRenderType.INVENTORY){
				GL11.glTranslatef(-0.001f, -0.001f, -0.001f);
				GL11.glScalef(1.02f, 1.02f, 1.02f);
			}
			
			switch(side){
			case 0:
				GL11.glBegin(GL11.GL_QUADS);
				Main.draw(mX-0.25f,0.6f,-0.001f);
				Main.draw(mX, 0.6f,-0.001f);
				Main.draw(mX,0.375f,-0.001f);
				Main.draw(mX-0.25f,0.375f,-0.001f);
				GL11.glEnd();
				mX-=0.25f;
				break;
			case 1:
				GL11.glBegin(GL11.GL_QUADS);
				Main.draw(-0.001f, 0.6f,mZ+0.25f);
				Main.draw(-0.001f, 0.6f,mZ);
				Main.draw(-0.001f,0.375f,mZ);
				Main.draw(-0.001f,0.375f,mZ+0.25f);
				GL11.glEnd();
				
				mZ+=0.25f;
				break;
			case 2:
				GL11.glBegin(GL11.GL_QUADS);
				Main.draw(mX+0.25f, 0.6f,1.001f);
				Main.draw(mX,0.6f,1.001f);
				Main.draw(mX,0.375f,1.001f);
				Main.draw(mX+0.25f,0.375f,1.001f);
				GL11.glEnd();
				mX+=0.25f;
				break;
			case 3:
				GL11.glBegin(GL11.GL_QUADS);
				Main.draw(1.001f,0.6f,mZ-0.25f);
				Main.draw(1.001f,0.6f,mZ);
				Main.draw(1.001f, 0.375f,mZ);
				Main.draw(1.001f,0.375f,mZ-0.25f);
				GL11.glEnd();
				mZ-=0.25f;
				break;
			}
			
			if(type == ItemRenderType.INVENTORY){
				GL11.glScalef(-1.02f, -1.02f, -1.02f);
				GL11.glTranslatef(0.001f, 0.001f, 0.001f);
			}
			
			cache++;
			if(cache == TileentityAnimatedClient.textures.size())
				cache = 0;
			
		}
				
		GL11.glTranslatef(0, 1, 0);
		
		t.bindTexture(ClientLoop.aBlockTex);
		
		GL11.glTranslatef(0, 0.5f, 0);
		GL11.glTranslatef(0.375f,0.1f+ClientLoop.y,0.375f);
		GL11.glScalef(0.25f, 0.25f, 0.25f);
		
		GL11.glTranslatef(0.5f,-0.5f,0.5f);
		GL11.glRotatef(ClientLoop.degrees,0,1,0);
		/*GL11.glRotatef(45, 1, 0, 0);
		GL11.glRotatef(45, 0, 0, 1);*/
		//GL11.glRotatef(45, 0, 1, 0);
		GL11.glTranslatef(-0.5f,0.5f,-0.5f);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1, 1, 1);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3d(0, 0, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(1, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3d(1, -1, 0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3d(0, -1, 0);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(0, 0, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(0,-1,0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, -1, 1);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(1, 0, 0);
		GL11.glTexCoord2d(0,0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1,-1,0);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2d(0,1);
		GL11.glVertex3f(0, -1, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, -1, 1);
		
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, 0, 1);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex3f(1, 0, 1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, 0, 0);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 0, 0);
		
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, -1, 1);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(0, -1, 1);
		GL11.glTexCoord2f(0,1);
		GL11.glVertex3f(0, -1, 0);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, -1, 0);

		GL11.glEnd();
		
		GL11.glEnable(GL11.GL_LIGHTING);

	    GL11.glPopMatrix();
	}

}
