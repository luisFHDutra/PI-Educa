
package negocio;

import java.util.ArrayList;

public class Turma {
    
    private Integer idTurma;
    private String nome;
    private Integer anoLetivo;
    private ArrayList<Professor> professores;
    private ArrayList<Disciplina> disciplinas;

    public Turma(Integer idTurma, String nome, Integer anoLetivo, ArrayList<Professor> professores, ArrayList<Disciplina> disciplinas) {
        this.idTurma = idTurma;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.professores = professores;
        this.disciplinas = disciplinas;
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

    public ArrayList<Professor> getProfessores() {
        return professores;
    }

    public void setProfessores(ArrayList<Professor> professores) {
        this.professores = professores;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(ArrayList<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
}
