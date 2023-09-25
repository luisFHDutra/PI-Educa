package apresentacao;

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
import negocio.Professor;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.ProfessorDao;

public class FXMLConsultaProfessorController implements Initializable {

    @FXML
    private TableView<Professor> tabela;
    @FXML
    private TableColumn<Professor, Integer> id;
    @FXML
    private TableColumn<Professor, String> nome;
    @FXML
    private TableColumn<Professor, String> area_espec;
    @FXML
    private TableColumn<Professor, String> contato;

    private ObservableList<Professor> obsProfessores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Professor, Integer>("idProfessor"));
        nome.setCellValueFactory(new PropertyValueFactory<Professor, String>("nome"));
        area_espec.setCellValueFactory(new PropertyValueFactory<Professor, String>("areaEspecializacao"));
        contato.setCellValueFactory(new PropertyValueFactory<Professor, String>("contato"));
        
        List<Professor> profs = DaoFactory.criarProfessorDao().readAll();
        obsProfessores = FXCollections.observableArrayList(profs);
        
        tabela.setItems(obsProfessores);
        
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
        obsProfessores.clear();
        
        id.setCellValueFactory(new PropertyValueFactory<Professor, Integer>("idProfessor"));
        nome.setCellValueFactory(new PropertyValueFactory<Professor, String>("nome"));
        area_espec.setCellValueFactory(new PropertyValueFactory<Professor, String>("areaEspecializacao"));
        contato.setCellValueFactory(new PropertyValueFactory<Professor, String>("contato"));
        
        List<Professor> profs = DaoFactory.criarProfessorDao().readAll();
        obsProfessores = FXCollections.observableArrayList(profs);
        
        tabela.setItems(obsProfessores);
    }
    
    public void cadastroProfessor (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLCadastroProfessores.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void deletar (MouseEvent event) throws Exception {
        Professor prof = tabela.getSelectionModel().getSelectedItem();
        
        ProfessorDao professor = new ProfessorDao();
        int id = professor.maxId();
        
        DaoFactory.criarProfessorDao().delete(prof.getIdProfessor());
        
        refresh();
        
        if (id > professor.maxId()) {
            check();
        } else {
            error();
        }
    }
    
    private void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Erro ao deletar usuário ou professor");
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
}
