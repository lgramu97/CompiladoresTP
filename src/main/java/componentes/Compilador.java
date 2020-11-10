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
  private int EAX = 0;
  private int EBX = 1;
  private int ECX = 2;
  private int EDX = 3;
  private boolean[] registrosOcupados = {false, false, false, false};
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

  public boolean checkTipos(String op1, String op2) {
    HashMap<String, Object> attrsOp1 = tablaSimbolos.get(op1);
    HashMap<String, Object> attrsOp2 = tablaSimbolos.get(op2);
    if (!attrsOp1.get(TIPO).equals(attrsOp2.get(TIPO))) {
      // Conversiones implicita o error
      if (!attrsOp1.get(TIPO).equals(FLOAT)) { // Convercion sobre parametro real.
        throw new Error('Conversion implicita no valida!');
        return false;
      }
    }
    return true;
  }

  public String getReg(int i) {
    switch (i) {
      case 0:
        return "EAX";
      case 1:
        return "EBX";
      case 2:
        return "ECX";
      case 3:
        return "EDX";
      default:
        return null;
    }
  }

  public String getRegLibre(String operacion) {
    if (operacion.equals("ADD") || operacion.equals("SUB")) {
      for (int i = 0; i < registrosOcupados.length(); i++) {
        if (!registrosOcupados[i]) {
          registrosOcupados[i] = true;
          return getReg(i);
        }
      }
    } else {
      if (operacion.equals("IMUL")) {
        if (!registrosOcupados[0]) {
          registrosOcupados[0] = true;
          return getReg(0);
        } else {
          
        }
      }
    }
  }

  public void freeReg(SimboloPolaca op) {
    int reg = op.getReg();
    if (reg != -1) {
      op.freeReg();
      registrosOcupados[reg] = false;
    }
  }

  public void generateCodeSUB(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    //TODO:
  }

  public void generateCodeDIV(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    //TODO:
  }
  
  public void generateCodeADD(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    // ADD {__reg__, __mem__}, {__reg__, __mem__, __inmed__} ; Operación: dest <- dest + src
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: Aca generamos instrucciones para FLOAT
    } else {
      // Aca generamos instrucciones para LONGINT
      String reg;
      if (!op1.isReg()) { // op1 es vble
        if (!op2.isReg()) {// Situacion 1: vble1 vble2 OP
          reg = getRegLibre();
          asm.add("MOV " + reg + ", " + op1.getSimbolo());
          asm.add("ADD " + reg + ", " + op2.getSimbolo());
          op1.setReg(reg);
        } else { // Situacion 4.a: vble reg +
          String reg2 = getReg(op2.getReg());
          asm.add("ADD " + reg2 + ", " + op1.getSimbolo());
        }
      } else { // op1 es reg
        reg = getReg(op1.getReg());
        if (!op2.isReg()) { // Situcacion 2: reg vble OP
          asm.add("ADD " + reg + ", " + op2.getSimbolo()); 
        } else { // Situacion 3: reg1 reg2 OP
          String reg2 = getReg(op2.getReg());
          asm.add("ADD " + reg + ", " + reg2);
          freeReg(op2);
        }
      }
      asm.add("JO "+ ERROR_OVERFLOW_SUMA); // Chequear.
    }
  }

  public void generateCodeOPConmutativas() {

  }

  public void generateCodeMUL(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    // IMUL EAX, {__reg__, __mem__} ; EDX: EAX <- EAX * {reg32|mem32|inmed}
    if(checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: instrucciones FLOAT. 
    }
    String reg;
    if (!op1.isReg()) { // op1 es vble 
      if (!op2.isReg()) {// Situacion 1: vble1 vble2 OP
        reg = getRegLibre();
        asm.add("MOV " + reg + ", " + op1.getSimbolo())
        asm.add("IMUL " + reg + ", " + op2.getSimbolo());
        op1.setReg(reg);
      } else { // Situacion 4.a: vble reg +
        String reg2 = getReg(op2.getReg());
        asm.add("IMUL " + reg2 + ", " + op1.getSimbolo());
      }
    }
  }

  public void generateCodeResta()
                                                                
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
    assembler.add("invoke MessageBox, NULL, addr " + ERROR_OVERFLOW_SUMA + ", addr  " + ERROR_OVERFLOW_SUMA + ", MB_OK");
    assembler.add("invoke ExitProcess, 0"); 
    assembler.add("__etiquetaErrorDivCero__:");
    assembler.add("invoke MessageBox, NULL, addr " + ERROR_DIVISION_CERO +  ", addr " + ERROR_DIVISION_CERO +  ", MB_OK");
    assembler.add("invoke ExitProcess, 0");
    assembler.add("END START");
  }

  public ArrayList<String> getParametrosReales(ArrayList<SimboloPolaca> invocacion) {
    //devulve todos los parametros reales del procedimiento.
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
      if (checkTipos(paramFormal, paramsReales.get(i))) {
          conversionImplicita(paramsReales.get(i));
      }
  }

  public void conversionImplicita(String paramReal) {
    // TODO: conversion implicita
  }

  public void declararProc(ArrayList<SimboloPolaca> polacaProc) {
    procsAsm.add(polacaProc.get(0) + ":");
    for (int i = 1; i < polacaProc; i++) {
      // TODO: generar instrucciones
    }
  }
}
