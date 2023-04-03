package mitarbeiterVerwaltung;

import java.util.Date;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class DienstplanMitarbeiterFX {

	private DienstplanMitarbeiter person;

	private SimpleObjectProperty<Date> datum ;
	private SimpleStringProperty mitarbeiter1 ;
	private SimpleBooleanProperty schicht;


	public DienstplanMitarbeiterFX(DienstplanMitarbeiter person) {
		super();
		this.person = person;
		this.datum = new SimpleObjectProperty(person.getDatum());
		this.mitarbeiter1 = new SimpleStringProperty(person.getMitarbeiter1());		
	}

	public DienstplanMitarbeiter getPerson() {
		return person;
	}

	public void setPerson(DienstplanMitarbeiter person) {
		this.person = person;
	}

	public final SimpleObjectProperty<Date> datumProperty() {
		return this.datum;
	}


	public final Date getDatum() {
		return this.datumProperty().get();
	}


	public final void setDatum(final Date datum) {
		this.datumProperty().set(datum);
	}


	public final SimpleStringProperty mitarbeiter1Property() {
		return this.mitarbeiter1;
	}


	public final String getMitarbeiter1() {
		return this.mitarbeiter1Property().get();
	}


	public final void setMitarbeiter1(final String mitarbeiter1) {
		this.mitarbeiter1Property().set(mitarbeiter1);
	}

	public final SimpleBooleanProperty schichtProperty() {
		return this.schicht;
	}


	public final boolean isSchicht() {
		return this.schichtProperty().get();
	}


	public final void setSchicht(final boolean schicht) {
		this.schichtProperty().set(schicht);
	}

}
