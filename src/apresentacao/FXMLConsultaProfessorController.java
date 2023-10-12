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
import negocio.Professor;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.Filter;
import pieduca.Sys;

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
    @FXML
    private TableColumn<Professor, String> disciplina;
    
    @FXML
    private JFXButton btnAdicionar;
    @FXML
    private JFXButton btnDeletar;
    @FXML
    private JFXButton btnAtualizarRegistro;
    
    private ObservableList<Professor> obsProfessores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Professor, Integer>("idProfessor"));
        nome.setCellValueFactory(new PropertyValueFactory<Professor, String>("nome"));
        area_espec.setCellValueFactory(new PropertyValueFactory<Professor, String>("areaEspecializacao"));
        contato.setCellValueFactory(new PropertyValueFactory<Professor, String>("contato"));
        disciplina.setCellValueFactory(new PropertyValueFactory<Professor, String>("disciplina"));
        
        List<Professor> profs = DaoFactory.criarProfessorDao().readAll(new Filter<Professor>() {
            @Override
            public boolean isAccept(Professor record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        obsProfessores = FXCollections.observableArrayList(profs);
        
        tabela.setItems(obsProfessores);
        
        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() != 1) {
            btnDeletar.setDisable(true);
            btnAdicionar.setDisable(true);
            btnAtualizarRegistro.setDisable(true);
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
        obsProfessores.clear();
        
        id.setCellValueFactory(new PropertyValueFactory<Professor, Integer>("idProfessor"));
        nome.setCellValueFactory(new PropertyValueFactory<Professor, String>("nome"));
        area_espec.setCellValueFactory(new PropertyValueFactory<Professor, String>("areaEspecializacao"));
        contato.setCellValueFactory(new PropertyValueFactory<Professor, String>("contato"));
        disciplina.setCellValueFactory(new PropertyValueFactory<Professor, String>("disciplina"));
        
        List<Professor> profs = DaoFactory.criarProfessorDao().readAll(new Filter<Professor>() {
            @Override
            public boolean isAccept(Professor record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        obsProfessores = FXCollections.observableArrayList(profs);
        
        tabela.setItems(obsProfessores);
    }
    
    public void cadastroProfessor (MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroProfessores.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroProfessoresController controller = loader.getController();
        
        controller.setBtnCadastrar(Boolean.FALSE);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void atualizarProfessor (MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroProfessores.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroProfessoresController controller = loader.getController();
        
        controller.setBtnAtualizar(Boolean.FALSE);
        
        Professor prof = tabela.getSelectionModel().getSelectedItem();
        
        if (prof != null) {
            
            controller.preencherCampos(prof);
            
            controller.setProfessorSelecionado(prof);
            
    //      stage.initStyle(StageStyle.UNDECORATED);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
            
        } else {
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Selecione um item da tabela");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
            
        }
    }
    
    public void deletar (MouseEvent event) throws Exception {
        Professor prof = tabela.getSelectionModel().getSelectedItem();
        
        if (prof != null) {
            prof.setDeletado(Boolean.TRUE);

            DaoFactory.criarProfessorDao().delete(prof.getIdProfessor());

            refresh();

            Professor p = DaoFactory.criarProfessorDao().read(prof.getIdProfessor());
            
            if (p.getDeletado() == Boolean.TRUE) {
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
