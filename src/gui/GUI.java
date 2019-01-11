package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.AppData;
import app.MySqlConnection;
import components.Airfields;
import components.Airplanes;
import model.Airplane;
import javax.swing.JPanel;
import javax.swing.JButton;

public class GUI {
	
	private static Logger logger = AppData.logger;

	private final int WINDOW_WIDTH = 800;
	private final int WINDOW_HEIGHT = 600;
	private Frame mainFrame;
	
	private Airfields airfields;
	private Airplanes airplanes;

	private DefaultTableModel model;

	private AirplanesTable table;

	public GUI() {
		
		AppData.loadConfig();
		AppData.loadCities();
		
		initVariables();
		
		for (Airplane airplane : airplanes.getAirplanes()) {
		    new Thread(new Runnable() {
		    	public void run(){
		        	airplane.run();
		        }
		    }).start();
		}
		
		initUI();
		
	}

	private void initVariables() {
		
		logger.info("Witamy w aplikacji " + AppData.APPNAME);
		logger.info("Liczba samolotów: " + AppData.AIRPLANES.COUNT);
		logger.info("Liczba lotnisk: " + AppData.AIRFIELDS.COUNT);
		
		airfields = new Airfields();
		airplanes = new Airplanes(airfields);
	}

	private void initUI() {
		table = prepareTable();
		initFrame();
		initLayout();
		
		
	    new Thread(new Runnable() {
	    	public void run(){
	    		while(true) {	    			
	    			table.refresh(airplanes);
	    			try {
	    				Thread.sleep(1000/AppData.GUI.FRAME_RATE);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	    		}
	        }
	    }).start();
	}
	
	private void initFrame() {
		mainFrame = new Frame(AppData.APPNAME);
		mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		mainFrame.setResizable(AppData.GUI.RESIZABLE);
		
		table.refresh(airplanes);
	}
	
	private void initLayout() {
		mainFrame.setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane(table);
	
		mainFrame.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		{
			JButton startButton = new JButton("Start");
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					for (Airplane airplane : airplanes.getAirplanes()) {
						airplane.setIsPaused(false);
					}
				}
			});
			panel.add(startButton);
		}
		{
			JButton pauseButton = new JButton("Pause");
			pauseButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (Airplane airplane : airplanes.getAirplanes()) {
						airplane.setIsPaused(true);
					}
				}
			});
			panel.add(pauseButton);
		}	
		
		mainFrame.add(panel, BorderLayout.SOUTH);
		{
			JButton dbSaveButton = new JButton("Zapisz do bazy");
			dbSaveButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				MySqlConnection.SavePlanes(airplanes);					
				}
			});
			panel.add(dbSaveButton);
		}
		
		mainFrame.setVisible(true);
	}

	private AirplanesTable prepareTable() {
		String[] columns = new String[] {"ID", "SKĄD", "DOKĄD", "POZIOM PALIWA", "DYSTANS DO CELU", "CAŁKOWITY DYSTANS"};
		
		
		model = new DefaultTableModel();
		for (String colName : columns) {
			model.addColumn(colName);
		}
		
		for (Airplane airplane : airplanes.getAirplanes()) {
			model.addRow(new String[] 
					{
							String.valueOf(airplane.getId()),
							airplane.getLandedAirfield().getCity(),
							airplane.getDestinationAirfield() != null ? airplane.getDestinationAirfield().getCity() : "nie wybrano",
							airplane.currentFuelState() + "/" + AppData.AIRPLANES.MAX_FUEL,
							String.valueOf(airplane.getDistanceFromDestination()),
							"jeszcze nie ustalono"
					});
		}
		
		AirplanesTable table = new AirplanesTable(airplanes, model);
		table.setDefaultRenderer(String.class, new BoardTableCellRenderer());
		
		return table;
	}
	
	class BoardTableCellRenderer extends DefaultTableCellRenderer {
		
	}
}