package co.edu.unbosque.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AUDITORIA")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOG")
    private Long idLog;

    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "ACCION", length = 20)
    private String accion;

    @Column(name = "TABLA_AFECTADA", length = 50)
    private String tablaAfectada;

    @Column(name = "ID_REGISTRO_AFECTADO")
    private Integer idRegistroAfectado;

    @Column(name = "FECHA_HORA")
    private LocalDateTime fechaHora;

    @Column(name = "IPCLIENTE", length = 100)
    private String ipCliente;

    public Auditoria() {
        this.fechaHora = LocalDateTime.now();
    }

    public Long getIdLog() { return idLog; }
    public void setIdLog(Long idLog) { this.idLog = idLog; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }

    public Integer getIdRegistroAfectado() { return idRegistroAfectado; }
    public void setIdRegistroAfectado(Integer idRegistroAfectado) { this.idRegistroAfectado = idRegistroAfectado; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getIpCliente() { return ipCliente; }
    public void setIpCliente(String ipCliente) { this.ipCliente = ipCliente; }
}