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






//#line 2 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
	package componentes;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
	import java.util.Scanner;
	import javax.swing.JFileChooser;
    import java.util.Stack;
    import componentes.SimboloPolaca;
	
//#line 28 "Parser.java"




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
    4,    4,    6,    7,    7,    7,    7,    7,    7,    7,
    8,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   11,   12,    9,    9,    2,
    2,    3,    3,    3,    3,    3,    3,   16,   16,   16,
   16,   18,   18,   19,   19,   13,   17,   17,   17,   17,
   17,   17,   17,   17,   17,   17,   17,   17,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   14,   20,   20,   20,
   20,   21,   21,   21,   21,   24,   24,   23,   23,   23,
   15,   15,   15,    5,    5,    5,   25,   25,   25,   26,
   26,   26,   22,   22,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    2,    1,    9,    9,    8,    9,    9,   11,   11,
    1,    9,   13,    9,    8,    9,    9,   11,   11,   13,
   12,   13,   13,   15,   15,    1,    1,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    2,    5,    6,    6,
    3,    1,    3,    1,    1,    3,    5,    4,    4,    4,
    5,    6,    5,    5,    5,    7,    7,    3,   12,   11,
   11,   11,   11,   13,   13,   11,   11,   11,   12,   12,
   12,   12,   12,   12,   12,   14,   14,    1,    3,    5,
    7,    1,    3,    5,    7,    2,    3,    3,    3,    3,
    4,    4,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   54,   55,   21,    0,    0,    0,    1,
    0,    0,   44,    0,   43,   40,   41,   42,   45,   46,
    0,   47,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    4,    5,    0,    0,    0,    0,  110,  113,
  112,    0,    0,  111,    0,  109,   13,    0,    0,    0,
    0,    0,   51,    0,    0,    0,    0,    0,    0,    0,
    0,   68,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   56,  114,  103,    0,    0,
    0,    0,   12,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   60,
    0,    0,    0,   59,    0,    0,   58,    0,    0,  102,
  101,    0,    0,    0,    0,    0,   53,    0,    0,  107,
  108,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   36,    0,    0,    0,    0,    0,   48,    0,    0,    0,
   96,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  100,   99,   98,   65,    0,   64,   61,
    0,    0,   63,    0,   57,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   39,    0,    0,    0,    0,   49,
   50,   97,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   62,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,   67,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   25,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   16,
    0,    0,    0,    0,    0,   26,    0,   24,    0,   37,
    0,    0,    0,   27,   22,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   91,   17,   15,    0,
   18,   14,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   95,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   28,    0,   29,    0,
    0,   71,    0,   73,    0,   72,    0,    0,    0,   76,
   77,   78,   70,    0,    0,    0,    0,    0,    0,    0,
   19,   20,    0,    0,   31,    0,    0,    0,    0,   80,
   82,   79,    0,    0,    0,   81,   83,   84,   85,   69,
    0,   32,   30,    0,    0,   33,   23,   74,    0,   75,
    0,    0,    0,   86,   87,   34,   35,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   47,   48,   49,   13,   14,  141,   15,
  142,  301,   16,   17,   18,   19,   20,   38,   21,   63,
  100,   44,   64,  101,   45,   46,
};
final static short yysindex[] = {                       -89,
  142,  -26,  -24,    0,    0,    0, -155,  -35,    0,    0,
  -89,  -89,    0,   14,    0,    0,    0,    0,    0,    0,
 -240,    0,    6,    6,  -37,  -16,  -34,   20,   27,   51,
   31,  -20,    0,    0,    6,  -36,   33,   43,    0,    0,
    0, -190,   75,    0,    5,    0,    0,   19,   73,   -4,
 -134,   91,    0,  116, -104,   34,   44,   25,  124,  128,
  132,    0,  156,  162,   41,  -45,   37,  -31,  154,   78,
  174,   12,  -39,  190, -240,    0,    0,    0,    6,    6,
    6,    6,    0,    6,    6,    6,    6,    6,    6,  125,
  163,  -62,    6,    4,   49,  224, -124,    8,   60,  244,
  283,   63,  288,   -5,   77,   50,  -29,   65, -139,    0,
  275, -117,  280,    0,   66,   87,    0,  282,   57,    0,
    0,   82,  222,   83,    6,  -30,    0,    5,    5,    0,
    0,  167,  167,  167,  167,  167,  167,  -62,  -62,  -62,
    0,  226,  311,   97,  -62,  295,    0,  296,   86,  297,
    0,   88, -115,  298,   92,  299,   93,   52,  302,  -42,
  108,   98,   62,    0,    0,    0,    0,  322,    0,    0,
  328,  312,    0,  114,    0,  249,  -62,  251,  334,  254,
  257,  126,  256,  258,    0, -121,  261,  262,  265,    0,
    0,    0,  -17,  330,  348,  -17,  332,  -17,  333,  127,
  354,  -17,  -17,  -38,  129,  336,  338,  -32,  145, -117,
  341,    0,  343,  -62,  278,  -62,  137,  -62,  -62,  138,
  -93,  -85,  347,  284,  -62,  -62,  -76,  285,  -17, -115,
  286,  -17,  287,  -17,  350,  143,  290,  291,  292,  293,
  356,  -17,  -17,  -17,  -27,  149,  376,    0,    0,  303,
  363,  304,  300,  305,  306,  301,  368,  309,  374,  313,
    0,  -62,  310,  314,  375,  378,  315,  -89,  317,  397,
  -89,  319,  -89,  320,  -17,  383,  -89,  -89,  -89,  -89,
  -17,  323,  324,  325,  326,  327,  384,  195,  393,    0,
  394,  -62,  395,  396,  -62,    0,  -62,    0,  -62,    0,
  331,  -70,  -63,    0,    0,  -62,  335,  -89,  201,  337,
  -89,  339,  -89,  340,  -17,  342,  344,  346,  349,  345,
  -89,  -89,  -89,  -89,  -89,  -17,    0,    0,    0,  351,
    0,    0,  352,  353,  355,  199,  402,  358,  406,  359,
  360,  407,  361,    0,  413,  362,  414,  364,  -89,  365,
  416,  420,  424,  425,  -89,  366,  367,  369,  370,  371,
  377,  431,  434,  238,  240,  442,    0,  -62,    0,  -62,
 -105,    0,  443,    0,  444,    0,  445,  380,  -89,    0,
    0,    0,    0,  381,  448,  449,  450,  451,  452,  -89,
    0,    0,  453,  454,    0,  389,  390,  457,  458,    0,
    0,    0,  459,  398,  460,    0,    0,    0,    0,    0,
  399,    0,    0,  263,  266,    0,    0,    0,  461,    0,
  462,  467,  468,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   21,   23,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  469,    0,    0,    0,
    0,    0,    0,    0,  -28,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   32,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   39,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -21,   -1,    0,
    0,  488,  489,  490,  491,  492,  493,    0,    0,  410,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   47,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   64,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   72,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -11,    0,  -90,    0,   74,   59,    0,    0, -107,    0,
  -82, -223,    0,    0,    0,    0,    0,  463,  268,   15,
   11,  144,  -77, -126,   85,  141,
};
final static int YYTABLESIZE=538;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         33,
   34,  140,   51,   73,   31,   55,   42,   42,   42,  119,
  182,  163,  106,   25,  106,   27,  106,   42,  204,  104,
    2,  104,    3,  104,   42,   32,  195,   42,  245,   37,
  106,  106,  185,  106,  168,  157,   91,  104,  104,  105,
  104,  105,   53,  105,  144,   68,   81,  140,  140,  140,
   42,   82,  124,   36,  140,  183,  184,  105,  105,   56,
  105,   79,  189,   80,  105,  106,   58,  103,  107,  215,
   66,   67,   88,  334,   98,  335,   75,  116,   89,   92,
   88,  113,  341,   52,  102,   77,  140,   89,   93,  146,
  161,   61,  200,   71,   74,  117,   43,  174,  108,  114,
   28,   76,  209,  270,   90,   70,  250,  147,  252,   62,
  254,  255,   94,   90,   29,  175,  165,   79,   92,   80,
   79,   93,   80,  140,  170,  140,  145,  140,  140,  171,
  166,   94,  247,   78,  140,  140,  121,  223,   59,    4,
    5,  224,  263,  264,  396,    2,  397,    3,    4,    5,
  398,  143,   60,  399,  300,   97,   95,  132,  133,  134,
  135,  136,  137,  128,  129,  257,    1,    2,  201,  258,
    3,  140,   96,  259,    4,    5,    6,  260,    7,  265,
    8,  108,  266,  179,  330,  109,  267,  333,  337,  300,
  110,  300,  338,    1,    2,  339,  111,    3,  300,  340,
   22,  140,   23,    6,  140,  112,  140,    8,  140,   79,
  115,   80,  120,  203,  122,  140,  125,  239,   50,   72,
   30,  130,  131,  244,  118,  180,  162,  106,  285,   24,
  126,   26,   39,   39,  104,   69,  181,   40,   40,   40,
   41,   41,   54,  106,  106,  106,  106,  138,   40,   39,
  104,  104,  104,  104,  105,   40,  307,   41,   40,  310,
  300,  312,  300,  156,  148,  316,  317,  318,  319,   35,
  105,  105,  105,  105,   83,   39,  150,  140,  123,  140,
  104,   40,   57,   41,  152,  139,   65,   88,    4,    5,
   84,   85,   86,   87,   92,   97,  343,    4,    5,  346,
   60,  348,   89,   93,   97,  159,   59,    4,    5,  356,
  357,  358,  359,  360,   97,    4,    5,  207,  160,   90,
   60,   59,   97,   99,   99,   99,  153,   94,  155,  151,
  208,  154,  158,  167,  164,   60,  228,  378,  169,  231,
  173,  233,  172,  384,  177,  237,  238,  240,  176,  178,
  186,  187,  188,  190,  191,  192,  194,  193,  196,  198,
  197,  199,  202,  205,  149,  210,  206,  404,  211,  213,
  212,  214,  269,  216,  217,  272,  218,  274,  411,  219,
  221,  220,  222,  225,  226,  282,  283,  284,  286,  227,
  229,  230,  232,  234,  236,  235,  242,  241,  243,  248,
  246,  249,  251,  253,  256,  261,  262,  268,  271,  273,
  275,  276,  277,  278,  279,  280,  281,  287,  314,  288,
   99,  290,  292,  295,  320,   99,  296,  289,  291,  293,
  294,  297,  298,  304,  302,  299,  305,  306,  303,  308,
  309,  311,  313,  315,  326,  321,  322,  323,  324,  325,
  327,  328,  329,  331,  332,  336,  344,  366,  350,  342,
  367,  345,  349,  347,  369,  372,  351,  355,  352,  361,
  353,  374,  376,  354,  380,  362,  363,  364,  381,  365,
  368,  370,  382,  383,  371,  373,  375,  379,  377,  391,
  385,  386,  392,  387,  388,  389,  393,   99,  394,  390,
  395,  400,  401,  402,  403,  405,  406,  407,  408,  409,
  410,  412,  413,  414,  415,  416,  417,  418,  420,  424,
  425,  422,  419,  421,  423,  426,  427,   52,    9,    6,
    7,    8,   10,   11,   38,    0,    0,  127,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         11,
   12,   92,   40,   40,   40,   40,   45,   45,   45,   41,
   41,   41,   41,   40,   43,   40,   45,   45,   61,   41,
    0,   43,    0,   45,   45,   61,  153,   45,   61,  270,
   59,   60,  140,   62,  112,   41,   41,   59,   60,   41,
   62,   43,   59,   45,   41,   31,   42,  138,  139,  140,
   45,   47,   41,   40,  145,  138,  139,   59,   60,   40,
   62,   43,  145,   45,   40,   41,   40,   57,   58,  177,
   40,   41,   41,  297,   41,  299,   44,   41,   60,   41,
   62,   41,  306,   25,   41,  276,  177,   41,   41,   41,
   41,   41,   41,   35,   36,   59,   23,   41,   58,   59,
  256,   59,   41,  230,   41,   32,  214,   59,  216,   59,
  218,  219,   41,   41,  270,   59,  256,   43,  123,   45,
   43,  256,   45,  214,   59,  216,  123,  218,  219,  115,
  270,   41,  210,   59,  225,  226,   59,  259,  256,  264,
  265,  263,  225,  226,  368,  125,  370,  125,  264,  265,
  256,   93,  270,  259,  262,  271,   41,   84,   85,   86,
   87,   88,   89,   79,   80,  259,  256,  257,  158,  263,
  260,  262,  277,  259,  264,  265,  266,  263,  268,  256,
  270,   58,  259,  125,  292,   58,  263,  295,  259,  297,
   59,  299,  263,  256,  257,  259,   41,  260,  306,  263,
   59,  292,   61,  266,  295,   44,  297,  270,  299,   43,
  256,   45,   59,  256,   41,  306,  256,  256,  256,  256,
  256,   81,   82,  256,  256,  256,  256,  256,  256,  256,
   41,  256,  270,  270,  256,  256,  267,  276,  276,  276,
  278,  278,  277,  272,  273,  274,  275,  123,  276,  270,
  272,  273,  274,  275,  256,  276,  268,  278,  276,  271,
  368,  273,  370,  269,   41,  277,  278,  279,  280,  256,
  272,  273,  274,  275,  256,  270,  269,  368,  267,  370,
  256,  276,  256,  278,   41,  123,  256,  256,  264,  265,
  272,  273,  274,  275,  256,  271,  308,  264,  265,  311,
  270,  313,  256,  256,  271,  256,  256,  264,  265,  321,
  322,  323,  324,  325,  271,  264,  265,  256,  269,  256,
  270,  256,  271,   56,   57,   58,   44,  256,   41,  270,
  269,  269,  256,   59,  270,  270,  193,  349,   59,  196,
   59,  198,  256,  355,  123,  202,  203,  204,  267,  267,
  125,   41,  256,   59,   59,  270,  269,   61,   61,   61,
  269,  269,   61,  256,   97,   44,  269,  379,   41,  256,
   59,  123,  229,  123,   41,  232,  123,  234,  390,  123,
  125,  256,  125,  123,  123,  242,  243,  244,  245,  125,
   61,   44,   61,   61,   41,  269,   61,  269,   61,   59,
  256,   59,  125,  267,  267,   59,  123,  123,  123,  123,
   61,  269,  123,  123,  123,  123,   61,  269,  275,   44,
  153,   59,  123,  123,  281,  158,   59,  125,  125,  125,
  125,  123,   59,   59,  125,  123,   59,  123,  125,  123,
   44,  123,  123,   61,   61,  123,  123,  123,  123,  123,
  256,   59,   59,   59,   59,  125,  256,  259,  315,  125,
   59,  125,  123,  125,   59,   59,  125,  123,  125,  326,
  125,   59,   59,  125,   59,  125,  125,  125,   59,  125,
  123,  123,   59,   59,  125,  125,  125,  123,  125,   59,
  125,  125,   59,  125,  125,  125,  259,  230,  259,  123,
   59,   59,   59,   59,  125,  125,   59,   59,   59,   59,
   59,   59,   59,  125,  125,   59,   59,   59,   59,   59,
   59,  259,  125,  125,  259,   59,   59,   59,   41,   41,
   41,   41,   41,   41,  125,   -1,   -1,   75,
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
"condicion : expresion error",
"condicion_accion : condicion",
"clausula_while : incio_while '(' condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while '(' error ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while '(' error LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while error condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while '(' condicion_accion ')' error '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while '(' '(' error condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : incio_while '(' condicion_accion ')' ')' error LOOP '{' bloque_sentencias_control '}' ';'",
"incio_while : WHILE",
"clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF error condicion_accion ')' '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' error ';'",
"clausula_seleccion : IF '(' '(' error condicion_accion ')' '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion_accion ')' ')' error '{' bloque_then '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"clausula_seleccion : IF error condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' error ';'",
"clausula_seleccion : IF '(' '(' error condicion_accion ')' '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion_accion ')' ')' error '{' bloque_then '}' ELSE '{' bloque_else '}' END_IF ';'",
"bloque_then : bloque_sentencias_control",
"bloque_else : bloque_sentencias_control",
"bloque_sentencias_control : sentencias_ejecutables",
"bloque_sentencias_control : sentencias_ejecutables bloque_sentencias_control",
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
"invocacion_procedimiento : ID '(' '(' error ';'",
"invocacion_procedimiento : ID '(' ')' ')' error ';'",
"invocacion_procedimiento : ID '(' lista_parametros_invocacion error ';'",
"invocacion_procedimiento : ID '(' error ')' ';'",
"invocacion_procedimiento : ID error lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID '(' '(' error lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID '(' lista_parametros_invocacion ')' ')' error ';'",
"invocacion_procedimiento : ID error ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC error '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' error '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI error cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI '=' error '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC error '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' error '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI error cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' error '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' '(' error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"lista_parametros_invocacion : parametro_invocacion",
"lista_parametros_invocacion : parametro_invocacion ',' parametro_invocacion",
"lista_parametros_invocacion : parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion",
"lista_parametros_invocacion : parametro_invocacion ',' parametro_invocacion ',' parametro_invocacion ',' error",
"lista_parametros_declaracion : parametro_declaracion",
"lista_parametros_declaracion : parametro_declaracion ',' parametro_declaracion",
"lista_parametros_declaracion : parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion",
"lista_parametros_declaracion : parametro_declaracion ',' parametro_declaracion ',' parametro_declaracion ',' error",
"parametro_declaracion : tipo ID",
"parametro_declaracion : REF tipo ID",
"parametro_invocacion : ID ':' ID",
"parametro_invocacion : ID ':' error",
"parametro_invocacion : error ':' ID",
"asignacion : ID '=' expresion ';'",
"asignacion : ID '=' error ';'",
"asignacion : error '=' expresion ';'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
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

//#line 209 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"


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
//#line 724 "Parser.java"
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
//#line 20 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");}
break;
case 12:
//#line 35 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion");}
break;
case 13:
//#line 38 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{apilarPasoIncompleto(SimboloPolaca.BF);}
break;
case 14:
//#line 47 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
break;
case 15:
//#line 48 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE.");}
break;
case 16:
//#line 49 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
break;
case 17:
//#line 50 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
break;
case 18:
//#line 51 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");}
break;
case 19:
//#line 52 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");}
break;
case 20:
//#line 53 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");}
break;
case 21:
//#line 56 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{apilarPasoActual();}
break;
case 22:
//#line 59 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
break;
case 23:
//#line 60 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
break;
case 24:
//#line 61 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del IF.");}
break;
case 25:
//#line 62 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el )");}
break;
case 26:
//#line 63 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el (");}
break;
case 27:
//#line 64 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
break;
case 28:
//#line 65 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");}
break;
case 29:
//#line 66 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");}
break;
case 30:
//#line 67 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la condicion del IF ELSE.");}
break;
case 31:
//#line 68 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
break;
case 32:
//#line 69 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
break;
case 33:
//#line 70 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
break;
case 34:
//#line 71 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");}
break;
case 35:
//#line 72 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");}
break;
case 36:
//#line 75 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{completarPasoIncompleto();apilarPasoIncompleto(SimboloPolaca.BI);}
break;
case 37:
//#line 85 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{}
break;
case 43:
//#line 97 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{completarPasoIncompleto();}
break;
case 44:
//#line 102 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{completarPasoIncompleto();generarBIinicio();}
break;
case 47:
//#line 105 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Syntax error");}
break;
case 48:
//#line 108 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");}
break;
case 49:
//#line 109 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");}
break;
case 50:
//#line 110 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}
break;
case 51:
//#line 111 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla");}
break;
case 56:
//#line 122 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");}
break;
case 57:
//#line 125 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");}
break;
case 58:
//#line 126 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");}
break;
case 59:
//#line 127 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 60:
//#line 128 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 61:
//#line 129 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 62:
//#line 130 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 63:
//#line 131 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 64:
//#line 132 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
break;
case 65:
//#line 133 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 66:
//#line 134 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 67:
//#line 135 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 68:
//#line 136 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento.");}
break;
case 69:
//#line 139 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
break;
case 70:
//#line 140 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
break;
case 71:
//#line 141 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 72:
//#line 142 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 73:
//#line 143 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 74:
//#line 144 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 75:
//#line 145 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 76:
//#line 146 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 77:
//#line 147 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 78:
//#line 148 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 79:
//#line 149 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 80:
//#line 150 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 81:
//#line 151 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 82:
//#line 152 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 83:
//#line 153 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 84:
//#line 154 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 85:
//#line 155 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 86:
//#line 156 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 87:
//#line 157 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 91:
//#line 164 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 95:
//#line 171 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 99:
//#line 179 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 100:
//#line 180 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 101:
//#line 183 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 102:
//#line 184 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 103:
//#line 185 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 104:
//#line 188 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(2).sval);addSimbolo(val_peek(0).sval);addSimbolo(val_peek(1).sval);}
break;
case 105:
//#line 189 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(2).sval);addSimbolo(val_peek(0).sval);addSimbolo(val_peek(1).sval);}
break;
case 107:
//#line 193 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(2).sval);addSimbolo(val_peek(0).sval);addSimbolo(val_peek(1).sval);}
break;
case 108:
//#line 194 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(2).sval);addSimbolo(val_peek(0).sval);addSimbolo(val_peek(1).sval);}
break;
case 113:
//#line 203 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 114:
//#line 205 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 1191 "Parser.java"
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
