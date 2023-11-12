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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import negocio.Aluno;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.Filter;
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
    private TableColumn<Aluno, String> turma;
    @FXML
    private TableColumn<Aluno, String> edit;

    @FXML
    private HBox iconContainer;
    
    private ObservableList<Aluno> obsAlunos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Aluno, Integer>("idAluno"));
        nome.setCellValueFactory(new PropertyValueFactory<Aluno, String>("nome"));
        nascimento.setCellValueFactory(new PropertyValueFactory<Aluno, String>("dataNascimento"));
        filiacao.setCellValueFactory(new PropertyValueFactory<Aluno, String>("filiacao"));
        rg.setCellValueFactory(new PropertyValueFactory<Aluno, String>("rg"));
        turma.setCellValueFactory(new PropertyValueFactory<Aluno, String>("turma"));
        
        Callback<TableColumn<Aluno, String>, TableCell<Aluno, String>> cellFoctory = (TableColumn<Aluno, String> param) -> {

            final TableCell<Aluno, String> cell = new TableCell<Aluno, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#aaaaaa;"
                            );
                            editIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#aaaaaa;"
                            );
                        
                        if (Sys.getInstance().getUser().getPermissao().getIdPermissao() == 1) {
                            deleteIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#ff1744;"
                            );
                            editIcon.setStyle(
                                    " -fx-cursor: hand ;"
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#00E676;"
                            );
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {

                                try {
                                    deletar(event);
                                } catch (Exception ex) {
                                    error();
                                }

                            });
                            editIcon.setOnMouseClicked((MouseEvent event) -> {

                                try {
                                    atualizarAluno(event);
                                } catch (Exception ex) {
                                    error();
                                }

                            });
                        }

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
        edit.setCellFactory(cellFoctory);
        
        FontAwesomeIconView adicionarIcon = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
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
                        cadastroAluno(event);
                    } catch (Exception ex) {
                        error();
                    }
                }
            };
            adicionarIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, adicionarHandler);
        }
        
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
                    error();
                }
            }
        };
        refreshIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, refreshHandler);
        
        iconContainer.getChildren().addAll(adicionarIcon, refreshIcon);
        
        List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
            @Override
            public boolean isAccept(Aluno record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        obsAlunos = FXCollections.observableArrayList(alunos);
        
        tabela.setItems(obsAlunos);
        
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
        obsAlunos.clear();
        
        id.setCellValueFactory(new PropertyValueFactory<Aluno, Integer>("idAluno"));
        nome.setCellValueFactory(new PropertyValueFactory<Aluno, String>("nome"));
        nascimento.setCellValueFactory(new PropertyValueFactory<Aluno, String>("dataNascimento"));
        filiacao.setCellValueFactory(new PropertyValueFactory<Aluno, String>("filiacao"));
        rg.setCellValueFactory(new PropertyValueFactory<Aluno, String>("rg"));
        turma.setCellValueFactory(new PropertyValueFactory<Aluno, String>("turma"));
        
        List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
            @Override
            public boolean isAccept(Aluno record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        obsAlunos = FXCollections.observableArrayList(alunos);
        
        tabela.setItems(obsAlunos);
    }
    
    public void deletar (MouseEvent event) throws Exception {
        Aluno aluno = tabela.getSelectionModel().getSelectedItem();
        
        if (aluno != null) {
            aluno.setDeletado(Boolean.TRUE);

            DaoFactory.criarAlunoDao().update(aluno);

            refresh();

            Aluno a = DaoFactory.criarAlunoDao().read(aluno.getIdAluno());
            
            if (a.getDeletado() == Boolean.TRUE) {
                check();
            } else {
                error();
            }
        } else {
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            icon.setSize("2em");
            icon.setStyle(" -fx-fill: red ;");
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.graphic(icon);
            notification.text("Selecione um item da tabela");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
            
        }
               
    }
    
    private void error(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
        icon.setSize("2em");
        icon.setStyle(" -fx-fill: red ;");
        
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.graphic(icon);
        notification.text("Erro ao realizar a operação");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }

    private void check(){
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        icon.setSize("2em");
        icon.setStyle(" -fx-fill: green ;");
        
        Notifications notification = Notifications.create();
        notification.title("Sucesso");
        notification.graphic(icon);
        notification.text("Operação realizada com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
    
    public void cadastroAluno (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroAluno.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroAlunoController controller = loader.getController();
        
        controller.setBtnCadastrar(Boolean.FALSE);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void atualizarAluno (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroAluno.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroAlunoController controller = loader.getController();
        
        controller.setBtnAtualizar(Boolean.FALSE);
        
        Aluno aluno = tabela.getSelectionModel().getSelectedItem();
        
        if (aluno != null) {
            
            controller.preencherCampos(aluno);
            
            controller.setAlunoSelecionado(aluno);
            
    //      stage.initStyle(StageStyle.UNDECORATED);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.getIcons().add(icon);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
            
        } else {
            FontAwesomeIconView errorIcon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            errorIcon.setSize("2em");
            errorIcon.setStyle(" -fx-fill: red ;");
            
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.graphic(errorIcon);
            notification.text("Selecione um item da tabela");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
            
        }
    }
}
