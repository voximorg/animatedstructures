package com.awesomeholden.guis.tool;

import java.util.ArrayList;
import java.util.List;

import com.awesomeholden.Main;
import com.awesomeholden.Tileentities.TileentityAnimatedClient;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.guis.animationeditorguiwidges.TextBox;
import com.awesomeholden.guis.animationeditorguiwidges.TextBoxInput;
import com.awesomeholden.packets.gui.SetTexture;

import net.minecraft.util.ResourceLocation;

public class ToolPencil extends Tool{

	public ToolPencil() {
		icon = new ResourceLocation("animatedstructures:textures/tools/pencil.png");
		cursorIcon = icon;
	}
	
	@Override
	public void onEvent(int event){
			if(event == AnimationEditorGui.button && AnimationEditorGui.hoverAnimated != null){
				Main.network.sendToServer(new SetTexture(AnimationEditorGui.currentTexTextbox.text, AnimationEditorGui.controller.theControlled.indexOf(AnimationEditorGui.hoverAnimated), AnimationEditorGui.controller.coords, AnimationEditorGui.currentFrame));
				
				List<TileentityAnimatedClient> onLayer = new ArrayList<TileentityAnimatedClient>();
				
				for(int i=0;i<AnimationEditorGui.controller.theControlled.size();i++){
					TileentityAnimatedClient c = AnimationEditorGui.controller.theControlled.get(i);
					if((c.yCoord-AnimationEditorGui.smallestCoords[1]) == AnimationEditorGui.currentLayer)
						onLayer.add(c);
				}
				
				int tEntity = onLayer.indexOf(AnimationEditorGui.hoverAnimated);
				
				int index = TileentityAnimatedClient.getTextureByString(AnimationEditorGui.currentTexTextbox.text);
				
				if(index==-1){
					TileentityAnimatedClient.textures.add(new ResourceLocation(AnimationEditorGui.currentTexTextbox.text));
					
					AnimationEditorGui.frames[tEntity] = TileentityAnimatedClient.textures.size()-1;
				}else{
					AnimationEditorGui.frames[tEntity] = index;
				}
			}
	}

}
