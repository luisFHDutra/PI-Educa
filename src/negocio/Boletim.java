package negocio;

public class Boletim {
    
    private Disciplina disciplina;
    private double nota;
    private double frequencia;
    private String aprovado;

    public Boletim(Disciplina disciplina, double nota, double frequencia, String aprovado) {
        this.disciplina = disciplina;
        this.nota = nota;
        this.frequencia = frequencia;
        this.aprovado = aprovado;
    }

    public Boletim(Disciplina disciplina) {
        this.disciplina = disciplina;
        this.frequencia = 0;
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

    public double getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(double frequencia) {
        this.frequencia += frequencia;
    }

    public String isAprovado() {
        return aprovado;
    }

    public void setAprovado(String aprovado) {
        this.aprovado = aprovado;
    }

    @Override
    public String toString() {
        return getDisciplina().getNome();
    }
}
