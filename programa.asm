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
@aux DQ ?
_x1@main dd ?
_x2@main dd ?
_f2@main dd ?
_Constante0 dd 0
_Cadena0 db "iguales", 0
_Constante1 dd 1
_Constante2 dd 0.0
_Constante3 dd 1.0
_x3@main dd ?
_Constante4 dd 3.0
_f3@main dd ?
_@aux1 dd ?
_f1@main dd ?
_Cadena1 db "no iguales", 0
_@aux0 dd ?
.code
START:
MOV EAX, _Constante0
MOV _x1@main, EAX
MOV EAX, _Constante1
MOV _x2@main, EAX
MOV EAX, _Constante0
MOV _x3@main, EAX
MOV EAX, _Constante3
MOV _f1@main, EAX
MOV EAX, _Constante2
MOV _f2@main, EAX
MOV EAX, _Constante4
MOV _f3@main, EAX
MOV EAX, _x1@main
MOV _@aux0, EAX
FILD _@aux0
FSTP _@aux0
FLD _@aux0
FADD _f1@main
FST @aux
CALL OVERFLOW_FLOAT
FSTP _@aux1
MOV EAX, _@aux1
MOV _f2@main, EAX
MOV EAX , _f2@main
CMP EAX , _x2@main
JNE L32
invoke MessageBox, NULL, addr _Cadena0, addr  _Cadena0, MB_OK
JMP L35
L32:
invoke MessageBox, NULL, addr _Cadena1, addr  _Cadena1, MB_OK
L35:
invoke ExitProcess, 0
__etiquetaErrorOverflow__:
invoke MessageBox, NULL, addr ERROR_OVERFLOW_SUMA, addr  ERROR_OVERFLOW_SUMA, MB_OK
invoke ExitProcess, 0
__etiquetaErrorDivCero__:
invoke MessageBox, NULL, addr ERROR_DIVISION_CERO, addr ERROR_DIVISION_CERO, MB_OK
invoke ExitProcess, 0
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
END START
