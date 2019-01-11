package model;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import app.AppData;

public class Airfield extends PhysicalObject {
	
	private static Logger logger = AppData.logger;
	
	//backend
	private String city;
	private boolean isOccupied;
	private List<Airplane> waitingAirplanes;
	
	private int airstripCount;
	
	public Airfield(int id, int x, int y) {
		this.id = id;
		this.city = AppData.getCity(); 
		xPos = x;
		yPos = y;
		waitingAirplanes = new ArrayList<>();
		logger.info(this + " is ready!");
	}

	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Airplane> getWaitingAirplanes() {
		return waitingAirplanes;
	}

	public void setWaitingAirplanes(List<Airplane> waitingAirplanes) {
		this.waitingAirplanes = waitingAirplanes;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public void setPos(int x, int y) {
		this.xPos = x;
		this.yPos = y;
	}

	public void addToQueue(Airplane airplane) {
		waitingAirplanes.add(airplane);
	}

	@Override
	public String getName() {
		return "Lotnisko_" + id;
	}
	
	@Override
	public String toString() {
		return String.format("%1$s[%2$f, %3$f]", getName(), xPos, yPos);
	}
}
