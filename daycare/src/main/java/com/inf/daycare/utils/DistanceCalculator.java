package com.inf.daycare.utils;

public class DistanceCalculator {
    // Método para calcular la distancia entre dos puntos geográficos (Haversine formula)
    // Retorna la distancia en kilómetros
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en kilómetros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distancia en kilómetros
        return distance;
    }

    // Opcional: método para convertir kilómetros a "cuadras" (aproximadamente)
    // 1 cuadra ~ 100 metros = 0.1 km
    public static double convertBlocksToKm(int blocks) {
        return blocks * 0.1;
    }
}
