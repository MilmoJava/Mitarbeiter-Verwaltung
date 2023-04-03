package mitarbeiterVerwaltung;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/*
 * • Die Klasse "DetailsDialog" erstellt ein Dialogfeld zur Anzeige und Bearbeitung von Mitarbeiterinformationen.
 * • Das Dialogfeld enthält Textfelder und ComboBoxen um die Mitarbeiterdaten anzuzeigen und zu ändern.
 */
public class DetailsDialog extends Dialog<ButtonType>{

	public DetailsDialog(Mitarbeiter mitarbeiter) {
		this.setTitle("Details");

		// Erstellt eine ObservableList mit den Geschlechter-Optionen "Männlich", "Weiblich", "Divers" und initialisiert
		// damit ein ComboBox-Objekt.
		ObservableList<String> options = FXCollections.observableArrayList("Männlich", "Weiblich", "Divers");
		ComboBox<String> comboBox = new ComboBox<>(options);
		if(mitarbeiter.getGeschlecht() != null) 
			comboBox.setValue(mitarbeiter.getGeschlecht());

		// Erstellen der Textfelder zur Anzeige und Bearbeitung der Mitarbeiterdaten und entsprechendem Inhalt aus dem Mitarbeiter-Objekt.
		TextField vornametxt = new TextField(mitarbeiter.getVorname());
		vornametxt.setPrefWidth(200);
		TextField nachnametxt = new TextField(mitarbeiter.getNachname());
		nachnametxt.setPrefWidth(200);
		TextField personalnummertxt = new TextField(mitarbeiter.getPersonalnummer());
		personalnummertxt.setPrefWidth(200);
		TextField plztxt = new TextField(Integer.toString(mitarbeiter.getPlz()));
		plztxt.setPrefWidth(200);
		TextField orttxt = new TextField(mitarbeiter.getOrt());
		orttxt.setPrefWidth(200);
		TextField strassetxt = new TextField(mitarbeiter.getStrasse());
		strassetxt.setPrefWidth(200);	
		TextField hausnummertxt = new TextField((mitarbeiter.getHausnummer()));
		hausnummertxt.setPrefWidth(200);
		TextField ibantxt = new TextField(mitarbeiter.getIban());
		ibantxt.setPrefWidth(200);
		TextField bictxt = new TextField(mitarbeiter.getBic());
		bictxt.setPrefWidth(200);
		TextField banktxt = new TextField(mitarbeiter.getBank());
		banktxt.setPrefWidth(200);
		TextField telefontxt = new TextField(mitarbeiter.getTelefon());
		telefontxt.setPrefWidth(200);
		TextField emailtxt = new TextField(mitarbeiter.getEmail());
		emailtxt.setPrefWidth(200);

		//Erstellt ein GridPane mit Labels und Textfeldern zur Anzeige und Eingabe der Mitarbeiterdaten.
		GridPane gp = new GridPane();
		gp.add(new Label("Vorname"), 0, 0);
		gp.add(new Label("Nachname"), 0, 1);
		gp.add(new Label("Personalnummer"), 0, 2);
		gp.add(new Label("Geschlecht"), 0, 3);
		gp.add(new Label("PLZ"), 0, 4);
		gp.add(new Label("Ort"), 0, 5);
		gp.add(new Label("Strasse"), 0, 6);
		gp.add(new Label("Hausnummer"), 0, 7);
		gp.add(new Label("IBAN"), 0, 8);
		gp.add(new Label("BIC"), 0, 9);
		gp.add(new Label("Bank"), 0, 10);
		gp.add(new Label("Telefon"), 0, 11);
		gp.add(new Label("Email"), 0, 12);
		gp.add(vornametxt, 1, 0);
		gp.add(nachnametxt, 1, 1);
		gp.add(personalnummertxt, 1, 2);
		gp.add(comboBox, 1, 3);
		gp.add(plztxt, 1, 4);
		gp.add(orttxt, 1, 5);
		gp.add(strassetxt, 1, 6);
		gp.add(hausnummertxt, 1, 7);
		gp.add(ibantxt, 1, 8);
		gp.add(bictxt, 1, 9);
		gp.add(banktxt, 1, 10);
		gp.add(telefontxt, 1, 11);
		gp.add(emailtxt, 1, 12);

		this.getDialogPane().setContent(gp);

		// Hier werden Dialog-Buttons hinzugefügt: "Speichern" und "Abbrechen"
		ButtonType speichern = new ButtonType("Speichern", ButtonData.OK_DONE);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(speichern, abbrechen);
		
		/*
		 * • Diese Methode setzt den Callback für den "Speichern"-Button im Details-Dialog.
		 * • Wenn der Button gedrückt wird, werden die Eingabefelder des Dialogs ausgelesen und in ein Mitarbeiter-Objekt gespeichert.
		 * • Anschließend wird entweder ein neuer Mitarbeiter in die Datenbank eingefügt oder ein bestehender Mitarbeiter aktualisiert.
		 * • Im Falle einer SQLException wird eine Fehlermeldung ausgegeben.
		 * • Die Methode gibt den gedrückten Button-Typ zurück.
		 */
		this.setResultConverter(new Callback<ButtonType, ButtonType>(){

			@Override

			public ButtonType call(ButtonType arg0) {
				if(arg0 == speichern) {

					mitarbeiter.setVorname(vornametxt.getText());
					mitarbeiter.setNachname(nachnametxt.getText());
					mitarbeiter.setGeschlecht(comboBox.getSelectionModel().getSelectedItem());
					mitarbeiter.setPlz(Integer.parseInt(plztxt.getText()));
					mitarbeiter.setOrt(orttxt.getText());
					mitarbeiter.setStrasse(strassetxt.getText());
					mitarbeiter.setHausnummer(hausnummertxt.getText());
					mitarbeiter.setIban(ibantxt.getText());
					mitarbeiter.setBic(bictxt.getText());
					mitarbeiter.setBank(banktxt.getText());
					mitarbeiter.setTelefon(telefontxt.getText());
					mitarbeiter.setEmail(emailtxt.getText());

					try {
						if(mitarbeiter.getPersonalnummer() == null) {
							mitarbeiter.setPersonalnummer(personalnummertxt.getText());
							Datenbank.insertMitarbeiter(mitarbeiter);
						}
						else {
							Datenbank.updateMitarbeiter(mitarbeiter);
						}
					}catch (SQLException e) {
					    Alert alert = new Alert(AlertType.ERROR, "Es ist ein Fehler beim Hinzufügen oder Aktualisieren des Mitarbeiters aufgetreten."
					    		+ " Bitte stellen Sie sicher, dass alle erforderlichen Felder ausgefüllt sind und versuchen Sie es erneut.");
					    alert.showAndWait();
					}
				}
				return arg0;	
			}
		});
	}

}
