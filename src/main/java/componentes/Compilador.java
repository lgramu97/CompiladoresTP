package componentes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

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
import static componentes.AnalizadorLexico.OUT;
import static componentes.SimboloPolaca.BF;
import static componentes.SimboloPolaca.BI;

public class Compilador {

  private String condBF;
  private final ArrayList<ArrayList<SimboloPolaca>> polaca;
  private final ArrayList<String> assembler = new ArrayList<>();
  private final ArrayList<String> assemblerData = new ArrayList<>();
  private final ArrayList<String> assemblerHeader = new ArrayList<>();
  private final ArrayList<String> procsAsm = new ArrayList<>();
  private final HashMap<String, HashMap<String, Object>> tablaSimbolos;
  private final HashMap<String, String> cadenaVar = new HashMap<>();
  private final HashMap<String, String> numbers = new HashMap<>();
  private final Parser parser;
  private final SimboloPolaca[] regs = {null, null, null, null};
  private final int EAX = 0;
  private final int EBX = 1;
  private final int ECX = 2;
  private final int EDX = 3;
  private int countVarsAux = 0;
  public static final String AUX = "@aux";
  public static final String ERROR_DIVISION_CERO = "ERROR_DIVISION_CERO";
  public static final String ERROR_OVERFLOW_SUMA = "ERROR_OVERFLOW_SUMA";
  public static final String ETIQUETA_OVERFLOW = "__etiquetaErrorOverflow__";
  public static final String FLOAT_CERO = "FLOAT_CERO";
  public static final String INV = "INV";
  public static final String MAXIMO_POSITIVO = "3.40282347e38";
  public static final String MAXIMO_NEGATIVO = "-" + MAXIMO_POSITIVO;
  public static final String MINIMO_POSITIVO = "1.17549435e-38";
  public static final String MINIMO_NEGATIVO = "-" + MINIMO_POSITIVO;


  public Compilador(Parser parser) {
    this.tablaSimbolos = parser.getAnalizadorLexico().getTabla_simbolos();
    this.polaca = parser.getListaSimboloPolaca();
    this.parser = parser;
  }

  public ArrayList<String> getAssembler(){
    generateHeader();
    generateCode();
    generateData();
    ArrayList<String> asm =  new ArrayList<>();
    asm.addAll(assemblerHeader);
    asm.addAll(assemblerData);
    asm.addAll(assembler);
    return asm;
  }

  private void generateHeader() {
    assemblerHeader.add(".386");
    assemblerHeader.add(".STACK 200h");
    assemblerHeader.add(".model flat, stdcall");
    assemblerHeader.add("option casemap :none");
    assemblerHeader.add("include \\masm32\\include\\windows.inc");
    assemblerHeader.add("include \\masm32\\include\\kernel32.inc");
    assemblerHeader.add("include \\masm32\\include\\user32.inc");
    assemblerHeader.add("includelib \\masm32\\lib\\kernel32.lib");
    assemblerHeader.add("includelib \\masm32\\lib\\user32.lib");
  }

  private void generateData() {
    assemblerData.add(".data");
    assemblerData.add(ERROR_DIVISION_CERO + " db " + "Error: no es posible divir por cero.");
    assemblerData.add(ERROR_OVERFLOW_SUMA + " db " + "Error: overflow en suma.");
    // variables overflow 
    assemblerData.add(FLOAT_CERO + " DQ 0.0");
    assemblerData.add(MINIMO_POSITIVO + " DQ " + MINIMO_POSITIVO);
    assemblerData.add(MINIMO_NEGATIVO + " DQ " + MINIMO_NEGATIVO);
    assemblerData.add(MAXIMO_POSITIVO + " DQ " + MAXIMO_POSITIVO);
    assemblerData.add(MINIMO_NEGATIVO + " DQ " + MINIMO_NEGATIVO);
    assemblerData.add(AUX + " DQ ?");
    for (String key : tablaSimbolos.keySet()) {
      addData(cadenaVar, key);
    }
  }
  
  public void checkOverflowADD(String extremo, String salto, String etiqueta) {
    procsAsm.add("FINIT"); // Resetea control y estado en cada comparacion.
    procsAsm.add("FLD " + AUX); // Cargo a ST(0) el valor de AUX.
    procsAsm.add("FCOM " + extremo); // Comparo con el extremo
    procsAsm.add("FSTSW AX");// palabra de estado a mem.
    procsAsm.add("SAHF"); // copia indicadores.
    procsAsm.add(salto + " " + etiqueta); //Salto si condicion
  }

  private void addDeclaracionOverflowADD() {
    /* Generacion de llamadas para chequear overflow en float (suma)*/
    procsAsm.add("FLOAT_VALIDO:");
    procsAsm.add("RET");

    procsAsm.add("OVERFLOW_FLOAT:");
    checkOverflowADD(MAXIMO_POSITIVO, "JA", ETIQUETA_OVERFLOW); //Comparo max positivo / salto si es mayor.
    checkOverflowADD(MINIMO_POSITIVO, "JA", "FLOAT_VALIDO");//Comparo min positivo / salto si es mayor.
    // Retorno al CALL porque estoy entre un valor valido positivo.
    checkOverflowADD(MAXIMO_NEGATIVO, "JB", ETIQUETA_OVERFLOW);// Comparo mas neg / salto si es menor. 
    checkOverflowADD(MINIMO_NEGATIVO, "JB", "FLOAT_VALIDO");// Comparo min neg / salto si es menor.  
    // Retorno al CALL porque estoy entre un valor valido negativo.
    checkOverflowADD(FLOAT_CERO, "JE", "FLOAT_VALIDO");// Comparo cero / salto si es igual.
    // Retorno al CALL porque es cero.
    procsAsm.add("JMP " + ETIQUETA_OVERFLOW);// FLOAT INVALIDO.    
  }

  private void addData(HashMap<String, String> cadenaVar, String key) {
    HashMap<String, Object> atributos = tablaSimbolos.get(key);
    if (atributos.containsKey(USO)) {
      addVariable(key, atributos);
    } else if(atributos.get(TIPO).equals(CADENA)) {
      addCadena(cadenaVar, key);
    } else {
      addConstante(key, atributos);
    }
    //Ver el caso de constante en codigo. No contiene USO, si se agrega -> addVariable.
  }

  // Lexema: 3
	// Atributo: Tipo   Valor: LONGINT
  private void addConstante(String key, HashMap<String, Object> atributos) {
    String tipo = (String) atributos.get(TIPO);
    if (tipo.equals(LONGINT) || tipo.equals(FLOAT)) { // Caso FLOAT y LONGINT.
      String number = "_Constante" + numbers.size();
      assemblerData.add(number + " dd " + key); // inicializar con el valor de la constante.
      numbers.put(key,number);
      // El signo ? indica que no esta inicializado
    }
  }

  private void addVariable(String key, HashMap<String, Object> atributos) {
    String uso = (String) atributos.get(USO);
    if (uso.equals(VARIABLE) || uso.equals(PARAMETRO)) {
      String tipo = (String) atributos.get(TIPO);
      if (tipo.equals(LONGINT) || tipo.equals(FLOAT)) { // Caso FLOAT y LONGINT.
        assemblerData.add("_" + key + " dd ?");
        // El signo ? indica que no esta inicializado
      }
    }
  }

  private void addCadena(HashMap<String, String> cadenaVar, String key) {
    String var = "_Cadena" + cadenaVar.size();
    assemblerData.add(var + " db " + key + ", 0");
    cadenaVar.put(key, var);
  }

  // ProgramText db "Hello World!", 0
  // _bad: invoke StdOut, addr ProgramText

  public String getTipo(SimboloPolaca op) {
    if (!op.isVble()) return LONGINT;
    HashMap<String, Object> attrs = tablaSimbolos.get(op.getSimbolo());
    return (String) attrs.get(TIPO);
  }

  public boolean isFloat(SimboloPolaca op) {
    return getTipo(op).equals(FLOAT);
  }

  public boolean checkTipos(SimboloPolaca op1, SimboloPolaca op2) {
    boolean op1IsFloat = isFloat(op1);
    if (op1IsFloat == isFloat(op2)) return false;
    // Conversiones implicita: sobre parametro real.
    if (op1IsFloat) return true;
    // error
    parser.addErrorSemantico("Conversion implicita no valida.");
    throw new Error("Conversion implicita no valida!");
  }

  public String getReg(int reg) {
    switch (reg) {
      case EAX:
        return "EAX";
      case EBX:
        return "EBX";
      case ECX:
        return "ECX";
      case EDX:
        return "EDX";
    }
    return null;
  }

  public boolean itsBusy(int reg) {
    return regs[reg] != null;
  }

  public void occupyReg(SimboloPolaca op, int reg) {
    regs[reg] = op;
    op.setReg(reg);
  }

  public void vacateReg(int reg) {
    if (itsBusy(reg)) {
      swapRegLibre(reg);
    }
  }

  public String getRegLibreGral(SimboloPolaca operando) {
    for (int reg = 0; reg < regs.length; reg++) {
      if (!itsBusy(reg)) {
        occupyReg(operando, reg);
        return getReg(reg);
      }
    }
    return null;
  }

  public String getRegLibreEAX(SimboloPolaca operando) {
    vacateReg(EAX);
    occupyReg(operando, EAX);
    return getReg(EAX);
  }

  public String getRegLibreDIV(SimboloPolaca operando, int numOp) {
    vacateReg(EDX);
    if (numOp == 1) return getRegLibreEAX(operando);
    int reg = itsBusy(EBX) ? ECX : EBX;
    occupyReg(operando, reg);
    return getReg(reg); // "EBX" || "ECX"
  }

  public void swapRegLibre(int reg) {
    if (regs[reg] == null) return;
    for (int i = 1; i < regs.length; i++) {
      if (regs[i] == null) {
        regs[i] = regs[reg];
        regs[i].setReg(i);
        regs[reg] = null;
      }
    }
  }

  public void freeReg(SimboloPolaca operando) {
    int reg = operando.getReg();
    if (reg == -1) return;
    operando.freeReg();
    regs[reg] = null;
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
    String reg = getRegLibreGral(op1);
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
    if (checkTipos(op1, op2)) conversionImplicita(asm, op2);
    String reg;
    // op1 siempre es vble
    if (op2.isVble()) { // op2 es vble
      reg = getRegLibreGral(op2);
      if (ref) {
        op2.setSimbolo("[" + op2.getSimbolo() + "]");
      }
      asm.add("MOV " + reg + ", " + op2.getSimbolo());
    } else { // op2 es reg
      reg = getReg(op2.getReg());
    }
    asm.add("MOV " + op1.getSimbolo() + ", " + reg);
    freeReg(op2);
  }

  public void generateCodeSUB(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    //SUB {__reg__, __mem__}, {__reg__, __mem__, __inmed__} ; Operación: dest <- dest - src.
    if (isFloat(op1) && isFloat(op2)) {
      asm.add("FLD " + op1.getSimbolo()); // Cargo op1 ST(2)
      asm.add("SUB " + op2.getSimbolo());
      asm.add("FSTP " + crearVarAux()); // Copia el valor de la pila a la var auxiliar
    } else {
      String reg;
      if (op1.isVble()) { // op1 es vble
        reg = getRegLibreGral(op1);
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
    if (isFloat(op1) && isFloat(op2)) {
      asm.add("FLD" + op1.getSimbolo()); // Cargo op1 ST(2)
      asm.add("FLD" + op2.getSimbolo()); // Cargo op2 ST(1)
      asm.add("FLD" + FLOAT_CERO); // Cargo cero ST(0)
      asm.add("FCOMP"); // Comparo ST(0) con ST(1) y saco ST(0)
      asm.add("FSTSW AX"); // palabra de estado a mem.
      asm.add("SAHF"); // Almacena indicadores.
      asm.add("JE ERROR_DIVISION_CERO");
      asm.add("FDIV");// division
      asm.add("FSTP " + crearVarAux()); // Copia el valor de la pila a la var auxiliar
    } else {
      String reg, reg2;
      if (op1.isVble()) { // op1 es vble
        reg = getRegLibreDIV(op1, 1);// Asigno EAX.
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          reg2 = getRegLibreDIV(op2, 2);
          asm.add("MOV " + reg2 + ", " + op2.getSimbolo());
        } else { // Situacion 4.b: vble reg
          reg2 = getReg(op2.getReg()); // Nunca va a ser EAX ni EDX porque ya lo movio getRegLibre
        }
        asm.add("MOV " + reg + ", " + op1.getSimbolo());
        freeReg(op2);
      } else { // op1 es reg
        if (op1.getReg() != EAX) {
          vacateReg(EAX);
          occupyReg(op1, EAX);
        }
        vacateReg(EDX);
        if (op2.isVble()) { // Situcacion 2: reg vble OP
          reg2 = getRegLibreDIV(op2, 2);
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
    }
  }
  
  public void generateCodeADD(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    // ADD {__reg__, __mem__}, {__reg__, __mem__, __inmed__} ; Operación: dest <- dest + src
    if (isFloat(op1) && isFloat(op2)) {
      asm.add("FLD " + op1.getSimbolo()); // Cargo op1 ST(1)
      asm.add("FADD " + op2.getSimbolo());
      asm.add("FST " + AUX); // Cargo en aux el valor de la pila.
      asm.add("CALL OVERFLOW_FLOAT");
      asm.add("FSTP " + crearVarAux());  
    } else {
      // Aca generamos instrucciones para LONGINT
      String reg;
      if (op1.isVble()) { // op1 es vble
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          reg = getRegLibreGral(op1);
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
      asm.add("JO "+ ERROR_OVERFLOW_SUMA);
    }
  }

  public void generateCodeOPConmutativas() {
    // TODO:
  }

  public void generateCodeMUL(ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    // IMUL EAX, {__reg__, __mem__} ; EDX: EAX <- EAX * {reg32|mem32|inmed}
    if (isFloat(op1) && isFloat(op2)) {
      asm.add("FLD " + op1.getSimbolo()); // Cargo op1 ST(1)
      asm.add("FLD " + op2.getSimbolo()); // Cargo op2 ST(0)
      asm.add("FMUL "); // Multiplico ST(1) * ST(0) y resultado en ST(0)
      // Tambien podria ser FMUL op2.getSimbolo();
      asm.add("FSTP " + crearVarAux());  
    } else {
      String reg;
      if (op1.isVble()) { // op1 es vble 
        if (op2.isVble()) {// Situacion 1: vble1 vble2 OP
          reg = getRegLibreEAX(op1);
          asm.add("MOV " + reg + ", " + op1.getSimbolo());
          asm.add("IMUL " + reg + ", " + op2.getSimbolo());
        } else { // Situacion 4.a: vble reg *
          String reg2;
          if (op2.getReg() != EAX) {
            if (itsBusy(EAX)) {
              swapRegLibre(EAX);
            }
            regs[EAX] = op2;
            op2.setReg(EAX);
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
          op1.setReg(EAX);
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
    }
  }

  public void declararProc(ArrayList<SimboloPolaca> polacaProc, Set<String> procsDeclarados) {
    generateTag(procsAsm, polacaProc.get(0));
    generarInstruccionesAsm(procsAsm, polacaProc, procsDeclarados);
    procsAsm.add("RET");
  }

  public ArrayList<SimboloPolaca> findProc(String name) {
    for (int i = 1; i < polaca.size(); i++) {
      ArrayList<SimboloPolaca> proc = polaca.get(i);
      if (proc.get(0).getSimbolo().equals(name)) return new ArrayList<>(proc);
    }
    return null;
  }

  public ArrayList<Pair<SimboloPolaca, SimboloPolaca>> getParams(ArrayList<SimboloPolaca> invocacion, String nameProc) {
    ArrayList<Pair<SimboloPolaca, SimboloPolaca>> params = new ArrayList<>();
    int i = 1; // primer parametro o INV
    SimboloPolaca simbolo = invocacion.get(i);
    while (!simbolo.getSimbolo().equals(INV)) {
      simbolo = invocacion.get(i++); // paramFormal
      SimboloPolaca paramFormal = new SimboloPolaca(simbolo.getSimbolo() + nameMangling(nameProc));
      SimboloPolaca paramReal = invocacion.get(i++); // paramReal
      params.add(new Pair<>(paramFormal, paramReal));
      simbolo = invocacion.get(++i); // segundo paramFormal o INV
    }
    return params;
  }

  public void generateCodeInvocacion(ArrayList<String> asm, ArrayList<SimboloPolaca> invocacion, Set<String> procsDeclarados) {
    SimboloPolaca simbolo = invocacion.get(0);
    String nameProc = simbolo.getSimbolo();  // obtengo el nombre del proc
    if (!procsDeclarados.contains(nameProc)) { // si el proc no esta declarado
      ArrayList<SimboloPolaca> procActual = findProc(nameProc);
      procsDeclarados.add(nameProc);
      declararProc(procActual, procsDeclarados);
    }
//    ArrayList<Pair<SimboloPolaca, SimboloPolaca>> params = getParams(i, main, nameProc);
    ArrayList<Pair<SimboloPolaca, SimboloPolaca>> params = getParams(invocacion, nameProc);
    for (Pair<SimboloPolaca, SimboloPolaca> pair : params) {
      SimboloPolaca paramFormal = pair.getValue0();
      SimboloPolaca paramReal = pair.getValue1();
      HashMap<String, Object> attrs = tablaSimbolos.get(paramFormal.getSimbolo());
      boolean ref = attrs.get(TIPO).equals(REFERENCIA);
      generateAsignacion(asm, paramFormal, paramReal, ref);
    }
    asm.add("CALL " + nameProc);
  }

  public void generateCodeOperacion(Stack<SimboloPolaca> pilaEjecucion, char operacion, ArrayList<String> asm, SimboloPolaca op1, SimboloPolaca op2) {
    if (checkTipos(op1, op2)) conversionImplicita(asm, op2);
    switch (operacion) {
      case '+':
        generateCodeADD(asm, op1, op2);
        break;
      case '-':
        generateCodeSUB(asm, op1, op2);
        break;
      case '/':
        generateCodeDIV(asm, op1, op2);
        break;
      case '*':
        generateCodeMUL(asm, op1, op2);
        break;
    }
    if (!op1.isVble()) {
      pilaEjecucion.push(op1);
    } else {
      pilaEjecucion.push(op2);
    }
  }

  public void generarInstruccionesAsm(ArrayList<String> asm, ArrayList<SimboloPolaca> polaca, Set<String> procsDeclarados) {
    Stack<SimboloPolaca> pilaEjecucion = new Stack<>();
    for (int i = 0; i < polaca.size(); i++) {
      SimboloPolaca simbolo = polaca.get(i);
      SimboloPolaca op1, op2, op;
      switch (simbolo.getSimbolo()) {
        case PROC:
          ArrayList<SimboloPolaca> invocacion = new ArrayList<>();
          while (!polaca.get(++i).getSimbolo().equals(INV)) {
            invocacion.add(polaca.get(i));
          }
          invocacion.add(polaca.get(i++)); // agrego INV
          generateCodeInvocacion(asm, invocacion, procsDeclarados);
          break;
        case "-":
        case "+":
        case "/":
        case "*":
          op2 = pilaEjecucion.pop();
          op1 = pilaEjecucion.pop();
          generateCodeOperacion(pilaEjecucion, simbolo.getSimbolo().charAt(0), asm, op1, op2);
          break;
        case ":=":
          op2 = pilaEjecucion.pop();
          op1 = pilaEjecucion.pop();
          generateAsignacion(asm, op1, op2, false);
          break;
        case OUT:
          op = pilaEjecucion.pop();
          generateCodeOUT(asm, op);
          break;
        case BF:
          op = pilaEjecucion.pop();
          generateSalto(asm, op);
          break;
        case BI:
          condBF = "JMP";
          op = pilaEjecucion.pop();
          generateSalto(asm, op);
          break;
        case ">":
        case "<":
        case "==":
        case ">=":
        case "<=":
        case "!=":
          op2 = pilaEjecucion.pop();
          op1 = pilaEjecucion.pop();
          generateComparacion(asm, op1, op2, simbolo.getSimbolo());
          break;
        default:
          if (simbolo.getSimbolo().charAt(0) == 'L') break;
          // Apilo la constante -> obtengo variable del hash.
          if (!isId(simbolo.getSimbolo()) && !isAddress(polaca, i+1)){
            // FIXME: Chequear que no entre nunca por el caso de ':', creo que lo sacamos ya.
            // Por el default entra con tags (L), con ids, y con constantes.
            // En el caso de las constantes puede ser una constante en codigo que debo buscar el valor
            // o puede ser la direccion de salto previo al BF/BI que lo agrego como tal en la pila
            // porque en la siguiente instruccion de la polaca genero el tag.
            String variable = numbers.get(simbolo.getSimbolo());
            simbolo.setSimbolo(variable);
          }
          pilaEjecucion.push(simbolo);
      }
    }
  }
/*
  VALOR POLACA [58]: 0.
  VALOR POLACA [59]: b@main@producto (el siguiente es variable, buscar en hash la cte)

  VALOR POLACA [47]: 6
  VALOR POLACA [48]: BI/BF (el siguiente es BI o BF, el anterior se agrega sin usar el hash porque es etiqueta luega)
*/
  public boolean isAddress(ArrayList<SimboloPolaca> polaca, int indexOp){
    // True si es una constante en codigo. False si es direccion de salto (previo a BF/BI)
    if (indexOp < polaca.size()){
      String nextOp = polaca.get(indexOp).getSimbolo();
      return  nextOp.equals(BF) || nextOp.equals(BI);
    }
    return false;
  }

  public boolean isId(String op){
    char c = op.toLowerCase().charAt(0);
    return c >= 'a' && c <= 'z' ;
  }

  public void generateCode() {
    assembler.add(".code");
    assembler.add("START:");
    ArrayList<SimboloPolaca> main = polaca.get(0);
    Set<String> procsDeclarados = new HashSet<>();
    addDeclaracionOverflowADD();
    generarInstruccionesAsm(assembler, main, procsDeclarados);
    assembler.add("invoke ExitProcess, 0"); 
    generateCodeError();
    assembler.add("invoke ExitProcess, 0");
    assembler.add("END START");
  }
  
  public void generateCodeError() {
    assembler.add(ETIQUETA_OVERFLOW + ":");
    assembler.add("invoke MessageBox, NULL, addr " + ERROR_OVERFLOW_SUMA + ", addr  " + ERROR_OVERFLOW_SUMA + ", MB_OK");
    assembler.add("invoke ExitProcess, 0"); 
    assembler.add("__etiquetaErrorDivCero__:");
    assembler.add("invoke MessageBox, NULL, addr " + ERROR_DIVISION_CERO +  ", addr " + ERROR_DIVISION_CERO +  ", MB_OK");
    assembler.add("invoke ExitProcess, 0"); 

  }

/*  public ArrayList<String> getParametrosReales(ArrayList<SimboloPolaca> invocacion) {
    // DELETEME
    //devulve todos los parametros reales del procedimiento.
    String simboloActual;
    ArrayList<String> out = new ArrayList<>();
    for (int i = 0; i < invocacion.size(); i++) {
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
  }*/

  public String nameMangling(String proc) {
    // Busca desde el ambito actual hacia atra s.
    return proc.substring(proc.indexOf('@'));
  }

/*  public boolean checkParams(ArrayList<SimboloPolaca> declaracion, ArrayList<SimboloPolaca> invocacion) {
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
  }*/

  public String crearVarAux() {
    String nameVarAux = "@aux" + countVarsAux++;
    tablaSimbolos.put(nameVarAux, new HashMap<String, Object>(){{ put(TIPO, FLOAT); }});
    return nameVarAux;
  }

  public void conversionImplicita(ArrayList<String> asm, SimboloPolaca operando) {
    String varAux = crearVarAux();
    operando.setReg(-1); // es una vble
    String reg;
    if (!operando.isVble()) { // operando es un reg
      reg = getReg(operando.getReg());
    } else {
      reg = getRegLibreGral(operando);
      asm.add("MOV " + reg + ", " + operando.getSimbolo());
    }
    asm.add("MOV " + varAux + ", " + reg);
    asm.add("FILD " + varAux);
    asm.add("FSTP " + varAux);
    operando.setSimbolo(varAux);

    /*
      procemiento(a:b);
      procedimiento@main a b :

      :
      b   <---reemplazo--- @aux0 = b
      a   
    */

/*     z := a + b * c
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
    } */
  }

  // "hjola", _Cadena1
  public void generateCodeOUT(ArrayList<String> asm, SimboloPolaca op) {
    String titulo = cadenaVar.get(op.getSimbolo());
    // titulo, contenido
    asm.add("invoke MessageBox, NULL, addr " + titulo + ", addr  " + titulo + ", MB_OK");
  }
}
