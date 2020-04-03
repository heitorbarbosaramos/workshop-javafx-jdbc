package gui.util;

import java.io.IOException;

import application.Main;
import gui.DepartmentListController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class LoadView {

public synchronized void loadView(String absoluteName) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));		
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox);
			
		} catch (IOException e) {
			Alerts.showAlert("Io Exception", "Error Load View", e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

public synchronized void loadView2(String absoluteName) {
	
	try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));		
		VBox newVbox = loader.load();
		
		Scene mainScene = Main.getMainScene();
		VBox mainVbox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
		
		Node mainMenu = mainVbox.getChildren().get(0);
		mainVbox.getChildren().clear();
		mainVbox.getChildren().add(mainMenu);
		mainVbox.getChildren().addAll(newVbox);
		
		DepartmentListController controller = loader.getController();
		controller.setDepartmentService(new DepartmentService());
		controller.updateTableView();
		
	} catch (IOException e) {
		Alerts.showAlert("Io Exception", "Error Load View", e.getMessage(), AlertType.ERROR);
		e.printStackTrace();
	}
}
}
