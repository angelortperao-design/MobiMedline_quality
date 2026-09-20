# Guía de Contribución

## Flujo de Trabajo en Git

Para trabajar de manera ordenada y evitar borrar el trabajo de los demás, utilizaremos una versión simplificada de **Gitflow**:

1. **Rama principal (`main` o `master`):** 
   - Contiene únicamente código estable, probado y listo para entregarse. 
   - **Nadie debe subir cambios directamente a esta rama.**

2. **Ramas de desarrollo (`feature/`):**
   - Cada integrante trabajará en su propia rama para cada tarea o funcionalidad.
   - **Nombre de la rama:** usa minúsculas y guiones cortados describiendo la tarea.
     - Ejemplo: `feature/login-usuario`
     - Ejemplo: `feature/calculo-metricas-psp`
     - Ejemplo: `fix/error-conexion-bd`

3. **Pasos para trabajar en una tarea:**
   1. Actualiza tu rama principal: `git checkout main` y luego `git pull`
   2. Crea tu rama de trabajo: `git checkout -b feature/nombre-de-tu-tarea`
   3. Realiza tus cambios y haz commits periódicos.
   4. Al terminar, sube tu rama al repositorio: `git push origin feature/nombre-de-tu-tarea`
   5. Abre un **Pull Request (PR)** hacia la rama `main`.

---

## Formato de Mensajes de Commit (Especifica la convención)

Cada commit debe ser claro y explicar brevemente qué se hizo. Usaremos el estándar de **Conventional Commits**:

- `feat:` Se añade una nueva funcionalidad.
  - *Ejemplo:* `feat: agregar pantalla de registro de usuarios`
- `fix:` Se corrige un error o bug en el código.
  - *Ejemplo:* `fix: corregir validación de correo electrónico`
- `docs:` Cambios o añadidos en la documentación.
  - *Ejemplo:* `docs: actualizar instrucciones de instalación en README`
- `style:` Cambios de formato, espacios o sangrías (sin alterar la lógica del código).
  - *Ejemplo:* `style: formatear clases del paquete controllers`
- `refactor:` Reorganización o mejora del código sin añadir funcionalidad ni corregir bugs.
  - *Ejemplo:* `refactor: simplificar método de cálculo de horas`

---

## Reglas de Código (Basado en `ConvencionesCodigoJava`)

Para mantener el código legible y consistente entre todos los integrantes, aplicaremos las siguientes reglas básicas de Java:

1. **Nombres de Clases e Interfases:** 
   - Usar **`PascalCase`** (comenzar con mayúscula cada palabra).
   - Utilizar sustantivos representativos.
   - *Ejemplos:* `UsuarioController`, `ConexionBD`, `CalculadoraPSP`.

2. **Nombres de Métodos y Variables:** 
   - Usar **`camelCase`** (comenzar en minúscula y mayúscula en palabras posteriores).
   - Usar verbos para métodos (ej. `obtenerUsuario()`, `calcularTotal()`).
   - Usar nombres descriptivos para variables (evitar nombres de una sola letra como `x`, `a`, `tmp`, excepto en bucles `i`, `j`).
   - *Ejemplos:* `nombreUsuario`, `listaProductos`, `validarCredenciales()`.

3. **Constantes:** 
   - Usar **`UPPERCASE_SNAKE_CASE`** (todo en mayúsculas separado por guiones bajos) junto con `static final`.
   - *Ejemplo:* `public static final int MAX_INTENTOS = 3;`

4. **Nombres de Paquetes:** 
   - Todo en **minúsculas continuas** sin espacios ni guiones.
   - *Ejemplo:* `com.proyectocalidad.controllers`

5. **Estructura y Buenas Prácticas:**
   - **Formatear el código:** Antes de hacer commit, presiona la combinación de teclas de tu IDE para autoformatear (en NetBeans/IntelliJ/Eclipse).
   - **Funciones cortas:** Intenta que un método no sobrepase las 20-30 líneas de código.
   - **Eliminar código muerto:** No dejes código comentado que ya no se utilice.

Nota: para más informacion consulte: \proyecto-calidad\docs\ConvencionesCodigoJava

---

## Proceso de Pull Request (Pasos obligatorios antes de integrar código)

Antes de que el código de una rama individual pase a la rama principal (`main`), debe cumplir con el siguiente protocolo de calidad:

1. **Auto-revisión local:**
   - El código compila correctamente sin errores.
   - No hay advertencias severas ni imports sin usar.
   - Se probaron los escenarios principales de la funcionalidad en el equipo local.

2. **Creación del Pull Request (PR):**
   - Asignar un título claro al PR (ej. `[FEAT] Implementación del módulo de login`).
   - Escribir una breve descripción de lo que incluye el PR y cómo se probó.

3. **Revisión por Pares (Peer Review):**
   - **Obligatorio:** Al menos **1 compañero de equipo** debe revisar el código del PR.
   - Si hay observaciones o sugerencias de corrección, el autor deberá corregirlas en su rama antes de la aprobación.

4. **Fusión (Merge):**
   - Una vez aprobada la revisión, se realiza el *Merge* hacia la rama principal.
   - Borrar la rama remota `feature/...` para mantener el repositorio ordenado.

---

## Ficha Git