package com.orioninc.statsfabric.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Fechas {

    private Fechas() {
    }

    public static double restarFechasSinFinesDeSemana(String fechaInicioStr, String fechaFinStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
        LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

        // Excluir los fines de semana
        Set<Integer> diasNoLaborables = new HashSet<>();
        diasNoLaborables.add(6); // Sábado
        diasNoLaborables.add(7); // Domingo

        long segundosTotales = 0;

        while (!fechaInicio.isAfter(fechaFin)) {
            if (!diasNoLaborables.contains(fechaInicio.getDayOfWeek().getValue())) {
                segundosTotales += ChronoUnit.SECONDS.between(fechaInicio, fechaInicio.plusDays(1).withHour(0).withMinute(0).withSecond(0));
            }
            fechaInicio = fechaInicio.plusDays(1).withHour(0).withMinute(0).withSecond(0);
        }

        return segundosTotales;
    }
}
