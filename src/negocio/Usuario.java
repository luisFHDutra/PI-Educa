
package negocio;

public class Usuario {
    
    private Integer idUsuario;
    private String hashCode;
    private Permissao permissao;

    public Usuario(Integer idUsuario, String hashCode, Permissao permissao) {
        this.idUsuario = idUsuario;
        this.hashCode = hashCode;
        this.permissao = permissao;
    }
    
    public Integer getId() {
        return idUsuario;
    }

    public void setId(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }
}
