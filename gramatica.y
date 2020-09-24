// {%

// %}

%token IF THEN END_IF OUT FUNC RETURN ELSE LONGINT FLOAT WHILE LOOP PROC NI ID REF DISTINTO IGUAL MAYOR_IGUAL MENOR_IGUAL CTE CADENA error

%%

// reglas

programa : conjunto_sentencias 
         ;

conjunto_sentencias : sentencias_declarativas
                    | sentencias_ejecutables
                    | conjunto_sentencias sentencias_declarativas
                    | conjunto_sentencias sentencias_ejecutables
                    ;

condicion : expresion IGUAL expresion
          | expresion MAYOR_IGUAL expresion
          | expresion MENOR_IGUAL expresion
          | expresion DISTINTO expresion
          | expresion '>' expresion
          | expresion '<' expresion
          ;

clausula_while : WHILE '(' condicion ')' LOOP bloque_sentencias_control
               ;

clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control END_IF ';'
                   | IF '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'
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
                       | error ';' {print("sintax error");}
                       ;

sentencia_salida : OUT '(' CADENA ')' ';'
                 ;

lista_variables: ID
               | ID ',' lista_variables
               ;

tipo : LONGINT
     | FLOAT
     ;

sentencia_declaracion_datos : tipo lista_variables ';'
                            ;

invocacion_procedimiento : ID '(' lista_parametros_invocacion ')' ';'
                         ;

sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'
                          ;

cuerpo_procedimiento : sentencias_declarativas
                     | sentencias_ejecutables
                     | sentencias_declarativas cuerpo_procedimiento
                     | sentencias_ejecutables cuerpo_procedimiento
                     ;

lista_parametros_invocacion: /* Esta vacio o lambda */
			   | parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion
                           | parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion
                           ;

lista_parametros_declaracion : /* Esta vacio o lambda*/
			 | parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion
			 | parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion
			 ;

parametro_declaracion: tipo ID
         | REF tipo ID
         ;

parametro_invocacion : ID ':' ID
                     ;

asignacion : ID '=' expresion ';'
           | error ';' {print("Error de asignación!");}
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

cte : CTE 
    | '-' CTE /* TODO: accion chequear rango. */

%%

// codigo

