package chessgame.javafx.controller;

import chessgame.javafx.GameApplication;
import javafx.application.Platform;
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

    public void handleExitButton(ActionEvent actionEvent) {
        Logger.debug("{} is pressed.", ((Button) actionEvent.getSource()).getText());
        Logger.info("Application terminated.");
        GameApplication.audioClip.stop();
        Platform.exit();
        System.exit(0);
    }
}