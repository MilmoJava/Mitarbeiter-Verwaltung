package mitarbeiterVerwaltung;

import java.sql.Date;

public class Abwesenheit {

	private int id;
	private String mitarbeiter1;
	private Date datum;
	private String grund;

	public Abwesenheit(String mitarbeiter1, Date datum, String grund) {
		super();
		this.mitarbeiter1 = mitarbeiter1;
		this.datum = datum;
		this.grund = grund;
	}

	public Abwesenheit(int id, String mitarbeiter1, Date datum, String grund) {
		super();
		this.id = id;
		this.mitarbeiter1 = mitarbeiter1;
		this.datum = datum;
		this.grund = grund;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMitarbeiter1() {
		return mitarbeiter1;
	}
	public void setMitarbeiter1(String mitarbeiter1) {
		this.mitarbeiter1 = mitarbeiter1;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public String getGrund() {
		return grund;
	}
	public void setGrund(String grund) {
		this.grund = grund;
	}

}
