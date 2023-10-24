package apresentacao;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import negocio.Aluno;
import negocio.Turma;
import org.controlsfx.control.Notifications;
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
    private TableColumn<Turma, String> edit;
    
    @FXML
    private HBox iconContainer;
    
    private ObservableList<Turma> obsTurmas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        turma.setCellValueFactory(new PropertyValueFactory<Turma, String>("nome"));
        ano.setCellValueFactory(new PropertyValueFactory<Turma, String>("anoLetivo"));
        alunos.setCellValueFactory(new PropertyValueFactory<Turma, Integer>("quantidadeAlunos"));
        
        Callback<TableColumn<Turma, String>, TableCell<Turma, String>> cellFoctory = (TableColumn<Turma, String> param) -> {

            final TableCell<Turma, String> cell = new TableCell<Turma, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            
                            try {
                                atualizarTurma(event);
                            } catch (Exception ex) {
                                error();
                            }

                        });

                        HBox managebtn = new HBox(editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        edit.setCellFactory(cellFoctory);
        
        FontAwesomeIconView adicionarIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
        adicionarIcon.setSize("2em");
        adicionarIcon.getStyleClass().add("icon");

        adicionarIcon.setStyle(" -fx-cursor: hand ;"
                    + "-glyph-size:28px;"
                    + "-fx-fill:#aaaaaa;"
            );
        
        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() == 1) {
            
            adicionarIcon.setStyle(" -fx-cursor: hand ;"
                    + "-glyph-size:28px;"
                    + "-fx-fill:#1aa7ec;"
            );
            
            EventHandler<MouseEvent> adicionarHandler = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        cadastroTurma(event);
                    } catch (Exception ex) {
                        error();
                    }
                }
            };
            adicionarIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, adicionarHandler);
        }
        
        iconContainer.getChildren().add(adicionarIcon);
        
        List<Turma> turmas = obterTurmas();
        
        obsTurmas = FXCollections.observableArrayList(turmas);
        
        tabela.setItems(obsTurmas);
        
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
    
    public void cadastroTurma (MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroTurma.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroTurmaController controller = loader.getController();
        
        controller.setBtnCadastrar(Boolean.FALSE);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void atualizarTurma (MouseEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroTurma.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroTurmaController controller = loader.getController();
        
        controller.setBtnAtualizar(Boolean.FALSE);
        
        Turma turma = tabela.getSelectionModel().getSelectedItem();
        
        if (turma != null) {
            
            controller.preencherCampos(turma);
            
            controller.setTurmaSelecionado(turma);
            
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
    
    public void refresh() {
        obsTurmas.clear();
        
        turma.setCellValueFactory(new PropertyValueFactory<Turma, String>("nome"));
        ano.setCellValueFactory(new PropertyValueFactory<Turma, String>("anoLetivo"));
        alunos.setCellValueFactory(new PropertyValueFactory<Turma, Integer>("quantidadeAlunos"));
        
        List<Turma> turmas = obterTurmas();
        
        obsTurmas = FXCollections.observableArrayList(turmas);
        
        tabela.setItems(obsTurmas);
    }
    
    private void error(){
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Erro ao realizar a operação");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
}
