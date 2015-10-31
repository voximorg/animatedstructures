package com.awesomeholden.guis.animationeditorguiwidges;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import org.lwjgl.input.Keyboard;

import com.awesomeholden.proxies.ClientProxy;

public class TextBoxInput extends TextBox{
	
	public boolean entered = false;
	
	public String blacklist;

	public TextBoxInput(float[] coordsTop, float[] coordsBottom, String text,String blacklist) {
		super(coordsTop, coordsBottom, text);
		this.blacklist = blacklist;
	}
	
	byte previousChar = 1;
	@Override
	public void render(){
		if(active){
		byte chr = (byte) Keyboard.getEventCharacter();
		
		if(chr != previousChar && blacklist.indexOf(chr) == -1){
			Minecraft.getMinecraft().displayGuiScreen(new GuiChat("/"));
			if(chr == 13) entered = true;
			else if(chr == 8 && text.length()>0){
				text = text.substring(0,text.length()-1); 
				moveSpot(false);
			}else if(chr != 0){
				text += ""+(char)chr;
				moveSpot(true);
			}
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
		
		previousChar = chr;
		}
		
		super.render();	
		
	}

}
