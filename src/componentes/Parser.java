//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
	package componentes;
    import java.util.ArrayList;

	
//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short END_IF=259;
public final static short OUT=260;
public final static short FUNC=261;
public final static short RETURN=262;
public final static short ELSE=263;
public final static short LONGINT=264;
public final static short FLOAT=265;
public final static short WHILE=266;
public final static short LOOP=267;
public final static short PROC=268;
public final static short NI=269;
public final static short ID=270;
public final static short REF=271;
public final static short DISTINTO=272;
public final static short IGUAL=273;
public final static short MAYOR_IGUAL=274;
public final static short MENOR_IGUAL=275;
public final static short CTE=276;
public final static short CADENA=277;
public final static short ERROR=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    4,    4,    4,    4,    4,
    4,    6,    6,    6,    6,    6,    6,    6,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    7,    7,    2,    2,    3,    3,    3,    3,
    3,    3,   12,   12,   12,   12,   14,   14,   15,   15,
    9,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   10,   10,   10,   10,   10,   10,   10,   10,
   19,   19,   19,   19,   16,   16,   16,   17,   17,   17,
   21,   21,   20,   20,   20,   11,   11,   11,    5,    5,
    5,   22,   22,   22,   23,   23,   23,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    6,    6,    5,    6,    6,    7,    7,    7,    9,
    7,    6,    6,    7,    8,    8,    9,    8,    8,    9,
   10,   10,    4,    5,    1,    1,    1,    1,    1,    1,
    1,    2,    5,    6,    6,    3,    1,    3,    1,    1,
    3,    5,    4,    4,    4,    5,    5,    5,    5,    5,
    6,    6,   12,   11,   11,   11,   11,   11,   11,   11,
    1,    1,    2,    2,    1,    3,    5,    1,    3,    5,
    2,    3,    3,    3,    3,    4,    4,    4,    4,    4,
    1,    3,    3,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   49,   50,    0,    0,    0,    0,    1,
    0,    0,   39,   38,   35,   36,   37,   40,   41,    0,
   42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    0,   95,   98,   97,
    0,    0,   96,    0,   94,    0,    0,    0,    0,    0,
   46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   51,   99,   88,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   55,    0,
    0,    0,   54,    0,    0,    0,   53,    0,    0,   87,
   86,   48,    0,    0,   92,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   43,    0,    0,   14,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   81,    0,
    0,   85,   84,   83,   60,    0,   59,   56,    0,   57,
   58,    0,   52,   89,   90,    0,    0,   23,    0,    0,
    0,   22,    0,    0,    0,    0,    0,    0,   44,   45,
   15,   13,    0,   16,   12,    0,    0,    0,    0,   82,
    0,    0,    0,    0,    0,    0,   61,   62,   33,    0,
    0,   21,    0,    0,    0,    0,    0,    0,   24,   19,
    0,   17,   18,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   77,   34,   29,    0,   28,   25,    0,   26,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   80,   27,    0,    0,   30,   20,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   31,   32,   73,
   74,    0,    0,    0,    0,    0,    0,    0,    0,   65,
   67,   66,   68,   69,   70,   64,    0,   63,
};
final static short yydgoto[] = {                          9,
   10,  258,  259,   49,   50,   13,   81,   14,   15,   16,
   17,   18,   19,   37,   20,   64,  105,   43,  260,   65,
  106,   44,   45,
};
final static short yysindex[] = {                      -121,
   31,  -27,    6,    0,    0,    7, -161,  -36,    0,    0,
 -121, -121,    0,    0,    0,    0,    0,    0,    0, -226,
    0,  -25,   15,  -40,   32,  -37,  -25,  -39,   26,    8,
    1,   -1,  -43,    0,    0,   70,   78,    0,    0,    0,
 -119,   34,    0,   52,    0,   25,  -19,  -25,  114,  -26,
    0,  143,  -86,  151,  -11,  -25,  153,  157,  158,   -6,
  142,  144,  145,  160,  159,   -9,    4,   -8,  -34,  146,
   57, -226,    0,    0,    0,  -25,  -25,  -25,  -25,  -85,
 -144,   25, -117,  165,  -15,  -25,  -25,  -25,  -25,  -25,
  -25,   -4,  166,  -59,   25,  -58,  170,  -33,  -57,  -55,
  -54,  -98, -202,  -52,  178,  177,  -45, -132,    0,  169,
 -130,  202,    0,  207,  226,  209,    0,  211,   23,    0,
    0,    0,   21,   59,    0,    0,  147, -106,  214,   25,
  -83,  216,   25,   25,   25, -100,   77,   77,   77,   77,
   77,   77,  217,    0,  218,   25,    0,   25,   12,   25,
   25,   13,  220,  221,  222,   14,  224,  -46,    0,    9,
 -103,    0,    0,    0,    0,  242,    0,    0,  228,    0,
    0,  229,    0,    0,    0,  230,  167,    0,   35,  231,
   25,    0,   36,  -81,  -80,  232,  234,   25,    0,    0,
    0,    0,   25,    0,    0,   25,  -22,  -22,  -22,    0,
  -22,  -22,  -24,  235,  253, -130,    0,    0,    0,  239,
  240,    0,   42,  241,  243,   25,  244,   25,    0,    0,
  -82,    0,    0,  181,  182,  183,  184,  185,  186,  187,
  -22, -103,    0,    0,    0,  252,    0,    0,   53,    0,
   54,  255,  256, -121, -121, -121, -121, -121, -121, -121,
  193,    0,    0,  258,  259,    0,    0, -121, -121,  194,
  195,  196,  197,  198,  199,  200, -121,    0,    0,    0,
    0,  267,  268,  269,  270,  271,  272,  273,  208,    0,
    0,    0,    0,    0,    0,    0,  275,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  335,  336,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  278,    0,    0,    0,    0,
    0,    0,    0,  -31,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -32,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  297,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  298,  299,  300,  301,
  302,  303,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -30,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  304,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  223,  225,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  175,  141,   41,   33,   43,    0,  -23,    0,    0,    0,
    0,    0,    0,  274,  -44,   11,    0, -128, -162,  -93,
 -134,  113,  118,
};
final static int YYTABLESIZE=350;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   56,   41,   53,   32,   41,   41,  119,  152,   75,   91,
   76,   91,   24,   91,  203,  104,   76,  166,   77,   41,
   41,   82,   41,   83,   33,  135,  205,   91,   91,   96,
   91,  112,  116,   91,  103,   90,  143,   80,   67,   68,
   12,   63,   69,   36,  114,   26,   28,   60,  107,  113,
  117,   12,   12,  157,  144,   46,  128,  156,  131,   54,
   57,  136,   78,  172,   42,   58,  158,   79,  224,  225,
  226,  147,  227,  228,  230,   71,   76,  115,   77,  174,
   84,  173,  261,  262,  263,  264,  265,  266,   97,   21,
   51,   22,   75,   78,   29,  270,  271,  252,   79,   76,
   78,   77,  251,   80,  279,   79,  179,   80,   30,  183,
  184,  185,  233,   72,  129,  121,  104,  175,  130,   76,
  127,   77,  191,  163,  192,   61,  194,  195,  137,  138,
  139,  140,  141,  142,    1,    2,   73,  164,    3,   62,
   11,  132,    4,    5,    6,  133,    7,   80,    8,    1,
    2,   11,   11,    3,   85,  186,   74,  213,  187,    6,
    4,    5,  188,    8,  221,    4,    5,  102,  177,  222,
    1,    2,  223,  242,    3,  180,  243,  215,  217,  181,
    6,  216,  218,   92,    8,   34,   35,  104,  123,  124,
   93,   94,  239,   98,  241,  125,  126,   99,  100,  107,
  110,  108,  111,  109,  120,  134,  145,  146,  148,  202,
  149,  153,   70,  154,  155,   47,   55,  159,  160,   31,
  161,  118,  150,   75,  162,   76,   38,  165,   23,   38,
   38,  229,   39,  151,   40,   39,   39,   40,   40,   52,
   91,   91,   91,   91,   38,   86,   87,   88,   89,  101,
   39,   39,   40,   39,   66,   95,   61,    4,    5,   61,
  167,   25,   27,   59,  102,  168,  169,  170,   62,  171,
   62,  176,  178,   62,  182,  189,  190,  204,  193,  196,
  197,  198,  199,  200,  201,  206,  207,  208,  209,  212,
  219,  210,  220,  211,  214,  231,  232,  234,  235,  237,
  236,  238,  240,  244,  245,  246,  247,  248,  249,  250,
  253,  254,  255,  256,  257,  267,  268,  269,  272,  273,
  274,  275,  276,  277,  278,  280,  281,  282,  283,  284,
  285,  286,  287,  288,    2,    3,   47,   78,    9,    6,
    7,    8,   10,   11,   79,  122,    0,   71,    0,   72,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   45,   40,   40,   45,   45,   41,   41,   41,   41,
   41,   43,   40,   45,   61,   60,   43,  111,   45,   45,
   45,   41,   45,   47,   61,   41,  161,   59,   60,   41,
   62,   41,   41,   60,   41,   62,   41,  123,   40,   41,
    0,   41,   32,  270,   41,   40,   40,   40,   58,   59,
   59,   11,   12,  256,   59,   41,   80,  102,   82,   27,
   28,   85,   42,   41,   22,   40,  269,   47,  197,  198,
  199,   95,  201,  202,  203,   33,   43,   67,   45,   59,
   48,   59,  245,  246,  247,  248,  249,  250,   56,   59,
   59,   61,   59,   42,  256,  258,  259,  232,   47,   43,
   42,   45,  231,  123,  267,   47,  130,  123,  270,  133,
  134,  135,  206,   44,  259,   59,  161,   59,  263,   43,
   80,   45,  146,  256,  148,  256,  150,  151,   86,   87,
   88,   89,   90,   91,  256,  257,   59,  270,  260,  270,
    0,  259,  264,  265,  266,  263,  268,  123,  270,  256,
  257,   11,   12,  260,   41,  256,  276,  181,  259,  266,
  264,  265,  263,  270,  188,  264,  265,  271,  128,  193,
  256,  257,  196,  256,  260,  259,  259,  259,  259,  263,
  266,  263,  263,   41,  270,   11,   12,  232,   76,   77,
  277,   41,  216,   41,  218,   78,   79,   41,   41,   58,
   41,   58,   44,   59,   59,   41,   41,  267,  267,  256,
   41,  269,  256,  269,  269,  256,  256,  270,   41,  256,
   44,  256,  256,  256,  270,  256,  270,   59,  256,  270,
  270,  256,  276,  267,  278,  276,  276,  278,  278,  277,
  272,  273,  274,  275,  270,  272,  273,  274,  275,  256,
  276,  276,  278,  276,  256,  267,  256,  264,  265,  256,
   59,  256,  256,  256,  271,   59,   41,   59,  270,   59,
  270,  125,   59,  270,   59,   59,   59,  269,  267,  267,
   61,   61,   61,  270,   61,   44,   59,   59,   59,   59,
   59,  125,   59,  259,  259,   61,   44,   59,   59,   59,
  259,   59,   59,  123,  123,  123,  123,  123,  123,  123,
   59,  259,  259,   59,   59,  123,   59,   59,  125,  125,
  125,  125,  125,  125,  125,   59,   59,   59,   59,   59,
   59,   59,  125,   59,    0,    0,   59,   41,   41,   41,
   41,   41,   41,   41,   41,   72,   -1,  125,   -1,  125,
};
}
final static short YYFINAL=9;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"IF","THEN","END_IF","OUT","FUNC","RETURN",
"ELSE","LONGINT","FLOAT","WHILE","LOOP","PROC","NI","ID","REF","DISTINTO",
"IGUAL","MAYOR_IGUAL","MENOR_IGUAL","CTE","CADENA","ERROR",
};
final static String yyrule[] = {
"$accept : programa",
"programa : conjunto_sentencias",
"conjunto_sentencias : sentencias_declarativas",
"conjunto_sentencias : sentencias_ejecutables",
"conjunto_sentencias : sentencias_declarativas conjunto_sentencias",
"conjunto_sentencias : sentencias_ejecutables conjunto_sentencias",
"condicion : expresion IGUAL expresion",
"condicion : expresion MAYOR_IGUAL expresion",
"condicion : expresion MENOR_IGUAL expresion",
"condicion : expresion DISTINTO expresion",
"condicion : expresion '>' expresion",
"condicion : expresion '<' expresion",
"clausula_while : WHILE '(' condicion ')' LOOP bloque_sentencias_control",
"clausula_while : WHILE '(' error ')' LOOP bloque_sentencias_control",
"clausula_while : WHILE '(' error LOOP bloque_sentencias_control",
"clausula_while : WHILE error condicion ')' LOOP bloque_sentencias_control",
"clausula_while : WHILE '(' condicion ')' error bloque_sentencias_control",
"clausula_while : WHILE '(' '(' condicion ')' LOOP bloque_sentencias_control",
"clausula_while : WHILE '(' condicion ')' ')' LOOP bloque_sentencias_control",
"clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' error ')' bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' error bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF error ')' bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' error ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' error bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF error ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' bloque_sentencias_control ELSE bloque_sentencias_control END_IF ';'",
"bloque_sentencias_control : '{' sentencias_ejecutables '}' ';'",
"bloque_sentencias_control : '{' bloque_sentencias_control sentencias_ejecutables '}' ';'",
"sentencias_declarativas : sentencia_declaracion_datos",
"sentencias_declarativas : sentencia_declaracion_procedimiento",
"sentencias_ejecutables : asignacion",
"sentencias_ejecutables : clausula_seleccion",
"sentencias_ejecutables : clausula_while",
"sentencias_ejecutables : sentencia_salida",
"sentencias_ejecutables : invocacion_procedimiento",
"sentencias_ejecutables : error ';'",
"sentencia_salida : OUT '(' CADENA ')' ';'",
"sentencia_salida : OUT '(' CADENA ')' ')' ';'",
"sentencia_salida : OUT '(' '(' CADENA ')' ';'",
"sentencia_salida : OUT error ';'",
"lista_variables : ID",
"lista_variables : ID ',' lista_variables",
"tipo : LONGINT",
"tipo : FLOAT",
"sentencia_declaracion_datos : tipo lista_variables ';'",
"invocacion_procedimiento : ID '(' lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID '(' ')' ';'",
"invocacion_procedimiento : ID '(' error ';'",
"invocacion_procedimiento : ID error ')' ';'",
"invocacion_procedimiento : ID '(' '(' ')' ';'",
"invocacion_procedimiento : ID '(' ')' ')' ';'",
"invocacion_procedimiento : ID '(' lista_parametros_invocacion error ';'",
"invocacion_procedimiento : ID '(' error ')' ';'",
"invocacion_procedimiento : ID error lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID '(' '(' lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID '(' lista_parametros_invocacion ')' ')' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC error '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' error NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID error ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' error '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI error cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI '=' error '{' cuerpo_procedimiento '}' ';'",
"cuerpo_procedimiento : sentencias_declarativas",
"cuerpo_procedimiento : sentencias_ejecutables",
"cuerpo_procedimiento : sentencias_declarativas cuerpo_procedimiento",
"cuerpo_procedimiento : sentencias_ejecutables cuerpo_procedimiento",
"lista_parametros_invocacion : parametro_invocacion",
"lista_parametros_invocacion : parametro_invocacion ',' parametro_invocacion",
"lista_parametros_invocacion : parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion",
"lista_parametros_declaracion : parametro_declaracion",
"lista_parametros_declaracion : parametro_declaracion ',' parametro_declaracion",
"lista_parametros_declaracion : parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion",
"parametro_declaracion : tipo ID",
"parametro_declaracion : REF tipo ID",
"parametro_invocacion : ID ':' ID",
"parametro_invocacion : ID ':' error",
"parametro_invocacion : error ':' ID",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' error ';'",
"asignacion : error '=' expresion ';'",
"expresion : expresion '+' termino ';'",
"expresion : expresion '-' termino ';'",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : cte",
"factor : ERROR",
"cte : CTE",
"cte : '-' CTE",
};

//#line 164 "gramatica.y"

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
//#line 514 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 14 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");}
break;
case 2:
//#line 17 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencias declarativas");}
break;
case 3:
//#line 18 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencias ejecutables.");}
break;
case 12:
//#line 31 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
break;
case 13:
//#line 32 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE.");}
break;
case 14:
//#line 33 "gramatica.y"
{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
break;
case 15:
//#line 34 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
break;
case 16:
//#line 35 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");}
break;
case 17:
//#line 36 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay un ( de mas del lado izquierdo.");}
break;
case 18:
//#line 37 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay un ) de mas del lado derecho.");}
break;
case 19:
//#line 40 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
break;
case 20:
//#line 41 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
break;
case 21:
//#line 42 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF.");}
break;
case 22:
//#line 43 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el )");}
break;
case 23:
//#line 44 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el (");}
break;
case 24:
//#line 45 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
break;
case 25:
//#line 46 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay un ( de mas del lado izquierdo");}
break;
case 26:
//#line 47 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay un ) de mas del lado derecho");}
break;
case 27:
//#line 48 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF ELSE.");}
break;
case 28:
//#line 49 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
break;
case 29:
//#line 50 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
break;
case 30:
//#line 51 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
break;
case 31:
//#line 52 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay un ( de mas del lado izquierdo");}
break;
case 32:
//#line 53 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay un ) de mas del lado derecho");}
break;
case 42:
//#line 69 "gramatica.y"
{addErrorSintactico("Syntax error");}
break;
case 43:
//#line 72 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");}
break;
case 44:
//#line 73 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");}
break;
case 45:
//#line 74 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}
break;
case 46:
//#line 75 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla");}
break;
case 51:
//#line 86 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");}
break;
case 52:
//#line 89 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");}
break;
case 53:
//#line 90 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");}
break;
case 54:
//#line 91 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 55:
//#line 92 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 56:
//#line 93 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
break;
case 57:
//#line 94 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
break;
case 58:
//#line 95 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 59:
//#line 96 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
break;
case 60:
//#line 97 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 61:
//#line 98 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
break;
case 62:
//#line 99 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
break;
case 63:
//#line 102 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
break;
case 64:
//#line 103 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
break;
case 65:
//#line 104 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 66:
//#line 105 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 67:
//#line 106 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 68:
//#line 107 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 69:
//#line 108 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 70:
//#line 109 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 84:
//#line 133 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 85:
//#line 134 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 86:
//#line 137 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 87:
//#line 138 "gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 88:
//#line 139 "gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 98:
//#line 158 "gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 99:
//#line 160 "gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 889 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
