package apresentacao;

import auth.Authenticator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import negocio.Disciplina;
import negocio.Permissao;
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
    @FXML
    private JFXComboBox cbPermissoes;
    @FXML
    private JFXComboBox cbDisciplina;
    
    @FXML
    private JFXButton btnAtualizar;
    @FXML
    private JFXButton btnCadastrar;
    
    private ObservableList<Permissao> obsPermissoes;
    
    private ObservableList<Disciplina> obsDisciplinas;
    
    private Professor professor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Permissao> permissoes = DaoFactory.criarPermissaoDao().readAll();
        
        obsPermissoes = FXCollections.observableArrayList(permissoes);
       
        cbPermissoes.setItems(obsPermissoes);
        
        List<Disciplina> disciplinas = DaoFactory.criarDisciplinaDao().readAll();
        
        obsDisciplinas = FXCollections.observableArrayList(disciplinas);
       
        cbDisciplina.setItems(obsDisciplinas);
        
        btnCadastrar.setDisable(true);
        btnAtualizar.setDisable(true);
    }   
    
    public void voltar (MouseEvent event) throws Exception {
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
 
    public void cadastrarProfessor (MouseEvent event) throws Exception {
        String senha = tfPassword.getText();
        String nome = tfNome.getText();
        String area = tfAreaEspecializacao.getText();
        String contato = tfContato.getText();
        Permissao permissao = (Permissao) cbPermissoes.getSelectionModel().getSelectedItem();
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        
        if (senha.isEmpty() || nome.isEmpty() || area.isEmpty() || !checkEmailInput()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos corretamente");
            alerta.showAndWait();
        } else {
            ProfessorDao professordao = new ProfessorDao();
            int id = professordao.maxId();

            Authenticator auth = new Authenticator();
            String hashCode = auth.generateHashCode(senha);

            Usuario user = new Usuario(id, hashCode, permissao, false);
            
            Professor prof = new Professor(id, nome, area, contato, user, false, disciplina);

            DaoFactory.criarProfessorDao().create(prof);

            limpar();
            
            if (id < professordao.maxId()) {
                check();
            } else {
                error();
            }
        }
        
    }
    
    public void setProfessorSelecionado (Professor objeto) {
        this.professor = objeto;
    }
    
    public void setBtnAtualizar (Boolean flag) {
        this.btnAtualizar.setDisable(flag);
    }
    
    public void setBtnCadastrar (Boolean flag) {
        this.btnCadastrar.setDisable(flag);
    }
    
    public void preencherCampos (Professor objeto) {
        tfNome.setText(objeto.getNome());
        tfAreaEspecializacao.setText(objeto.getAreaEspecializacao());
        tfContato.setText(objeto.getContato());
    }
    
    public void atualizarProfessor (MouseEvent event) throws Exception {
        String senha = tfPassword.getText();
        String nome = tfNome.getText();
        String area = tfAreaEspecializacao.getText();
        String contato = tfContato.getText();
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        Permissao permissao = (Permissao) cbPermissoes.getSelectionModel().getSelectedItem();
        
        if (!nome.isEmpty()) {
            this.professor.setNome(nome);
        }
        
        if (!area.isEmpty()) {
            this.professor.setAreaEscpecializacao(area);
        }
        
        if (!contato.isEmpty()) {
            
            if (checkEmailInput()) {
                this.professor.setContato(contato);
            }
            
        }
        
        if (disciplina != null) {
            this.professor.setDisciplina(disciplina);
        }
        
        if (!senha.isEmpty()) {
            Authenticator auth = new Authenticator();
            String hashCode = auth.generateHashCode(senha);
            
            Usuario user = this.professor.getUsuario();
            
            user.setHashCode(hashCode);
            
            this.professor.setUsuario(user);
        }
        
        if (permissao != null) {
            Usuario user = this.professor.getUsuario();
            
            user.setPermissao(permissao);
            
            this.professor.setUsuario(user);
        }
        
        DaoFactory.criarProfessorDao().update(this.professor);
        
        check();
    }
    
    public void limparCampos() {
        limpar();
    }
    
    private void limpar() {
        tfNome.setText(null);
        tfContato.setText(null);
        tfAreaEspecializacao.setText(null);
        tfPassword.setText(null);
        cbDisciplina.setValue(null);
        cbPermissoes.setValue(null);
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
        notification.text("Operação realizada com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
     }
     
     private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}(\\.[A-Z]{2,})?$", Pattern.CASE_INSENSITIVE
    );

    private boolean checkEmailInput() {
        String email = tfContato.getText();
        
        if (!email.isEmpty()) {
            if (EMAIL_PATTERN.matcher(email).matches()) {
                tfContato.setStyle("-fx-border-color: blue;");

                return true;
            } else {
                tfContato.setStyle("-fx-border-color: red;");

                Notifications notification = Notifications.create();
                notification.title("Error");
                notification.text("Formato do e-mail incorreto");
                notification.hideAfter(Duration.seconds(3));
                notification.position(Pos.BOTTOM_CENTER);
                notification.show();
                
                return false;
            }
        } else {
            return false;
        }
        
    }
}
