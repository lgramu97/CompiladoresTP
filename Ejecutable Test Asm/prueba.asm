.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
ERROR_DIVISION_CERO db "Error: no es posible divir por cero.", 0
ERROR_OVERFLOW_SUMA db "Error: overflow en suma.", 0
FLOAT_CERO DQ 0.0
MINIMO_POSITIVO DQ 1.17549435e-38
MINIMO_NEGATIVO DQ -1.17549435e-38
MAXIMO_POSITIVO DQ 3.40282347e38
MAXIMO_NEGATIVO DQ -3.40282347e38
@aux DD ?
_a@main dd ?
_al1@main@suma@anidado dd ?
_x2@main dd ?
_l1@main@suma dd ?
_Cadena0 db "es igual", 0
_Cadena1 db "es distinto", 0
_Cadena2 db "es mayor", 0
_Cadena3 db "se pasa bien", 0
_x3@main@suma dd ?
_Cadena4 db "Finaliza el programa", 0
_c@main dd ?
_@aux0 dd ?
_x1@main dd ?
_Constante0 dd 14
_b@main dd ?
_Constante1 dd -2
_Constante2 dd -4
_Cadena5 db "es menor", 0
_l2@main@suma dd ?
_Constante3 dd 0
_comparacion@main@suma@anidado dd ?
_Constante4 dd 1
_Constante5 dd 0.0
_Constante6 dd 2
_Constante7 dd 3
_Constante8 dd 2.0
_Constante9 dd 4.0
_Cadena6 db "multiplico valor por -2", 0
_Cadena7 db "Termino de comparar", 0
.code
START:
MOV EAX, _Constante7
MOV _a@main, EAX
MOV EAX, _Constante7
MOV _b@main, EAX
MOV EAX, _Constante3
MOV _c@main, EAX
FLD _Constante5
FSTP _x1@main
FLD _Constante9
FSTP _x2@main
MOV EAX, [_b@main]
MOV _l1@main@suma, EAX
MOV EAX, [_a@main]
MOV _l2@main@suma, EAX
CALL suma@main
MOV EAX , _b@main
CMP EAX , _Constante7
JE L33
invoke MessageBox, NULL, addr _Cadena1, addr  _Cadena1, MB_OK
JMP L36
L33:
invoke MessageBox, NULL, addr _Cadena0, addr  _Cadena0, MB_OK
L36:
CALL imprimir_fin@main
invoke ExitProcess, 0
__etiquetaErrorOverflow__:
invoke MessageBox, NULL, addr ERROR_OVERFLOW_SUMA, addr  ERROR_OVERFLOW_SUMA, MB_OK
invoke ExitProcess, 0
__etiquetaErrorDivCero__:
invoke MessageBox, NULL, addr ERROR_DIVISION_CERO, addr ERROR_DIVISION_CERO, MB_OK
invoke ExitProcess, 0
OVERFLOW_FLOAT:
FINIT
FLD @aux
FCOM MAXIMO_POSITIVO
FSTSW AX
SAHF
JA __etiquetaErrorOverflow__
FINIT
FLD @aux
FCOM MINIMO_POSITIVO
FSTSW AX
SAHF
JA FLOAT_VALIDO
FINIT
FLD @aux
FCOM MAXIMO_NEGATIVO
FSTSW AX
SAHF
JB __etiquetaErrorOverflow__
FINIT
FLD @aux
FCOM MINIMO_NEGATIVO
FSTSW AX
SAHF
JB FLOAT_VALIDO
FINIT
FLD @aux
FCOM FLOAT_CERO
FSTSW AX
SAHF
JE FLOAT_VALIDO
JMP __etiquetaErrorOverflow__
FLOAT_VALIDO:
RET
anidado@main@suma:
MOV EAX, _Constante2
MOV _comparacion@main@suma@anidado, EAX
L4:
MOV EAX , _comparacion@main@suma@anidado
CMP EAX , _al1@main@suma@anidado
JNL L19
MOV EAX, _comparacion@main@suma@anidado
IMUL EAX, _Constante1
MOV _comparacion@main@suma@anidado, EAX
invoke MessageBox, NULL, addr _Cadena6, addr  _Cadena6, MB_OK
JMP L4
L19:
invoke MessageBox, NULL, addr _Cadena7, addr  _Cadena7, MB_OK
RET
suma@main:
MOV EAX , _l1@main@suma
CMP EAX , _Constante7
JNE L10
invoke MessageBox, NULL, addr _Cadena3, addr  _Cadena3, MB_OK
JMP L11
L10:
L11:
MOV EAX, _l1@main@suma
ADD EAX, _l2@main@suma
JO __etiquetaErrorOverflow__
MOV _@aux0, EAX
FILD _@aux0
FSTP _@aux0
FLD _@aux0
FSTP _x3@main@suma
MOV EAX, _Constante0
MOV _l2@main@suma, EAX
FINIT
FLD _x3@main@suma
FCOMP _Constante8
FSTSW AX
SAHF
JNA L29
invoke MessageBox, NULL, addr _Cadena2, addr  _Cadena2, MB_OK
JMP L32
L29:
invoke MessageBox, NULL, addr _Cadena5, addr  _Cadena5, MB_OK
L32:
MOV EAX, _l2@main@suma
MOV _al1@main@suma@anidado, EAX
CALL anidado@main@suma
RET
imprimir_fin@main:
invoke MessageBox, NULL, addr _Cadena4, addr  _Cadena4, MB_OK
RET
END START
