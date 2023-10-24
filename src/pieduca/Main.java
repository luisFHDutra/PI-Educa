package pieduca;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image("/imagens/book-icon.png");
        
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLLogin.fxml"));
        Scene scene = new Scene(root);
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    }
