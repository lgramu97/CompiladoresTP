%{
	package componentes;
    import java.util.ArrayList;

	
%}

%token IF THEN END_IF OUT FUNC RETURN ELSE LONGINT FLOAT WHILE LOOP PROC NI ID REF DISTINTO IGUAL MAYOR_IGUAL MENOR_IGUAL CTE CADENA ERROR

%%

// reglas

programa : conjunto_sentencias {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");} 
         ;

conjunto_sentencias : sentencias_declarativas {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencias declarativas");}
                    | sentencias_ejecutables {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencias ejecutables.");}
                    | sentencias_declarativas conjunto_sentencias 
                    | sentencias_ejecutables conjunto_sentencias 
                    ;

condicion : expresion IGUAL expresion
          | expresion MAYOR_IGUAL expresion
          | expresion MENOR_IGUAL expresion
          | expresion DISTINTO expresion
          | expresion '>' expresion
          | expresion '<' expresion
          ;

clausula_while : WHILE '(' condicion ')' LOOP bloque_sentencias_control{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
               | WHILE '(' error ')' LOOP bloque_sentencias_control{addErrorSintactico("Error en la condicion del WHILE.");}
               | WHILE '(' error LOOP bloque_sentencias_control{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
               | WHILE error condicion ')' LOOP bloque_sentencias_control {addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
               | WHILE '(' condicion ')' error bloque_sentencias_control {addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");} 
               | WHILE '(' '(' condicion ')' LOOP bloque_sentencias_control{addErrorSintactico("Error en la condicion del WHILE: hay un ( de mas del lado izquierdo.");}
               | WHILE '(' condicion ')' ')' LOOP bloque_sentencias_control{addErrorSintactico("Error en la condicion del WHILE: hay un ) de mas del lado derecho.");}
               ;

clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
                   | IF '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
                   | IF '(' error ')' bloque_sentencias_control END_IF ';'{addErrorSintactico("Error en la condicion del IF.");}
                   | IF '(' error bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el )");}
                   | IF error ')' bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el (");}
                   | IF '(' condicion ')' bloque_sentencias_control error ';' {addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
                   | IF '(' '(' condicion ')' bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay un ( de mas del lado izquierdo");}
                   | IF '(' condicion ')' ')' bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay un ) de mas del lado derecho");}
                   | IF '(' error ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la condicion del IF ELSE.");}
                   | IF '(' error bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
                   | IF error ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
                   | IF '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control error ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
                   | IF '(' '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay un ( de mas del lado izquierdo");}
                   | IF '(' condicion ')' ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay un ) de mas del lado derecho");}
                   ;

bloque_sentencias_control : '{' sentencias_ejecutables '}' ';'
                          | '{' bloque_sentencias_control sentencias_ejecutables '}' ';'
                          ;

sentencias_declarativas : sentencia_declaracion_datos
                        | sentencia_declaracion_procedimiento
                        ;

sentencias_ejecutables : asignacion
                       | clausula_seleccion
                       | clausula_while
                       | sentencia_salida
                       | invocacion_procedimiento
                       | error ';' {addErrorSintactico("Syntax error");}
                       ;

sentencia_salida : OUT '(' CADENA ')' ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");}
                 | OUT '(' CADENA ')' ')' ';' {addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");} 
                 | OUT '(' '(' CADENA ')' ';' {addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}  
                 | OUT error ';' {addErrorSintactico("Error al imprimir por pantalla");}
                 ;

lista_variables: ID
               | ID ',' lista_variables
               ;

tipo : LONGINT
     | FLOAT
     ;

sentencia_declaracion_datos : tipo lista_variables ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");}
                            ;

invocacion_procedimiento : ID '(' lista_parametros_invocacion ')' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");}
			 | ID '(' ')' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");}
             | ID '(' error ';' {addErrorSintactico("Error al invocar procedimiento: falta )");}
             | ID error ')' ';' {addErrorSintactico("Error al invocar procedimiento: falta (");}
             | ID '(' '(' ')' ';' {addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
             | ID '('  ')' ')' ';' {addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
             | ID '(' lista_parametros_invocacion error ';' {addErrorSintactico("Error al invocar procedimiento: falta )");}
             | ID '(' error ')' ';' {addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
             | ID error lista_parametros_invocacion ')' ';' {addErrorSintactico("Error al invocar procedimiento: falta (");}
			 | ID '(' '(' lista_parametros_invocacion ')' ';' {addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
             | ID '(' lista_parametros_invocacion ')' ')' ';' {addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
             ;

sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
				    | PROC ID '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
                    | PROC error '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta ID");} 
                    | PROC ID '(' error NI '=' cte '{' cuerpo_procedimiento '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta )");} 
                    | PROC ID error ')' NI '=' cte '{' cuerpo_procedimiento '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta (");} 
                    | PROC ID '(' ')' error '=' cte '{' cuerpo_procedimiento '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta NI");} 
                    | PROC ID '(' ')' NI error cte '{' cuerpo_procedimiento '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta =");} 
                    | PROC ID '(' ')' NI '=' error '{' cuerpo_procedimiento '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta cte");}
                    ;

cuerpo_procedimiento : sentencias_declarativas
                     | sentencias_ejecutables
                     | sentencias_declarativas cuerpo_procedimiento
                     | sentencias_ejecutables cuerpo_procedimiento
                     ;

lista_parametros_invocacion: parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion
                           ;

lista_parametros_declaracion : parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion
			 ;

parametro_declaracion: tipo ID
         | REF tipo ID
         ;

parametro_invocacion : ID ':' ID
                     | ID ':' error { addErrorSintactico("Error en la definicion de parametro del lado derecho");}
                     | error ':' ID { addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
                     ;

asignacion : ID '=' expresion ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
           | ID '=' error ';' {addErrorSintactico("Error de asignación a la derecha.");}
           | error '=' expresion ';' {addErrorSintactico("Error de asignación a la izquierda.");}
           //| ID '=' expresion error {addErrorSintactico("Error de asignación: falta ;");}
           ;

expresion : expresion '+' termino ';'
          | expresion '-' termino ';'
          | termino
          ;

termino : termino '*' factor
        | termino '/' factor
        | factor
        ;

factor : ID
       | cte
       | ERROR
       ;

cte : CTE {if (!analizadorLexico.check_rango_longint($1.sval)){
                addErrorSintactico("Error longint fuera de rango");}} 
    | '-' CTE {analizadorLexico.updateTablaSimbolos($2.sval);
               $$ = new ParserVal("-"+$2.sval);}
    ;
%%

// codigo


AnalizadorLexico analizadorLexico = new AnalizadorLexico();
ArrayList<String> erroresSintacticos = new ArrayList<>();
ArrayList<String> erroresParser = new ArrayList<>();
ArrayList<String> tokens = new ArrayList<>();
ArrayList<String> estructuras = new ArrayList<>();

public int yylex(){
		int token = analizadorLexico.yylex();
        //Si el token es un ID, CTE, CADENA necesito el valor del lexema.
		if(token == 270 || token == 276 || token  == 277)
            yylval = analizadorLexico.yylval();
		tokens.add(token+"");
		return token;
}

public ArrayList<String> getTokens(){
	return this.tokens;
}

public ArrayList<String> getEstructuras(){
	return this.estructuras;
}

public void addErrorSintactico(String error){
    erroresSintacticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + "  " + error);
}

public void yyerror(String error){
	    erroresParser.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + "  " + error);
}

public StringBuilder copiarErrores(ArrayList<String> errores){
    StringBuilder out = new StringBuilder();
    for ( int i=0; i<errores.size();i++){
        out.append("\t" +errores.get(i));
        out.append("\n");
    }
    return out;
}

public String getErrores(){
    StringBuilder errores = new StringBuilder("Errores Lexicos: ");
    errores.append("\n");
    errores.append(copiarErrores(analizadorLexico.getErrores()));
    errores.append("\n");
    errores.append("Errores Sintacticos: ");
    errores.append("\n");
    errores.append(copiarErrores(this.erroresSintacticos));
    return errores.toString();
}


public static void main(String args[]){
	Parser parser = new Parser();
	System.out.println(parser.yyparse());
	System.out.println(parser.getErrores());
	System.out.println(parser.getTokens());
	System.out.println(parser.getEstructuras());
}