package edu.depauw.csc480.jdbc;

import java.sql.*;

import org.apache.derby.jdbc.EmbeddedDriver;

public class CreateGameDB {
	public static void main(String[] args) {
		String url = "jdbc:derby:db/gamedb;create=true";
		Driver d = new EmbeddedDriver();

		try (Connection conn = d.connect(url, null)) {
			// First clean up from previous runs, if any
			dropConstraints(conn);
			dropTables(conn);

			// Now create the schema without constraints
			addTables(conn);

			// Insert some bulk data
			insertData(conn);

			// Finally add in the constraints
			addConstraints(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void doUpdate(Connection conn, String statement, String message) {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(statement);
			System.out.println(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void doUpdateNoError(Connection conn, String statement, String message) {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(statement);
			System.out.println(message);
		} catch (SQLException e) {
			// Ignore error
		}
	}

	private static void addTables(Connection conn) {
		doUpdate(conn,
				"create table GAMES(game_id int, title varchar(100), release_year int, developer varchar(50), platform varchar(100), genre varchar(50), primary key (game_id))",
				"Table GAMES created.");

		doUpdate(conn,
				"create table PLAYERS(player_id int, username varchar(50), email varchar(100), age int, primary key (player_id))",
				"Table PLAYERS created.");

		doUpdate(conn,
				"create table GAME_PLAYERS(game_player_id int, game_id int, player_id int, playtime_hour int, primary key (game_player_id))",
				"Table GAME_PLAYERS created.");

		doUpdate(conn,
				"create table REVIEWS(review_id int, game_id int, player_id int, rating int, comment varchar(1000), primary key (review_id))",
				"Table REVIEWS created.");
	}

	private static void addConstraints(Connection conn) {
		doUpdate(conn,
				"alter table GAME_PLAYERS add constraint fk_game_players_games foreign key (game_id) references GAMES",
				"Added foreign key GAME_PLAYERS->GAMES.");

		doUpdate(conn,
				"alter table GAME_PLAYERS add constraint fk_game_players_players foreign key (player_id) references PLAYERS",
				"Added foreign key GAME_PLAYERS->PLAYERS.");

		doUpdate(conn, "alter table REVIEWS add constraint fk_reviews_games foreign key (game_id) references GAMES",
				"Added foreign key REVIEWS->GAMES.");

		doUpdate(conn,
				"alter table REVIEWS add constraint fk_reviews_players foreign key (player_id) references PLAYERS",
				"Added foreign key REVIEWS->PLAYERS.");
	}

	private static void dropTables(Connection conn) {
		doUpdateNoError(conn, "drop table GAMES", "Table GAMES dropped.");
		doUpdateNoError(conn, "drop table PLAYERS", "Table PLAYERS dropped.");
		doUpdateNoError(conn, "drop table GAME_PLAYERS", "Table GAME_PLAYERS dropped.");
		doUpdateNoError(conn, "drop table REVIEWS", "Table REVIEWS dropped.");
	}

	private static void dropConstraints(Connection conn) {
		doUpdateNoError(conn, "alter table GAME_PLAYERS drop constraint fk_game_players_games",
				"Dropped foreign key GAME_PLAYERS->GAMES.");
		doUpdateNoError(conn, "alter table GAME_PLAYERS drop constraint fk_game_players_players",
				"Dropped foreign key GAME_PLAYERS->PLAYERS.");
		doUpdateNoError(conn, "alter table REVIEWS drop constraint fk_reviews_games",
				"Dropped foreign key REVIEWS->GAMES.");
		doUpdateNoError(conn, "alter table REVIEWS drop constraint fk_reviews_players",
				"Dropped foreign key REVIEWS->PLAYERS.");
	}

	private static void insertData(Connection conn) {
		try (Statement stmt = conn.createStatement()) {
			String[] gamesvals = {
					"(1, 'LOL', 2010, 'Riot Games', 'Windows & MacOS','MOBA')",
					"(2, 'CSGO', 2009, 'Blizzard', 'Windows','FPS')",
					"(3, 'Yugioh!', 2000, 'Konami', 'MacOS','TCG')",
			};
			for (String val : gamesvals) {
				stmt.executeUpdate(
						"insert into GAMES(game_id, title , release_year , developer, platform, genre) values " + val);
			}
			System.out.println("GAMES records inserted.");

			String[] playersvals = {
					"(1, 'Andy','Andy@gamer.com',18)",
					"(2, 'Bunny','Bunny@gamer.com',19)",
					"(3, 'Charles','Charles@gamer.com',20)",
					"(4, 'Doug','Doug@gamer.com',21)"
			};
			for (String val : playersvals) {
				stmt.executeUpdate("insert into PLAYERS(player_id , username, email, age) values " + val);
			}
			System.out.println("PLAYERS records inserted.");

			String[] gamesplayersvals = {
					"(1, 1, 1, 100)",
					"(2, 2, 1, 150)",
					"(3, 2, 2, 300)",
					"(4, 3, 3, 50)",
					"(5, 3, 4, 100)",
					"(6, 1, 4, 80)"
			};
			for (String val : gamesplayersvals) {
				stmt.executeUpdate(
						"insert into GAME_PLAYERS(game_player_id, game_id, player_id, playtime_hour) values " + val);
			}
			System.out.println("GAME_PLAYERS records inserted.");

			String[] reviewsvals = {
					"(1, 1, 1, 9, 'This game is exciting!')",
					"(2, 2, 2, 3, 'A lot of cheat and tool players')",
					"(3, 3, 3, 10, 'The best TCG game. Must try!')",
					"(4, 3, 4, 1, 'Pay to win game. Pls Avoid!')"
			};
			for (String val : reviewsvals) {
				stmt.executeUpdate("insert into REVIEWS(review_id, game_id, player_id, rating, comment) values " + val);
			}
			System.out.println("REVIEWS records inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
