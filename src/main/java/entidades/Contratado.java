package entidades;

public class Contratado extends Empleado {

double valorHora;

    public Contratado(String nombre, Integer legajo, double valor) {
        super(nombre,legajo);
        this.valorHora=valor;
        this.disponible=true;
    }


    public double getValorHora() {
        return this.valorHora;
}


    @Override
    public double calcularCostoEmpleado(Double cantidad_dias) {
        if (cantidad_dias < 0) return 0;

        // 1 día = 8 horas, 0.5 días = 4 horas
        double cantidad_horas = cantidad_dias * 8.0;

        return cantidad_horas * this.valorHora;}
}
