package apresentacao;

import auth.Authenticator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import negocio.Professor;
import negocio.Usuario;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import pieduca.Sys;

public class FXMLLoginController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private JFXPasswordField tfPassword;
    @FXML
    private JFXTextField tfUsuario;
    @FXML
    private JFXButton btnLogin;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image img = new Image("/imagens/icon-user.png");
        imgLogo.setImage(img);
        
        FontAwesomeIconView lockIcon = new FontAwesomeIconView(FontAwesomeIcon.LOCK);
        lockIcon.setSize("2em");
        btnLogin.setGraphic(lockIcon);
    }    
    
    public void login (MouseEvent event) throws Exception {
        if (tfUsuario.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos");
            alerta.showAndWait();
        } else {
            int id = Integer.valueOf(tfUsuario.getText());
            
            Professor professor = DaoFactory.criarProfessorDao().read(id);
            
            if (!professor.getDeletado()) {
                Usuario user = DaoFactory.criarUsuarioDao().read(id);

                Authenticator auth = new Authenticator(user, id, tfPassword.getText());

                if (auth.isRight()){
                    Sys.getInstance().setUser(user);
                    Image icon = new Image("/imagens/book-icon.png");

                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLMain.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    stage.getIcons().add(icon);

        //            stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                    ((Node)event.getSource()).getScene().getWindow().hide();
                    check();
                } else {
                    error();
                }
        
            } else {
                error();
            }
        }
    }
    
    private void error(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        icon.setSize("2em");
        icon.setStyle(" -fx-fill: red ;");
        
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Usuário ou senha incorretos");
        notification.graphic(icon);
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
    
    private void check(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        icon.setSize("2em");
        icon.setStyle(" -fx-fill: green ;");
        
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.text("Bem vindo");
        notification.graphic(icon);
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
    
    public void checkInput(KeyEvent event) {
        
        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")){
            event.consume();
            tfUsuario.setStyle("-fx-border-color: red");
        } else {
            tfUsuario.setStyle("-fx-border-color: blue");
        }
        
    }
}
