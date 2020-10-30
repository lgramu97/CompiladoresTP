package componentes;

public class SimboloPolaca {

    public static final String BF = "BF";
    public static final String BI = "BI";
    private String simbolo;

    public SimboloPolaca(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

}
