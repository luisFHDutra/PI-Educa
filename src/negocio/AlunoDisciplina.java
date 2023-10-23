
package negocio;

import java.util.ArrayList;

public class AlunoDisciplina {
    
    private Aluno aluno;
    private ArrayList<Presenca> presencas;
    private ArrayList<Nota> notas;

    public AlunoDisciplina(Aluno aluno, ArrayList<Presenca> presencas, ArrayList<Nota> notas) {
        this.aluno = aluno;
        this.presencas = presencas;
        this.notas = notas;
    }
    
    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public ArrayList<Presenca> getPresencas() {
        return presencas;
    }

    public void setPresencas(ArrayList<Presenca> presencas) {
        this.presencas = presencas;
    }

    public ArrayList<Nota> getNotas() {
        return notas;
    }

    public void setNotas(ArrayList<Nota> notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return this.aluno.getNome();
    }
}
