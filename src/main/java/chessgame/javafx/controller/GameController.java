package chessgame.javafx.controller;

import chessgame.state.GameState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.tinylog.Logger;
import util.Stopwatch;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Stream;

public class GameController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private String playerName;

    private Image[] knightImages;

    private boolean chessSelected = false;

    private IntegerProperty steps = new SimpleIntegerProperty();

    private int originalRow;
    private int originalCol;

    private Instant startTime;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label stepsLabel;

    private Stopwatch stopwatch = new Stopwatch();

    @FXML
    private Button endButton;

    @FXML
    private Button resetButton;

    @FXML
    private GridPane grid;

    private GameState state = new GameState();

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void initialization() {
        Logger.info("Board initialization initiated.");
        state = new GameState();
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        stopwatch.start();
        stepsLabel.textProperty().bind(steps.asString());
        loadImages();
        populateGrid();
        displayGameState();
    }

    private void loadImages() {
        knightImages = Stream.of("black.jpg", "white.jpg", "red.jpg")
                .map(s -> "/images/" + s)
                .peek(s -> Logger.debug("Loading image resource {}", s))
                .map(Image::new)
                .toArray(Image[]::new);
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
        cell.setOnMouseClicked(this::handleClickOnCell);
        if ((row + col) % 2 == 0) {
            cell.getStyleClass().add("white-cell");
        } else {
            cell.getStyleClass().add("grey-cell");
        }
        return cell;
    }

    private void displayGameState() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageView view = (ImageView) grid.getChildren().get(i * 8 + j);
                view.getStyleClass().add("image");
                switch (state.getBoard()[i][j]) {
                    case 'b' -> view.setImage(knightImages[0]);
                    case 'w' -> view.setImage(knightImages[1]);
                    case 't' -> view.setImage(knightImages[2]);
                    default -> view.setImage(null);
                }
            }
        }
    }

    @FXML
    private void handleClickOnCell(MouseEvent event) {
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
        handleMovement(row, col);
        handleSolved();
    }

    private void handleMovement(int row, int col) {
        char[][] board = state.getBoard();
        boolean isCellEmpty = board[row][col] != 'w' && board[row][col] != 'b' && board[row][col] != 't';
        if (!chessSelected && (board[row][col] == 'b' || board[row][col] == 'w')) {
            Logger.debug("Chess ({}, {}) is picked", row, col);
            originalRow = row;
            originalCol = col;
            chessSelected = true;
            messageLabel.setText("");
        } else if (state.isValidMovement(row, col, board[originalRow][originalCol]) && chessSelected && isCellEmpty) {
            Logger.debug("Destination ({}, {}) is selected.", row ,col);
            switchPosition(originalRow, originalCol, row, col);
            state.performMovement(row, col, board[originalRow][originalCol]);
            steps.set(steps.get() + 1);
            chessSelected = false;
            messageLabel.setText("");
        } else {
            Logger.debug("The movement is invalid!");
            messageLabel.setText("The movement is invalid!");
        }
    }

    private void handleSolved() {
        if (state.isAllFilled()) {
            Logger.info("Player {} has won the game in {} steps.", playerName, steps.get() / 2);
        }
    }

    private void switchPosition(int originalRow, int originalCol, int row, int col) {
        ImageView original = (ImageView) grid.getChildren().get(originalRow * 8 + originalCol);
        ImageView destination = (ImageView) grid.getChildren().get(row * 8 + col);
        destination.setImage(original.getImage());
        original.setImage(knightImages[2]);
    }

    @FXML
    private void handleEndButton(ActionEvent actionEvent) throws IOException {
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

    @FXML
    private void handleResetButton(ActionEvent event) {
        Logger.info("Initiating reset operation.");
        state = new GameState();
        initialization();
        stopwatch.stop();
        stopwatch.reset();
        stopwatch.start();
        steps.set(0);
        startTime = Instant.now();
        messageLabel.setText("");
        Logger.info("Reset operation is successfully completed.");
    }

}
