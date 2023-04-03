package mitarbeiterVerwaltung;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/*
 * • Diese Klasse erzeugt ein Dialogfenster für das Anlegen einer neuen Abwesenheit.
 * • Der Nutzer kann einen Mitarbeiter für einen Abwesenheit auswählen anschließend den Grund für die Abwesenheit angeben.
 * • Dabei wird über eine ComboBox aus einer vorgegebenen Liste von Abwesenheitsgründen ausgewählt.
 * • Zum Schluss wird der Abwesende Mitarbeiter in die Datenbank gespeichert.
 */
public class AbwesenheitDialog extends Dialog<ButtonType>{

	// ObservableList-Objekt für die Anzeige der Mitarbeiter in der Tabelle.
	private ObservableList<MitarbeiterFX> mitarbeiter = FXCollections.observableArrayList();

	public AbwesenheitDialog() {
		
		this.setTitle("Abwesenheit eintragen");
		
		// Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(10);
		gp.prefHeight(100);
		gp.minWidth(400);

		//Erstellt ein DatePicker-Objekt und setzt den Wert auf das aktuelle Datum.
		DatePicker datePickerVon = new DatePicker();
		datePickerVon.setValue(LocalDate.now());

		//Erstellt ein DatePicker-Objekt und setzt den Wert auf das aktuelle Datum.
		DatePicker datePickerBis = new DatePicker();
		datePickerBis.setValue(LocalDate.now());

		//Erstellt eine TableView mit drei Tabellenspalten und fügt diese der TableView hinzu. Außerdem wird die Methode "sucheMitarbeiter" aufgerufen.
		TableColumn<MitarbeiterFX, String> vornameCol = new TableColumn<>("Vorname");
		vornameCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		vornameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> nachnameCol = new TableColumn<>("Nachname");
		nachnameCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		nachnameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> personalnummerCol = new TableColumn<>("Personalnummer");
		personalnummerCol.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
		personalnummerCol.setPrefWidth(150);
		TableView<MitarbeiterFX> alleMA = new TableView<>(mitarbeiter);
		alleMA.getColumns().addAll(vornameCol, nachnameCol, personalnummerCol);
		sucheMitarbeiter();

		//Erstellt VBox mit Texten für Datum, Mitarbeiterauswahl und Grundangabe.
		VBox vBox1 = new VBox();
		vBox1.setStyle("-fx-background-color: lightgreen;");
		vBox1.setPrefHeight(35);
		vBox1.setPrefWidth(100);	    
		Text text = new Text("Datum von");
		text.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox1.setAlignment(Pos.CENTER);
		vBox1.getChildren().add(text);

		VBox vBox4 = new VBox();
		vBox4.setStyle("-fx-background-color: lightgreen;");
		vBox4.setPrefHeight(35);
		vBox4.setPrefWidth(100);	    
		Text text4 = new Text("bis");
		text4.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox4.setAlignment(Pos.CENTER);
		vBox4.getChildren().add(text4);

		VBox vBox2 = new VBox();
		vBox2.setStyle("-fx-background-color: lightgreen;");
		vBox2.setPrefHeight(35);
		vBox2.setPrefWidth(100);    
		Text text2 = new Text("Mitarbeiter auswählen");
		text2.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox2.setAlignment(Pos.CENTER);
		vBox2.getChildren().add(text2);

		VBox vBox3 = new VBox();
		vBox3.setStyle("-fx-background-color: lightgreen;");
		vBox3.setPrefHeight(35);
		vBox3.setPrefWidth(100);	    
		Text text3 = new Text("Grund angeben");
		text3.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox3.setAlignment(Pos.CENTER);
		vBox3.getChildren().add(text3);

		//Erstellt eine ComboBox für verschiedene Abwesenheitsgründe.
		ObservableList<String> options = FXCollections.observableArrayList("Krank", "Urlaub", "Schulung", "Familiäres Ereigniss", "Pflege naher Angehöriger", "Umzug", "Sonstige");
		ComboBox<String> grund2 = new ComboBox<>(options);
		grund2.setValue("Krank");
		
		//Erstellt einen Button namens "Abwesenheit hinzufügen" und deaktiviere ihn.
		Button abwesenheit = new Button("Abwesenheit hinzufügen");
		abwesenheit.setDisable(true);	    

		//Listener für die Auswahl eines Mitarbeiters in der TableView und Aktivierung/Deaktivierung des Abwesenheits-Buttons.
		alleMA.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {

			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1,
					MitarbeiterFX arg2) {
				if(arg2 != null) {
					abwesenheit.setDisable(false);
				}
				else {
					abwesenheit.setDisable(true); 
				}
			}
		});

		/*
		 * ActionListener, der bei Betätigung des Buttons "Abwesenheit hinzufügen" ein Abwesenheitsobjekt erstellt und in der Datenbank speichert,
		 * sofern der ausgewählte Mitarbeiter an den ausgewählten Tagen nicht bereits als abwesend eingetragen ist.
		 * Falls doch, wird dem Nutzer eine Fehlermeldung angezeigt.
		 */
		abwesenheit.setOnAction(e -> {
			Stream<LocalDate> nextDays = datePickerVon.getValue().datesUntil(datePickerBis.getValue().plusDays(1));
			boolean informationGezeigt = false;
			int[] tage = new int[1];
			nextDays.forEach(a -> {
				try {
					if (!Datenbank.isMitarbeiterAbwesend(alleMA.getSelectionModel().getSelectedItem().getPersonalnummer(), Date.valueOf(a))) {
						Abwesenheit objekt = new Abwesenheit(alleMA.getSelectionModel().getSelectedItem().getPersonalnummer(), Date.valueOf(a), grund2.getValue());
						Datenbank.insertAbwesenheit(objekt);
						tage[0]++;

					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Mitarbeiter ist bereits abwesend");
						alert.setHeaderText("Der Mitarbeiter " + alleMA.getSelectionModel().getSelectedItem().getVorname() + " " + alleMA.getSelectionModel().getSelectedItem().getNachname() + " ist bereits am " + a.toString() + " abwesend!");
						alert.showAndWait();
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
			if (tage[0] > 0 && !informationGezeigt) { //Musste in seperate if Abfrage gelegt werden, ansosnten würde Information 1x/pro Tag angezeigt werden
				JOptionPane.showMessageDialog(null, "Der Mitarbeiter wurde erfolgreich zur Abwesenheitsliste hinzugefügt.", "Information", JOptionPane.INFORMATION_MESSAGE);
				informationGezeigt = true;
			}

		}); 

		//Hinzufügen der verschiedenen Elemente zur GridPane.
		HBox buttons = new HBox(abwesenheit);
		gp.add(buttons, 0, 2);
		gp.add(vBox1, 0, 0);
		gp.add(vBox3, 2, 0);
		gp.add(vBox4, 1, 0);
		gp.add(vBox2, 3, 0);
		gp.add(datePickerVon, 0, 1);
		gp.add(datePickerBis, 1, 1);
		gp.add(grund2, 2, 1);
		gp.add(alleMA, 3, 1);


		this.getDialogPane().setContent(gp);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(abbrechen);
	}

	/*
	Diese Methode liest alle Mitarbeiter aus der Datenbank und fügt sie der Tabelle hinzu.
	Wenn ein Fehler auftritt, wird eine Fehlermeldung angezeigt.
	 */
	private void sucheMitarbeiter() {
		try {
			ArrayList<Mitarbeiter> alMitarbeiter = Datenbank.readMitarbeiter();
			mitarbeiter.clear();
			for (Mitarbeiter einMitarbeiter : alMitarbeiter)
				mitarbeiter.add(new MitarbeiterFX(einMitarbeiter));
		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

}
