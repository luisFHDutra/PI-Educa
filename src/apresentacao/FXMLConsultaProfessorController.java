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
import javafx.util.Callback;
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
    private TableColumn<Professor, String> edit;
 
    @FXML
    private HBox iconContainer;
    
    private ObservableList<Professor> obsProfessores;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<Professor, Integer>("idProfessor"));
        nome.setCellValueFactory(new PropertyValueFactory<Professor, String>("nome"));
        area_espec.setCellValueFactory(new PropertyValueFactory<Professor, String>("areaEspecializacao"));
        contato.setCellValueFactory(new PropertyValueFactory<Professor, String>("contato"));
        disciplina.setCellValueFactory(new PropertyValueFactory<Professor, String>("disciplina"));
        
        Callback<TableColumn<Professor, String>, TableCell<Professor, String>> cellFoctory = (TableColumn<Professor, String> param) -> {

            final TableCell<Professor, String> cell = new TableCell<Professor, String>() {
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
                                    atualizarProfessor(event);
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
                        cadastroProfessor(event);
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
        
        List<Professor> profs = DaoFactory.criarProfessorDao().readAll(new Filter<Professor>() {
            @Override
            public boolean isAccept(Professor record) {
                return (record.getDeletado() == Boolean.FALSE);
            }
        });
        
        obsProfessores = FXCollections.observableArrayList(profs);
        
        tabela.setItems(obsProfessores);
        
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
        Image icon = new Image("/imagens/book-icon.png");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/apresentacao/FXMLCadastroProfessores.fxml"));
        Parent root = loader.load();
        
        FXMLCadastroProfessoresController controller = loader.getController();
        
        controller.setBtnCadastrar(Boolean.FALSE);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(icon);
//      stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
        ((Node)event.getSource()).getScene().getWindow().hide();
        
    }
    
    public void atualizarProfessor (MouseEvent event) throws Exception {
        Image icon = new Image("/imagens/book-icon.png");
        
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
        notification.text("Erro ao deletar usuário ou professor");
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
        notification.text("Delete realizado com sucesso");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
}
