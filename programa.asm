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
_x1@main dd ?
_Constante0 dd 2
_b@main dd ?
_Constante1 dd 4
_Constante2 dd 5
_a@main dd ?
_Constante3 dd -5
_Cadena0 db "conversion implicita simple", 0
_x2@main dd ?
_Constante4 dd -3.0f+23
_c@main dd ?
_@aux0 dd ?
.code
START:
MOV EAX, _Constante1
MOV _a@main, EAX
MOV EAX, _Constante2
MOV _b@main, EAX
MOV EAX, _Constante4
MOV _x2@main, EAX
MOV EAX, _a@main
IMUL EAX, _b@main
ADD EAX, _Constante3
JO ERROR_OVERFLOW_SUMA
MOV _c@main, EAX
invoke MessageBox, NULL, addr _Cadena0, addr  _Cadena0, MB_OK
MOV EAX, _Constante0
MOV _@aux0, EAX
FILD _@aux0
FSTP _@aux0
MOV _x1@main, EAX
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
