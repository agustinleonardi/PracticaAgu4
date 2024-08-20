package org.example;

import org.example.DTO.Contador;
import org.example.DTO.DiaDTO;
import org.example.DTO.DiaFeriado;
import org.example.servicios.feriadoService;

import java.util.Collection;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        feriadoService feriadoServie = new feriadoService();

        DiaFeriado diaStart = new DiaFeriado();
        DiaFeriado diaFin = new DiaFeriado();

        diaStart.setDia(28);
        diaStart.setMes(1);
        diaStart.setAnio(2024);

        diaFin.setDia(27);
        diaFin.setMes(5);
        diaFin.setAnio(2024);

        List<DiaDTO> resultList = feriadoServie.diasLaboralesEntreFechas(diaStart,diaFin);
        System.out.println(resultList.stream().count());


        List<Contador> contadors = feriadoServie.cantidadaFeriadosPorMes(2024);
        for(Contador c : contadors){
            System.out.println(c.getMes()+ " " + c.getCantidad());
        }

        double l = feriadoServie.promedioDiasLaborales(5);
        System.out.println(l+"%");
    }
}