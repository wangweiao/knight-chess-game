package chessgame.javafx;

import chessgame.state.GameState;
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

    private GameState state = new GameState();

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void initialization() {
        Logger.info("Board initialization initiated.");
        state = new GameState();
        loadImages();
        populateGrid();
    }

    private void populateGrid() {
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var col = 0; col < grid.getColumnCount(); col++) {
                var Cell = createCell(row, col);
                grid.add(Cell, col, row);
            }
        }
    }

    private StackPane createCell(int row, int col) {
        var cell = new StackPane();
        cell.setOnMouseClicked(this::handleMouseClick);
        cell.getStyleClass().add("cell");

        for (var i = 0; i < 2; i++) {
            var knightView = new ImageView(knightImages[i]);
            knightView.setFitHeight(35);
            knightView.setFitWidth(35);
            cell.getChildren().add(knightView);
        }
        return cell;
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
        var col = GridPane.getColumnIndex(source);
        Logger.debug("Click on square ({}, {})", row, col);
    }

    public void handleEndButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed.", buttonText);
        Logger.info("The game ends.");
        Logger.info("Display high score scene.");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
