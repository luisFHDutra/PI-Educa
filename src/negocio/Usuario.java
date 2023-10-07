
package negocio;

public class Usuario {
    
    private Integer idUsuario;
    private String hashCode;
    private Permissao permissao;
    private Boolean deletado;

    public Usuario(Integer idUsuario, String hashCode, Permissao permissao, Boolean deletado) {
        this.idUsuario = idUsuario;
        this.hashCode = hashCode;
        this.permissao = permissao;
        this.deletado = deletado;
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

    public Boolean getDeletado() {
        return deletado;
    }

    public void setDeletado(Boolean deletado) {
        this.deletado = deletado;
    }
    
}
