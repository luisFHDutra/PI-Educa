
package negocio;

import java.util.ArrayList;

public class Disciplina {
    
    private Integer idDisciplina;
    private String nome;
    private Integer cargaHorariaTotal;
    private DiaSemana diaSemana;
    private Periodo periodo;
    private ArrayList<AlunoDisciplina> alunoDisciplina;

    public Disciplina(Integer idDisciplina, String nome, Integer cargaHorariaTotal, DiaSemana diaSemana, Periodo periodo, ArrayList<AlunoDisciplina> alunoDisciplina) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.diaSemana = diaSemana;
        this.periodo = periodo;
        this.alunoDisciplina = alunoDisciplina;
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

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public ArrayList<AlunoDisciplina> getAlunoDisciplina() {
        return alunoDisciplina;
    }

    public void setAlunoDisciplina(ArrayList<AlunoDisciplina> alunoDisciplina) {
        this.alunoDisciplina = alunoDisciplina;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
}
