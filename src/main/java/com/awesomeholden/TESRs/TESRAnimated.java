package com.awesomeholden.TESRs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;


import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.Tileentities.TileentityAnimatedServer;

import cpw.mods.fml.client.FMLClientHandler;

public class TESRAnimated extends TileEntitySpecialRenderer{
	
	//private IModelCustom model;
    //private ResourceLocation texture;
    
    public TESRAnimated(){
    	//casinoMachine = AdvancedModelLoader.loadModel("/assets/models/casinoMachine.obj");
        //System.out.println("\n\n\n\nFullPath: "+texture.getResourcePath()+"\n\n\n\n");
    }

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float another) {
		TileentityAnimatedClient tEntity = (TileentityAnimatedClient) te;
		//System.out.println(te+"   "+tEntity+"   "+tEntity.texture);
		
		//renderEngine.bindTexture(ResourceLocation);
		
		//Minecraft.getMinecraft().renderEngine.bindTexture("/HP/wand.png");
		//casinoMachine.renderAll();
		
		
		/*if(tEntity.pH == tEntity.interval){
			tEntity.color0 = (float)Math.random();
			tEntity.color1 = (float)Math.random();
			tEntity.color2 = (float)Math.random();
			tEntity.color3 = (float)Math.random();
			tEntity.pH = 0;
    	}else{
    		//System.out.println(pH);
    		tEntity.pH++;
    	}*/
				
		GL11.glPushMatrix();
		bindTexture(tEntity.texture);
		/*System.out.println("CURRENT TEXTURE: "+tEntity.texture);
		System.out.println("TEXTURE NUMBER: "+TileentityAnimatedServer.textures.size());*/
		//System.out.println("TEXTURE: "+tEntity.texture.getResourceDomain()+':'+tEntity.texture.getResourcePath());
		//System.out.println(tEntity);
        //GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glDisable(GL11.GL_TEXTURE_2D);
        //GL11.glEnable(GL11.GL_BLEND);
        
        //int bright = 0xF0;
        int bright = 0xE0;
        int brightX = bright % 65536;
        int brightY = bright / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
                
        //FMLClientHandler.instance().getClient().getTextureManager().bindTexture(texture);
        //Tessellator tes = Tessellator.instance;
        GL11.glTranslated(x, y+1, z);
        
        GL11.glBegin(GL11.GL_QUADS);
    	//GL11.glColor4f(tEntity.color0,tEntity.color1,tEntity.color2,tEntity.color3);
        
        Main.draw(0, 0, 0);
        Main.draw(1, 0, 0);
        Main.draw(1, -1, 0);
        Main.draw(0, -1, 0);
    	
        Main.draw(1, 0, 0);
        Main.draw(0, 0, 0);
        Main.draw(0, 0, 1);
        Main.draw(1, 0, 1);
    	
        Main.draw(1, -1, 1);
        Main.draw(0, -1, 1);
        Main.draw(0, -1, 0);
        Main.draw(1, -1, 0);
    	
        Main.draw(0, 0, 1);
        Main.draw(0, 0, 0);
        Main.draw(0, -1, 0);
        Main.draw(0, -1, 1);
    	
        GL11.glTexCoord3f(1, 0, 0);
        GL11.glVertex3f(1, -1, 1);
        GL11.glTexCoord3f(0, 0, 0);
        GL11.glVertex3f(1, -1, 0);
        GL11.glTexCoord3f(0, -1,0);
        GL11.glVertex3f(1, 0, 0);
        GL11.glTexCoord3f(1, -1,0);
        GL11.glVertex3f(1, 0, 1);
    	
    GL11.glEnd();
    GL11.glTranslated(0,0,1);
    GL11.glBegin(GL11.GL_QUADS);
    	//GL11.glColor4f(tEntity.color0, tEntity.color1, tEntity.color2,tEntity.color3);
    	GL11.glTexCoord3f(1, 0, 0);
    	GL11.glVertex3f(0, -1, 0);
    	GL11.glTexCoord3f(0, 0, 0);
    	GL11.glVertex3f(1, -1, 0);
    	GL11.glTexCoord3f(0, -1,0);
    	GL11.glVertex3f(1, 0, 0);
    	GL11.glTexCoord3f(1, -1,0);
    	GL11.glVertex3f(0, 0, 0);
    GL11.glEnd();
    
    GL11.glEnable(GL11.GL_LIGHTING);
    //GL11.glEnable(GL11.GL_TEXTURE_2D);
    //GL11.glPopAttrib();
    GL11.glPopMatrix();
		
	}
	
}
