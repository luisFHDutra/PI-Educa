package persistencia;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationSQL {
    
    public void chaveDuplicada() {
        Notifications notification = Notifications.create();
        notification.title("Inserção no banco de dados");
        notification.text("Chave primária duplicada");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void tabelaNaoExiste() {
        Notifications notification = Notifications.create();
        notification.title("Consulta no banco de dados");
        notification.text("Tabela não existe");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void dataTypeErrado() {
        Notifications notification = Notifications.create();
        notification.title("Consulta no banco de dados");
        notification.text("DataType errado");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void erroSintaxe() {
        Notifications notification = Notifications.create();
        notification.title("Consulta no banco de dados");
        notification.text("Erro de sintaxe ou semântica");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void conexaoBD() {
        Notifications notification = Notifications.create();
        notification.title("Conexão com o banco de dados");
        notification.text("Erro fatal na configuração de acesso ao banco de dados");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
    
    public void rollback() {
        Notifications notification = Notifications.create();
        notification.title("Transação SQL");
        notification.text("Erro ao realizar rollback");
        notification.hideAfter(Duration.seconds(3));
        notification.position(Pos.BOTTOM_CENTER);
        notification.show();
    }
}
