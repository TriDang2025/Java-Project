package edu.depauw.csc480.jdbc;

import java.sql.*;
import java.util.Scanner;

import org.apache.derby.jdbc.EmbeddedDriver;

public class ChangeUserName {
	public static void main(String[] args) {
		System.out.print("Enter current username: ");
		Scanner sc = new Scanner(System.in);
		String currUN = sc.nextLine();
		System.out.print("Enter new username: ");
		String newUN = sc.nextLine();
		sc.close();

		String url = "jdbc:derby:db/gamedb";
		String cmd = "update PLAYERS set username='" + newUN + "' where username='" + currUN + "'";

		Driver d = new EmbeddedDriver();
		try (Connection conn = d.connect(url, null); Statement stmt = conn.createStatement()) {
			int howmany = stmt.executeUpdate(cmd);
			System.out.println(howmany + " records changed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
