
package auth;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class EmailAuthenticator extends Authenticator {
    
    private String usuario;
    private String senha;

    public EmailAuthenticator(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(usuario, senha);
    }
    
}
