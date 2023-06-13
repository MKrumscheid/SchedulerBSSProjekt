package scheduler;

import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Random;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Scheduler extends Subject {
	// für einfacheres debuggen auf public gestellt, ich hoffe ich erinner mich
	// daran das wieder zu ändern
	public Deque<Prozess> bereit;
	public ArrayList<Prozess> nichtBereit;
	private ArrayList<Prozess> abgeschlossen;
	private int betriebszeit = 0;
	private double durchVerweil;
	private double durchReaktion;
	private double durchWarte;
	ArrayList<Prozess> bereitList;
	private Prozess letzterProzess;
	private String strategie;

	public Scheduler(Prozess... prozess) {
		durchVerweil = 0;
		durchReaktion = 0;
		durchWarte = 0;
		bereit = new ArrayDeque<>();
		nichtBereit = new ArrayList<>();
		abgeschlossen = new ArrayList<>();

		for (int i = 0; i < prozess.length; i++) {
			if (prozess[i].getAnkunftszeit() == 0) {
				bereit.offerLast(prozess[i]);
			} else {
				nichtBereit.add(prozess[i]);
			}
		}
	}

	// Definitionsgemäß werden neue Prozesse am Anfang der Umschaltzeit hinzugefügt
	// neue Prozesse werden an das Ende der Queue eingefügt

	public void FCFS(int umschaltzeit) {
		Prozess running;
		bereitList = new ArrayList<>(bereit);
		// So im nachhinein hätte ich "bereit" direkt als Liste statt als Queue
		// deklarieren
		// sollen, aber ich wollte unbedingt mal eine andere Datenstruktur ausprobieren,
		// also bleib ich dabei. Ich weiß, dass es nicht sehr effizient ist. Mea culpa.
		for (int i = 0; i < nichtBereit.size(); i++) {
			bereitList.add(nichtBereit.get(i));
		}
		Collections.sort(bereitList, new Vgl_Prozess_Ankunftszeit());
		while (!bereitList.isEmpty()) {
			running = bereitList.remove(0);
			if (running.getAnkunftszeit() > betriebszeit) {
				betriebszeit += running.getAnkunftszeit() - betriebszeit;
			}
			running.setRunningStartzeit(betriebszeit);
			running.setWartezeit(running.getReaktionszeit());
			betriebszeit += running.getDauer();
			running.setDauer(0);
			running.setEndzeit(betriebszeit);
			running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
			abgeschlossen.add(running);
			betriebszeit += umschaltzeit;
		}
		berechneDurschnitte(abgeschlossen);
	}

	public void SJF(int umschaltzeit) {
		Prozess running;
		int arbeitszeit;
		// Da Queus nicht sortierbar sind, werden hier ArrayListen verwendet
		bereitList = new ArrayList<>(bereit);
		// nichtBereit einmal initial zu nach Ankunftszeit sortieren, um neu
		// eintreffende Prozesse zu simulieren
		Collections.sort(nichtBereit, new Vgl_Prozess_Ankunftszeit());

		while (!nichtBereit.isEmpty() || !bereitList.isEmpty()) {
			for (int i = 0; i < nichtBereit.size(); i++) {
				Prozess p = nichtBereit.get(i);
				if (p.getAnkunftszeit() <= betriebszeit) {
					nichtBereit.remove(p);
					bereitList.add(p);
					i--;
				}
			}
			Collections.sort(bereitList, new Vgl_Prozess_Dauer());

			if (!bereitList.isEmpty()) {

				running = bereitList.remove(0);
				running.setRunningStartzeit(betriebszeit);
				arbeitszeit = running.getDauer();
				running.setDauer(0);

				betriebszeit += arbeitszeit;
				running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
				running.setEndzeit(betriebszeit);
				setWarteZeitpSJF(arbeitszeit);
				abgeschlossen.add(running);
			}
			if (umschaltzeit == 0 && bereitList.isEmpty() && !nichtBereit.isEmpty()) {
				betriebszeit += nichtBereit.get(0).getAnkunftszeit() - betriebszeit;
			} else {
				betriebszeit += umschaltzeit;
			}
			setWarteZeitpSJF(umschaltzeit);
		}
		berechneDurschnitte(abgeschlossen);
	}

	public void roundRobin(int quantum, int umschaltzeit) {
		Prozess running;
		while (!bereit.isEmpty() || !nichtBereit.isEmpty()) {
			for (int i = 0; i < nichtBereit.size(); i++) {
				Prozess p = nichtBereit.get(i);
				if (p.getAnkunftszeit() <= betriebszeit) {
					nichtBereit.remove(p);
					bereit.offerLast(p);
					i--;
				}
			}
			running = bereit.poll();
			if (running != null) {

				running.setRunningStartzeit(betriebszeit);
				running.setDauer(running.getDauer() - quantum);

				if (running.getDauer() > 0) {
					betriebszeit += quantum;
					setWarteZeit(quantum);
					bereit.offerLast(running);
				} else if (running.getDauer() == 0) {
					betriebszeit += quantum;
					running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
					running.setEndzeit(betriebszeit);
					setWarteZeit(quantum);
					abgeschlossen.add(running);

				} else {
					betriebszeit += quantum - Math.abs(running.getDauer());
					running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
					running.setEndzeit(betriebszeit);
					for (Prozess p : bereit) {
						p.setWartezeit(p.getWartezeit() + quantum - Math.abs(running.getDauer()));
					}
					for (Prozess p : nichtBereit) {
						if (p.getAnkunftszeit() < betriebszeit) {
							p.setWartezeit(betriebszeit - p.getAnkunftszeit());
						}
					}
					abgeschlossen.add(running);
				}
			}
			// Wenn die Bereit Liste leer ist und die Umschaltzeit 0 beträgt, würde keine
			// Zeit vergehen

			if (umschaltzeit == 0 && bereit.isEmpty() && !nichtBereit.isEmpty()) {
				betriebszeit += nichtBereit.get(0).getAnkunftszeit() - betriebszeit;
			} else {
				betriebszeit += umschaltzeit;
			}

			setWarteZeit(umschaltzeit);
		}

		berechneDurschnitte(abgeschlossen);
	}

	public void pSJF(int umschaltzeit) {
		Prozess running;
		int arbeitszeit;
		int verbleibendeDauer;
		// Da Queus nicht sortierbar sind, werden hier ArrayListen verwendet
		bereitList = new ArrayList<>(bereit);
		// nichtBereit einmal initial zu nach Ankunftszeit sortieren, um neu
		// eintreffende Prozesse zu simulieren
		Collections.sort(nichtBereit, new Vgl_Prozess_Ankunftszeit());

		while (!nichtBereit.isEmpty() || !bereitList.isEmpty()) {
			for (int i = 0; i < nichtBereit.size(); i++) {
				Prozess p = nichtBereit.get(i);
				if (p.getAnkunftszeit() <= betriebszeit) {
					nichtBereit.remove(p);
					bereitList.add(p);
					i--;
				}
			}
			Collections.sort(bereitList, new Vgl_Prozess_Dauer());

			if (!bereitList.isEmpty()) {

				running = bereitList.remove(0);
				running.setRunningStartzeit(betriebszeit);
				verbleibendeDauer = running.getDauer();
				// Der Prozess darf solange rechnen bis der nächste Prozess von der
				// nichtBereitListe eintrifft
				if (!nichtBereit.isEmpty()) {
					arbeitszeit = nichtBereit.get(0).getAnkunftszeit() - betriebszeit;
					// Der nächste Prozess trifft erst nach der aktuellen Betriebszeit ein
					if (arbeitszeit > 0) {
						running.setDauer(running.getDauer() - arbeitszeit);
					} else {
						// der neue Prozess trifft bereits während der lezten Umschaltzeit ein, kann
						// aber noch nicht in die bereit Liste aufgenommen werden, da dies am Anfang der
						// Umschaltzeit passiert. Der aktuelle running
						// Prozess erhält nun eine Zeiteinheit zum rechnen.
						arbeitszeit = 1;
						running.setDauer(running.getDauer() - arbeitszeit);
					}
					// Für den Fall dass die nichtBereitList beim aktuellen durchlauf leer geworden
					// ist
				} else {
					// Wenn kein Prozess mehr in der nichtBereit Liste ist, darf der aktuelle
					// Prozess solange rechnen bis er fertig ist
					arbeitszeit = running.getDauer();
					running.setDauer(0);
				}
				if (running.getDauer() > 0) {
					betriebszeit += arbeitszeit;
					setWarteZeitpSJF(arbeitszeit);
					bereitList.add(running);
				} else if (running.getDauer() == 0) {
					betriebszeit += arbeitszeit;
					running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
					running.setEndzeit(betriebszeit);
					setWarteZeitpSJF(arbeitszeit);
					abgeschlossen.add(running);
				} else {
					arbeitszeit = verbleibendeDauer;
					betriebszeit += arbeitszeit;
					running.setVerweilzeit(betriebszeit - running.getAnkunftszeit());
					running.setEndzeit(betriebszeit);
					setWarteZeitpSJF(arbeitszeit);
					abgeschlossen.add(running);
				}
			}
			if (umschaltzeit == 0 && bereitList.isEmpty() && !nichtBereit.isEmpty()) {
				betriebszeit += nichtBereit.get(0).getAnkunftszeit() - betriebszeit;
			} else {
				betriebszeit += umschaltzeit;
			}
			setWarteZeitpSJF(umschaltzeit);
		}

		berechneDurschnitte(abgeschlossen);
	}

	public void runAll(int quantum, int umschaltzeit) {
		// Setze alle Prozesse in eine Liste um sie den Schedulern als Array zu
		// übergeben
		nichtBereit.addAll(bereit);
		Prozess[] prozessArray = new Prozess[nichtBereit.size()];

		for (int i = 0; i < nichtBereit.size(); i++) {
			Prozess p = nichtBereit.get(i);
			prozessArray[i] = new Prozess(p.getAnkunftszeit(), p.getDauer());
		}
		Scheduler s1 = new Scheduler(prozessArray);
		s1.FCFS(umschaltzeit);
		s1.setStrategie("FCFS");
		s1.ausgabe();

		for (int i = 0; i < nichtBereit.size(); i++) {
			Prozess p = nichtBereit.get(i);
			prozessArray[i] = new Prozess(p.getAnkunftszeit(), p.getDauer());
		}
		Scheduler s2 = new Scheduler(prozessArray);
		s2.SJF(umschaltzeit);
		s2.setStrategie("SJF");
		s2.ausgabe();

		for (int i = 0; i < nichtBereit.size(); i++) {
			Prozess p = nichtBereit.get(i);
			prozessArray[i] = new Prozess(p.getAnkunftszeit(), p.getDauer());
		}
		Scheduler s3 = new Scheduler(prozessArray);
		s3.roundRobin(quantum, umschaltzeit);
		s3.setStrategie("Round Robin");
		s3.ausgabe();

		for (int i = 0; i < nichtBereit.size(); i++) {
			Prozess p = nichtBereit.get(i);
			prozessArray[i] = new Prozess(p.getAnkunftszeit(), p.getDauer());
		}
		Scheduler s4 = new Scheduler(prozessArray);
		s4.pSJF(umschaltzeit);
		s4.setStrategie("pSJF");
		s4.ausgabe();

		TableGUI tabelle = new TableGUI(erstelleTabelle(s1, s2, s3, s4));
	}

	public void reset() {
		bereit = new ArrayDeque<>();
		nichtBereit = new ArrayList<>();
		abgeschlossen = new ArrayList<>();
		betriebszeit = 0;
		durchVerweil = 0;
		durchReaktion = 0;
		durchWarte = 0;

	}

	public void ausgabe() {
		// die Ausgabe wird zuerst nach der Ankunftszeit der Prozesse sortiert. Dafür
		// wird eine Vergleichsklasse benutzt die den Comparator implementiert und die
		// Ankunftszeiten der Prozesse vergleicht.
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		Collections.sort(abgeschlossen, new Vgl_Prozess_Ankunftszeit());
		for (int i = 0; i < abgeschlossen.size(); i++) {
			System.out.println("Prozess " + i + ": \n" + abgeschlossen.get(i).toString());
		}
		System.out.println("Durchschnittliche Wartezeit: " + decimalFormat.format(durchWarte)
				+ "\nDurchschnittliche Reaktionszeit: " + decimalFormat.format(durchReaktion)
				+ "\nDurchschnittliche Verweilzeit " + decimalFormat.format(durchVerweil) + "\n");
	}

	public void setWarteZeit(int wartezeit) {
		for (Prozess p : bereit) {
			p.setWartezeit(p.getWartezeit() + wartezeit);
		}
		for (Prozess p : nichtBereit) {
			if (p.getAnkunftszeit() < betriebszeit) {
				p.setWartezeit(betriebszeit - p.getAnkunftszeit());
			}
		}
	}

	public void setWarteZeitpSJF(int wartezeit) {
		for (Prozess p : bereitList) {
			p.setWartezeit(p.getWartezeit() + wartezeit);
		}
		for (Prozess p : nichtBereit) {
			if (p.getAnkunftszeit() < betriebszeit) {
				p.setWartezeit(betriebszeit - p.getAnkunftszeit());
			}
		}
	}

	public void berechneDurschnitte(ArrayList<Prozess> abgeschlossen) {
		int warte = 0;
		int reaktion = 0;
		int verweil = 0;
		for (Prozess p : abgeschlossen) {
			warte += p.getWartezeit();
			reaktion += p.getReaktionszeit();
			verweil += p.getVerweilzeit();
		}
		durchWarte = (double) warte / abgeschlossen.size();
		durchReaktion = (double) reaktion / abgeschlossen.size();
		durchVerweil = (double) verweil / abgeschlossen.size();
	}

	public void addProzess(Prozess p) {
		letzterProzess = p;
		if (p.getAnkunftszeit() == 0) {
			bereit.offerLast(p);
		} else
			nichtBereit.add(p);
	}

	public void removeProzess() {
		if (bereit.getLast() == letzterProzess) {
			bereit.remove(letzterProzess);
		} else
			nichtBereit.remove(letzterProzess);
	}
	
	public void erstelleRandomProzesse(int anzahl, int bisDauer, int bisAnkunft) {
		Random rnd = new Random();
		for (int i = 0; i < anzahl; i++) {
			addProzess(new Prozess(rnd.nextInt((bisAnkunft+1)), rnd.nextInt(1, (bisDauer+1))));
		}
		System.out.println(bereit.size());
		System.out.println(nichtBereit.size());
	}

	public ArrayList<Prozess> getAbgeschlossen() {
		return abgeschlossen;
	}

	public Deque<Prozess> getBereit() {
		return bereit;
	}

	public ArrayList<Prozess> getNichtBereit() {
		return nichtBereit;
	}

	public ArrayList<Prozess> getBereitList() {
		return bereitList;
	}

	public double getDurchVerweil() {
		return durchVerweil;
	}

	public double getDurchReaktion() {
		return durchReaktion;
	}

	public double getDurchWarte() {
		return durchWarte;
	}

	public String getStrategie() {
		return strategie;
	}

	public void setStrategie(String strategie) {
		this.strategie = strategie;
	}

	public TableView<Scheduler> erstelleTabelle(Scheduler... s) {
	    TableView<Scheduler> ergebnis = new TableView<>();
	    TableColumn<Scheduler, String> strategie = new TableColumn<>("Strategie");
	    TableColumn<Scheduler, Double> reaktionszeit = new TableColumn<>("Durchschnittliche Reaktionszeit");
	    TableColumn<Scheduler, Double> wartezeit = new TableColumn<>("Durchschnittliche Wartezeit");
	    TableColumn<Scheduler, Double> verweilzeit = new TableColumn<>("Durchschnittliche Verweilzeit");
	    strategie.setCellValueFactory(new PropertyValueFactory<>("strategie"));
	    reaktionszeit.setCellValueFactory(new PropertyValueFactory<>("durchReaktion"));
	    wartezeit.setCellValueFactory(new PropertyValueFactory<>("durchWarte"));
	    verweilzeit.setCellValueFactory(new PropertyValueFactory<>("durchVerweil"));

	    ergebnis.getColumns().addAll(strategie, reaktionszeit, wartezeit, verweilzeit);

	    for (int i = 0; i < s.length; i++) {
	        ergebnis.getItems().add(s[i]);
	    }
	    ergebnis.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	    reaktionszeit.setCellFactory(column -> createCustomCell());

	    wartezeit.setCellFactory(column -> createCustomCell());

	    verweilzeit.setCellFactory(column -> createCustomCell());

	    return ergebnis;
	}

	private TableCell<Scheduler, Double> createCustomCell() {
	    return new TableCell<Scheduler, Double>() {
	        @Override
	        protected void updateItem(Double item, boolean empty) {
	            super.updateItem(item, empty);

	            if (item == null || empty) {
	                setText(null);
	            } else {
	                // Runde den absoluten Wert auf zwei Nachkommastellen
	                DecimalFormat df = new DecimalFormat("#.00");
	                String roundedValue = df.format(item);

	                setText(roundedValue);

	                // Bestwert finden (hier als Beispiel wird der kleinste Wert verwendet)
	                TableColumn<Scheduler, Double> currentColumn = (TableColumn<Scheduler, Double>) getTableColumn();
	                double bestwert = getTableView().getItems().stream()
	                        .mapToDouble(scheduler -> currentColumn.getCellObservableValue(scheduler).getValue())
	                        .min()
	                        .orElse(0.0);

	                // Prozentuale Abweichung berechnen
	                double abweichung = ((item - bestwert) / bestwert) * 100;

	                // Text mit Prozentuale Abweichung formatieren und hinzufügen
	                setText(getText() + " (" + String.format("%.2f%%", abweichung) + ")");
	            }
	        }
	    };
	}

}
