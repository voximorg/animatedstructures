package com.awesomeholden.itemrenderers;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.items.MItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemRendererAnimation implements IItemRenderer{
	
	public static ItemRendererAnimation instance = new ItemRendererAnimation();
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		GL11.glPushMatrix();
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		//GL11.glEnable(GL11.GL_BLEND);
		//System.out.println("randrange: "+Main.randRange(5));
		AnimationEditorGui.textureManager.bindTexture(ClientLoop.aBlockTex);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1,1,1);
		GL11.glTexCoord2f(0, -1);
		GL11.glVertex3d(0, 0, 0);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3d(0, 16, 0);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3d(16, 16, 0);
		GL11.glTexCoord2f(1, -1);
		GL11.glVertex3d(16, 0, 0);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_LIGHTING);
		//GL11.glDisable(GL11.GL_BLEND);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
		
	}
	
}
