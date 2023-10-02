package apresentacao;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Aluno;
import org.controlsfx.control.Notifications;
import persistencia.AlunoDao;
import persistencia.DaoFactory;
import pieduca.Sys;

public class FXMLConsultaAlunoController implements Initializable {

    @FXML
    private TableView<Aluno> tabela;
    @FXML
    private TableColumn<Aluno, Integer> id;
    @FXML
    private TableColumn<Aluno, String> nome;
    @FXML
    private TableColumn<Aluno, String> nascimento;
    @FXML
    private TableColumn<Aluno, String> filiacao;
    @FXML
    private TableColumn<Aluno, String> rg;

    @FXML
    private JFXButton btnAdicionar;
    @FXML
    private JFXButton btnDeletar;
    
    private ObservableList<Aluno> obsAlunos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Aluno, Integer>("idAluno"));
        nome.setCellValueFactory(new PropertyValueFactory<Aluno, String>("nome"));
        nascimento.setCellValueFactory(new PropertyValueFactory<Aluno, String>("dataNascimento"));
        filiacao.setCellValueFactory(new PropertyValueFactory<Aluno, String>("filiacao"));
        rg.setCellValueFactory(new PropertyValueFactory<Aluno, String>("rg"));
        
        List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll();
        obsAlunos = FXCollections.observableArrayList(alunos);
        
        tabela.setItems(obsAlunos);
        
        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() != 1) {
            btnDeletar.setDisable(true);
            btnAdicionar.setDisable(true);
        }
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
    
    public void refreshTabela (MouseEvent event) throws Exception {
        refresh();
    }
    
    private void refresh() {
        obsAlunos.clear();
        
        id.setCellValueFactory(new PropertyValueFactory<Aluno, Integer>("idAluno"));
        nome.setCellValueFactory(new PropertyValueFactory<Aluno, String>("nome"));
        nascimento.setCellValueFactory(new PropertyValueFactory<Aluno, String>("dataNascimento"));
        filiacao.setCellValueFactory(new PropertyValueFactory<Aluno, String>("filiacao"));
        rg.setCellValueFactory(new PropertyValueFactory<Aluno, String>("rg"));
        
        List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll();
        obsAlunos = FXCollections.observableArrayList(alunos);
        
        tabela.setItems(obsAlunos);
    }
    
     public void deletar (MouseEvent event) throws Exception {
        Aluno aluno = tabela.getSelectionModel().getSelectedItem();
        
        if (aluno != null) {
            AlunoDao alunodao = new AlunoDao();
            int id = alunodao.maxId();

            DaoFactory.criarAlunoDao().delete(aluno.getIdAluno());

            refresh();

            if (id > alunodao.maxId()) {
                check();
            } else {
                error();
            }
        } else {
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Selecione um item da tabela");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
            
        }
               
    }
    
    private void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Erro ao deletar aluno");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }

    private void check(){
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.text("Delete realizado com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void cadastroAluno (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLCadastroAluno.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
}
