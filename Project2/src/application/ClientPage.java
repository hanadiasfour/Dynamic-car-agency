package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClientPage extends TabPane {

	private TableView<Car> carsTable;
	private Button next, prev, searchCar, openfilters, addToCart, apply, editInfo, removeFilters, deleteB, updateB,
			addNewB, deleteC, updateC, addNewC, upB, upC, addB, addC, cancelB, cancelC;
	private TextField cartxf, brandtxf, modeltxf, colortxf, yeartxf, pricetxf;
	private Label warning, BrandHeading, CarHeading, brandWarning, carWarning;
	private ComboBox<Brand> brandBox;
	private ComboBox<String> modelBox, colorBox, yearBox, budgetBox;
	private Brand brand;
	private Car car;
	private Customer customer;
	private Tab client;
	private GridPane adminEditingButtons = new GridPane();
	private HBox clientButtons = new HBox(20);

	ClientPage() {

		ImageView img = new ImageView("arrow.png");
		img.setRotate(180);

		// initializing
		next = new Button("", new ImageView("arrow.png"));
		prev = new Button("", img);
		deleteB = new Button("Delete Brand");
		updateB = new Button("Update Brand");
		addB = new Button("Add");
		addNewB = new Button("Add new Brand");
		upB = new Button("Update");
		deleteC = new Button("Delete Car");
		updateC = new Button("Update Car");
		upC = new Button("Update");
		addNewC = new Button("Add new Car");
		addC = new Button("Add");
		cancelB = new Button("Cancel");
		cancelC = new Button("Cancel");
		searchCar = new Button("Search");
		openfilters = new Button("Add filters");
		addToCart = new Button("Add to cart");
		apply = new Button("Apply");
		editInfo = new Button("Edit my info");
		removeFilters = new Button("Remove All Filters");
		carsTable = new TableView<Car>();
		brandBox = new ComboBox<Brand>();
		modelBox = new ComboBox<String>();
		colorBox = new ComboBox<String>();
		yearBox = new ComboBox<String>();
		budgetBox = new ComboBox<String>();
		warning = new Label("");
		brandWarning = new Label("");
		carWarning = new Label("");
		cartxf = new TextField();
		modeltxf = new TextField();
		colortxf = new TextField();
		yeartxf = new TextField();
		pricetxf = new TextField();
		brandtxf = new TextField();

		setProperties();

		// admin editing buttons
		adminEditingButtons.setAlignment(Pos.CENTER);
		adminEditingButtons.setHgap(10);
		adminEditingButtons.setVgap(10);
		adminEditingButtons.add(addNewB, 0, 0);
		adminEditingButtons.add(updateB, 1, 0);
		adminEditingButtons.add(deleteB, 2, 0);
		adminEditingButtons.add(addNewC, 0, 1);
		adminEditingButtons.add(updateC, 1, 1);
		adminEditingButtons.add(deleteC, 2, 1);

		// setting top
		HBox top = new HBox(50);
		top.setAlignment(Pos.CENTER);
		top.getChildren().addAll(prev, brandBox, next);

		// client buttons
		clientButtons.getChildren().addAll(openfilters, addToCart, editInfo);
		clientButtons.setAlignment(Pos.CENTER);

		// adding search fields to gridPane
		GridPane searching = new GridPane();
		searching.setAlignment(Pos.CENTER);
		searching.setHgap(10);
		searching.setVgap(15);

		searching.add(cartxf, 0, 1);
		searching.add(searchCar, 1, 1);

		VBox root = new VBox(10);
		root.setAlignment(Pos.TOP_CENTER);
		root.setPadding(new Insets(20, 10, 20, 10));
		root.setStyle("-fx-background-color: rgb(206, 81, 81);");
		root.getChildren().addAll(top, carsTable, clientButtons, adminEditingButtons, searching, warning);

		client = new Tab("Client", root);
		getTabs().add(client);
		client.setStyle("-fx-background-color: rgb(241, 177, 188);");

	}

	public BorderPane getfilterPage() {

		BorderPane root = new BorderPane();

		GridPane middle = new GridPane();

		Label filterslbl = new Label("Filters: ");
		Label modellbl = new Label("Model:");
		Label colorlbl = new Label("Color:");
		Label yearlbl = new Label("Year:");
		Label pricelbl = new Label("Budget:");

		filterslbl.setFont(Font.font("Elephant", FontWeight.BOLD, 30));
		modellbl.setFont(Font.font("Courier New", FontWeight.BOLD, 23));
		colorlbl.setFont(Font.font("Courier New", FontWeight.BOLD, 23));
		yearlbl.setFont(Font.font("Courier New", FontWeight.BOLD, 23));
		pricelbl.setFont(Font.font("Courier New", FontWeight.BOLD, 23));

		middle.add(modellbl, 0, 1);
		middle.add(colorlbl, 0, 2);
		middle.add(yearlbl, 0, 3);
		middle.add(pricelbl, 0, 4);

		middle.add(modelBox, 1, 1);
		middle.add(colorBox, 1, 2);
		middle.add(yearBox, 1, 3);
		middle.add(budgetBox, 1, 4);
		middle.setHgap(17);
		middle.setVgap(20);
		middle.setAlignment(Pos.CENTER);

		HBox buttons = new HBox(20);
		buttons.getChildren().addAll(apply, removeFilters);
		buttons.setAlignment(Pos.TOP_CENTER);

		root.setPadding(new Insets(25, 20, 25, 20));
		root.setTop(filterslbl);
		root.setCenter(middle);
		root.setBottom(buttons);
		root.setAlignment(filterslbl, Pos.CENTER);

		root.setStyle("-fx-background-color: rgb(255, 204, 213);");

		return root;

	}

	public BorderPane getBrandEditor() {

		BrandHeading = new Label("heading");
		BrandHeading.setFont(Font.font("Elephant", FontWeight.BOLD, 27));

		GridPane buttons = new GridPane();
		buttons.setAlignment(Pos.CENTER);
		buttons.setHgap(30);
		buttons.add(addB, 0, 0);
		buttons.add(upB, 2, 0);
		buttons.add(cancelB, 1, 0);

		VBox vb = new VBox(20);
		vb.setPadding(new Insets(60, 10, 10, 10));
		vb.setAlignment(Pos.TOP_CENTER);
		vb.getChildren().addAll(brandtxf, buttons, brandWarning);

		BorderPane root = new BorderPane();
		root.setAlignment(BrandHeading, Pos.CENTER);
		root.setAlignment(vb, Pos.BOTTOM_CENTER);
		root.setPadding(new Insets(60, 20, 20, 20));
		root.setStyle("-fx-background-color: rgb(255, 204, 213);");

		root.setTop(BrandHeading);
		root.setCenter(vb);
		return root;

	}

	public VBox getCarEditor() {

		VBox editor = new VBox(30);

		Label modellbl = new Label("Model:");
		Label colorlbl = new Label("Color:");
		Label yearlbl = new Label("Year:");
		Label pricelbl = new Label("Price:");
		CarHeading = new Label("Al-3inwan");
		CarHeading.setFont(Font.font("ELEPHANT", FontWeight.BOLD, 30));

		modellbl.setFont(Font.font("ELEPHANT", 22));
		colorlbl.setFont(Font.font("ELEPHANT", 22));
		yearlbl.setFont(Font.font("ELEPHANT", 22));
		pricelbl.setFont(Font.font("ELEPHANT", 22));

		GridPane gp = new GridPane();

		gp.add(modellbl, 0, 1);
		gp.add(colorlbl, 0, 2);
		gp.add(yearlbl, 0, 3);
		gp.add(pricelbl, 0, 4);

		gp.add(modeltxf, 1, 1);
		gp.add(colortxf, 1, 2);
		gp.add(yeartxf, 1, 3);
		gp.add(pricetxf, 1, 4);

		gp.setHgap(15);
		gp.setVgap(20);
		gp.setAlignment(Pos.CENTER);

		gp.add(upC, 0, 5);
		gp.add(addC, 1, 5);
		gp.add(cancelC, 2, 5);
		gp.setPadding(new Insets(0, 0, 0, 20));

		editor.getChildren().addAll(CarHeading, gp, carWarning);
		editor.setStyle("-fx-background-color: rgb(255, 204, 213);");
		editor.setAlignment(Pos.CENTER);

		return editor;
	}

	private void setProperties() {

		warning.setStyle("-fx-text-fill:white;");
		warning.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		warning.setPadding(new Insets(15));

		brandWarning.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		brandWarning.setPadding(new Insets(15));

		carWarning.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
		carWarning.setPadding(new Insets(15));

		// setting button sizes
		next.setPrefWidth(50);
		next.setPrefHeight(30);
		prev.setPrefWidth(50);
		prev.setPrefHeight(30);
		apply.setPrefWidth(70);
		apply.setPrefHeight(35);
		removeFilters.setPrefWidth(160);
		removeFilters.setPrefHeight(35);
		openfilters.setPrefWidth(120);
		openfilters.setPrefHeight(60);
		editInfo.setPrefWidth(120);
		editInfo.setPrefHeight(60);
		addToCart.setPrefWidth(120);
		addToCart.setPrefHeight(60);
		searchCar.setPrefWidth(100);
		searchCar.setPrefHeight(35);
		upC.setPrefWidth(100);
		upC.setPrefHeight(30);
		addB.setPrefWidth(70);
		addB.setPrefHeight(30);
		cancelC.setPrefWidth(70);
		cancelC.setPrefHeight(30);
		upB.setPrefWidth(100);
		upB.setPrefHeight(30);
		addC.setPrefWidth(70);
		addC.setPrefHeight(30);
		cancelB.setPrefWidth(70);
		cancelB.setPrefHeight(30);
		deleteB.setPrefWidth(120);
		deleteB.setPrefHeight(60);
		deleteC.setPrefWidth(120);
		deleteC.setPrefHeight(60);
		updateB.setPrefWidth(120);
		updateB.setPrefHeight(60);
		updateC.setPrefWidth(120);
		updateC.setPrefHeight(60);
		addNewB.setPrefWidth(125);
		addNewC.setPrefHeight(60);
		addNewC.setPrefWidth(125);
		addNewC.setPrefHeight(60);
		carsTable.setPrefWidth(550);
		carsTable.setMinHeight(200);

		modeltxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");
		yeartxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");
		pricetxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");
		colortxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");

		addNewC.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		addNewB.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		updateC.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		updateB.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		deleteB.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		deleteC.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");

		openfilters.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");
		addToCart.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");
		editInfo.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 2;-fx-font-size:15;");
		carsTable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(241, 177, 188);-fx-control-inner-background: rgb(241, 177, 188);");
		searchCar.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;");
		apply.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:17;-fx-text-fill:white;");
		removeFilters.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:17;-fx-text-fill:white;");
		cancelB.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");
		addB.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");
		upB.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");

		cancelC.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");
		addC.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");
		upC.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-border-color:black;-fx-border-width: 1;-fx-font-size:15;-fx-text-fill:white;");

		brandBox.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		brandBox.setPrefWidth(150);
		modelBox.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		modelBox.setPrefWidth(150);
		colorBox.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		colorBox.setPrefWidth(150);
		yearBox.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		yearBox.setPrefWidth(150);
		budgetBox.setStyle(
				"-fx-border-style: solid;-fx-border-color: bLack;-fx-border-width: 2;-fx-background-color: rgb(241, 177, 188);-fx-font-size:15;");
		budgetBox.setPrefWidth(150);

		cartxf.setAlignment(Pos.CENTER);
		cartxf.setMinWidth(150);
		cartxf.setMinHeight(35);
		cartxf.setFont(Font.font(15));
		cartxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");

		brandtxf.setAlignment(Pos.CENTER);
		brandtxf.setMinWidth(150);
		brandtxf.setMinHeight(35);
		brandtxf.setFont(Font.font(15));
		brandtxf.setStyle("-fx-background-color: rgb(241, 177, 188);-fx-border-color:black;-fx-border-width:3;");

		prev.setScaleX(1.5);
		prev.setScaleY(1.5);
		next.setScaleX(1.5);
		next.setScaleY(1.5);
		next.setStyle("-fx-background-color: transparent;");
		prev.setStyle("-fx-background-color: transparent;");

	}

	public void changeName(String s) {

		client.setText(s);

	}

	public TableView<Car> getCarsTable() {
		return carsTable;
	}

	public Button getNext() {
		return next;
	}

	public Button getPrev() {
		return prev;
	}

	public Button getSearchCar() {
		return searchCar;
	}

	public Button getOpenfilters() {
		return openfilters;
	}

	public Button getAddToCart() {
		return addToCart;
	}

	public Button getApply() {
		return apply;
	}

	public Button getEditInfo() {
		return editInfo;
	}

	public TextField getCartxf() {
		return cartxf;
	}

	public Label getWarning() {
		return warning;
	}

	public ComboBox<Brand> getBrandBox() {
		return brandBox;
	}

	public ComboBox<String> getModelBox() {
		return modelBox;
	}

	public ComboBox<String> getColorBox() {
		return colorBox;
	}

	public ComboBox<String> getYearBox() {
		return yearBox;
	}

	public ComboBox<String> getBudgetBox() {
		return budgetBox;
	}

	public Brand getBrand() {
		return brand;
	}

	public Car getCar() {
		return car;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Button getRemoveFilters() {
		return removeFilters;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Button getDeleteB() {
		return deleteB;
	}

	public Button getUpdateB() {
		return updateB;
	}

	public Button getAddNewB() {
		return addNewB;
	}

	public Button getDeleteC() {
		return deleteC;
	}

	public Button getUpdateC() {
		return updateC;
	}

	public Button getAddNewC() {
		return addNewC;
	}

	public Button getUpB() {
		return upB;
	}

	public Button getUpC() {
		return upC;
	}

	public Button getAddB() {
		return addB;
	}

	public Button getAddC() {
		return addC;
	}

	public Button getCancelB() {
		return cancelB;
	}

	public Button getCancelC() {
		return cancelC;
	}

	public TextField getBrandtxf() {
		return brandtxf;
	}

	public TextField getModeltxf() {
		return modeltxf;
	}

	public TextField getColortxf() {
		return colortxf;
	}

	public TextField getYeartxf() {
		return yeartxf;
	}

	public TextField getPricetxf() {
		return pricetxf;
	}

	public Label getBrandHeading() {
		return BrandHeading;
	}

	public Label getCarHeading() {
		return CarHeading;
	}

	public Label getBrandWarning() {
		return brandWarning;
	}

	public Label getCarWarning() {
		return carWarning;
	}

	public GridPane getAdminEditingButtons() {
		return adminEditingButtons;
	}

	public HBox getClientButtons() {
		return clientButtons;
	}

}
