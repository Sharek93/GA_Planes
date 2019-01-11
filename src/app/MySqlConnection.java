package app;

import java.sql.*;

import components.Airplanes;
import model.Airplane;

public class MySqlConnection {

	public static void SavePlanes(Airplanes airplanes) {
		System.out.println("saving planes...");

		Connection con;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost/sys", "root", "123qwe");

			String query = "insert into `sys`.`planes` (`planeID`, `from`, `to`, `fuel`, `distanceTo`, `totalDistance`)"
					+ " values (?, ?, ?, ?, ?, ?)";

			PreparedStatement preparedStmt = con.prepareStatement(query);

			for (Airplane plane : airplanes.getAirplanes()) {

				System.out.println(plane.getId() + " " + String.valueOf(plane.getLandedAirfield().getCity()) + " "
						+ String.valueOf(plane.getDestinationAirfield().getCity()) + " "
						+ String.valueOf(plane.currentFuelState()) + " "
						+ String.valueOf(plane.getDistanceFromDestination()) + " "
						+ String.valueOf(plane.getTotalDistance()));

				preparedStmt.setInt(1, plane.getId());
				preparedStmt.setString(2, String.valueOf(plane.getLandedAirfield().getCity()));
				preparedStmt.setString(3, String.valueOf(plane.getDestinationAirfield().getCity()));
				preparedStmt.setString(4, String.valueOf(plane.currentFuelState()));
				preparedStmt.setString(5, String.valueOf(plane.getDistanceFromDestination()));
				preparedStmt.setString(6, String.valueOf(plane.getTotalDistance()));

				preparedStmt.execute();
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
