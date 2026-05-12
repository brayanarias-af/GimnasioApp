package Gimnasio.Controlador;

import Gimnasio.Modelo.Rutina;
import java.util.ArrayList;
import java.util.List;

public class RutinaControlador {

    private static final List<Rutina> rutinas = new ArrayList<>();

    static {
        rutinas.add(new Rutina("R001", "Fuerza Total", "Ganar músculo", 8, "Avanzado",
                "Entrenamiento de fuerza con énfasis en grupos musculares grandes."));
        rutinas.add(new Rutina("R002", "Cardio Blast", "Bajar de peso", 6, "Intermedio",
                "Rutina de cardio HIIT para quema de grasa eficiente."));
        rutinas.add(new Rutina("R003", "Inicio Saludable", "Acondicionamiento", 4, "Principiante",
                "Rutina base para personas que inician en el gimnasio."));
        rutinas.add(new Rutina("R004", "Resistencia Extrema", "Resistencia", 12, "Avanzado",
                "Entrenamiento de resistencia muscular y cardiovascular."));
    }

    public static List<Rutina> obtenerTodas() { return new ArrayList<>(rutinas); }

    public static boolean agregar(Rutina r) {
        for (Rutina x : rutinas) if (x.getCodigoRutina().equals(r.getCodigoRutina())) return false;
        rutinas.add(r);
        return true;
    }

    public static boolean actualizar(String codigo, Rutina nueva) {
        for (int i = 0; i < rutinas.size(); i++) {
            if (rutinas.get(i).getCodigoRutina().equals(codigo)) { rutinas.set(i, nueva); return true; }
        }
        return false;
    }

    public static boolean eliminar(String codigo) {
        return rutinas.removeIf(r -> r.getCodigoRutina().equals(codigo));
    }

    public static int totalRutinas() { return rutinas.size(); }
}
