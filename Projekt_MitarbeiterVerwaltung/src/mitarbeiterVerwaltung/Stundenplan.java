package mitarbeiterVerwaltung;

import java.time.LocalDate;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/*
 * • Diese Klasse ist ein Dialog die eine GUI für einen Dienstplan mit zwei Schichten (Vormittag und Nachmittag) bereitstellt.
 * • Sie enthält zwei TableView-Objekte die mit Objekten von MitarbeiterFX gefüllt werden.
 * • Es werden zwei Methoden aufgerufen um die Tabellen für den aktuellen Tag/Schicht zu aktualisieren.
 * • Die Klasse hat auch eine Methode zum löschen von Mitarbeitern aus dem Dienstplan,
 *   sowie einen Button zum hinzufügen von Mitarbeitern über einen DetailsDialogDienstplan. 
 */
public class Stundenplan extends Dialog<ButtonType>{

	//Zwei ObservableList vom Typ MitarbeiterFX werden erstellt, um die Daten der Mitarbeiter jeweils in der Schicht anzuzeigen.
	private ObservableList<MitarbeiterFX> mitarbeiter = FXCollections.observableArrayList();
	private ObservableList<MitarbeiterFX> mitarbeiter2 = FXCollections.observableArrayList();

	public Stundenplan() {
		this.setTitle("Dienstplan");

		// Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(10);
		gp.prefHeight(100);
		gp.minWidth(400);

		/*
		 * • Erstellt eine Tabelle mit drei Spalten für Vorname, Nachname und Personalnummer der Mitarbeiter.
		 * • Der TableView wird mit einer ObservableList von MitarbeiterFX initialisiert und die Methode "updateTVVormittag" wird aufgerufen,
		 *   um die Tabelle mit den Mitarbeitern für den aktuellen Tag/Schicht zu füllen.
		 */
		TableColumn<MitarbeiterFX, String> vornameCol = new TableColumn<>("Vorname"); 
		vornameCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		vornameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> nachnameCol = new TableColumn<>("Nachname");
		nachnameCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		nachnameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> personalnummerCol = new TableColumn<>("Personalnummer");
		personalnummerCol.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
		personalnummerCol.setPrefWidth(150);
		TableView<MitarbeiterFX> tvVormittag = new TableView<>(mitarbeiter);																		
		tvVormittag.getColumns().addAll(vornameCol, nachnameCol, personalnummerCol);
		updateTVVormittag(LocalDate.now());

		/*
		 * • Erstellt eine Tabelle mit drei Spalten für Vorname, Nachname und Personalnummer der Mitarbeiter.
		 * • Der TableView wird mit einer ObservableList von MitarbeiterFX initialisiert und die Methode "updateTVNachmittag" wird aufgerufen,
		 *   um die Tabelle mit den Mitarbeitern für den aktuellen Tag/Schicht zu füllen.
		 */
		TableColumn<MitarbeiterFX, String> vornameNachmittag = new TableColumn<>("Vorname");																								
		vornameNachmittag.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		vornameNachmittag.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> nachnameNachmmittag = new TableColumn<>("Nachname");
		nachnameNachmmittag.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		nachnameNachmmittag.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> personalnummerNachmittag = new TableColumn<>("Personalnummer");
		personalnummerNachmittag.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
		personalnummerNachmittag.setPrefWidth(150);
		TableView<MitarbeiterFX> tvNachmittag = new TableView<>(mitarbeiter2);
		tvNachmittag.getColumns().addAll(vornameNachmittag, nachnameNachmmittag, personalnummerNachmittag);
		updateTVNachmittag(LocalDate.now());

		//Erstellt ein DatePicker Objekt und setzt den Wert auf das aktuelle Datum und weist dem EventHandler "updateTVVormittag" und "updateTVNachmittag" zu.
		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now());

		datePicker.setOnAction(event -> {
			LocalDate date = datePicker.getValue();
			updateTVVormittag(date);
			updateTVNachmittag(date);

		});

		//Erstellt zwei Buttons. Der Button "Löschen" wird auf disable gesetzt.
		Button maHinzufügen = new Button("Mitarbeiter hinzufügen");
		Button löschen = new Button("Löschen");
		löschen.setDisable(true);

		// Füge Listener zum Aktivieren/Deaktivieren des Löschen-Buttons hinzu.
		tvVormittag.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {
			// legt die Buttons auf disable, bis ein Mitarbeiter ausgewählt wird.
			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if (arg2 != null) {					
					löschen.setDisable(false);
				} else {
					löschen.setDisable(true);
				}
			}
		});

		// Füge Listener zum Aktivieren/Deaktivieren des Löschen-Buttons hinzu.
		tvNachmittag.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {
			// legt die Buttons auf disable, bis ein Mitarbeiter ausgewählt wird.
			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if (arg2 != null) {					
					löschen.setDisable(false);
				} else {
					löschen.setDisable(true);
				}
			}
		});

		// ActionListener für den Löschen-Button, der den ausgewählten Mitarbeiter aus der Datenbank löscht und die Tabellen(Schichten) aktualisiert.
		löschen.setOnAction(e -> { 
			TableView<MitarbeiterFX> gewählterTable = null;
			if(tvVormittag.getSelectionModel().getSelectedItem() != null) {
				gewählterTable = tvVormittag;
			} else if (tvNachmittag.getSelectionModel().getSelectedItem() != null) {
				gewählterTable = tvNachmittag;
			}

			if(gewählterTable != null) {
				try {
					Datenbank.deleteDienstplanMitarbeiter(
							gewählterTable.getSelectionModel().getSelectedItem().getPerson().getPersonalnummer(),
							Date.valueOf(datePicker.getValue()));
					if(gewählterTable == tvVormittag) {
						updateTVVormittag(datePicker.getValue());
					} else {
						updateTVNachmittag(datePicker.getValue());
					}
				} catch (SQLException e1) {
					new Alert(AlertType.ERROR, e1.toString()).showAndWait();
				}
			}
		});

		//Erstellt einen Action-Listener für den "Mitarbeiter hinzufügen" Button der den DetailsDialogDienstplan öffnet wenn der Button gedrückt wird.
		maHinzufügen.setOnAction(e -> {
			Optional<ButtonType> r = new DetailsDialogDienstplan(datePicker.getValue(), this).showAndWait();
		});

		datePicker.setShowWeekNumbers(true);

		//Erstelle Boxen und Kreise für die Anzeige der Schichten "Vormittag" und "Nachmittag" und füge sie dem GridPane hinzu.
		VBox vBox1 = new VBox();
		vBox1.setStyle("-fx-background-color: lightgreen;");
		vBox1.setPrefHeight(35);
		vBox1.setPrefWidth(100);	    
		Text text = new Text("Vormittag");
		text.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox1.setAlignment(Pos.CENTER);
		vBox1.getChildren().add(text);

		VBox vBox2 = new VBox();
		vBox2.setStyle("-fx-background-color: lightgreen;");
		vBox2.setPrefHeight(35);
		vBox2.setPrefWidth(100);    
		Text text2 = new Text("Nachmittag");
		text2.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox2.setAlignment(Pos.CENTER);
		vBox2.getChildren().add(text2);

		Circle circle1 = new Circle(40, Color.AQUAMARINE);
		gp.add(circle1, 3, 1);

		Circle circle2 = new Circle(70, Color.BISQUE);
		gp.add(circle2, 4, 1);

		Circle circle3 = new Circle(40, Color.CADETBLUE);
		gp.add(circle3, 5, 1);

		Circle circle4 = new Circle(70, Color.BLUE);
		gp.add(circle4, 6, 1);

		Circle circle5 = new Circle(40, Color.CADETBLUE);
		gp.add(circle5, 7, 1);

		gp.add(vBox1, 1, 0);
		gp.add(vBox2, 2, 0);

		gp.add(datePicker, 0, 1);
		gp.add(tvVormittag, 1, 1);
		gp.add(tvNachmittag, 2, 1);

		//Fügt eine HBox mit Buttons hinzu.
		HBox hb = new HBox(10, maHinzufügen, löschen);
		hb.setPadding(new Insets(7));

		gp.add(hb, 1, 2);

		this.getDialogPane().setContent(gp);
		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(abbrechen);

	}

	//Methoden zur Rückgabe der ObservableLists für die Vormittag und Nachmittag-Schicht.
	public ObservableList<MitarbeiterFX> getMitarbeiter() {
		return mitarbeiter;
	}

	public ObservableList<MitarbeiterFX> getMitarbeiter2() {
		return mitarbeiter2;
	}


	/*
	 * • Diese Methode aktualisiert die TableView für den Vormittag im Dienstplan.
	 * • Es wird das Datum übergeben.
	 * • Zunächst wird versucht aus der Datenbank die DienstplanMitarbeiter für den gewünschten Tag zu lesen.
	 * • Dann werden die Mitarbeiter aus der Datenbank gelesen und in einer neuen Liste gespeichert.
	 * • Die Liste der Mitarbeiter wird dann als ObservableList in der TableView angezeigt.
	 * • Falls ein SQLException auftreten sollte, wird eine Fehlermeldung auf der Konsole angezeigt.
	 */
	public void updateTVVormittag(LocalDate datum) {
		try {
			ArrayList<DienstplanMitarbeiter> a = Datenbank.readDienstplanMitarbeiter(Date.valueOf(datum), true);
			ArrayList<Mitarbeiter> neu = new ArrayList<>();
			for (DienstplanMitarbeiter s : a) {
				ArrayList<Mitarbeiter> dbMA = Datenbank.readMitarbeiter(s.getMitarbeiter1());
				neu.addAll(dbMA);
			}

			mitarbeiter.clear();
			for (Mitarbeiter ma : neu) {
				mitarbeiter.add(new MitarbeiterFX(ma));
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/*
	 * • Diese Methode aktualisiert die TableView für den Nachmittag im Dienstplan.
	 * • Es wird das Datum übergeben.
	 * • Zunächst wird versucht aus der Datenbank die DienstplanMitarbeiter für den gewünschten Tag zu lesen.
	 * • Dann werden die Mitarbeiter aus der Datenbank gelesen und in einer neuen Liste gespeichert.
	 * • Die Liste der Mitarbeiter wird dann als ObservableList in der TableView angezeigt.
	 * • Falls ein SQLException auftreten sollte, wird eine Fehlermeldung auf der Konsole angezeigt.
	 */
	public void updateTVNachmittag(LocalDate datum) {
		try {
			ArrayList<DienstplanMitarbeiter> a = Datenbank.readDienstplanMitarbeiter(Date.valueOf(datum), false);
			ArrayList<Mitarbeiter> neu = new ArrayList<>();
			for (DienstplanMitarbeiter s : a) {
				ArrayList<Mitarbeiter> dbMA = Datenbank.readMitarbeiter(s.getMitarbeiter1());
				neu.addAll(dbMA);
			}

			mitarbeiter2.clear();
			for (Mitarbeiter ma : neu) {
				mitarbeiter2.add(new MitarbeiterFX(ma));
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

}