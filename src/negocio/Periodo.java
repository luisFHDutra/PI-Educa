
package negocio;

public class Periodo {
    
    private Integer idPeriodo;
    private Integer horaInicio;
    private Integer horaFim;

    public Periodo(Integer idPeriodo, Integer horaInicio, Integer horaFim) {
        this.idPeriodo = idPeriodo;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Integer horaFim) {
        this.horaFim = horaFim;
    }
    
}
