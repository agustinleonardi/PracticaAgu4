package org.example.entidades;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dias")
public class dia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "dia", nullable = false)
    private int dia;
    @Column(name = "mes")
    private int mes;
    @Column(name = "anio")
    private int anio;
    @Column(name = "fin_semana", nullable = false)
    private boolean fin_semana;
    @OneToMany(mappedBy = "dia_trabajo_id", fetch = FetchType.LAZY)
    private List<feriado> feriado;

    public dia(int dia, int mes, int anio, List<org.example.entidades.feriado> feriado) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.feriado = feriado;
    }

    public dia() {
    }

    public boolean isFin_semana() {
        return fin_semana;
    }

    public void setFin_semana(boolean fin_semana) {
        this.fin_semana = fin_semana;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<org.example.entidades.feriado> getFeriado() {
        return feriado;
    }

    public void setFeriado(List<org.example.entidades.feriado> feriado) {
        this.feriado = feriado;
    }
}
