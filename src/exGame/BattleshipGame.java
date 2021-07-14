package exGame;

import java.util.Scanner;
import java.io.*;

public class BattleshipGame {

	static int count = 0;
	
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		BattleshipField bf = new BattleshipField();
		int menu_choice = 0;
		String player_name;

		System.out.print("Player name: ");
		player_name = sc.nextLine();
		
		do {
			System.out.println("\n0. End the program");
			System.out.println("1. Show the leaderboard");
			System.out.println("2. Start a new game");
			menu_choice = sc.nextInt();
			sc.nextLine();

			switch(menu_choice) {
			case 0:
				System.out.println("Program terminated!");
				System.exit(0);
				break;
			case 1:
				try {
					File f = new File("leaderboard.txt");				
					if (f.exists()) {
						FileReader file = new FileReader(f);
						int next;
						
						do {
							next = file.read();
							
							if (next != -1) {
								char nextc = (char) next;
								System.out.print(nextc);
							}
						}while(next != -1);
						
						file.close();
					}
					else
						System.out.println("The file doesn't exist yet!");
				} catch (FileNotFoundException e) {
					System.out.println("File not found");
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("");
				break;
			case 2:
				bf.generateGameField(player_name);
				break;
			default:
				System.out.println("I need a correct number. Try again.");
				break;
			}
			
		} while (menu_choice != 0);
		sc.close();
	}
}
