
package negocio;

import java.util.ArrayList;

public class Professor {
    
    private Integer idProfessor;
    private String nome;
    private String areaEspecializacao;
    private String contato;
    private Usuario usuario;
    private Boolean deletado;
    private Disciplina disciplina;

    public Professor(String nome, String areaEspecializacao, String contato, Usuario usuario, Boolean deletado, Disciplina disciplina) {
        this.nome = nome;
        this.areaEspecializacao = areaEspecializacao;
        this.contato = contato;
        this.usuario = usuario;
        this.deletado = deletado;
        this.disciplina = disciplina;
    }
    
    public Professor(Integer idProfessor, String nome, String areaEspecializacao, String contato, Usuario usuario, Boolean deletado, Disciplina disciplina) {
        this.nome = nome;
        this.areaEspecializacao = areaEspecializacao;
        this.contato = contato;
        this.usuario = usuario;
        this.idProfessor = idProfessor;
        this.deletado = deletado;
        this.disciplina = disciplina;
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

    public String getAreaEspecializacao() {
        return areaEspecializacao;
    }

    public void setAreaEscpecializacao(String areaEspecializacao) {
        this.areaEspecializacao = areaEspecializacao;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getDeletado() {
        return deletado;
    }

    public void setDeletado(Boolean deletado) {
        this.deletado = deletado;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
}
