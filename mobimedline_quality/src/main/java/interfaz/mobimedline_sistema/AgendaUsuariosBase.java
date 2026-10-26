/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaz.mobimedline_sistema;

import interfaz.mobimedline_sistema.model.Usuarios;
import java.util.ArrayList;
import java.util.List;

/**
 *Clase dedicada a guardar los usuarios cargados en el codigo
 * @author Mike
 */
public class AgendaUsuariosBase {
    private static List<Usuarios> usuariosBase = new ArrayList<>();
    
    //Se agrego para hacer un puente que cuando se modifique algo se guarde directamente en esta clase de base 
    public static void setUsuariosBase(List<Usuarios> nuevaLista) {
    usuariosBase = nuevaLista;
    }
    
    // Bloque estático para inicializar los Usuarios base una sola vez
    static {
        // --- Usuario Base: Gerente ---
        Usuarios usuario1 = new Usuarios("DOSJ01", "Jhon", "Dhoe", "Smith", "10sey0u");
        usuario1.setPermisos(true); // Damos permiso de administrador
        // Se crea con la plantilla
        usuariosBase.add(usuario1);
        
        // --- Usuario Base: usuario ---
        Usuarios usuario2 = new Usuarios(
            "OIPM01", "Miguel Angel", "Ortiz", "Pérez", "c0mewlth", 
            "miguel@mobimedline.com", "5551234567"
        );
        usuario2.setAprobado(false);
        usuariosBase.add(usuario2);
        
        // --- Usuario Base: usuario ---
        Usuarios usuario3 = new Usuarios(
            "RAAA01", "Alejandro Rodolfo", "Ramirez", "Arzate", "1Oney", 
            "alejandro@mobimedline.com", "5551234534"
        );
        usuario3.setAprobado(false);
        usuariosBase.add(usuario3);
        
        // --- Usuario Base: usuario ---
        Usuarios usuario4 = new Usuarios("GAGV01", "Veronica", "Garcia", "Gonzalez", "n0th1ng");
        // Se crea con la plantilla
        usuariosBase.add(usuario4);
    }
    
    public static List<Usuarios> getUsuariosBase() {
        return usuariosBase;
    }
    
}
