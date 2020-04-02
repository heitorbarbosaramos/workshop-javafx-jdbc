package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.LoadView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController  implements Initializable{

	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	LoadView loadView = new LoadView();
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView.loadView("/gui/DepartmentList.fxml");
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView.loadView("/gui/About.fxml");
		System.out.println("onMenuItemAboutAction");
	}
	
	public void initialize(URL uri, ResourceBundle rb) {	
	}
	
	

}
