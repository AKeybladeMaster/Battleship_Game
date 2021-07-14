package exGame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BattleshipField {

	Scanner sc = new Scanner(System.in);
	static int pc_wins_counter = 0, player_wins_counter = 0;

	String gameField[][] = new String[15][15];
	int row, column;
	static int ship_counter = 0, pc_ship_counter = 10;
	boolean inGame = true;

	public void generateGameField(String player_name) {
		System.out.println("Set your 10 ships.");

		for (int j = 0; j < 10; j++) {
			do {
				try {
					System.out.println("- Ship " + (ship_counter + 1) + " - ");
					do {
						System.out.print("Input a row number from 1 to 15: ");
						row = sc.nextInt();
						sc.nextLine();
					} while (row < 1 || row > 15);
					do {
						System.out.print("Input a column number from 1 to 15: ");
						column = sc.nextInt();
						sc.nextLine();
					} while (column < 1 || column > 15);

					if (checkPosition(gameField, --row, --column) == false) {
						System.out.println(
								"There's a ship nearby or you're trying to overload one's position. Try again.");
					} else {
						System.out.println("Ship added!\n");
					}
				} catch (InputMismatchException e) {
					System.out.println("I asked for a number. Sending you to the menu.");
					sc.nextLine();
					ship_counter = 0;
					clearMat(gameField);
					return;
				} catch (Exception e) {
					System.out.println("Error:\n" + e + "\nSending you to the menu.");
					sc.nextLine();
					ship_counter = 0;
					clearMat(gameField);
					return;
				}
			} while (checkPosition(gameField, row, column) == false);
			gameField[row][column] = "S";
			ship_counter++;
		}
		
		System.out.println("Computer adjusting its ships...\n");
		for (int q = 0; q < 10; q++) {
			do {
				row = ThreadLocalRandom.current().nextInt(0, 14);
				column = ThreadLocalRandom.current().nextInt(0, 14);

			} while (!checkPosition(gameField, row, column) || gameField[row][column] == "S"
					|| gameField[row][column] == "C");
			gameField[row][column] = "C";
		}
		System.out.println("- Use this for reference on computer's ships positions -\n");
		showMatClear(gameField);
		System.out.println("");

		playGame(player_name);
	}

	public void playGame(String player_name) {
		inGame = true;
		do {
			System.out.println(player_name + "'s turn - ");
			try {
				do {
					System.out.print("Input a row number from 1 to 15: ");
					row = sc.nextInt();
					sc.nextLine();
				} while (row < 1 || row > 15);
				do {
					System.out.print("Input a column number from 1 to 15: ");
					column = sc.nextInt();
					sc.nextLine();
				} while (column < 1 || column > 15);

				System.out.println(playerHitOrMiss(--row, --column) + "\n");

				row = ThreadLocalRandom.current().nextInt(0, 14);
				column = ThreadLocalRandom.current().nextInt(0, 14);

				System.out.println("Computer's turn - Move at (" + (row + 1) + ", " + (column + 1) + ")");

				System.out.println(pcHitOrMiss(row, column) + "\n");

				showMat(gameField);
				System.out.println("\nThe player has still " + ship_counter + " ships");
				System.out.println("\nThe computer has still " + pc_ship_counter + " ships\n");

				if (ship_counter == 0) {
					System.out.println("The computer won!\n");
					inGame = false;
					FileWriter pc_won = new FileWriter("leaderboard.txt");
					pc_won.write("Computer wins: " + ++pc_wins_counter);
					pc_won.write('\n');
					pc_won.write(player_name + " wins: " + player_wins_counter);
					pc_won.close();
				} else if (pc_ship_counter == 0) {
					System.out.println(player_name + " won!\n");
					inGame = false;
					FileWriter player_won = new FileWriter("leaderboard.txt");
					player_won.write(player_name + " wins: " + ++player_wins_counter);
					player_won.write('\n');
					player_won.write("Computer wins: " + pc_wins_counter);
					player_won.close();
				}
			} catch (InputMismatchException e) {
				System.out.println("I asked for a number. Sending you to the menu.");
				sc.nextLine();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Error:\n" + e + "\nSending you to the menu.");
				sc.nextLine();
				return;
			}
		} while (inGame);
		ship_counter = 0;
		pc_ship_counter = 10;
		clearMat(gameField);
	}

	private boolean checkPosition(String campoDiGioco[][], int x, int y) {
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < 15 && j >= 0 && j < 15) {
					if (campoDiGioco[i][j] == "S")
						return false;
					if (campoDiGioco[i][j] == "C")
						return false;
				}
			}
		}
		return true;
	}

	private void showMat(String gameField[][]) {
		for (int i = 0; i < gameField.length; i++) {
			for (int k = 0; k < gameField.length; k++) {
				if (gameField[i][k] == null) {
					gameField[i][k] = " ";
				}
				if (gameField[i][k].equalsIgnoreCase("C")) {
					System.out.print(" |");
				} else
					System.out.print(gameField[i][k] + "|");
			}
			System.out.println("");
		}
	}
	
	private void showMatClear(String campoDiGioco[][]) {
		for (int i = 0; i < campoDiGioco.length; i++) {
			for (int k = 0; k < campoDiGioco.length; k++) {
				if (campoDiGioco[i][k] == null) {
					campoDiGioco[i][k] = " ";
				}
				System.out.print(campoDiGioco[i][k] + "|");
			}
			System.out.println("");
		}
	}

	private void clearMat(String gameField[][]) {
		for (int i = 0; i < gameField.length; i++) {
			for (int k = 0; k < gameField.length; k++) {
				gameField[i][k] = " ";
			}
		}
	}

	private String playerHitOrMiss(int row, int column) {
		if (gameField[row][column] == " ") {
			gameField[row][column] = "O";
			return "Missed!\n";
		} else if (gameField[row][column] == "C") {
			gameField[row][column] = "X";
			pc_ship_counter--;
			return "Hit and destroyed!\n";
		} else if (gameField[row][column] == "O") {
			return "Missed again! Be more careful!\n";
		} else if (gameField[row][column] == "S") {
			return "You can't destroy your own ship!\n";
		}
		return "Something went wrong\n";
	}

	private String pcHitOrMiss(int row, int column) {
		if (gameField[row][column] == "S") {
			gameField[row][column] = "X";
			ship_counter--;
			return "Hit and destroyed!\n";
		} else if (gameField[row][column] == " ") {
			gameField[row][column] = "O";
			return "Missed!\n";
		} else if (gameField[row][column] == "O") {
			return "Missed again! This computer sure sucks.\n";
		}
		return "Something went wrong";
	}
}