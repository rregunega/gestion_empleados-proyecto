package entidades;

public class Tarea {
//valores
private String estado=Estado.pendiente;         //si la tarea esta activa, esta asignada, sino esta pendiente
private boolean asignada=false;       //si la tarea esta asignada o no
private String titulo;			//nombre breve de la tarea.
private String descripcion;		//detalle de lo que implica la tarea.
private double duracionDias;		//tiempo estimado en días (puede ser 0.5).
private double retrasoDias=0;		//días de retraso (0 si no hubo).
private Integer legajo;	// numero de legajo del empleado asignado a la tarea (puede ser null si aún no está asignado).
private double dias=0; //cantidad de días a agregar

//Operaciones

public Tarea(String titulo, String descripcion, double duracionDias){
    this.titulo=titulo;
    this.descripcion=descripcion;
    this.duracionDias=duracionDias;


} //constructor

public String getTitulo(){
    return this.titulo;
} // devuelve el título de la tarea.

    public void getDescripcion(){} // devuelve la descripción.

    //devuelve el estado de la tarea
    public String obtenerEstado(){return this.estado;}

    public double getDuracionDias(){return this.duracionDias;} // devuelve la duración estimada de la tarea.

    public void registrarRetraso(double dias){
    this.retrasoDias+=dias;
    } // suma los días de retraso.

    public void asignarEmpleado(Integer legajo) throws Exception {
    if(this.legajo!= null){throw new Exception("la tarea ya tiene un empleado asignado");}
    this.legajo=legajo;
    this.estado=Estado.activo;
    System.out.println("empleado numero "+legajo+" asignado de forma correcta");

    }// asigna un empleado como responsable; parámetro e = empleado a asignar.

    public void reasignarEmpleado(Integer legajo) throws Exception {
        if(legajo== null){throw new Exception("la tarea no tiene un empleado asignado, por lo que no es posible la reasignacion");}
        this.legajo=legajo;
    }
    public double getTiempoReal(){return this.retrasoDias+this.duracionDias;}
    public Integer getResponsable(){return this.legajo;}// devuelve el empleado asignado, o null si no hay.

    public Integer terminarTarea(){
    this.estado=Estado.finalizado;
    return legajo;}

    public void getRetrasoDias(){} // devuelve el retraso acumulado.


        }
