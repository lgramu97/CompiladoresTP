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
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.util.ArrayList;
	import java.util.Scanner;
	import javax.swing.JFileChooser;

	
//#line 27 "Parser.java"




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
    4,    4,    6,    6,    6,    6,    6,    6,    6,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    7,    7,    2,    2,    3,    3,    3,
    3,    3,    3,   12,   12,   12,   12,   14,   14,   15,
   15,    9,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   16,   16,   16,   16,   17,   17,   17,
   17,   20,   20,   19,   19,   19,   11,   11,   11,    5,
    5,    5,   21,   21,   21,   22,   22,   22,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    2,    9,    9,    8,    9,    9,   11,   11,    9,
   13,    9,    8,    9,    9,   11,   11,   13,   12,   13,
   13,   15,   15,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    5,    6,    6,    3,    1,    3,    1,
    1,    3,    5,    4,    4,    4,    5,    6,    5,    5,
    5,    7,    7,    3,   12,   11,   11,   11,   11,   13,
   13,   11,   11,   11,   12,   12,   12,   12,   12,   12,
   12,   14,   14,    1,    3,    5,    7,    1,    3,    5,
    7,    2,    3,    3,    3,    3,    4,    4,    4,    3,
    3,    1,    3,    3,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   50,   51,    0,    0,    0,    0,    1,
    0,    0,   40,   39,   36,   37,   38,   41,   42,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    0,  106,  109,  108,
    0,    0,  107,    0,  105,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   52,  110,   99,    0,    0,    0,    0,
    0,   12,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,    0,    0,    0,   55,    0,    0,   54,
    0,    0,   98,   97,   49,    0,    0,  103,  104,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   92,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   96,   95,   94,
   61,    0,   60,   57,    0,    0,   59,    0,   53,    0,
    0,   35,    0,    0,    0,    0,   45,   46,    0,    0,
    0,    0,    0,    0,    0,   93,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   58,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   62,   63,    0,    0,    0,    0,   23,    0,    0,
    0,    0,    0,    0,    0,   15,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   24,    0,   22,    0,    0,    0,    0,   25,
   20,    0,   16,   14,    0,   17,   13,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   87,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   91,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   26,    0,   27,    0,    0,   18,   19,   67,
    0,   69,    0,   68,    0,    0,    0,   72,   73,   74,
   66,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   29,    0,    0,    0,    0,   76,   78,   75,    0,    0,
    0,   77,   79,   80,   81,   65,    0,   30,   28,    0,
    0,   31,   21,   70,    0,   71,    0,    0,    0,   82,
   83,   32,   33,
};
final static short yydgoto[] = {                          9,
   10,   11,   12,   46,   47,   13,  139,   14,   15,   16,
   17,   18,   19,   37,   20,   65,  103,   43,   66,  104,
   44,   45,
};
final static short yysindex[] = {                       -68,
   38,  -30,  -29,    0,    0,  -28, -128,  -33,    0,    0,
  -68,  -68,    0,    0,    0,    0,    0,    0,    0, -229,
    0,  -26,  -26,  -37,   28,  -34,  -26,  -36,   76,   -3,
   31,    4,  -40,    0,    0,   89,  101,    0,    0,    0,
  -98,   84,    0,   42,    0,  134,  -27,  -12,  -74,  153,
    0,  154,  -78,  160,   22,  -53,  163,   15,   17,   -2,
  147,  149,  152,    0,  171,  169,   10,  -39,   14,  -24,
  159,   87, -229,    0,    0,    0,  -26,  -26,  -26,  -26,
   98,    0,  -26,  -26,  -26,  -26,  -26,  -26,   99, -119,
  -26,   -5,   16,  184,   24,  108,   43,  -26,   41,  -80,
   -4,    5,  279,  277,   57,  290,  -10,   77,   29,    2,
   64, -125,    0,  273, -120,  278,    0,  -46,   83,    0,
  282,   45,    0,    0,    0,   42,   42,    0,    0, -119,
  129,  129,  129,  129,  129,  129, -119, -119,  218,  306,
   92, -119,  291,    0,  292,  226, -119,  229,  312,  231,
  232,  100,   88,  296,    0,   91,  -95,  300,   93,  302,
   96,   19,  303,  -47,  112,  103,   36,    0,    0,    0,
    0,  323,    0,    0,  328,  311,    0,  117,    0,  250,
  253,    0, -198,  256,  257,  258,    0,    0, -119,  263,
 -119,  114, -119, -119,  115,    0,  -20,  329,  345,  -20,
  330,  -20,  331,  124,  353,  -20,  -20,  -21,  126,  335,
  336,  -41,  142, -120,  340,    0,  341, -192, -111,  342,
  280, -119, -119, -102,  281,  343,  283,  284,  285,  286,
  289,  295,  -20,  -95,  297,  -20,  299,  -20,  344,  135,
  301,  304,  305,  307,  348,  -20,  -20,  -20,  -19,  144,
  370,    0,    0,  356,  308,  360,  309,    0, -119,  310,
  313,  366,  367,  314,  374,    0,  375, -119,  377,  380,
 -119,  -68,  317,  385,  -68,  318,  -68,  319,  -20,  382,
  -68,  -68,  -68,  -68,  -20,  321,  322,  324,  325,  326,
  389,  190,    0, -119,    0, -119,  327, -101,  -86,    0,
    0, -119,    0,    0,  332,    0,    0,  333,  334,  -68,
  195,  337,  -68,  338,  -68,  346,  -20,  339,  347,  349,
  350,  354,  -68,  -68,  -68,  -68,  -68,  -20,    0,  351,
  355,  194,  395,  358,  397,  359,  361,  401,  402,  406,
  362,    0,  408,  363,  409,  364,  -68,  368,  411,  412,
  414,  419,  -68,  365,  369,  371,  372,  373,  376,  220,
  224,  425,    0, -119,    0, -119, -135,    0,    0,    0,
  426,    0,  433,    0,  436,  378,  -68,    0,    0,    0,
    0,  379,  441,  442,  443,  446,  447,  -68,  448,  449,
    0,  384,  386,  451,  453,    0,    0,    0,  454,  390,
  455,    0,    0,    0,    0,    0,  391,    0,    0,  260,
  261,    0,    0,    0,  458,    0,  459,  462,  463,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   30,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  464,    0,    0,    0,    0,
    0,    0,    0,   21,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   11,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   50,   55,    0,    0,    0,
  483,  484,  485,  486,  487,  488,    0,  405,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   44,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   47,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   51,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   53,    0,    0,    0,    0,    0,    0,
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
    0,    0,    0,
};
final static short yygindex[] = {                         0,
  -11,    0,  -88,   95,   80,    0, -115,    0,    0,    0,
    0,    0,    0,  460,  259,    8,  -13,  138,  -94, -109,
   62,  111,
};
final static int YYTABLESIZE=533;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         34,
   35,  138,   49,   56,   41,   53,   32,   41,   41,   24,
   26,   28,  174,  208,  180,   77,  122,   78,   41,  249,
  172,  181,  182,   41,   41,   41,  186,   33,   89,    2,
  161,  190,   88,    3,   87,  141,   60,  108,  109,   70,
   36,  138,  167,   68,   69,  106,  110,  199,  138,  138,
  116,   84,   88,  138,  119,  101,  143,  105,  138,  204,
  220,  102,   97,  102,  221,  102,  254,  111,  117,  165,
  255,   63,  120,  225,  144,  227,  213,  229,  230,  102,
  102,  152,  102,   79,   85,  178,   51,   89,   80,   64,
  100,   86,  100,   90,  100,  101,   21,  101,   22,  101,
  138,   42,  138,  179,  138,  138,  260,  261,  100,  100,
   90,  100,   72,  101,  101,   58,  101,  142,   50,  251,
  394,   54,   57,  395,  274,  175,   77,   29,   78,   77,
  169,   78,   73,  138,  138,   61,    1,    2,  126,  127,
    3,   30,   76,  297,  170,  124,    6,  256,  205,   62,
    8,  257,  305,  262,    2,  308,  263,  333,    3,   74,
  264,  334,  131,  132,  133,  134,  135,  136,    4,    5,
  138,   77,  335,   78,   81,  100,  336,   75,  330,  138,
  331,   91,  138,    4,    5,  140,  337,    1,    2,  128,
  129,    3,  149,   92,   93,    4,    5,    6,   94,    7,
   95,    8,   98,   99,  111,  138,  112,  138,  207,   61,
  113,  114,  115,  138,  248,   71,  118,  123,   48,   55,
  130,  137,   31,   62,  145,   23,   25,   27,   82,   38,
  147,  121,   38,   38,  243,   39,  289,   40,   39,   39,
   40,   40,   52,   38,   83,   84,   85,   86,  392,   39,
  393,   40,   59,  107,   39,   39,   39,  166,  160,   67,
  309,    4,    5,  312,  154,  314,   84,   88,  100,  318,
  319,  320,  321,   62,  155,  138,  102,  138,    4,    5,
    4,    5,    4,    5,  163,  100,   61,  100,   96,  100,
  146,  211,  102,  102,  102,  102,  150,  164,  341,   85,
   62,  344,   89,  346,  212,  100,   86,  151,   90,  148,
  101,  354,  355,  356,  357,  358,  102,  102,  102,  156,
  157,  100,  100,  100,  100,  158,  101,  101,  101,  101,
  159,  171,  162,  168,  232,  376,  173,  235,  176,  237,
  177,  382,  183,  241,  242,  244,  184,  185,  189,  187,
  188,  191,  192,  193,  194,  195,  197,  196,  153,  198,
  200,  201,  202,  206,  203,  400,  214,  209,  215,  216,
  273,  210,  217,  276,  218,  278,  407,  219,  222,  223,
  228,  231,  224,  286,  287,  288,  290,  226,  234,  233,
  236,  238,  239,  240,  245,  246,  247,  250,  252,  253,
  258,  266,  259,  280,  279,  265,  268,  267,  285,  269,
  270,  271,  291,  292,  293,  102,  316,  272,  295,  275,
  102,  277,  322,  281,  300,  301,  282,  283,  311,  284,
  294,  296,  303,  304,  298,  306,  302,  299,  307,  310,
  313,  315,  317,  323,  324,  329,  325,  326,  327,  328,
  342,  332,  362,  363,  348,  365,  338,  339,  340,  368,
  369,  343,  345,  349,  370,  359,  372,  374,  347,  378,
  379,  350,  380,  351,  352,  360,  353,  381,  389,  361,
  364,  366,  390,  391,  396,  367,  371,  373,  375,  383,
  377,  397,  102,  384,  398,  385,  386,  387,  388,  402,
  403,  404,  399,  401,  405,  406,  408,  409,  410,  412,
  411,  413,  414,  416,  415,  417,  420,  421,  418,  419,
  422,  423,   48,    9,    6,    7,    8,   10,   11,   34,
    0,    0,  125,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         11,
   12,   90,   40,   40,   45,   40,   40,   45,   45,   40,
   40,   40,   59,   61,  130,   43,   41,   45,   45,   61,
  115,  137,  138,   45,   45,   45,  142,   61,   41,    0,
   41,  147,   60,    0,   62,   41,   40,   40,   41,   32,
  270,  130,   41,   40,   41,   59,   60,  157,  137,  138,
   41,   41,   41,  142,   41,   41,   41,   41,  147,   41,
  259,   41,   41,   43,  263,   45,  259,   58,   59,   41,
  263,   41,   59,  189,   59,  191,   41,  193,  194,   59,
   60,   41,   62,   42,   41,   41,   59,   41,   47,   59,
   41,   41,   43,   41,   45,   41,   59,   43,   61,   45,
  189,   22,  191,   59,  193,  194,  222,  223,   59,   60,
  123,   62,   33,   59,   60,   40,   62,  123,   24,  214,
  256,   27,   28,  259,  234,  118,   43,  256,   45,   43,
  256,   45,   44,  222,  223,  256,  256,  257,   77,   78,
  260,  270,   59,  259,  270,   59,  266,  259,  162,  270,
  270,  263,  268,  256,  125,  271,  259,  259,  125,   59,
  263,  263,   83,   84,   85,   86,   87,   88,  264,  265,
  259,   43,  259,   45,   41,  271,  263,  276,  294,  268,
  296,  256,  271,  264,  265,   91,  302,  256,  257,   79,
   80,  260,   98,   41,   41,  264,  265,  266,  277,  268,
   41,  270,  256,   41,   58,  294,   58,  296,  256,  256,
   59,   41,   44,  302,  256,  256,  256,   59,  256,  256,
  123,  123,  256,  270,   41,  256,  256,  256,  256,  270,
  123,  256,  270,  270,  256,  276,  256,  278,  276,  276,
  278,  278,  277,  270,  272,  273,  274,  275,  364,  276,
  366,  278,  256,  256,  276,  276,  276,  256,  269,  256,
  272,  264,  265,  275,  269,  277,  256,  256,  271,  281,
  282,  283,  284,  270,  270,  364,  256,  366,  264,  265,
  264,  265,  264,  265,  256,  271,  256,  271,  267,  271,
  267,  256,  272,  273,  274,  275,  256,  269,  310,  256,
  270,  313,  256,  315,  269,  256,  256,  267,  256,  267,
  256,  323,  324,  325,  326,  327,   58,   59,   60,   41,
   44,  272,  273,  274,  275,  269,  272,  273,  274,  275,
   41,   59,  256,  270,  197,  347,   59,  200,  256,  202,
   59,  353,  125,  206,  207,  208,   41,  256,  123,   59,
   59,  123,   41,  123,  123,  256,   61,  270,  100,  269,
   61,  269,   61,   61,  269,  377,   44,  256,   41,   59,
  233,  269,  256,  236,  125,  238,  388,  125,  123,  123,
  267,  267,  125,  246,  247,  248,  249,  125,   44,   61,
   61,   61,  269,   41,  269,   61,   61,  256,   59,   59,
   59,   59,  123,  269,   61,  125,  123,  125,   61,  125,
  125,  123,  269,   44,   59,  157,  279,  123,   59,  123,
  162,  123,  285,  123,   59,   59,  123,  123,   44,  123,
  123,  123,   59,   59,  125,   59,  123,  125,   59,  123,
  123,  123,   61,  123,  123,  256,  123,  123,  123,   61,
  256,  125,  259,   59,  317,   59,  125,  125,  125,   59,
   59,  125,  125,  125,   59,  328,   59,   59,  123,   59,
   59,  125,   59,  125,  125,  125,  123,   59,  259,  125,
  123,  123,  259,   59,   59,  125,  125,  125,  125,  125,
  123,   59,  234,  125,   59,  125,  125,  125,  123,   59,
   59,   59,  125,  125,   59,   59,   59,   59,  125,   59,
  125,   59,   59,   59,  125,  125,   59,   59,  259,  259,
   59,   59,   59,   41,   41,   41,   41,   41,   41,  125,
   -1,   -1,   73,
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
"clausula_while : WHILE '(' condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' error ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' error LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE error condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' condicion ')' error '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' '(' error condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' condicion ')' ')' error LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' error condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' error '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' error condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
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

//#line 179 "gramatica.y"

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
//#line 678 "Parser.java"
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
//#line 19 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Fin de programa.");}
break;
case 12:
//#line 34 "gramatica.y"
{addErrorSintactico("Error en la condicion");}
break;
case 13:
//#line 37 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
break;
case 14:
//#line 38 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE.");}
break;
case 15:
//#line 39 "gramatica.y"
{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
break;
case 16:
//#line 40 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
break;
case 17:
//#line 41 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");}
break;
case 18:
//#line 42 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");}
break;
case 19:
//#line 43 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");}
break;
case 20:
//#line 46 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
break;
case 21:
//#line 47 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
break;
case 22:
//#line 48 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF.");}
break;
case 23:
//#line 49 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el )");}
break;
case 24:
//#line 50 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el (");}
break;
case 25:
//#line 51 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
break;
case 26:
//#line 52 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");}
break;
case 27:
//#line 53 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");}
break;
case 28:
//#line 54 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF ELSE.");}
break;
case 29:
//#line 55 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
break;
case 30:
//#line 56 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
break;
case 31:
//#line 57 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
break;
case 32:
//#line 58 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");}
break;
case 33:
//#line 59 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");}
break;
case 43:
//#line 75 "gramatica.y"
{addErrorSintactico("Syntax error");}
break;
case 44:
//#line 78 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");}
break;
case 45:
//#line 79 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");}
break;
case 46:
//#line 80 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}
break;
case 47:
//#line 81 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla");}
break;
case 52:
//#line 92 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");}
break;
case 53:
//#line 95 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");}
break;
case 54:
//#line 96 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");}
break;
case 55:
//#line 97 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 56:
//#line 98 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 57:
//#line 99 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 58:
//#line 100 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 59:
//#line 101 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 60:
//#line 102 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
break;
case 61:
//#line 103 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 62:
//#line 104 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 63:
//#line 105 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 64:
//#line 106 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento.");}
break;
case 65:
//#line 109 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
break;
case 66:
//#line 110 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
break;
case 67:
//#line 111 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 68:
//#line 112 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 69:
//#line 113 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 70:
//#line 114 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 71:
//#line 115 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 72:
//#line 116 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 73:
//#line 117 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 74:
//#line 118 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 75:
//#line 119 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 76:
//#line 120 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 77:
//#line 121 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 78:
//#line 122 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 79:
//#line 123 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 80:
//#line 124 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 81:
//#line 125 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 82:
//#line 126 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 83:
//#line 127 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 87:
//#line 134 "gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 91:
//#line 141 "gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 95:
//#line 149 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 96:
//#line 150 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 97:
//#line 153 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 98:
//#line 154 "gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 99:
//#line 155 "gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 109:
//#line 173 "gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 110:
//#line 175 "gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 1105 "Parser.java"
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
