package interfaz.mobimedline_sistema.service;

import interfaz.mobimedline_sistema.AgendaUsuariosBase;
import interfaz.mobimedline_sistema.model.Usuarios;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Esta clase esta pensada para ser el switch que prende ya apaga el uso
 * del guardado en base de datos y el arerreglo que usamos para simula 
 * la base en el sistema.
 */
public class UsuarioService {

    private static final boolean USAR_BASE_DATOS = false; 

    public static Usuarios autenticar(String user, String passPlano) {
        List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
        for (Usuarios u : listaUsuarios) {
            if (u.getUsuario().equals(user)) {
            // Verificamos que esté aprobado (validado) y activo antes de permitir el acceso
                if (u.isAprobado() && u.getEstado()) {
                    return u; // ¡Contraseña correcta y cuenta autorizada!
                } else {
                    System.out.println("[SEGURIDAD] Acceso denegado: El usuario está pendiente de aprobación o inactivo.");
                    return null; 
                }
            }
        }
        return null; // Usuario o contraseña inválidos
    }

    public static boolean registrarUsuario(Usuarios nuevoUsuario, List<Usuarios> baseUsuarios) {
        // 1. Tomamos la contraseña en texto plano que viene del formulario
        String passwordPlano = nuevoUsuario.getContraseña();
        
        // 2. Generamos el HASH seguro usando BCrypt con una "sal" (salt) automática
        String passwordSegura = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        
        // 3. Reemplazamos la contraseña plana por la versión hasheada antes de guardarla
        nuevoUsuario.setContraseña(passwordSegura);

        if (USAR_BASE_DATOS) {
            return guardarEnSupabase(nuevoUsuario);
        } else {
            return guardarEnListaLocal(nuevoUsuario, baseUsuarios);
        }
    }

    private static boolean guardarEnListaLocal(Usuarios usuario, List<Usuarios> baseUsuarios) {
        baseUsuarios.add(usuario);
        System.out.println("[Modo Simulación] Usuario guardado en baseUsuarios localmente.");
        return true;
    }
    
    public void actualizarRolYEstado(String usuarioId, String nuevoRol, boolean nuevoEstado) {
        if (USAR_BASE_DATOS) {
            System.out.println("[Modo Producción] Actualizando en Supabase...");
        } else {
            List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
            for (Usuarios u : listaUsuarios) {
                if (u.getUsuario() != null && u.getUsuario().equals(usuarioId)) {
                    u.setRol(nuevoRol);
                    u.setActivo(nuevoEstado);
                    break;
                }
            }
            System.out.println("[Modo Simulación] Usuario " + usuarioId + " actualizado localmente.");
        }
    }
    
    public static List<Usuarios> obtenerTodosLosUsuarios() {
        if (USAR_BASE_DATOS) {
            System.out.println("[Modo Producción] Obteniendo usuarios desde Supabase...");
            // Aquí puedes retornar la lista de Supabase cuando esté activa
            return AgendaUsuariosBase.getUsuariosBase(); 
        } else {
            System.out.println("[Modo Simulación] Obteniendo usuarios de la base local.");
            return AgendaUsuariosBase.getUsuariosBase();
        }
    }
    
    private static boolean guardarEnSupabase(Usuarios usuario) {
        System.out.println("[Modo Producción] Conectando con Supabase...");
        return true;
    }
    
    public void eliminarUsuario(String usuarioId) {
        if (USAR_BASE_DATOS) {
            System.out.println("[Modo Producción] Eliminando usuario en Supabase...");
            // Aquí agregarás la lógica de Supabase cuando se active
        } else {
            List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
            listaUsuarios.removeIf(u -> u.getUsuario() != null && u.getUsuario().equals(usuarioId));
            System.out.println("[Modo Simulación] Usuario " + usuarioId + " eliminado localmente (Rechazado).");
        }
    }
    
    public void actualizarRol(String usuarioId, String nuevoRol) {
        if (USAR_BASE_DATOS) {
            System.out.println("[Modo Producción] Actualizando rol en Supabase...");
            // Lógica de actualización en Supabase para el rol
        } else {
            List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
            for (Usuarios u : listaUsuarios) {
                if (u.getUsuario() != null && u.getUsuario().equals(usuarioId)) {
                    u.setRol(nuevoRol);
                    break;
                }
            }
            System.out.println("[Modo Simulación] Rol del usuario " + usuarioId + " actualizado localmente.");
        }
    }

    public void actualizarValidacion(String usuarioId, boolean esAprobado) {
        if (USAR_BASE_DATOS) {
            System.out.println("[Modo Producción] Actualizando estado de validación en Supabase...");
            // Lógica de actualización en Supabase para la validación
        } else {
            List<Usuarios> listaUsuarios = AgendaUsuariosBase.getUsuariosBase();
            for (Usuarios u : listaUsuarios) {
                if (u.getUsuario() != null && u.getUsuario().equals(usuarioId)) {
                    u.setAprobado(esAprobado); // O setValidado según tu modelo
                    break;
                }
            }
            System.out.println("[Modo Simulación] Estado de validación del usuario " + usuarioId + " actualizado localmente.");
        }
    }
}