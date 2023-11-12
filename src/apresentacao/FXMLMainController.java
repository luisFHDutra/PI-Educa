package apresentacao;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
    @FXML
    private Label lbUser;
    
    @FXML
    private JFXButton btnNota;
    @FXML
    private JFXButton btnAluno;
    @FXML
    private JFXButton btnProfessor;
    @FXML
    private JFXButton btnTurma;
    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXButton btnPresenca;
    @FXML
    private JFXButton btnBoletim;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Professor prof = DaoFactory.criarProfessorDao().read(Sys.getInstance().getUser().getId());
            
            lbUsuario.setText(prof.getNome());
            lbPermissao.setText(prof.getUsuario().getPermissao().getNome());
            
            FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
            userIcon.setSize("2em");
            lbUser.setGraphic(userIcon);
            
            FontAwesomeIconView userIcon1 = new FontAwesomeIconView(FontAwesomeIcon.USER);
            userIcon1.setSize("2em");
            btnProfessor.setGraphic(userIcon1);
            
            FontAwesomeIconView lockIcon = new FontAwesomeIconView(FontAwesomeIcon.LOCK);
            lockIcon.setSize("2em");
            btnLogin.setGraphic(lockIcon);
            
            FontAwesomeIconView alunoIcon = new FontAwesomeIconView(FontAwesomeIcon.GRADUATION_CAP);
            alunoIcon.setSize("2em");
            btnAluno.setGraphic(alunoIcon);
            
            FontAwesomeIconView preseIcon = new FontAwesomeIconView(FontAwesomeIcon.CALENDAR_CHECK_ALT);
            preseIcon.setSize("2em");
            btnPresenca.setGraphic(preseIcon);
            
            FontAwesomeIconView bolIcon = new FontAwesomeIconView(FontAwesomeIcon.FILE_TEXT);
            bolIcon.setSize("2em");
            btnBoletim.setGraphic(bolIcon);
            
            FontAwesomeIconView notaIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            notaIcon.setSize("2em");
            btnNota.setGraphic(notaIcon);
            
            FontAwesomeIconView turmaIcon = new FontAwesomeIconView(FontAwesomeIcon.USERS);
            turmaIcon.setSize("2em");
            btnTurma.setGraphic(turmaIcon);
            
        } catch (NotFoundException ex) {
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Nenhum usuário logado");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
            
        }
        
    }  
    
    public void consultaProfessor (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLConsultaProfessor.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void consultaAluno (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLConsultaAluno.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void nota (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLNota.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void turma (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLTurma.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void presenca (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLPresenca.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void boletim (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLBoletim.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void loginoff (MouseEvent event) throws Exception {
        Sys.getInstance().setUser(null);
        
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLLogin.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
}
