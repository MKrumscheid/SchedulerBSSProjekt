package scheduler;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

public class Controller {

	private SchedulerGUI view;
	private Scheduler modell;
	private String status = "";

	public Controller(SchedulerGUI view, Scheduler modell) {
		this.view = view;
		this.modell = modell;

		view.weiterAnmelden(new weiterEventHandler());
		view.zurueckAnmelden(new zurueckEventHandler());
		view.FCFSAnmelden(new FCFSEventHandler());
		view.SJFAnmelden(new SJFEventHandler());
		view.RRAnmelden(new roundRobinEventHandler());
		view.pSJFAnmelden(new pSJFEventHandler());
		view.runAllAnmelden(new runAllEventHandler());
		view.erstelleRandomAnmelden(new erstelleRandomProzessEventHandler());
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	class erstelleRandomProzessEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			TextInputDialog tid = new TextInputDialog();
			tid.setHeaderText(
					"Bitte geben Sie ein wie viele Prozesse Sie erstellen möchten, wie lange ein Prozess maximal dauern darf und bis wann er spätestens eintrifft. \nAuf Basis dieser Paramater werden zufällig Prozesse erstellt, die sich innerhalb dieser Paramentergrenzen bewegen");
			tid.setTitle("Eingabe Prozessvariablen");
			int bisAnkunft = 0;
			int bisDauer = 0;
			int anzahl = 0;

			TextField anzahlEingabe = new TextField();
			TextField bisAnkunftEingabe = new TextField();
			TextField bisDauerEingabe = new TextField();

			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.addRow(0, new Label("Anzahl: "), anzahlEingabe);
			gp.addRow(1, new Label("Maximale Ankunftszeit: "), bisAnkunftEingabe);
			gp.addRow(2, new Label("Maximale Bearbeitungsdauer: "), bisDauerEingabe);

			tid.getDialogPane().setContent(gp);

			Optional<String> res = tid.showAndWait();
			if (res.isPresent()) {
				try {
					anzahl = Integer.parseInt(anzahlEingabe.getText());
					bisAnkunft = Integer.parseInt(bisAnkunftEingabe.getText());
					bisDauer = Integer.parseInt(bisDauerEingabe.getText());
				} catch (NumberFormatException e) {
					Alert nfe = new Alert(AlertType.ERROR);
					nfe.setHeaderText("Eingabefehler");
					nfe.setContentText("Bitte geben Sie einen gültigen Integer Wert ein");
					nfe.show();
				}
			}
			modell.erstelleRandomProzesse(anzahl, bisDauer, bisAnkunft);
			;
			setStatus("Es wurden " + anzahl + " neue Prozesse erstellt! Sie können weitere Daten eingeben oder eine Methode zur Auswertung auswählen.");
			view.aktualisieren();

		}

	}

	class runAllEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			TextInputDialog tid = new TextInputDialog();
			tid.setHeaderText("Bitte geben Sie die Umschaltzeit des Systems und das Quantum als Integer Wert ein ein");
			tid.setTitle("Eingabe Umschaltzeit");
			int umschaltzeit = 0;
			int quantum = 0;

			TextField umschaltFeld = new TextField();
			TextField quantumFeld = new TextField();

			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.addRow(0, new Label("Umschaltzeit: "), umschaltFeld);
			gp.addRow(1, new Label("Quantum: "), quantumFeld);

			tid.getDialogPane().setContent(gp);

			Optional<String> res = tid.showAndWait();
			if (res.isPresent()) {
				try {
					umschaltzeit = Integer.parseInt(umschaltFeld.getText());
					quantum = Integer.parseInt(quantumFeld.getText());
				} catch (NumberFormatException e) {
					Alert nfe = new Alert(AlertType.ERROR);
					nfe.setHeaderText("Eingabefehler");
					nfe.setContentText("Bitte geben Sie einen gültigen Integer Wert ein");
					nfe.show();
				}
			}
			modell.runAll(quantum, umschaltzeit);
			setStatus("Alle Simulationen erfolgreich abgeschlossen!\n Die können neue Daten eingeben.");
			view.aktualisieren();
			modell.reset();

		}

	}

	class FCFSEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			modell.FCFS(getUmschaltzeit());
			modell.ausgabe();
			setStatus("First Come First Serve erfolgreich simuliert! Sie können neue Daten eingeben.");
			modell.setStrategie("FCFS");
			TableGUI tabelle = new TableGUI(modell.erstelleTabelle(modell));
			view.aktualisieren();
			modell.reset();

		}

	}

	class roundRobinEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			TextInputDialog tid = new TextInputDialog();
			tid.setHeaderText("Bitte geben Sie die Umschaltzeit des Systems und das Quantum als Integer Wert ein ein");
			tid.setTitle("Eingabe Umschaltzeit");
			int umschaltzeit = 0;
			int quantum = 0;

			TextField umschaltFeld = new TextField();
			TextField quantumFeld = new TextField();

			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.addRow(0, new Label("Umschaltzeit: "), umschaltFeld);
			gp.addRow(1, new Label("Quantum: "), quantumFeld);

			tid.getDialogPane().setContent(gp);

			Optional<String> res = tid.showAndWait();
			if (res.isPresent()) {
				try {
					umschaltzeit = Integer.parseInt(umschaltFeld.getText());
					quantum = Integer.parseInt(quantumFeld.getText());
				} catch (NumberFormatException e) {
					Alert nfe = new Alert(AlertType.ERROR);
					nfe.setHeaderText("Eingabefehler");
					nfe.setContentText("Bitte geben Sie einen gültigen Integer Wert ein");
					nfe.show();
				}
			}
			modell.roundRobin(quantum, umschaltzeit);
			modell.ausgabe();
			modell.setStrategie("Round Robin");
			TableGUI tabelle = new TableGUI(modell.erstelleTabelle(modell));
			setStatus("Round Robin erfolgreich simuliert! Sie können neue Daten eingeben.");
			view.aktualisieren();
			modell.reset();

		}

	}

	class SJFEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			modell.SJF(getUmschaltzeit());
			modell.ausgabe();
			setStatus("Shortes Job First erfolgreich simuliert! Sie können neue Daten eingeben.");
			modell.setStrategie("SJF");
			TableGUI tabelle = new TableGUI(modell.erstelleTabelle(modell));
			view.aktualisieren();
			modell.reset();
		}

	}

	class pSJFEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			modell.pSJF(getUmschaltzeit());
			modell.ausgabe();
			setStatus(" Preemptive Shortes Job First erfolgreich simuliert! Sie können neue Daten eingeben.");
			modell.setStrategie("pSJF");
			TableGUI tabelle = new TableGUI(modell.erstelleTabelle(modell));
			view.aktualisieren();
			modell.reset();
		}

	}

	class zurueckEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			System.out.println("zurueck");
			modell.removeProzess();
			setStatus("Prozess wurde gelöscht!");
			view.aktualisieren();
			for (Prozess p : modell.bereit) { // visibilty im scheduler noch ändern
				System.out.println("Auf Bereit Liste: \n" + p.toString());
			}
			for (Prozess p : modell.nichtBereit) { // visibilty im scheduler noch ändern
				System.out.println("Auf nichtBereit Liste \n" + p.toString());
			}
		}

	}

	class weiterEventHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			System.out.println("in weiter");
			int dauer = 0;
			int ankunft = 0;
			try {
				dauer = Integer.parseInt(view.getDauer());
				ankunft = Integer.parseInt(view.getAnkunft());
			} catch (NumberFormatException e) {
				Alert nfe = new Alert(AlertType.ERROR);
				nfe.setHeaderText("Eingabefehler");
				nfe.setContentText("Bitte geben Sie einen gültigen Integer Wert ein");
				nfe.show();
			}
			modell.addProzess(new Prozess(ankunft, dauer));
			setStatus("Prozess wurde hinzugefügt!");
			view.aktualisieren();
			for (Prozess p : modell.bereit) { // visibilty im scheduler noch ändern
				System.out.println("Auf Bereit Liste: \n" + p.toString());
			}
			for (Prozess p : modell.nichtBereit) { // visibilty im scheduler noch ändern
				System.out.println("Auf nichtBereit Liste \n" + p.toString());
			}
		}

	}

	public int getUmschaltzeit() {
		TextInputDialog tid = new TextInputDialog();
		tid.setContentText("Wert");
		tid.setHeaderText("Bitte geben Sie die Umschaltzeit des Systems als Integer Wert ein ein");
		tid.setTitle("Eingabe Umschaltzeit");
		int umschaltzeit = 0;
		Optional<String> res = tid.showAndWait();
		if (res.isPresent()) {
			String input = res.get();
			try {
				umschaltzeit = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				Alert nfe = new Alert(AlertType.ERROR);
				nfe.setHeaderText("Eingabefehler");
				nfe.setContentText("Bitte geben Sie einen gültigen Integer Wert ein");
				nfe.show();
			}
		}
		return umschaltzeit;
	}
}
