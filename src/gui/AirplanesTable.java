package gui;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import app.AppData;
import components.Airplanes;
import model.Airplane;

public class AirplanesTable extends JTable {

	private DefaultTableModel model;

	public AirplanesTable(Airplanes airplanes, DefaultTableModel model) {
		super(model);
		this.model = model;

		for (int i = 0; i < model.getColumnCount(); i++) {
			getColumnModel().getColumn(i).setPreferredWidth(AppData.GUI.CANVAS_WIDTH / model.getColumnCount());
		}

		refresh(airplanes);
	}

	public void refresh(Airplanes airplanes) {

		
		for (int i = model.getRowCount() -1; i >= 0; i--) {
			model.removeRow(i);
		}				
		
		for (Airplane airplane : airplanes.getAirplanes()) {
			System.out.println("aiplane " + airplane.getId() + " - Xpos:" + airplane.getxPos());
			model.addRow(new String[] { 
					String.valueOf(airplane.getId()) + " (x: " + airplane.getxPos() + " y: " + airplane.getyPos() + ")", 
					airplane.getLandedAirfield().getCity(),
					airplane.getDestinationAirfield() != null ? airplane.getDestinationAirfield().getCity() : "nie wybrano",
					airplane.currentFuelState() + "/" + AppData.AIRPLANES.MAX_FUEL,
					String.valueOf(airplane.getDistanceFromDestination()) + " KM",
					String.valueOf(airplane.getTotalDistance()) + " KM"});
			}
		this.setModel(model);
	}
}
