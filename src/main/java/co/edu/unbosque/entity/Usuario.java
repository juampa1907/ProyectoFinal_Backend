package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "USERNAME", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 255)
    private String password;

    @Column(name = "NOMBREAPELLIDO", unique = true, nullable = false, length = 50)
    private String nombreApellido;

    @Column(name = "ID_ROL")
    private Integer idRol;

    @Column(name = "ESTADO", length = 1)
    private String estado;

    @Column(name = "INTENTOS")
    private Integer intentos;

    @Column(name = "FECHA_ULT_CLAVE")
    private LocalDateTime fechaUltClave;

    @Column(name = "CORREO", unique = true, nullable = false, length = 255)
    private String correo;

    public Usuario() {
        this.estado = "A";
        this.intentos = 0;
        this.fechaUltClave = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombreApellido() { return nombreApellido; }
    public void setNombreApellido(String nombreApellido) { this.nombreApellido = nombreApellido; }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getIntentos() { return intentos; }
    public void setIntentos(Integer intentos) { this.intentos = intentos; }

    public LocalDateTime getFechaUltClave() { return fechaUltClave; }
    public void setFechaUltClave(LocalDateTime fechaUltClave) { this.fechaUltClave = fechaUltClave; }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
