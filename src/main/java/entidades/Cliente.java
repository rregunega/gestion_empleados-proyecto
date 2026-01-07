package entidades;

public class Cliente {

String nombre;
String telefono;
String email;
String direccion;


    public Cliente(String nombre,String email,String telefono,String direccion) {

        this.nombre=nombre;
        this.telefono=telefono;
        this.direccion=direccion;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }
}