package interfaz.mobimedline_sistema.controllers;

import interfaz.mobimedline_sistema.model.Usuarios;
import interfaz.mobimedline_sistema.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ControlEmpleadosController {

    @FXML private TableView<Usuarios> tblUsuarios;
    @FXML private TableColumn<Usuarios, String> colUsuario;
    @FXML private TableColumn<Usuarios, String> colNombre;
    @FXML private TableColumn<Usuarios, String> colCorreo;
    @FXML private TableColumn<Usuarios, String> colTelefono;
    @FXML private TableColumn<Usuarios, Boolean> colEstado;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroEstado;
    
    // Contadores para el personal validado
    @FXML private Label lblTotalRegistros;
    @FXML private Label lblActivos;
    @FXML private Label lblInactivos;

    // Componentes del panel inferior de acciones offline
    @FXML private VBox panelAcciones;
    @FXML private Label lblTituloAcciones;
    @FXML private ComboBox<String> cmbRolAccion;
    @FXML private ComboBox<String> cmbEstadoAccion;
    @FXML private Button btnGuardarCambios;

    private final ObservableList<Usuarios> listaValidados = FXCollections.observableArrayList();
    private FilteredList<Usuarios> listaFiltrada;
    private final UsuarioService usuarioService = new UsuarioService();
    private Usuarios usuarioSeleccionado;

    @FXML
    public void initialize() {
        configurarColumnas();
        configurarFiltros();
        configurarPanelInferior();
        cargarDatos();
    }

    private void configurarColumnas() {
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        // Renderizado visual del Estado booleano (activo / inactivo)
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean estado, boolean empty) {
                super.updateItem(estado, empty);
                if (empty || estado == null) {
                    setText(null);
                    setStyle("");
                } else if (estado) {
                    setText("activo");
                    setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
                } else {
                    setText("inactivo");
                    setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
                }
            }
        });

        // Aplicar ajuste automático de columnas desde Java para evitar errores de FXML
        tblUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Escucha de selección en la tabla para cargar el panel inferior
        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            usuarioSeleccionado = newVal;
            cargarDatosEnPanelInferior(newVal);
        });
    }

    private void configurarFiltros() {
        if (cmbFiltroEstado != null) {
            cmbFiltroEstado.setItems(FXCollections.observableArrayList("Todos", "Activos", "Inactivos"));
            cmbFiltroEstado.setValue("Todos");
            cmbFiltroEstado.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
        }

        listaFiltrada = new FilteredList<>(listaValidados, p -> true);

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
        }

        tblUsuarios.setItems(listaFiltrada);
    }

    private void configurarPanelInferior() {
        cmbRolAccion.setItems(FXCollections.observableArrayList("Empleado", "Gerente"));
        cmbEstadoAccion.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
        setPanelAccionesHabilitado(false);
    }

    private void cargarDatosEnPanelInferior(Usuarios user) {
        if (user != null) {
            lblTituloAcciones.setText("Acciones para el Empleado Seleccionado (Usuario: " + user.getUsuario() + ")");
            cmbRolAccion.setValue(user.getRol() != null ? user.getRol() : "Empleado");
            cmbEstadoAccion.setValue(user.getEstado() ? "Activo" : "Inactivo");
            setPanelAccionesHabilitado(true);
        } else {
            lblTituloAcciones.setText("Acciones para el Usuario Seleccionado (Ninguno)");
            setPanelAccionesHabilitado(false);
        }
    }

    private void setPanelAccionesHabilitado(boolean habilitado) {
        cmbRolAccion.setDisable(!habilitado);
        cmbEstadoAccion.setDisable(!habilitado);
        btnGuardarCambios.setDisable(!habilitado);
    }

    @FXML
    public void handleGuardarCambios() {
        if (usuarioSeleccionado == null) return;

        String nuevoRol = cmbRolAccion.getValue();
        boolean nuevoEstadoBool = "Activo".equals(cmbEstadoAccion.getValue());

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Guardar Cambios Operativos");
        alert.setHeaderText(null);
        alert.setContentText("¿Desea actualizar la configuración para el usuario " + usuarioSeleccionado.getUsuario() + "?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            usuarioSeleccionado.setRol(nuevoRol);
            usuarioSeleccionado.setActivo(nuevoEstadoBool);
            
            // Persistir cambios mediante el servicio
            usuarioService.actualizarRolYEstado(usuarioSeleccionado.getUsuario(), nuevoRol, nuevoEstadoBool);
            
            tblUsuarios.refresh();
            actualizarContadores();
            
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Éxito");
            info.setHeaderText(null);
            info.setContentText("Los cambios se aplicaron correctamente.");
            info.showAndWait();
        }
    }

    private void aplicarFiltros() {
        String busqueda = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";
        String filtroEstado = cmbFiltroEstado != null ? cmbFiltroEstado.getValue() : "Todos";

        listaFiltrada.setPredicate(user -> {
            boolean coincideBusqueda = busqueda.isEmpty()
                    || (user.getNombreCompleto() != null && user.getNombreCompleto().toLowerCase().contains(busqueda))
                    || (user.getCorreo() != null && user.getCorreo().toLowerCase().contains(busqueda))
                    || (user.getUsuario() != null && user.getUsuario().toLowerCase().contains(busqueda));

            boolean coincideEstado = true;
            if ("Activos".equals(filtroEstado)) {
                coincideEstado = user.getEstado();
            } else if ("Inactivos".equals(filtroEstado)) {
                coincideEstado = !user.getEstado();
            }

            // Mantiene el filtro estricto de solo usuarios validados
            return coincideBusqueda && coincideEstado && user.isAprobado();
        });

        actualizarContadores();
    }

    @FXML
    public void handleRefrescar() {
        cargarDatos();
    }

    private void cargarDatos() {
        listaValidados.clear();
        // Cargar únicamente los registros que tienen validado == true
        var todos = usuarioService.obtenerTodosLosUsuarios();
        for (Usuarios u : todos) {
            if (u.isAprobado()) {
                listaValidados.add(u);
            }
        }
        tblUsuarios.getSelectionModel().clearSelection();
        actualizarContadores();
    }

    private void actualizarContadores() {
        long totalRegistros = listaValidados.size();
        long activos = listaValidados.stream().filter(Usuarios::getEstado).count();
        long inactivos = listaValidados.stream().filter(u -> !u.getEstado()).count();

        if (lblTotalRegistros != null) lblTotalRegistros.setText("Total: " + totalRegistros);
        if (lblActivos != null) lblActivos.setText("Activos: " + activos);
        if (lblInactivos != null) lblInactivos.setText("Inactivos: " + inactivos);
    }
}