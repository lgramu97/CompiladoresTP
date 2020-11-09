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
import static componentes.AnalizadorLexico.PROC;

public class Compilador {

  private ArrayList<ArrayList<SimboloPolaca>> polaca;
  private ArrayList<String> assembler;
  private ArrayList<String> procsAsm;
  private HashMap<String, HashMap<String, Object>> tablaSimbolos;
  public static final String ERROR_DIVISION_CERO = "ERROR_DIVISION_CERO";
  public static final String ERROR_OVERFLOW_SUMA = "ERROR_OVERFLOW_SUMA";
  public static final String INV = "INV";
  public static final String PARAMETROS = "Parametros";


  public Compilador(HashMap<String, HashMap<String, Object>> tablaSimbolos, ArrayList<ArrayList<SimboloPolaca>> polaca) {
    this.tablaSimbolos = tablaSimbolos;
    this.polaca = polaca;
    this.assembler = new ArrayList<>();
  }

  private void generateHeader() {
    assembler.add(".386");
    assembler.add(".STACK 200h");
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
    HashMap<String, String> numbers = new HashMap<>();
    assembler.add(".data");
    assembler.add(ERROR_DIVISION_CERO + " db " + "Error: no es posible divir por cero.");
    assembler.add(ERROR_OVERFLOW_SUMA + " db " + "Error: overflow en suma.");
    for (String key : tablaSimbolos.keySet()) {
      addData(cadenaVar, numbers, key);
    }
  }

  private void addData(HashMap<String, String> cadenaVar, HashMap<String, String> numbers, String key) {
    HashMap<String, Object> atributos = tablaSimbolos.get(key);
    if (atributos.containsKey(USO)) {
      addVariable(key, atributos);
    } else if(atributos.get(TIPO).equals(CADENA)) {
      addCadena(cadenaVar, key);
    } else {
      addConstante(key, numbers);
    }
    //Ver el caso de constante en codigo. No contiene USO, si se agrega -> addVariable.
  }

  // Lexema: 3
	// Atributo: Tipo   Valor: LONGINT
  private void addConstante(String key, HashMap<String, Object> atributos, HashMap<String, String> numbers) {
    String tipo = (String) atributos.get(TIPO);
    if (tipo.equals(LONGINT) || tipo.equals(FLOAT)) { // Caso FLOAT y LONGINT.
      String number = "_Constante" + numbers.size();
      assembler.add(number + " dd ?");
      numbers.put(key,number);
      // El signo ? indica que no esta inicializado
    }
  }

  private void addVariable(String key, HashMap<String, Object> atributos) {
    String uso = (String) atributos.get(USO);
    if (uso.equals(VARIABLE) || uso.equals(PARAMETRO)) {
      String tipo = (String) atributos.get(TIPO);
      if (tipo.equals(LONGINT) || tipo.equals(FLOAT)) { // Caso FLOAT y LONGINT.
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

  public void generateCode() {
    assembler.add(".code");
    assembler.add("START:");
    ArrayList<SimboloPolaca> main = polaca.get(0);
    boolean invProc = false;
    for (SimboloPolaca simbolo : main) {
      ArrayList<SimboloPolaca> invProcActual;
      if (simbolo.getSimbolo().equals(PROC)) {
        invProc = true;
        // ArrayList<SimboloPolaca> procActual;
        // for (int i = 1; i < polaca.size(); i++) {
        //   if (polaca.get(i))
        // }
      } else if (invProc) {
        if (simbolo.getSimbolo().equals(INV)) {
          invProc = false;
          if (!procsAsm.contains(invProcActual.get(0))) {
            ArrayList<SimboloPolaca> procPolaca;
            for (ArrayList<SimboloPolaca> lista : polaca) {
              if (lista.get(0).equals(invProcActual.get(0))) {
                procPolaca = lista;
              }
            }
            if (checkParams(procPolaca, invProcActual)) {
              declararProc(invProcActual);
            }
          }
        } else {
          invProcActual.add(simbolo);
        }
      } else  {

      }
    }
    assembler.add("invoke ExitProcess, 0"); 
    assembler.add("__etiquetaErrorOverflow__:");
    assembler.add("invoke MessageBox, NULL, addr ERROR_OVERFLOW_SUMA, addr ERROR_OVERFLOW_SUMA, MB_OK");
    assembler.add("invoke ExitProcess, 0"); 
    assembler.add("__etiquetaErrorDivCero__:");
    assembler.add("invoke MessageBox, NULL, addr ERROR_DIVISION_CERO, addr ERROR_DIVISION_CERO, MB_OK");
    assembler.add("invoke ExitProcess, 0");
    assembler.add("END START");
  }

  public ArrayList<String> getParametrosReales(ArrayList<SimboloPolaca> invocacion) {
    //retorno todos los parametros reales del procedimiento.
    String simboloActual;
    ArrayList<String> out = new ArrayList<>();
    for (int i = 0; i < invocacion; i++) {
      simboloActual = simbolo.getSimbolo();
      if (simboloActual.equals(":")) {
        out.add(invocacion.get(i-1).getSimbolo());
      }
      // PROC
      // VALOR POLACA [21]: procedimiento@main
      // VALOR POLACA [22]: w
      // VALOR POLACA [23]: a@main
      // VALOR POLACA [24]: :
      // VALOR POLACA [25]: x
      // VALOR POLACA [26]: a@main
      // VALOR POLACA [27]: :
      // VALOR POLACA [28]: z
      // VALOR POLACA [29]: x1@main
      // VALOR POLACA [30]: :
      // VALOR POLACA [31]: INV}
      return out;
  }

  public String nameMangling(String proc) {
    // Busca desde el ambito actual hacia atra s.
    return proc.substring(proc.indexOf('@'),proc.length());
  }

  public boolean checkParams(ArrayList<SimboloPolaca> declaracion, ArrayList<SimboloPolaca> invocacion) {
    HashMap<String, Object> atributos = tablaSimbolos.get(invocacion.get(0));
    ArrayList<String> paramsFormales = atributos.get(PARAMETROS);
    ArrayList<String> paramsReales = getParametrosReales(invocacion);
    for (int i = 0; i < paramsFormales.size(); i++) {
      String paramFormal  = paramsFormales.get(i) + nameMangling(invocacion);
      HashMap<String, Object> atrsParamFormal = tablaSimbolos.get(paramFormal);
      HashMap<String, Object> atrsParamReal = tablaSimbolos.get(paramsReales.get(i));
      if (!atrsParamFormal.get(TIPO).equals(atrsParamReal.get(TIPO))) {
        // Conversiones implicita o error
        if (atrsParamFormal.get(TIPO).equals(FLOAT)) { // Convercion sobre parametro real.
          conversionImplicita(paramsReales.get(i)); //TODO
        } else {
          throw new Error('Conversion implicita no valida!');
          return false;
        }
      }
      // comparo tipos de parametros
      // tengo que buscar a la tabla de simbolos
    }
    return true;
  }

  public void declararProc(ArrayList<SimboloPolaca> polacaProc) {
    
  }
}
