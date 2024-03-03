package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminPage extends TabPane {

	private Button loadOrders, navBrands, sell, cancel, toBack, last10, viewAll;
	private Label warning;
	private TableView<Order> finishedOrdersTable, inProcessOrdersTable;
	private TableView<Statistics> statTable;
	private Tab adminTab, statTab;
	private Order order;

	AdminPage() {

		loadOrders = new Button("Load Orders");
		navBrands = new Button("Navigate Brands");
		sell = new Button("Accept Order");
		cancel = new Button("Deny Order");
		toBack = new Button("Move to Bottom");
		last10 = new Button("Last 10 sold Cars");
		viewAll = new Button("All sold Cars");
		warning = new Label();
		statTable = new TableView<Statistics>();

		statTable.setPrefHeight(500);
		toBack.setDisable(true);
		cancel.setDisable(true);

		finishedOrdersTable = new TableView<Order>();
		inProcessOrdersTable = new TableView<Order>();
		Label leftTitle = new Label("In Process Orders :");
		Label rightTitle = new Label("Finished Orders :");

		rightTitle.setFont(Font.font("Elephant", FontWeight.BOLD, 31));
		leftTitle.setFont(Font.font("Elephant", FontWeight.BOLD, 31));
		setProperties();// setting some properties

		// MIDDLE
		// left Pane
		HBox leftButtons = new HBox(15);
		leftButtons.setAlignment(Pos.CENTER);
		leftButtons.getChildren().addAll(sell, cancel, toBack);

		VBox leftPane = new VBox(30);
		leftPane.setPadding(new Insets(15));
		leftPane.setAlignment(Pos.CENTER);
		leftPane.getChildren().addAll(leftTitle, inProcessOrdersTable, leftButtons);
		leftPane.setStyle("-fx-background-color: rgb(255, 204, 213);");

		// rightPane
		HBox rightButtons = new HBox(20);
		rightButtons.setAlignment(Pos.CENTER);
		rightButtons.getChildren().addAll(last10, viewAll);

		VBox rightPane = new VBox(30);
		rightPane.setPadding(new Insets(15));
		rightPane.setAlignment(Pos.CENTER);
		rightPane.getChildren().addAll(rightTitle, finishedOrdersTable, rightButtons);
		rightPane.setStyle("-fx-background-color: rgb(255, 204, 213);");

		SplitPane middle = new SplitPane();
		middle.getItems().addAll(leftPane, rightPane);
		middle.setDividerPositions(0.5);

		// BOTTOM
		HBox buttons = new HBox(50);
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(loadOrders, navBrands);

		VBox bottom = new VBox(20);
		bottom.getChildren().addAll(buttons, warning);
		bottom.setAlignment(Pos.CENTER);

		BorderPane root = new BorderPane();

		root.setTop(middle);
		root.setCenter(bottom);
		root.setStyle("-fx-background-color: rgb(207, 90, 81);");

		adminTab = new Tab("Admin", root);
		statTab = new Tab("Reports", getStatPage());

		adminTab.setStyle("-fx-background-color: rgb(241, 177, 188);");
		statTab.setStyle("-fx-background-color: rgb(241, 177, 188);");
		getTabs().addAll(adminTab, statTab);

	}

	public VBox getStatPage() {

		Label header = new Label("GENERAL STATISTICS");
		header.setFont(Font.font("Elephant", FontWeight.BOLD, 30));
		header.setTextFill(Color.WHITE);

		VBox vb = new VBox(20);
		vb.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(10, 10, 10, 10));
		vb.setStyle("-fx-background-color: rgb(207, 90, 81);");
		vb.getChildren().addAll(header, statTable);

		return vb;

	}

	private void setProperties() {

		cancel.setDisable(true);
		toBack.setDisable(true);
		sell.setDisable(true);
		last10.setDisable(true);
		viewAll.setDisable(true);

		loadOrders.setPrefWidth(150);
		loadOrders.setPrefHeight(25);
		navBrands.setPrefWidth(150);
		navBrands.setPrefHeight(25);
		sell.setPrefWidth(150);
		sell.setPrefHeight(35);
		cancel.setPrefWidth(150);
		cancel.setPrefHeight(35);
		toBack.setPrefWidth(150);
		toBack.setPrefHeight(35);
		last10.setPrefWidth(150);
		last10.setPrefHeight(35);
		viewAll.setPrefWidth(150);
		viewAll.setPrefHeight(35);

		loadOrders.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-text-fill:black;-fx-font-size:17;-fx-border-Width:1;-fx-border-color:black;");
		navBrands.setStyle(
				"-fx-background-color: rgb(241, 177, 188);-fx-text-fill:black;-fx-font-size:17;-fx-border-Width:1;-fx-border-color:black;");
		sell.setStyle(
				"-fx-background-color: rgb(206, 90, 81);-fx-text-fill:white;-fx-font-size:15;-fx-border-Width:1;-fx-border-color:black;");
		cancel.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-text-fill:white;-fx-font-size:15;-fx-border-Width:1;-fx-border-color:black;");
		toBack.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-text-fill:white;-fx-font-size:15;-fx-border-Width:1;-fx-border-color:black;");
		last10.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-text-fill:white;-fx-font-size:15;-fx-border-Width:1;-fx-border-color:black;");
		viewAll.setStyle(
				"-fx-background-color: rgb(207, 90, 81);-fx-text-fill:white;-fx-font-size:15;-fx-border-Width:1;-fx-border-color:black;");

		warning.setFont(Font.font("Courier New", FontWeight.BOLD, 26));
		warning.setAlignment(Pos.CENTER);
		warning.setTextFill(Color.WHITE);

		finishedOrdersTable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(241, 177, 188);-fx-control-inner-background: rgb(241, 177, 188);");
		inProcessOrdersTable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(241, 177, 188);-fx-control-inner-background: rgb(241, 177, 188);");
		statTable.setStyle(
				"-fx-border-color:black;-fx-border-Width:3;-fx-background-color:rgb(241, 177, 188);-fx-control-inner-background: rgb(241, 177, 188);");

	}

	public Button getLoadOrders() {
		return loadOrders;
	}

	public Button getNavBrands() {
		return navBrands;
	}

	public Button getSell() {
		return sell;
	}

	public Button getCancel() {
		return cancel;
	}

	public Button getToBack() {
		return toBack;
	}

	public Button getLast10() {
		return last10;
	}

	public Button getViewAll() {
		return viewAll;
	}

	public Label getWarning() {
		return warning;
	}

	public TableView<Order> getFinishedOrdersTable() {
		return finishedOrdersTable;
	}

	public TableView<Order> getInProcessOrdersTable() {
		return inProcessOrdersTable;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public TableView<Statistics> getStatTable() {
		return statTable;
	}

	public Tab getAdminTab() {
		return adminTab;
	}

	public Tab getStatTab() {
		return statTab;
	}

}
