package entidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HomeSolution implements IHomeSolution {
        HashMap<String , Proyecto> lista_proyectos=new HashMap<>();
        HashMap <Integer , Empleado> lista_empleados =new HashMap<>();
        HashMap <String , Cliente> lista_clientes=new HashMap<>();  //identificador telefono
        Integer numeroDeLegajos=1; //asigna un numero de legajo unico segun el orden
        Integer numeroDeProyecto=0;

        public HomeSolution(){}



        //el apartado de nombre no puede ser vacio y valor debe ser mayor a 0
        public void registrarEmpleado(String nombre, double valor) throws IllegalArgumentException {
            test_datosEmpleado(nombre, valor);

            Contratado empleado=new Contratado(nombre,this.numeroDeLegajos,valor);

            if(!lista_empleados.containsKey(numeroDeLegajos)) add_empleado(nombre, empleado);

        }

        private void add_empleado(String nombre, Empleado empleado) {
            lista_empleados.put(empleado.getLegajo(), empleado);
            System.out.println("el empleado "+ nombre +" fue añadido a la lista de forma correcta");
            this.numeroDeLegajos++;
        }


        private static void test_datosEmpleado(String nombre, double valor) {
            if(nombre ==null){throw new IllegalArgumentException("el campo nombre no debe estar vacio");}
            if(valor <=0){throw new IllegalArgumentException("el valor hora no puede ser 0 o menor a 0");}
        }

        //registra un empleado de planta permanente, en valor va el valor por dia del empleado,
        // ademas la categoria debe ser valida
        public void registrarEmpleado(String nombre, double valor, String categoria) throws IllegalArgumentException {
            test_datosEmpleado(nombre, valor);
            Permanente empleado=new Permanente(nombre,this.numeroDeLegajos,valor, categoria);
            add_empleado(nombre,empleado);
        }

        //el formato de las fechas debe ser (YYYY-MM-DD)
        //titulo,descripcion y dias deben tener el mismo length y ninguno debe ser null

        public void registrarProyecto(String[] titulos, String[] descripcion, double[] dias, String domicilio, String[] cliente, String inicio, String fin) throws IllegalArgumentException {
            numeroDeProyecto++;
            Proyecto proyecto=new Proyecto(inicio,fin,domicilio,numeroDeProyecto.toString(),cliente[1]);

            Cliente clienteProyecto=new Cliente(cliente[0],cliente[1],cliente[2],domicilio);
            for(int i=0;i<titulos.length;i++) {
                proyecto.AgregarTarea(titulos[i],descripcion[i],dias[i]);
            }
            lista_proyectos.put(numeroDeProyecto.toString(),proyecto);
            lista_clientes.put(clienteProyecto.getEmail(),clienteProyecto);
        }

        @Override
        public void asignarResponsableEnTarea(Integer numero, String titulo) throws Exception {
            Proyecto proyecto = manejarNUllProyecto(numero);

            Tarea tarea = proyecto.getTarea(titulo);
            verificar_tareaYresponsable(titulo, tarea);

            for (Empleado empleado : lista_empleados.values()) {
                if (empleado.consultar_Disponibilidad()) {

                    // Antes de asignar, le informamos al proyecto sobre el costo.
                    proyecto.registrarAsignacion(tarea, empleado);

                    proyecto.asignarEmpleado(empleado.getLegajo(), titulo);
                    empleado.marcar_ocupado();
                    return;
                }
            }
            throw new Exception("No se encontraron empleados disponibles");
        }



    @Override
        public void asignarResponsableMenosRetraso(Integer numero, String titulo) throws Exception {

        Proyecto proyecto = lista_proyectos.get(numero.toString());
        verificar_proyecto(numero, proyecto);

        Empleado empleadoEficiente = traerEmpleado_responsable();
            Tarea tarea = proyecto.getTarea(titulo);
            verificar_tareaYresponsable(titulo,tarea);
            proyecto.registrarAsignacion(tarea, empleadoEficiente);
            empleadoEficiente.marcar_ocupado();
            System.out.println("el empleado mas eficiente tiene:"+empleadoEficiente.contRetrasos);
            proyecto.asignarEmpleado(empleadoEficiente.getLegajo(), titulo);
        }



    public Empleado traerEmpleado_responsable() throws Exception {
            Empleado empleadoEficiente = null;
            // 1. Renombrar la variable y empezar con el valor más alto
            int retrasosMinimos = Integer.MAX_VALUE;

            for (Empleado empleado : lista_empleados.values()) {
                if (empleado.consultar_Disponibilidad()) {
                    // 2. Lógica simplificada: ¿Es este empleado mejor que el mínimo actual?
                    if (empleado.consultar_retrasos() < retrasosMinimos) {

                        // 3. Si es mejor, actualizamos AMBAS variables
                        retrasosMinimos = empleado.consultar_retrasos(); // Guardamos el nuevo récord
                        empleadoEficiente = empleado; // Guardamos al empleado
                    }
                }
            }

            if (empleadoEficiente == null) {
                throw new Exception("No se encontraron empleados disponibles");
            }
            return empleadoEficiente;
        }

        public Proyecto manejarNUllProyecto(Integer numero)throws IllegalArgumentException{
            Proyecto proyecto = lista_proyectos.get(numero.toString());
            if (proyecto == null) {// Lanzamos una excepción
                throw new IllegalArgumentException("El proyecto con ID " + numero.toString() + " no existe.");
            }
            return proyecto;}

        public Empleado manejarNUllEmpleado(Integer numero)throws IllegalArgumentException{
            Empleado empleado = lista_empleados.get(numero);
            if (empleado== null) {// Lanzamos una excepción
                throw new IllegalArgumentException("El proyecto con ID " + numero.toString() + " no existe.");
            }
            return empleado;}

        @Override
        public void registrarRetrasoEnTarea(Integer numero, String titulo, double cantidadDias) throws Exception {
            Proyecto proyecto = manejarNUllProyecto(numero);
            Tarea tarea = proyecto.getTarea(titulo);
            verificar_tarea(titulo,tarea);
            tiene_responsable(tarea);
            Empleado empleado = manejarNUllEmpleado(tarea.getResponsable());

            // LÓGICA DE COSTO AÑADIDA
            // Informamos al proyecto sobre el costo del retraso y la gestión del bono.
            proyecto.registrarCostoRetraso(empleado, cantidadDias);


            // (Esta lógica es para el historial de retrasos del empleado, no para el costo)
            Integer legajo = proyecto.registrarRetraso(titulo, cantidadDias);
            empleado.registrar_retrasos((int)Math.ceil(cantidadDias));
        }

    private static void tiene_responsable(Tarea tarea) {
        if (tarea.getResponsable() == null) {
            throw new IllegalArgumentException("No se puede registrar retraso en tarea no asignada");
        }
    }


    @Override
        public void agregarTareaEnProyecto(Integer numero, String titulo, String descripcion, double dias) throws IllegalArgumentException {
            Proyecto proyecto = manejarNUllProyecto(numero);
            proyecto.AgregarTarea(titulo,descripcion,dias);
        }

        @Override
        public void finalizarTarea(Integer numero, String titulo) throws Exception {
            Proyecto proyecto = manejarNUllProyecto(numero);
            Integer legajo=proyecto.terminarTarea(titulo);
            Empleado empleadoDesocupado= lista_empleados.get(legajo);
            empleadoDesocupado.liberar();
        }

        @Override
        public void finalizarProyecto(Integer numero, String fin) throws IllegalArgumentException {
            Proyecto proyecto=manejarNUllProyecto(numero);
            Object[] lista=proyecto.terminarProyecto(fin);

            for (Object o : lista) {
                Empleado empleado;
                empleado = lista_empleados.get(o);
                if(empleado==null){
                    continue;
                }
                empleado.liberar();
            }

        }

        @Override
        public void reasignarEmpleadoEnProyecto(Integer numero, Integer legajo, String titulo) throws Exception {
            Proyecto proyecto = manejarNUllProyecto(numero);
            Tarea tarea = proyecto.getTarea(titulo);
            if (tarea == null) {
                throw new IllegalArgumentException("Tarea no encontrada");
            }

            Integer legajoViejo = tarea.getResponsable();
            if (legajoViejo == null) {
                throw new Exception("La tarea no tiene un empleado para reasignar");
            }

            Empleado empleadoViejo = manejarNUllEmpleado(legajoViejo);
            Empleado empleadoNuevo = manejarNUllEmpleado(legajo);

            if (!empleadoNuevo.consultar_Disponibilidad()) {
                throw new Exception("El empleado " + legajo + " no está disponible");
            }

            //logica del costo

            // Quitamos el costo del empleado viejo
            proyecto.registrarDesasignacion(tarea, empleadoViejo);

            // Añadimos el costo del empleado nuevo
            proyecto.registrarAsignacion(tarea, empleadoNuevo);

            proyecto.reasignar(legajo, titulo);

            // Liberamos al viejo y ocupamos al nuevo
            empleadoViejo.liberar();
            empleadoNuevo.marcar_ocupado();
        }

        @Override
        public void reasignarEmpleadoConMenosRetraso(Integer numero, String titulo) throws Exception {
            Proyecto proyecto=manejarNUllProyecto(numero);
            proyecto.reasignar(traerEmpleado_responsable().getLegajo(),titulo);
        }

        @Override
        public double costoProyecto(Integer numero) {
            Proyecto proyecto = manejarNUllProyecto(numero);

            //Debe ser O(1)
            return proyecto.getCostoTotalFinal();
        }

        @Override
        public List<Tupla<Integer, String>> proyectosFinalizados() {
            return List.of();
        }

        @Override
        public List<Tupla<Integer, String>> proyectosPendientes() {
            ArrayList<Tupla<Integer,String>> pendientes=new ArrayList<>();
            for(Proyecto proyecto:this.lista_proyectos.values()){
                if(proyecto.getEstado_proyecto().equals(Estado.pendiente)) {
                    Tupla<Integer, String> tupla = new Tupla(Integer.valueOf(proyecto.obtenerNumero()), proyecto.obtenerDomicilio());
                    pendientes.add(tupla);
                }
            }
            return pendientes;
        }

        @Override
        public List<Tupla<Integer, String>> proyectosActivos() {
            ArrayList<Tupla<Integer,String>> activos=new ArrayList<>();
            for(Proyecto proyecto:this.lista_proyectos.values()){
                if(proyecto.getEstado_proyecto().equals(Estado.activo)) {
                    Tupla<Integer, String> tupla = new Tupla(Integer.valueOf(proyecto.obtenerNumero()), proyecto.obtenerDomicilio());
                    activos.add(tupla);
                }
            }
            return activos;
        }

        @Override
        public Object[] empleadosNoAsignados() {

            ArrayList<String> noAsignados=new ArrayList<>();

            for (Empleado empleado : lista_empleados.values()) {

                if (empleado.consultar_Disponibilidad()) {
                    // 3. Asignar el empleado.
                    noAsignados.add(empleado.getLegajo().toString());
                }
            }
            return noAsignados.toArray();
        }

        @Override
        public boolean estaFinalizado(Integer numero) {
            Proyecto proyecto=manejarNUllProyecto(numero);
            return proyecto.getEstado_proyecto().equals(Estado.finalizado);
        }

        @Override
        public int consultarCantidadRetrasosEmpleado(Integer legajo) {
            Empleado empleado=lista_empleados.get(legajo);
            if(empleado==null){throw new IllegalArgumentException("empleado no registrado");}
            return empleado.consultar_retrasos();
        }

        @Override
        public List<Tupla<Integer, String>> empleadosAsignadosAProyecto(Integer numero) {
            Proyecto proyecto=manejarNUllProyecto(numero);
            Set<Integer> empleados=proyecto.devolverEmpleados();
            Tupla<Integer, String> tupla;
            ArrayList<Tupla<Integer, String>> lista=new ArrayList<>();
            for(Integer legajo: empleados){
                Empleado empleado =lista_empleados.get(legajo);
                System.out.println(empleado.nombre+" "+empleado.consultar_retrasos());
                tupla=new Tupla<>(empleado.getLegajo(),empleado.getNombre());
                lista.add(tupla);
            }
            return lista;
        }

        @Override
        public Object[] tareasProyectoNoAsignadas(Integer numero) throws IllegalArgumentException{
            Proyecto proyecto=manejarNUllProyecto(numero);
            if (proyecto.getEstado_proyecto().equals(Estado.finalizado)){
                throw new IllegalArgumentException("el proyecto proporcionado esta marcado como finalizado");
            }
            return proyecto.tareas_sin_asginar().toArray();
        }

        @Override
        public Object[] tareasDeUnProyecto(Integer numero) {
            Proyecto proyecto=manejarNUllProyecto(numero);

            return proyecto.tareas_sin_asginar().toArray();
        }

        @Override
        public String consultarDomicilioProyecto(Integer numero) {
            Proyecto proyecto=manejarNUllProyecto(numero);
            return proyecto.obtenerDomicilio();
        }

        @Override
        public boolean tieneRestrasos(Integer legajo) {
            Empleado empleado=lista_empleados.get(legajo);
            return empleado.consultar_retrasos() != 0;
        }

        @Override
        public List<Tupla<Integer, String>> empleados() {
            ArrayList<Tupla<Integer, String>> lista=new ArrayList<>();
            for(Empleado empleado:lista_empleados.values()){
                Tupla<Integer, String> tupla = new Tupla<>(empleado.getLegajo(),empleado.nombre);
                lista.add(tupla);
            }
            return lista;
        }

        @Override
        public String consultarProyecto(Integer numero) {
            Proyecto proyecto=manejarNUllProyecto(numero);
            return proyecto.toString();
        }

    private static void verificar_proyecto(Integer numero, Proyecto proyecto) throws Exception {
        if (proyecto == null) {
            throw new Exception("El proyecto con ID " + numero.toString() + " no existe.");
        }
        if (proyecto.getEstado_proyecto().equals(Estado.finalizado)) {
            throw new RuntimeException("El proyecto ya está finalizado");
        }
    }

    private static void verificar_tareaYresponsable(String titulo, Tarea tarea) throws Exception {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea " + titulo + " no existe.");
        }
        if (tarea.getResponsable() != null) {
            throw new Exception("La tarea "+titulo+" ya está asignada");
        }
    }
    private static void verificar_tarea(String titulo, Tarea tarea) throws Exception {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea " + titulo + " no existe.");
        }
    }

    public <T> boolean verificarLista(T identificador, HashMap<T,?> lista){
        return lista.containsKey(identificador);
    }

    }


