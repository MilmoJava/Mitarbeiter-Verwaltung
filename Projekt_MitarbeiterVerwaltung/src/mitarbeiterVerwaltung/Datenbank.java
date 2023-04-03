package mitarbeiterVerwaltung;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.TreeSet;

public class Datenbank {


	//Hier wird die Datenbank Location angegeben.
	private static final String DB_LOCATION = "C:\\Users\\hilmo\\OneDrive\\Desktop\\DatenbankMitarbeiter";
	private static final String CONNECTION_URL = "jdbc:derby:" + DB_LOCATION + ";create=true"; 

	// Definiert die Namen der Tabellen und Spalten die für die Mitarbeiter, Dienstpläne und Abwesenheiten verwendet werden.
	private static final String MITARBEITER_TABLE = "Mitarbeiter";
	private static final String MITARBEITER_VORNAME = "MitarbeiterVorname";
	private static final String MITARBEITER_NACHNAME = "MitarbeiterNachname";
	private static final String MITARBEITER_PERSONALNUMMER = "MitarbeiterPersonalnummer";
	private static final String MITARBEITER_GESCHLECHT = "MitarbeiterGeschlecht";
	private static final String MITARBEITER_PLZ = "MitarbeiterPLZ";
	private static final String MITARBEITER_ORT = "MitarbeiterOrt";
	private static final String MITARBEITER_STRASSE = "MitarbeiterStrasse";
	private static final String MITARBEITER_HAUSNUMMER = "MitarbeiterHausnummer";
	private static final String MITARBEITER_IBAN = "MitarbeiterIban";
	private static final String MITARBEITER_BIC = "MitarbeiterBic";
	private static final String MITARBEITER_BANK = "MitarbeiterBank";
	private static final String MITARBEITER_TELEFON = "MitarbeiterTelefon";
	private static final String MITARBEITER_EMAIL = "MitarbeiterEmail";
	private static final String MITARBEITER_DELETED = "MitarbeiterDeleted";
	private static final String DIENSTPLANMITARBEITER_TABLE = "Dienstplan";
	private static final String DIENSTPLANMITARBEITER_ID = "DienstplanMitarbeiterId";
	private static final String DIENSTPLANMITARBEITER_DATUM = "DienstplanMitarbeiterDatum";
	private static final String DIENSTPLANMITARBEITER_MITARBEITER1 = "DienstplanMitarbeiterMitarbeiter1";
	private static final String DIENSTPLANMITARBEITER_SCHICHT = "DienstplanMitarbeiterSchicht";
	private static final String ABWESENHEIT_TABLE = "Abwesenheit";
	private static final String ABWESENHEIT_ID = "AbwesenheitId";
	private static final String ABWESENHEIT_MITARBEITER1 = "AbwesenheitMitarbeiter1";
	private static final String ABWESENHEIT_DATUM = "AbwesenheitDatum";
	private static final String ABWESENHEIT_GRUND = "AbwesenheitGrund";
	
/**
 * 
 * • Falls die Tabellen bereits nicht vorhanden sind werden sie erstellt.
 * • @throws SQLException falls ein Fehler bei der Verbindung zur Datenbank auftritt.
 */
	public static void dropAndCreateTable() throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.createStatement();			
			rs = conn.getMetaData().getTables(null, null, MITARBEITER_TABLE.toUpperCase(), new String[] {"TABLE"});

			if(!rs.next()) {
				String ct = "CREATE TABLE " + MITARBEITER_TABLE + " (" +
						MITARBEITER_VORNAME + " VARCHAR(200)," +
						MITARBEITER_NACHNAME + " VARCHAR(200)," +
						MITARBEITER_PERSONALNUMMER + " VARCHAR(200)," +
						MITARBEITER_GESCHLECHT+ " VARCHAR(200)," +
						MITARBEITER_PLZ + " INTEGER," +
						MITARBEITER_ORT + " VARCHAR(200)," +
						MITARBEITER_STRASSE + " VARCHAR(200)," +
						MITARBEITER_HAUSNUMMER + " VARCHAR(200)," +
						MITARBEITER_IBAN + " VARCHAR(200)," +
						MITARBEITER_BIC + " VARCHAR(200)," +
						MITARBEITER_BANK + " VARCHAR(200)," +
						MITARBEITER_TELEFON + " VARCHAR(200)," +
						MITARBEITER_EMAIL + " VARCHAR(200)," +
						MITARBEITER_DELETED + " BOOLEAN," + 
						"PRIMARY KEY(" + MITARBEITER_PERSONALNUMMER + "))";
				stmt.executeUpdate(ct);
			}

			rs = conn.getMetaData().getTables(null, null, DIENSTPLANMITARBEITER_TABLE.toUpperCase(), new String[] {"TABLE"});

			if(!rs.next()) {
				String st = "CREATE TABLE " + DIENSTPLANMITARBEITER_TABLE + " (" +
						DIENSTPLANMITARBEITER_ID + " INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1)," + // ID wird jedes mal um 1 mehr
						DIENSTPLANMITARBEITER_DATUM + " DATE," +
						DIENSTPLANMITARBEITER_MITARBEITER1 + " VARCHAR(200) references " + MITARBEITER_TABLE + "(" + MITARBEITER_PERSONALNUMMER + ")," +
						DIENSTPLANMITARBEITER_SCHICHT + " BOOLEAN," + 
						"CONSTRAINT primary_key PRIMARY KEY (" + DIENSTPLANMITARBEITER_ID + "))"; 
				System.out.println(st);
				stmt.executeUpdate(st);
			}

			rs = conn.getMetaData().getTables(null, null, ABWESENHEIT_TABLE.toUpperCase(), new String[] {"TABLE"});
			if(!rs.next()) {
				String at = "CREATE TABLE " + ABWESENHEIT_TABLE + " (" +
						ABWESENHEIT_ID + " INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY(Start with 1, Increment by 1)," +
						ABWESENHEIT_MITARBEITER1 + " VARCHAR(200) references " + MITARBEITER_TABLE + "(" + MITARBEITER_PERSONALNUMMER + ")," +
						ABWESENHEIT_DATUM + " DATE," +
						ABWESENHEIT_GRUND + " VARCHAR(200)," +

					"PRIMARY KEY(" + ABWESENHEIT_ID + "))";
				stmt.executeUpdate(at);

			}
		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Liest alle DienstplanMitarbeiter aus der Datenbank und gibt sie als ArrayList zurück.
	 * • @return ArrayList vom DienstplanMitarbeiter.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<DienstplanMitarbeiter> readDienstplanMitarbeiter() throws SQLException{
		return readDienstplanMitarbeiter(null, null);
	}

	/**
	 * 
	 * • Liest alle DienstplanMitarbeiter mit einem Datum und einer bestimmten Schicht aus der Datenbank und gibt sie als ArrayList zurück.
	 * • @param datum der DienstplanMitarbeiter, die gelesen werden sollen.
	 * • @param schicht der DienstplanMitarbeiter(true für die Spätschicht, false für die Frühschicht) die gelesen werden sollen.
	 * • @return ArrayList von DienstplanMitarbeiter die den angegebenen Kriterien entsprechen.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<DienstplanMitarbeiter> readDienstplanMitarbeiter(Date datum, Boolean schicht) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<DienstplanMitarbeiter> alMitarbeiter = new ArrayList<>();
		String select = "SELECT * FROM " + DIENSTPLANMITARBEITER_TABLE;
		if(datum != null)
			select += " WHERE " + DIENSTPLANMITARBEITER_DATUM + "=? and DienstplanMitarbeiterSchicht = ?";

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			if(datum != null) {
				stmt.setDate(1, datum);
				stmt.setBoolean(2, schicht);
			}
			rs = stmt.executeQuery();
			while(rs.next())
				alMitarbeiter.add(new DienstplanMitarbeiter(rs.getDate(DIENSTPLANMITARBEITER_DATUM),
						rs.getString(DIENSTPLANMITARBEITER_MITARBEITER1), rs.getBoolean(DIENSTPLANMITARBEITER_SCHICHT)));

			rs.close();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
		return alMitarbeiter;
	}

	/**
	 * 
	 * • Liest alle Mitarbeiter aus der Datenbank.
	 * • @return ArrayList von Mitarbeitern.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<Mitarbeiter> readMitarbeiter() throws SQLException{
		return readMitarbeiter(null);
	}
	
	/**
	 * 
	 * • Liest einen Mitarbeiter mit gegebener Personalnummer aus der Datenbank.
	 * • @param personalnummer des gesuchten Mitarbeiters.
	 * • @return ArrayList von Mitarbeitern.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<Mitarbeiter> readMitarbeiter(String personalnummer) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Mitarbeiter> alMitarbeiter = new ArrayList<>();
		String select = "SELECT * FROM " + MITARBEITER_TABLE + " WHERE " + MITARBEITER_DELETED + "=?";
		if(personalnummer != null)
			select += " AND " + MITARBEITER_PERSONALNUMMER + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setBoolean(1, false);
			if(personalnummer != null)
				stmt.setString(2, personalnummer);
			rs = stmt.executeQuery();
			while(rs.next())
				alMitarbeiter.add(new Mitarbeiter(rs.getString(MITARBEITER_VORNAME), rs.getString(MITARBEITER_NACHNAME),
						rs.getString(MITARBEITER_PERSONALNUMMER), rs.getString(MITARBEITER_GESCHLECHT), rs.getInt(MITARBEITER_PLZ), rs.getString(MITARBEITER_ORT),
						rs.getString(MITARBEITER_STRASSE), rs.getString(MITARBEITER_HAUSNUMMER), rs.getString(MITARBEITER_IBAN), rs.getString(MITARBEITER_BIC), 
						rs.getString(MITARBEITER_BANK), rs.getString(MITARBEITER_TELEFON), rs.getString(MITARBEITER_EMAIL)));

			rs.close();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
		return alMitarbeiter;
	}

	/**
	 * 
	 * • Gibt eine ArrayList von Abwesenheit-Objekten zurück.
	 *   Wenn es keine Einträge gibt wird eine leere Liste zurückgegeben.
	 * • @return ArrayList von Abwesenheit-Objekten.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<Abwesenheit> readAbwesenheit() throws SQLException{
		return readAbwesenheit(null);
	}

	/**
	 * 
	 * • Prüft ob der Mitarbeiter mit der angegebenen Personalnummer an dem angegebenen Datum bereits in der Tabelle ABWESENHEIT_TABLE ist.
	 * • @param datum an dem die Abwesenheit überprüft wird.
	 * • @param personalnummer des Mitarbeiters.
	 * • @return true, wenn der Mitarbeiter nicht abwesend ist.
	 * • @throws SQLException
	 */
	public static boolean isNotAbwesend(Date datum, String personalnummer) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null; // ResultSet würde false zurückgeben wenn alMitarbeiter nicht leer ist und es mindestens einen Eintrag in der Tabelle ABWESENHEIT_TABLE gibt.
		ArrayList<Abwesenheit> alMitarbeiter = new ArrayList<>(); // gibt Liste aller Abwesenden MA zurück
		String select = "SELECT * FROM " + ABWESENHEIT_TABLE + " WHERE " + ABWESENHEIT_DATUM + "=?" + " AND " + ABWESENHEIT_MITARBEITER1 + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);

			stmt.setDate(1, datum);
			stmt.setString(2, personalnummer);
			rs = stmt.executeQuery();
			while(rs.next())
				alMitarbeiter.add(new Abwesenheit(rs.getInt(ABWESENHEIT_ID),rs.getString(ABWESENHEIT_MITARBEITER1), rs.getDate(ABWESENHEIT_DATUM),
						rs.getString(ABWESENHEIT_GRUND)));

			rs.close();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
		return alMitarbeiter.isEmpty(); //if true, ist die ABWESENHEIT_TABLE empty, bedeutet kein Datum und Personalnummer in ABWESENHEIT_TABLE vorhanden.
	}

	/**
	 * 
	 * • Liest Einträge aus der Tabelle ABWESENHEIT_TABLE.
	 * • @param datum, für das Abwesenheitseinträge ausgelesen werden sollen.
	 * • @return ArrayList von Abwesenheit Objekten.
	 * • @throws SQLException wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<Abwesenheit> readAbwesenheit(Date datum) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Abwesenheit> alMitarbeiter = new ArrayList<>();
		String select = "SELECT * FROM " + ABWESENHEIT_TABLE;
		if(datum != null)
			select += " WHERE " + ABWESENHEIT_DATUM + "=?";

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			if(datum != null)
				stmt.setDate(1, datum);
			rs = stmt.executeQuery();
			while(rs.next())
				alMitarbeiter.add(new Abwesenheit(rs.getInt(ABWESENHEIT_ID),rs.getString(ABWESENHEIT_MITARBEITER1), rs.getDate(ABWESENHEIT_DATUM),
						rs.getString(ABWESENHEIT_GRUND)));

			rs.close();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
		return alMitarbeiter;
	}
	
/**
 * 
 * • Prüft ob der Mitarbeiter mit der gegebenen Personalnummer an einem bestimmten Datum abwesend ist.
 * • @param personalnummer des Mitarbeiters die geprüft werden soll.
 * • @param datum an dem der Mitarbeiter geprüft werden soll.
 * • @return true  falls der Mitarbeiter abwesend ist, sonst false.
 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
 */
	public static boolean isMitarbeiterAbwesend(String personalnummer, Date datum) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean result = false;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement("SELECT * FROM " + ABWESENHEIT_TABLE + " WHERE " + ABWESENHEIT_MITARBEITER1 + "=? AND " + ABWESENHEIT_DATUM + "=?");
			stmt.setString(1, personalnummer);
			stmt.setDate(2, datum);
			rs = stmt.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			throw e;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				throw e;
			}
		}

		return result;
	}

	/**
	 * 
	 * • Liest die Abwesenheitsstatistik aus der Datenbank aus.
	 * • @param datumVon, aus dem die Abwesenheitsstatistik gelesen werden soll.
	 * • @param datumBis, aus dem die Abwesenheitsstatistik gelesen werden soll.
	 * • @param grund der Abwesenheit, nach dem gefiltert werden soll.
	 * • @return ArrayList mit Abwesenheit Objekten die im angegebenen Zeitraum und mit dem Grund abwesend waren.
	 * • @throws SQLException Falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static ArrayList<Abwesenheit> readAbwesenheitStatistik(Date datumVon, Date datumBis, String grund) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Abwesenheit> alMitarbeiter = new ArrayList<>();
		String select = "SELECT * FROM " + ABWESENHEIT_TABLE + " WHERE " + ABWESENHEIT_DATUM + " BETWEEN ? AND ?"; // BETWEEN ? bedeutet, dass die Abwesenheitseinträge in der Spalte ABWESENHEIT_DATUM zwischen zwei bestimmten Daten liegt
		if (grund != null) {
			select += " AND " + ABWESENHEIT_GRUND + " = ?"; // ein grund welcher in der ABWESENHEIT_GRUND Spalte/Tabelle gespeichert ist wird mit dem select ersetzt
		}

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			stmt = conn.prepareStatement(select);
			stmt.setDate(1, datumVon);
			stmt.setDate(2, datumBis);
			if (grund != null) {
				stmt.setString(3, grund);
			}
			rs = stmt.executeQuery();
			while(rs.next())
				alMitarbeiter.add(new Abwesenheit(rs.getInt(ABWESENHEIT_ID),rs.getString(ABWESENHEIT_MITARBEITER1), rs.getDate(ABWESENHEIT_DATUM),
						rs.getString(ABWESENHEIT_GRUND)));

			rs.close();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
		return alMitarbeiter;
	}


	/**
	 * 
	 * • Fügt einen neuen Mitarbeiter in die Datenbank hinzu.
	 * • @param person(Mitarbeiter) der gespeichert werden soll.
	 * • @throws SQLException wenn ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static void insertMitarbeiter(Mitarbeiter person) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + MITARBEITER_TABLE + " VALUES(" +
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," + 
					"?," +
					"?," +
					"? )";

			stmt = conn.prepareStatement(insert);

			stmt.setString(1, person.getVorname());
			stmt.setString(2, person.getNachname());
			stmt.setString(3, person.getPersonalnummer());
			stmt.setString(4, person.getGeschlecht());
			stmt.setInt(5, person.getPlz());
			stmt.setString(6, person.getOrt());
			stmt.setString(7, person.getStrasse());
			stmt.setString(8, person.getHausnummer());
			stmt.setString(9, person.getIban());
			stmt.setString(10, person.getBic());
			stmt.setString(11, person.getBank());
			stmt.setString(12, person.getTelefon());
			stmt.setString(13, person.getEmail());
			stmt.setBoolean(14, false);
			stmt.executeUpdate();


		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Speichert einen Mitarbeiter und die Schicht in die Datenbank.
	 * • @param person der DienstplanMitarbeiter
	 * • @param schicht true, falls der Mitarbeiter in der Schicht ist, sonst false.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auftritt.
	 */
	public static void insertDienstplanMitarbeiter(DienstplanMitarbeiter person, boolean schicht) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + DIENSTPLANMITARBEITER_TABLE + " (DienstplanMitarbeiterDatum,DienstplanMitarbeiterMitarbeiter1,DienstplanMitarbeiterSchicht"
					+ ") VALUES(" +
					"?," + 
					"?," + 
					"? )";  

			stmt = conn.prepareStatement(insert);

			stmt.setDate(1, person.getDatum());
			stmt.setString(2, person.getMitarbeiter1());
			stmt.setBoolean(3, schicht);

			stmt.executeUpdate();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Fügt eine neue Abwesenheit in die Datenbank hinzu.
	 * • @param person die eingefügt werden soll.
	 * • @throws SQLException falls ein Fehler beim Zugriff auf die Datenbank auf.
	 */
	public static void insertAbwesenheit(Abwesenheit person) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String insert = "INSERT INTO " + ABWESENHEIT_TABLE +" (AbwesenheitMitarbeiter1,AbwesenheitDatum,AbwesenheitGrund)" + " VALUES(" +
					"?," + 
					"?," + 
					"? )"; 

			stmt = conn.prepareStatement(insert);


			stmt.setString(1, person.getMitarbeiter1());
			stmt.setDate(2, person.getDatum());
			stmt.setString(3, person.getGrund());

			stmt.executeUpdate();


		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Aktualisiert die Daten eines Mitarbeiters in der Datenbank.
	 * • @param person(Mitarbeiter Objekt) mit den aktualisierten Daten.
	 * • @throws SQLException falls ein SQL-Fehler auftritt.
	 */
	public static void updateMitarbeiter(Mitarbeiter person) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String update = "UPDATE " + MITARBEITER_TABLE + " SET " +
					MITARBEITER_VORNAME + "=?," + 
					MITARBEITER_NACHNAME + "=?," + 
					MITARBEITER_GESCHLECHT + "=?," + 
					MITARBEITER_PLZ + "=?," + 
					MITARBEITER_ORT + "=?," + 
					MITARBEITER_STRASSE + "=?," + 
					MITARBEITER_HAUSNUMMER + "=?," + 
					MITARBEITER_IBAN + "=?," + 
					MITARBEITER_BIC + "=?," + 
					MITARBEITER_BANK + "=?," + 
					MITARBEITER_TELEFON + "=?," + 
					MITARBEITER_EMAIL + "=? WHERE " + MITARBEITER_PERSONALNUMMER + "=?"; // Email/Personalnummer

			stmt = conn.prepareStatement(update);


			stmt.setString(1, person.getVorname());
			stmt.setString(2, person.getNachname());
			stmt.setString(3, person.getGeschlecht());
			stmt.setInt(4, person.getPlz());
			stmt.setString(5, person.getOrt());
			stmt.setString(6, person.getStrasse());
			stmt.setString(7, person.getHausnummer());
			stmt.setString(8, person.getIban());
			stmt.setString(9, person.getBic());
			stmt.setString(10, person.getBank());
			stmt.setString(11, person.getTelefon());
			stmt.setString(12, person.getEmail());
			stmt.setString(13, person.getPersonalnummer());
			stmt.executeUpdate();


		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Diese Methode löscht den Mitarbeiter nicht wirklich aus der Datenbank, sondern setzt lediglich "delete" auf true
	 *   um später noch auf die Informationen zugreifen zu können. Dieses Vorgang ermöglicht es den Mitarbeiter nicht mehr anzuzeigen,
	 *   ohne seine Informationen dauerhaft aus der Datenbank zu entfernen.
	 * • @param person(Mitarbeiter), der aus der Datenbank gelöscht werden soll.
	 * • @throws SQLException falls ein SQL-Fehler auftritt.
	 */
	public static void deleteMitarbeiter(Mitarbeiter person) throws SQLException{ 
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String delete = "UPDATE " + MITARBEITER_TABLE + " SET "+ MITARBEITER_DELETED + "=? WHERE " + MITARBEITER_PERSONALNUMMER + "=?"; 

			stmt = conn.prepareStatement(delete);	
			stmt.setBoolean(1, true);
			stmt.setString(2, person.getPersonalnummer());
			stmt.executeUpdate();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Diese Methode löscht einen Eintrag aus der Tabelle ABWESENHEIT_TABLE der zu einem bestimmten Mitarbeiter und Datum passt.
	 *   Die Methode erwartet als Parameter die Personalnummer und das Datum der Abwesenheit.
	 * • @param id(Personalnummer des Mitarbeiters), dessen Abwesenheitseintrag gelöscht werden sollen.
	 * • @param datum(Datum des Abwesenheitseintrags), der gelöscht werden sollen.
	 * • @throws SQLException falls ein SQL-Fehler auftritt.
	 */
	public static void deleteAbwesenheit(String id, Date datum) throws SQLException{ 
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String delete = "DELETE FROM " + ABWESENHEIT_TABLE + " WHERE " + ABWESENHEIT_MITARBEITER1 + "=? AND AbwesenheitDatum = ?"; 

			stmt = conn.prepareStatement(delete);				
			stmt.setString(1, id);
			stmt.setDate(2, datum);
			stmt.executeUpdate();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

	/**
	 * 
	 * • Diese Methode löscht einen Eintrag aus der Tabelle DIENSTPLANMITARBEITER_TABLE der zu einem bestimmten Mitarbeiter und Datum passt.
	 * • @param (die Personalnummer) des betroffenen Mitarbeiters als String.
	 * • @param datum als Objekt, für das der Eintrag im Dienstplan des Mitarbeiters gelöscht werden soll.
	 * • @throws SQLException falls ein Fehler beim Löschen des Eintrags auftritt.
	 */
	public static void deleteDienstplanMitarbeiter(String id, Date datum) throws SQLException{
		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DriverManager.getConnection(CONNECTION_URL);
			String delete = "DELETE FROM " + DIENSTPLANMITARBEITER_TABLE + " WHERE " + DIENSTPLANMITARBEITER_MITARBEITER1 + "=? AND DienstplanMitarbeiterDatum = ?"; 

			stmt = conn.prepareStatement(delete);	
			stmt.setString(1, id);
			stmt.setDate(2, datum);
			System.out.println(datum);
			System.out.println(id.toString());
			stmt.executeUpdate();

		}
		catch (SQLException e) {
			throw e;
		}
		finally {
			try {
				if(stmt != null)
					stmt.close();
				if(conn != null)
					conn.close();
			}
			catch (SQLException e) {
				throw e;
			}
		}
	}

}
