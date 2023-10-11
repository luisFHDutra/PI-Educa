
package negocio;

import java.util.ArrayList;

public class Disciplina {
    
    private Integer idDisciplina;
    private String nome;
    private Integer cargaHorariaTotal;
    private ArrayList<Presenca> presencas;
    private ArrayList<Nota> notas;

    public Disciplina(Integer idDisciplina, String nome, Integer cargaHorariaTotal, ArrayList<Presenca> presencas, ArrayList<Nota> notas) {
        this.idDisciplina = idDisciplina;
        this.nome = nome;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.presencas = presencas;
        this.notas = notas;
    }

    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCargaHorariaTotal() {
        return cargaHorariaTotal;
    }

    public void setCargaHorariaTotal(Integer cargaHorariaTotal) {
        this.cargaHorariaTotal = cargaHorariaTotal;
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
        return getNome();
    }
    
}
