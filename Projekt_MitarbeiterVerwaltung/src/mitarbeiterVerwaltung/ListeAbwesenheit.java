package mitarbeiterVerwaltung;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

/*
 * • Diese Klasse ist ein Dialog in der die Liste der abwesenden Mitarbeiter angezeigt wird.
 * • Die angezeigten Daten werden aus der Datenbank gelesen und in der TableView angezeigt.
 * • Die TableView wird durch die Methode "updateTV" aktualisiert.
 * • Es ist möglich eine Abwesenheit zu löschen indem ein Mitarbeiter aus der TableView ausgewählt und auf den Button "Entfernen" geklickt wird.
 * • Die Klasse verwendet auch einen DatePicker um die Informationen zu einem bestimmten Datum anzuzeigen.
 */
public class ListeAbwesenheit  extends Dialog<ButtonType>{

	//Eine ObservableList vom Typ MitarbeiterFX wird erstellt, um die Daten der abwesenden Mitarbeiter anzuzeigen.
	private ObservableList<MitarbeiterFX> mitarbeiter2 = FXCollections.observableArrayList();
	
	public ListeAbwesenheit(Abwesenheit grund) { 
		this.setTitle("Liste der abwesenden Mitarbeiter");

		//Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(0);
		gp.prefHeight(100);
		gp.minWidth(400);

		//Erstellt ein DatePicker Objekt und setzt den Wert auf das aktuelle Datum.
		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());

		/*
		 * Setzt den EventHandler für den DatePicker, so dass bei Änderung des Datums die Methode updateTV aufgerufen wird,
		 * um die Tabelle mit den Abwesenheiten zu aktualisieren.
		 */
		datePicker.setOnAction(event -> {
			LocalDate date = datePicker.getValue();
			updateTV(date);
		});

		// Erstellt einen Button "löschen" und deaktiviert ihn zu Beginn.
		Button löschen = new Button("Entfernen");
		löschen.setDisable(true);

		//Erzeugt eine Tabelle mit Spalten und fügt sie zur GridPane hinzu. Anschließend wird die Tabelle mit der Methode updateTV aktualisiert.
		AbwesenheitDialog ab = new AbwesenheitDialog();
		TableColumn<MitarbeiterFX, String> ansichtVorname = new TableColumn<>("Vorname");
		ansichtVorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		ansichtVorname.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> ansichtNachname = new TableColumn<>("Nachname");
		ansichtNachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		ansichtNachname.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> ansichtPersonalnummer = new TableColumn<>("Personalnummer");
		ansichtPersonalnummer.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
		ansichtPersonalnummer.setPrefWidth(150);
		TableView<MitarbeiterFX> ansichtMA = new TableView<>(mitarbeiter2);
		TableColumn<MitarbeiterFX, String> ansichtGrund = new TableColumn<>("Grund");
		ansichtGrund.setCellValueFactory(new PropertyValueFactory<>("grund"));
		ansichtGrund.setPrefWidth(150);
		ansichtMA.getColumns().addAll(ansichtVorname, ansichtNachname, ansichtPersonalnummer,ansichtGrund);
		updateTV(datePicker.getValue());

		gp.add(datePicker, 0, 0);
		gp.add(ansichtMA, 0,1);
		gp.add(löschen, 0, 2);

		//Fügt einen Listener zur TableView ansichtMA hinzu, der den Button "löschen" wird auf deaktiviert gesetzt, wenn keine Zeile im TableView ausgewählt ist.
		ansichtMA.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {

			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if (arg2 != null) {					
					löschen.setDisable(false);
				} else {
					löschen.setDisable(true);
				}
			}
		});

		//Das Event für den "Löschen" Button, bei dem eine Abwesenheit aus der Datenbank entfernt wird.
		löschen.setOnAction(e -> { 
			if(ansichtMA.getSelectionModel().getSelectedItem().getPerson().getPersonalnummer() != null) {
				try {
					Datenbank.deleteAbwesenheit(
							ansichtMA.getSelectionModel().getSelectedItem().getPerson().getPersonalnummer(),
							Date.valueOf(datePicker.getValue()));
					updateTV(datePicker.getValue());

				} catch (SQLException e1) {
					new Alert(AlertType.ERROR, e1.toString()).showAndWait();
				}
			}

		});

		this.getDialogPane().setContent(gp);

		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(abbrechen);

	}
	/*
	 * Aktualisiert die Tabelle der abwesenden Mitarbeiter anhand des ausgewählten Datums.
	 * Die Methode ruft die Methode "readAbwesenheit" aus der Datenbank auf und holt sich eine Liste aller Abwesenheiten an dem ausgewählten Datum.
	 * Für jeden Eintrag in der Liste holt sich die Methode alle Mitarbeiter mit der entsprechenden Personalnummer und fügt sie einer neuen ArrayList hinzu. 
	 * Dabei wird jeder Mitarbeiter in einen MitarbeiterFX umgewandelt, damit er in der TableView angezeigt werden kann.
	 * Der Abwesenheitsgrund wird über die neue Methode setGrund in den MitarbeiterFX gespeichert. Schließlich wird die ObservableList "mitarbeiter2" aktualisiert.
	 */
	public void updateTV(LocalDate datum) {
		try {
			ArrayList<Abwesenheit> a = Datenbank.readAbwesenheit(Date.valueOf(datum));
			ArrayList<MitarbeiterFX> neu = new ArrayList<>();
			for (Abwesenheit s : a) {
				ArrayList<Mitarbeiter> dbMA = Datenbank.readMitarbeiter(s.getMitarbeiter1());
				for (Mitarbeiter ma : dbMA) {
					MitarbeiterFX maFX = new MitarbeiterFX(ma);
					maFX.setGrund(s.getGrund());
					neu.add(maFX);
				}
			}
			mitarbeiter2.setAll(neu);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
