package componentes;

import java.util.ArrayList;
import java.util.HashMap;
import org.javatuples.Pair;

import static componentes.AnalizadorLexico.CADENA;
import static componentes.AnalizadorLexico.FLOAT;
import static componentes.AnalizadorLexico.LONGINT;
import static componentes.AnalizadorLexico.PARAMETRO;
import static componentes.AnalizadorLexico.TIPO;
import static componentes.AnalizadorLexico.USO;
import static componentes.AnalizadorLexico.VARIABLE;
import static componentes.AnalizadorLexico.PROC;
import static componentes.AnalizadorLexico.REFERENCIA;

public class Compilador {

  private ArrayList<ArrayList<SimboloPolaca>> polaca;
  private ArrayList<String> assembler;
  private ArrayList<String> procsAsm;
  private HashMap<String, HashMap<String, Object>> tablaSimbolos;
  private final int EAX = 0;
  private final int EBX = 1;
  private final int ECX = 2;
  private final int EDX = 3;
  private SimboloPolaca[] regs = {null, null, null, null};
  public static final String ERROR_DIVISION_CERO = "ERROR_DIVISION_CERO";
  public static final String ERROR_OVERFLOW_SUMA = "ERROR_OVERFLOW_SUMA";
  public static final String INV = "INV";
  public static final String PARAMETROS = "Parametros";
  private String condBF;
  private int countVarsAux = 0;

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

  public boolean checkTipos(SimboloPolaca op1, SimboloPolaca op2) {
    HashMap<String, Object> attrsOp1 = tablaSimbolos.get(op1.getSimbolo());
    HashMap<String, Object> attrsOp2 = tablaSimbolos.get(op2.getSimbolo());
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

  public boolean itsBusy(int numReg) {
    return regs[numReg] != null;
  }

  public String getRegLibre(String operacion, SimboloPolaca op, int numOperando) {
    if (operacion.equals("ADD") || operacion.equals("SUB") || operacion.equals(":=") || operacion.equals("CMP")) {
      for (int i = 0; i < regs.length; i++) {
        if (!itsBusy(i)) {
          regs[i] = op;
          op.setReg(i);
          return getReg(i);
        }
      }
    } else if (operacion.equals("IMUL")) {
      if (itsBusy(EAX)) {
        swapRegLibre(EAX);
      }
      regs[EAX] = op;
      op.setReg(EAX);
      return getReg(EAX);
    } else if (operacion.equals("IDIV")) {
      if (itsBusy(EDX)) {
        swapRegLibre(EDX);
      }
      if (numOperando == 1) {
        if (itsBusy(EAX)) {
          swapRegLibre(EAX);
        }
        regs[EAX] = op;
        op.setReg(EAX);
        return getReg(EAX);  // "EAX"
      } else if (numOperando == 2) {
        for (int i = 0; i < regs.length; i++) {
          if (i != EAX && i != EDX && !itsBusy(i)) {
            regs[i] = op;
            op.setReg(i);
            return getReg(i); // "EBX" || "ECX"
          }
        }
      }
    }
  }

  public void swapRegLibre(int reg) {
    if (regs[reg] != null) {
      for (int i = 1; i < regs.length; i++) {
        if (regs[i] == null) {
          regs[i] = regs[reg];
          regs[i].setReg(i);
          regs[reg] = null;
        }
      }
    } 
  }

  public void freeReg(SimboloPolaca op) {
    int reg = op.getReg();
    if (reg != -1) {
      op.freeReg();
      regs[reg] = null;
    }
  }

/*   
  BF -> generateSalto(asm,  tope);
  BI -> generateSalto(asm,  tope); MODIFICAR CONDICION BF ANTES POR "JMP"
*/

/* 
VALOR POLACA [0]: a
VALOR POLACA [1]: b
VALOR POLACA [2]: -
VALOR POLACA [3]: c
VALOR POLACA [4]: 1.0
VALOR POLACA [5]: +
VALOR POLACA [6]: >
VALOR POLACA [7]: 14
VALOR POLACA [8]: BF
VALOR POLACA [9]: b
VALOR POLACA [10]: a
VALOR POLACA [11]: =
VALOR POLACA [12]: 15
VALOR POLACA [13]: BI
VALOR POLACA [14]: L14
VALOR POLACA [15]: L15

Process finished with exit code 0

*/

  public void generateTag(ArrayList<String> asm, SimboloPolaca tag) {
    asm.add(tag.getSimbolo() + ":");
  }

  public void generateSalto(ArrayList<String> asm, SimboloPolaca tag) {
    asm.add(condBF + " L" + tag.getSimbolo());
  }

  public void generateComparacion(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2, String operador) {
    switch (operador) {
      case ">":
        condBF = "JNG"; // <=
        break;
      case "<=":
        condBF = "JNLE"; // >
        break;
      case ">=":
        condBF = "JNGE"; // <
        break;
      case "<":
        condBF = "JNL"; // >=
        break;
      case "==":
        condBF = "JNE"; // !=
        break;
      case "!=":
        condBF = "JE"; // ==
        break;
    }
    reg = getRegLibre("CMP", op1, 1);
    asm.add("MOV " + reg + " , " + op1.getSimbolo());
    asm.add("CMP " + reg + " , " + op2.getSimbolo());
    freeReg(op1);
  }

/*   
  a > b + z
  b z + a >
  R1 a >
  IGUAL JE
  MAYOR JLE 
*/

  public void generateAsignacion(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2, boolean ref) {
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
    }
    String reg;
    // op1 siempre es vble
    if (op2.isVble()) { // op2 es vble
      reg = getRegLibre(":=", op2, 1);
      if (ref) {
        op2.setSimbolo("[" + op2.getSimbolo() + "]");
      }
      asm.add("MOV " + reg + ", " + op2.getSimbolo());
    } else { // op2 es reg
      reg = getReg(op2.getReg());
    }
    asm.add("MOV " + op1.setSimbolo() + ", " + reg);
    freeReg(op2);
  }

  public void generateCodeSUB(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    //SUB {__reg__, __mem__}, {__reg__, __mem__, __inmed__} ; Operación: dest <- dest - src.
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: Aca generamos instrucciones para FLOAT
    } else {
      String reg;
      if (op1.isVble()) { // op1 es vble
        reg = getRegLibre("SUB", op1, 1);
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          asm.add("MOV " + reg + ", " + op1.getSimbolo());
          asm.add("SUB " + reg + ", " + op2.getSimbolo());
        } else { // Situacion 4.b: vble reg -
          String reg2 = getReg(op2.getReg());
          asm.add("MOV " + reg + ", " + op1.getSimbolo());
          asm.add("SUB " + reg + ", " + reg2);
          freeReg(op2);
        }
      } else { // op1 es reg
        reg = getReg(op1.getReg());
        if (op2.isVble()) { // Situcacion 2: reg vble OP
          asm.add("SUB " + reg + ", " + op2.getSimbolo()); 
        } else { // Situacion 3: reg1 reg2 OP
          String reg2 = getReg(op2.getReg());
          asm.add("SUB " + reg + ", " + reg2);
          freeReg(op2);
        }
      }
    }
  }


  /* 
  primero muevo el dividendo a EAX
  tengo que extender el dato a 32 bits.
  si el dato es negativo, tiene que contener todos 1., si no todos 0.
  en lugar de hacerlo a mano ->  CDQ extiendo de EAX a el par EAX:EDX.
  b/a -> EAX : b , CDQ , DIV.
  b a / 
   */
  public void generateCodeDIV(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: Aca generamos instrucciones para FLOAT
    } else {
      String reg, reg2;
      if (op1.isVble()) { // op1 es vble
        reg = getRegLibre("IDIV",op1, 1);// Asigno EAX.
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          reg2 = getRegLibre("IDIV", op2, 2);
          asm.add("MOV " + reg2 + ", " + op2.getSimbolo());
        } else { // Situacion 4.b: vble reg
          reg2 = getReg(op2.getReg()); // Nunca va a ser EAX ni EDX porque ya lo movio getRegLibre
        }
        asm.add("MOV " + reg + ", " + op1.getSimbolo());
        freeReg(op2);
      } else { // op1 es reg
        if (op1.getReg() != EAX) {
          if (itsBusy(EAX)) {
            swapRegLibre(EAX); //Libero EAX.
          }
          regs[EAX] = op1;
          op1.setReg(EAX);
        }
        reg = getReg(EAX); // op1 ya es EAX
        if (itsBusy(EDX)) { // libero EDX si esta ocupado.
          swapRegLibre(EDX);
        }
        if (op2.isVble()) { // Situcacion 2: reg vble OP
          for (int i = 0; i < regs.length; i++) {
            if (i != EAX && i != EDX && !itsBusy(i)) {
              regs[i] = op2;
              op2.setReg(i);
              reg2 = getReg(op2.getReg()); // "EBX" || "ECX"
            }
          }
          asm.add("MOV " + reg2 + ", " + op2.getSimbolo());
        } else { // Situacion 3: reg1 reg2 OP
          // op1 ya es EAX
          // EDX ya esta libre
          reg2 = getReg(op2.getReg()); // "EBX" || "ECX"
        }
        freeReg(op2);
      }
      asm.add("CDQ");
      asm.add("CMP "+  reg2 + " , 0");
      asm.add("JE ERROR_DIVISION_CERO");
      asm.add("IDIV " + reg2);
      // Agregar a la pila SimboloPolaca EAX.
    }
  }
  
  public void generateCodeADD(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    // ADD {__reg__, __mem__}, {__reg__, __mem__, __inmed__} ; Operación: dest <- dest + src
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: Aca generamos instrucciones para FLOAT
    } else {
      // Aca generamos instrucciones para LONGINT
      String reg;
      if (op1.isVble()) { // op1 es vble
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          reg = getRegLibre("ADD", op1, 1);
          asm.add("MOV " + reg + ", " + op1.getSimbolo());
          asm.add("ADD " + reg + ", " + op2.getSimbolo());
        } else { // Situacion 4.a: vble reg +
          String reg2 = getReg(op2.getReg());
          asm.add("ADD " + reg2 + ", " + op1.getSimbolo());
        }
      } else { // op1 es reg
        reg = getReg(op1.getReg());
        if (op2.isVble()) { // Situcacion 2: reg vble OP
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
    if (checkTipos(op1, op2)) {
      conversionImplicita(op2);
      //TODO: instrucciones FLOAT. 
    }
    String reg;
    if (op1.isVble()) { // op1 es vble 
      if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
        reg = getRegLibre("IMUL", op1, 1); // Devuelve si o si EAX.
        asm.add("MOV " + reg + ", " + op1.getSimbolo())
        asm.add("IMUL " + reg + ", " + op2.getSimbolo());
      } else { // Situacion 4.a: vble reg *
        String reg2;
        if (op2.getReg() != EAX) {
          if (itsBusy(EAX)) {
            swapRegLibre(EAX);
          }
          regs[EAX] = op2;
          op2.setReg();
        }
        reg2 = getReg(EAX); // en este punto op2 es si o si EAX
        asm.add("IMUL " + reg2 + ", " + op1.getSimbolo());
      }
    } else { // op1 es reg
      if (op1.getReg() != EAX) {
        if (itsBusy(EAX)) {
          swapRegLibre(EAX);
        }
        regs[EAX] = op1;
        op1.setReg();
      }
      reg = getReg(EAX);
      if (op2.isVble()) {  // Situcacion 2: reg vble OP
        asm.add("IMUL " + reg + ", " + op2.getSimbolo());
      } else { // Situacion 3: reg1 reg2 OP
        String reg2 = getReg(op2.getReg());
        asm.add("IMUL " + reg + ", " + reg2);
        freeReg(op2);
      }
    }
    // Apilar nuevo simbolo Polaca con el nombre EAX
  }

  public void declararProc(ArrayList<SimboloPolaca> polacaProc) {
    generateTag(procsAsm, polacaProc.get(0));
    for (int i = 1; i < polacaProc; i++) {
      // TODO: abstraer funcionalidad del main
    }
    procsAsm.add("ret");
  }

  public ArrayList<SimboloPolaca> findProc(String name) {
    for (int i = 1; i < polaca.size(); i++) {
      ArrayList<SimboloPolaca> proc = polaca.get(i);
      if (proc.get(0).equals(name)) {
        return new ArrayList<>(proc);
      }
    }
  }

  public ArrayList<Pair<SimboloPolaca, SimboloPolaca>> getParams(Integer i, ArrayList<SimboloPolaca> main, String nameProc) {
    ArrayList<Pair<SimboloPolaca, SimboloPolaca>> params = new ArrayList<>();
    while (!main.get(i).getSimbolo().equals(INV)) {
      i++; // la primera vez estoy sobre el nombre del proc y en cada vuelta sobre los ":"
      SimboloPolaca paramFormal = main.get(i) + nameMangling(nameProc);
      i++; // aumento para tomar el paramReal
      SimboloPolaca paramReal = main.get(i);
      i++; // aumento para ir a los ":"
      params.add(new Pair<SimboloPolaca, SimboloPolaca>(paramFormal, paramReal));
    }
    return params;
  }

  public void generateCodeInvocacion(ArrayList<String> asm, Integer i, ArrayList<SimboloPolaca> main, Set<String> procsDeclarados) {
    i++;
    simbolo = main.get(i);
    String nameProc = simbolo.getSimbolo();  // obtengo el nombre del proc
    if (!procsDeclarados.contains(nameProc)) { // si el proc no esta declarado
      ArrayList<SimboloPolaca> procActual = findProc(nameProc);
      procsDeclarados.add(nameProc);
      declararProc(procActual);
    }
    ArrayList<Pair<SimboloPolaca, SimboloPolaca>> params = getParams(i, main, nameProc); 
    for (Pair<SimboloPolaca, SimboloPolaca> pair : params) {
      SimboloPolaca paramFormal = pair.getValue0();
      SimboloPolaca paramReal = pair.getValue1();
      HashMap<String, Object> attrs = tablaSimbolos.get(paramFormal);
      boolean ref = attrs.get(TIPO).equals(REFERENCIA);
      generateAsignacion(asm, paramFormal, paramReal, ref);
    }
  }

  public void generateCode() {
    assembler.add(".code");
    assembler.add("START:");
    ArrayList<SimboloPolaca> main = polaca.get(0);
    Set<String> procsDeclarados = new HashSet<>();
    for (Integer i = 0; i < main.size(); i++) {
      SimboloPolaca simbolo = main.get(i);
      ArrayList<SimboloPolaca> invProcActual;
      if (simbolo.getSimbolo().equals(PROC)) {
        generateCodeInvocacion(assembler, i, main, procsDeclarados);
      } else {

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
    // DELETEME
    //devulve todos los parametros reales del procedimiento.
    String simboloActual;
    ArrayList<String> out = new ArrayList<>();
    for (int i = 0; i < invocacion.length; i++) {
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
    // DELETEME
    HashMap<String, Object> atributos = tablaSimbolos.get(invocacion.get(0));
    ArrayList<String> paramsFormales = atributos.get(PARAMETROS);
    ArrayList<String> paramsReales = getParametrosReales(invocacion);
    for (int i = 0; i < paramsFormales.size(); i++) {
      String paramFormal  = paramsFormales.get(i) + nameMangling(invocacion);
      if (checkTipos(paramFormal, paramsReales.get(i))) {
          conversionImplicita(paramsReales.get(i));
      }
    }
  }

  public void conversionImplicita(SimboloPolaca operando) {
    // TODO: conversion implicita
    // setSimbolo(variable auxiliar)
    // setReg(-1) // para decir que no es un registro
    String nameVarAux = "@aux" + countVarsAux;
    countVarsAux++;
    SimboloPolaca varAux = new SimboloPolaca(nameVarAux);
    HashMap<String, Object> attrs = new HashMap<>();
    attrs.push(TIPO, FLOAT);
    tablaSimbolos.push(varAux.getSimbolo(), attrs);
    operando.setSimbolo(varAux.getSimbolo());
    operando.setReg(-1); // es una vble
    if (!operando.isVble()) { // operando es un reg
      // TODO: mover el contenido del registro a la variable auxiliar
    }

    /*
      procemiento(a:b);
      procedimiento@main a b :

      :
      b   <---reemplazo--- @aux0 = b
      a   
    */

    z := a + b * c
    a, z FLOAT
    b, c LONGINT
    polaca: a b c * + z :=

    *     
    c     +   :=
    b     b   z   
    a     a   b
    

    op.setReg(EAX);

    if (!op.isVble()) {
      getReg(op.getReg()); // EAX
      op.getSimbolo()
    }
  }
}
