package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GRUPOS")
public class Grupo {

    @Id
    @Column(name = "ID_GRUPO", length = 1)
    private String idGrupo;

    @Column(name = "DESCRIPCION", length = 50)
    private String descripcion;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    public Grupo() {
        this.estado = "A";
    }

    public String getIdGrupo() { return idGrupo; }
    public void setIdGrupo(String idGrupo) { this.idGrupo = idGrupo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}