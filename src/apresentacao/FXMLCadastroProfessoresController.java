package apresentacao;

import auth.Authenticator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Professor;
import negocio.Usuario;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.ProfessorDao;

public class FXMLCadastroProfessoresController implements Initializable {

    @FXML
    private JFXPasswordField tfPassword;
    @FXML
    private JFXTextField tfNome;
    @FXML
    private JFXTextField tfAreaEspecializacao;
    @FXML
    private JFXTextField tfContato;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
    public void voltarMain (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLMain.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
 
    public void cadastrarProfessor (MouseEvent event) throws Exception {
        
        ProfessorDao professor = new ProfessorDao();
        int id = professor.maxId();
        
        Authenticator auth = new Authenticator();
        String hashCode = auth.generateHashCode(tfPassword.getText());
        
        Usuario user = new Usuario(id, hashCode);
        
        Professor prof = new Professor(id, tfNome.getText(), tfAreaEspecializacao.getText(), tfContato.getText(), user);

        DaoFactory.criarProfessorDao().create(prof);
        
        if (id < professor.maxId()) {
            check();
        } else {
            error();
        }
        
    }
    
    private void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Erro ao cadastrar usuário ou professor");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
     }

     private void check(){
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.text("Cadastro realizado com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
     }
}
