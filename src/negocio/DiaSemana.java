
package negocio;

public class DiaSemana {
    
    private Integer idDiaSemana;
    private String nome;

    public DiaSemana(Integer idDiaSemana, String nome) {
        this.idDiaSemana = idDiaSemana;
        this.nome = nome;
    }

    public Integer getIdDiaSemana() {
        return idDiaSemana;
    }

    public void setIdDiaSemana(Integer idDiaSemana) {
        this.idDiaSemana = idDiaSemana;
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
