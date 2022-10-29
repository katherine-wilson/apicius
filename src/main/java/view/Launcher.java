package view;

import java.util.Scanner;

import javafx.application.Application;

public class Launcher {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		boolean cont = true;
		while (cont) {
			System.out.println("1 for text, 2 for GUI");
			
			if (scanner.hasNextLine()) {
				String response = scanner.nextLine().strip();
				if (response.length() != 0) {
					if (response.charAt(0) == '1') {
						cont = false;
						try {
							TextView.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (response.charAt(0) == '2') {
						cont = false;
						Application.launch(GUI.class, args);
					} else {
						System.out.println("Invalid response");
					}
				}
			}
		}
	}

}
