package edu.depauw.csc480.jdbc;

import java.sql.*;
import org.apache.derby.jdbc.EmbeddedDriver;

public class BestRatedGame {
	public static void main(String[] args) {
		String url = "jdbc:derby:db/gamedb";
		String qry = "SELECT title, AVG(rating) AS average_rating FROM GAMES INNER JOIN REVIEWS ON GAMES.game_id = REVIEWS.game_id GROUP BY title ORDER BY average_rating DESC";

		Driver d = new EmbeddedDriver();
		try (Connection conn = d.connect(url, null);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(qry)) {
			System.out.println("Game\t\tRating");
			System.out.println("-----------------------------");
			while (rs.next()) {
				String title = rs.getString("title");
				int rating = rs.getInt("average_rating");
				System.out.println(title + "\t\t" + rating);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
