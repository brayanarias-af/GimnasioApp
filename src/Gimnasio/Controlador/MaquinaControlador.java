package Gimnasio.Controlador;

import Gimnasio.Modelo.Maquina;
import java.util.ArrayList;
import java.util.List;

public class MaquinaControlador {

    private static final List<Maquina> maquinas = new ArrayList<>();

    static {
        maquinas.add(new Maquina("M001", "Cinta de correr Premium", "Cardio",    "Disponible", "TechnoGym",  "2026-03-10"));
        maquinas.add(new Maquina("M002", "Bicicleta estática",       "Cardio",    "Disponible", "Life Fitness","2026-01-20"));
        maquinas.add(new Maquina("M003", "Prensa de piernas",         "Fuerza",   "En uso",     "Hammer",     "2025-12-05"));
        maquinas.add(new Maquina("M004", "Polea alta/baja",           "Fuerza",   "Disponible", "BH Fitness", "2026-02-14"));
        maquinas.add(new Maquina("M005", "Elíptica",                  "Cardio",   "Mantenimiento","TechnoGym","2026-04-01"));
        maquinas.add(new Maquina("M006", "Banco de pesas",            "Fuerza",   "Disponible", "Domyos",     "2026-01-30"));
    }

    public static List<Maquina> obtenerTodas() { return new ArrayList<>(maquinas); }

    public static boolean agregar(Maquina m) {
        for (Maquina x : maquinas) if (x.getCodigoMaquina().equals(m.getCodigoMaquina())) return false;
        maquinas.add(m);
        return true;
    }

    public static boolean actualizar(String codigo, Maquina nueva) {
        for (int i = 0; i < maquinas.size(); i++) {
            if (maquinas.get(i).getCodigoMaquina().equals(codigo)) { maquinas.set(i, nueva); return true; }
        }
        return false;
    }

    public static boolean eliminar(String codigo) {
        return maquinas.removeIf(m -> m.getCodigoMaquina().equals(codigo));
    }

    public static int totalMaquinas() { return maquinas.size(); }
    public static long maquinasDisponibles() {
        return maquinas.stream().filter(m -> "Disponible".equals(m.getEstado())).count();
    }
}
