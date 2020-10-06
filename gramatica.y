%{
	package componentes;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
	import java.util.Scanner;
	import javax.swing.JFileChooser;

	
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

clausula_while : WHILE '(' condicion ')' LOOP '{' bloque_sentencias_control '}'';'{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
               | WHILE '(' error ')' LOOP '{' bloque_sentencias_control '}'';'{addErrorSintactico("Error en la condicion del WHILE.");}
               | WHILE '(' error LOOP '{' bloque_sentencias_control '}'';'{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
               | WHILE error condicion ')' LOOP '{' bloque_sentencias_control '}'';' {addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
               | WHILE '(' condicion ')' error '{' bloque_sentencias_control'}' ';'{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");} 
               | WHILE '(' '(' error condicion  ')' LOOP '{' bloque_sentencias_control'}'';'{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");}
               | WHILE '(' condicion ')' ')' error LOOP '{' bloque_sentencias_control'}'';'{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");}
               ;

clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
                   | IF '(' condicion ')''{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
                   | IF '(' error ')' '{' bloque_sentencias_control '}' END_IF ';'{addErrorSintactico("Error en la condicion del IF.");}
                   | IF '('  error '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el )");}
                   | IF  error condicion ')' '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: falta el (");}
                   | IF '(' condicion ')' '{' bloque_sentencias_control '}' error  ';' {addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
                   | IF '(' '(' error condicion ')' '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");}
                   | IF '(' condicion ')' ')' error '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");}
                   | IF '(' error ')'  '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la condicion del IF ELSE.");}
                   | IF '('  error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
                   | IF error condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
                   | IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' error ';' {addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
                   | IF '(' '(' error condicion ')''{'  bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");}
                   | IF '(' condicion ')' ')' error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';' {addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");}
                   ;

bloque_sentencias_control :  sentencias_ejecutables 
                          |  sentencias_ejecutables bloque_sentencias_control
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

expresion : expresion '+' termino 
          | expresion '-' termino 
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
			for (int i = 0; i<tokens.size();i++) {
				salida.write("\t" + tokens.get(i) + "\n");
			}
			
			salida.write("\n" + "Estructuras detectadas en el codigo fuente: " + "\n");
			for (int i = 0; i<estructuras.size();i++) {
				salida.write("\t" + estructuras.get(i) + "\n");
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


public static void main(String args[]){
	Parser parser = new Parser();
	parser.yyparse();
	System.out.println(parser.getErrores());
	
	ArrayList<String> tokens = parser.getTokens();
	System.out.println();
	System.out.println("Tokens detectados en el codigo fuente: ");
	for (int i = 0; i<tokens.size();i++) {
		System.out.println(tokens.get(i));
	}
	
	ArrayList<String> estructuras = parser.getEstructuras();
	System.out.println();
	System.out.println("Estructuras detectadas en el codigo fuente: ");
	for (int i = 0; i<estructuras.size();i++) {
		System.out.println(estructuras.get(i));
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
	
}