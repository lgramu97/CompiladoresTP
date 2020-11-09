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
    import org.javatuples.Pair;


//#line 30 "Parser.java"




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
   17,   17,   17,   17,   17,   17,   17,   17,   20,   14,
   14,   14,   14,   14,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   14,   14,   22,   22,   21,   21,
   21,   21,   23,   23,   23,   23,   26,   26,   25,   25,
   25,   15,   15,   15,    5,    5,    5,   27,   27,   27,
   28,   28,   28,   24,   24,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    2,    1,    9,    9,    8,    9,    9,   11,   11,
    1,    9,   13,    9,    8,    9,    9,   11,   11,   13,
   12,   13,   13,   15,   15,    1,    1,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    2,    5,    6,    6,
    3,    1,    3,    1,    1,    3,    4,    3,    3,    4,
    5,    4,    4,    6,    6,    4,    5,    3,    2,   11,
   10,   10,   10,   12,   12,   10,   10,   10,   11,   11,
   11,   11,   11,   11,   13,   13,    2,    3,    1,    3,
    5,    7,    1,    3,    5,    7,    2,    3,    3,    3,
    3,    4,    4,    4,    3,    3,    1,    3,    3,    1,
    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   54,   55,   21,    0,    0,    0,    1,
    0,    0,   44,    0,   43,   40,   41,   42,   45,   46,
    0,    0,    0,   47,    0,    0,    0,    0,    0,    0,
   87,    0,   69,    0,    4,    5,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  111,  114,
  113,    0,    0,  112,    0,  110,   13,    0,    0,    0,
    0,    0,   51,    0,    0,   88,    0,    0,   68,    0,
    0,    0,    0,    0,    0,    0,    0,   56,    0,   59,
    0,    0,    0,    0,   58,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  115,  104,    0,
    0,    0,    0,   12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   66,    0,  103,
  102,    0,    0,    0,    0,    0,   53,   63,  101,  100,
   99,   60,    0,    0,   62,    0,   57,    0,    0,    0,
   97,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  108,  109,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   36,    0,    0,    0,    0,    0,
   48,    0,   67,    0,    0,    0,    0,    0,    0,    0,
    0,   61,    0,    0,   98,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   39,    0,    0,    0,    0,   49,   50,    0,
    0,    0,    0,    0,    0,    0,   64,   65,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,    0,    0,    0,
    0,   16,    0,    0,    0,    0,    0,   92,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,    0,   24,    0,
   37,    0,    0,    0,   27,   22,    0,   17,   15,    0,
   18,   14,    0,    0,    0,   96,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   73,    0,   72,    0,    0,    0,   76,   77,   78,
   71,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   28,    0,   29,    0,    0,   19,   20,   81,   79,
    0,    0,    0,   80,   82,   83,   84,   70,    0,    0,
    0,   31,    0,    0,    0,    0,   74,    0,   75,    0,
   32,   30,    0,    0,   33,   23,   85,   86,    0,    0,
   34,   35,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   57,   58,   59,   13,   14,  165,   15,
  166,  312,   16,   17,   18,   19,   20,   40,   21,   22,
   45,   23,   92,   54,   46,   93,   55,   56,
};
final static short yysindex[] = {                       -86,
  129,  -26,  -25,    0,    0,    0, -116,  -27,    0,    0,
  -86,  -86,    0,  -24,    0,    0,    0,    0,    0,    0,
 -239,  -35,   53,    0,  -13,  -13,  -36,  -23,  -30,    9,
    0,   13,    0,  -10,    0,    0,  -13,  -33,   37,   47,
   75,   74, -142,   19,   25,   98,  -38,   39,    0,    0,
    0, -127,   93,    0,   -9,    0,    0,   24,  120,   -4,
  -84,  134,    0,  142,  -75,    0,  135,  148,    0,  164,
  151,   94,  168,   41,  -45,  173, -239,    0,  156,    0,
  -54, -113,  -48,  -39,    0,  159,   76, -112,  -69,  -41,
  -46,  184,  212,    4,   18,   15,   29,    0,    0,  -13,
  -13,  -13,  -13,    0,  -13,  -13,  -13,  -13,  -13,  -13,
  144,  149, -101,  -13,    6,   87,  278,    0,  262,    0,
    0,   60,  199,   62,  -13,   49,    0,    0,    0,    0,
    0,    0,  287,  274,    0,   78,    0,  291,   66,  276,
    0,   69,  -79,  279,   70,   36,  280,  -44,   86,   79,
   46,   -9,   -9,    0,    0,  146,  146,  146,  146,  146,
  146, -101, -101, -101,    0,  218,  303,   89, -101,  288,
    0,  290,    0,  227, -101,  228,  311,  230,  231,   99,
  297,    0,  300, -112,    0,  -15,  302,  322,  -15,  306,
  104,  327,  -15,  -15,  -37,  105,  314,  315,  -43,  121,
  253,  254,    0, -151,  257,  258,  259,    0,    0, -101,
  260, -101,  115, -101, -101,  116,    0,    0,  342,  264,
  -15,  -79,  265,  -15,  328,  122,  267,  269,  270,  272,
  335,  -15,  -15,  -15,  -18,  128, -150, -118,  339,  281,
 -101, -101, -188,  277,  340,  282,  283,  284,  285,  289,
  145,  -86,  292,  359,  -86,  293,  -15,  344,  -86,  -86,
  -86,  -86,  -15,  294,  295,  296,  298,  299,  347,  352,
  301,  354,  304,    0, -101,  305,  308,  355,  361,  312,
  364,    0,  367, -101,  369,  370, -101,    0,  313,  -86,
  175,  316,  -86,  317,  -15,  319,  320,  321,  323,  324,
  -86,  -86,  -86,  -86,  -86,  -15,    0, -101,    0, -101,
    0,  325,  -97,  -82,    0,    0, -101,    0,    0,  326,
    0,    0,  329,  377,  330,    0,  380,  331,  -86,  334,
  383,  390,  393,  394,  -86,  336,  337,  338,  341,  343,
  346,  345,  348,  200,  405,  349,  406,  351,  350,  412,
  417,    0,  418,    0,  419,  356,  -86,    0,    0,    0,
    0,  357,  420,  421,  424,  425,  426,  -86,  229,  232,
  427,    0, -101,    0, -101,  -96,    0,    0,    0,    0,
  428,  365,  430,    0,    0,    0,    0,    0,  368,  433,
  435,    0,  371,  372,  436,  439,    0,  440,    0,  441,
    0,    0,  242,  243,    0,    0,    0,    0,  444,  445,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   22,   26,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  446,    0,
    0,    0,    0,    0,    0,   50,    0,    0,    0,    0,
    0,    0,    0,    0,  -20,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   55,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   56,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    3,   14,    0,    0,  465,  466,  467,  468,  469,
  470,    0,    0,  387,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   57,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
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
    0,    0,
};
final static short yygindex[] = {                         0,
  -11,    0, -111,    0,   16,   73,    0,    0,  150,    0,
  -74, -225,    0,    0,    0,    0,    0,  437,  -28,    0,
   11,    0,  -19,  137,  -64, -115,  100,  101,
};
final static int YYTABLESIZE=525;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         35,
   36,  164,   90,   61,   43,   44,   75,   52,   52,   65,
  132,   52,   33,   27,   29,   38,  195,  235,   91,   91,
  107,    2,  107,  138,  107,    3,   52,  188,   97,   52,
   39,   52,  102,   34,   52,   63,  112,  103,  107,  107,
   53,  107,   70,  105,  145,  105,  168,  105,   66,   72,
  164,  164,  164,   68,  106,  149,  106,  164,  106,   84,
  139,  105,  105,  164,  105,   87,  100,  278,  101,  151,
  279,   69,  106,  106,  280,  106,  191,   85,   95,   96,
   77,  124,  342,  110,  343,  109,  200,  201,  202,  180,
   89,  349,   48,  133,  207,   93,   90,   94,  164,   62,
  164,   91,  164,  164,   95,   78,  254,  239,  270,   73,
   76,  240,  271,   83,   91,   79,  136,   91,  113,  219,
  156,  157,  158,  159,  160,  161,  192,  170,  169,  164,
  164,   82,   81,   80,  137,  100,  100,  101,  101,   30,
  272,   88,  130,   67,  273,  171,    2,  393,   98,  394,
    3,   99,  121,   31,    1,    2,  131,   42,    3,  395,
  111,  345,  396,  164,    6,  346,  276,  277,    8,    1,
    2,  114,  164,    3,  115,  164,  347,    4,    5,    6,
  348,    7,  116,    8,    4,    5,  167,   24,  100,   25,
  101,   89,   81,   91,    4,    5,  164,  177,  164,  152,
  153,  117,  154,  155,  119,  164,  118,   67,  122,  120,
  125,  194,  234,  126,  128,  129,  134,  135,  229,   60,
   41,   42,   74,  141,  142,    4,    5,  140,   32,   26,
   28,   37,   89,   49,   42,  107,   49,  267,   50,   50,
  289,   51,   50,  292,   51,   71,   64,  296,  297,  298,
  299,  107,  107,  107,  107,  143,   49,   50,  105,   49,
   50,  164,   50,  164,   51,   50,  162,   51,   67,  106,
  147,  163,  144,  146,  105,  105,  105,  105,  325,  104,
   86,  328,   42,  148,  150,  106,  106,  106,  106,  336,
  337,  338,  339,  340,   94,  105,  106,  107,  108,    4,
    5,  198,    4,    5,  178,   89,   89,  123,   47,   89,
   93,   90,   94,  203,  199,  179,   91,  356,  172,   95,
  173,  175,  220,  362,  211,  223,  174,  181,  176,  227,
  228,  230,  182,  183,  184,  185,  186,  187,  190,  189,
  193,  196,  204,  205,  206,  382,  208,  197,  209,  210,
  212,  213,  214,  215,  216,  217,  389,  253,  218,  244,
  256,  246,  221,  248,  249,  222,  224,  226,  264,  265,
  266,  268,  225,  231,  232,  233,  236,  237,  238,  241,
  242,  247,  250,  243,  245,  251,  252,  255,  257,  259,
  258,  260,  261,  294,  262,  263,  269,  274,  282,  300,
  288,  281,  291,  275,  295,  284,  283,  306,  285,  286,
  307,  287,  309,  315,  290,  293,  301,  302,  303,  316,
  304,  305,  318,  308,  311,  319,  310,  321,  322,  313,
  326,  330,  314,  320,  317,  352,  323,  324,  354,  329,
  327,  358,  341,  331,  332,  333,  335,  334,  359,  344,
  350,  360,  361,  351,  353,  355,  357,  311,  371,  311,
  363,  364,  365,  372,  374,  366,  311,  367,  368,  369,
  377,  373,  370,  375,  376,  378,  379,  380,  384,  385,
  381,  383,  386,  387,  388,  392,  397,  390,  399,  398,
  391,  401,  400,  402,  405,  403,  404,  406,  407,  408,
  409,  410,  411,  412,   52,    9,    6,    7,    8,   10,
   11,   38,    0,  127,    0,    0,    0,    0,    0,    0,
    0,    0,  311,    0,  311,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         11,
   12,  113,   41,   40,   40,   41,   40,   45,   45,   40,
   59,   45,   40,   40,   40,   40,   61,   61,   47,   48,
   41,    0,   43,   88,   45,    0,   45,  143,   48,   45,
  270,   45,   42,   61,   45,   59,   41,   47,   59,   60,
   25,   62,   32,   41,   41,   43,   41,   45,   40,   34,
  162,  163,  164,   41,   41,   41,   43,  169,   45,   41,
   89,   59,   60,  175,   62,   41,   43,  256,   45,   41,
  259,   59,   59,   60,  263,   62,   41,   59,   40,   41,
   44,   41,  308,   60,  310,   62,   41,  162,  163,   41,
   41,  317,   40,   83,  169,   41,   41,   41,  210,   27,
  212,   41,  214,  215,   41,   59,  222,  259,  259,   37,
   38,  263,  263,  256,  143,   41,   41,  146,  123,  184,
  105,  106,  107,  108,  109,  110,  146,   41,  123,  241,
  242,   58,   58,   59,   59,   43,   43,   45,   45,  256,
  259,   44,  256,  256,  263,   59,  125,  373,  276,  375,
  125,   59,   59,  270,  256,  257,  270,  270,  260,  256,
   41,  259,  259,  275,  266,  263,  241,  242,  270,  256,
  257,  256,  284,  260,   41,  287,  259,  264,  265,  266,
  263,  268,   41,  270,  264,  265,  114,   59,   43,   61,
   45,  271,   58,  222,  264,  265,  308,  125,  310,  100,
  101,  277,  102,  103,   41,  317,   59,  256,   41,   59,
  256,  256,  256,   41,   59,  270,  256,   59,  256,  256,
  256,  270,  256,  270,   41,  264,  265,  269,  256,  256,
  256,  256,  271,  270,  270,  256,  270,  256,  276,  276,
  252,  278,  276,  255,  278,  256,  277,  259,  260,  261,
  262,  272,  273,  274,  275,   44,  270,  276,  256,  270,
  276,  373,  276,  375,  278,  276,  123,  278,  256,  256,
  256,  123,  269,  256,  272,  273,  274,  275,  290,  256,
  256,  293,  270,  269,  256,  272,  273,  274,  275,  301,
  302,  303,  304,  305,  256,  272,  273,  274,  275,  264,
  265,  256,  264,  265,  256,  256,  271,  267,  256,  271,
  256,  256,  256,  164,  269,  267,  256,  329,   41,  256,
   59,  123,  186,  335,  175,  189,  267,   41,  267,  193,
  194,  195,   59,  256,   44,  270,   61,  269,  269,   61,
   61,  256,  125,   41,  256,  357,   59,  269,   59,  123,
  123,   41,  123,  123,  256,   59,  368,  221,   59,  210,
  224,  212,   61,  214,  215,   44,   61,   41,  232,  233,
  234,  235,  269,  269,   61,   61,  256,  125,  125,  123,
  123,  267,  267,  125,  125,   44,  123,  123,   61,  123,
  269,  123,  123,  257,  123,   61,  269,   59,   59,  263,
  256,  125,   44,  123,   61,  123,  125,   61,  125,  125,
   59,  123,   59,   59,  123,  123,  123,  123,  123,   59,
  123,  123,   59,  123,  275,   59,  123,   59,   59,  125,
  256,  295,  125,  284,  123,   59,  287,  125,   59,  123,
  125,   59,  306,  125,  125,  125,  123,  125,   59,  125,
  125,   59,   59,  125,  125,  125,  123,  308,  259,  310,
  125,  125,  125,   59,   59,  125,  317,  125,  123,  125,
   59,  123,  125,  123,  125,   59,   59,   59,   59,   59,
  125,  125,   59,   59,   59,   59,   59,  259,   59,  125,
  259,   59,  125,   59,   59,  125,  125,   59,   59,   59,
  259,  259,   59,   59,   59,   41,   41,   41,   41,   41,
   41,  125,   -1,   77,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  373,   -1,  375,
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
"clausula_while : inicio_while '(' condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while '(' error ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while '(' error LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while error condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while '(' condicion_accion ')' error '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while '(' '(' error condicion_accion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : inicio_while '(' condicion_accion ')' ')' error LOOP '{' bloque_sentencias_control '}' ';'",
"inicio_while : WHILE",
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
"invocacion_procedimiento : inicio_inv_proc lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : inicio_inv_proc ')' ';'",
"invocacion_procedimiento : inicio_inv_proc error ';'",
"invocacion_procedimiento : inicio_inv_proc '(' error ';'",
"invocacion_procedimiento : inicio_inv_proc ')' ')' error ';'",
"invocacion_procedimiento : inicio_inv_proc lista_parametros_invocacion error ';'",
"invocacion_procedimiento : inicio_inv_proc error ')' ';'",
"invocacion_procedimiento : inicio_inv_proc '(' error lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : inicio_inv_proc lista_parametros_invocacion ')' ')' error ';'",
"invocacion_procedimiento : ID error ')' ';'",
"invocacion_procedimiento : ID error lista_parametros_invocacion ')' ';'",
"invocacion_procedimiento : ID error ';'",
"inicio_inv_proc : ID '('",
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

//#line 584 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"

ArrayList<SimboloPolaca> listaReglasActual = new ArrayList<>();
ArrayList<ArrayList<SimboloPolaca>> listaReglas = new ArrayList<>();
Stack<Integer> pasosIncompletos = new Stack<>();
AnalizadorLexico analizadorLexico = new AnalizadorLexico();
ArrayList<String> erroresSintacticos = new ArrayList<>();
ArrayList<String> erroresSemanticos = new ArrayList<>();
ArrayList<String> erroresParser = new ArrayList<>();
ArrayList<String> tokens = new ArrayList<>();
ArrayList<String> estructuras = new ArrayList<>();
ArrayList<String> lista_variables = new ArrayList<>();
ArrayList<String> ambito = new ArrayList<String>() { { add("@main"); } };
Stack<String> ids = new Stack<>();
Stack<ListParameters> parametros = new Stack<ListParameters>();
ArrayList<String> parametrosInvocacion = new ArrayList<>();
Stack<Pair<String,String>> parametrosInvocacionPar = new Stack<>();
String idProcActual = null;

public void addPair(String paramForaml, String paramReal){
    parametrosInvocacionPar.push(new Pair<String,String>(paramForaml,paramReal));
}

public void addDireccionParametroReferencia(String idProc) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    while (!parametrosInvocacionPar.empty()) {
        Pair<String, String> pair = parametrosInvocacionPar.pop();
        String paramFormal = pair.getValue0();
        String paramReal = pair.getValue1();
        String lex_mangling = nameMangling(paramFormal) + "@" + idProc;
        if (ts.containsKey(lex_mangling) && ts.get(lex_mangling).get("Pasaje").equals("REFERENCIA")) {
            ArrayList<String> ambitoCopia = new ArrayList<>(ambito);
            HashMap<String, Object> direccion = null;
            for(int i = ambitoCopia.size(); i > 0; i--) {
                String newVar = paramReal + listToString(ambitoCopia);
                if (ts.containsKey(newVar)) {
                    direccion = ts.get(newVar);
                    break;
                }
                ambitoCopia.remove(ambitoCopia.size()-1);
            }
            if (direccion != null) {
                ts.get(lex_mangling).put("DIR " + paramReal, direccion);
            }
            HashMap<String,Object> atributos = direccion;
        }
    }
}

public boolean checkInvocacionProcedimiento(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    boolean seCumple = false;
    if (ts.containsKey(lex_mangling)) {
    	if ((Integer)ts.get(lex_mangling).get("Invocaciones") > (Integer)ts.get(lex_mangling).get("Llamadas")){
        	ListParameters parameters = ((ListParameters) ts.get(lex_mangling).get("Parametros"));
        	seCumple = parametrosInvocacion.size() == parameters.getCantidad();
        	if(seCumple)
        	    ts.get(lex_mangling).put("Llamadas",(Integer) ts.get(lex_mangling).get("Llamadas")+1);
    	}else
    		erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Maximo numero de invocaciones alcanzada." );

    }
    parametrosInvocacion.clear();
    return seCumple;
}

public void addParametrosProcedimiento(String lexema){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    ts.get(lex_mangling).put("Parametros", parametros.pop());
}

public void addInvocacionesProcedimiento(String lexema, String invocaciones){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    String lex_mangling = nameMangling(lexema);
    ts.get(lex_mangling).put("Invocaciones",Integer.valueOf(invocaciones));
    ts.get(lex_mangling).put("Llamadas",0);
}

public void addTipoPasajeParametros(String lexema, String pasaje){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    ts.get(nameMangling(lexema)).put("Pasaje",pasaje);
}

public boolean checkTipoCte(String cte){
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    return ts.get(cte).get("Tipo").equals("LONGINT");
}

public void checkIDNoDeclarado(String variable) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    ArrayList<String> ambitoCopia = new ArrayList<>(ambito);
    if (idProcActual != null)
    	ambitoCopia.add("@"+idProcActual);
    /* 
    proc(FLOAT x, FLOAT y) {} // x@main@proc && y@main@proc
    FLOAT a, b; // a@main && b@main
    proc(x:a, y:b); // a@main && b@main
                    // x@main (pero lo tendria que buscar en x@main@proc)
                    // y@main (pero lo tendria que buscar en y@main@proc)
    */
    for(int i = ambitoCopia.size(); i > 0; i--) {
        String newVar = variable + listToString(ambitoCopia);
        if (ts.containsKey(newVar)) {
            break;
        }
        ambitoCopia.remove(ambitoCopia.size()-1);
    }
    if (ambitoCopia.size()==0) {
        erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Variable '" + variable + "' no declarada");
    }
    if (ts.containsKey(variable)) {
        ts.remove(variable);
    }
}

public void checkIDReDeclarado(String variable) {
    HashMap<String, HashMap<String,Object>> ts = analizadorLexico.getTabla_simbolos();
    if (ts.containsKey(nameMangling(variable))) {
        erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Variable '" + variable + "' re-declarada");
    }
}

public void addAmbito(String ambito_actual){
    ambito.add("@" + ambito_actual);
}

public void deleteAmbito(){
    ambito.remove(ambito.size()-1);
}

public String listToString(ArrayList<String> list) {
    return list.toString()
        .replaceAll("\\[|]|, ", "");
}

public String nameMangling(String simbolo) {
    return simbolo + listToString(ambito);
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

public void addSimbolo(String simbol) {
	listaReglasActual.add(new SimboloPolaca(simbolo));
}

public void apilarPasoIncompleto(String nombre) {
	apilarPasoActual();
	addSimbolo(null);
	addSimbolo(nombre);
}

public void completarPasoIncompleto(boolean fin) {
	int posIncompleto = pasosIncompletos.pop();
	SimboloPolaca simbolo = listaReglasActual.get(posIncompleto);
    int pos = listaReglasActual.size();
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
	pasosIncompletos.push(listaReglasActual.size());
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
    return this.listaReglasActual;
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

			/*
			salida.write("Tokens detectados en el codigo fuente: " + "\n");
            for (String token : tokens) {
                salida.write("\t" + token + "\n");
            }

			salida.write("\n" + "Estructuras detectadas en el codigo fuente: " + "\n");
            for (String estructura : estructuras) {
                salida.write("\t" + estructura + "\n");
            }
			*/
			salida.write("\n"+"Contenido de la tabla de simbolos: " + "\n");
			salida.write(this.getAnalizadorLexico().getDatosTabla_simbolos());

			salida.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	 }
}

public void mostrar_tokens(){
	System.out.println();
	System.out.println("Tokens detectados en el codigo fuente: ");
    for (String token : this.tokens) {
        System.out.println(token);
    }
}

public void mostrar_estructuras(){
	System.out.println();
	System.out.println("Estructuras detectadas en el codigo fuente: ");
    for (String estructura : this.estructuras) {
        System.out.println(estructura);
    }
}

public static void main(String[] args){
	Parser parser = new Parser();
	parser.yyparse();
	System.out.println(parser.getErrores());

	//parser.mostrar_tokens();
	//parser.mostrar_estructuras();

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
//#line 886 "Parser.java"
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
//#line 23 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");
        }
break;
case 6:
//#line 35 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("==");
        }
break;
case 7:
//#line 39 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo(">=");
        }
break;
case 8:
//#line 43 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("<=");
        }
break;
case 9:
//#line 47 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("!=");
        }
break;
case 10:
//#line 51 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo(">");
        }
break;
case 11:
//#line 55 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("<");
        }
break;
case 12:
//#line 59 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addErrorSintactico("Error en la condicion");
        }
break;
case 13:
//#line 65 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    apilarPasoIncompleto(SimboloPolaca.BF);
                }
break;
case 14:
//#line 77 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");
                }
break;
case 15:
//#line 81 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la condicion del WHILE.");
                }
break;
case 16:
//#line 85 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la definicion del WHILE: falta el )");
                }
break;
case 17:
//#line 89 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la condicion del WHILE: falta el (.");
                }
break;
case 18:
//#line 93 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");
                }
break;
case 19:
//#line 97 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");
                }
break;
case 20:
//#line 101 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");
                }
break;
case 21:
//#line 107 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                apilarPasoActual();
                addSimbolo("L"+listaReglas.size());
            }
break;
case 22:
//#line 114 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");
                    }
break;
case 23:
//#line 118 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");
                    }
break;
case 24:
//#line 122 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la condicion del IF.");
                    }
break;
case 25:
//#line 126 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF: falta el )");
                    }
break;
case 26:
//#line 130 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF: falta el (");
                    }
break;
case 27:
//#line 134 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF: falta el END_IF");
                    }
break;
case 28:
//#line 138 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");
                    }
break;
case 29:
//#line 142 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");
                    }
break;
case 30:
//#line 146 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la condicion del IF ELSE.");
                    }
break;
case 31:
//#line 150 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el )");
                    }
break;
case 32:
//#line 154 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el (");
                    }
break;
case 33:
//#line 158 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");
                    }
break;
case 34:
//#line 162 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");
                    }
break;
case 35:
//#line 166 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");
                    }
break;
case 36:
//#line 172 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                completarPasoIncompleto(false);
                apilarPasoIncompleto(SimboloPolaca.BI);
                addSimbolo("L"+listaReglas.size());
            }
break;
case 37:
//#line 186 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{}
break;
case 43:
//#line 199 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        completarPasoIncompleto(true);
                        addSimbolo("L"+listaReglas.size());
                    }
break;
case 44:
//#line 208 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        completarPasoIncompleto(false);
                        generarBIinicio();
                        addSimbolo("L"+listaReglas.size());
                    }
break;
case 46:
//#line 215 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addSimbolo("INV");
                    }
break;
case 47:
//#line 219 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Syntax error");
                    }
break;
case 48:
//#line 226 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");
                    addSimbolo(val_peek(2).sval);
                    addSimbolo("OUT");
                }
break;
case 49:
//#line 232 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");
                }
break;
case 50:
//#line 236 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");
                }
break;
case 51:
//#line 240 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    addErrorSintactico("Error al imprimir por pantalla");
                }
break;
case 52:
//#line 246 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    checkIDReDeclarado(val_peek(0).sval);
                    modificarLexema(val_peek(0).sval);
                }
break;
case 53:
//#line 251 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    checkIDReDeclarado(val_peek(2).sval);
                    modificarLexema(val_peek(2).sval);
                }
break;
case 54:
//#line 258 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
         yyval = new ParserVal("LONGINT");
     }
break;
case 55:
//#line 262 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
         yyval = new ParserVal("FLOAT");
     }
break;
case 56:
//#line 268 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");
                                addTipoListaVariables(val_peek(2).sval,"VARIABLE");
                            }
break;
case 57:
//#line 275 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            if (checkInvocacionProcedimiento(val_peek(3).sval)){
                                addDireccionParametroReferencia(val_peek(3).sval);
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");
                            } else {
                                erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Error en la invocacion del procedimiento." );
                            }
                        }
break;
case 58:
//#line 284 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            if (checkInvocacionProcedimiento(val_peek(2).sval)){
                                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");
                            } else {
                                erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " Error en la invocacion del procedimiento." );
                            }
                        }
break;
case 59:
//#line 292 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: falta )");
                        }
break;
case 60:
//#line 296 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");
                        }
break;
case 61:
//#line 300 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");
                        }
break;
case 62:
//#line 304 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: falta )");
                        }
break;
case 63:
//#line 308 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");
                        }
break;
case 64:
//#line 312 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");
                        }
break;
case 65:
//#line 316 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");
                        }
break;
case 66:
//#line 320 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: falta (");
                        }
break;
case 67:
//#line 324 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento: falta (");
                        }
break;
case 68:
//#line 328 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                            addErrorSintactico("Error al invocar procedimiento.");
                        }
break;
case 69:
//#line 334 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                    idProcActual = null;
                    checkIDNoDeclarado(val_peek(1).sval);/*VER CASO EN EL QUE LA INVOCACION SE HACE DENTRO DEL PROCEDIMIENTO.*/
                    idProcActual = val_peek(1).sval;
                    addSimbolo("PROC");
                    addSimbolo(val_peek(1).sval);
                    yyval =  new ParserVal(val_peek(1).sval);
                }
break;
case 70:
//#line 345 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        deleteAmbito();
                                        if (checkTipoCte(val_peek(4).sval)) {
                                            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");
                                            String val = ids.pop();
                                            addInvocacionesProcedimiento(val,val_peek(4).sval);
                                            addParametrosProcedimiento(val);
                                        }
                                        else
                                            erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " La constante declarada en el procedimiento no es de tipo LONGINT." );
                                    }
break;
case 71:
//#line 357 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        deleteAmbito();
                                        if (checkTipoCte(val_peek(4).sval)) {
                                            estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");
                                            addInvocacionesProcedimiento(ids.pop(),val_peek(4).sval);
                                        }
                                        else
                                            erroresSemanticos.add("Numero de linea: "+ (analizadorLexico.getFilaActual()+1) + " La constante declarada en el procedimiento no es de tipo LONGINT." );
                                    }
break;
case 72:
//#line 367 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta )");
                                    }
break;
case 73:
//#line 371 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta (");
                                    }
break;
case 74:
//#line 375 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");
                                    }
break;
case 75:
//#line 379 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");
                                    }
break;
case 76:
//#line 383 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta NI");
                                    }
break;
case 77:
//#line 387 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta =");
                                    }
break;
case 78:
//#line 391 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta cte");
                                    }
break;
case 79:
//#line 395 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");
                                    }
break;
case 80:
//#line 399 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta )");
                                    }
break;
case 81:
//#line 403 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta (");
                                    }
break;
case 82:
//#line 407 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta NI");
                                    }
break;
case 83:
//#line 411 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta =");
                                    }
break;
case 84:
//#line 415 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: falta cte");
                                    }
break;
case 85:
//#line 419 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");
                                    }
break;
case 86:
//#line 423 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                        addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");
                                    }
break;
case 87:
//#line 429 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            	checkIDReDeclarado(val_peek(0).sval);/*Check que exista el id.*/
                modificarLexema(val_peek(0).sval);
                addTipoListaVariables("PROC","PROC");
                addAmbito(val_peek(0).sval);
                ids.push(val_peek(0).sval);
                /* Agregar para el procedimiento, nombre de los parametros.*/
            }
break;
case 88:
//#line 438 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                addErrorSintactico("Error al declarar procedimiento: falta ID");
            }
break;
case 92:
//#line 447 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");
                            }
break;
case 93:
//#line 453 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                parametros.push(new ListParameters(val_peek(0).sval));
                            }
break;
case 94:
//#line 457 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                parametros.push(new ListParameters(val_peek(2).sval, val_peek(0).sval));
                            }
break;
case 95:
//#line 461 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                parametros.push(new ListParameters(val_peek(4).sval, val_peek(2).sval, val_peek(0).sval));
                            }
break;
case 96:
//#line 465 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                                addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");
                            }
break;
case 97:
//#line 471 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        checkIDReDeclarado(val_peek(0).sval);
                        modificarLexema(val_peek(0).sval);
                        addTipoListaVariables(val_peek(1).sval,"PARAMETRO");
                        addTipoPasajeParametros(val_peek(0).sval,"COPIA-VALOR");
                        yyval = new ParserVal(val_peek(0).sval);
                    }
break;
case 98:
//#line 479 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        checkIDReDeclarado(val_peek(0).sval);
                        modificarLexema(val_peek(0).sval);
                        addTipoListaVariables(val_peek(1).sval,"PARAMETRO");
                        addTipoPasajeParametros(val_peek(0).sval,"REFERENCIA");
                        yyval = new ParserVal(val_peek(0).sval);
                    }
break;
case 99:
//#line 489 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        checkIDNoDeclarado(val_peek(2).sval);
                        /*
                        * CHEQUEAR QUE SE CORRESPONDA CON EL NOMBRE DEL PARAMETRO DECLARADO.
                        * Yo los agregaria todos los $1 a una lista, y cuando invoca al proc, chquear
                        * con los parametros reales
                        * Si falta alguno, se repite alguno, o alguno no se corresponde, informar error.
                        * Para esto en la declaracion de proc, agregar atributo en la ts (parametros) con
                        * una lista de los ids.
                        */
                        checkIDNoDeclarado(val_peek(0).sval);
                        if (!parametrosInvocacion.contains(val_peek(2).sval)) {
                            parametrosInvocacion.add(val_peek(2).sval);
                            addPair(val_peek(2).sval,val_peek(0).sval);
                        }
                        addSimbolo(val_peek(2).sval);
                        addSimbolo(val_peek(0).sval);
                        addSimbolo(":");
                    }
break;
case 100:
//#line 509 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion de parametro del lado derecho");
                    }
break;
case 101:
//#line 513 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                        addErrorSintactico("Error en la definicion de parametros del lado izquierdo");
                    }
break;
case 102:
//#line 519 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");
                addSimbolo(val_peek(3).sval);
                addSimbolo("=");
                idProcActual = null;
                checkIDNoDeclarado(val_peek(3).sval);
            }
break;
case 103:
//#line 527 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                addErrorSintactico("Error de asignación a la derecha.");
            }
break;
case 104:
//#line 531 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
                addErrorSintactico("Error de asignación a la izquierda.");
            }
break;
case 105:
//#line 537 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("+");
        }
break;
case 106:
//#line 541 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("-");
        }
break;
case 108:
//#line 548 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("*");
        }
break;
case 109:
//#line 552 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo("/");
        }
break;
case 111:
//#line 559 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
            addSimbolo(val_peek(0).sval);
	        idProcActual = null;
            checkIDNoDeclarado(val_peek(0).sval);
        }
break;
case 112:
//#line 565 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
           addSimbolo(val_peek(0).sval);
        }
break;
case 114:
//#line 572 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
        if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
            addErrorSintactico("Error longint fuera de rango");}
        yyval = val_peek(0);
    }
break;
case 115:
//#line 578 "/home/guido/Documents/Facultad/Compiladores/CompiladoresTP/gramatica.y"
{
        analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
        yyval = new ParserVal("-"+val_peek(0).sval);
    }
break;
//#line 1704 "Parser.java"
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
