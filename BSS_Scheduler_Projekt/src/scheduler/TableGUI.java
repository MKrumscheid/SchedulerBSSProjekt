package scheduler;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableGUI extends Stage {
	private TableView<Scheduler> tv;
	private Label tabellenName;

	public TableGUI(TableView<Scheduler> tv) {
		this.tv = tv;
		tabellenName = new Label("Auswertung Scheduler Strategie");
		tabellenName.setAlignment(Pos.BASELINE_CENTER);
		tabellenName.setStyle("-fx-font-size: 12px; -fx-font-weight: bold");
		VBox root = new VBox();
		root.getChildren().addAll(tabellenName, tv);
		root.setPrefWidth(700);
//		root.setAlignment(Pos.BASELINE_CENTER);
		Scene scene = new Scene(root);
		this.setScene(scene);
		this.sizeToScene();
		this.show();

	}

}
