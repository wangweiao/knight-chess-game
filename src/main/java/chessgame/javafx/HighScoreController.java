package chessgame.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class HighScoreController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed.", ((Button) actionEvent.getSource()).getText());
        Logger.info("Loading opening scene.");
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}