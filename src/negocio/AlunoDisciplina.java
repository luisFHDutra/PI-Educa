
package negocio;

public class AlunoDisciplina {
    
    private Aluno aluno;
    private Integer presenca;
    private double nota;

    public AlunoDisciplina(Aluno aluno, Integer presenca, double nota) {
        this.aluno = aluno;
        this.presenca = presenca;
        this.nota = nota;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Integer getPresenca() {
        return presenca;
    }

    public void setPresenca(Integer presenca) {
        this.presenca = presenca;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return this.aluno.getNome();
    }
    
}
