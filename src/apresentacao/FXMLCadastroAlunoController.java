package apresentacao;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Aluno;
import org.controlsfx.control.Notifications;
import persistencia.AlunoDao;
import persistencia.DaoFactory;

public class FXMLCadastroAlunoController implements Initializable {

    @FXML
    private JFXTextField tfNome;
    @FXML
    private JFXTextField tfFiliacao;
    @FXML
    private JFXTextField tfRg;
    @FXML
    private JFXDatePicker tfDataNascimento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void voltar (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLConsultaAluno.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void cadastrarAluno (MouseEvent event) throws Exception {
        String nome = tfNome.getText();
        String filiacao = tfFiliacao.getText();
        String rg = tfRg.getText();
        String data = tfDataNascimento.getValue().toString();
        
        if (nome.isEmpty() || rg.isEmpty() || filiacao.isEmpty() || data.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos");
            alerta.showAndWait();
        } else {
            AlunoDao alunodao = new AlunoDao();
            int id = alunodao.maxId();
            
            Aluno aluno = new Aluno(id, nome, data, rg, filiacao);

            DaoFactory.criarAlunoDao().create(aluno);

            limpar();
            
            if (id < alunodao.maxId()) {
                check();
            } else {
                error();
            }
        }
        
    }
    
    public void limparCampos() {
        limpar();
    }
    
    private void limpar() {
        tfNome.setText(null);
        tfFiliacao.setText(null);
        tfRg.setText("");
        tfDataNascimento.setValue(null);
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
    
    public void checkInput(KeyEvent event) {
        
        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")){
            event.consume();
            tfRg.setStyle("-fx-border-color: red");
        } else {
            tfRg.setStyle("-fx-border-color: blue");
        }
        
    }
    
    public void checkNumeros(KeyEvent event) {
        
//        if (tfRg.getText().length() >= 10){
//            event.consume();
//            tfRg.setStyle("-fx-border-color: red");
//        }

        tfRg.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                tfRg.setText(oldValue); // Reverta para o valor anterior se exceder o tamanho máximo
            }
        });
        
    }
}
