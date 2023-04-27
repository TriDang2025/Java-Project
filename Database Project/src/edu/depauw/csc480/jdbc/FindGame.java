package edu.depauw.csc480.jdbc;

import java.sql.*;
import java.util.Scanner;

import org.apache.derby.jdbc.EmbeddedDriver;

public class FindGame {
	public static void main(String[] args) {
		System.out.print("Enter a game name: ");
		Scanner sc = new Scanner(System.in);
		String game = sc.next();
		sc.close();

		String qry = "select username, comment " + "from games g, reviews r, players p "
				+ "where g.game_id = r.game_id and r.player_id = p.player_id " + "and title = '" + game + "'";

		String url = "jdbc:derby:db/gamedb";
		Driver d = new EmbeddedDriver();
		try (Connection conn = d.connect(url, null);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(qry)) {

			System.out.println("Here are the reviews for " + game);
			System.out.println("Player\t\tComment");
			System.out.println("------------------------------------------");
			while (rs.next()) {
				String username = rs.getString("username");
				String comment = rs.getString("comment");
				System.out.println(username + "\t\t" + comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
