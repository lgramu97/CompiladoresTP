package componentes;

public class SimboloPolaca {

    public static final String BF = "BF";
    public static final String BI = "BI";
    private String simbolo;
    private int reg;

    public SimboloPolaca(String simbolo) {
        this.simbolo = simbolo;
        this.reg = -1;
    }

    public SimboloPolaca(String simbolo, int reg) {
        this.simbolo = simbolo;
        this.reg = reg;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getSimboloASM() {
        return "_" + simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public boolean isVble() {
        return this.getReg() == -1;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

    public int getReg() {
        return this.reg;
    }

    public void freeReg() {
        this.setReg(-1);
    }
}
