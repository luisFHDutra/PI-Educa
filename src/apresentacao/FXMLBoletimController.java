package apresentacao;

import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import negocio.Aluno;
import negocio.AlunoDisciplina;
import negocio.Boletim;
import negocio.Disciplina;
import negocio.Nota;
import negocio.Presenca;
import negocio.Turma;
import org.controlsfx.control.Notifications;
import persistencia.DaoFactory;
import persistencia.Filter;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class FXMLBoletimController implements Initializable {

    @FXML
    private TableView<Boletim> tabela;
    @FXML
    private TableColumn<Boletim, String> disciplina;
    @FXML
    private TableColumn<Boletim, Double> nota;
    @FXML
    private TableColumn<Boletim, String> frequencia;
    @FXML
    private TableColumn<Boletim, String> aprovado;
    
    @FXML
    private JFXComboBox cbTurma;
    private ObservableList<Turma> obsTurmas;
    
    @FXML
    private JFXComboBox cbAluno;
    private ObservableList<Aluno> obsAlunos;
    
    @FXML
    private JFXComboBox cbAno;
    private ObservableList<Integer> obsAnos;

    private ObservableList<Boletim> obsBoletim;
    
    @FXML
    private HBox iconContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FontAwesomeIconView lupaIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
        lupaIcon.setSize("2em");
        lupaIcon.getStyleClass().add("icon");

        lupaIcon.setStyle(" -fx-cursor: hand ;"
                + "-glyph-size:28px;"
                + "-fx-fill:#1aa7ec;"
        );
        
        EventHandler<MouseEvent> lupaHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    pesquisar(event);
                } catch (Exception ex) {
                    Notifications notification = Notifications.create();
                    notification.title("Error");
                    notification.text("Erro ao procurar boletim");
                    notification.hideAfter(Duration.seconds(3));
                    notification.position(Pos.TOP_RIGHT);
                    notification.show();
                }
            }
        };
        lupaIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, lupaHandler);
        
        FontAwesomeIconView printIcon = new FontAwesomeIconView(FontAwesomeIcon.PRINT);
        printIcon.setSize("2em");
        printIcon.getStyleClass().add("icon");

        printIcon.setStyle(" -fx-cursor: hand ;"
                + "-glyph-size:28px;"
                + "-fx-fill:#1aa7ec;"
        );
        
        iconContainer.getChildren().addAll( lupaIcon, printIcon);
        
        List<Turma> turmas = DaoFactory.criarTurmaDao().readAll();
        
        obsTurmas = FXCollections.observableArrayList(turmas);
       
        cbTurma.setItems(obsTurmas);
        
        List<Integer> anos = new ArrayList();
        int anoAtual = Year.now().getValue();
        for (int ano = 2000; ano <= anoAtual; ano++) {
            anos.add(ano);
        }
        
        obsAnos = FXCollections.observableArrayList(anos);
        
        cbAno.setItems(obsAnos);
        
        cbAno.setValue(anoAtual);
    }    
    
    public void selecionarAluno (MouseEvent event) throws Exception {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        
        if (turma != null) {
            List<Aluno> alunos = DaoFactory.criarAlunoDao().readAll(new Filter<Aluno>() {
                @Override
                public boolean isAccept(Aluno record) {
                    return (record.getDeletado() == Boolean.FALSE &&
                            record.getTurma().getIdTurma() == turma.getIdTurma());
                }
            });

            obsAlunos = FXCollections.observableArrayList(alunos);

            cbAluno.setItems(obsAlunos);
        }
    }
        
    public void pesquisar (MouseEvent event) throws Exception {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Aluno aluno = (Aluno) cbAluno.getSelectionModel().getSelectedItem();
        Integer ano = (Integer) cbAno.getSelectionModel().getSelectedItem();
        
        if (turma != null || aluno != null || ano != null) {
            
            disciplina.setCellValueFactory(new PropertyValueFactory<Boletim, String>("disciplina"));
            nota.setCellValueFactory(new PropertyValueFactory<Boletim, Double>("nota"));

            frequencia.setCellValueFactory(data -> {
                Boletim boletim = data.getValue();
                
                double cargaHoraria = boletim.getDisciplina().getCargaHorariaTotal();
                double porcentagem = (boletim.getFrequencia() / cargaHoraria) * 100;
                
                return new SimpleStringProperty(String.format("%.2f%%", porcentagem));
            });
            
            aprovado.setCellValueFactory(data -> {
                Boletim boletim = data.getValue();
                
                double cargaHoraria = boletim.getDisciplina().getCargaHorariaTotal();
                double porcentagem = (boletim.getFrequencia() / cargaHoraria) * 100;
                
                if (boletim.getNota() >= 7 && porcentagem >= 75) {
                    
                    return new SimpleStringProperty("Aprovado");
                    
                } else if (boletim.getNota() < 7) {
                    
                    return new SimpleStringProperty("Reprovado por nota");
                } else if (porcentagem < 75) {
                    
                    return new SimpleStringProperty("Reprovado por falta");
                }
                
                return new SimpleStringProperty("Reprovado");
            });
            
            List<Boletim> boletins = new ArrayList();
            
            List<Disciplina> disciplinas = DaoFactory.criarDisciplinaDao().readAll();
            
            for (Disciplina disciplina : disciplinas) {
                Boletim b = new Boletim(disciplina);
                boletins.add(b);
            }
            
            List<AlunoDisciplina> alunos = DaoFactory.criarAlunoDisciplinaDao().readAll(new Filter<AlunoDisciplina>() {
                @Override
                public boolean isAccept(AlunoDisciplina record) {
                    return (record.getAluno().getDeletado() == Boolean.FALSE
                            && record.getAluno().getTurma().getIdTurma() == turma.getIdTurma()
                            && record.getAluno().getIdAluno() == aluno.getIdAluno());
                }
            });
            
            AlunoDisciplina alunoFiltrado = alunos.get(0);
            
            for (Boletim b : boletins) {
                for (Nota n : alunoFiltrado.getNotas()) {
                    
                    if (b.getDisciplina().getNome().equals(n.getDisciplina().getNome())) {
                        b.setNota(n.getNota());
                    }
                    
                }
                
                for (Presenca p : alunoFiltrado.getPresencas()) {
                    
                    if (b.getDisciplina().getNome().equals(p.getDisciplina().getNome()) &&
                            p.getData().substring(0, 4).equals(String.valueOf(ano))) {
                        
                        if (p.getPresente()) {
                            b.setFrequencia(1);
                        }
                    }
                    
                }
            }
            
            obsBoletim = FXCollections.observableArrayList(boletins);
            
            tabela.setItems(obsBoletim);
            
        } else {
            Notifications notification = Notifications.create();
            notification.title("Error");
            notification.text("Selecione todas as opções para continuar");
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
        }
    }
    
    public void enviarEmail (MouseEvent event) throws Exception {
        
        // Configurar as propriedades
        Properties propriedades = new Properties();
        propriedades.put("mail.smtp.host", "smtp.example.com"); // Substitua pelo servidor SMTP apropriado
        propriedades.put("mail.smtp.port", "587"); // Porta SMTP
        propriedades.put("mail.smtp.auth", "true");

        // Configurar credenciais
        String usuario = "seu_email@example.com";
        String senha = "sua_senha";

        // Criar uma sessão
        Session sessao = Session.getInstance(propriedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        // Criar mensagem de e-mail
        Message mensagem = new MimeMessage(sessao);
        mensagem.setFrom(new InternetAddress(usuario));
        mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(""));
        mensagem.setSubject("");
        mensagem.setText("");

        // Enviar o e-mail
        Transport.send(mensagem);
        
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
}
