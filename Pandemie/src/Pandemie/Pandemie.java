package Pandemie;

import terminal.VT;

import terminal.VTerm;

import java.util.Scanner;

public class Pandemie {

	// globale Matrix für das Speichern der Population
	static int[][] population = new int[101][101];
	static VTerm console = VTerm.getInstance(25, 80, "Pandemie", VT.CS_TINY);

	public static void main(String[] args) {
        Scanner eingabe = new Scanner(System.in);
        
        System.out.println("Wie groß soll die Simulation werden?");
        int a = eingabe.nextInt() + 1; // 20
        int b = eingabe.nextInt() + 1; // 50
		
		int i, j; // Laufvariablen für die Schleifen
		// Die Werte im Array population werden auf 0 gesetzt
		for (i = 0; i < a; i++) {
			for (j = 0; j < b; j++) {
				population[i][j] = 0;
			}
		}

		// Ausgabe im Fenster von console (Typ VTerm)
		print(a, b);

		console.delay(500); // Warten für 500 Millisekunden
		// erste Person soll angesteckt werden
		init(a, b);

		System.out.println("Wie lange soll die Simulation dauern?");
		int d = eingabe.nextInt() + 1; // 20
		/* System.out.println("Wie ansteckend ist die Krankheit?");
		int n = eingabe.nextInt(); */
		int counter_erkrankt = 3;
		int counter_geheilt = 0;
		for (int tage = 0; tage < d; tage++) {
			for (i = 1; i < a-1; i++) {
				for (j = 1; j < b-1; j++) {
					// Berechnung der Ansteckung
					int [] values = ansteckung(i, j, /*n,*/ d, counter_erkrankt, counter_geheilt);
					counter_erkrankt = values[0];
					counter_geheilt = values[1];
				}
			}
			System.out.println("Simulationsschritt: " + tage);
			System.out.println("Noch nicht erkrankte Personen: " + ((a-1)*(b-1)-counter_erkrankt));
			System.out.println("Kranke Personen: " + (counter_erkrankt-counter_geheilt));
			System.out.println("Geheilte Personen: " + counter_geheilt);

			console.clearScreen(); // Löschen des Inhalts von console
			console.println("Tag " + tage);

			// Ausgabe im Fenster von console (Typ VTerm)
			print(a, b);
			console.delay(500);
		}

		// Schliessen der Konsole mit Enter
		console.readyToExit(true);
	}

	/* Hinzufügen der neuen Methoden */
	public static void print(int a, int b) {
		int i, j;
		for (i = 0; i < a; i++) {
			for (j = 0; j < b; j++) {
				if (population[i][j] > 0 && population[i][j] < 8) {
					console.print("" + population[i][j]);
				} else {
					console.print(' ');
				}
			}
			console.println();
		}
	}

	public static int[] ansteckung(int x, int y, /*int n,*/ int d, int counter_erkrankt, int counter_geheilt) {		
		if (population[x][y] == 0) {
			boolean [] counter_infectious_list = {infectioes(x-1,y), infectioes(x,y-1), infectioes(x+1,y), infectioes(x,y+1)};
			double counter_infectious = 0;
			for (int k = 0; k < 4; k++) {
				if (counter_infectious_list[k]) {
					counter_infectious += 1;
				}
			}
			if (infectioes(x-1,y) || infectioes(x,y-1) || infectioes(x+1,y)
					|| infectioes(x,y+1)) {
				int zufall = (int) (Math.random() * 0.25 * d * (1.0/counter_infectious));
				if (zufall == 0) {
					population[x][y] = 1;
					counter_erkrankt += 1;
				}
			}
		} else if (population[x][y] < 8) {
			if (population[x][y] == 7) {
				counter_geheilt += 1;
			}
			population[x][y] += 1;
		}
		int [] values = {counter_erkrankt, counter_geheilt};
		return values;

	}

	public static void init(int a, int b) {
		int x, y;
		for (int i = 0; i < 3; i++) {
			x = (int) (Math.random() * (a-1) + 1);
			y = (int) (Math.random() * (b-1) + 1);
			population[x][y] = 1;
		}
	}
	
	public static boolean infectioes(int x, int y) {
		boolean ansteckend = false;
		if (population[x][y] > 0 && population[x][y] < 8) {
			ansteckend = true;
		}
		return ansteckend;
	}
	
}