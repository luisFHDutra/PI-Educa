package auth;

import java.util.ArrayList;
import negocio.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator
{
    private Usuario user;
    private Usuario loggedUser;
    private Integer id;
    private String senha;

    public Authenticator()
    {
        this.user = null;
        this.loggedUser = null;
    }

    public Authenticator(Usuario user, Integer id, String senha)
    {
        this.user = user;
        this.loggedUser = null;
        this.id = id;
        this.senha = senha;
    }
    
    public boolean isRight()
    {
        boolean ok = false;
        if (id != 0 && !senha.isEmpty())
        {
            
            if (user.getId() == id)
            {
                if (checkPassword(senha, user.getHashCode()))
                {
                    this.loggedUser = user;
                    ok = true;
                }
            }
            
        }
        return ok;
    }

    public boolean checkPassword(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }

    public String generateHashCode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    public Usuario getLoggedUser()
    {
        return loggedUser;
    }
}
