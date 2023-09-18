package auth;

import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class Authenticator
{
    private ArrayList<? extends User> users;
    private User loggedUser;
    private String username;
    private String senha;

    public Authenticator(ArrayList<? extends User> users, String username, String senha)
    {
        this.users = users;
        this.loggedUser = null;
        this.username = username;
        this.senha = senha;
    }

    public boolean isRight()
    {
        boolean ok = false;
        if (!username.isEmpty() && !senha.isEmpty())
        {
            for (User user : users)
            {
                if (user.getUsername().equals(username))
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
    
    public User getLoggedUser()
    {
        return loggedUser;
    }
}
