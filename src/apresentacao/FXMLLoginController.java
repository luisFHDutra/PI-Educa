package apresentacao;

import auth.Authenticator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import negocio.Usuario;
import org.controlsfx.control.Notifications;

public class FXMLLoginController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private JFXPasswordField tfPassword;
    @FXML
    private JFXTextField tfUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image img = new Image("/imagens/avatar-batman.png");
        imgLogo.setImage(img);
    }    
    
    public void login (MouseEvent event) throws Exception {
        // teste
        Usuario userAdmin = new Usuario(1, "admin", "$2a$10$dtAPkculHQqRuDd6Znn0KOQQIa61Jlt0iL73ZbmyPm3gD6VSfGcQa");
        
        ArrayList<Usuario> users = new ArrayList();
        users.add(userAdmin);
        
        Authenticator auth = new Authenticator(users, tfUsuario.getText(), tfPassword.getText());
        
//        System.out.println(auth.generateHashCode("admin"));

        if (auth.isRight()){
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLMain.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
//            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
            check();
        } else {
            error();
        }
    }
    
    public void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Usuário ou senha incorretos");
        notification.hideAfter(Duration.seconds(5));
        notification.position(Pos.BASELINE_RIGHT);
        notification.show();
    }
    
    public void check(){
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.text("Bem vindo");
        notification.hideAfter(Duration.seconds(5));
        notification.position(Pos.BASELINE_RIGHT);
        notification.show();
    }
}
