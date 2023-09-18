
package negocio;

import auth.User;

public class Usuario implements User {
    
    private Integer idUsuario;
    private String username;
    private String hashCode;

    public Usuario(Integer idUsuario, String username, String hashCode) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.hashCode = hashCode;
    }
    
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return getUsername();
    }
    
}
