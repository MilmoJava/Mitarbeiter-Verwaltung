package mitarbeiterVerwaltung;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/*
 * • Diese Klasse zeigt ein Dialogfenster zum hinzufügen von Mitarbeitern in den Dienstplan.
 * • Sie hat Methoden zur Anzeige einer Liste von Mitarbeitern und zum Filtern der Liste um nur verfügbare Mitarbeiter anzuzeigen die nicht Abwesend sind.
 * • Es wird auch eine ComboBox zum Auswählen der Schichten (Vormittag oder Nachmittag) und ein Button zum hinzufügen von Mitarbeitern in den Dienstplan bereitgestellt.
 */
public class DetailsDialogDienstplan extends Dialog<ButtonType> {

	//Eine ObservableList vom Typ MitarbeiterFX wird erstellt um die Daten der abwesenden Mitarbeiter anzuzeigen.
	private ObservableList<MitarbeiterFX> mitarbeiter = FXCollections.observableArrayList();
	private LocalDate time;
	
	public DetailsDialogDienstplan(LocalDate s, Stundenplan sp) {
		this.setTitle("Dienstplan einteilen");
		this.time = s;
		
		// Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(15);
		gp.prefHeight(100);
		gp.minWidth(400);

		/*
		 * • Erstellt eine Tabelle mit drei Spalten für Vorname, Nachname und Personalnummer der Mitarbeiter.
		 * • Der TableView wird mit einer ObservableList von MitarbeiterFX initialisiert und die Methode "sucheMitarbeiter" wird aufgerufen,
		 *   um die Tabelle mit den Mitarbeitern zu füllen.
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
		TableView<MitarbeiterFX> tvMitarbeiter = new TableView<>(mitarbeiter);
		tvMitarbeiter.getColumns().addAll(vornameCol, nachnameCol, personalnummerCol);
		sucheMitarbeiter();

		Button hinzufügen = new Button("Hinzufügen");	

		ObservableList<String> options = FXCollections.observableArrayList();
		ComboBox<String> comboBox = new ComboBox<>(options);
		
		/*
		 * Wenn man einen Mitarbeiter in der TableView auswählt werden alle Schichten
		 * für die dieser Mitarbeiter noch nicht im sp-Objekt des Stundenplans eingeteilt wurde als Optionen zur ComboBox hinzugefügt.
		 * Wenn man beispielsweise einen Mitarbeiter für den Vormittag einstellen möchte
		 * und dieser Mitarbeiter noch nicht für den Vormittag eingeteilt wurde, wird "Vormittag" als Option in der ComboBox angezeigt.
		 * Wenn der Mitarbeiter bereits für den Vormittag eingeteilt wurde wird diese Option nicht in der ComboBox angezeigt.
		 */
		tvMitarbeiter.setOnMouseClicked(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				MitarbeiterFX mitarbeiter = tvMitarbeiter.getSelectionModel().getSelectedItem();
				options.clear();
				if(sp.getMitarbeiter().stream().filter(m -> m.getPersonalnummer().equals(mitarbeiter.getPersonalnummer())).findFirst().isEmpty()) {
					options.add("Vormittag");
				}
				if(sp.getMitarbeiter2().stream().filter(m -> m.getPersonalnummer().equals(mitarbeiter.getPersonalnummer())).findFirst().isEmpty()) {
					options.add("Nachmittag");
				}					
			}

		});

		//Erstellt VBox mit Texten für die Mitarbeiterauswahl und die Schichtauswahl.
		VBox vBox1 = new VBox();
		vBox1.setStyle("-fx-background-color: lightgreen;");
		vBox1.setPrefHeight(35);
		vBox1.setPrefWidth(100);	    
		Text text = new Text("Mitarbeiter auswählen");
		text.setFont(Font.font(null, FontWeight.BOLD, 15));
		vBox1.setAlignment(Pos.CENTER);
		vBox1.getChildren().add(text);

		VBox vBox2 = new VBox();
		vBox2.setStyle("-fx-background-color: green;");
		vBox2.setPrefHeight(35);
		vBox2.setPrefWidth(100);    
		Text text2 = new Text("Schicht auswählen");
		text2.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox2.setAlignment(Pos.CENTER);
		vBox2.getChildren().add(text2);

		gp.add(vBox1, 0, 0);
		gp.add(vBox2, 1, 0);
		gp.add(tvMitarbeiter, 0, 1);
		HBox hb = new HBox(10, comboBox, hinzufügen);
		hb.setPadding(new Insets(7));
		gp.add(hb, 1, 1);

		/*
		 * ActionListener beim klick auf "Hinzufügen": Wenn eine Person aus der TableView ausgewählt wurde und eine Schicht aus der ComboBox ausgewählt wurde,
		 * wird ein neues DienstplanMitarbeiter-Objekt erstellt und in die Datenbank eingefügt.
		 * Wenn die Schicht "Vormittag" ausgewählt wurde, wird die updateTVVormittag-Methode aufgerufen um die TableView des Stundenplans zu aktualisieren,
		 * und die Schicht "Vormittag" wird aus der ComboBox entfernt.
		 * Wenn die Schicht "Nachmittag" ausgewählt wurde, wird die updateTVNachmittag-Methode aufgerufen um die TableView des Stundenplans zu aktualisieren,
		 * und die Schicht "Nachmittag" wird aus der ComboBox entfernt.
		 */
		hinzufügen.setOnAction(event -> {
			for(MitarbeiterFX mb : tvMitarbeiter.getSelectionModel().getSelectedItems()) {	
				if(comboBox.getSelectionModel().getSelectedItem() == null)
					return;
				DienstplanMitarbeiter  dpms = new DienstplanMitarbeiter(Date.valueOf(s) , mb.getPersonalnummer(), true);
				if( comboBox.getSelectionModel().getSelectedItem().equals("Vormittag")) {
					dpms =  new DienstplanMitarbeiter(Date.valueOf(s) , mb.getPersonalnummer(), true);
					try {
						Datenbank.insertDienstplanMitarbeiter(dpms,true);
						sp.updateTVVormittag(s);
						comboBox.getItems().remove("Vormittag");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
				else if( comboBox.getSelectionModel().getSelectedItem().equals("Nachmittag")) {
					dpms =  new DienstplanMitarbeiter(Date.valueOf(s) , mb.getPersonalnummer(), false);
					try {
						Datenbank.insertDienstplanMitarbeiter(dpms,false);
						sp.updateTVNachmittag(s);
						comboBox.getItems().remove("Nachmittag");
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}				
			}
		});

		this.getDialogPane().setContent(gp);

		ButtonType abbrechen = new ButtonType("Schließen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(abbrechen);
	}


	/*
	Diese Methode liest alle Mitarbeiter aus der Datenbank, welche noch nicht in der Abwesenheitsliste sind und fügt sie der Tabelle hinzu.
	Wenn ein Fehler auftritt, wird eine Fehlermeldung angezeigt.
	 */
	private void sucheMitarbeiter() {
		try {
			ArrayList<Mitarbeiter> alMitarbeiter = Datenbank.readMitarbeiter();
			mitarbeiter.clear();

			for (Mitarbeiter einMa : alMitarbeiter)
				if(Datenbank.isNotAbwesend(Date.valueOf(time), einMa.getPersonalnummer()))//Filtert mit dieser Methode nach Mitarbeitern die nicht Abwesend sind.
					mitarbeiter.add(new MitarbeiterFX(einMa));

		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	};

}
