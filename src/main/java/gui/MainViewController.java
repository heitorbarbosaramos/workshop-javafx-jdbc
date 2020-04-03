package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.LoadView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import model.services.DepartmentService;

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
		loadView.loadView("/gui/DepartmentList.fxml",(DepartmentListController controller) ->{
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView.loadView("/gui/About.fxml",x -> {});
		System.out.println("onMenuItemAboutAction");
	}
	
	public void initialize(URL url, ResourceBundle rb) {	
	}
	
	

}
