%{
	package componentes;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
	import java.util.Scanner;
	import javax.swing.JFileChooser;
    import java.util.Stack;
    import componentes.SimboloPolaca;
	
%}

%token IF THEN END_IF OUT FUNC RETURN ELSE LONGINT FLOAT WHILE LOOP PROC NI ID REF DISTINTO IGUAL MAYOR_IGUAL MENOR_IGUAL CTE CADENA ERROR

%%

// reglas

programa : conjunto_sentencias {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");} 
         ;

conjunto_sentencias : sentencias_declarativas 
                    | sentencias_ejecutables
                    | sentencias_declarativas conjunto_sentencias 
                    | sentencias_ejecutables conjunto_sentencias 
                    ;

condicion : expresion IGUAL expresion
     | expresion MAYOR_IGUAL expresion
     | expresion MENOR_IGUAL expresion
     | expresion DISTINTO expresion
     | expresion '>' expresion
     | expresion '<' expresion
     | expresion error {addErrorSintactico("Error en la condicion");}
     ;

condicion_accion: condicion {apilarPasoIncompleto(SimboloPolaca.BF);}
	/* PASO 1 del IF y PASO 2 del WHILE
		TERMINA LA EVALUACIÓN DE LA CONDICIÓN. (Generar BF)
		1. Crear espacio en blanco
		2. Apilar la dirección del paso incompleto.
		3. Crear el paso del BF.
	*/
	;

clausula_while : incio_while '(' condicion_accion ')' LOOP '{' bloque_sentencias_control '}'';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
               | incio_while '(' error ')' LOOP '{' bloque_sentencias_control '}'';'{addErrorSintactico("Error en la condicion del WHILE.");}
               | incio_while '(' error LOOP '{' bloque_sentencias_control '}'';'{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
               | incio_while error condicion_accion ')' LOOP '{' bloque_sentencias_control '}'';' {addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
               | incio_while '(' condicion_accion ')' error '{' bloque_sentencias_control'}' ';'{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");}
               | incio_while '(' '(' error condicion_accion  ')' LOOP '{' bloque_sentencias_control'}'';'{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");}
               | incio_while '(' condicion_accion ')' ')' error LOOP '{' bloque_sentencias_control'}'';'{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");}
               ;

incio_while: WHILE {apilarPasoActual();}
	   ;

clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
                   | IF '(' condicion_accion ')''{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
                   | IF '(' error ')' '{' bloque_then '}' END_IF ';'{addErrorSintactico("Error en la condicion del IF.");}
                   | IF '('  error '{' bloque_then '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el )");}
                   | IF  error condicion_accion ')' '{' bloque_then '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el (");}
                   | IF '(' condicion_accion ')' '{' bloque_then '}' error  ';' {addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
                   | IF '(' '(' error condicion_accion ')' '{' bloque_then '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");}
                   | IF '(' condicion_accion ')' ')' error '{' bloque_then '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");}
                   | IF '(' error ')'  '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {addErrorSintactico("Error en la condicion del IF ELSE.");}
                   | IF '('  error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
                   | IF error condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
                   | IF '(' condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' error ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
                   | IF '(' '(' error condicion_accion ')''{'  bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");}
                   | IF '(' condicion_accion ')' ')' error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");}
                   ;

bloque_then: bloque_sentencias_control {completarPasoIncompleto();apilarPasoIncompleto(SimboloPolaca.BI);}
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
                       | clausula_seleccion {completarPasoIncompleto();}
                       	// PASO 3
                       	// #_paso_incomp = desapilar_paso(); //Desapila dirección incompleta.
			// completar_paso(#_paso_incomp, #_paso_actual + 1); //Completa el destino de
			// BI.
                       | clausula_while {completarPasoIncompleto();generarBIinicio();}
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
             | ID '(' '(' error ';' {addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
             | ID '('  ')' ')' error ';' {addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
             | ID '(' lista_parametros_invocacion error ';' {addErrorSintactico("Error al invocar procedimiento: falta )");}
             | ID '(' error ')' ';' {addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
             | ID error lista_parametros_invocacion ')' ';' {addErrorSintactico("Error al invocar procedimiento: falta (");}
			 | ID '(' '(' error lista_parametros_invocacion ')' ';' {addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
             | ID '(' lista_parametros_invocacion ')' ')' error ';' {addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
             | ID error ';' {addErrorSintactico("Error al invocar procedimiento.");}
             ;

sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
				    | PROC ID '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
                    | PROC error '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta ID");} 
                    | PROC ID '(' error NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta )");} 
                    | PROC ID error ')' NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta (");} 
                    | PROC ID '(' '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");} 
                    | PROC ID  '('  ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}       
                    | PROC ID '(' ')' error '=' cte '{' conjunto_sentencias '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta NI");} 
                    | PROC ID '(' ')' NI error cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta =");} 
                    | PROC ID '(' ')' NI '=' error '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta cte");}
                    | PROC ID '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
                    | PROC error '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta ID");} 
                    | PROC ID '(' lista_parametros_declaracion error NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta )");} 
                    | PROC ID error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta (");} 
                    | PROC ID '(' lista_parametros_declaracion ')' error '=' cte '{' conjunto_sentencias '}' ';'  {addErrorSintactico("Error al declarar procedimiento: falta NI");} 
                    | PROC ID '(' lista_parametros_declaracion ')' NI error cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta =");} 
                    | PROC ID '(' lista_parametros_declaracion ')' NI '=' error '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: falta cte");}
                    | PROC ID '(' '(' error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");} 
                    | PROC ID  '(' lista_parametros_declaracion ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';' {addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}       
                    ;


lista_parametros_invocacion: parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion ',' error {addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
                           
                           ;

lista_parametros_declaracion : parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion ',' error {addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
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
           ;

expresion : expresion '+' termino {addSimbolo($1.sval);addSimbolo($3.sval);addSimbolo($2.sval);}
          | expresion '-' termino {addSimbolo($1.sval);addSimbolo($3.sval);addSimbolo($2.sval);}
          | termino
          ;

termino : termino '*' factor {addSimbolo($1.sval);addSimbolo($3.sval);addSimbolo($2.sval);}
        | termino '/' factor {addSimbolo($1.sval);addSimbolo($3.sval);addSimbolo($2.sval);}
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


ArrayList<SimboloPolaca> listaReglas = new ArrayList<>();
Stack<Integer> pasosIncompletos = new Stack<>();
AnalizadorLexico analizadorLexico = new AnalizadorLexico();
ArrayList<String> erroresSintacticos = new ArrayList<>();
ArrayList<String> erroresParser = new ArrayList<>();
ArrayList<String> tokens = new ArrayList<>();
ArrayList<String> estructuras = new ArrayList<>();

public void addSimbolo(String simbolo) {
    System.out.println("Valor a agregar : " + simbolo);
	listaReglas.add(new SimboloPolaca(simbolo));
}

public void apilarPasoIncompleto(String nombre) {
	apilarPasoActual();
	addSimbolo(null);
	addSimbolo(nombre);
}

public void completarPasoIncompleto() {
	int posIncompleto = pasosIncompletos.pop();
	SimboloPolaca simbolo = listaReglas.get(posIncompleto);
	int pos = listaReglas.size()+2;
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
		if(token == 270 || token == 276 || token  == 277)
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
    return "Errores Lexicos: " + "\n" +
            copiarErrores(analizadorLexico.getErrores()) +
            "\n" +
            "Errores Sintacticos: " +
            "\n" +
            copiarErrores(this.erroresSintacticos);
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

			salida.write("Tokens detectados en el codigo fuente: " + "\n");
            for (String token : tokens) {
                salida.write("\t" + token + "\n");
            }

			salida.write("\n" + "Estructuras detectadas en el codigo fuente: " + "\n");
            for (String estructura : estructuras) {
                salida.write("\t" + estructura + "\n");
            }

			salida.write("\n"+"Contenido de la tabla de simbolos: " + "\n");
			salida.write(this.getAnalizadorLexico().getDatosTabla_simbolos());

			salida.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
}


public static void main(String[] args){
	Parser parser = new Parser();
	parser.yyparse();
	System.out.println(parser.getErrores());

	ArrayList<String> tokens = parser.getTokens();
	System.out.println();
	System.out.println("Tokens detectados en el codigo fuente: ");
    for (String token : tokens) {
        System.out.println(token);
    }

	ArrayList<String> estructuras = parser.getEstructuras();
	System.out.println();
	System.out.println("Estructuras detectadas en el codigo fuente: ");
    for (String estructura : estructuras) {
        System.out.println(estructura);
    }
	
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
    for (SimboloPolaca simboloPolaca : lista) {
        System.out.println("VALOR POLACA: " + simboloPolaca.getSimbolo());
    }

}