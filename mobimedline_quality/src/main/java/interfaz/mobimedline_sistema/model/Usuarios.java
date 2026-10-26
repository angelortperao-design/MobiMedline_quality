package interfaz.mobimedline_sistema.model;

/**
 * Representa la entidad de Usuario dentro del sistema MobiMedline,
 * gestionando tanto los datos de autenticación como los estados operativos
 * de control de acceso.
 * @version 1.2
 */
public class Usuarios {
    
    public static final String ROL_GERENTE = "Gerente";
    public static final String ROL_EMPLEADO = "Empleado";
    
    // --- Atributos Originales --- 
    private String usuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String contraseña;
    private boolean permisos; // true = Gerente / Administrador, false = Empleado
    
    // --- Nuevos Campos Operativos (Control de Accesos y Estado) ---
    private String correo = "";
    private String telefono = "";
    private boolean aprobado = false; // true = registros validados, false = Nuevos registros sin validar
    private boolean activo = false;   // true = Acceso Permitido / Activo, false = Acceso Denegado / Suspendido
    private String codigoVerificacion = ""; 
            
    // --- Constructor Original ---
    public Usuarios(String usuario, String nombre, String apellidoP, String apellidoM, String contraseña){
        this.usuario = usuario; 
        this.nombre = nombre;
        apellidoPaterno = apellidoP;
        apellidoMaterno = apellidoM;
        this.contraseña = contraseña;
        this.permisos = false;
        this.aprobado = true;
        this.activo = true;
        this.codigoVerificacion = "";
    }
    
    // --- Constructor Para el Control de Accesos completo ---
    public Usuarios(String usuario, String nombre, String apellidoP, String apellidoM, String contraseña, String correo, String telefono){
        this(usuario, nombre, apellidoP, apellidoM, contraseña);
        this.correo = correo;
        this.telefono = telefono;
        this.aprobado = false;
        this.activo = true;
        this.permisos = false;
    }
    
    // --- Metodos de ayuda --
    public String getNombreCompleto() {
        String apMat = (apellidoMaterno != null) ? " " + apellidoMaterno : "";
        return (nombre != null ? nombre : "") + " " + (apellidoPaterno != null ? apellidoPaterno : "") + apMat;
    }
    
    public String getRol() {
        return this.permisos ? ROL_GERENTE : ROL_EMPLEADO;
    }

    public void setRol(String rol) {
        if (rol != null) {
            this.permisos = rol.equalsIgnoreCase("Gerente");
        }
    }
    
    public String getEstadoTexto() {
        return this.activo ? "activo" : "inactivo";
    }
            
    // --- Getters Originales ---
    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean getPermisos() {
        return permisos;
    }

    public String getUsuario() {
        return usuario;
    }
    
    // --- Nuevos Getters Operativos ---
    public String getCorreo() {
        return correo != null ? correo : "";
    }

    public String getTelefono() {
        return telefono != null ? telefono : "";
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public boolean getEstado() {
        return activo;
    }
    
    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }
    
    // --- Setters Originales ---
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public void setPermisos(boolean tipoUsuario) {
        this.permisos = tipoUsuario;
    }   

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    // --- Nuevos Setters Operativos ---
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public void SetCodigoVerificacion(String codigo) {
        this.codigoVerificacion = codigo;
    }
}