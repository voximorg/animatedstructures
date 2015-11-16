package com.awesomeholden.guis.tool;

import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.packets.RefreshAnimation;
import com.awesomeholden.packets.gui.Erase;
import com.awesomeholden.packets.gui.GetFrameByLayerAndFrame;

import net.minecraft.util.ResourceLocation;

public class ToolEraser extends Tool{
	
	public ToolEraser(){
		icon = new ResourceLocation("animatedstructures:textures/tools/eraser.png");
		cursorIcon = icon;
	}
	
	public void onEvent(int event){
		if(event == AnimationEditorGui.button && AnimationEditorGui.hoverAnimated != null && AnimationEditorGui.currentFrame < AnimationEditorGui.frameIntervals.size()){
			Main.network.sendToServer(new Erase(AnimationEditorGui.controller.coords,AnimationEditorGui.currentFrame,AnimationEditorGui.controller.theControlled.indexOf(AnimationEditorGui.hoverAnimated)));
			
			Main.network.sendToServer(new GetFrameByLayerAndFrame(AnimationEditorGui.smallestCoords[1],AnimationEditorGui.currentFrame,AnimationEditorGui.currentLayer,AnimationEditorGui.controller.coords));
			
			Main.network.sendToServer(new RefreshAnimation(AnimationEditorGui.controller.coords));
		}
	}

}
