package edu.depauw.csc480.projectv1;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import org.apache.derby.jdbc.EmbeddedDriver;

public class Main {
	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;

	public static void main(String[] args) {
		try (Connection conn = getConnection("jdbc:derby:db/gamedb")) {
			displayMenu();
			loop: while (true) {
				switch (requestString("Selection (0 to quit, 9 for menu)? ")) {
				case "0": // Quit
					break loop;

				case "1": // Reset
					resetTables(conn);
					break;

				case "2": // List games
					listGames(conn);
					break;

				case "3": // List reivews
					listPlayersInGames(conn);
					break;

				case "4": // List players
					listReviews(conn);
					break;

				case "5": // Add game
					addGames(conn);
					break;

				case "6": // Add Player
					addPlayer(conn);
					break;

				case "7": // Add Game Player
					addGamePlayers(conn);
					break;

				case "8": // Add Review
					addReview(conn);
					break;
				default:
					displayMenu();
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("Done");
	}

	private static Connection getConnection(String url) {
		Driver driver = new EmbeddedDriver();

		// try to connect to an existing database
		Properties prop = new Properties();
		prop.put("create", "false");
		try {
			Connection conn = driver.connect(url, prop);
			return conn;
		} catch (SQLException e) {
			// database doesn't exist, so try creating it
			try {
				prop.put("create", "true");
				Connection conn = driver.connect(url, prop);
				createTables(conn);
				return conn;
			} catch (SQLException e2) {
				throw new RuntimeException("cannot connect to database", e2);
			}
		}
	}

	private static void displayMenu() {
		out.println("0: Quit");
		out.println("1: Reset tables");
		out.println("2: List Games");
		out.println("3: List Players and Games Playing");
		out.println("4: List Reviews");
		out.println("5: Add Game");
		out.println("6: Add Player");
		out.println("7: Add Game_Player");
		out.println("8: Add Review");
	}

	private static String requestString(String prompt) {
		out.print(prompt);
		out.flush();
		return in.nextLine();
	}

	private static void createTables(Connection conn) {
		// First clean up from previous runs, if any
		dropTables(conn);

		// Now create the schema
		addTables(conn);
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
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE GAMES (");
		sb.append("  game_id INT,");
		sb.append("  title VARCHAR(100),");
		sb.append("  release_year INT,");
		sb.append("  developer VARCHAR(50),");
		sb.append("  platform VARCHAR(100),");
		sb.append("  genre VARCHAR(50),");
		sb.append("  PRIMARY KEY (game_id)");
		sb.append(")");
		doUpdate(conn, sb.toString(), "Table GAMES created.");

		sb = new StringBuilder();
		sb.append("create table PLAYERS(");
		sb.append("  player_id int,");
		sb.append("  username varchar(50),");
		sb.append("  email varchar(100),");
		sb.append("  age int,");
		sb.append("  primary key (player_id)");
		sb.append(")");
		doUpdate(conn, sb.toString(), "Table PLAYERS created.");

		sb = new StringBuilder();
		sb.append("create table GAME_PLAYERS(");
		sb.append("  game_player_id int,");
		sb.append("  game_id int,");
		sb.append("  player_id int,");
		sb.append("  playtime_hour int,");
		sb.append("  primary key (game_player_id),");
		sb.append("  foreign key (game_id) references GAMES(game_id) on delete cascade,");
		sb.append("  foreign key (player_id) references PLAYERS(player_id) on delete cascade");
		sb.append(")");
		doUpdate(conn, sb.toString(), "Table GAME_PLAYERS created.");

		sb = new StringBuilder();
		sb.append("create table REVIEWS(");
		sb.append("  review_id int,");
		sb.append("  game_id int,");
		sb.append("  player_id int,");
		sb.append("  rating int,");
		sb.append("  comment varchar(1000),");
		sb.append("  primary key (review_id),");
		sb.append("  foreign key (game_id) references GAMES(game_id) on delete cascade,");
		sb.append("  foreign key (player_id) references PLAYERS(player_id) on delete cascade");
		sb.append(")");
		doUpdate(conn, sb.toString(), "Table REVIEWS created.");
	}

	private static void dropTables(Connection conn) {
		doUpdateNoError(conn, "drop table REVIEWS", "Table REVIEWS dropped.");
		doUpdateNoError(conn, "drop table GAME_PLAYERS", "Table GAME_PLAYERS dropped.");
		doUpdateNoError(conn, "drop table PLAYERS", "Table PLAYERS dropped.");
		doUpdateNoError(conn, "drop table GAMES", "Table GAMES dropped.");
	}

	private static void resetTables(Connection conn) {
		try (Statement stmt = conn.createStatement()) {
			int count = 0;
			count += stmt.executeUpdate("delete from REVIEWS");
			count += stmt.executeUpdate("delete from GAME_PLAYERS");
			count += stmt.executeUpdate("delete from PLAYERS");
			count += stmt.executeUpdate("delete from GAMES");
			System.out.println(count + " records deleted");

			String[] gamesvals = {
					"(1, 'LOL', 2010, 'Riot Games', 'Windows & MacOS','MOBA')",
					"(2, 'CSGO', 2009, 'Blizzard', 'Windows','FPS')",
					"(3, 'Yugioh!', 2000, 'Konami', 'MacOS','TCG')",
			};
			count = 0;
			for (String val : gamesvals) {
				count += stmt.executeUpdate(
						"insert into GAMES(game_id, title , release_year , developer, platform, genre) values " + val);
			}
			System.out.println(count + " GAMES records inserted.");

			String[] playersvals = {
					"(1, 'Andy','Andy@gamer.com',18)",
					"(2, 'Bunny','Bunny@gamer.com',19)",
					"(3, 'Charles','Charles@gamer.com',20)",
					"(4, 'Doug','Doug@gamer.com',21)"
			};
			count = 0;
			for (String val : playersvals) {
				count += stmt.executeUpdate("insert into PLAYERS(player_id , username, email, age) values " + val);
			}
			System.out.println(count + " PLAYERS records inserted.");

			String[] gamesplayersvals = {
					"(1, 1, 1, 100)",
					"(2, 2, 1, 150)",
					"(3, 2, 2, 300)",
					"(4, 3, 3, 50)",
					"(5, 3, 4, 100)",
					"(6, 1, 4, 80)"
			};
			count = 0;
			for (String val : gamesplayersvals) {
				count += stmt.executeUpdate(
						"insert into GAME_PLAYERS(game_player_id, game_id, player_id, playtime_hour) values " + val);
			}
			System.out.println(count + " GAME_PLAYERS records inserted.");

			String[] reviewsvals = {
					"(1, 1, 1, 9, 'This game is exciting!')",
					"(2, 2, 2, 3, 'A lot of cheat and tool players')",
					"(3, 3, 3, 10, 'The best TCG game. Must try!')",
					"(4, 3, 4, 1, 'Pay to win game. Pls Avoid!')"
			};
			count = 0;
			for (String val : reviewsvals) {
				count += stmt.executeUpdate(
						"insert into REVIEWS(review_id, game_id, player_id, rating, comment) values " + val);
			}
			System.out.println(count + " REVIEWS records inserted.");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void listGames(Connection conn) {
		StringBuilder query = new StringBuilder();
		query.append("select g.game_id, g.title, g.developer, g.genre");
		query.append("  from GAMES g");

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query.toString())) {
			out.printf("%-3s %-10s %-15s %-8s\n", "Id", "Title", "Developer", "Genre");
			out.println("----------------------------------------");
			while (rs.next()) {
				int id = rs.getInt("game_id");
				String title = rs.getString("title");
				String developer = rs.getString("developer");
				String genre = rs.getString("genre");

				out.printf("%-3s %-10s %-15s %-8s\n", id, title, developer, genre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void listPlayersInGames(Connection conn) {
		StringBuilder query = new StringBuilder();
		query.append("select g.game_id, g.title, p.player_id, p.username, gp.game_id, gp.player_id, gp.playtime_hour");
		query.append(" from GAMES g, PLAYERS p, GAME_PLAYERS gp ");
		query.append("where (g.game_id = gp.game_id) and (p.player_id = gp.player_id)");

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query.toString())) {
			out.printf("%-10s %-15s %-15s\n", "Player", "Game Playing", "Playing Time");
			out.println("----------------------------------------");
			while (rs.next()) {
				String username = rs.getString("username");
				String game = rs.getString("title");
				int time = rs.getInt("playtime_hour");

				out.printf("%-10s %-15s %-15s\n", username, game, time);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void listReviews(Connection conn) {
		StringBuilder query = new StringBuilder();
		query.append("select g.game_id, g.title, p.player_id, p.username, r.game_id, r.player_id, r.rating, r.comment");
		query.append(" from GAMES g, PLAYERS p, REVIEWS r ");
		query.append("where (g.game_id = r.game_id) and (p.player_id = r.player_id)");

		try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query.toString())) {
			out.printf("%-10s %-15s %-15s %-15s\n", "Player", "Game Playing", "Rating (/10)", "Comment");
			out.println("---------------------------------------------------------------");
			while (rs.next()) {
				String username = rs.getString("username");
				String game = rs.getString("title");
				int rating = rs.getInt("rating");
				String comment = rs.getString("comment");

				out.printf("%-10s %-15s %-15s %-15s\n", username, game, rating, comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void addGames(Connection conn) {
		String game_id = requestString("Game_id number? ");
		String title = requestString("Game title? ");
		String release = requestString("Release year? ");
		String developer = requestString("Developer? ");
		String platform = requestString("Platform? ");
		String genre = requestString("Genre? ");

		StringBuilder command = new StringBuilder();
		// how to use auto_increment for game_id
		command.append("INSERT INTO GAMES(game_id, title, release_year, developer, platform, genre) ");
		command.append("  VALUES (?, ?, ?, ?, ?, ?)");

		try (PreparedStatement pstmt = conn.prepareStatement(command.toString())) {
			pstmt.setString(1, game_id);
			pstmt.setString(2, title);
			pstmt.setString(3, release);
			pstmt.setString(4, developer);
			pstmt.setString(5, platform);
			pstmt.setString(6, genre);

			int count = pstmt.executeUpdate();

			out.println(count + " game(s) inserted");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void addPlayer(Connection conn) {
		String player_id = requestString("PlayerId number? ");
		String username = requestString("Username? ");
		String email = requestString("Email? ");
		String age = requestString("Age? ");

		StringBuilder command = new StringBuilder();
		command.append("insert into PLAYERS(player_id, username, email, age) VALUES (?, ?, ?, ?)");

		try (PreparedStatement pstmt = conn.prepareStatement(command.toString())) {
			pstmt.setString(1, player_id);
			pstmt.setString(2, username);
			pstmt.setString(3, email);
			pstmt.setString(4, age);

			int count = pstmt.executeUpdate();

			out.println(count + " record(s) inserted");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void addGamePlayers(Connection conn) {
		String game_players_id = requestString("Game_player_id number ? ");
		String player_id = requestString("PlayerId number? ");
		String game_id = requestString("GameId number? ");
		String playtime = requestString("Playtime(hour) ? ");

		StringBuilder command = new StringBuilder();
		command.append(
				"insert into GAME_PLAYERS(game_player_id, game_id, player_id, playtime_hour) VALUES (?, ?, ?, ?)");

		try (PreparedStatement pstmt = conn.prepareStatement(command.toString())) {
			pstmt.setString(1, game_players_id);
			pstmt.setString(2, game_id);
			pstmt.setString(3, player_id);
			pstmt.setString(4, playtime);

			int count = pstmt.executeUpdate();

			out.println(count + " record(s) inserted");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void addReview(Connection conn) {
		String review_id = requestString("Review_id number ? ");
		String game_id = requestString("Game_id number? ");
		String player_id = requestString("Player_id number? ");
		String rating = requestString("Rating(/10) ? ");
		String comment = requestString("Comment ? ");

		StringBuilder command = new StringBuilder();
		command.append("insert into REVIEWS(review_id , game_id, player_id, rating, comment) VALUES (?, ?, ?, ?, ?)");

		try (PreparedStatement pstmt = conn.prepareStatement(command.toString())) {
			pstmt.setString(1, review_id);
			pstmt.setString(2, game_id);
			pstmt.setString(3, player_id);
			pstmt.setString(4, rating);
			pstmt.setString(5, comment);

			int count = pstmt.executeUpdate();

			out.println(count + " record(s) inserted");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
