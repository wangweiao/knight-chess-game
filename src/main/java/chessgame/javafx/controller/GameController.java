package chessgame.javafx.controller;

import chessgame.result.GameResult;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;
import util.Stopwatch;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    private boolean isBlackTurn;

    @FXML
    private Label stopwatchLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Label stepsLabel;

    @FXML
    private Circle circle;

    private Stopwatch stopwatch = new Stopwatch();

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

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

    @FXML
    public void initialize() {
        Logger.info("Board initialization initiated.");
        state = new GameState();
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        stopwatch.start();
        startTime = Instant.now();
        stepsLabel.textProperty().bind(steps.asString());
        chessSelected = false;
        isBlackTurn = true;
        loadImages();
        populateGrid();
        displayGameState();
        circle.setFill(Color.BLACK);
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
            if (isBlackTurn && board[row][col] != 'b') {
                messageLabel.setText("Invalid operation!");
                Logger.debug("The operation is invalid!");
                return;
            } else if (!isBlackTurn && board[row][col] != 'w') {
                messageLabel.setText("Invalid operation!");
                Logger.debug("The operation is invalid!");
                return;
            }
            originalRow = row;
            originalCol = col;
            chessSelected = true;
            messageLabel.setText("");
            isBlackTurn = !isBlackTurn;
        } else if (state.isValidMovement(row, col, board[originalRow][originalCol]) && chessSelected && isCellEmpty) {
            Logger.debug("Destination ({}, {}) is selected.", row ,col);
            switchPosition(originalRow, originalCol, row, col);
            state.performMovement(row, col, board[originalRow][originalCol]);
            steps.set(steps.get() + 1);
            chessSelected = false;
            messageLabel.setText("");
            if (isBlackTurn) {
                circle.setFill(Color.BLACK);
            } else {
                circle.setFill(Color.WHITE);
            }
        } else {
            Logger.debug("The operation is invalid!");
            messageLabel.setText("Invalid operation!");
        }
    }

    private void switchPosition(int originalRow, int originalCol, int row, int col) {
        ImageView original = (ImageView) grid.getChildren().get(originalRow * 8 + originalCol);
        ImageView destination = (ImageView) grid.getChildren().get(row * 8 + col);
        destination.setImage(original.getImage());
        original.setImage(knightImages[2]);
    }

    private void handleSolved() {
//        if (state.isAllFilled()) {
        if (steps.get() == 4) {
            Logger.info("Player {} has won the game in {} steps.", playerName, steps.get() / 2);
            messageLabel.setText(playerName + " Won!");
            stopwatch.stop();
            GameResult gameResult = createGameResult();



        }
    }

    private GameResult createGameResult() {
        return GameResult.builder()
                .name(playerName)
                .step(steps.get())
                .duration(Duration.between(startTime, Instant.now()).getSeconds())
                .time(ZonedDateTime.now().format(dateTimeFormatter))
                .build();
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
        initialize();
        stopwatch.stop();
        stopwatch.reset();
        stopwatch.start();
        steps.set(0);
        startTime = Instant.now();
        messageLabel.setText("");
        Logger.info("Reset operation is successfully completed.");
    }

}
