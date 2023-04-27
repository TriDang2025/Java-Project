package edu.depauw.csc480.jdbc;

import java.sql.*;

import org.apache.derby.jdbc.EmbeddedDriver;

public class NumberOfPlayers {
	public static void main(String[] args) {
		String url = "jdbc:derby:db/gamedb";

		String qry = "SELECT title, COUNT(DISTINCT player_id) AS num_players" + " FROM GAMES"
				+ " INNER JOIN GAME_PLAYERS ON GAMES.game_id = GAME_PLAYERS.game_id " + " GROUP BY title ";

		Driver d = new EmbeddedDriver();
		try (Connection conn = d.connect(url, null);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(qry)) {
			System.out.println("Game\t\tNumber Of Player");
			System.out.println("------------------------------------");
			while (rs.next()) {
				String title = rs.getString("title");
				int rating = rs.getInt("num_players");
				System.out.println(title + "\t\t" + rating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}