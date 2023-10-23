
package negocio;

public class Nota {
    
    private Disciplina disciplina;
    private double nota;

    public Nota(Disciplina disciplina, double nota) {
        this.disciplina = disciplina;
        this.nota = nota;
    }
    
    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.nota);
    }
}
