
package negocio;

public class Disciplina {
    
    private Integer idDisciplina;
    private String nome;
    private Integer cargaHorariaTotal;

    public Disciplina(Integer idDisciplina, String nome, Integer cargaHorariaTotal) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.cargaHorariaTotal = cargaHorariaTotal;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCargaHorariaTotal() {
        return cargaHorariaTotal;
    }

    public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
        this.cargaHorariaTotal = cargaHorariaTotal;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
}
