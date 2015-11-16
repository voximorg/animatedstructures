package com.awesomeholden.guis.animationeditorguiwidges;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.awesomeholden.Main;
import com.awesomeholden.guis.AnimationEditorGui;
import com.awesomeholden.proxies.ClientProxy;

public class TextBox {
	
	public static int tick = 0;
	
	public static float[] outlineColor = new float[]{1,0,0,1};
	public static float[] color = new float[]{1,1,1,0.5f};
	public static float textSize = 0.03f;
	public static float spaceSize = 0.01f;
	
	public boolean active = false;
	public int index = 0;
	
	public float[] coordsTop;
	public float[] coordsBottom;
	public double[] size;
	public String text;
	
	public String displayedText = "didn't work";
	
	public byte alignment = 0;
	
	public TextBox(float[] coordsTop,float[] coordsBottom,String text){
		this.coordsTop = coordsTop;
		this.coordsBottom = coordsBottom;
		this.text = text;
		size = new double[]{Math.abs(coordsTop[0]-coordsBottom[0]),Math.abs(coordsTop[1]-coordsBottom[1])};
		updateSpot();
		ClientProxy.textBoxs.add(this);
	}
		
	public void updateSpot(){
		//System.out.println("Index: "+index+" Other: "+size[0]+','+textSize);
		if((text.length()-index)*textSize>size[0]){
			displayedText = text.substring(index, (int)(size[0]/textSize)+index);		
		}else{
			displayedText = text.substring(index);
		}
	}
	
	public void moveSpot(boolean leftorright){
		if(leftorright){
			//System.out.println("COORDS TOP: "+coordsTop[0]+','+coordsTop[1]+"   COORDS BOTTOM: "+coordsBottom[0]+','+coordsBottom[1]);
			
		if(((text.length()-index)*textSize)>size[0]){
			System.out.println("THIS SHOULDN'T BE HAPPENING");
			index++;
			//System.out.println("Size: "+size[0]+','+size[1]+" index: "+index+" textSize: "+textSize);
			displayedText = text.substring(index, (int)(size[0]/textSize)+index);
			
			if(((int)(size[0]/textSize)+index)==text.length())
				displayedText = text.substring(index);
		}else{
			displayedText = text.substring(index);
			
		}
		}else{
			if(index > 0){
				index--;
				displayedText = text.substring(index, (int)(size[0]/textSize)+index);
			}
		}
		
		
	}
	
	public double blitat;
	public void render(){
		float[] mousePos = ClientProxy.mousePos;
		if(AnimationEditorGui.newEventButton && AnimationEditorGui.eventButton == AnimationEditorGui.button && Mouse.getEventButtonState()){
			if(mousePos[0]>=coordsTop[0] && mousePos[1]<=coordsTop[1] && mousePos[0]<=coordsBottom[0] && mousePos[1]>=coordsBottom[1]){
				active = true;
			}else{
				active = false;
			}
		}
		if(active){
			//System.out.println("active");
			if(tick >= 2){
				tick = 0;
				if(ClientProxy.rightPressed){
					moveSpot(true);
				}else if(ClientProxy.leftPressed){
					moveSpot(false);
				}
			}
			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glColor4f(outlineColor[0],outlineColor[1],outlineColor[2],outlineColor[3]);
			GL11.glVertex2f(coordsBottom[0],coordsTop[1]);
			GL11.glVertex2f(coordsTop[0], coordsTop[1]);
			GL11.glVertex2f(coordsTop[0], coordsBottom[1]);
			GL11.glVertex2f(coordsBottom[0], coordsBottom[1]);
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glEnd();
		}
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(color[0],color[1],color[2],color[3]);
			GL11.glVertex2f(coordsBottom[0],coordsTop[1]);
			GL11.glVertex2f(coordsTop[0], coordsTop[1]);
			GL11.glVertex2f(coordsTop[0], coordsBottom[1]);
			GL11.glVertex2f(coordsBottom[0], coordsBottom[1]);
			GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnd();
		
		blitat = coordsTop[0];
		if(alignment == 1){
			blitat+=(size[0]-(displayedText.length()*textSize))/2;
		}else if(alignment == 2){
			blitat+=size[0]-(displayedText.length()*textSize);
		}
		Main.drawString(displayedText, new double[]{blitat,coordsTop[1]}, new double[]{displayedText.length()*textSize,size[1]}, spaceSize, "");
		
		//System.out.println("DISPlayed TEXT: "+displayedText);
	}
	
	public void updateCoords(float[] v1,float[] v2){
		coordsTop = v1;
		coordsBottom = v2;
		size = new double[]{Math.abs(coordsTop[0]-coordsBottom[0]),Math.abs(coordsTop[1]-coordsBottom[1])};
		updateSpot();
	}

}
