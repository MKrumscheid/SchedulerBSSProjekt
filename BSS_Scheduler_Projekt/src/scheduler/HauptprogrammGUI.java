package scheduler;

import javafx.application.Application;
import javafx.stage.Stage;

public class HauptprogrammGUI extends Application{
	private Scheduler modell;
	
	@Override
	public void init() {
		modell = new Scheduler();
	}
	@Override
	public void start(Stage Stage) throws Exception {
		SchedulerGUI stage = new SchedulerGUI(modell);
		stage.show();
		
	}
	
	public static void main (String[] args) {
		launch(args);
	}

}
