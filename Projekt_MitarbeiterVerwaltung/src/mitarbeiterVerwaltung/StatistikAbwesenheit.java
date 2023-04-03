package mitarbeiterVerwaltung;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/*
 * • Diese Klasse ist ein Dialog der ein PieChart-Diagramm anzeigt,
 *   das die Abwesenheitsstatistik der Mitarbeiter mit dem Grund und den Abwesenheitstagen in einem bestimmten Zeitraum anzeigt.
 * • Der Benutzer kann einen Zeitraum und den Abwesenheitsgrund auswählen um das Diagramm zu aktualisieren.
 * • Die Methode "updateChart" liest die Abwesenheitsdaten aus der Datenbank und speichert die Anzahl der Abwesenheitstage pro Mitarbeiter in einer HashMap
 *   und aktualisiert dann das PieChart-Objekt mit den Daten aus der HashMap.
 */
public class StatistikAbwesenheit extends Dialog<ButtonType> {

	public StatistikAbwesenheit() {

		this.setTitle("Liste der abwesenden Mitarbeiter");
		this.getDialogPane().setPrefSize(700, 700);

		// Erstelle eine GridPane und setze Padding, Vgap, Hgap, prefHeight und minWidth-Eigenschaften.
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(10, 10, 10, 10));
		gp.setVgap(8);
		gp.setHgap(55);
		gp.prefHeight(100);
		gp.minWidth(400);

		//Erstellt ein DatePicker-Objekt und setzt den Wert auf das aktuelle Datum.
		DatePicker datePickerVon = new DatePicker();
		datePickerVon.setValue(LocalDate.now());

		//Erstellt ein DatePicker-Objekt und setzt den Wert auf das aktuelle Datum.
		DatePicker datePickerBis = new DatePicker();
		datePickerBis.setValue(LocalDate.now());

		//Erstellt eine ComboBox mit vorgegebenen Optionen.
		ComboBox<String> options = new ComboBox<>();
		options.getItems().addAll("Krank", "Urlaub", "Schulung", "Familiäres Ereigniss", "Pflege naher Angehöriger", "Umzug", "Sonstige");
		options.setValue("Krank");

		//Hier wird ein neues PieChart-Objekt erstellt. Dann wird die Methode "updateChart" aufgerufen um das Diagramm mit den aktuellen Daten zu füllen.
		PieChart chart = new PieChart();
		chart.setTitle("Abwesenheitsstatistik");
		updateChart(datePickerVon.getValue(), datePickerBis.getValue(), options.getValue(), chart);

		//Erstellt VBox mit Texten für das Datum und die Grundangabe.
		VBox vBox1 = new VBox();
		vBox1.setStyle("-fx-background-color: lightgreen;");
		vBox1.setPrefHeight(35);
		vBox1.setPrefWidth(100);	    
		Text text = new Text("Datum von");
		text.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox1.setAlignment(Pos.CENTER);
		vBox1.getChildren().add(text);

		VBox vBox2 = new VBox();
		vBox2.setStyle("-fx-background-color: limegreen;");
		vBox2.setPrefHeight(35);
		vBox2.setPrefWidth(100);	    
		Text text2 = new Text("bis");
		text2.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox2.setAlignment(Pos.CENTER);
		vBox2.getChildren().add(text2);
		
		VBox vBox3 = new VBox();
		vBox3.setStyle("-fx-background-color: green;");
		vBox3.setPrefHeight(35);
		vBox3.setPrefWidth(100);	    
		Text text3 = new Text("Optionen");
		text3.setFont(Font.font(null, FontWeight.BOLD, 18));
		vBox3.setAlignment(Pos.CENTER);
		vBox3.getChildren().add(text3);
		
		//Fügt die Labels, DatePicker, ComboBox und den Chart zur GridPane hinzu.
		gp.add(vBox1, 0, 0);
		gp.add(datePickerVon, 0, 1);
		gp.add(vBox2, 1, 0);
		gp.add(datePickerBis, 1, 1);
		gp.add(vBox3, 2, 0);
		gp.add(options, 2, 1);
		gp.add(chart, 0, 2, 4, 1);

		/*
		 * Dieser Event wird ausgelöst, wenn man das Datum im ändert.
		 * Dabei werden die Werte von "datePickerVon", "datePickerBis" und "options" ausgelesen und an die Methode "updateChart" übergeben
		 * um den Chart auf den neusten Stand zu setzten.
		 */
		datePickerVon.setOnAction(event -> {
			LocalDate dateVon = datePickerVon.getValue();
			LocalDate dateBis = datePickerBis.getValue();
			String grundOption = options.getValue();
			updateChart(dateVon, dateBis, grundOption, chart);
		});

		/*
		 * Dieser Event wird ausgelöst, wenn man das Datum im ändert.
		 * Dabei werden die Werte von "datePickerVon", "datePickerBis" und "options" ausgelesen und an die Methode "updateChart" übergeben
		 * um den Chart auf den neusten Stand zu setzten.
		 */
		datePickerBis.setOnAction(event -> {
			LocalDate dateVon = datePickerVon.getValue();
			LocalDate dateBis = datePickerBis.getValue();
			String grundOption = options.getValue();
			updateChart(dateVon, dateBis, grundOption, chart);
		});

		/*
		 * Dieser Event wird ausgelöst wenn eine neue Option in der ComboBox ausgewählt wird.
		 * Die Methode "updateChart" wird mit den Werten der DatePickers und der ausgewählten Option
		 * und dem PieChart aufgerufen um das Diagramm zu aktualisieren.
		 */
		options.setOnAction(event -> {
			LocalDate dateVon = datePickerVon.getValue();
			LocalDate dateBis = datePickerBis.getValue();
			String grundOption = options.getValue();
			updateChart(dateVon, dateBis, grundOption, chart);
		});

		ButtonType abbrechen = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
		this.getDialogPane().getButtonTypes().addAll(abbrechen);
		this.getDialogPane().setContent(gp);
	}

	/**
	 * • Diese Methode aktualisiert das PieChart-Objekt das die Abwesenheitsstatistik anzeigt.
	 * • Dazu werden die Abwesenheitsdaten aus der Datenbank gelesen und die Anzahl der Abwesenheitstage für jeden  Mitarbeiter
	 *   in eine HashMap gespeichert. Mitarbeiter die nicht mehr in der Datenbank vorhanden sind werden aus der HashMap entfernt.
	 * • Falls es einen SQLException gibt wird eine  Fehlermeldung angezeiigt.
	 * • @param dateVon, Startdatum.
	 * • @param dateBis, Enddatum.
	 * • @param grundOption, der Grund der Abwesenheit der angezeigt werden soll.
	 * • @param chart, das PieChart-Objekt welches aktualisiert werden soll.
	 */
	public void updateChart(LocalDate dateVon, LocalDate dateBis, String grundOption, PieChart chart) {
		try {
			chart.setPrefSize(700, 500);
			ArrayList<Abwesenheit> abwesenheiten = Datenbank.readAbwesenheitStatistik(Date.valueOf(dateVon), Date.valueOf(dateBis), grundOption);

			HashMap<String, Integer> countVonMA = new HashMap<>(); // die Anzahl der Abwesenheitstage pro Mitarbeiter wird gespeichert
			for (Abwesenheit abwesenheit : abwesenheiten) {
				String mitarbeiter = abwesenheit.getMitarbeiter1();
				if (!countVonMA.containsKey(mitarbeiter)) { // prüft, ob der Mitarbeiter bereits als Schlüssel in der HashMap vorhanden ist, wenn nicht ->
					countVonMA.put(mitarbeiter, 0); // wird einmal jeder MA in die HashMap hinzugefügt und hat anfangs den Wert 0
				}
				countVonMA.put(mitarbeiter, countVonMA.get(mitarbeiter) + 1);
			}

			// Entferne den Mitarbeiter aus der HashMap, wenn er nicht mehr in der Datenbank vorhanden ist
			ArrayList<Mitarbeiter> alleMitarbeiter = Datenbank.readMitarbeiter();
			countVonMA.keySet().removeIf(pn -> alleMitarbeiter.stream().noneMatch(m -> m.getPersonalnummer().equals(pn)));

			ObservableList<PieChart.Data> pieChart = FXCollections.observableArrayList();
			for (Map.Entry<String, Integer> entry : countVonMA.entrySet()) { // das entry Objekt bekommt aus der HashMap den Vornamen und die Abwesenheitstage
				String pn = entry.getKey();
				int zähler = entry.getValue();
				String vorname = "";
				for (Mitarbeiter mitarbeiter : alleMitarbeiter) {
					if (mitarbeiter.getPersonalnummer().equals(pn)) { // wenn die Personalnummer die selbe wie bei dem entry ist  wird es auf name und vorname übertragen
						vorname = mitarbeiter.getVorname();
					}
				}
				pieChart.add(new PieChart.Data(vorname, zähler));
				int index = pieChart.size() - 1;
				pieChart.get(index).setName(pn + " " + vorname + " (" + zähler + ")");
			}

			chart.setData(pieChart);

		} catch (SQLException e) {
			new Alert(AlertType.ERROR, e.toString()).showAndWait();
		}
	}

}
