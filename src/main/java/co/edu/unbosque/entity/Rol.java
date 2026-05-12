package co.edu.unbosque.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "NOMBRE_ROL", nullable = false, length = 50)
    private String nombreRol;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    public Rol() {
        this.estado = "A";
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}