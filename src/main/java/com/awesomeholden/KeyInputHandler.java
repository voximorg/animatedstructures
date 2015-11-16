package com.awesomeholden;

import net.minecraft.client.Minecraft;

import com.awesomeholden.proxies.ClientProxy;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event1) {
        /*if(KeyBindings.ping.isPressed())
            ClientProxy.currentTileEntityForGui = null;
        	Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
        	System.out.println("PING WAS PRESSED!");
        /*if(KeyBindings.ESCAPE.isPressed())
        	System.out.println("ESCAPE PRESSED");*/
        if(KeyBindings.RIGHT.isPressed()){
        	ClientProxy.rightPressed = true;
        }else{
        	ClientProxy.rightPressed = false;
        }if(KeyBindings.LEFT.isPressed()){
        	ClientProxy.leftPressed = true;
        }else{
        	ClientProxy.leftPressed = false;
        }
	}

}
