package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARTIDOS")
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARTIDO")
    private Integer idPartido;

    @Column(name = "ID_EQUIPO_LOCAL")
    private Integer idEquipoLocal;

    @Column(name = "ID_EQUIPO_VISITANTE")
    private Integer idEquipoVisitante;

    @Column(name = "FASE", length = 50)
    private String fase;

    @Column(name = "GOLES_LOCAL")
    private Integer golesLocal;

    @Column(name = "GOLES_VISITANTE")
    private Integer golesVisitante;

    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    public Partido() {
        this.estado = "A";
        this.golesLocal = 0;
        this.golesVisitante = 0;
    }

    public Integer getIdPartido() { return idPartido; }
    public void setIdPartido(Integer idPartido) { this.idPartido = idPartido; }

    public Integer getIdEquipoLocal() { return idEquipoLocal; }
    public void setIdEquipoLocal(Integer idEquipoLocal) { this.idEquipoLocal = idEquipoLocal; }

    public Integer getIdEquipoVisitante() { return idEquipoVisitante; }
    public void setIdEquipoVisitante(Integer idEquipoVisitante) { this.idEquipoVisitante = idEquipoVisitante; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public Integer getGolesLocal() { return golesLocal; }
    public void setGolesLocal(Integer golesLocal) { this.golesLocal = golesLocal; }

    public Integer getGolesVisitante() { return golesVisitante; }
    public void setGolesVisitante(Integer golesVisitante) { this.golesVisitante = golesVisitante; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}