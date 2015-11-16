package com.awesomeholden.guis.animationeditorguiwidges;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import com.awesomeholden.ClientLoop;
import com.awesomeholden.proxies.ClientProxy;

public class TextBoxInput extends TextBox{
	
	public boolean entered = false;
	
	public static int deleteTick = 0;
	
	public static boolean deleting = false;
	
	public String blacklist;

	public TextBoxInput(float[] coordsTop, float[] coordsBottom, String text,String blacklist) {
		super(coordsTop, coordsBottom, text);
		this.blacklist = blacklist;
	}
	
	@Override
	public void render(){
		if(active){
			
		byte chr = ClientLoop.eventChar;
		
		if(deleting && deleteTick>=2){
			if(text.length()>0){
				text = text.substring(0,text.length()-1); 
				moveSpot(false);
			}
			deleteTick = 0;
		}
		
		if(ClientLoop.newEventChar && blacklist.indexOf(chr) == -1){
			
			if(deleting)
				deleting = false;
						
			ClientLoop.newEventChar = false;
			//Minecraft.getMinecraft().displayGuiScreen(new GuiChat("/"));
			if(chr == 13) entered = true;
			
			else if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_V)){
				String cb = Sys.getClipboard();
								
				if(cb.length()<150){
					for(int i=0;i<cb.length();i++){
						char l = cb.charAt(i);
						
						if(blacklist.indexOf(l) == -1){
							text += ""+l;
							
							index = text.length()-((int)(size[0]/textSize));
							
							if(index<0) index = 0;
							
							displayedText = text.substring(index);
						}
					}
				}
			}else if(chr == 8){
				deleting = true;
				deleteTick = -3;
				
				if(text.length()>0){
				
				text = text.substring(0,text.length()-1); 
				
				index = text.length()-((int)(size[0]/textSize));
				
				if(index<0) index = 0;
				
				displayedText = text.substring(index);
				}
				
			}else if(chr != 0){
				text += ""+(char)chr;
				
				index = text.length()-((int)(size[0]/textSize));
				
				if(index<0) index = 0;
				
				displayedText = text.substring(index);
			}
		}
		
		}
		
		super.render();	
		
	}

}
