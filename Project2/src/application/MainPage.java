package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MainPage extends Pane {
	private Button customer, admin, loadCars, saveInfo, save;
	private Label warning1, warning2;
	private TextField nametxf, mobiletxf;

	MainPage() {

		// initializing
		customer = new Button("Customer");
		admin = new Button("Administrator");
		loadCars = new Button("Load cars");
		saveInfo = new Button("Save to files");
		save = new Button("Save");
		warning1 = new Label("");
		warning2 = new Label("");
		nametxf = new TextField();
		mobiletxf = new TextField();

		setProperties();// setting properties for all of the elements

		Label heading = new Label("Car DealerShip");
		heading.setFont(Font.font("Elephant", FontWeight.BOLD, 55));
		heading.setLayoutX(95);
		heading.setLayoutY(65);

		ImageView img = new ImageView("front.png");
		img.setScaleX(1.17);
		img.setScaleY(1.23);
		img.setX(60);
		img.setY(45);

		HBox hb1 = new HBox(20);
		hb1.setAlignment(Pos.TOP_CENTER);
		hb1.getChildren().addAll(customer, admin);

		HBox hb2 = new HBox(20);
		hb2.setAlignment(Pos.TOP_CENTER);
		hb2.getChildren().addAll(loadCars, saveInfo);

		VBox vb = new VBox(20);
		vb.setMargin(hb1, new Insets(20));
		vb.getChildren().addAll(hb1, hb2);
		vb.setAlignment(Pos.TOP_CENTER);
		vb.setLayoutX(95);
		vb.setLayoutY(190);

		getChildren().addAll(img, heading, vb, warning1);

	}

	public VBox getEditInfoPane() {

		Label infolbl = new Label("My Information: ");
		Label namelbl = new Label("Name:");
		Label mobilelbl = new Label("Mobile:");

		namelbl.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
		mobilelbl.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
		infolbl.setFont(Font.font("Elephant", FontWeight.BOLD, 30));

		GridPane gp = new GridPane();
		gp.add(namelbl, 0, 0);
		gp.add(mobilelbl, 0, 1);
		gp.add(nametxf, 1, 0);
		gp.add(mobiletxf, 1, 1);
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		gp.setVgap(15);

		VBox root = new VBox(50);
		root.setStyle("-fx-background-color: rgb(255, 204, 213);");
		root.setAlignment(Pos.TOP_CENTER);
		root.setPadding(new Insets(90, 0, 10, 0));
		root.getChildren().addAll(infolbl, gp, save, warning2);

		return root;
	}

	private void setProperties() {// setting properties for all of the elements

		nametxf.setAlignment(Pos.CENTER);
		mobiletxf.setAlignment(Pos.CENTER);

		warning1.setTranslateX(590);
		warning1.setTranslateY(30);
		warning1.setFont(Font.font("Courier New", FontWeight.BOLD, 25));
		warning1.setTextFill(Color.RED);

		customer.setPrefWidth(170);
		customer.setPrefHeight(40);
		admin.setPrefWidth(170);
		admin.setPrefHeight(40);
		loadCars.setPrefWidth(150);
		loadCars.setPrefHeight(25);
		saveInfo.setPrefWidth(150);
		saveInfo.setPrefHeight(25);
		save.setPrefWidth(80);
		save.setPrefHeight(30);
		mobiletxf.setPrefWidth(190);
		mobiletxf.setPrefHeight(35);
		nametxf.setPrefWidth(190);
		nametxf.setPrefHeight(35);

		customer.setStyle("-fx-background-color: rgb(206, 81, 81);-fx-text-fill:white;-fx-font-size:23;");
		admin.setStyle("-fx-background-color: rgb(206, 81, 81);-fx-text-fill:white;-fx-font-size:23;");
		loadCars.setStyle("-fx-background-color: rgb(206, 81, 81);-fx-text-fill:white;-fx-font-size:17;");
		saveInfo.setStyle("-fx-background-color: rgb(206, 81, 81);-fx-text-fill:white;-fx-font-size:17;");
		save.setStyle(
				"-fx-background-color: rgb(206, 81, 81);-fx-text-fill:white;-fx-font-size:17;-fx-border-color: black;");

		nametxf.setStyle(
				"-fx-border-style: solid;-fx-border-color: black;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		mobiletxf.setStyle(
				"-fx-border-style: solid;-fx-border-color: black;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");

		warning2.setFont(Font.font("Courier New", FontWeight.BOLD, 22));

	}

	public Button getCustomer() {
		return customer;
	}

	public Button getAdmin() {
		return admin;
	}

	public Button getLoadCars() {
		return loadCars;
	}

	public Label getWarning1() {
		return warning1;
	}

	public Button getSaveInfo() {
		return saveInfo;
	}

	public Button getSave() {
		return save;
	}

	public TextField getNametxf() {
		return nametxf;
	}

	public TextField getMobiletxf() {
		return mobiletxf;
	}

	public Label getWarning2() {
		return warning2;
	}

}
