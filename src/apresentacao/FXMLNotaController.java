package apresentacao;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.AlunoDisciplina;
import negocio.Disciplina;
import negocio.Nota;
import negocio.Turma;
import org.controlsfx.control.Notifications;
import persistencia.AlunoDisciplinaDao;
import persistencia.DaoFactory;
import persistencia.Filter;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Turma> turmas = DaoFactory.criarTurmaDao().readAll();
        
        obsTurma = FXCollections.observableArrayList(turmas);
       
        cbTurma.setItems(obsTurma);
        
        List<Disciplina> disciplinas = DaoFactory.criarDisciplinaDao().readAll();
        
        obsDisciplina = FXCollections.observableArrayList(disciplinas);
       
        cbDisciplina.setItems(obsDisciplina);
        
    }    
    
    public void refreshTable (MouseEvent event) throws Exception {
        refresh();
    }
    
    private void refresh() {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Disciplina disciplina = (Disciplina) cbDisciplina.getSelectionModel().getSelectedItem();
        
        if (turma != null && disciplina != null) {

            aluno.setCellValueFactory(new PropertyValueFactory<AlunoDisciplina, String>("aluno"));

            nota.setCellValueFactory(data -> {
            AlunoDisciplina alunoDisciplina = data.getValue();
            List<Nota> notas = alunoDisciplina.getNotas();
            if (notas.size() >= 1) {
                return new SimpleStringProperty(String.valueOf(notas.get(0).getNota()));
            }
                return new SimpleStringProperty("0");
            });

            List<AlunoDisciplina> alunos = DaoFactory.criarAlunoDisciplinaDao().readAll(new Filter<AlunoDisciplina>() {
                @Override
                public boolean isAccept(AlunoDisciplina record) {
                    return (record.getAluno().getDeletado() == Boolean.FALSE
                            && record.getAluno().getTurma().getIdTurma() == turma.getIdTurma());
                }
            });
            
            List<AlunoDisciplina> filtroDisciplina = alunos.stream()
                .filter(alunoDisciplina -> 
                    alunoDisciplina.getAluno().getTurma().getDisciplinas().stream()
                        .anyMatch(d -> d.getIdDisciplina() == disciplina.getIdDisciplina())
                )
            .collect(Collectors.toList());
            
            System.out.println("tam " + filtroDisciplina.size());
            
            for (AlunoDisciplina alunoDisciplina : filtroDisciplina) {
                System.out.println("filtro disciplina " + alunoDisciplina.getAluno().getNome());
            }
            
            
            
            List<AlunoDisciplina> alunosFiltrados = filtroDisciplina.stream()
                .filter(alunoDisciplina -> 
                    alunoDisciplina.getNotas().stream()
                        .anyMatch(nota -> nota.getDisciplina().getIdDisciplina() == disciplina.getIdDisciplina())
                )
            .collect(Collectors.toList());
            
            HashSet<AlunoDisciplina> set = new HashSet<>(alunosFiltrados);
            set.addAll(filtroDisciplina);

            obsAlunos = FXCollections.observableArrayList(set);

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
    
    public void voltarMain (MouseEvent event) throws Exception {
        
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/apresentacao/FXMLMain.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
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
            
            Notifications notification = Notifications.create();
            notification.title("Sucesso");
            notification.text("Operação realizada com sucesso");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
            notification.show();
            
        } else {
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Necessário selecionar um aluno e digitar a nota");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.BOTTOM_CENTER);
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
