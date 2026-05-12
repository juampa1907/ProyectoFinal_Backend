package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "EQUIPOS")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EQUIPO")
    private Integer idEquipo;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "ID_GRUPO", length = 1)
    private String idGrupo;

    @Column(name = "ENTRENADOR", length = 100)
    private String entrenador;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    @Column(name = "BANDERA", length = 100)
    private String bandera;

    public Equipo() {
        this.estado = "A";
    }

    public Integer getIdEquipo() { return idEquipo; }
    public void setIdEquipo(Integer idEquipo) { this.idEquipo = idEquipo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIdGrupo() { return idGrupo; }
    public void setIdGrupo(String idGrupo) { this.idGrupo = idGrupo; }

    public String getEntrenador() { return entrenador; }
    public void setEntrenador(String entrenador) { this.entrenador = entrenador; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getBandera() { return bandera; }
    public void setBandera(String bandera) { this.bandera = bandera; }
}