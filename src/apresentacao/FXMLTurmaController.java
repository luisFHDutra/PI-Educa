package apresentacao;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import negocio.Aluno;
import negocio.Turma;
import persistencia.DaoFactory;
import persistencia.Filter;
import pieduca.Sys;

public class FXMLTurmaController implements Initializable {

    @FXML
    private TableView<Turma> tabela;
    @FXML
    private TableColumn<Turma, String> turma;
    @FXML
    private TableColumn<Turma, String> ano;
    @FXML
    private TableColumn<Turma, Integer> alunos;
    @FXML
    private JFXButton btnAdicionar;
    @FXML
    private JFXButton btnAtualizarItem;

    private ObservableList<Turma> obsTurmas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        turma.setCellValueFactory(new PropertyValueFactory<Turma, String>("nome"));
        ano.setCellValueFactory(new PropertyValueFactory<Turma, String>("anoLetivo"));
        alunos.setCellValueFactory(new PropertyValueFactory<Turma, Integer>("quantidadeAlunos"));
        
        List<Turma> turmas = obterTurmas();
        
        obsTurmas = FXCollections.observableArrayList(turmas);
        
        tabela.setItems(obsTurmas);
        
        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() != 1) {
            btnAdicionar.setDisable(true);
            btnAtualizarItem.setDisable(true);
        }
    }    
    
    private List<Turma> obterTurmas() {
        
        List<Turma> turmas = DaoFactory.criarTurmaDao().readAll();
        
        List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
            @Override
            public boolean isAccept(Aluno record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        for (Aluno aluno : alunos) {
        Turma turmaDoAluno = aluno.getTurma();

            if (turmaDoAluno != null) {
                // Encontre a instância de Turma correspondente na lista de turmas
                for (Turma turma : turmas) {
                    if (turma.getIdTurma() == turmaDoAluno.getIdTurma()) {
                        turma.incrementarQuantidadeAlunos();
                        break; // Uma vez que a turma correspondente foi encontrada, podemos sair do loop interno
                    }
                }
            }
        }
        
        return turmas;
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
}
