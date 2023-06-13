package scheduler;

public class Prozess {

	private int ankunftszeit;
	private int dauer;
	private int verweilzeit;
	private int reaktionszeit;
	private int wartezeit;
	private int startzeit;
	private int endzeit;
	private final int dauerFinal;

	public Prozess(int ankunftszeit, int dauer) {
		this.ankunftszeit = ankunftszeit;
		this.dauer = dauer;
		verweilzeit = 0;
		reaktionszeit = 0;
		wartezeit = 0;
		startzeit = -1;
		endzeit = -1;
		dauerFinal = dauer;
	}

	public int getEndzeit() {
		return endzeit;
	}

	public void setEndzeit(int endzeit) {
		this.endzeit = endzeit;
	}

	public int getVerweilzeit() {
		return verweilzeit;
	}

	public void setVerweilzeit(int verweilzeit) {
		this.verweilzeit = verweilzeit;
	}

	public int getReaktionszeit() {
		return reaktionszeit;
	}

	public void setReaktionszeit(int reaktionszeit) {
		this.reaktionszeit = reaktionszeit;
	}

	public int getWartezeit() {
		return wartezeit;
	}

	public void setWartezeit(int wartezeit) {
		this.wartezeit = wartezeit;
	}

	public int getAnkunftszeit() {
		return ankunftszeit;
	}

	public int getDauer() {
		return dauer;
	}

	public void setDauer(int dauer) {
		this.dauer = dauer;
	}

	public int getStartzeit() {
		return startzeit;
	}

	public void setStartzeit(int startzeit) {
		this.startzeit = startzeit;
	}

	public String toString() {
		return "Ankunftszeit: " + ankunftszeit + "\nStartzeit: " + startzeit + "\nReaktionszeit: " + reaktionszeit
				+ "\nWartezeit: " + wartezeit + "\nVerweilzeit: " + verweilzeit + "\nEndzeit: " + endzeit
				+ "\nAusgangsdauer: " + dauerFinal + "\nRestdauer: " + dauer + "\n";
	}

	public void setRunningStartzeit(int betriebszeit) {
		if (this.getStartzeit() == -1) {
			this.setStartzeit(betriebszeit);
			this.setReaktionszeit(betriebszeit - this.getAnkunftszeit());
		}
	}

	public void reset() {
		verweilzeit = 0;
		reaktionszeit = 0;
		wartezeit = 0;
		startzeit = -1;
		endzeit = -1;
		dauer = dauerFinal;
	}

}
