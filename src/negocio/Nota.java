
package negocio;

public class Nota {
    
    private Aluno aluno;
    private double nota;

    public Nota(Aluno aluno, double nota) {
        this.aluno = aluno;
        this.nota = nota;
    }
    
    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
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
