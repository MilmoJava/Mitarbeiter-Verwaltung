package mitarbeiterVerwaltung;

public class Mitarbeiter {

	private String vorname;
	private String nachname;
	private String personalnummer;
	private String geschlecht;
	private int plz;
	private String ort;
	private String strasse;
	private String hausnummer; 
	private String iban;
	private String bic;
	private String bank;
	private String telefon;
	private String email;

	public Mitarbeiter(String vorname, String nachname, String personalnummer, String geschlecht, int plz, String ort, String strasse,
			String hausnummer, String iban, String bic, String bank, String telefon, String email) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.personalnummer = personalnummer;
		this.geschlecht = geschlecht;
		this.plz = plz;
		this.ort = ort;
		this.strasse = strasse;
		this.hausnummer = hausnummer;
		this.iban = iban;
		this.bic = bic;
		this.bank = bank;
		this.telefon = telefon;
		this.email = email;
	}

	public Mitarbeiter() {

	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getPersonalnummer() {
		return personalnummer;
	}

	public void setPersonalnummer(String personalnummer) {
		this.personalnummer = personalnummer;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getGeschlecht() {
		return geschlecht;
	}

	public void setGeschlecht(String geschlecht) {
		this.geschlecht = geschlecht;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	@Override
	public String toString() {
		return "Vorname=" + vorname + ", Nachname=" + nachname + ", Personalnummer=" + personalnummer;
	}
}
