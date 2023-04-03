package mitarbeiterVerwaltung;

import java.util.Objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MitarbeiterFX {

	private Mitarbeiter person;

	private SimpleStringProperty vorname;
	private SimpleStringProperty nachname;
	private SimpleStringProperty personalnummer;
	private SimpleStringProperty geschlecht;
	private SimpleIntegerProperty plz;
	private SimpleStringProperty ort;
	private SimpleStringProperty strasse;
	private SimpleStringProperty hausnummer;
	private SimpleStringProperty iban;
	private SimpleStringProperty bic;
	private SimpleStringProperty bank;
	private SimpleStringProperty telefon;
	private SimpleStringProperty email;

	private final StringProperty grund;

	public MitarbeiterFX(Mitarbeiter person) {
		super();
		this.person = person;
		vorname = new SimpleStringProperty(person.getVorname());
		nachname = new SimpleStringProperty(person.getNachname());
		personalnummer = new SimpleStringProperty(person.getPersonalnummer());
		geschlecht = new SimpleStringProperty(person.getGeschlecht());
		plz = new SimpleIntegerProperty(person.getPlz());
		ort = new SimpleStringProperty(person.getOrt());
		strasse = new SimpleStringProperty(person.getStrasse());
		hausnummer = new SimpleStringProperty(person.getHausnummer());
		iban = new SimpleStringProperty(person.getIban());
		bic = new SimpleStringProperty(person.getBic());
		bank = new SimpleStringProperty(person.getBank());
		telefon = new SimpleStringProperty(person.getTelefon());
		email = new SimpleStringProperty(person.getEmail());
		this.grund = new SimpleStringProperty("");
	}

	public Mitarbeiter getPerson() {
		return person;
	}

	@Override
	public int hashCode() {
		return Objects.hash(personalnummer);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MitarbeiterFX other = (MitarbeiterFX) obj;
		return Objects.equals(personalnummer, other.personalnummer);
	}


	public final SimpleStringProperty vornameProperty() {
		return this.vorname;
	}


	public final String getVorname() {
		return this.vornameProperty().get();
	}


	public final void setVorname(final String vorname) {
		this.vornameProperty().set(vorname);
	}


	public final SimpleStringProperty nachnameProperty() {
		return this.nachname;
	}


	public final String getNachname() {
		return this.nachnameProperty().get();
	}


	public final void setNachname(final String nachname) {
		this.nachnameProperty().set(nachname);
	}


	public final SimpleStringProperty geschlechtProperty() {
		return this.geschlecht;
	}


	public final String getGeschlecht() {
		return this.geschlechtProperty().get();
	}


	public final void setGeschlecht(final String geschlecht) {
		this.geschlechtProperty().set(geschlecht);
	}


	public final SimpleIntegerProperty plzProperty() {
		return this.plz;
	}


	public final int getPlz() {
		return this.plzProperty().get();
	}


	public final void setPlz(final int plz) {
		this.plzProperty().set(plz);
	}


	public final SimpleStringProperty ortProperty() {
		return this.ort;
	}


	public final String getOrt() {
		return this.ortProperty().get();
	}


	public final void setOrt(final String ort) {
		this.ortProperty().set(ort);
	}


	public final SimpleStringProperty strasseProperty() {
		return this.strasse;
	}


	public final String getStrasse() {
		return this.strasseProperty().get();
	}


	public final void setStrasse(final String strasse) {
		this.strasseProperty().set(strasse);
	}


	public final SimpleStringProperty ibanProperty() {
		return this.iban;
	}


	public final String getIban() {
		return this.ibanProperty().get();
	}


	public final void setIban(final String iban) {
		this.ibanProperty().set(iban);
	}


	public final SimpleStringProperty bicProperty() {
		return this.bic;
	}


	public final String getBic() {
		return this.bicProperty().get();
	}


	public final void setBic(final String bic) {
		this.bicProperty().set(bic);
	}


	public final SimpleStringProperty bankProperty() {
		return this.bank;
	}


	public final String getBank() {
		return this.bankProperty().get();
	}


	public final void setBank(final String bank) {
		this.bankProperty().set(bank);
	}


	public final SimpleStringProperty emailProperty() {
		return this.email;
	}


	public final String getEmail() {
		return this.emailProperty().get();
	}


	public final void setEmail(final String email) {
		this.emailProperty().set(email);
	}

	public final SimpleStringProperty personalnummerProperty() {
		return this.personalnummer;
	}


	public final String getPersonalnummer() {
		return this.personalnummerProperty().get();
	}


	public final void setPersonalnummer(final String personalnummer) {
		this.personalnummerProperty().set(personalnummer);
	}

	public final SimpleStringProperty telefonProperty() {
		return this.telefon;
	}


	public final String getTelefon() {
		return this.telefonProperty().get();
	}


	public final void setTelefon(final String telefon) {
		this.telefonProperty().set(telefon);
	}

	public final StringProperty grundProperty() {
		return this.grund;
	}


	public final String getGrund() {
		return this.grundProperty().get();
	}


	public final void setGrund(final String grund) {
		this.grundProperty().set(grund);
	}

	public final SimpleStringProperty hausnummerProperty() {
		return this.hausnummer;
	}
	

	public final String getHausnummer() {
		return this.hausnummerProperty().get();
	}
	

	public final void setHausnummer(final String hausnummer) {
		this.hausnummerProperty().set(hausnummer);
	}
	

}
