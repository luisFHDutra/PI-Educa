
package negocio;

public class Presenca {
    
    private Aluno aluno;
    private String data;
    private Boolean presente;

    public Presenca(Aluno aluno, String data, Boolean presente) {
        this.aluno = aluno;
        this.data = data;
        this.presente = presente;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getPresente() {
        return presente;
    }

    public void setPresente(Boolean presente) {
        this.presente = presente;
    }

    @Override
    public String toString() {
        return this.aluno.getNome();
    }
    
}
