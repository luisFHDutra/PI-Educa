
package negocio;

import java.util.ArrayList;

public class Turma {
    
    private Integer idTurma;
    private String nome;
    private String anoLetivo;
    private ArrayList<Professor> professores;
    private ArrayList<Disciplina> disciplinas;
    private Integer quantidadeAlunos;

    public Turma(Integer idTurma, String nome, String anoLetivo, ArrayList<Professor> professores, ArrayList<Disciplina> disciplinas) {
        this.idTurma = idTurma;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.professores = professores;
        this.disciplinas = disciplinas;
    }

    public Turma(Integer idTurma, String nome, String anoLetivo, ArrayList<Professor> professores, ArrayList<Disciplina> disciplinas, Integer quantidadeAlunos) {
        this.idTurma = idTurma;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.professores = professores;
        this.disciplinas = disciplinas;
        this.quantidadeAlunos = quantidadeAlunos;
    }

    public Turma(Integer idTurma, String nome, String anoLetivo) {
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

    public String getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(String anoLetivo) {
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

    public Integer getQuantidadeAlunos() {
        return quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }
    
    public void incrementarQuantidadeAlunos() {
        this.quantidadeAlunos++;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
}
