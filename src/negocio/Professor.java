
package negocio;

import java.util.ArrayList;

public class Professor {
    
    private Integer idProfessor;
    private String nome;
    private String cpf;
    private String endereco;
    private String dataNascimento;
    private Usuario usuario;
    private ArrayList<Disciplina> disciplinas;

    public Professor(Integer idProfessor, String nome, String cpf, String endereco, String dataNascimento, Usuario usuario, ArrayList<Disciplina> disciplinas) {
        this.idProfessor = idProfessor;
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
        this.usuario = usuario;
        this.disciplinas = disciplinas;
    }

    public Integer getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(Integer idProfessor) {
        this.idProfessor = idProfessor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
