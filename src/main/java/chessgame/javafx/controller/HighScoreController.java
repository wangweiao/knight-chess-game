package chessgame.javafx.controller;

import chessgame.javafx.GameApplication;
import chessgame.result.GameResult;
import chessgame.result.GameResultDao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Controls the appearance and behaviours of the high score result scene.
 */
public class HighScoreController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, Integer> duration;

    @FXML
    private TableColumn<GameResult, String> created;

    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private void initialize() {
        Logger.debug("Loading high score table.");
        Jdbi jdbi = Jdbi.create("jdbc:oracle:thin:@oracle.inf.unideb.hu:1521:ora19c", "U_H104TX", "kalvinter");
        jdbi.installPlugin(new SqlObjectPlugin());
        List<GameResult> winnerResults = jdbi.withExtension(GameResultDao.class, GameResultDao::listTopFiveResults);
        player.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        steps.setCellValueFactory(new PropertyValueFactory<>("stepsByPlayer"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("gameEndTime"));
        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(winnerResults);
        highScoreTable.setItems(observableResult);
        List<GameResult> gameResults = jdbi.withExtension(GameResultDao.class, dao -> {
            return dao.listGameResults();
        });
        gameResults.forEach(Logger::debug);
    }

    @FXML
    private void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed.", ((Button) actionEvent.getSource()).getText());
        Logger.info("Loading opening scene.");
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleExitButton(ActionEvent actionEvent) {
        Logger.debug("{} is pressed.", ((Button) actionEvent.getSource()).getText());
        Logger.info("Application terminated.");
        GameApplication.audioClip.stop();
        Platform.exit();
        System.exit(0);
    }

}