package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	private SellerService sellerService;
	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangerListerner = new ArrayList<DataChangeListener>();

	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirtDate;
	@FXML
	private TextField txtBaseSalary;
	@FXML
	private Label labelErrorName;
	@FXML
	private Label labelErrorEmail;
	@FXML
	private Label labelErrorBirthDate;
	@FXML
	private Label labelErrorBaseSalary;
	@FXML
	private Button btSalve;
	@FXML
	private Button btCancel;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private ObservableList<Department> obsList;

	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (sellerService == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormdata();
			sellerService.saveOrUpdate(entity);
			notifyDataChangerListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setMessagemErrors(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error Saving Object", "Error Saving Object", e.getMessage(), AlertType.ERROR);
		}
		System.out.println("onBtSaveAction");
	}

	private void notifyDataChangerListeners() {
		for (DataChangeListener listeners : dataChangerListerner) {
			listeners.onDataChanged();
		}

	}

	@FXML
	public void onBtCanelAction(ActionEvent event) {
		Utils.currentStage(event).close();
		System.out.println("onBtCanelAction");
	}

	private Seller getFormdata() {
		Seller obj = new Seller();

		ValidationException exception = new ValidationException("validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			Alerts.showAlert("ERROR NAME", "ERROR NAME","NAME, FIELD CAN�T BE EMPTY", AlertType.ERROR);
			exception.addErros("name", "Field can't be empty");
		}
		
		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			Alerts.showAlert("ERROR NAME", "ERROR EMAIL","EMAIL, FIELD CAN�T BE EMPTY", AlertType.ERROR);
			exception.addErros("email", "Field can't be empty");
		}
		
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			Alerts.showAlert("ERROR BASE SALARY", "ERROR BASE SALARY","SALARY, FIELD CAN�T BE EMPTY", AlertType.ERROR);
			exception.addErros("base salary", "Field can't be empty");
		}
		
		
		Instant instant = null;
		if(dpBirtDate == null) {
			Alerts.showAlert("ERROR DATE BIRT", "ERROR DATE BIRT","DATE BIRT, FIELD CAN�T BE EMPTY", AlertType.ERROR);
			exception.addErros("date birt", "Field can't be empty");
		}else{
			instant = Instant.from(dpBirtDate.getValue().atStartOfDay(ZoneId.systemDefault()));
		}
		
		obj.setDepartment(comboBoxDepartment.getValue());
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		obj.setBirthDate(Date.from(instant));
		obj.setName(txtName.getText());
		obj.setEmail(txtEmail.getText());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirtDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setService(SellerService sellerService, DepartmentService departimentService) {
		this.sellerService = sellerService;
		this.departmentService = departimentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangerListerner.add(listener);
	}

	public void updateFormData() {

		if (entity == null) {
			Alerts.showAlert("Entity was null", "Entity was null", "Entity was null", AlertType.INFORMATION);
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirtDate.setValue(
					LocalDateTime.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()).toLocalDate());
		}
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			Alerts.showAlert("ERROR DEPARTIMENT SERVICE", "ERROR DEPARTIMENT SERVICE",
					"DEPARTIMENT WAS NULL \n SellerFormControl.java line 172", AlertType.ERROR);
			throw new IllegalStateException("Department Service was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setMessagemErrors(Map<String, String> errors) {
		Set<String> field = errors.keySet();

		labelErrorName.setText(field.contains("name")? errors.get("name") : "");
		labelErrorEmail.setText(field.contains("email")? errors.get("email") : "");
		labelErrorBaseSalary.setText(field.contains("baseSalary")? errors.get("baseSalary") : "");
		labelErrorBirthDate.setText(field.contains("birtDate")? errors.get("birtDate") : "");
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};

		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
