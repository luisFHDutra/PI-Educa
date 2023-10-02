package negocio;

public class Permissao {
    
    private Integer idPermissao;
    private String nome;

    public Permissao(Integer idPermissao, String nome) {
        this.idPermissao = idPermissao;
        this.nome = nome;
    }

    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return getNome();
    }
    
}
