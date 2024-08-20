package org.example.DTO;

public class Contador {
    private int mes;
    private int cantidad;

    public Contador() {
    }

    public Contador(int mes, int cantidad) {
        this.mes = mes;
        this.cantidad = cantidad;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
