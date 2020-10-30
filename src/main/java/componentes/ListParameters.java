package componentes;

import java.util.ArrayList;

public class ListParameters {
  private String param1 = null;
  private String param2 = null;
  private String param3 = null;
  private int cantidad;
  
  public ListParameters() {
    this.cantidad = 0;
  }
  
  public ListParameters(String param1) {
    this.param1 = param1;
    this.cantidad = 1;
  }
  
  public ListParameters(String param1, String param2) {
    this.param1 = param1;
    this.param2 = param2;
    this.cantidad = 2;
  }

  public ListParameters(String param1, String param2, String param3) {
    this.param1 = param1;
    this.param2 = param2;
    this.param3 = param3;
    this.cantidad = 3;
  }
  
  public ArrayList<String> getParameters() {
    ArrayList<String> out = new ArrayList<>();
    if (param1 != null) {
      out.add(param1);
      if (param2 != null) {
        out.add(param2);
        if (param3 != null) {
          out.add(param3);
        }
      }
    }
    return out;
  }

  public int getCantidad() {
    return cantidad;
  }
}