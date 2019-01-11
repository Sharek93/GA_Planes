package components;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.AppData;
import model.Airfield;

public class Airfields {
	
	private final int MAP_WIDTH = 640;
	private final int MAP_HEIGHT = 480;
	private List<Airfield> airfields;
	private Random r = new Random();
	

	public Airfields() {
		airfields = new ArrayList<>();
		createAirFields();
	}

	private void createAirFields() {
		
		for (int i = 1; i <= AppData.AIRFIELDS.COUNT; i++) {
			boolean farEnough;
			int xPos;
			int yPos;
			
			do {
				farEnough = true;
				
				xPos = r.nextInt(MAP_WIDTH);
				yPos = r.nextInt(MAP_HEIGHT);

				boolean isOnLand;
				do {
					isOnLand = true;
					
					xPos = r.nextInt(MAP_WIDTH);
					yPos = r.nextInt(MAP_HEIGHT);
										
				}while(!isOnLand);
				
				for (Airfield airfield : airfields) {
					if (Math.abs(Math.hypot(airfield.getXPos() - xPos, airfield.getYPos() - yPos)) < AppData.AIRFIELDS.DISTANCE_FROM_ANOTHER_AIRFIELD) {
						farEnough = false;
					}
				}
				
			}while(!farEnough);
			
			airfields.add(new Airfield(i, xPos, yPos));
		}
	}
	
	public List<Airfield> getAirfields() {
		return airfields;
	}

	public void setAirfields(List<Airfield> airfields) {
		this.airfields = airfields;
	}
	
	public Airfield getAirfieldById(int id) {
		return airfields.get(id - 1); 	
	}
}
