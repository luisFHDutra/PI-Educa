
package negocio;

public class Usuario {
    
    private Integer idUsuario;
    private String username;
    private String senha;

    public Usuario(Integer idUsuario, String username, String senha) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.senha = senha;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return getUsername();
    }
    
}
