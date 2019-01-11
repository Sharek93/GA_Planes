package model;

import java.awt.Graphics;

public abstract class PhysicalObject {

	protected int id;
	protected float xPos;
	protected float yPos;
	
	public abstract String getName();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getXPos() {
		return xPos;
	}
	public void setXPos(float xPos) {
		this.xPos = xPos;
	}
	
	public float getYPos() {
		return yPos;
	}
	public void setYPos(float yPos) {
		this.yPos = yPos;
	}
}
