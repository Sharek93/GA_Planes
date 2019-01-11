package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class AppData {
	
	public static String APPNAME;

	public static boolean SHOW_LOGGER;
	
	public static class AIRPLANES {
		public static int SIZE;
		public static int COUNT;
		public static int MAX_FUEL;
		public static int STARTING_FUEL;
		public static int VELOCITY;
		public static int REFUEL_TIME;
	}
	
	public static class AIRFIELDS {
		public static int DEFAULT_STRIP_COUNT;
		public static int COUNT;
		public static int WAITING_DISTANCE;
		public static int WAITING_TIME;
		public static int DISTANCE_FROM_ANOTHER_AIRFIELD;
	}
	
	public static class GUI {
		public static String FONT_TYPE;
		public static int FRAME_RATE;
		public static int CANVAS_WIDTH;
		public static int CANVAS_HEIGHT;
		public static int FONT_SIZE;
		public static int BUFFERER_STRATEGY;
		public static float DT;
		public static boolean RESIZABLE;
	}
	
    public static Logger logger = Logger.getLogger("Symulacja");

	private static List<String> cities;
	private static Random r = new Random();
	
	public static void loadConfig() {
		
		logger.setUseParentHandlers(false);
		
		if (SHOW_LOGGER) {
			Handler conHdlr = new ConsoleHandler();
			conHdlr.setFormatter(new Formatter() {
				public String format(LogRecord record) {
					return record.getLevel() + ": ["
							+ record.getSourceClassName() + "]: "
							+ record.getMessage() + "\n";
				}
			});
			logger.addHandler(conHdlr);
		}
	}
	
	public static void loadCities() {
		cities = new ArrayList<>();
		try {
			BufferedReader bf = new BufferedReader(new FileReader("cities.txt"));
			String temp;
			while((temp = bf.readLine()) != null) {
				cities.add(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCity() {
		if (cities.isEmpty()) {
			loadCities();
		}
        return cities.get(r.nextInt(cities.size()));
	}
	
	static {
		 
    	String configFile = "config.properties";
        Properties p = new Properties();
		try(FileInputStream fis = new FileInputStream(configFile)) {
			p.load(fis);
		} catch (IOException e) {
			throw new IllegalStateException("Coudnt find config file [" + configFile + "]", e);
		}
		
		fillProperties(p);
		
	}

	private static void fillProperties(Properties p) {
		APPNAME = p.getProperty("appname");
		SHOW_LOGGER = Boolean.parseBoolean(p.getProperty("logger.show"));
		
		AIRPLANES.COUNT = Integer.parseInt(p.getProperty("airplanes.count"));
		AIRPLANES.MAX_FUEL = Integer.parseInt(p.getProperty("airplane.state.fuel.max"));
		AIRPLANES.STARTING_FUEL = Integer.parseInt(p.getProperty("airplane.state.fuel.starting"));
		AIRPLANES.VELOCITY = Integer.parseInt(p.getProperty("airplane.state.velocity"));
		AIRPLANES.REFUEL_TIME = Integer.parseInt(p.getProperty("airplane.refuel.time"));
		
		AIRFIELDS.COUNT = Integer.parseInt(p.getProperty("airfields.count"));
		AIRFIELDS.WAITING_DISTANCE = Integer.parseInt(p.getProperty("airfield.waiting.distance")); 
		AIRFIELDS.WAITING_TIME = Integer.parseInt(p.getProperty("airfield.waiting.time"));
		AIRFIELDS.DISTANCE_FROM_ANOTHER_AIRFIELD = Integer.parseInt(p.getProperty("airfield.distance.another.airfield"));
		AIRFIELDS.DEFAULT_STRIP_COUNT = Integer.parseInt(p.getProperty("airfield.strip.count"));
	
		
		GUI.FRAME_RATE = Integer.parseInt(p.getProperty("gui.framerate"));
		GUI.RESIZABLE = Boolean.parseBoolean(p.getProperty("gui.resizable"));
	}
}
