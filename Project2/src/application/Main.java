package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");// formatter for the date
	private SingleLinkedList orderQueue = new SingleLinkedList();
	private SingleLinkedList orderStack = new SingleLinkedList();
	private SingleLinkedList orders = new SingleLinkedList();
	private CircularDoublyLinkedList brandsList = new CircularDoublyLinkedList();
	private ObservableList<Brand> items = FXCollections.observableArrayList();
	private Order priority;
	private MainPage front = new MainPage();
	private ClientPage client = new ClientPage();
	private AdminPage admin = new AdminPage();
	private boolean ordersWereRead, carsWereRead = false;
	private File ordersSelectedFile, carsSelectedFile;

	@Override
	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		Scene mainScene = new Scene(root, 920, 490);
		Scene brandScene = new Scene(client, 600, 660);
		Scene filterScene = new Scene(client.getfilterPage(), 400, 450);
		Scene infoEditorScene = new Scene(front.getEditInfoPane(), 450, 460);
		Scene adminScene = new Scene(admin, 1500, 700);
		Scene brandEditorScene = new Scene(client.getBrandEditor(), 450, 360);
		Scene CarEditorScene = new Scene(client.getCarEditor(), 500, 500);

		Stage brandsStage = new Stage();
		Stage adminStage = new Stage();

		setColumns();

		// opens the information page then continues to the catalog O(1)
		front.getCustomer().setOnAction(e -> {

			// changing buttons visibility
			client.getClientButtons().setVisible(true);
			client.getAdminEditingButtons().setVisible(false);
			client.getWarning().setText("");

			// setting combo box & tableView
			client.setBrand((Brand) brandsList.getFirst());
			client.getBrandBox().setValue(client.getBrand());
			if (client.getBrand() != null)
				client.getCarsTable().setItems(client.getBrand().getSortedData());

			// setting some properties
			refreshTables();
			client.changeName("Client");
			brandsStage.setScene(infoEditorScene);
			brandsStage.setTitle("MY INFORMATION");
			brandsStage.toFront();
			brandsStage.show();
			client.setCustomer(new Customer());

		});

		front.getSave().setOnAction(e -> {// saves name and mobile from text fields O(1)

			// getting info from fields
			String name = front.getNametxf().getText();
			String mobile = front.getMobiletxf().getText();

			// constructing the customer object when strings aren't null
			if (!name.isEmpty() && !mobile.isEmpty()) {
				client.getCustomer().setMobile(mobile);
				client.getCustomer().setName(name);
				brandsStage.setScene(brandScene);
				brandsStage.setTitle("CATALOGUE-CLIENT");
				front.getWarning2().setText("");
				refreshTables();

			} else // null
				front.getWarning2().setText("Enter all fields.");

		});

		front.getSaveInfo().setOnAction(e -> {// saves cars & orders to files O(n+m)

			// allows saving when both files were read
			if (ordersWereRead && carsWereRead) {
				saveToFiles();
			}
		});

		front.getAdmin().setOnAction(e -> {// opens Admins page
			refreshTables();
			adminStage.setScene(adminScene);
			adminStage.setTitle("ADMIN");
			adminStage.show();
			adminStage.toFront();

		});

		front.getLoadCars().setOnAction(e -> {// reads the cars file O(n)
			readInfo();
			client.getBrandBox().setItems(items);// setting comboBox items
		});

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// CLIENT CONTROLS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		client.getCarsTable()// selecting a Car from the table
				.setOnMouseClicked(e -> client.setCar(client.getCarsTable().getSelectionModel().getSelectedItem()));

		client.getBrandBox().setOnAction(e -> {// when item is selected form brand combo box
			if (client.getBrand() != null) {// changing content of cars table
				try {
					client.setBrand(client.getBrandBox().getSelectionModel().getSelectedItem());
					client.getCarsTable().setItems(client.getBrand().getSortedData());

				} catch (NullPointerException d) {

				}
			}

		});

		client.getNext().setOnAction(e -> {// proceeding to next brand O(n)

			if (client.getBrand() != null) {// current brand not null
				Brand b = brandsList.getNext(client.getBrand());// getting next brand in CDLL O(n)
				client.setBrand(b);
				client.getBrandBox().setValue(client.getBrand());
				client.getCarsTable().setItems(client.getBrand().getSortedData());
				refreshTables();
				client.getWarning().setText("");
			}

		});

		client.getPrev().setOnAction(e -> {// going back to the previous brand O(n)

			if (client.getBrand() != null) {// current brand not null
				Brand b = brandsList.getPrevious(client.getBrand());// getting prev brand in CDLL O(n)
				client.setBrand(b);
				client.getBrandBox().setValue(client.getBrand());
				client.getCarsTable().setItems(client.getBrand().getSortedData());
				refreshTables();
				client.getWarning().setText("");

			}

		});

		client.getAddToCart().setOnAction(e -> {// adds selected car to the order queue O(n)

			Car c = client.getCar();// getting car selected

			if (c != null) {
				// constructing a new order for this car
				Order o = new Order(client.getCustomer(), client.getBrand(), c, new Date(), "InProcess");
				orders.addLast(o);// enQueueing

				if (ordersWereRead) {// if the orders file was read we combine the original orders to the new ones
					combine();
					showQueue();// showing queue on table
					admin.getSell().setDisable(false);

					if (orderQueue.isEmpty())// if that order had the priority
						priority = o;// set the priority to the next order

				}
				client.getWarning().setText("Car added to queue for admin to accept.");

			} else // no car is selected
				client.getWarning().setText("Please select a Car first.");

		});

		client.getEditInfo().setOnAction(e -> {// opens stage to edit the customers information

			if (client.getCustomer() != null) {
				front.getNametxf().setText(client.getCustomer().getName());
				front.getMobiletxf().setText(client.getCustomer().getMobile());
			}

			brandsStage.setScene(infoEditorScene);
			brandsStage.setTitle("MY INFORMATION");
			refreshTables();

		});

		client.getOpenfilters().setOnAction(e -> {// opens a stage to apply filters O(1)

			if (client.getBrand() != null) {
				setItemsInComboBox();
				refreshTables();
				brandsStage.setScene(filterScene);
				brandsStage.setTitle("FILTERS");

			} else// when the current brand is empty
				client.getWarning().setText("No Items to Filter.");

		});

		client.getRemoveFilters().setOnAction(e -> {// removing all selected filters O(1)
			client.getYearBox().setValue(null);
			client.getModelBox().setValue(null);
			client.getColorBox().setValue(null);
			client.getBudgetBox().setValue(null);
			refreshTables();

		});

		client.getApply().setOnAction(e -> {// applying selected filters to cars in the SLL O(n)

			if (client.getBrand() != null) {
				// list containing filtered items(cars)
				ObservableList<Car> filtered = FXCollections.observableArrayList(client.getBrand().getData());

				// getting the selected values form the combo box
				String model = client.getModelBox().getValue();
				String color = client.getColorBox().getValue();
				int year = -1;
				if (client.getYearBox().getValue() != null)
					year = Integer.parseInt(client.getYearBox().getValue());

				double budget = -1;
				if (client.getBudgetBox().getValue() != null)
					budget = getPriceFromFormat(client.getBudgetBox().getValue());

				// looping in the list and removing the cars that don't match the requirements
				// O(n)
				for (int i = 0; i < client.getBrand().getData().size(); i++) {
					Car c = client.getBrand().getData().get(i);

					if (model != null)
						if (!c.getModel().equalsIgnoreCase(model))
							filtered.remove(c);

					if (color != null)
						if (!c.getColor().equalsIgnoreCase(color))
							filtered.remove(c);

					if (year != -1)
						if (c.getYear() != year)
							filtered.remove(c);

					if (budget != -1)
						if (c.getPrice() > budget)
							filtered.remove(c);

				}

				client.getCarsTable().setItems(filtered.sorted());// setting table items to the sorted filtered one
			}
			brandsStage.setScene(brandScene);
			brandsStage.setTitle("CATALOGUE-CLIENT");
			refreshTables();

		});

		client.getSearchCar().setOnAction(e -> {// searching car by model O(m+n)

			Brand b = client.getBrand();// getting current selected brand

			if (b != null) {

				String s = client.getCartxf().getText();

				if (!s.isEmpty()) {// textField not empty
					Car c = (Car) ((Brand) brandsList.get(b)).getCarsList().get(s);// retrieving Car with the model name
																					// (O(n+m))
					if (c != null) {// car found
						client.setCar(c);
						client.getCarsTable().getSelectionModel().select(c);// selects car from table
						client.getCarsTable().scrollTo(client.getCarsTable().getSelectionModel().getSelectedIndex());
						client.getWarning().setText("This Car was found and selected!");

					} else // car not found
						client.getWarning().setText("This Car does not exist.");
					client.getCartxf().setText("");

				} else// no text to search
					client.getWarning().setText("Please enter a Car Model to search.");

			} else// brand is null
				client.getWarning().setText("No Brands to search from.");
			refreshTables();

		});

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ADMIN CONTROLS
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		admin.getLoadOrders().setOnAction(e -> loadOrders());// reads the orders file O(n)

		admin.getNavBrands().setOnAction(e -> {

			// changing buttons visibility
			client.getClientButtons().setVisible(false);
			client.getAdminEditingButtons().setVisible(true);
			client.getWarning().setText("");

			// setting combo box & tableView
			client.setBrand((Brand) brandsList.getFirst());
			client.getBrandBox().setValue(client.getBrand());
			if (client.getBrand() != null)
				client.getCarsTable().setItems(client.getBrand().getSortedData());

			// setting some properties
			refreshTables();
			client.changeName("Admin");
			brandsStage.setScene(brandScene);
			brandsStage.setTitle("CATALOGUE-ADMIN");
			brandsStage.show();
			brandsStage.toFront();

		});

		admin.getLast10().setOnAction(e -> {// shows last 10 sold cars O(n)

			SingleLinkedList dummyStack = new SingleLinkedList();// creating a dummy Stack
			ObservableList<Order> stackItems = FXCollections.observableArrayList();
			int count = 0;

			// looping in the stack while filling the table and the dummy
			while (count < 10 && !orderStack.isEmpty()) {

				Order o = (Order) orderStack.getFirst();// Pop from orders stack
				orderStack.deleteFirst();
				stackItems.add(o);// adding to table
				dummyStack.addFirst(o);// push to dummy
				count++;
			}
			// setting items in Table
			admin.getFinishedOrdersTable().setItems(stackItems);

			// returning the orders to the Stack
			while (!dummyStack.isEmpty()) {

				Order o = (Order) dummyStack.getFirst();// Pop from dummy
				dummyStack.deleteFirst();
				orderStack.addFirst(o);// push to orders stack
			}
			refreshTables();
		});

		admin.getViewAll().setOnAction(e -> getAllSoldCars());// displaying all orders to in stack O(n)

		admin.getCancel().setOnAction(e -> {// cancel/deny an order O(n)

			Order first = (Order) orderQueue.getFirst();// saving first element before deQueueing
			orderQueue.deleteFirst();// deQueue

			if (priority.equals(first) && orderQueue.getFirst() != null)// if that order had the priority
				priority = (Order) orderQueue.getFirst();// set the priority to the next order

			showQueue();// refreshing queue in table
			admin.getWarning().setText("Order was rejected/canceled");

			if (orderQueue.isEmpty()) // disabling buttons when no items are left in the queue
				disableButtons();
			refreshTables();

			admin.getToBack().setDisable(true);
			admin.getCancel().setDisable(true);

		});

		admin.getToBack().setOnAction(e -> {// moving order to the back of the queue O(n)

			// deQueue and enQueue
			orderQueue.addLast(orderQueue.getFirst());
			orderQueue.deleteFirst();

			showQueue();// refreshing queue in table
			admin.getWarning().setText("Order was moved to the back of the queue");

			if (orderQueue.isEmpty()) // disabling buttons when no items are left in the queue
				disableButtons();
			refreshTables();
			admin.getToBack().setDisable(true);
			admin.getCancel().setDisable(true);

		});

		admin.getSell().setOnAction(e -> {// selling the order in queue then moving it to stack O(n+m)

			Order o = (Order) orderQueue.getFirst();// peeking on the queue
			Brand b = (Brand) brandsList.get(o.getBrand());// getting the brand from CDLL O(n)

			if (b != null) {// when the brand exists
				Car c = (Car) b.getCarsList().get(o.getCar());// getting the car from SLL O(m)

				if (c != null) {// when car is available

					o.setOrderStatus("Finished");// changing status to finished
					orderStack.addFirst(o);// pushing order to stack
					getAllSoldCars();// refreshing contents of the stack tableView

					Order first = (Order) orderQueue.getFirst();// peeking first order in queue
					orderQueue.deleteFirst();// deQueueing the first order

					if (priority.equals(first) && orderQueue.getFirst() != null)// if that order had the priority
						priority = (Order) orderQueue.getFirst();// set the priority to the next order

					b.getCarsList().delete(c);// deleting from car SLL O(m)
					b.getData().remove(c);// removing from TableView
					refreshTables();
					showQueue();// refreshing queue in table
					admin.getWarning().setText("Successfully sold the car!");

				} else {// car not available
					admin.getWarning().setText("This Car is not re-stocked yet");
					admin.getToBack().setDisable(false);
					admin.getCancel().setDisable(false);
				}
			} else {// brand not available
				admin.getWarning().setText("This Brand is not re-stocked yet");
				admin.getToBack().setDisable(false);
				admin.getCancel().setDisable(false);
			}
			if (orderQueue.isEmpty()) // disabling buttons when queue is empty
				disableButtons();

		});

		admin.getStatTab().setOnSelectionChanged(e -> {
			// filling reports table
			if (carsWereRead)
				setTable();
			refreshTables();
		});

		client.getDeleteB().setOnAction(e -> {// deleting current selected brand O(n)

			if (client.getBrand() != null) {
				Brand b = client.getBrand();// saving brand before deletion
				client.setBrand(brandsList.getNext(b));// setting the current brand to the the next
				brandsList.delete(b);// deleting this Brand from CDLL O(n)
				client.getWarning().setText("Successfully deleted " + b.getBrandName());
				setBrand(client.getBrand()); // setting current brand and items
				items.remove(b);// removing from comboBox O(n)
				refreshTables();

			}
		});

		client.getDeleteC().setOnAction(e -> {// deleting selected car O(m+m)

			Car c = client.getCar();
			if (c != null) {// when a Car was selected (not null)

				client.getBrand().getData().remove(c);// remove from observable List (O(log(m)))
				((Brand) brandsList.get(client.getBrand())).getCarsList().delete(c);// remove from SLL (O(n+m))
				client.getCarWarning().setText("Car successfully deleted!");

			} else// car not selected
				client.getWarning().setText("Please select/search a Car to delete.");

			client.getCarsTable().getSelectionModel().clearSelection();
			client.setCar(null);// clearing selections
			refreshTables();
		});

		client.getAddNewB().setOnAction(e -> {// opens stage to add a location O(1)

			client.getBrandHeading().setText("NEW BRAND:");
			// switching buttons visibility
			client.getUpB().setVisible(false);
			client.getAddB().setVisible(true);
			emptyFields();// emptying textFields
			brandsStage.setScene(brandEditorScene);
			brandsStage.setTitle("NEW BRAND");
		});

		client.getAddB().setOnAction(e -> {

			String s = client.getBrandtxf().getText();// name from TextField

			if (s.isEmpty()) {// no location name
				client.getBrandWarning().setText("Enter a Brand to proceed.");
			} else {
				Brand b = new Brand(s);

				if (brandsList.get(b) == null) {// when the Brand DNE in the CDLL O(n)

					// adding the new Brand
					brandsList.addSorted(b);// O(n)
					client.getWarning().setText("Brand succesfully added!");
					client.setBrand(null);
					items.add(b);// adding it to comboBox
					client.getBrandBox().setItems(items.sorted());
					client.setBrand(b);
					client.getBrandBox().setValue(client.getBrand());
					brandsStage.setScene(brandScene);
					brandsStage.setTitle("CATALOGUE-ADMIN");
					refreshTables();

				} else// location already exists
					client.getBrandWarning().setText("Fail...Brand already exists.");
			}

		});

		client.getAddNewC().setOnAction(e -> {// opens stage to add a location O(1)

			if (client.getBrand() != null) {
				client.getCarHeading().setText("NEW CAR:");
				// switching buttons visibility
				client.getUpC().setVisible(false);
				client.getAddC().setVisible(true);
				emptyFields();// emptying textFields
				brandsStage.setScene(CarEditorScene);
				brandsStage.setTitle("NEW CAR");

			} else
				client.getWarning().setText("add a car Brand first");
		});

		client.getAddC().setOnAction(e -> {

			String model;
			String color;
			String year;
			String price;

			try {
				// setting the variable to text written by user
				model = client.getModeltxf().getText();
				color = client.getColortxf().getText();
				year = client.getYeartxf().getText();
				price = getPriceFromFormat(client.getPricetxf().getText()) + "";

				// checking if the values are full
				if (model.isEmpty() || color.isEmpty() || price.isEmpty())
					throw new IllegalArgumentException();
				else {// all fields are filled with the right values

					if (year.isEmpty())// current year if its empty
						year = String.valueOf(new GregorianCalendar().get(Calendar.YEAR));
					Car c;
					// constructing Car Object
					c = new Car(model, Integer.parseInt(year), color, Double.parseDouble(price));

					((Brand) brandsList.get(client.getBrand())).getCarsList().addSorted(c);// O(n+m)
					client.getBrand().getData().add(c);// O(log(n))
					adjustLists(client.getBrand(), c);
					client.getWarning().setText("Car Successfully added!");
					brandsStage.setScene(brandScene);
					brandsStage.setTitle("CATALOGUE-ADMIN");
					refreshTables();

				}

			} catch (IllegalArgumentException h) {// error in one of the fields
				client.getCarWarning().setText("Enter all values in their correct form.");
			}

		});

		client.getUpdateB().setOnAction(e -> {// opens brand editor

			if (client.getBrand() != null) {
				client.getBrandHeading().setText("UPDATE BRAND:");
				// switching buttons visibility
				client.getUpB().setVisible(true);
				client.getAddB().setVisible(false);
				client.getBrandtxf().setText(client.getBrand().getBrandName());
				brandsStage.setScene(brandEditorScene);
				brandsStage.setTitle("UPDATE BRAND");

			}

		});

		client.getUpB().setOnAction(e -> {// update the brand to its new name O(n)

			String s = client.getBrandtxf().getText();// BrandName form TextField

			if (s.isEmpty()) {// empty name
				client.getBrandWarning().setText("Enter Brand name to proceed.");
			} else {// assigning the Brand with the new name

				Brand temp = (Brand) brandsList.get(client.getBrand());// saving brand to preserve SLL
				client.setBrand(null);// to prevent null error when triggering comboBox action
				brandsList.delete(temp);// removing from CDLL O(n)
				items.remove(temp);// removing from comboBox
				temp.setBrandName(s);// changing name
				brandsList.addSorted(temp);// adding back the Brand with changed name O(n)
				items.add(temp);// adding it to comboBox
				client.getBrandBox().setItems(items.sorted());
				client.setBrand(temp);
				client.getBrandBox().setValue(client.getBrand());
				client.getWarning().setText("Brand succesfully updated!");
				brandsStage.setScene(brandScene);
				brandsStage.setTitle("CATALOGUE-ADMIN");
				refreshTables();
			}

		});

		client.getUpdateC().setOnAction(e -> {// opens Car editor

			if (client.getCar() != null) {
				client.getCarHeading().setText("UPDATE CAR:");
				// switching buttons visibility
				client.getUpC().setVisible(true);
				client.getAddC().setVisible(false);
				// filling fields
				client.getModeltxf().setText(client.getCar().getModel());
				client.getYeartxf().setText(client.getCar().getYear() + "");
				client.getColortxf().setText(client.getCar().getColor());
				client.getPricetxf().setText(client.getCar().getPrice() + "");
				brandsStage.setScene(CarEditorScene);
				brandsStage.setTitle("UPDATE CAR");

			} else// car not selected
				client.getWarning().setText("Select a Car first.");

		});

		client.getUpC().setOnAction(e -> {// updates the current car to its new values O(n+m)

			String model;
			String color;
			String year;
			String price;

			try {
				// setting the variable to text written by user
				model = client.getModeltxf().getText();
				color = client.getColortxf().getText();
				year = client.getYeartxf().getText();
				price = getPriceFromFormat(client.getPricetxf().getText()) + "";

				// checking if the values are full
				if (model.isEmpty() || color.isEmpty() || price.isEmpty())
					throw new IllegalArgumentException();

				else {// all fields are filled with the right values
					Car c;

					if (year.isEmpty())// current year if its empty
						year = String.valueOf(new GregorianCalendar().get(Calendar.YEAR));

					Brand b = ((Brand) brandsList.get(client.getBrand()));// getting Brand from CDLL O(n)

					// editing the Car Object
					c = client.getCar();
					c.setModel(model);
					c.setColor(color);
					c.setYear(Integer.parseInt(year));
					c.setPrice(Double.parseDouble(price));

					b.getData().remove(client.getCar());// removing from table O(log(m))
					b.getCarsList().delete(client.getCar());// removing from SLL O(m)
					b.getCarsList().addSorted(c);// adding updated car sorted O(m)
					b.getData().add(c);// adding updated car to table O(log(m)
					refreshTables();
					adjustLists(client.getBrand(), c);
					client.getCarsTable().getSelectionModel().clearSelection();
					client.setCar(null);// clearing selections
					emptyFields();// emptying fields
					client.getWarning().setText("Car Successfully updated!");
					brandsStage.setScene(brandScene);
					brandsStage.setTitle("CATALOGUE-ADMIN");

				}
			} catch (IllegalArgumentException h) {// error in one of the fields
				client.getCarWarning().setText("Enter all values in their correct form.");
			}

		});

		client.getCancelB().setOnAction(e -> {
			client.getBrandWarning().setText("");
			refreshTables();
			brandsStage.setScene(brandScene);
			brandsStage.setTitle("CATALOGUE-ADMIN");
		});

		client.getCancelC().setOnAction(e -> {
			client.getCarWarning().setText("");
			brandsStage.setScene(brandScene);
			brandsStage.setTitle("CATALOGUE-ADMIN");
			client.getCarsTable().getSelectionModel().clearSelection();
			client.setCar(null);
			refreshTables();
			emptyFields();
		});

		root.setCenter(front);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		primaryStage.setTitle("CAR DEALERSHIP SYSTEM");
		buttonMovements();

	}

	public void combine() {// combines the general queue with the new orders queue O(n)

		while (!orders.isEmpty()) {
			// deQueue and enQueue
			orderQueue.addLast((Order) orders.getFirst());
			orders.deleteFirst();
		}

	}

	public void showQueue() {// presenting the queue to the table view O(n)

		ObservableList<Order> queueList = FXCollections.observableArrayList();

		for (int i = 0; i < orderQueue.getSize(); i++) {
			Order o = (Order) orderQueue.getFirst();// peek
			orderQueue.deleteFirst();// deQueue
			queueList.add(o);
			orderQueue.addLast(o);// enQueue
		}

		admin.getInProcessOrdersTable().setItems(queueList);
		refreshTables();
	}

	public void getAllSoldCars() {// presenting all of the stack to the table view O(n)
		ObservableList<Order> stackList = FXCollections.observableArrayList();
		SingleLinkedList dummyStack = new SingleLinkedList();

		while (!orderStack.isEmpty()) {// moving all orders to a dummy stack O(n)
			Order o = (Order) orderStack.getFirst();// peek
			orderStack.deleteFirst();// deQueue
			stackList.add(o);// adding to observable list
			dummyStack.addFirst(o);// enQueue

		}

		admin.getFinishedOrdersTable().setItems(stackList);
		refreshTables();

		while (!dummyStack.isEmpty()) {// moving orders back to the stack O(n)
			Order o = (Order) dummyStack.getFirst();// peek
			dummyStack.deleteFirst();// deQueue
			orderStack.addFirst(o);// enQueue

		}

	}

	private void refreshTables() {

		admin.getStatTable().refresh();
		admin.getFinishedOrdersTable().refresh();
		admin.getInProcessOrdersTable().refresh();
		client.getCarsTable().refresh();

	}

	private void setBrand(Brand b) {// sets the the current brand and car items
		client.getBrandBox().setValue(b);
		client.getCarsTable().setItems(b.getSortedData());

	}

	private void emptyFields() {// emptying all textFields and warning labels
		client.getBrandtxf().setText("");
		client.getModeltxf().setText("");
		client.getYeartxf().setText("");
		client.getColortxf().setText("");
		client.getPricetxf().setText("");
		client.getWarning().setText("");

	}

	private void disableButtons() {// disabling buttons when queue is empty
		admin.getSell().setDisable(true);
		admin.getCancel().setDisable(true);
		admin.getToBack().setDisable(true);

	}

	// converts the formated price to a double value
	private double getPriceFromFormat(String fomrattedPrice) throws IllegalArgumentException {

		String symbol = fomrattedPrice.substring(fomrattedPrice.length() - 1);// getting the symbol at the end

		if (symbol != null && Character.isLetter(symbol.charAt(0))) {
			String num = fomrattedPrice.substring(0, fomrattedPrice.length() - 1);

			if (symbol.equalsIgnoreCase("k")) {// thousands
				return Double.parseDouble(num) * 1000;

			} else if (symbol.equalsIgnoreCase("m"))// millions
				return Double.parseDouble(num) * 1000000;
			else// not million or thousand
				throw new IllegalArgumentException();
		}

		// no symbol at the end(return same value as the string)
		return Double.parseDouble(fomrattedPrice);
	}

	private void setItemsInComboBox() {// filling combo boxes

		if (client.getBrand() != null) {
			client.getModelBox().setItems(client.getBrand().getModelsList().sorted());
			client.getColorBox().setItems(client.getBrand().getColorsList().sorted());
			// using comparator to sort the values of the price Strings
			Collections.sort(client.getBrand().getPriceList(), new comparing());
			client.getBudgetBox().setItems(client.getBrand().getPriceList());
			client.getYearBox().setItems(client.getBrand().getYearList().sorted());

		}

	}

	private void setTable() {

		DNode curr = brandsList.getFirstNode();// first Brand
		ObservableList generalCalculations = FXCollections.observableArrayList();
		admin.getStatTable().setItems(generalCalculations);// setting items

		// filling the observable list with each Brand
		for (int i = 0; i < brandsList.getSize(); i++) {
			generalCalculations.add(new Statistics((Brand) curr.getElement()));
			curr = curr.getNext();
		}
	}

	private void adjustLists(Brand b, Car c) {// adds the car properties to the combo boxes accordingly O(n)

		if (!((Brand) brandsList.get(b)).getModelsList().contains(c.getModel()))
			((Brand) brandsList.get(b)).getModelsList().add(c.getModel());

		if (!((Brand) brandsList.get(b)).getColorsList().contains(c.getColor()))
			((Brand) brandsList.get(b)).getColorsList().add(c.getColor());

		if (!((Brand) brandsList.get(b)).getPriceList().contains(c.getFormattedPrice()))
			((Brand) brandsList.get(b)).getPriceList().add(c.getFormattedPrice());

		if (!((Brand) brandsList.get(b)).getYearList().contains(Integer.toString(c.getYear())))
			((Brand) brandsList.get(b)).getYearList().add(Integer.toString(c.getYear()));

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void saveToFiles() {// saves the orders and car inventory to their corresponding files

		// printing Orders
		try {

			PrintWriter write = new PrintWriter(ordersSelectedFile);// creating print writer with order file
			write.print("");// emptying the file then adding the header
			write.println("CustomerName, CustomerMobile, Brand, Model, Year, Color, Price, OrderDate, OrderStatus");

			//// printing Stack ////
			SingleLinkedList dummyStack = new SingleLinkedList();

			while (!orderStack.isEmpty()) {// moving it to a dummy stack o(n)
				Order o = (Order) orderStack.getFirst();// peek
				orderStack.deleteFirst();// pop
				dummyStack.addFirst(o);// push to dummy
			}

			while (!dummyStack.isEmpty()) {// filling order stack again O(n)
				Order o = (Order) dummyStack.getFirst();// peek
				dummyStack.deleteFirst();// pop
				write.println(o.toString());// writing to file
				orderStack.addFirst(o);// push to order stack

			}

			//// printing queue ////
			if (!orderQueue.isEmpty()) {
				while (!((Order) orderQueue.getFirst()).equals(priority)) {// looping in the queue until order with
																			// priority is encountered
					orderQueue.addLast(orderQueue.getFirst());// enQueue
					orderQueue.deleteFirst();// deQueue
				}

				for (int i = 0; i < orderQueue.getSize(); i++) {// looping the queue and printing the orders
					Order o = (Order) orderQueue.getFirst();// peek
					orderQueue.deleteFirst();// deQueue
					write.println(o.toString());// printing to file
					orderQueue.addLast(o);// enQueue
				}
			}
			write.close();// closing printWriter
			front.getWarning1().setText("Saved!");
		} catch (FileNotFoundException | NullPointerException e) {
			front.getWarning1().setText("Error Saving.");
		}

		// printing cars
		// top row (headers of the columns)
		String header = "Brand, Model, Year, Color, Price";

		try {

			// using printWriter
			PrintWriter output = new PrintWriter(carsSelectedFile);
			output.print("");// clearing all contents of the file
			output.println(header);// printing the header

			DNode currentBrand = brandsList.getFirstNode();// the head node in the CDLL
			// the head node of the SLL at the first Brand
			SNode currentCar = ((Brand) currentBrand.getElement()).getCarsList().getFirstNode();

			for (int i = 0; i < brandsList.getSize(); i++) {// looping through all brands(n)
				while (currentCar != null) {// looping until we reach the last car(m)

					Car c = (Car) currentCar.getElement();
					output.println(((Brand) currentBrand.getElement()).getBrandName() + ", " + c.toString());// printing
					currentCar = currentCar.getNext();// next car node
				}

				currentBrand = currentBrand.getNext();// next brand node
				// head node in this brand SLL
				currentCar = ((Brand) currentBrand.getElement()).getCarsList().getFirstNode();
			}

			output.close();// closing printWriter
			front.getWarning1().setVisible(true);
			front.getWarning1().setText("Saved!");

		} catch (FileNotFoundException | NullPointerException r) {// when file isn't chosen or not found
			front.getWarning1().setVisible(true);
			front.getWarning1().setText("Error Saving.");
		}

	}

	private void loadOrders() {

		FileChooser fileChooser = new FileChooser();
		ordersSelectedFile = fileChooser.showOpenDialog(new Stage());
		// to read string form of date

		try {

			Scanner input = new Scanner(ordersSelectedFile);// scanning file
			input.nextLine();// skipping header

			while (input.hasNextLine()) {// reading line by line O(k)
				Scanner read = new Scanner(input.nextLine());// reading word by word
				read.useDelimiter(",");
				try {

					String name = read.next().trim();
					String mobile = read.next().trim();
					String brandName = read.next().trim();
					String model = read.next().trim();
					int year = Integer.parseInt(read.next().trim());
					String color = read.next().trim();
					String price = read.next().trim();
					Date orderDate = formatter.parse(read.next().trim());
					String orderStatus = read.next().trim();

					// checking if values are full
					if (name.isEmpty() || mobile.isEmpty() || brandName.isEmpty() || color.isEmpty() || price.isEmpty()
							|| orderStatus.isEmpty())
						throw new IllegalArgumentException();

					// initializing values
					Brand b = (Brand) brandsList.get(new Brand(brandName));
					Customer customer = new Customer(name, mobile);
					Order order = new Order(customer, new Brand(brandName), new Car(model, year, color, price),
							orderDate, orderStatus);

					if (b != null) {// when brand exists

						Car c = (Car) b.getCarsList().get(new Car(model, year, color, price));

						if (c != null) // when car in the brand exists

							order = new Order(customer, b, c, orderDate, orderStatus);

						else// car in the brand does not exist
							order = new Order(customer, b, new Car(model, year, color, price), orderDate, orderStatus);

					} else// the brand does not exist
						order = new Order(customer, new Brand(brandName), new Car(model, year, color, price), orderDate,
								orderStatus);

					if (orderStatus.equalsIgnoreCase("Finished")) // adding finished orders to stack
						orderStack.addFirst(order);

					else if (orderStatus.equalsIgnoreCase("InProcess")) // adding the in-process orders to queue
						orderQueue.addLast(order);

				} catch (NullPointerException | NoSuchElementException | IllegalArgumentException | ParseException e) {
					// Skipping lines with illegal inputs

				}
			}
			input.close();// closing Scanner
			combine();
			showQueue();
			getAllSoldCars();
			ordersWereRead = true;
			priority = (Order) orderQueue.getFirst();
			admin.getLast10().setDisable(false);
			admin.getViewAll().setDisable(false);
			admin.getSell().setDisable(false);
			refreshTables();

		} catch (NullPointerException | FileNotFoundException |

				NoSuchElementException e1) {
			// In-case of wrong file selection
			admin.getWarning().setText("Faild To Read File.");
		}

	}

	private void readInfo() {// reads information from file (O(k*(m+n))) k->#lines | m->SLL | n->CDLL

		front.getWarning1().setVisible(false);

		// choosing file from desktop
		FileChooser fileChooser = new FileChooser();
		carsSelectedFile = fileChooser.showOpenDialog(new Stage());

		// to read string form of date
		try {
			Scanner input = new Scanner(carsSelectedFile);// scanning file
			input.nextLine();// skipping header

			while (input.hasNextLine()) {// reading line by line O(k)
				Scanner read = new Scanner(input.nextLine());// reading word by word
				read.useDelimiter(",");
				try {
					// filling variables with values from the line
					String brandName = read.next().trim();
					String model = read.next().trim();
					String y = read.next().trim();
					int year = new GregorianCalendar().get(Calendar.YEAR);
					if (!y.isEmpty())
						year = Integer.parseInt(y);

					String color = read.next().trim();
					String price = read.next().trim();

					Car c = new Car(model, year, color, price);// creating Car object
					Brand b = new Brand(brandName);// creating Brand Object

					if (brandsList.get(b) != null) {// Location already exists in CDLL O(m+n)
						b = (Brand) brandsList.get(b);// O(n)
						b.getData().add(c);// adding car to the brand observable list O(log(m))
						b.getCarsList().addSorted(c);// O(m)

					} else {// adding new Brand to CDLL
						brandsList.addSorted(b);// O(n)
						b = (Brand) brandsList.get(b);// O(n)
						b.getData().add(c);// adding car to the brand observable list O(log(m))
						items.add(b);// adding to observable list for comboBox
						b.getCarsList().addSorted(c);// O(m)
					}
					adjustLists(b, c);// adding car properties to combo box lists

				} catch (NullPointerException | IllegalArgumentException | NoSuchElementException e) {
					// Skipping lines with illegal inputs or empty lines
				}

			}
			input.close();// closing Scanner
			carsWereRead = true;
			front.getWarning1().setVisible(true);
			front.getWarning1().setText("Read File.");

		} catch (NullPointerException | FileNotFoundException | NoSuchElementException e1) {
			// In-case of wrong file selection
			front.getWarning1().setVisible(true);
			front.getWarning1().setText("Faild To Read File.");

		}
		Collections.sort(items);

	}

	private void setColumns() {// setting columns in the tables and their properties

		// Car Table
		TableColumn<Car, String> model = new TableColumn<Car, String>("Model");
		TableColumn<Car, Integer> year = new TableColumn<Car, Integer>("Year");
		TableColumn<Car, String> color = new TableColumn<Car, String>("Color");
		TableColumn<Car, String> price = new TableColumn<Car, String>("Price");

		model.setCellValueFactory(new PropertyValueFactory<Car, String>("model"));
		year.setCellValueFactory(new PropertyValueFactory<Car, Integer>("year"));
		color.setCellValueFactory(new PropertyValueFactory<Car, String>("color"));
		price.setCellValueFactory(new PropertyValueFactory<Car, String>("formattedPrice"));

		model.setStyle("-fx-alignment: CENTER;-fx-pref-width: 138;");
		year.setStyle("-fx-alignment: CENTER;-fx-pref-width: 138;");
		color.setStyle("-fx-alignment: CENTER;-fx-pref-width: 138;");
		price.setStyle("-fx-alignment: CENTER;-fx-pref-width: 138;");

		// preventing automatic sorting
		model.setSortable(false);
		year.setSortable(false);
		color.setSortable(false);
		price.setSortable(false);

		// to prevent moving the columns
		model.setReorderable(false);
		year.setReorderable(false);
		color.setReorderable(false);
		price.setReorderable(false);

		// setting columns for each table
		client.getCarsTable().getColumns().addAll(model, year, color, price);

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// Queue columns

		TableColumn<Order, Customer> customer = new TableColumn<Order, Customer>("Customer");
		TableColumn<Order, Brand> brand = new TableColumn<Order, Brand>("Brand");
		TableColumn<Order, Car> car = new TableColumn<Order, Car>("Car");
		TableColumn<Order, String> date = new TableColumn<Order, String>("Date");
		TableColumn<Order, String> status = new TableColumn<Order, String>("Status");

		customer.setCellValueFactory(new PropertyValueFactory<Order, Customer>("customer"));
		brand.setCellValueFactory(new PropertyValueFactory<Order, Brand>("brand"));
		car.setCellValueFactory(new PropertyValueFactory<Order, Car>("car"));
		date.setCellValueFactory(new PropertyValueFactory<Order, String>("formattedDate"));
		status.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderStatus"));

		customer.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size:13;");
		brand.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;-fx-font-size:15;");
		car.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size:15;");
		date.setStyle("-fx-alignment: CENTER;-fx-pref-width: 90;-fx-font-size:15;");
		status.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;-fx-font-size:15;");

		// preventing automatic sorting
		customer.setSortable(false);
		brand.setSortable(false);
		car.setSortable(false);
		date.setSortable(false);
		status.setSortable(false);

		// to prevent moving the columns
		customer.setReorderable(false);
		brand.setReorderable(false);
		car.setReorderable(false);
		date.setReorderable(false);
		status.setReorderable(false);

		// setting columns for each table
		admin.getInProcessOrdersTable().getColumns().addAll(customer, brand, car, date, status);

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// Stack columns

		TableColumn<Order, Customer> customer1 = new TableColumn<Order, Customer>("Customer");
		TableColumn<Order, Brand> brand1 = new TableColumn<Order, Brand>("Brand");
		TableColumn<Order, Car> car1 = new TableColumn<Order, Car>("Car");
		TableColumn<Order, String> date1 = new TableColumn<Order, String>("Date");
		TableColumn<Order, String> status1 = new TableColumn<Order, String>("Status");

		customer1.setCellValueFactory(new PropertyValueFactory<Order, Customer>("customer"));
		brand1.setCellValueFactory(new PropertyValueFactory<Order, Brand>("brand"));
		car1.setCellValueFactory(new PropertyValueFactory<Order, Car>("car"));
		date1.setCellValueFactory(new PropertyValueFactory<Order, String>("formattedDate"));
		status1.setCellValueFactory(new PropertyValueFactory<Order, String>("OrderStatus"));

		customer1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size:13;");
		brand1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;-fx-font-size:15;");
		car1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size:15;");
		date1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 90;-fx-font-size:15;");
		status1.setStyle("-fx-alignment: CENTER;-fx-pref-width: 100;-fx-font-size:15;");

		// preventing automatic sorting
		customer1.setSortable(false);
		brand1.setSortable(false);
		car1.setSortable(false);
		date1.setSortable(false);
		status1.setSortable(false);

		// to prevent moving the columns
		customer1.setReorderable(false);
		brand1.setReorderable(false);
		car1.setReorderable(false);
		date1.setReorderable(false);
		status1.setReorderable(false);
		// setting columns for each table
		admin.getFinishedOrdersTable().getColumns().addAll(customer1, brand1, car1, date1, status1);

		/////////////////////////////////////////////////////////////////////////////////////////////////////
		// Statistics columns

		TableColumn<Statistics, String> brandName = new TableColumn<Statistics, String>("Brand");
		TableColumn<Statistics, String> highestPrice = new TableColumn<Statistics, String>("Highest");
		TableColumn<Statistics, String> lowestPrice = new TableColumn<Statistics, String>("Lowest");
		TableColumn<Statistics, String> highestModel = new TableColumn<Statistics, String>("Highest");
		TableColumn<Statistics, String> lowestModel = new TableColumn<Statistics, String>("Lowest");
		// main columns
		TableColumn<Statistics, String> models = new TableColumn<Statistics, String>("Model");
		TableColumn<Statistics, String> prices = new TableColumn<Statistics, String>("Price");

		brandName.setCellValueFactory(new PropertyValueFactory<Statistics, String>("brandName"));
		highestPrice.setCellValueFactory(new PropertyValueFactory<Statistics, String>("highestPrice"));
		lowestPrice.setCellValueFactory(new PropertyValueFactory<Statistics, String>("lowestPrice"));
		highestModel.setCellValueFactory(new PropertyValueFactory<Statistics, String>("highestModel"));
		lowestModel.setCellValueFactory(new PropertyValueFactory<Statistics, String>("lowestModel"));

		models.getColumns().addAll(highestModel, lowestModel);
		prices.getColumns().addAll(highestPrice, lowestPrice);

		admin.getStatTable().getColumns().addAll(brandName, prices, models);

		// preventing automatic sorting
		brandName.setSortable(false);
		highestPrice.setSortable(false);
		lowestPrice.setSortable(false);
		highestModel.setSortable(false);
		lowestModel.setSortable(false);

		// to prevent moving the columns
		brandName.setReorderable(false);
		highestPrice.setReorderable(false);
		lowestPrice.setReorderable(false);
		highestModel.setReorderable(false);
		lowestModel.setReorderable(false);

		brandName.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size: 17;");
		highestPrice.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size: 17;");
		lowestPrice.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size: 17;");
		highestModel.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size: 17;");
		lowestModel.setStyle("-fx-alignment: CENTER;-fx-pref-width: 200;-fx-font-size: 17;");
		models.setStyle("-fx-alignment: CENTER;-fx-pref-width: 400;-fx-font-size: 17;");
		prices.setStyle("-fx-alignment: CENTER;-fx-pref-width: 400;-fx-font-size: 17;");

		admin.getStatTable().setMaxWidth(1000);

	}

	private void buttonMovements() {

		front.getCustomer()
				.setOnMouseEntered(e -> front.getCustomer().setTranslateY(front.getCustomer().getTranslateY() - 7));
		front.getCustomer()
				.setOnMouseExited(e -> front.getCustomer().setTranslateY(front.getCustomer().getTranslateY() + 7));

		front.getAdmin().setOnMouseEntered(e -> front.getAdmin().setTranslateY(front.getAdmin().getTranslateY() - 7));
		front.getAdmin().setOnMouseExited(e -> front.getAdmin().setTranslateY(front.getAdmin().getTranslateY() + 7));

		front.getLoadCars()
				.setOnMouseEntered(e -> front.getLoadCars().setTranslateY(front.getLoadCars().getTranslateY() - 7));
		front.getLoadCars()
				.setOnMouseExited(e -> front.getLoadCars().setTranslateY(front.getLoadCars().getTranslateY() + 7));

		front.getCustomer()
				.setOnMouseEntered(e -> front.getCustomer().setTranslateY(front.getCustomer().getTranslateY() - 7));
		front.getCustomer()
				.setOnMouseExited(e -> front.getCustomer().setTranslateY(front.getCustomer().getTranslateY() + 7));
		front.getSaveInfo()
				.setOnMouseEntered(e -> front.getSaveInfo().setTranslateY(front.getSaveInfo().getTranslateY() - 7));
		front.getSaveInfo()
				.setOnMouseExited(e -> front.getSaveInfo().setTranslateY(front.getSaveInfo().getTranslateY() + 7));

	}

}
