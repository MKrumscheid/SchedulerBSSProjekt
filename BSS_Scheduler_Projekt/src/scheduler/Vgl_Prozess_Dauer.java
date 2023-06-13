package scheduler;

import java.util.Comparator;

public class Vgl_Prozess_Dauer implements Comparator<Prozess>{

	public int compare(Prozess p1, Prozess p2) {
		return p1.getDauer() - p2.getDauer();
	}
}
