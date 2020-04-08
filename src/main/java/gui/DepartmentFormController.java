package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangerListerner = new ArrayList<DataChangeListener>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErrorName;
	@FXML
	private Button btSalve;
	@FXML
	private Button btCancel;

	@FXML
	public void onBtSaveAction(ActionEvent event) {

		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		try {
			entity = getFormdata();
			service.saveOrUpdate(entity);
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
		for(DataChangeListener listeners : dataChangerListerner) {
			listeners.onDataChanged();
		}
		
	}



	@FXML
	public void onBtCanelAction(ActionEvent event) {
		Utils.currentStage(event).close();
		System.out.println("onBtCanelAction");
	}
	
	
	private Department getFormdata() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addErros("name", "Field can't be empty");
		}
		obj.setName(txtName.getText());
		if(exception.getErrors().size() > 0) {
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
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
	}

	private void setMessagemErrors(Map<String,String> errors) {
		Set<String> field = errors.keySet();
		
		if(field.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}


}
