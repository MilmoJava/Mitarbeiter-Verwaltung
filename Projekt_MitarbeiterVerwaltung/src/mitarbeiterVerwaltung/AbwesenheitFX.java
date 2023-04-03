package mitarbeiterVerwaltung;

import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class AbwesenheitFX {

	private Abwesenheit person;

	private SimpleIntegerProperty id;
	private SimpleStringProperty mitarbeiter1;
	private SimpleObjectProperty<Date> datum;
	private SimpleStringProperty grund;

	public AbwesenheitFX(Abwesenheit person) {
		super();
		this.person = person;
		this.id =  new SimpleIntegerProperty(person.getId());
		this.mitarbeiter1 =  new SimpleStringProperty(person.getMitarbeiter1());
		this.datum = new SimpleObjectProperty(person.getDatum());
		this.grund = new SimpleStringProperty(person.getGrund());
	}

	public AbwesenheitFX(Mitarbeiter ma, String grund2) {
	}

	public Abwesenheit getPerson() {
		return person;
	}

	public final SimpleIntegerProperty idProperty() {
		return this.id;
	}


	public final int getId() {
		return this.idProperty().get();
	}


	public final void setId(final int id) {
		this.idProperty().set(id);
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


	public final SimpleObjectProperty<Date> datumProperty() {
		return this.datum;
	}


	public final Date getDatum() {
		return this.datumProperty().get();
	}


	public final void setDatum(final Date datum) {
		this.datumProperty().set(datum);
	}


	public final SimpleStringProperty grundProperty() {
		return this.grund;
	}


	public final String getGrund() {
		return this.grundProperty().get();
	}


	public final void setGrund(final String grund) {
		this.grundProperty().set(grund);
	}

}
