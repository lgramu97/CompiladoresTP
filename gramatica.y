%{
	package componentes;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.HashMap;
	import java.util.Scanner;
	import javax.swing.JFileChooser;
    import java.util.Stack;
    import org.javatuples.Pair;

	
%}

%token IF THEN END_IF OUT FUNC RETURN ELSE LONGINT FLOAT WHILE LOOP PROC NI ID REF DISTINTO IGUAL MAYOR_IGUAL MENOR_IGUAL CTE CADENA ERROR

%%

// reglas

programa : conjunto_sentencias 
        {
            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");
        } 
        ;

conjunto_sentencias : sentencias_declarativas 
                    | sentencias_ejecutables
                    | sentencias_declarativas conjunto_sentencias 
                    | sentencias_ejecutables conjunto_sentencias 
                    ;

condicion : expresion IGUAL expresion 
        {
            addSimbolo("==");
        }
        | expresion MAYOR_IGUAL expresion 
        {
            addSimbolo(">=");
        }
        | expresion MENOR_IGUAL expresion 
        {
            addSimbolo("<=");
        }
        | expresion DISTINTO expresion 
        {
            addSimbolo("!=");
        }
        | expresion '>' expresion 
        {
            addSimbolo(">");
        }
        | expresion '<' expresion 
        {
            addSimbolo("<");
        }
        | expresion error 
        {
            addErrorSintactico("Error en la condicion");
        }
        ;

condicion_accion: condicion 
                {
                    apilarPasoIncompleto(SimboloPolaca.BF);
                }
                    /* PASO 1 del IF y PASO 2 del WHILE
                        TERMINA LA EVALUACIÓN DE LA CONDICIÓN. (Generar BF)
                        1. Crear espacio en blanco
                        2. Apilar la dirección del paso incompleto.
                        3. Crear el paso del BF.
                    */
                ;

clausula_while : incio_while '(' condicion_accion ')' LOOP '{' bloque_sentencias_control '}'';'
                {
                    estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");
                }
                | incio_while '(' error ')' LOOP '{' bloque_sentencias_control '}'';'
                {
                    addErrorSintactico("Error en la condicion del WHILE.");
                }
                | incio_while '(' error LOOP '{' bloque_sentencias_control '}'';'
                {
                    addErrorSintactico("Error en la definicion del WHILE: falta el )");
                }
                | incio_while error condicion_accion ')' LOOP '{' bloque_sentencias_control '}'';' 
                {
                    addErrorSintactico("Error en la condicion del WHILE: falta el (.");
                }
                | incio_while '(' condicion_accion ')' error '{' bloque_sentencias_control'}' ';'
                {
                    addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");
                }
                | incio_while '(' '(' error condicion_accion  ')' LOOP '{' bloque_sentencias_control'}'';'
                {
                    addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");
                }
                | incio_while '(' condicion_accion ')' ')' error LOOP '{' bloque_sentencias_control'}'';'
                {
                    addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");
                }
                ;

incio_while : WHILE 
            {
                apilarPasoActual();
            }
            ;

clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' END_IF ';' 
                    {
                        estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");
                    }
                    | IF '(' condicion_accion ')''{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");
                    }
                    | IF '(' error ')' '{' bloque_then '}' END_IF ';'
                    {
                        addErrorSintactico("Error en la condicion del IF.");
                    }
                    | IF '('  error '{' bloque_then '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF: falta el )");
                    }
                    | IF  error condicion_accion ')' '{' bloque_then '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF: falta el (");
                    }
                    | IF '(' condicion_accion ')' '{' bloque_then '}' error  ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF: falta el END_IF");
                    }
                    | IF '(' '(' error condicion_accion ')' '{' bloque_then '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");
                    }
                    | IF '(' condicion_accion ')' ')' error '{' bloque_then '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");
                    }
                    | IF '(' error ')'  '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la condicion del IF ELSE.");
                    }
                    | IF '('  error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el )");
                    }
                    | IF error condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el (");
                    }
                    | IF '(' condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' error ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");
                    }
                    | IF '(' '(' error condicion_accion ')''{'  bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");
                    }
                    | IF '(' condicion_accion ')' ')' error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' 
                    {
                        addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");
                    }
                    ;

bloque_then: bloque_sentencias_control 
            {
                completarPasoIncompleto(false);
                apilarPasoIncompleto(SimboloPolaca.BI);
            }
                /* PASO 2
                    #_paso_incomp = desapilar_paso(); //Desapila dirección incompleta.
                    completar(#_paso_incomp , #_paso_actual + 2); //Completa el destino de BF.
                    #_paso_actual = create_paso(“ ”); //Crea paso incompleto.
                    apilar_paso(#_paso_actual); //Apila el número del paso incompleto.
                    #_paso_actual = generar_paso(“BI”); //Se crea el paso BI.
                */
            ;

bloque_else: bloque_sentencias_control {}
           ;

bloque_sentencias_control :  sentencias_ejecutables 
                          |  sentencias_ejecutables bloque_sentencias_control
                          ;

sentencias_declarativas : sentencia_declaracion_datos
                        | sentencia_declaracion_procedimiento
                        ;

sentencias_ejecutables : asignacion
                    | clausula_seleccion 
                    {
                        completarPasoIncompleto(true);
                    }
                    // PASO 3
                    // #_paso_incomp = desapilar_paso(); //Desapila dirección incompleta.
                    // completar_paso(#_paso_incomp, #_paso_actual + 1); //Completa el destino de
                    // BI.
                    | clausula_while 
                    {
                        completarPasoIncompleto(false);generarBIinicio();
                    }
                    | sentencia_salida
                    | invocacion_procedimiento 
                    {
                        addSimbolo("INV");
                    }
                    | error ';' 
                    {
                        addErrorSintactico("Syntax error");
                    }
                    ;

sentencia_salida : OUT '(' CADENA ')' ';' 
                {
                    estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");
                    addSimbolo($3.sval);
                    addSimbolo("OUT");
                }
                | OUT '(' CADENA ')' ')' ';' 
                {
                    addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");
                } 
                | OUT '(' '(' CADENA ')' ';' 
                {
                    addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");
                }  
                | OUT error ';' 
                {
                    addErrorSintactico("Error al imprimir por pantalla");
                }
                ;

lista_variables: ID 
                {
                    checkIDReDeclarado($1.sval);
                    modificarLexema($1.sval);
                }
                | ID ',' lista_variables 
                {
                    checkIDReDeclarado($1.sval);
                    modificarLexema($1.sval);
                }
                ;

tipo : LONGINT 
     {
         $$ = new ParserVal("LONGINT");
     }
     | FLOAT 
     {
         $$ = new ParserVal("FLOAT");
     }
     ;

sentencia_declaracion_datos : tipo lista_variables ';'
                            {
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");
                                addTipoListaVariables($1.sval,"VARIABLE");
                            }
                            ;

invocacion_procedimiento : inicio_inv_proc lista_parametros_invocacion ')' ';'
                        {
                            if (checkInvocacionProcedimiento($1.sval)){
                                addDireccionParametroReferencia($1.sval);
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");

                            } else {
                                erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Error en la invocacion del procedimiento." );
                            }
                        }
                        | inicio_inv_proc ')' ';'
                        {
                            if (checkInvocacionProcedimiento($1.sval)){
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");
                            } else {
                                erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Error en la invocacion del procedimiento." );
                            }
                        }
                        | inicio_inv_proc error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: falta )");
                        }
                        | inicio_inv_proc '(' error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");
                        }
                        | inicio_inv_proc  ')' ')' error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");
                        }
                        | inicio_inv_proc lista_parametros_invocacion error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: falta )");
                        }
                        | inicio_inv_proc error ')' ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");
                        }
                        | inicio_inv_proc '(' error lista_parametros_invocacion ')' ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");
                        }
                        | inicio_inv_proc lista_parametros_invocacion ')' ')' error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");
                        }
                        | ID error ')' ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: falta (");
                        }
                        | ID error lista_parametros_invocacion ')' ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento: falta (");
                        }
                        | ID error ';' 
                        {
                            addErrorSintactico("Error al invocar procedimiento.");
                        }
                        ;

inicio_inv_proc: ID '(' 
                {
                    checkIDNoDeclarado($1.sval);//VER CASO EN EL QUE LA INVOCACION SE HACE DENTRO DEL PROCEDIMIENTO.
                    addSimbolo("PROC");
                    addSimbolo($1.sval);
                    $$ =  new ParserVal($1.sval);
                }
                ;

sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'
                                    {
                                        deleteAmbito();
                                        if (checkTipoCte($7.sval)) {
                                            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");
                                            String val = ids.pop();
                                            addInvocacionesProcedimiento(val,$7.sval);
                                            addParametrosProcedimiento(val);
                                        }
                                        else
                                            erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " La constante declarada en el procedimiento no es de tipo LONGINT." );
                                    }
                                    | inicio_proc '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'
                                    {
                                        deleteAmbito();
                                        if (checkTipoCte($6.sval)) {
                                            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");
                                            addInvocacionesProcedimiento(ids.pop(),$6.sval);
                                        }
                                        else
                                            erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " La constante declarada en el procedimiento no es de tipo LONGINT." );
                                    }
                                    | inicio_proc '(' error NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta )");
                                    } 
                                    | inicio_proc error ')' NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta (");
                                    } 
                                    | inicio_proc '(' '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");
                                    } 
                                    | inicio_proc  '('  ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");
                                    }       
                                    | inicio_proc '(' ')' error '=' cte '{' conjunto_sentencias '}' ';'  
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta NI");
                                    } 
                                    | inicio_proc '(' ')' NI error cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta =");
                                    } 
                                    | inicio_proc '(' ')' NI '=' error '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta cte");
                                    }
                                    | inicio_proc '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");
                                    }
                                    | inicio_proc '(' lista_parametros_declaracion error NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta )");
                                    } 
                                    | inicio_proc error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta (");
                                    } 
                                    | inicio_proc '(' lista_parametros_declaracion ')' error '=' cte '{' conjunto_sentencias '}' ';'  
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta NI");
                                    } 
                                    | inicio_proc '(' lista_parametros_declaracion ')' NI error cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta =");
                                    } 
                                    | inicio_proc '(' lista_parametros_declaracion ')' NI '=' error '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: falta cte");
                                    }
                                    | inicio_proc '(' '(' error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");
                                    } 
                                    | inicio_proc  '(' lista_parametros_declaracion ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';' 
                                    {
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");
                                    }       
                                    ;

inicio_proc: PROC ID 
            {
            	checkIDReDeclarado($2.sval);//Check que exista el id.
                modificarLexema($2.sval);
                addTipoListaVariables("PROC","PROC");
                addAmbito($2.sval);
                ids.push($2.sval);
                // Agregar para el procedimiento, nombre de los parametros.
            }
            | PROC error '(' 
            {
                addErrorSintactico("Error al declarar procedimiento: falta ID");
            }
            ;

lista_parametros_invocacion : parametro_invocacion 
                            | parametro_invocacion ',' parametro_invocacion
                            | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion
                            | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion ',' error 
                            {
                                addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");
                            }  
                            ;

lista_parametros_declaracion : parametro_declaracion 
                            {
                                parametros.push(new ListParameters($1.sval));
                            }
                            | parametro_declaracion ',' parametro_declaracion
                            {
                                parametros.push(new ListParameters($1.sval, $3.sval));
                            }
                            | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion
                            {
                                parametros.push(new ListParameters($1.sval, $3.sval, $5.sval));
                            }
                            | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion ',' error 
                            {
                                addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");
                            }
                            ;

parametro_declaracion: tipo ID 
                    {
                        checkIDReDeclarado($2.sval);
                        modificarLexema($2.sval);
                        addTipoListaVariables($1.sval,"PARAMETRO");
                        addTipoPasajeParametros($2.sval,"COPIA-VALOR");
                        $$ = new ParserVal($2.sval); 
                    }
                    | REF tipo ID 
                    {
                        checkIDReDeclarado($3.sval);
                        modificarLexema($3.sval);
                        addTipoListaVariables($2.sval,"PARAMETRO");
                        addTipoPasajeParametros($3.sval,"REFERENCIA");
                        $$ = new ParserVal($3.sval); 
                    }
                    ;

parametro_invocacion: ID ':' ID 
                    {
                        checkIDNoDeclarado($1.sval);
                        /*
                        * CHEQUEAR QUE SE CORRESPONDA CON EL NOMBRE DEL PARAMETRO DECLARADO.
                        * Yo los agregaria todos los $1 a una lista, y cuando invoca al proc, chquear 
                        * con los parametros reales
                        * Si falta alguno, se repite alguno, o alguno no se corresponde, informar error.
                        * Para esto en la declaracion de proc, agregar atributo en la ts (parametros) con 
                        * una lista de los ids.
                        */
                        checkIDNoDeclarado($3.sval);
                        if (!parametrosInvocacion.contains($1.sval)) {
                            parametrosInvocacion.add($1.sval);
                            addPair($1.sval,$3.sval);
                        }
                        addSimbolo($1.sval);
                        addSimbolo($3.sval);
                        addSimbolo(":");
                    }
                    | ID ':' error 
                    {
                        addErrorSintactico("Error en la definicion de parametro del lado derecho");
                    }
                    | error ':' ID 
                    {
                        addErrorSintactico("Error en la definicion de parametros del lado izquierdo");
                    }
                    ;

asignacion : ID '=' expresion ';'
            {
                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");
                addSimbolo($1.sval);
                addSimbolo("=");
                checkIDNoDeclarado($1.sval);
            }
            | ID '=' error ';' 
            {
                addErrorSintactico("Error de asignación a la derecha.");
            }
            | error '=' expresion ';' 
            {
                addErrorSintactico("Error de asignación a la izquierda.");
            }
            ;

expresion : expresion '+' termino 
        {
            addSimbolo("+");
        }
        | expresion '-' termino 
        {
            addSimbolo("-");
        }
        | termino 
        ;

termino : termino '*' factor 
        {
            addSimbolo("*");
        }
        | termino '/' factor 
        {
            addSimbolo("/");
        }
        | factor 
        ;

factor : ID 
        {
            addSimbolo($1.sval);
            checkIDNoDeclarado($1.sval);
        }
        | cte 
        {
           addSimbolo($1.sval);
        }
        | ERROR
        ;

cte : CTE 
    {
        if (!analizadorLexico.check_rango_longint($1.sval)){
            addErrorSintactico("Error longint fuera de rango");}
        $$ = $1;
    } 
    | '-' CTE 
    {
        analizadorLexico.updateTablaSimbolos($2.sval);
        $$ = new ParserVal("-"+$2.sval);
    }
    ;
%%

ArrayList<SimboloPolaca> listaReglas = new ArrayList<>();
Stack<Integer> pasosIncompletos = new Stack<>();
AnalizadorLexico analizadorLexico = new AnalizadorLexico();
ArrayList<String> erroresSintacticos = new ArrayList<>();
ArrayList<String> erroresSemanticos = new ArrayList<>();
ArrayList<String> erroresParser = new ArrayList<>();
ArrayList<String> tokens = new ArrayList<>();
ArrayList<String> estructuras = new ArrayList<>();
ArrayList<String> lista_variables = new ArrayList<>();
ArrayList<String> ambito = new ArrayList<String>() { { add("@main"); } };
Stack<String> ids = new Stack<>();
Stack<ListParameters> parametros = new Stack<ListParameters>();
ArrayList<String> parametrosInvocacion = new ArrayList<>();
Stack<Pair<String,String>> parametrosInvocacionPar = new Stack<>();

public void addPair(String paramForaml, String paramReal){
    parametrosInvocacionPar.push(new Pair<String,String>(paramForaml,paramReal));
}

public void addDireccionParametroReferencia(String idProc) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    while (!parametrosInvocacionPar.empty()) {
        Pair<String, String> pair = parametrosInvocacionPar.pop();
        String paramFormal = pair.getValue0();
        String paramReal = pair.getValue1();
        String lex_mangling = nameMangling(paramFormal) + "@" + idProc;
        if (ts.containsKey(lex_mangling) && ts.get(lex_mangling).get("Pasaje").equals("REFERENCIA")) {
            ArrayList<String> ambitoCopia = new ArrayList<>(ambito);
            String direccion = null;
            for(int i = ambitoCopia.size(); i > 0; i--) {
                String newVar = paramReal + listToString(ambitoCopia);
                if (ts.containsKey(newVar)) {
                    direccion = &ts.get(newVar);
                    break;
                }
                ambitoCopia.remove(ambitoCopia.size()-1);
            }
            if (direccion != null) {
                ts.get(lex_mangling).put("DIR " + paramReal, direccion);
            }
            HashMap<String,Object> atributos = *direccion; 
            System.out.println("IMPRIMO LOS ATRIBUTOS : " + atributos);
        }
    }
}

public boolean checkInvocacionProcedimiento(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    boolean seCumple = parametrosInvocacion.size() == ((ListParameters) ts.get(lex_mangling).get("Parametros")).getCantidad();
    parametrosInvocacion.clear();
    return seCumple;
}

public void addParametrosProcedimiento(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    ts.get(lex_mangling).put("Parametros", parametros.pop());
}

public void addInvocacionesProcedimiento(String lexema, String invocaciones){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    ts.get(lex_mangling).put("Invocaciones",Integer.valueOf(invocaciones));
    ts.get(lex_mangling).put("Llamadas",0);
}

public void addTipoPasajeParametros(String lexema, String pasaje){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    ts.get(nameMangling(lexema)).put("Pasaje",pasaje);
}

public boolean checkTipoCte(String cte){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    return ts.get(cte).get("Tipo").equals("LONGINT");
}

public void checkIDNoDeclarado(String variable) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    ArrayList<String> ambitoCopia = new ArrayList<>(ambito);
    for(int i = ambitoCopia.size(); i > 0; i--) {
        String newVar = variable + listToString(ambitoCopia);
        if (ts.containsKey(newVar)) {
            break;
        }
        ambitoCopia.remove(ambitoCopia.size()-1);
    }
    if (ambitoCopia.size()==0) {
        erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Variable '" + variable + "' no declarada");
    }
    if (ts.containsKey(variable)) {
        ts.remove(variable);
    }
}

public void checkIDReDeclarado(String variable) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    if (ts.containsKey(nameMangling(variable))) {
        erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Variable '" + variable + "' re-declarada");
    }
}

public void addAmbito(String ambito_actual){
    ambito.add("@" + ambito_actual);
}

public void deleteAmbito(){
    ambito.remove(ambito.size()-1);
}

public String listToString(ArrayList<String> list) {
    return list.toString()
        .replaceAll("\\[|]|, ", "");
}

public String nameMangling(String simbolo) {
    return simbolo + listToString(ambito);
}

public void modificarLexema(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    HashMap<String,Object> atributos = ts.get(lexema);
    ts.remove(lexema);
    String newLexema = nameMangling(lexema);
    ts.put(newLexema ,atributos);
    lista_variables.add(newLexema);
}

public void addTipoListaVariables(String tipo, String uso){
  HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    for (String lista_variable : lista_variables) {
        HashMap<String, Object> atributos =  ts.get(lista_variable);
        atributos.put("Tipo", tipo);
        atributos.put("Uso", uso);
    }
  lista_variables.clear();
}

public void addSimbolo(String simbolo) {
	listaReglas.add(new SimboloPolaca(simbolo));
}

public void apilarPasoIncompleto(String nombre) {
	apilarPasoActual();
	addSimbolo(null);
	addSimbolo(nombre);
}

public void completarPasoIncompleto(boolean fin) {
	int posIncompleto = pasosIncompletos.pop();
	SimboloPolaca simbolo = listaReglas.get(posIncompleto);
    int pos = listaReglas.size();
    if (!fin) {
        pos += 2;
    }
	simbolo.setSimbolo(pos+"");
}

public void generarBIinicio(){
	int posInicial = pasosIncompletos.pop();
	addSimbolo(posInicial+"");
	addSimbolo(SimboloPolaca.BI);
}

public void apilarPasoActual() {
	pasosIncompletos.push(listaReglas.size());
}


public int yylex(){
		int token = analizadorLexico.yylex();
        //Si el token es un ID, CTE, CADENA necesito el valor del lexema.
		if(token == Parser.ID || token == Parser.CTE || token  == Parser.CADENA)
            yylval = analizadorLexico.yylval();
		tokens.add("Linea numero: "+ (analizadorLexico.getFilaActual()+1) +" token " + token+" --" + analizadorLexico.tokenToString(token));
		return token;
}

public AnalizadorLexico getAnalizadorLexico() {
	return this.analizadorLexico;
}

public ArrayList<String> getTokens(){
	return this.tokens;
}

public ArrayList<String> getEstructuras(){
	return this.estructuras;
}

public ArrayList<SimboloPolaca> getListaSimboloPolaca(){
    return this.listaReglas;
}

public void addErrorSintactico(String error){
    erroresSintacticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + "  " + error);
}


public void yyerror(String error){
	erroresParser.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + "  " + error);
}


public StringBuilder copiarErrores(ArrayList<String> errores){
    StringBuilder out = new StringBuilder();
    for (String errore : errores) {
        out.append("\t").append(errore);
        out.append("\n");
    }
    return out;
}

public String getErrores(){
    return "Errores Lexicos: " + 
            "\n" +
            copiarErrores(analizadorLexico.getErrores()) +
            "\n" +
            "Errores Sintacticos: " +
            "\n" +
            copiarErrores(this.erroresSintacticos) + 
            "\n" + 
            "Errores Semanticos: " + 
            "\n" + 
            copiarErrores(this.erroresSemanticos);
}

public void saveFile() {
	 JFileChooser jchooser=new JFileChooser();
	 File workingDirectory = new File(System.getProperty("user.dir"));
	 jchooser.setCurrentDirectory(workingDirectory);
	 jchooser.setDialogTitle("Guardar archivo de salida.");
	 jchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	 if (jchooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		File out = jchooser.getSelectedFile();

	    try {
			FileWriter salida = new FileWriter(out+"/out.txt");

			salida.write(this.getErrores() + "\n");

			/*
			salida.write("Tokens detectados en el codigo fuente: " + "\n");
            for (String token : tokens) {
                salida.write("\t" + token + "\n");
            }

			salida.write("\n" + "Estructuras detectadas en el codigo fuente: " + "\n");
            for (String estructura : estructuras) {
                salida.write("\t" + estructura + "\n");
            }
			*/
			salida.write("\n"+"Contenido de la tabla de simbolos: " + "\n");
			salida.write(this.getAnalizadorLexico().getDatosTabla_simbolos());

			salida.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
}

public void mostrar_tokens(){
	System.out.println();
	System.out.println("Tokens detectados en el codigo fuente: ");
    for (String token : this.tokens) {
        System.out.println(token);
    }
}

public void mostrar_estructuras(){
	System.out.println();
	System.out.println("Estructuras detectadas en el codigo fuente: ");
    for (String estructura : this.estructuras) {
        System.out.println(estructura);
    }
}
 
public static void main(String[] args){
	Parser parser = new Parser();
	parser.yyparse();
	System.out.println(parser.getErrores());

	//parser.mostrar_tokens();
	//parser.mostrar_estructuras();

	System.out.println();
	System.out.println("Contenido de la tabla de simbolos: ");
	System.out.println(parser.getAnalizadorLexico().getDatosTabla_simbolos());
	
	System.out.println();
	Scanner in = new Scanner(System.in);
	System.out.println("Desea guardar la salida en un documento de texto? Y/N");
	String rta = in.nextLine();
	if (rta.equals("Y") || rta.equals("y"))
		parser.saveFile();
	in.close();


    ArrayList<SimboloPolaca> lista = parser.getListaSimboloPolaca();
    System.out.println("Tamaño de la lista de simbolos: " + lista.size());
    for (int i = 0, listaSize = lista.size(); i < listaSize; i++) {
        SimboloPolaca simboloPolaca = lista.get(i);
        System.out.println("VALOR POLACA [" + i + "]: " + simboloPolaca.getSimbolo());
    }
    System.out.println("VALOR POLACA [" + lista.size() +  "]: ...");

}