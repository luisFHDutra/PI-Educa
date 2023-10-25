package apresentacao;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Aluno;
import negocio.Turma;
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
    
    @FXML
    private JFXButton btnAtualizar;
    @FXML
    private JFXButton btnCadastrar;
    
    @FXML
    private JFXComboBox cbTurma;
    private ObservableList<Turma> obsTurmas;
    
    private Aluno aluno;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Turma> turmas = DaoFactory.criarTurmaDao().readAll();
        
        obsTurmas = FXCollections.observableArrayList(turmas);
       
        cbTurma.setItems(obsTurmas);
        
        btnCadastrar.setDisable(true);
        btnAtualizar.setDisable(true);
    }    
    
    public void setBtnAtualizar (Boolean flag) {
        this.btnAtualizar.setDisable(flag);
    }
    
    public void setBtnCadastrar (Boolean flag) {
        this.btnCadastrar.setDisable(flag);
    }
    
    public void setAlunoSelecionado (Aluno objeto) {
        this.aluno = objeto;
    }
    
    public void preencherCampos (Aluno objeto) {
        tfNome.setText(objeto.getNome());
        tfFiliacao.setText(objeto.getFiliacao());
        tfRg.setText(objeto.getRg());
    }
    
    public void voltar (MouseEvent event) throws Exception {
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
    
    public void cadastrarAluno (MouseEvent event) throws Exception {
        String data = "";
        try {
            data = tfDataNascimento.getValue().toString();
        } catch (NullPointerException ex) {
            data = "";
        }
        
        String nome = tfNome.getText();
        String filiacao = tfFiliacao.getText();
        String rg = tfRg.getText();
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        
        if (nome.isEmpty() || rg.isEmpty() || filiacao.isEmpty() || data.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos");
            alerta.showAndWait();
        } 
        else if (rg.length() != 10) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setHeaderText(null);
            alerta.setContentText("RG inválido");
            alerta.showAndWait();
        }
        else {
            AlunoDao alunodao = new AlunoDao();
            int id = alunodao.maxId();
            
            Aluno aluno = new Aluno(id, nome, data, rg, filiacao, Boolean.FALSE, turma);

            DaoFactory.criarAlunoDao().create(aluno);
            
            limpar();
            
            if (id < alunodao.maxId()) {
                check();
            } else {
                error();
            }
        }
    
    }
    
    public void atualizarAluno (MouseEvent event) throws Exception {
        String data = "";
        try {
            data = tfDataNascimento.getValue().toString();
        } catch (NullPointerException ex) {
            data = "";
        }
        
        String nome = tfNome.getText();
        String filiacao = tfFiliacao.getText();
        String rg = tfRg.getText();
        
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        
        if (!nome.isEmpty()) {
            this.aluno.setNome(nome);
        }
        
        if (!filiacao.isEmpty()) {
            this.aluno.setFiliacao(filiacao);
        }
        
        if (!rg.isEmpty()) {
            
            if (rg.length() != 10) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setHeaderText(null);
                alerta.setContentText("RG inválido");
                alerta.showAndWait();
            } else {
                this.aluno.setRg(rg);
            }
            
        }
        
        if (!data.isEmpty()) {
            this.aluno.setDataNascimento(data);
        }
        
        if (turma != null) {
            this.aluno.setTurma(turma);
        }
        
        DaoFactory.criarAlunoDao().update(this.aluno);
        
        check();
    }
    
    public void limparCampos() {
        limpar();
    }
    
    private void limpar() {
        tfNome.setText(null);
        tfFiliacao.setText(null);
        tfRg.setText("");
        tfDataNascimento.setValue(null);
        cbTurma.setValue(null);
    }
    
    private void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Erro ao cadastrar usuário ou professor");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }

    private void check(){
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.text("Operação realizada com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
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

        tfRg.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                tfRg.setText(oldValue); // Reverta para o valor anterior se exceder o tamanho máximo
            }
        });
        
    }
}
