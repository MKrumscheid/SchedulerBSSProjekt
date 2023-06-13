package scheduler;

public class HauptProgrammScheduler {

	public static void main(String[] args) {
		Prozess p1 = new Prozess(0,22);
		Prozess p2 = new Prozess(0,2);
		Prozess p3 = new Prozess(0,3);
		Prozess p4 = new Prozess(0,5);
		Prozess p5 = new Prozess(0,8);
		Prozess p6 = new Prozess(0,15);
		Prozess p7 = new Prozess(0,7);
		Prozess p8 = new Prozess(3,1);
		Prozess p9 = new Prozess(6,4);
		Prozess p10 = new Prozess(6,8);
		Prozess p11 = new Prozess(120,20);
		
		Scheduler s1 = new Scheduler(p6,p7, p8, p9, p10);
		Scheduler s2 = new Scheduler(p1,p2, p3, p4, p5);
//		s1.roundRobin(1,1);
		s1.runAll(1,1);
		
//		s2.ausgabe();
//		s1.pSJF(0);
		s1.ausgabe();
	}

}
