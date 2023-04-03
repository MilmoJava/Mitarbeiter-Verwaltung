
package mitarbeiterVerwaltung;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

/*
 • Die Klasse MitarbeiterPage implementiert eine JavaFX Applikation zur Verwaltung von Mitarbeitern.
 • Sie ermöglicht das anzeigen, hinzufügen, bearbeiten und entfernen von Mitarbeiterdaten sowie das Eintragen von Abwesenheiten.
 • Die Klasse beinhaltet eine TableView, Button und Dialog.
 • Die Mitarbeiter werden in einer ObservableList gespeichert und werden in der TableView angezeigt.
 • Die Buttons für bearbeiten und entfernen sind deaktiviert und werden erst bei Auswahl eines Mitarbeiters aktiviert.
 • Bei Auswahl des Buttons "Arbeitsplan" wird ein Dialogfenster zur Anzeige des Dienstplans angezeigt.
 • Bei Auswahl des Buttons "Abwesenheit eintragen" wird vorab eine Information angezeigt und danach ein Dialogfenster zum Eintragen der Abwesenheit geöffnet.
 • Bei Auswahl des Buttons "Liste der abwesenden Mitarbeiter" wird ein Dialogfenster mit der Liste der abwesenden Mitarbeiter zu einem bestimmten Tag angezeigt.
 • Bei Auswahl des Buttons "Diagramm der abwesenden Mitarbeiter" wird ein Dialogfenster mit einem Kreisdiagramm zur Anzeige der Abwesenheitsstatistik angezeigt.
 */
public class MitarbeiterPage  extends Application { 

	private ObservableList<MitarbeiterFX> mitarbeiter = FXCollections.observableArrayList();


	// Die Methode start() wird beim Starten der Anwendung aufgerufen und befüllt die Oberfläche mit Tabellenansicht, Schaltflächen.

	@Override
	public void start(Stage primaryStage) {		
		// Erstellt die Tabelle in der Datenbank wenn diese noch nicht vorhanden ist.
		try {
			Datenbank.dropAndCreateTable();
		} catch (SQLException e2) {
			e2.printStackTrace();
		}	

		// Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(10);
		gp.prefHeight(100);
		gp.minWidth(400);

		// Erstellt die Spalten für die TableView
		TableColumn<MitarbeiterFX, String> vornameCol = new TableColumn<>("Vorname");
		vornameCol.setCellValueFactory(new PropertyValueFactory<>("vorname"));
		vornameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> nachnameCol = new TableColumn<>("Nachname");
		nachnameCol.setCellValueFactory(new PropertyValueFactory<>("nachname"));
		nachnameCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> personalnummerCol = new TableColumn<>("Personalnummer");
		personalnummerCol.setCellValueFactory(new PropertyValueFactory<>("personalnummer"));
		personalnummerCol.setPrefWidth(120);
		TableColumn<MitarbeiterFX, String> geschlechtCol = new TableColumn<>("Geschlecht");
		geschlechtCol.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
		geschlechtCol.setPrefWidth(150);
		TableColumn<MitarbeiterFX, Integer> plzCol = new TableColumn<>("PLZ");
		plzCol.setCellValueFactory(new PropertyValueFactory<>("plz"));
		plzCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> ortCol = new TableColumn<>("Ort");
		ortCol.setCellValueFactory(new PropertyValueFactory<>("ort"));
		ortCol.setPrefWidth(170);
		TableColumn<MitarbeiterFX, String> strasseCol = new TableColumn<>("Strasse");
		strasseCol.setCellValueFactory(new PropertyValueFactory<>("strasse"));
		strasseCol.setPrefWidth(170);
		TableColumn<MitarbeiterFX, Integer> hausnummerCol = new TableColumn<>("Hausnummer");
		hausnummerCol.setCellValueFactory(new PropertyValueFactory<>("hausnummer"));
		hausnummerCol.setPrefWidth(100);
		TableColumn<MitarbeiterFX, String> ibanCol = new TableColumn<>("Iban");
		ibanCol.setCellValueFactory(new PropertyValueFactory<>("iban"));
		ibanCol.setPrefWidth(160);
		TableColumn<MitarbeiterFX, String> bicCol = new TableColumn<>("Bic");
		bicCol.setCellValueFactory(new PropertyValueFactory<>("bic"));
		bicCol.setPrefWidth(140);
		TableColumn<MitarbeiterFX, String> bankCol = new TableColumn<>("Bank");
		bankCol.setCellValueFactory(new PropertyValueFactory<>("bank"));
		bankCol.setPrefWidth(150);
		TableColumn<MitarbeiterFX, Integer> telefonCol = new TableColumn<>("Telefon");
		telefonCol.setCellValueFactory(new PropertyValueFactory<>("telefon"));
		telefonCol.setPrefWidth(140);
		TableColumn<MitarbeiterFX, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setPrefWidth(200);
		// Erzeuge die TableView und füge die Spalten hinzu.
		TableView<MitarbeiterFX> tvMitarbeiter = new TableView<>(mitarbeiter);
		tvMitarbeiter.getColumns().addAll(vornameCol, nachnameCol, personalnummerCol, geschlechtCol, plzCol, ortCol,
				strasseCol, hausnummerCol, ibanCol, bicCol, bankCol, telefonCol, emailCol);
		tvMitarbeiter.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		// Rufe die Methode sucheMitarbeiter() auf, um die Tabelle mit Daten zu füllen.
		sucheMitarbeiter();

		// Erzeuge die Buttons
		Button arbeitsplan = new Button("Dienstplan");
		Button neu = new Button("Mitarbeiter hinzufügen");
		Button bearbeiten = new Button("Bearbeiten");
		Button entfernen = new Button("Entfernen");
		Button abwesenheit = new Button("Abwesenheit eintragen");
		Button abwesenheitListe = new Button("Liste der abwesenden Mitarbeiter");
		Button diagrammAbwesenheit = new Button("Diagramm der abwesenden Mitarbeiter");
		
		// Deaktiviere die Buttons zum Bearbeiten und Entfernen, da zunächst kein Mitarbeiter ausgewählt ist.
		bearbeiten.setDisable(true);
		entfernen.setDisable(true);
		
		// Erzeuge die HBox und füge die Buttons hinzu.
		HBox hb = new HBox(10, arbeitsplan, neu, bearbeiten, entfernen, abwesenheit, abwesenheitListe, diagrammAbwesenheit);
		hb.setPadding(new Insets(10));

		// Erzeuge die VBox und füge die TableView und die HBox hinzu.
		VBox vb = new VBox(10, tvMitarbeiter, hb);
		vb.setPadding(new Insets(5));

		// Füge den Buttons Aktionen hinzu.
		arbeitsplan.setOnAction(e -> {
			Optional<ButtonType> r = new Stundenplan().showAndWait();
		});
		
		abwesenheit.setOnAction(e -> {
			Alert alert = new Alert(AlertType.INFORMATION, "Bevor Sie die Abwesenheit eintragen, müssen Sie den Mitarbeiter aus dem Dienstplan entfernen !");
			alert.showAndWait();
			Optional<ButtonType> r = new AbwesenheitDialog().showAndWait();

		});

		abwesenheitListe.setOnAction(e -> {
			Optional<ButtonType> r = new ListeAbwesenheit(null).showAndWait();

		});

		diagrammAbwesenheit.setOnAction(e -> {
			Optional<ButtonType> r = new StatistikAbwesenheit().showAndWait();

		});

		neu.setOnAction(e -> {
			Mitarbeiter mitarbeiter = new Mitarbeiter();
			Optional<ButtonType> r = new DetailsDialog(mitarbeiter).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				sucheMitarbeiter();
		});

		bearbeiten.setOnAction(e -> {
			Mitarbeiter mitarbeiter = tvMitarbeiter.getSelectionModel().getSelectedItem().getPerson();
			Optional<ButtonType> r = new DetailsDialog(mitarbeiter).showAndWait();
			if (r.isPresent() && r.get().getButtonData() == ButtonData.OK_DONE)
				sucheMitarbeiter();
		});

		entfernen.setOnAction(e -> {
			try {
				Datenbank.deleteMitarbeiter(tvMitarbeiter.getSelectionModel().getSelectedItem().getPerson());
				sucheMitarbeiter();
			} catch (SQLException e1) {
				new Alert(AlertType.ERROR, e1.toString()).showAndWait();
			}
		});

		// Füge Listener zum Aktivieren/Deaktivieren der Bearbeiten/Entfernen-Buttons hinzu.
		tvMitarbeiter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MitarbeiterFX>() {
			// legt die Buttons auf disable, bis ein Mitarbeiter ausgewählt wird.
			@Override
			public void changed(ObservableValue<? extends MitarbeiterFX> arg0, MitarbeiterFX arg1, MitarbeiterFX arg2) {
				if (arg2 != null) {

					bearbeiten.setDisable(false);
					entfernen.setDisable(false);
				} else {
					bearbeiten.setDisable(true);
					entfernen.setDisable(true);
				}
			}
		});

		gp.add(tvMitarbeiter, 0, 0);
		gp.add(hb, 0, 1);
		
		//Hier wird die GridPane, welches die TableView und die Buttons enthält, dem Fenster hinzugefügt und angezeigt.
		primaryStage.setScene(new Scene(gp));
		primaryStage.setTitle("Mitarbeiter Verwaltung");
		primaryStage.show();

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

	public static void main(String[] args) {
		launch(args);
	}

}
