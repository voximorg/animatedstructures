package com.awesomeholden;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindings{
	
	//public static KeyBinding ping;
	//public static KeyBinding ESCAPE;
	public static KeyBinding RIGHT;
	public static KeyBinding LEFT;
	
	public static void create(){
		//ping = new KeyBinding("key.Q", Keyboard.KEY_Q, "key.categories.AnimatedStructures");
		//ESCAPE = new KeyBinding("key.ESCAPE", Keyboard.KEY_ESCAPE, "key.categories.AnimatedStructures");
		RIGHT = new KeyBinding("key.RIGHT", Keyboard.KEY_RIGHT, "key.categories.AnimatedStructures");
		LEFT = new KeyBinding("key.LEFT", Keyboard.KEY_LEFT, "key.categories.AnimatedStructures");
	}

}
