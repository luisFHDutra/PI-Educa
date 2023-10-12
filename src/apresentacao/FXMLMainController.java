package apresentacao;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Professor;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.NotFoundException;
import pieduca.Sys;


public class FXMLMainController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private Label lbPermissao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Professor prof = DaoFactory.criarProfessorDao().read(Sys.getInstance().getUser().getId());
            
            lbUsuario.setText(prof.getNome());
            lbPermissao.setText(prof.getUsuario().getPermissao().getNome());
        } catch (NotFoundException ex) {
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Nenhum usuário logado");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
            
        }
        
    }  
    
    public void consultaProfessor (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLConsultaProfessor.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void consultaAluno (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLConsultaAluno.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void loginoff (MouseEvent event) throws Exception {
        Sys.getInstance().setUser(null);
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLLogin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
}
