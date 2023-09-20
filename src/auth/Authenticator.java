package auth;

import java.util.ArrayList;
import negocio.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator
{
    private ArrayList<Usuario> users;
    private Usuario loggedUser;
    private Integer id;
    private String senha;

    public Authenticator(ArrayList<Usuario> users, Integer id, String senha)
    {
        this.users = users;
        this.loggedUser = null;
        this.id = id;
        this.senha = senha;
    }

    public boolean isRight()
    {
        boolean ok = false;
        if (id != 0 && !senha.isEmpty())
        {
            for (Usuario user : users)
            {
                if (user.getId().equals(id))
                {
                    // Verifique a senha usando a API de verificação de senha
                    if (checkPassword(senha, user.getHashCode()))
                    {
                        this.loggedUser = user;
                        ok = true;
                        break;
                    }
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
