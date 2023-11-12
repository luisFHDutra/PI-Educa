package apresentacao;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Turma;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.TurmaDao;

public class FXMLCadastroTurmaController implements Initializable {

    @FXML
    private JFXButton btnAtualizar;
    @FXML
    private JFXButton btnCadastrar;
    @FXML
    private JFXTextField tfNome;
    @FXML
    private JFXTextField tfAno;
    
    private Turma turma;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        btnCadastrar.setDisable(true);
        btnAtualizar.setDisable(true);
    }    
    
    public void setBtnAtualizar (Boolean flag) {
        this.btnAtualizar.setDisable(flag);
    }
    
    public void setBtnCadastrar (Boolean flag) {
        this.btnCadastrar.setDisable(flag);
    }
    
    public void setTurmaSelecionado (Turma objeto) {
        this.turma = objeto;
    }
    
    public void preencherCampos (Turma objeto) {
        tfNome.setText(objeto.getNome());
        tfAno.setText(objeto.getAnoLetivo());
    }
    
    public void limparCampos() {
        limpar();
    }
    
    private void limpar() {
        tfNome.setText(null);
        tfAno.setText(null);
    }
    
    public void voltar (MouseEvent event) throws Exception {
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
    
    public void cadastrarTurma (MouseEvent event) throws Exception {
        
        String nome = tfNome.getText();
        String ano = tfAno.getText();
        
        if (nome.isEmpty() || ano.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos");
            alerta.showAndWait();
        }
        else {
            TurmaDao turmaDao = new TurmaDao();
            int id = turmaDao.maxId();
            
            Turma turma = new Turma(id, nome, ano);

            DaoFactory.criarTurmaDao().create(turma);
            
            limpar();
            
            if (id < turmaDao.maxId()) {
                check();
            } else {
                error();
            }
        }
    
    }
    
    public void atualizarTurma (MouseEvent event) throws Exception {
        
        String nome = tfNome.getText();
        String ano = tfAno.getText();
        
        if (!nome.isEmpty()) {
            this.turma.setNome(nome);
        }
        
        if (!ano.isEmpty()) {
            this.turma.setAnoLetivo(ano);
        }
        DaoFactory.criarTurmaDao().update(this.turma);
        
        check();
    }
    
    private void error(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        icon.setSize("2em");
        icon.setStyle(" -fx-fill: red ;");
        
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.graphic(icon);
        notification.text("Erro ao cadastrar usuário ou professor");
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
        notification.graphic(icon);
        notification.text("Operação realizada com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
}
