
package negocio;

public class Aluno {
    
    private Integer idAluno;
    private String nome;
    private String dataNascimento;
    private String rg;
    private String filiacao;
    private Boolean deletado;
    private Turma turma;

    public Aluno(Integer idAluno, String nome, String dataNascimento, String rg, String filiacao, Boolean deletado, Turma turma) {
        this.idAluno = idAluno;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.rg = rg;
        this.filiacao = filiacao;
        this.deletado = deletado;
        this.turma = turma;
    }

    public Integer getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(Integer idAluno) {
        this.idAluno = idAluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
    }

    public Boolean getDeletado() {
        return deletado;
    }

    public void setDeletado(Boolean deletado) {
        this.deletado = deletado;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
}
