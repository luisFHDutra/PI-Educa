
package negocio;

public class Turma {
    
    private Integer idTurma;
    private String nome;
    private Integer anoLetivo;

    public Turma(Integer idTurma, String nome, Integer anoLetivo) {
        this.idTurma = idTurma;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
    }
    
    public Integer getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(Integer anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    @Override
    public String toString() {
        return getNome();
    }
    
}
