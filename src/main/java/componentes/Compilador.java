package componentes;

import java.util.ArrayList;
import java.util.HashMap;

import static componentes.AnalizadorLexico.CADENA;
import static componentes.AnalizadorLexico.FLOAT;
import static componentes.AnalizadorLexico.LONGINT;
import static componentes.AnalizadorLexico.PARAMETRO;
import static componentes.AnalizadorLexico.TIPO;
import static componentes.AnalizadorLexico.USO;
import static componentes.AnalizadorLexico.VARIABLE;

public class Compilador {

  private ArrayList<SimboloPolaca> polaca;
  private ArrayList<String> assembler;
  private HashMap<String, HashMap<String, Object>> tablaSimbolos;
  public static final String ERROR_DIVISION_CERO = "ERROR_DIVISION_CERO";
  public static final String ERROR_OVERFLOW_SUMA = "ERROR_OVERFLOW_SUMA";

  public Compilador(HashMap<String, HashMap<String, Object>> tablaSimbolos, ArrayList<SimboloPolaca> polaca) {
    this.tablaSimbolos = tablaSimbolos;
    this.polaca = polaca;
    this.assembler = new ArrayList<>();
  }

  private void generateHeader() {
    assembler.add(".386");
    assembler.add(".model flat, stdcall");
    assembler.add("option casemap :none");
    assembler.add("include \\masm32\\include\\windows.inc");
    assembler.add("include \\masm32\\include\\kernel32.inc");
    assembler.add("include \\masm32\\include\\user32.inc");
    assembler.add("includelib \\masm32\\lib\\kernel32.lib");
    assembler.add("includelib \\masm32\\lib\\user32.lib");
  }

  private void generateData() {
    HashMap<String, String> cadenaVar = new HashMap<>();
    assembler.add(".data");
    assembler.add(ERROR_DIVISION_CERO + " db " + "Error: no es posible divir por cero.");
    assembler.add(ERROR_OVERFLOW_SUMA + " db " + "Error: overflow en suma.");
    for (String key : tablaSimbolos.keySet()) {
      addData(cadenaVar, key);
    }
  }

  private void addData(HashMap<String, String> cadenaVar, String key) {
    HashMap<String, Object> atributos = tablaSimbolos.get(key);
    if (atributos.containsKey(USO)) {
      addVariable(key, atributos);
    } else if(atributos.get(TIPO).equals(CADENA)) {
      addCadena(cadenaVar, key);
    }//Ver el caso de constante en codigo. No contiene USO, si se agrega -> addVariable.
  }

  private void addVariable(String key, HashMap<String, Object> atributos) {
    String uso = (String) atributos.get(USO);
    if (uso.equals(VARIABLE) || uso.equals(PARAMETRO)) {
      String tipo = (String) atributos.get(TIPO);
      if (tipo.equals(LONGINT) || tipo.equals(FLOAT)) {
        assembler.add("_" + key + " dd ?");
        // El signo ? indica que no esta inicializado
      }
    }
  }

  private void addCadena(HashMap<String, String> cadenaVar, String key) {
    String var = "_Cadena" + cadenaVar.size();
    assembler.add(var + " db " + key + ", 0");
    cadenaVar.put(key, var);
  }

  // ProgramText db "Hello World!", 0
  // _bad: invoke StdOut, addr ProgramText

}
