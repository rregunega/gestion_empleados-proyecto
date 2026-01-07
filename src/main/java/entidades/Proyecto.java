package entidades;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Proyecto {
    /*Lista de tareas a realizar para finalizar
    * cuando se reasigne una tarea debe ser en O(1)
    * por lo que es necesaria la implementacion de HashMap*/
    private HashSet<String> empleadosConRetraso = new HashSet<>();
    private HashMap<String, Double> bonosActivosPorEmpleado = new HashMap<>();
    private final String Nproyecto;
    private final String domicilio; //domicilio del proyecto
    private final String emailCliente;//email del cliente
    private String estado_proyecto; // verdadero finalizado, false sin terminar

    HashMap<String, Tarea> Lista_tareas=new HashMap<>(); //las tareas se guardan por titulo
    HashMap<Integer, Integer> Lista_retrasos=new HashMap<>(); //key legajo del empleado
    //tupla con un empleado y con su numero de retrasos en un proyecto
    //tambien puede servir como historial de los empleados que pasaron por el proyecto

    //valores para calcular el tiempo dias-meses-años-horas, podemos usar esta clase
    // localdatetime gestiona eso
    private double costoTotalFinalO1 = 0.0;
    // Suma de los costos base (sin margen, sin bono)
    private double costoBaseAcumulado = 0.0;
    // Suma de todos los bonos activos (2%)
    private double bonoActivoAcumulado = 0.0;
    boolean tuvoRetrasoGeneral=false;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate fecha_inicio;
    LocalDate fecha_finalizacion_estimada;
    LocalDate fecha_finalizacion_real;
    //Objeto que almacena la informacion del cliente



    public Tarea manejarNullTarea(String titulo){return null;}

    //el formato debe ser dd-mm-yyyy
    public Proyecto(String fechaInicio,String fechaFinal,String domicilio,String Nproyecto,String emailCliente) throws IllegalArgumentException{

        fecha_inicio=LocalDate.parse(fechaInicio,formato);
        fecha_finalizacion_estimada=LocalDate.parse(fechaFinal,formato);
        if(fecha_inicio.isAfter(fecha_finalizacion_estimada)){throw new IllegalArgumentException("la fecha de fin debe ser despues de la fecha de inicio");}
        this.fecha_inicio=fecha_inicio;
        this.fecha_finalizacion_estimada=fecha_finalizacion_estimada;
        this.estado_proyecto=Estado.pendiente;
        this.domicilio=domicilio;
        this.Nproyecto=Nproyecto;
        this.emailCliente=emailCliente;
    }

    public String obtenerNumero(){return this.Nproyecto;}

    public double getCostoTotalFinal() {
        return this.costoTotalFinalO1;
    }

    public String obtenerDomicilio(){return this.domicilio;}

    public boolean empleadoTuvoRetraso(String legajo) {
        return empleadosConRetraso.contains(legajo);
    }


    public void registrarAsignacion(Tarea tarea, Empleado emp) {
        double diasACobrar = tarea.getDuracionDias();

        // Regla: "medio día, se considera que trabajan el día completo"
        if (emp instanceof Permanente) {
            diasACobrar = Math.ceil(diasACobrar);
        }

        // Calcular costo base
        double costoBase = emp.calcularCostoEmpleado(diasACobrar);
        double bono = 0.0;

        // Calcular bono (si aplica)
        // "recibirán un adicional del 2%"
        // "se toma en cuenta para el costo total al inicio del proyecto"
        if (emp instanceof Permanente) {
            bono = costoBase * 0.02;

            // Guardamos el bono de este empleado para poder quitarlo después si es necesario
            String legajo = emp.getLegajo().toString();
            bonosActivosPorEmpleado.put(legajo, bonosActivosPorEmpleado.getOrDefault(legajo, 0.0) + bono);
        }

        // Recalcular el costo total del proyecto
        actualizarCostos(costoBase, bono);
    }

    public void registrarCostoRetraso(Empleado emp, double diasRetraso) {
        double diasACobrar = diasRetraso;
        String legajo = emp.getLegajo().toString();

        // Aplicamos la misma regla de día completo
        if (emp instanceof Permanente) {
            diasACobrar = Math.ceil(diasACobrar);
        }

        // Marcar que el proyecto tuvo un retraso (para el margen)
        if (!this.tuvoRetrasoGeneral) {
            this.tuvoRetrasoGeneral = true; // [cite: 150]
        }

        // Marcar que este empleado tuvo un retraso
        this.empleadosConRetraso.add(legajo);

        // Calcular costo del retraso
        double costoRetraso = emp.calcularCostoEmpleado(diasACobrar);
        double bonoPerdido = 0.0;

        // Quitar el bono (si lo tenía) [cite: 14]
        if (bonosActivosPorEmpleado.containsKey(legajo)) {
            // "no se abonará el adicional" -> Se quita del costo.
            bonoPerdido = bonosActivosPorEmpleado.remove(legajo);
        }

        // Recalcular el costo total del proyecto
        //    Sumamos el costo del retraso, restamos el bono perdido.
        actualizarCostos(0.0, -bonoPerdido);
    }

    private void actualizarCostos(double costoBaseAdicional, double bonoAdicional) {
        // Actualizar los totales acumulados
        this.costoBaseAcumulado += costoBaseAdicional;
        this.bonoActivoAcumulado += bonoAdicional;

        // Calcular el subtotal (base + bonos activos)
        double subtotal = this.costoBaseAcumulado + this.bonoActivoAcumulado;

        // Determinar el margen
        double margen = this.tuvoRetrasoGeneral ? 0.25 : 0.35;

        // Calcular y guardar el costo final
        this.costoTotalFinalO1 = subtotal + (subtotal * margen);

    }
    /*asigna un empleado a la tarea desde entidades.HomeSolution llama a un objeto tarea
    y usa la funcion para asignar al empleado
    el empleado debe estar disponible y se debe marcar como ocupado una vez que se
    le asigne una tarea*/
    public void asignarEmpleado(Integer legajoEmpleado, String idTarea) throws Exception {
        Tarea tarea = Lista_tareas.get(idTarea);
        if (tarea==null){throw new IllegalArgumentException("la tarea "+idTarea+" no existe en la lista");}
        tarea.asignarEmpleado(legajoEmpleado);
        if (!Lista_retrasos.containsKey(legajoEmpleado)) {
            Lista_retrasos.put(legajoEmpleado, 0); // Inicia su historial de retrasos en 0
        }
        this.estado_proyecto=Estado.activo;
    }

    public void reasignar(Integer legajoEmpleado, String idTarea) throws Exception {
        Tarea tarea = Lista_tareas.get(idTarea);
        if (tarea==null){throw new IllegalArgumentException("la tarea "+idTarea+" no existe en la lista");}

        tarea.reasignarEmpleado(legajoEmpleado);
        if (!Lista_retrasos.containsKey(legajoEmpleado)) {
            Lista_retrasos.put(legajoEmpleado, 0);}

    }

    public void registrarDesasignacion(Tarea tarea, Empleado emp) {
        double diasACobrar = tarea.getDuracionDias();
        String legajo = emp.getLegajo().toString();

        // Regla: "medio día, se considera que trabajan el día completo"
        if (emp instanceof Permanente) {
            diasACobrar = Math.ceil(diasACobrar);
        }

        // Calcular el costo base que vamos a restar
        double costoBase = emp.calcularCostoEmpleado(diasACobrar);
        double bono = 0.0;

        // Calcular el bono que vamos a restar (sólo si todavía lo tiene)
        if (emp instanceof Permanente && bonosActivosPorEmpleado.containsKey(legajo)) {

            // Calculamos el bono que esta tarea había aportado
            double bonoDeEstaTarea = costoBase * 0.02;

            // Lo restamos del total de bonos de ese empleado
            double bonoActualTotal = bonosActivosPorEmpleado.get(legajo);
            double nuevoBonoTotal = bonoActualTotal - bonoDeEstaTarea;

            if (nuevoBonoTotal < 0.01) {
                bonosActivosPorEmpleado.remove(legajo);
            } else {
                bonosActivosPorEmpleado.put(legajo, nuevoBonoTotal);
            }

            bono = bonoDeEstaTarea; // Esta es la cantidad que restaremos del costo total
        }

        // 3. Recalcular el costo total (restando los valores)
        actualizarCostos(-costoBase, -bono);
    }
    /*crea un objeto cliente cuyo puntero ira al objeto cliente, no hay ireps a menos que
    * se quiera sobreescribir la info del mismo*/
    public void asignarCliente(String nombre, String telefono, String email, String direccion){
        Cliente cliente=new Cliente(nombre,telefono,email,direccion);
    }

    public void liberarEmpleado(String idTarea){}

    public Tarea getTarea(String titulo) {
        return Lista_tareas.get(titulo);
    }
    /*crea y registra una tarea nueva, por motivos de implementacion las tareas tienen titulos
    unicos
     */
    public void AgregarTarea(String titulo, String descripcion, double duracionDias)throws IllegalArgumentException{
        if(this.Lista_tareas.containsKey(titulo)){ throw new RuntimeException("la key ya esta incluida en la lista, esta debe ser unica");}
        Tarea tarea=new Tarea(titulo,descripcion,duracionDias);
        this.Lista_tareas.put(titulo,tarea);
        System.out.println("la tarea "+ titulo +" fue añadida de forma exitosa.");
    }

    public String devolverNoAsignadas() {
        return "";
    }

    /*conociendo el tiempo de retraso en dias este se agrega a la fecha de
    finalizacion real y se aumenta el tiempo de realizacion de la tarea en el objeto tarea
     */
    public Integer registrarRetraso(String idTarea,double dias){
        if (!Lista_tareas.containsKey(idTarea)) {
            throw new IllegalArgumentException("la tarea no esta registrada en el proyecto");
        }
        Tarea tarea = Lista_tareas.get(idTarea);

        // 1. Registra el retraso en la tarea (para el costo total)
        tarea.registrarRetraso(dias);

        Integer legajoResponsable = tarea.getResponsable();

        // 2. ¡AQUÍ! Actualiza el historial de retrasos DEL PROYECTO
        if (legajoResponsable != null) {

            // Redondeamos los días para el contador de retrasos
            int diasRetrasoInt = (int) Math.ceil(dias);

            // Obtenemos su retraso actual en este proyecto y le sumamos el nuevo
            int retrasoActual = Lista_retrasos.getOrDefault(legajoResponsable, 0);
            Lista_retrasos.put(legajoResponsable, retrasoActual + diasRetrasoInt);
        }
        this.tuvoRetrasoGeneral=true;

        return legajoResponsable; // Devuelve quién fue
    }

    //marca una tarea como terminada
    public Integer terminarTarea(String idTarea) throws Exception {
        if(!Lista_tareas.containsKey(idTarea)){throw new IllegalArgumentException("la tarea no esta registrada en el proyecto");}
        Tarea tarea=Lista_tareas.get(idTarea);
        if(tarea.obtenerEstado().equals(Estado.finalizado)){
            throw new Exception("la tarea ya estaba marcada como finalizada");
            }
        return tarea.terminarTarea();
    }

    //todas las tareaas deben estar finalizadas
    public Object[] terminarProyecto(String fin){
        LocalDate fecha_fin_real=LocalDate.parse(fin,formato);
        ArrayList<Integer> empleados_para_liberar=new ArrayList<>();
        if(this.fecha_inicio.isAfter(fecha_fin_real) || this.fecha_finalizacion_estimada.isAfter(fecha_fin_real)){
            throw new IllegalArgumentException("la fecha de fin debe ser despues de la fecha de inicio");}
        for(Tarea tarea:Lista_tareas.values()){
            if(!tarea.obtenerEstado().equals(Estado.finalizado)){
                empleados_para_liberar.add(tarea.terminarTarea());}
        }
        this.fecha_finalizacion_real=fecha_fin_real;
        this.estado_proyecto=Estado.finalizado;
        System.out.println("el proyecto numero"+this.Nproyecto+" fue marcado como finalizado");
        return empleados_para_liberar.toArray();
    }

    public Set<Integer> devolverEmpleados(){
        return Lista_retrasos.keySet();
    }

    public List<Tupla<Integer, Double>> CostoProyecto(
    ){
        ArrayList<Tupla<Integer, Double>> Costo_Dias = new ArrayList<>();
        for (Tarea tarea : Lista_tareas.values()) {

            // Verificamos que la tarea tenga un responsable asignado
            if (tarea.getResponsable() != null) {
                // Calculamos el tiempo real trabajado
                double tiempoTotal = tarea.getTiempoReal();

                Tupla<Integer, Double> tupla = new Tupla<>(tarea.getResponsable(), tiempoTotal);
                Costo_Dias.add(tupla);
            }
        }
        return Costo_Dias;
    }

    public List<String> tareas_sin_asginar(){
        ArrayList<String> lista=new ArrayList<>();
        for (Tarea tarea:Lista_tareas.values()){
            if(Objects.equals(tarea.obtenerEstado(), Estado.pendiente)){
                lista.add(tarea.getTitulo());
            }
        }
        return lista;
    }

    public List<String> todas_tareas(){
        ArrayList<String> lista=new ArrayList<>();
        for (Tarea tarea:Lista_tareas.values()){
                lista.add(tarea.getTitulo());
            }
        return lista;
    }
    public String getEstado_proyecto() {
        return estado_proyecto;
    }

    @Override
    public String toString() {
        return "numero de proyecto: "+this.Nproyecto+" " +
                "" +
                "domicilio: "+this.domicilio+
                "" +
                "email del cliente: "+ this.emailCliente+
                "" +
                "fecha de finalizacion estimada: "+this.fecha_finalizacion_estimada+
                "" +
                "tareas sin asignar: "+this.tareas_sin_asginar();
    }
}
