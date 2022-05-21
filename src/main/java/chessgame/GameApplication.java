package chessgame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import org.tinylog.Logger;

import static javafx.scene.media.AudioClip.INDEFINITE;

public class GameApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("The application has started.");
        AudioClip audioClip = new AudioClip(getClass().getResource("/music.wav").toString());
        audioClip.setCycleCount(INDEFINITE);
        audioClip.play();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/opening.fxml"));
        stage.setTitle("Knight Chess Game");
        stage.setResizable(false);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
