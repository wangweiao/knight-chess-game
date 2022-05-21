package chessgame.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.stream.Stream;

public class GameController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private String playerName;
    private Image[] knightImages;

    @FXML
    private Button endButton;

    @FXML
    private GridPane grid;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void initialize() {


    }

    private void loadImages() {
        knightImages = Stream.of("black.jpg", "white.jpg")
                .map(s -> "/images/" + s)
                .peek(s -> Logger.debug("Loading image resource {}", s))
                .map(Image::new)
                .toArray(Image[]::new);
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var source = (Node) event.getSource();
        var row = GridPane.getRowIndex(source);
        if (row == null) {
            row = 0;
        }
        var col = GridPane.getColumnIndex(source);
        if (col == null) {
            col = 0;
        }
        Logger.debug("Click on square ({}, {})", row, col);
    }



    public void handleClickOnCell(MouseEvent mouseEvent) {
        int row = GridPane.getRowIndex((Node) mouseEvent.getSource());
        int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        Logger.debug("Cell ({}, {}) is clicked.", row, col);

    }

    public void handleEndButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed.", buttonText);
        if (buttonText.equals("End")) {
            Logger.info("The game ends.");
        }
        Logger.info("Display high score scene.");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
