package scheduler;

import java.util.ArrayList;

public abstract class Subject {
	
	ArrayList<IObserver> observerList;
	
	public Subject() {
		observerList = new ArrayList<IObserver>();
	}

	public void anmelden(IObserver i) {
		observerList.add(i);
	}
	
	public void abmelden(IObserver i) {
		observerList.remove(i);
	}
	
	public void benachrichtigen() {
		for (IObserver i : observerList) {
			i.aktualisieren();
		}
	}
}
