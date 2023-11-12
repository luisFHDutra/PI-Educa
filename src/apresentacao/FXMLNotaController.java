package apresentacao;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Aluno;
import negocio.AlunoDisciplina;
import negocio.Disciplina;
import negocio.Nota;
import negocio.Professor;
import negocio.Turma;
import org.controlsfx.control.Notifications;
import persistencia.AlunoDisciplinaDao;
import persistencia.DaoFactory;
import persistencia.Filter;
import persistencia.NotFoundException;
import pieduca.Sys;

public class FXMLNotaController implements Initializable {

    @FXML
    private TableView<AlunoDisciplina> tabela;
    @FXML
    private TableColumn<AlunoDisciplina, String> aluno;
    @FXML
    private TableColumn<AlunoDisciplina, String> nota;
    
    private ObservableList<AlunoDisciplina> obsAlunos;
    
    @FXML
    private JFXComboBox cbTurma;
    private ObservableList<Turma> obsTurma;
    
    @FXML
    private JFXComboBox cbDisciplina;
    private ObservableList<Disciplina> obsDisciplina;
    
    @FXML
    private JFXTextField tfNota;
    
    @FXML
    private HBox iconContainer;
    
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
                    FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
                    icon.setSize("2em");
                    icon.setStyle(" -fx-fill: red ;");
                    
                    Notifications notification = Notifications.create();
                    notification.title("Error");
                    notification.graphic(icon);
                    notification.text("Erro ao carregar a tabela");
                    notification.hideAfter(Duration.seconds(3));
                    notification.position(Pos.TOP_RIGHT);
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
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            icon.setSize("2em");
            icon.setStyle(" -fx-fill: red ;");
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Professor não encontrado");
            notification.graphic(icon);
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
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
    }    
    
    public void refreshTabela (MouseEvent event) throws Exception {
        refresh();
    }
    
    private void refresh() {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        
        if (turma != null && disciplina != null) {

            obsAlunos = null;
            
            aluno.setCellValueFactory(new PropertyValueFactory<AlunoDisciplina, String>("aluno"));

            nota.setCellValueFactory(data -> {
                AlunoDisciplina alunoDisciplina = data.getValue();

                List<Nota> notas = alunoDisciplina.getNotas();

                Optional<Nota> nota = notas.stream()
                        .filter(n -> n.getDisciplina().getIdDisciplina() == disciplina.getIdDisciplina())
                        .findFirst();

                if (nota.isPresent()) {
                    return new SimpleStringProperty(String.valueOf(nota.get().getNota()));
                } else {
                    return new SimpleStringProperty("0");
                }
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
                    alunoDisciplina.getNotas().stream()
                        .anyMatch(nota -> nota.getDisciplina().getIdDisciplina() == disciplina.getIdDisciplina())
                )
            .collect(Collectors.toList());
            
            List<Aluno> alunoTodos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
                @Override
                public boolean isAccept(Aluno record) {
                    return (record.getDeletado() == Boolean.FALSE
                            && record.getTurma().getIdTurma() == turma.getIdTurma());
                }
            });
            
            ArrayList<Nota> notas = new ArrayList();
            
            ArrayList<AlunoDisciplina> todos = new ArrayList();
            
            for (Aluno a : alunoTodos) {
                AlunoDisciplina ad = new AlunoDisciplina(a, null, notas);
                
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
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            icon.setSize("2em");
            icon.setStyle(" -fx-fill: red ;");
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.graphic(icon);
            notification.text("Selecione a turma e disciplina");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
        }
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
    
    public void salvar (MouseEvent event) throws Exception {
        
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        String n = tfNota.getText();
        AlunoDisciplina ad = (AlunoDisciplina) tabela.getSelectionModel().getSelectedItem();
        
        if(!n.isEmpty() && ad != null) {
            
            Nota nota = new Nota(disciplina, Double.valueOf(n));
            
            AlunoDisciplinaDao add = new AlunoDisciplinaDao();
            
            add.updateNota(ad, nota);
            
            tfNota.setText("");
            
            refresh();
            
            FontAwesomeIconView checkIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
            checkIcon.setSize("2em");
            checkIcon.setStyle(" -fx-fill: green ;");
            
            Notifications notification = Notifications.create();
            notification.title("Sucesso");
            notification.graphic(checkIcon);
            notification.text("Operação realizada com sucesso");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
            
        } else {
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            icon.setSize("2em");
            icon.setStyle(" -fx-fill: red ;");
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.graphic(icon);
            notification.text("Necessário selecionar um aluno e digitar a nota");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
        }
        
    }
    
    public void checkInput(KeyEvent event) {
        
        if (event.getCharacter().matches("[^\\e\t\r\\d+$]")){
            event.consume();
            tfNota.setStyle("-fx-border-color: red");
        } else {
            tfNota.setStyle("-fx-border-color: blue");
        }
        
    }
    
    public void checkNumeros(KeyEvent event) {

        tfNota.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int nota = Integer.parseInt(newValue);
                    if (nota < 0 || nota > 10) {
                        tfNota.setText(oldValue); // Reverta para o valor anterior se exceder o tamanho máximo
                    }
                } catch (NumberFormatException e) {
                    tfNota.setText(oldValue); // Reverta para o valor anterior se não for um número válido
                }
            }
        });
    }
}
