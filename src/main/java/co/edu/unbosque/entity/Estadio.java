package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ESTADIOS")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTADIO")
    private Integer idEstadio;

    @Column(name = "DESCRIPCION", length = 50)
    private String descripcion;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    public Estadio() {
        this.estado = "A";
    }

    public Integer getIdEstadio() { return idEstadio; }
    public void setIdEstadio(Integer idEstadio) { this.idEstadio = idEstadio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
