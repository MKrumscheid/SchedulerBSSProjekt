package scheduler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SchedulerGUI extends Stage implements IObserver {

	private Scheduler modell;
	private Controller controller;
	private TextField prozessAnkunft;
	private TextField prozessDauer;
	private Button weiter;
	private Button zurueck;
	private Label platzhalter;
	private Button fcfs;
	private Button sjf;
	private Button roundRobin;
	private Button pSJF;
	private Button runAll;
	private Button erstelleRandom;

	public SchedulerGUI(Scheduler modell) {
		this.modell = modell;

		Label top1 = new Label(
				"Bitte geben Sie die Prozessdauer und die Ankunftzeit des Prozesses als Integer Wert ein ein. ");
		top1.setAlignment(Pos.BASELINE_CENTER);
		top1.setStyle("-fx-font-size: 12px; -fx-font-weight: bold");
		Label top2 = new Label(
				"\"Weiter\" fügt den eingebenen Prozess der Arbeitsliste hinzu, \"Zurück\" entfernt einmalig den zuletzt eingegeben Prozess");
		top2.setAlignment(Pos.BASELINE_CENTER);
		top2.setStyle("-fx-font-size: 12px; -fx-font-weight: bold");

		// Textfelder
		prozessDauer = new TextField("Prozessdauer");
		prozessAnkunft = new TextField("Prozessankunftszeit");
		prozessDauer.setPromptText("Prozessdauer");
		prozessAnkunft.setPromptText("Prozessankunftszeit");
		GridPane textfelder = new GridPane();
		textfelder.addRow(0, prozessDauer, prozessAnkunft);
		textfelder.setHgap(10);
		textfelder.setAlignment(Pos.CENTER);

		// Buttons
		GridPane buttonBoxOben = new GridPane();
		zurueck = new Button("Zurück");
		weiter = new Button("Weiter");
		erstelleRandom = new Button("Erzeuge zufällige Prozesse");
		buttonBoxOben.setHgap(25);
		buttonBoxOben.addRow(0, zurueck, erstelleRandom, weiter);
		buttonBoxOben.setAlignment(Pos.CENTER);

		Label bottom = new Label("Bitte drücken sie den Button für die gewünschte Simulation an");
		bottom.setAlignment(Pos.BASELINE_CENTER);
		bottom.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

		GridPane buttonBoxUnten = new GridPane();
		fcfs = new Button("FCFS");
		sjf = new Button("Shortest Job First");
		roundRobin = new Button("Round Robin");
		pSJF = new Button("Preemptive Shortest Job First");
		runAll = new Button("Run All");
		buttonBoxUnten.addRow(0, fcfs, sjf, roundRobin, pSJF, runAll);
		buttonBoxUnten.setHgap(10);
		buttonBoxUnten.setAlignment(Pos.CENTER);

		platzhalter = new Label();
		platzhalter.setPrefHeight(50);
		platzhalter.setAlignment(Pos.BASELINE_CENTER);

		VBox root = new VBox();
		root.setPadding(new Insets(20));
		root.setSpacing(10);
		root.getChildren().addAll(top1, top2, textfelder, buttonBoxOben, platzhalter, bottom, buttonBoxUnten);
		root.setAlignment(Pos.BASELINE_CENTER);
		root.setStyle("-fx-background-color: #ffffff;");
		this.setTitle("Scheduler");
		this.setScene(new Scene(root));
		this.sizeToScene();

		controller = new Controller(this, modell);
		modell.anmelden(this);
		aktualisieren();

	}

	public String getAnkunft() {
		return prozessAnkunft.getText();
	}

	public String getDauer() {
		return prozessDauer.getText();
	}

	public void weiterAnmelden(EventHandler<ActionEvent> e) {
		weiter.setOnAction(e);
	}

	public void zurueckAnmelden(EventHandler<ActionEvent> e) {
		zurueck.setOnAction(e);
	}

	public void FCFSAnmelden(EventHandler<ActionEvent> e) {
		fcfs.setOnAction(e);
	}
	
	public void erstelleRandomAnmelden(EventHandler<ActionEvent> e) {
		erstelleRandom.setOnAction(e);
	}

	public void SJFAnmelden(EventHandler<ActionEvent> e) {
		sjf.setOnAction(e);
	}

	public void RRAnmelden(EventHandler<ActionEvent> e) {
		roundRobin.setOnAction(e);
	}

	public void pSJFAnmelden(EventHandler<ActionEvent> e) {
		pSJF.setOnAction(e);
	}

	public void runAllAnmelden(EventHandler<ActionEvent> e) {
		runAll.setOnAction(e);
	}

	@Override
	public void aktualisieren() {
		clearText();
		setStatusText(controller.getStatus());
	}

	public void clearText() {
		prozessAnkunft.clear();
		prozessDauer.clear();

	}

	public void setStatusText(String text) {
		platzhalter.setText(text);
	}
	
	
}
