package components;

import java.util.ArrayList;
import java.util.List;

import app.AppData;
import model.Airplane;

public class Airplanes {

	private List<Airplane> airplanes;
	private Airfields airfields;

	public Airplanes(Airfields airfields) {
		this.airfields = airfields;
		airplanes = new ArrayList<>();
		createAirplanes();
	}

	private void createAirplanes() {
		for (int i = 1; i <= AppData.AIRPLANES.COUNT; i++) {
			Airplane airplane = new Airplane(i, airfields);
			airplanes.add(airplane);
		}
	}

	public List<Airplane> getAirplanes() {
		return airplanes;
	}
}
