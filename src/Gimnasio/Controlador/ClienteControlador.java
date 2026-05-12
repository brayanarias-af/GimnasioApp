package Gimnasio.Controlador;

import Gimnasio.Modelo.Entidades.Cliente;
import java.util.ArrayList;
import java.util.List;

public class ClienteControlador {

    private static final List<Cliente> clientes = new ArrayList<>();

    static {
        // Datos de ejemplo
        clientes.add(new Cliente("C001", 75.0, 1.75, "Ganar músculo", "Activa", "2025-01-10",
                null, null,
                "1096000001", "Carlos", "Martínez", 28, "M",
                "3001234567", "carlos@email.com", "Calle 10 #5-20", "cliente1", "pass123"));
        clientes.add(new Cliente("C002", 60.0, 1.62, "Bajar de peso", "Activa", "2025-03-15",
                null, null,
                "1102000002", "María", "García", 24, "F",
                "3107654321", "maria@email.com", "Carrera 8 #12-30", "maria", "maria2026"));
        clientes.add(new Cliente("C003", 85.0, 1.80, "Resistencia", "Vencida", "2024-11-20",
                null, null,
                "1096500003", "Andrés", "Torres", 35, "M",
                "3204445566", "andres@email.com", "Avenida 3 #44-10", "andres", "andres1"));
    }

    public static List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }

    public static boolean agregar(Cliente c) {
        // Verificar código único
        for (Cliente x : clientes) {
            if (x.getCodigoCliente().equals(c.getCodigoCliente())) return false;
        }
        clientes.add(c);
        return true;
    }

    public static boolean actualizar(String codigo, Cliente nuevo) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCodigoCliente().equals(codigo)) {
                clientes.set(i, nuevo);
                return true;
            }
        }
        return false;
    }

    public static boolean eliminar(String codigo) {
        return clientes.removeIf(c -> c.getCodigoCliente().equals(codigo));
    }

    public static Cliente buscarPorCodigo(String codigo) {
        for (Cliente c : clientes) {
            if (c.getCodigoCliente().equals(codigo)) return c;
        }
        return null;
    }

    public static int totalClientes() { return clientes.size(); }

    public static long clientesActivos() {
        return clientes.stream().filter(c -> "Activa".equals(c.getEstadoMembresia())).count();
    }
}
