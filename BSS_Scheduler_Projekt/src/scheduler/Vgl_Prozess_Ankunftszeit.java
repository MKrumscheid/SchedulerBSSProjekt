package scheduler;

import java.util.Comparator;

public class Vgl_Prozess_Ankunftszeit implements Comparator<Prozess>{

	public int compare(Prozess p1, Prozess p2) {
		return p1.getAnkunftszeit() - p2.getAnkunftszeit();
	}

	

}
