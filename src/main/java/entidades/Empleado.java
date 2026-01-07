package entidades;

public abstract class Empleado {
    //Valores (atributos)
    String nombre; 		// nombre del empleado.
    Integer legajo; 			// identificador único.
    boolean disponible;		//indica si está asignado a una tarea.
    int contRetrasos=0;     //contador de retrasos independiente del subtipo de empleado
    double valor;

    Empleado(String nombre,Integer legajo){
        this.nombre=nombre;
        this.legajo=legajo;
    }

    public String getNombre(){			//devuelve el nombre del empleado.
            return this.nombre;
    }

    public Integer getLegajo(){			//devuelve el número de legajo (identificador).
        return legajo;
    }

    public boolean consultar_Disponibilidad() { 		//indica si el empleado está libre o asignado a una tarea.
        return disponible;
    }

    public void marcar_ocupado(){ 	//marca al empleado como no disponible.
        this.disponible=false;
        System.out.println("el empleado "+this.legajo+" a sido asignado a una tarea y no esta disponible");
    }
    public void liberar(){   // marca al empleado como disponible.
        this.disponible=true;
        System.out.println("el empleado "+this.legajo+" a sido liberado de su tarea y esta disponible");
    }

    public int consultar_retrasos(){ //indica si el empleado tuvo o no retrasos
        return this.contRetrasos;
    }

    public void registrar_retrasos(int n)throws IllegalArgumentException{
        if(n < 0){throw new IllegalArgumentException("El número de días no puede ser negativo");}

        this.contRetrasos = contRetrasos + n;

    }

    public abstract double calcularCostoEmpleado(Double cantidad_dias);

}



