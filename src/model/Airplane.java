package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.AppData;
import components.Airfields;

public class Airplane extends PhysicalObject {

	private static Logger logger = AppData.logger;

	// backend
	private float currentFuelState;
	private float distanceFromDestination;
	private Random r = new Random();
	private Airfields airfields;
	private float angle;
	private float totalDistance;
	private final float PLANE_MOVE_DT = 0.005f;
	
	private Airfield destinationAirfield;
	private Airfield landedAirfield;
	
	private boolean IsPaused; 

	public Airplane(int id, Airfields airfields) {
		this.id = id;
		this.airfields = airfields;
		this.landedAirfield = getRandomAirfield();
		this.xPos = landedAirfield.getXPos();
		this.yPos = landedAirfield.getYPos();
		currentFuelState = AppData.AIRPLANES.STARTING_FUEL;
	}

	public void run() {

		logger.info(String.format("%1$s uruchomiony!", getName()));

		while (true) {
			destinationAirfield = getRandomAirfield(landedAirfield);			
			destinationAirfield = airfields.getAirfieldById(destinationAirfield.getId());
			UpdateTotalDistance();

			logger.info(this + " -> " + destinationAirfield);

			releaseAirfield();

			updateDistance();

			do {
				if(!IsPaused){
					move();
				}			
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					throw new IllegalAccessError();
				}
			} while (distanceFromDestination > AppData.AIRFIELDS.WAITING_DISTANCE);
			logger.info(String.format("%1$s jest w pobli�u %2$s", getName(), destinationAirfield.getName()));

			boolean tak = true;
			do {
				if(!IsPaused){
					if (destinationAirfield.isOccupied()) {
						tak = destinationAirfield.isOccupied();
						if (!destinationAirfield.getWaitingAirplanes().contains(this)) {
							destinationAirfield.addToQueue(this);
						}
					} else {
						tak = false;
						if (destinationAirfield.getWaitingAirplanes().contains(this)) {
							destinationAirfield.getWaitingAirplanes().remove(this);
						}
						destinationAirfield.setOccupied(true);
						updateLandedAirfield();
						fillingUp();
						wait(AppData.AIRPLANES.REFUEL_TIME);
					}
				}
				wait(AppData.AIRFIELDS.WAITING_TIME);
			} while (tak);
		}
	}

	private void wait(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void move() {
		repositionPlane();
		consumeFuel();
	}

	private void consumeFuel() {
		currentFuelState -= .5 * (AppData.AIRPLANES.VELOCITY - 99);
	}

	private void repositionPlane() {
		float startingPositionX = landedAirfield.getXPos();
		float startingPositionY = landedAirfield.getYPos();

		float destX = destinationAirfield.getXPos();
		float destY = destinationAirfield.getYPos();

		float distX = destX - startingPositionX;
		float distY = destY - startingPositionY;

		angle = (float) Math.atan2(distY, distX);

		float dx = (float) (AppData.AIRPLANES.VELOCITY * PLANE_MOVE_DT * Math.cos(angle));
		float dy = (float) (AppData.AIRPLANES.VELOCITY * PLANE_MOVE_DT * Math.sin(angle));

		xPos += dx;
		yPos += dy;

		updateDistance();
	}

	private void releaseAirfield() {
//		logger.log(Level.INFO, String.format("Samolot %1$d zwalnia lotnisko %2$d", id, landedAirfield.getId()));
		landedAirfield.setOccupied(false);
	}

	private void updateDistance() {
		int destX = (int) (destinationAirfield.getXPos());
		int destY = (int) (destinationAirfield.getYPos());

		double distance = Math.hypot(destX - xPos, destY - yPos);

		distanceFromDestination = (float) Math.round(distance);
	}
	
	private void UpdateTotalDistance() {
		int destX = (int) (destinationAirfield.getXPos());
		int destY = (int) (destinationAirfield.getYPos());
		
		totalDistance += Math.hypot(destX - xPos, destY - yPos);
	}

	private void updateLandedAirfield() {
		landedAirfield = destinationAirfield;
		xPos = destinationAirfield.getXPos();
		yPos = destinationAirfield.getYPos();
	}

	private void fillingUp() {
		currentFuelState = AppData.AIRPLANES.MAX_FUEL;
	}

	public float currentFuelState() {
		return currentFuelState;
	}

	private Airfield getRandomAirfield() {
		return airfields.getAirfieldById(r.nextInt(airfields.getAirfields().size()) + 1);
	}
	
	private Airfield getRandomAirfield(Airfield restrictedAirField) {
		Airfield airfield;
		do {
			airfield = getRandomAirfield();
		} while(airfield == restrictedAirField);
		return airfield;
	}

	public boolean isIsPaused() {
		return IsPaused;
	}

	public void setIsPaused(boolean isPaused) {
		IsPaused = isPaused;
	}

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	
	
	public float getTotalDistance() {
		return totalDistance;
	}

	public float getDistanceFromDestination() {
		return distanceFromDestination;
	}

	public void setDistanceFromDestination(float distanceFromDestination) {
		this.distanceFromDestination = distanceFromDestination;
	}

	public Airfield getDestinationAirfield() {
		return destinationAirfield;
	}

	public void setDestinationAirfield(Airfield destinationAirfield) {
		this.destinationAirfield = destinationAirfield;
	}

	public Airfield getLandedAirfield() {
		return landedAirfield;
	}

	public void setLandedAirfield(Airfield landedAirfield) {
		this.landedAirfield = landedAirfield;
	}

	@Override
	public String getName() {
		return "Samolot_" + id;
	}

	@Override
	public String toString() {
		return String.format("%1$s[%2$f, %3$f]", getName(), xPos, yPos);
	}
}
