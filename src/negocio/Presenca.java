
package negocio;

public class Presenca {
    
    private Disciplina disciplina;
    private String data;
    private Boolean presente;

    public Presenca(Disciplina disciplina, String data, Boolean presente) {
        this.disciplina = disciplina;
        this.data = data;
        this.presente = presente;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
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
        return this.disciplina.getNome();
    }
    
}
