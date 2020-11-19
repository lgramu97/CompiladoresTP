.386
.STACK 200h
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
ERROR_DIVISION_CERO db "Error: no es posible divir por cero."
ERROR_OVERFLOW_SUMA db "Error: overflow en suma."
FLOAT_CERO DQ 0.0
MINIMO_POSITIVO DQ 1.17549435e-38
MINIMO_NEGATIVO DQ -1.17549435e-38
MAXIMO_POSITIVO DQ 3.40282347e38
MAXIMO_NEGATIVO DQ -3.40282347e38
@aux DQ ?
_a@main@procedimiento dd ?
_x@main@procedimiento dd ?
_b@main dd ?
_b@main@procedimiento dd ?
_a@main dd ?
_z@main@procedimiento dd ?
_w@main dd ?
_Constante0 dd 1
_Constante1 dd 2
_Constante2 dd 3
_Constante3 dd 4
_Constante4 dd 2.0
_c@main@procedimiento dd ?
_@aux1 dd ?
_@aux0 dd ?
.code
START:
MOV EAX, _Constante2
MOV _a@main, EAX
MOV EAX, _Constante3
MOV _b@main, EAX
MOV EAX, _Constante4
MOV _w@main, EAX
MOV EAX, [_b@main]
MOV _z@main@procedimiento, EAX
MOV EAX, _w@main
MOV _x@main@procedimiento, EAX
CALL procedimiento@main
invoke ExitProcess, 0
__etiquetaErrorOverflow__:
invoke MessageBox, NULL, addr ERROR_OVERFLOW_SUMA, addr  ERROR_OVERFLOW_SUMA, MB_OK
invoke ExitProcess, 0
__etiquetaErrorDivCero__:
invoke MessageBox, NULL, addr ERROR_DIVISION_CERO, addr ERROR_DIVISION_CERO, MB_OK
invoke ExitProcess, 0
END START
FLOAT_VALIDO:
RET
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
procedimiento@main@procedimiento@invocacion:
MOV EAX, _a@main@procedimiento
MOV _b@main@procedimiento, EAX
RET
invocacion@main@procedimiento:
MOV EAX, _a@main@procedimiento
MOV _b@main@procedimiento, EAX
CALL procedimiento@main@procedimiento@invocacion
RET
procedimiento@main:
MOV EAX, _b@main@procedimiento
MOV _a@main@procedimiento, EAX
MOV EAX, _z@main@procedimiento
MOV _@aux0, EAX
FILD _@aux0
FSTP _@aux0
FLD _x@main@procedimiento
FLD _@aux0
FMUL 
FSTP _@aux1
MOV EAX, _@aux1
MOV _c@main@procedimiento, EAX
CALL invocacion@main@procedimiento
RET
