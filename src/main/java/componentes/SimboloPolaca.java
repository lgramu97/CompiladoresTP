package componentes;

public class SimboloPolaca {

    public static final String BF = "BF";
    public static final String BI = "BI";
    private String simbolo;
    private String tipo;
    private int reg;

    public SimboloPolaca(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public String isReg() {
        return this.getReg() != -1;
    }

    public String isMem() {
        return this.getReg() == -1;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

    public String getReg() {
        return this.reg;
    }

    public void freeReg() {
        this.setReg(-1);
    }
}
