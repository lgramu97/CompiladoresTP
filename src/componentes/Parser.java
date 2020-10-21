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
    import java.util.HashMap;
	import java.util.Scanner;
	import javax.swing.JFileChooser;
    import java.util.Stack;
	
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
   14,   14,   14,   14,   14,   21,   21,   20,   20,   20,
   20,   22,   22,   22,   22,   25,   25,   24,   24,   24,
   15,   15,   15,    5,    5,    5,   26,   26,   26,   27,
   27,   27,   23,   23,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    2,    1,    9,    9,    8,    9,    9,   11,   11,
    1,    9,   13,    9,    8,    9,    9,   11,   11,   13,
   12,   13,   13,   15,   15,    1,    1,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    2,    5,    6,    6,
    3,    1,    3,    1,    1,    3,    5,    4,    4,    4,
    5,    6,    5,    5,    5,    7,    7,    3,   11,   10,
   10,   10,   12,   12,   10,   10,   10,   11,   11,   11,
   11,   11,   11,   13,   13,    2,    3,    1,    3,    5,
    7,    1,    3,    5,    7,    2,    3,    3,    3,    3,
    4,    4,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   54,   55,   21,    0,    0,    0,    1,
    0,    0,   44,    0,   43,   40,   41,   42,   45,   46,
    0,    0,   47,    0,    0,    0,    0,    0,    0,   86,
    0,    0,    0,    4,    5,    0,    0,    0,    0,    0,
    0,  110,  113,  112,    0,    0,  111,    0,  109,   13,
    0,    0,    0,    0,    0,   51,    0,    0,   87,    0,
    0,    0,   68,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   56,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  114,  103,    0,    0,
    0,    0,   12,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   60,    0,
    0,    0,   59,    0,    0,   58,    0,    0,  102,  101,
    0,    0,    0,    0,    0,   53,    0,    0,   96,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  107,  108,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   36,    0,    0,    0,    0,    0,   48,    0,
  100,   99,   98,   65,    0,   64,   61,    0,    0,   63,
    0,   57,    0,    0,    0,    0,    0,    0,    0,   97,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   39,    0,    0,
    0,    0,   49,   50,    0,    0,   62,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   66,   67,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,    0,    0,    0,    0,    0,    0,    0,    0,
   16,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   26,    0,   24,    0,   37,    0,
    0,    0,   27,   22,    0,   91,   17,   15,    0,   18,
   14,    0,    0,    0,   95,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   72,    0,   71,    0,    0,    0,   75,   76,   77,   70,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   28,    0,   29,    0,    0,   19,   20,   80,   78,    0,
    0,    0,   79,   81,   82,   83,   69,    0,    0,    0,
   31,    0,    0,    0,    0,   73,    0,   74,    0,   32,
   30,    0,    0,   33,   23,   84,   85,    0,    0,   34,
   35,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   50,   51,   52,   13,   14,  153,   15,
  154,  310,   16,   17,   18,   19,   20,   39,   21,   64,
   22,   81,   47,   65,   82,   48,   49,
};
final static short yysindex[] = {                       -76,
  -17,  -34,  -31,    0,    0,    0, -185,  -36,    0,    0,
  -76,  -76,    0,  -29,    0,    0,    0,    0,    0,    0,
 -237,  -28,    0,  -13,  -13,  -35,   32,  -37,   24,    0,
   49,    5,    3,    0,    0,  -13,  -26,   63,   91,   39,
  -33,    0,    0,    0, -145,   33,    0,   76,    0,    0,
   55,  117,   17,  -79,  134,    0,  138,  -95,    0,  127,
  133,  128,    0,  152,  156,   62,  -55,  -10,  -23,  143,
   90,  162,   -7,  -52,  166, -237,    0, -123,  -57,  -56,
  175,  173,  -24,  -30,   13,  -20,    0,    0,  -13,  -13,
  -13,  -13,    0,  -13,  -13,  -13,  -13,  -13,  -13,   96,
  111,  -61,  -13,   21,   36,  188,  -12, -140,    0,  208,
 -134,  209,    0,  -46,   14,    0,  226,   47,    0,    0,
   31,  195,   57,  -13,   20,    0,   50,  261,    0,   64,
 -137,  264,   66,   42,  265,  -45,   80,   68,   52,   76,
   76,    0,    0,  129,  129,  129,  129,  129,  129,  -61,
  -61,  -61,    0,  207,  300,   86,  -61,  284,    0,  285,
    0,    0,    0,    0,  302,    0,    0,  306,  289,    0,
   93,    0,  228,  -61,  230,  316,  235,  236,  104,    0,
  -25,  301,  317,  -25,  303,   94,  324,  -25,  -25,    8,
   97,  307,  308,  -38,  115,  247,  248,    0, -184,  251,
  252,  257,    0,    0, -134,  318,    0,  325,  -61,  258,
  -61,  109,  -61,  -61,  118,  263,  -25, -137,  266,  -25,
  326,  119,  267,  268,  269,  270,  333,  -25,  -25,  -25,
   10,  126, -120, -104,  337,  274,  -61,  -61, -111,  354,
    0,    0,  275,  340,  276,  279,  278,  281,  286,  -76,
  287,  360,  -76,  290,  -25,  346,  -76,  -76,  -76,  -76,
  -25,  292,  293,  294,  295,  296,  347,  353,  297,  362,
  299,    0,  -61,  298,  304,  366,  367,  305,  174,  372,
    0,  373,  -61,  374,  375,  -61,  310,  -76,  180,  312,
  -76,  315,  -25,  314,  319,  320,  321,  327,  -76,  -76,
  -76,  -76,  -76,  -25,    0,  -61,    0,  -61,    0,  323,
 -103, -102,    0,    0,  -61,    0,    0,    0,  328,    0,
    0,  330,  381,  332,    0,  382,  334,  -76,  329,  383,
  392,  399,  401,  -76,  336,  338,  339,  341,  342,  345,
  344,  348,  203,  406,  349,  411,  351,  350,  412,  417,
    0,  418,    0,  419,  355,  -76,    0,    0,    0,    0,
  356,  420,  423,  424,  425,  426,  -76,  227,  229,  428,
    0,  -61,    0,  -61,  -94,    0,    0,    0,    0,  430,
  365,  432,    0,    0,    0,    0,    0,  368,  433,  435,
    0,  370,  371,  438,  439,    0,  440,    0,  441,    0,
    0,  242,  243,    0,    0,    0,    0,  444,  445,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   28,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  446,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -19,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    6,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   15,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   22,
   27,    0,    0,  465,  466,  467,  468,  469,  470,    0,
    0,  387,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   53,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   60,    0,    0,    0,    0,    0,    0,    0,    0,
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
    0,
};
final static short yygindex[] = {                         0,
  -11,    0, -100,    0,   72,    1,    0,    0,  141,    0,
  -91, -196,    0,    0,    0,    0,    0,  442,   -5,   37,
    0,   -2,  150,  -81, -116,   74,  106,
};
final static int YYTABLESIZE=518;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         34,
   35,  152,   58,   32,   54,   26,   84,   85,   28,   45,
   37,   41,  167,   74,  183,  190,  133,  118,   45,   45,
  139,  106,  231,  106,   33,  106,   55,    2,    3,  165,
  115,   45,   38,  123,   80,   80,   72,   75,   86,  106,
  106,   23,  106,   24,   67,   68,   88,   45,  116,  152,
  152,  152,   45,  137,   45,   92,  152,  101,  196,  197,
  179,  156,  104,   59,  104,  202,  104,  105,   69,  105,
   29,  105,  127,  152,  235,   89,  158,   90,  236,   79,
  104,  104,  186,  104,   30,  105,  105,  171,  105,   62,
   56,   88,  195,   89,  159,   46,   93,   89,   90,   90,
   94,  252,  112,  155,   71,  172,   76,   63,  152,  341,
  152,  342,  152,  152,   99,  162,   98,   91,  348,  107,
  113,   60,   92,  240,  176,   80,    4,    5,   80,  163,
   87,  187,   89,   78,   90,   61,  152,  152,  268,  102,
    4,    5,  269,  157,  276,  274,  275,  277,  120,   77,
  168,  278,    2,    3,  270,  344,  346,  100,  271,  345,
  347,  394,  140,  141,  395,  144,  145,  146,  147,  148,
  149,   89,  152,   90,  104,  392,  103,  393,  105,    1,
    2,  106,  152,    3,  107,  152,  109,    4,    5,    6,
  108,    7,  110,    8,    1,    2,  142,  143,    3,  111,
  114,  119,  121,  124,    6,  152,  125,  152,    8,   60,
  189,  128,   80,  129,  152,  130,  131,  230,  150,   31,
   53,   25,   83,   61,   27,  134,   36,   40,  160,   73,
    4,    5,  117,  151,   42,  138,  106,   78,  287,   57,
   43,  290,   44,   42,  132,  294,  295,  296,  297,   43,
   43,   44,  106,  106,  106,  106,   42,  161,   70,  122,
   66,   88,   43,  225,   44,  265,  164,  166,  135,  169,
   92,  152,   42,  152,   61,  177,  324,  104,   43,  327,
   44,  136,  105,   43,  170,   43,  178,  335,  336,  337,
  338,  339,  198,  104,  104,  104,  104,  173,  105,  105,
  105,  105,    4,    5,   60,    4,    5,  193,   89,   78,
   93,   93,   78,   90,  210,   94,  355,  174,   61,  180,
  194,  181,  361,  175,  184,  188,   94,   95,   96,   97,
  216,  199,  182,  219,  185,  191,  192,  223,  224,  226,
  200,  201,  203,  204,  381,  205,  206,  207,  208,  243,
  209,  245,  211,  247,  248,  388,  212,  213,  214,  215,
  218,  217,  221,  220,  222,  227,  251,  228,  229,  254,
  232,  233,  234,  237,  238,  246,  241,  262,  263,  264,
  266,  239,  244,  242,  249,  250,  255,  256,  253,  257,
  258,  259,  260,  261,  267,  272,  273,  279,  281,  280,
  282,  283,  284,  289,  292,  285,  293,  304,  286,  288,
  298,  305,  291,  309,  299,  300,  301,  302,  303,  306,
  307,  308,  311,  319,  313,  314,  322,  315,  312,  316,
  317,  318,  320,  321,  323,  325,  326,  328,  330,  351,
  353,  357,  329,  331,  332,  333,  309,  343,  309,  334,
  358,  356,  349,  340,  350,  309,  352,  359,  354,  360,
  362,  370,  363,  364,  371,  365,  366,  367,  368,  373,
  376,  372,  369,  374,  375,  377,  378,  379,  383,  380,
  382,  384,  385,  386,  387,  389,  391,  390,  396,  397,
  398,  400,  399,  401,  402,  403,  404,  405,  406,  407,
  408,  409,  410,  411,   52,    9,    6,    7,    8,   10,
   11,   38,  309,    0,  309,    0,    0,  126,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         11,
   12,  102,   40,   40,   40,   40,   40,   41,   40,   45,
   40,   40,   59,   40,  131,   61,   41,   41,   45,   45,
   41,   41,   61,   43,   61,   45,   26,    0,    0,  111,
   41,   45,  270,   41,   40,   41,   36,   37,   41,   59,
   60,   59,   62,   61,   40,   41,   41,   45,   59,  150,
  151,  152,   45,   41,   45,   41,  157,   41,  150,  151,
   41,   41,   41,   40,   43,  157,   45,   41,   32,   43,
  256,   45,   78,  174,  259,   43,   41,   45,  263,   41,
   59,   60,   41,   62,  270,   59,   60,   41,   62,   41,
   59,   59,   41,   41,   59,   24,   41,   43,   41,   45,
   41,  218,   41,  103,   33,   59,   44,   59,  209,  306,
  211,  308,  213,  214,   60,  256,   62,   42,  315,   58,
   59,  256,   47,  205,  124,  131,  264,  265,  134,  270,
  276,  134,   43,  271,   45,  270,  237,  238,  259,  123,
  264,  265,  263,  123,  256,  237,  238,  259,   59,   59,
  114,  263,  125,  125,  259,  259,  259,   41,  263,  263,
  263,  256,   89,   90,  259,   94,   95,   96,   97,   98,
   99,   43,  273,   45,   41,  372,  256,  374,   41,  256,
  257,  277,  283,  260,   58,  286,   59,  264,  265,  266,
   58,  268,   41,  270,  256,  257,   91,   92,  260,   44,
  256,   59,   41,  256,  266,  306,   41,  308,  270,  256,
  256,  269,  218,  270,  315,   41,   44,  256,  123,  256,
  256,  256,  256,  270,  256,  256,  256,  256,   41,  256,
  264,  265,  256,  123,  270,  256,  256,  271,  250,  277,
  276,  253,  278,  270,  269,  257,  258,  259,  260,  276,
  276,  278,  272,  273,  274,  275,  270,  270,  256,  267,
  256,  256,  276,  256,  278,  256,   59,   59,  256,  256,
  256,  372,  270,  374,  270,  256,  288,  256,  276,  291,
  278,  269,  256,  276,   59,  276,  267,  299,  300,  301,
  302,  303,  152,  272,  273,  274,  275,  267,  272,  273,
  274,  275,  264,  265,  256,  264,  265,  256,  256,  271,
  256,  256,  271,  256,  174,  256,  328,  123,  270,  270,
  269,   61,  334,  267,   61,   61,  272,  273,  274,  275,
  181,  125,  269,  184,  269,  256,  269,  188,  189,  190,
   41,  256,   59,   59,  356,   44,   41,   59,  256,  209,
  123,  211,  123,  213,  214,  367,   41,  123,  123,  256,
   44,   61,  269,   61,   41,  269,  217,   61,   61,  220,
  256,  125,  125,  123,  123,  267,   59,  228,  229,  230,
  231,  125,  125,   59,  267,  123,   61,  269,  123,  123,
  123,  123,  123,   61,  269,   59,  123,   44,   59,  125,
  125,  123,  125,   44,  255,  125,   61,   61,  123,  123,
  261,   59,  123,  273,  123,  123,  123,  123,  123,  123,
   59,  123,  125,  283,   59,   59,  286,  123,  125,  256,
   59,   59,   59,   59,  125,  256,  125,  123,  125,   59,
   59,   59,  293,  125,  125,  125,  306,  125,  308,  123,
   59,  123,  125,  304,  125,  315,  125,   59,  125,   59,
  125,  259,  125,  125,   59,  125,  125,  123,  125,   59,
   59,  123,  125,  123,  125,   59,   59,   59,   59,  125,
  125,   59,   59,   59,   59,  259,   59,  259,   59,  125,
   59,   59,  125,   59,  125,  125,   59,   59,   59,   59,
  259,  259,   59,   59,   59,   41,   41,   41,   41,   41,
   41,  125,  372,   -1,  374,   -1,   -1,   76,
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
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' ')' error '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' ')' NI error cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' ')' NI '=' error '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' error ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion error NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' error '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' NI error cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' NI '=' error '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' '(' error lista_parametros_declaracion ')' NI '=' cte '{' conjunto_sentencias '}' ';'",
"sentencia_declaracion_procedimiento : inicio_proc '(' lista_parametros_declaracion ')' ')' error NI '=' cte '{' conjunto_sentencias '}' ';'",
"inicio_proc : PROC ID",
"inicio_proc : PROC error '('",
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

//#line 226 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"

ArrayList<SimboloPolaca> listaReglas = new ArrayList<>();
Stack<Integer> pasosIncompletos = new Stack<>();
AnalizadorLexico analizadorLexico = new AnalizadorLexico();
ArrayList<String> erroresSintacticos = new ArrayList<>();
ArrayList<String> erroresSemanticos = new ArrayList<>();
ArrayList<String> erroresParser = new ArrayList<>();
ArrayList<String> tokens = new ArrayList<>();
ArrayList<String> estructuras = new ArrayList<>();
ArrayList<String> lista_variables = new ArrayList<>();
ArrayList<String> ambito = new ArrayList<String>() {
    {
        add("@main");
    }
};

public void checkIDNoDeclarado(String variable) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    if (!ts.containsKey(lexema)) {
        erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Variable no declarada");
    }
}

public void addAmbito(String ambito_actual){
    ambito.add("@" + ambito_actual);
}

public void deleteAmbito(){
    ambito.remove(ambito.size()-1);
}

public String nameMangling(String simbolo) {
    return simbolo + ambito.toString()
        .replaceAll("\\[|]|, ", "");
}

public void modificarLexema(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    HashMap<String,Object> atributos = ts.get(lexema);
    ts.remove(lexema);
    String newLexema = nameMangling(lexema);
    ts.put(newLexema ,atributos);
    lista_variables.add(newLexema);
}

public void addTipoListaVariables(String tipo, String uso){
  HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    for (String lista_variable : lista_variables) {
        HashMap<String, Object> atributos =  ts.get(lista_variable);
        atributos.put("Tipo", tipo);
        atributos.put("Uso", uso);
    }
  lista_variables.clear();
}

public void addSimbolo(String simbolo) {
	listaReglas.add(new SimboloPolaca(simbolo));
}

public void apilarPasoIncompleto(String nombre) {
	apilarPasoActual();
	addSimbolo(null);
	addSimbolo(nombre);
}

public void completarPasoIncompleto(boolean fin) {
	int posIncompleto = pasosIncompletos.pop();
	SimboloPolaca simbolo = listaReglas.get(posIncompleto);
    int pos = listaReglas.size();
    if (!fin) {
        pos += 2;
    }
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
		if(token == Parser.ID || token == Parser.CTE || token  == Parser.CADENA)
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

/*
public void yyerror(String error){
	erroresParser.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + "  " + error);
}
*/

public StringBuilder copiarErrores(ArrayList<String> errores){
    StringBuilder out = new StringBuilder();
    for (String errore : errores) {
        out.append("\t").append(errore);
        out.append("\n");
    }
    return out;
}

public String getErrores(){
    return "Errores Lexicos: " + 
            "\n" +
            copiarErrores(analizadorLexico.getErrores()) +
            "\n" +
            "Errores Sintacticos: " +
            "\n" +
            copiarErrores(this.erroresSintacticos) + 
            "\n" + 
            "Errores Semanticos: " + 
            "\n" + 
            copiarErrores(this.erroresSemanticos);
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
    for (int i = 0, listaSize = lista.size(); i < listaSize; i++) {
        SimboloPolaca simboloPolaca = lista.get(i);
        System.out.println("VALOR POLACA [" + i + "]: " + simboloPolaca.getSimbolo());
    }
    System.out.println("VALOR POLACA [" + lista.size() +  "]: ...");

}
//#line 773 "Parser.java"
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
case 6:
//#line 29 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("==");}
break;
case 7:
//#line 30 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(">=");}
break;
case 8:
//#line 31 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("<=");}
break;
case 9:
//#line 32 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("!=");}
break;
case 10:
//#line 33 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(">");}
break;
case 11:
//#line 34 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("<");}
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
{completarPasoIncompleto(false);apilarPasoIncompleto(SimboloPolaca.BI);}
break;
case 37:
//#line 85 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{}
break;
case 43:
//#line 97 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{completarPasoIncompleto(true);}
break;
case 44:
//#line 102 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{completarPasoIncompleto(false);generarBIinicio();}
break;
case 47:
//#line 105 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Syntax error");}
break;
case 48:
//#line 108 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");
                                           addSimbolo(val_peek(2).sval);}
break;
case 49:
//#line 110 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");}
break;
case 50:
//#line 111 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}
break;
case 51:
//#line 112 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla");}
break;
case 52:
//#line 115 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{modificarLexema(val_peek(0).sval);}
break;
case 53:
//#line 116 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{modificarLexema(val_peek(2).sval);}
break;
case 54:
//#line 119 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{yyval = new ParserVal("LONGINT");}
break;
case 55:
//#line 120 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{yyval = new ParserVal("FLOAT");}
break;
case 56:
//#line 123 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");
                                                      addTipoListaVariables(val_peek(2).sval,"VARIABLE");}
break;
case 57:
//#line 127 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");checkIDNoDeclarado(val_peek(4).sval);}
break;
case 58:
//#line 128 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");checkIDNoDeclarado(val_peek(3).sval);}
break;
case 59:
//#line 129 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 60:
//#line 130 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 61:
//#line 131 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 62:
//#line 132 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 63:
//#line 133 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 64:
//#line 134 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
break;
case 65:
//#line 135 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 66:
//#line 136 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 67:
//#line 137 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 68:
//#line 138 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al invocar procedimiento.");}
break;
case 69:
//#line 141 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");
                                                                                                                                  deleteAmbito();}
break;
case 70:
//#line 143 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");
                                                                                    deleteAmbito();}
break;
case 71:
//#line 145 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 72:
//#line 146 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 73:
//#line 147 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 74:
//#line 148 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 75:
//#line 149 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 76:
//#line 150 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 77:
//#line 151 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 78:
//#line 152 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 79:
//#line 153 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 80:
//#line 154 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 81:
//#line 155 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 82:
//#line 156 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 83:
//#line 157 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 84:
//#line 158 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 85:
//#line 159 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 86:
//#line 162 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    modificarLexema(val_peek(0).sval);
                    addTipoListaVariables("PROC","PROC");
                    addAmbito(val_peek(0).sval);}
break;
case 87:
//#line 166 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 91:
//#line 172 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 95:
//#line 179 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 96:
//#line 182 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
				addTipoListaVariables(val_peek(1).sval,"PARAMETRO");
                modificarLexema(val_peek(0).sval);}
break;
case 97:
//#line 185 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                       	addTipoListaVariables(val_peek(1).sval,"PARAMETRO");
                        modificarLexema(val_peek(0).sval);}
break;
case 98:
//#line 190 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{checkIDNoDeclarado(val_peek(2).sval);
                                  checkIDNoDeclarado(val_peek(1).sval);}
break;
case 99:
//#line 192 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 100:
//#line 193 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 101:
//#line 196 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");
								  addSimbolo( val_peek(3).sval); 
								  addSimbolo( "=");
                                  checkIDNoDeclarado(val_peek(3).sval);}
break;
case 102:
//#line 200 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 103:
//#line 201 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 104:
//#line 204 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("+");}
break;
case 105:
//#line 205 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("-");}
break;
case 107:
//#line 209 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("*");}
break;
case 108:
//#line 210 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo("/");}
break;
case 110:
//#line 214 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(0).sval);
            checkIDNoDeclarado(val_peek(0).sval);}
break;
case 111:
//#line 216 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{addSimbolo(val_peek(0).sval);}
break;
case 113:
//#line 220 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 114:
//#line 222 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 1316 "Parser.java"
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
