package entidades;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Categoria {
    public static final String inicial="INICIAL";
    public static final String tecnico="TECNICO";
    public static final String experto="EXPERTO";

    public static final List<String> categoriasValidas = Arrays.asList(inicial,tecnico,experto);

}
