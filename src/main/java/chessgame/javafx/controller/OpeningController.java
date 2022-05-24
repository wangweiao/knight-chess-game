package chessgame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

import org.tinylog.Logger;

/**
 * Controls the appearance and behaviours of the opening page.
 */
public class OpeningController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    private TextField firstPlayerNameTextField;

    @FXML
    private ImageView background;

    @FXML
    private TextField secondPlayerNameTextField;

    @FXML
    private Label errorLabel;

    @FXML
    private void startAction(ActionEvent actionEvent) throws IOException {
        if (firstPlayerNameTextField.getText().isEmpty() || secondPlayerNameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter player names!");
        } else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setFirstPlayerName(firstPlayerNameTextField.getText());
            fxmlLoader.<GameController>getController().setSecondPlayerName(secondPlayerNameTextField.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/ui.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            Logger.info("The user's name is set to {}, loading game scene", firstPlayerNameTextField.getText());
        }
    }

}
