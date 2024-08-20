package org.example.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "feriados")
public class feriado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dia_trabajo_id")
    private dia dia_trabajo_id;

    public feriado(String descripcion, dia dia_trabajo_id) {
        this.descripcion = descripcion;
        this.dia_trabajo_id = dia_trabajo_id;
    }

    public feriado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public dia getDia_trabajo_id() {
        return dia_trabajo_id;
    }

    public void setDia_trabajo_id(dia dia_trabajo_id) {
        this.dia_trabajo_id = dia_trabajo_id;
    }
}
