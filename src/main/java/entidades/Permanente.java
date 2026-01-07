package entidades;


import static entidades.Categoria.categoriasValidas;

public class Permanente extends Empleado {

    String categoria;
    double valorDia;

    public Permanente(String nombre, Integer legajo, double valorDia, String categoria) throws IllegalArgumentException {
        // 1. La llamada a super() debe ser lo primero.
        //    Asumimos que la clase 'super' (Empleado) valida nombre y legajo.
        super(nombre, legajo);

        // 2. Validación para valorDia
        if (valorDia <= 0) {
            throw new IllegalArgumentException("El valor por día debe ser un número positivo. Valor recibido: " + valorDia);
        }

        if (categoria == null || !categoriasValidas.contains(categoria)) {
            throw new IllegalArgumentException("Categoría no válida: '" + categoria + "'. Las categorías permitidas son: " + categoriasValidas.toString());
        }

        // 4. Si todas las validaciones pasan, asignamos los valores.
        this.categoria = categoria;
        this.valorDia = valorDia;
        this.disponible = true;
    }

    public void calcularAdicional(){
    };

    @Override
    public double calcularCostoEmpleado(Double cantidad_dias) {

        if (cantidad_dias < 0) return 0;

        // El costo es directo. El redondeo de días lo hace el Proyecto.
        return this.valorDia * cantidad_dias;
    }}



