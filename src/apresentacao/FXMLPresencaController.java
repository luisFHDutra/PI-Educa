package apresentacao;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import negocio.AlunoDisciplina;
import negocio.Disciplina;
import negocio.Professor;
import negocio.Turma;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.Filter;
import persistencia.NotFoundException;
import pieduca.Sys;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import negocio.Aluno;
import negocio.Presenca;

public class FXMLPresencaController implements Initializable {

    @FXML
    private TableView<AlunoDisciplina> tabela;
    @FXML
    private TableColumn<AlunoDisciplina, String> aluno;
    @FXML
    private TableColumn<AlunoDisciplina, String> presenca;
    
    @FXML
    private JFXDatePicker tfData;

    @FXML
    private JFXComboBox cbTurma;
    private ObservableList<Turma> obsTurma;
    
    @FXML
    private JFXComboBox cbDisciplina;
    private ObservableList<Disciplina> obsDisciplina;
    
    @FXML
    private HBox iconContainer;
    
    private ObservableList<AlunoDisciplina> obsAlunos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
        refreshIcon.setSize("2em");
        refreshIcon.getStyleClass().add("icon");

        refreshIcon.setStyle(" -fx-cursor: hand ;"
                + "-glyph-size:28px;"
                + "-fx-fill:#1aa7ec;"
        );
        
        EventHandler<MouseEvent> refreshHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    refreshTabela(event);
                } catch (Exception ex) {
                    Notifications notification = Notifications.create();
                    notification.title("Error");
                    notification.text("Erro ao deletar usuário ou professor");
                    notification.hideAfter(Duration.seconds(3));
                    notification.position(Pos.BOTTOM_CENTER);
                    notification.show();
                }
            }
        };
        refreshIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, refreshHandler);
        
        iconContainer.getChildren().add( refreshIcon);
        
        List<Turma> turmas = DaoFactory.criarTurmaDao().readAll();
        
        obsTurma = FXCollections.observableArrayList(turmas);
       
        cbTurma.setItems(obsTurma);
        
        Professor professor = null;
        try {
            professor = DaoFactory.criarProfessorDao().read(Sys.getInstance().getUser().getId());
        } catch (NotFoundException ex) {
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Professor não encontrado");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
        }
        
        List<Disciplina> disciplinas = null;
        
        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() == 1) {
            
            disciplinas = DaoFactory.criarDisciplinaDao().readAll();
            
        } else {
            
            int idDisciplina = professor.getDisciplina().getIdDisciplina();
            
            disciplinas = DaoFactory.criarDisciplinaDao().readAll(new Filter<Disciplina>() {
                @Override
                public boolean isAccept(Disciplina record) {
                    return (record.getIdDisciplina() == idDisciplina);
                }
            });
        }
        
        obsDisciplina = FXCollections.observableArrayList(disciplinas);
       
        cbDisciplina.setItems(obsDisciplina);
        
        LocalDate dataAtual = LocalDate.now();
        
        tfData.setValue(dataAtual);
    }    
    
    public void voltarMain (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLMain.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void refreshTabela (MouseEvent event) throws Exception {
        refresh();
    }
    
    private void refresh() {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        
        if (turma != null && disciplina != null && tfData.getValue() != null) {
            String dataEscolhida = tfData.getValue().toString();
            
            aluno.setCellValueFactory(new PropertyValueFactory<AlunoDisciplina, String>("aluno"));
            
            presenca.setCellValueFactory(data -> {
            AlunoDisciplina alunoDisciplina = data.getValue();
            List<Presenca> presencas = alunoDisciplina.getPresencas();
            if (presencas.size() >= 1) {
                return new SimpleStringProperty(String.valueOf(presencas.get(0).getPresente()));
            }
                return new SimpleStringProperty("false");
            });
            
            List<AlunoDisciplina> alunos = DaoFactory.criarAlunoDisciplinaDao().readAll(new Filter<AlunoDisciplina>() {
                @Override
                public boolean isAccept(AlunoDisciplina record) {
                    return (record.getAluno().getDeletado() == Boolean.FALSE
                            && record.getAluno().getTurma().getIdTurma() == turma.getIdTurma());
                }
            });
            
            List<AlunoDisciplina> alunosFiltrados = alunos.stream()
                .filter(alunoDisciplina -> 
                    alunoDisciplina.getPresencas().stream()
                        .anyMatch(presenca -> presenca.getDisciplina().getIdDisciplina() == disciplina.getIdDisciplina() &&
                                presenca.getData() == dataEscolhida)
                )
            .collect(Collectors.toList());
            
            List<Aluno> alunoTodos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
                @Override
                public boolean isAccept(Aluno record) {
                    return (record.getDeletado() == Boolean.FALSE
                            && record.getTurma().getIdTurma() == turma.getIdTurma());
                }
            });
            
            ArrayList<Presenca> presencas = new ArrayList();
            presencas.add(new Presenca(null, dataEscolhida, false));
            
            ArrayList<AlunoDisciplina> todos = new ArrayList();
            
            for (Aluno a : alunoTodos) {
                AlunoDisciplina ad = new AlunoDisciplina(a, presencas, null);
                
                todos.add(ad);
            }
            
            HashSet<Integer> set = new HashSet<>();
            
            for (AlunoDisciplina a : alunosFiltrados) {
                set.add(a.getAluno().getIdAluno());
            }
            
            for (AlunoDisciplina a: todos) {
                if (!set.contains(a.getAluno().getIdAluno())) {
                    alunosFiltrados.add(a);
                }
            }
            
            obsAlunos = FXCollections.observableArrayList(alunosFiltrados);

            tabela.setItems(obsAlunos);
            
        } else {
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Selecione a turma e disciplina");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
        }
    }
}
