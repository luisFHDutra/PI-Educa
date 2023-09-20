
package negocio;

public class Usuario {
    
    private Integer idUsuario;
    private String hashCode;

    public Usuario(Integer idUsuario, String hashCode) {
        this.idUsuario = idUsuario;
        this.hashCode = hashCode;
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
    
}
