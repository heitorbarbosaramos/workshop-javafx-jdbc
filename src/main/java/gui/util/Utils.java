package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent event) {

		return (Stage) ((Node) event.getSource()).getScene().getWindow();		
		
	}
	
	public static Integer tryParseToInt (String str) {
		try {
			return Integer.parseInt(str);
		}catch (NumberFormatException e) {
			Alerts.showAlert("Number Exceptio", "Number Exceptio","Parameter passed cannot be a number", AlertType.INFORMATION);
			return null;
		}
	}
}
