package apresentacao;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import negocio.Professor;
import negocio.Usuario;
import persistencia.DaoFactory;
import persistencia.IDao;
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
        
        Usuario user = new Usuario(id, tfPassword.toString());
        
        Professor prof = new Professor(id, tfNome.toString(), tfAreaEspecializacao.toString(), tfContato.toString(), user);
        
    }
}
