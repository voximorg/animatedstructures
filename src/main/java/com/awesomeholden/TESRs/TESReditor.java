package com.awesomeholden.TESRs;

import org.lwjgl.opengl.GL11;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimationEditorClient;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.itemrenderers.IR2MagicWand;
import com.awesomeholden.itemrenderers.ItemRendererAnimation;
import com.awesomeholden.proxies.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TESReditor extends TileEntitySpecialRenderer{
	
	int degrees = 0;
	
	private ResourceLocation obsidian = new ResourceLocation("textures/blocks/obsidian.png");
	
	private int texTrainSize = 16;
	public static int texTrainIndex = 0;
	public static Object texTrainIndexLock = new Object();

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float p) {
		
		bindTexture(obsidian);
		TileentityAnimationEditorClient t = (TileentityAnimationEditorClient) te;
 		
		GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        
        int bright = 0xE0;
        int brightX = bright % 65536;
        int brightY = bright / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
        
        bindTexture(obsidian);
		
		GL11.glTranslated(x, y+1, z);
		
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
		
		int cache = texTrainIndex;
		int side = 0;
		
		float mX = 1;
		float mZ = 0;
		
		GL11.glTranslatef(0, -1, 0);
		
		for(int i=0;i<texTrainSize;i++){
			if(i%4 == 0 && i != 0)
				side++;
			try{
				AnimationEditorGui.textureManager.bindTexture(TileentityAnimatedClient.textures.get(cache));
			}catch(IndexOutOfBoundsException e){
				AnimationEditorGui.textureManager.bindTexture(new ResourceLocation("minecraft:textures/items/apple.png"));
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
			
			cache++;
			if(cache == TileentityAnimatedClient.textures.size())
				cache = 0;
			
		}
		
		GL11.glTranslatef(0, 1, 0);
		
		bindTexture(ClientLoop.aBlockTex);
		
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
		GL11.glScalef(-0.5f, -0.5f, -0.5f);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	    GL11.glPopMatrix();
	    
	    degrees++;
	    if(degrees>360)
	    	degrees = 0;
			
	}

}
