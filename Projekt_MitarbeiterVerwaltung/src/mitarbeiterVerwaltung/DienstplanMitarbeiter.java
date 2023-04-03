package mitarbeiterVerwaltung;

import java.sql.Date;

public class DienstplanMitarbeiter {

	private int id;
	private Date datum;
	private String mitarbeiter1;
	private boolean schicht;

	public DienstplanMitarbeiter(Date datum, String mitarbeiter1, boolean schicht) {
		super();

		this.datum = datum;
		this.mitarbeiter1 = mitarbeiter1;
		this.schicht = schicht;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getMitarbeiter1() {
		return mitarbeiter1;
	}
	public void setMitarbeiter1(String mitarbeiter1) {
		this.mitarbeiter1 = mitarbeiter1;
	}
	public boolean isSchicht() {
		return schicht;
	}
	public void setSchicht(boolean schicht) {
		this.schicht = schicht;
	}

}
