package apresentacao;

import auth.EmailAuthenticator;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
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
import javax.mail.internet.MimeBodyPart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
    private JFXTextField tfEmail;
    
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
        
        FontAwesomeIconView fileIcon = new FontAwesomeIconView(FontAwesomeIcon.FILE);
        fileIcon.setSize("2em");
        fileIcon.getStyleClass().add("icon");

        fileIcon.setStyle(" -fx-cursor: hand ;"
                + "-glyph-size:28px;"
                + "-fx-fill:#1aa7ec;"
        );
        
        EventHandler<MouseEvent> fileHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    gerarPdf(event);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    
                    Notifications notification = Notifications.create();
                    notification.title("Error");
                    notification.text("Erro ao gerar PDF");
                    notification.hideAfter(Duration.seconds(3));
                    notification.position(Pos.TOP_RIGHT);
                    notification.show();
                }
            }
        };
        fileIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, fileHandler);
        
        FontAwesomeIconView mailIcon = new FontAwesomeIconView(FontAwesomeIcon.MAIL_FORWARD);
        mailIcon.setSize("2em");
        mailIcon.getStyleClass().add("icon");

        mailIcon.setStyle(" -fx-cursor: hand ;"
                + "-glyph-size:28px;"
                + "-fx-fill:#1aa7ec;"
        );
        
        EventHandler<MouseEvent> mailHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    enviarEmail(event);
                } catch (Exception ex) {                    
                    Notifications notification = Notifications.create();
                    notification.title("Error");
                    notification.text("Erro ao enviar o e-mail");
                    notification.hideAfter(Duration.seconds(3));
                    notification.position(Pos.TOP_RIGHT);
                    notification.show();
                }
            }
        };
        mailIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, mailHandler);
        
        iconContainer.getChildren().addAll( lupaIcon, fileIcon, mailIcon);
        
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
                    boletim.setAprovado("Aprovado");
                    return new SimpleStringProperty("Aprovado");
                    
                } else if (boletim.getNota() < 7) {
                    boletim.setAprovado("Reprovado por nota");
                    return new SimpleStringProperty("Reprovado por nota");
                } else if (porcentagem < 75) {
                    boletim.setAprovado("Reprovado por falta");
                    return new SimpleStringProperty("Reprovado por falta");
                }
                
                boletim.setAprovado("Reprovado");
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
            error();
        }
    }
    
    public void enviarEmail (MouseEvent event) throws Exception {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Aluno aluno = (Aluno) cbAluno.getSelectionModel().getSelectedItem();
        Integer ano = (Integer) cbAno.getSelectionModel().getSelectedItem();
        String email = tfEmail.getText();
        
        if (turma != null || aluno != null || ano != null || tabela.getSelectionModel() != null || !email.isEmpty()) {
            // Configurar credenciais
            String usuario = "educaprojetointegrador@gmail.com";
            String senha = "unkj emgk iutb yhve";

            // Define as propriedades de configuração
            Properties propriedades = new Properties();
            propriedades.put("mail.smtp.host", "smtp.gmail.com"); // Substitua pelo servidor SMTP apropriado
            propriedades.put("mail.smtp.port", "587"); // Porta SMTP
            propriedades.put("mail.smtp.auth", "true");
            propriedades.put("mail.smtp.user", usuario);
            propriedades.put("mail.smtp.password", senha);
            propriedades.put("mail.smtp.starttls.enable", "true");

            Authenticator authenticator = new EmailAuthenticator(usuario, senha);

            // Criar uma sessão
            Session sessao = Session.getInstance(propriedades, authenticator);
            
            String tabelaBoletim = "Olá, resumo do boletim do aluno " + aluno.getNome() + "\n\n";
            for (Boletim boletim : tabela.getItems()) {
                double cargaHoraria = boletim.getDisciplina().getCargaHorariaTotal();
                double porcentagem = (boletim.getFrequencia() / cargaHoraria) * 100;
                
                tabelaBoletim += boletim.getDisciplina().getNome() + " |\t" +
                    boletim.getNota() + " |\t" +
                    String.format("%.2f%%", porcentagem) + " |\t" +
                    boletim.isAprovado() + "\n";
            }
            
            MimeBodyPart corpoEmail = new MimeBodyPart();
            corpoEmail.setContent(tabelaBoletim, "text/plain");
            
            String conteudo = (String) corpoEmail.getContent();
            
            // Criar mensagem de e-mail
            Message mensagem = new MimeMessage(sessao);
            mensagem.setFrom(new InternetAddress(usuario));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mensagem.setSubject("Boletim " + ano + " - " + aluno);
            mensagem.setText(conteudo);

            Transport transport = sessao.getTransport("smtp");
            transport.connect("smtp.gmail.com", usuario, senha);

            // Envia o e-mail
            transport.sendMessage(mensagem, mensagem.getAllRecipients());
            transport.close();

            Notifications notification = Notifications.create();
            notification.title("Sucesso");
            notification.text("E-mail enviado para " + tfEmail.getText());
            notification.hideAfter(Duration.seconds(3));
            notification.position(Pos.TOP_RIGHT);
            notification.show();
        } else {
            error();
        }
 
    }
    
    public void gerarPdf (MouseEvent event) throws Exception {
        Turma turma = (Turma) cbTurma.getSelectionModel().getSelectedItem();
        Aluno aluno = (Aluno) cbAluno.getSelectionModel().getSelectedItem();
        Integer ano = (Integer) cbAno.getSelectionModel().getSelectedItem();
        
        if (turma != null || aluno != null || ano != null || tabela.getSelectionModel() != null) {
            try {
                String nomeTurma = turma.getNome();
                nomeTurma = nomeTurma.replaceAll("\\s", "");
                
                String nomeAluno = aluno.getNome();
                nomeAluno = nomeAluno.replaceAll("\\s", "");
                
                PDDocument document = new PDDocument();
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);     
                
                float margin = 50; // Margem esquerda
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                int cols = 4;
                float rowHeight = 20f;

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(tableWidth / 2, yPosition);
                contentStream.showText("BOLETIM");
                contentStream.endText();
                
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition - 25f);
                contentStream.showText("Aluno: " + aluno.getNome() + "    Turma: " + turma.getNome() + "    Ano: " + ano);
                contentStream.endText();
                 
                // Configurar os cabeçalhos da tabela
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                float nextX = margin;
                yPosition -= 60f;
                for (TableColumn<Boletim, ?> column : tabela.getColumns()) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX, yPosition);
                    contentStream.showText(column.getText());
                    contentStream.endText();
                    nextX += tableWidth / cols;
                }

                // Preencher a tabela com os dados da tabela JavaFX
                for (Boletim item : tabela.getItems()) {
                    yPosition -= rowHeight;
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    nextX = margin;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX, yPosition);
                    contentStream.showText(item.getDisciplina().getNome());
                    contentStream.endText();
                    
                    nextX += tableWidth / cols;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX, yPosition);
                    contentStream.showText(String.valueOf(item.getNota()));
                    contentStream.endText();
                    
                    nextX += tableWidth / cols;
                    double cargaHoraria = item.getDisciplina().getCargaHorariaTotal();
                    double porcentagem = (item.getFrequencia() / cargaHoraria) * 100;
                    
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX, yPosition);
                    contentStream.showText(String.format("%.2f%%", porcentagem));
                    contentStream.endText();
                    
                    nextX += tableWidth / cols;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(nextX, yPosition);
                    contentStream.showText(item.isAprovado());
                    contentStream.endText();
                }
                contentStream.close();

                // Salve o PDF em um arquivo
                File file = new File("boletim" + nomeTurma + nomeAluno + ano + ".pdf");
                document.save(file.getAbsolutePath());
    //            document.save("boletim.pdf");

                // Feche o documento
                document.close();

                Notifications notification = Notifications.create();
                notification.title("Sucesso");
                notification.text("Arquivo criado " + file.getAbsolutePath());
                notification.hideAfter(Duration.seconds(3));
                notification.position(Pos.TOP_RIGHT);
                notification.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            error();
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
    
    private void error() {
        Notifications notification = Notifications.create();
        notification.title("Error");
        notification.text("Selecione todas as opções para continuar");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.TOP_RIGHT);
        notification.show();
    }
}
