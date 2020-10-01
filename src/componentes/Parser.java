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
    8,    8,    8,    7,    7,    2,    2,    3,    3,    3,
    3,    3,    3,   12,   12,   12,   12,   14,   14,   15,
   15,    9,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   10,   19,   19,
   19,   19,   16,   16,   16,   17,   17,   17,   21,   21,
   20,   20,   20,   11,   11,   11,   11,    5,    5,    5,
   22,   22,   22,   23,   23,   23,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    9,    9,    8,    9,    9,   10,   10,    9,   13,
    9,    8,    8,    9,   10,   10,   12,   12,   12,   13,
   14,   14,   13,    1,    2,    1,    1,    1,    1,    1,
    1,    1,    2,    5,    6,    6,    3,    1,    3,    1,
    1,    3,    5,    4,    4,    4,    5,    5,    5,    5,
    5,    6,    6,   12,   11,   11,   11,   11,   11,   11,
   11,   12,   12,   12,   12,   12,   12,   12,    1,    1,
    2,    2,    1,    3,    5,    1,    3,    5,    2,    3,
    3,    3,    3,    4,    4,    4,    4,    4,    4,    1,
    3,    3,    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   50,   51,    0,    0,    0,    0,    1,
    0,    0,   40,   39,   36,   37,   38,   41,   42,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    0,  104,  107,  106,
    0,    0,  105,    0,  103,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   52,  108,   96,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   56,
    0,    0,    0,   55,    0,    0,    0,   54,    0,    0,
   95,   97,   94,   49,    0,    0,  101,  102,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   44,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   89,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   93,   92,   91,   61,    0,   60,
   57,    0,   58,   59,    0,   53,   98,   99,   35,    0,
    0,    0,    0,    0,    0,    0,   45,   46,    0,    0,
    0,    0,    0,    0,    0,   90,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   62,   63,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   85,   23,    0,    0,    0,   22,
    0,    0,    0,    0,    0,    0,    0,    0,   14,    0,
    0,    0,    0,    0,    0,    0,   88,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   21,    0,    0,    0,    0,    0,    0,   24,
   19,    0,   15,   13,    0,   16,   12,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   25,    0,
   26,    0,    0,    0,   17,   18,   81,   82,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   66,    0,   68,    0,   67,    0,   69,   70,   71,   65,
    0,    0,    0,    0,    0,   29,   27,   28,    0,    0,
    0,    0,    0,   73,   75,   72,   74,   76,   77,   78,
   64,    0,    0,   33,   30,   20,   31,   32,
};
final static short yydgoto[] = {                          9,
   10,  299,  129,   49,   50,   13,  130,   14,   15,   16,
   17,   18,   19,   37,   20,   64,  101,   43,  301,   65,
  102,   44,   45,
};
final static short yysindex[] = {                      -108,
  102,  -26,    4,    0,    0,    6, -171,  -33,    0,    0,
 -108, -108,    0,    0,    0,    0,    0,    0,    0, -232,
    0,  -19,   26,  -37,   59,  -31,  -19,  -34,   55,    8,
  -36,   14,  -41,    0,    0,   85,   78,    0,    0,    0,
 -131,   74,    0,   47,    0,   41,  -17,  -19,  114,  -20,
    0,  132, -100,  154,  -11,  -19,  156,   15,   18,  -39,
  135,  141,  147,  168,  169,   20,   21,   -8,   16,  157,
   37, -232,    0,    0,    0,  -19,  -19,  -19,  -19,  -72,
   95,  -72,  180,  -10,  -19,  -19,  -19,  -19,  -19,  -19,
   22,  183,   30,  145,   35,  255,   11,  -73,   39,   28,
  258,  260,   40,  265,  -24, -211,   29,   48, -166,    0,
  251, -156,  252,    0,  262,  278,  264,    0,  268,   24,
    0,    0,    0,    0,   27,   56,    0,    0,  -72,  203,
  -72,  204,  207,  208,  -70,  133,  133,  133,  133,  133,
  133,  273,    0,  274,  211,  -72,  212,   69,  214,  215,
   72,   70,  280,    0,   73,  -63,  282,   75,  284,   77,
  286,  -51,   79, -172,    0,    0,    0,    0,  305,    0,
    0,  291,    0,    0,  293,    0,    0,    0,    0, -129,
 -110, -124,  -72,  -72,    7,  228,    0,    0,  -72,  230,
  -72,  234,  -72,  -72,  235,    0,  -18,  298,  316,  -18,
  304,  -18,  306,  -18,  -18,  -13,  307,  308,  -42, -156,
    0,    0,  311,  243,  248,  113,  314,  253,  249,  250,
  115, -220,  254,  318,  256,  -72,  257,  259,  -72,  263,
  -18,  -63,  266,  -18,  267,  -18,  269,  270,  271,  272,
  -18,  -18,  -18,    5,    0,    0,  -72,  -72,  321,    0,
  -72, -113,  -88,  275,  324,  326,  276,  328,    0,  329,
  277,  332,  337,  279, -108,  283,    0, -108,  285, -108,
  287, -108, -108, -108, -108,  288,  289,  290,  292,  294,
  295,  296,    0,  297,  338,  300,  341,  301,  -72,    0,
    0,  -72,    0,    0,  342,    0,    0,  344, -108, -108,
  302, -108,  303, -108,  309, -108,  310,  312,  313,  315,
 -108, -108, -108, -108, -108,  146,  148,  150,    0,  -72,
    0,  -72,  317,  319,    0,    0,    0,    0,  355,  320,
  357,  322,  359,  323,  360,  366,  367,  370,  325,  327,
  330,  331,  333,  371,  372,  373,  334,  335,  174, -105,
    0,  377,    0,  380,    0,  382,    0,    0,    0,    0,
  384,  387,  390,  392,  394,    0,    0,    0,  195,  198,
  402,  403,  404,    0,    0,    0,    0,    0,    0,    0,
    0,  405,  406,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  466,  467,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  409,    0,    0,    0,    0,
    0,    0,    0,  -25,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   31,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   32,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -104,    0,
    0,    0,    0,    0,    0,  428,  429,  430,  431,  432,
  433,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   34,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  350,  351,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  192,  110,    1,   60,   38,    0,  -82,    0,    0,    0,
    0,    0,    0,  407,   33,    9,  152,  120, -132,  -90,
 -127,  151,  216,
};
final static int YYTABLESIZE=479;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        132,
   12,  106,   48,   41,   63,   56,   32,   41,   53,  206,
   41,   12,   12,   24,  216,  100,  160,  100,  244,  100,
   34,  169,   76,   81,   77,   41,   41,   33,  199,   95,
  134,   41,  117,  100,  100,  255,  100,   36,  256,   90,
   69,   89,  257,   26,  161,   28,  179,   60,  181,   41,
  118,  151,  186,   67,   68,   99,  120,  162,  103,   42,
  113,  115,  142,  190,  175,   21,   46,   22,   78,  164,
   71,   83,   86,   79,   84,  116,   87,  108,  114,   76,
  143,   77,  176,  208,   29,  177,   54,   57,   78,  166,
  100,  100,  100,   79,   58,  123,  209,   78,   30,   61,
  219,  220,   79,  167,  267,   82,  223,   83,  225,   11,
  227,  228,  135,   62,  178,   96,   76,   51,   77,  245,
   11,   11,  136,  137,  138,  139,  140,  141,   72,  213,
  152,  221,   75,  214,  217,  303,   73,  305,  218,  307,
  308,  309,  310,  261,   74,  285,  264,    1,    2,  286,
  372,    3,  215,  373,   84,    4,    5,    6,   34,    7,
   21,    8,   22,   80,  281,  282,  327,  328,  284,  330,
  287,  332,   91,  334,  288,   76,   92,   77,  339,  340,
  341,  342,  343,    1,    2,  185,    2,    3,  100,    3,
    4,    5,  108,    6,   93,    6,   97,    8,  109,    8,
    4,    5,   34,   35,  205,  110,  323,   98,  111,  324,
  104,  107,  112,  243,   70,  121,  105,  131,   47,   61,
  133,   55,   31,  144,    4,    5,  125,  126,   38,   23,
  100,   98,   38,   62,   39,   38,   40,  347,   39,  348,
   40,   39,  239,   40,  159,   52,  100,  100,  100,  100,
   38,   85,   86,   87,   88,   94,   39,   39,   40,   25,
  279,   27,   39,   59,  100,  300,  149,  146,  300,   66,
  300,  119,  300,  300,  300,  300,   61,  150,    4,    5,
   39,    4,    5,   62,  163,   98,   83,   86,   98,   84,
   62,   87,  122,  127,  128,  148,  145,  154,  155,  300,
  300,  147,  300,  156,  300,  158,  300,  153,  157,  168,
  170,  300,  300,  300,  300,  300,  230,  165,  172,  233,
  171,  235,  173,  237,  238,  240,  174,  180,  182,  183,
  184,  187,  188,  189,  191,  192,  193,  194,  195,  196,
  197,  198,  200,  201,  202,  203,  204,  207,  210,  211,
  266,  212,  222,  269,  224,  271,  226,  229,  231,  232,
  276,  277,  278,  280,  234,  247,  236,  241,  242,  246,
  248,  249,  250,  252,  253,  251,  259,  254,  258,  283,
  260,  262,  290,  263,  291,  265,  293,  294,  268,  270,
  296,  272,  273,  274,  275,  297,  319,  289,  292,  321,
  325,  295,  326,  298,  344,  302,  345,  304,  346,  306,
  311,  312,  313,  351,  314,  353,  315,  355,  357,  316,
  317,  318,  320,  322,  358,  359,  329,  331,  360,  366,
  367,  368,  371,  333,  335,  374,  336,  337,  375,  338,
  376,  349,  377,  350,  352,  378,  354,  356,  379,  361,
  380,  362,  381,  382,  363,  364,  383,  365,  369,  370,
  384,  385,  386,  387,  388,    2,    3,   48,    9,    6,
    7,    8,   10,   11,   79,   80,    0,    0,  124,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         82,
    0,   41,   40,   45,   41,   40,   40,   45,   40,   61,
   45,   11,   12,   40,  125,   41,   41,   43,   61,   45,
  125,  112,   43,   41,   45,   45,   45,   61,  156,   41,
   41,   45,   41,   59,   60,  256,   62,  270,  259,   60,
   32,   62,  263,   40,  256,   40,  129,   40,  131,   45,
   59,   41,  135,   40,   41,   41,   41,  269,   41,   22,
   41,   41,   41,  146,   41,   59,   41,   61,   42,   41,
   33,   41,   41,   47,   41,   67,   41,   58,   59,   43,
   59,   45,   59,  256,  256,   59,   27,   28,   42,  256,
   58,   59,   60,   47,   40,   59,  269,   42,  270,  256,
  183,  184,   47,  270,  232,  123,  189,   48,  191,    0,
  193,  194,  123,  270,   59,   56,   43,   59,   45,  210,
   11,   12,   85,   86,   87,   88,   89,   90,   44,  259,
   98,  125,   59,  263,  259,  268,   59,  270,  263,  272,
  273,  274,  275,  226,  276,  259,  229,  256,  257,  263,
  256,  260,  263,  259,   41,  264,  265,  266,  263,  268,
   59,  270,   61,  123,  247,  248,  299,  300,  251,  302,
  259,  304,   41,  306,  263,   43,  277,   45,  311,  312,
  313,  314,  315,  256,  257,  256,  257,  260,  156,  260,
  264,  265,   58,  266,   41,  266,   41,  270,   58,  270,
  264,  265,   11,   12,  256,   59,  289,  271,   41,  292,
   59,   60,   44,  256,  256,   59,  256,  123,  256,  256,
   41,  256,  256,   41,  264,  265,   76,   77,  270,  256,
  256,  271,  270,  270,  276,  270,  278,  320,  276,  322,
  278,  276,  256,  278,  269,  277,  272,  273,  274,  275,
  270,  272,  273,  274,  275,  267,  276,  276,  278,  256,
  256,  256,  276,  256,  232,  265,  256,  123,  268,  256,
  270,  256,  272,  273,  274,  275,  256,  267,  264,  265,
  276,  264,  265,  270,  256,  271,  256,  256,  271,  256,
  270,  256,  256,   78,   79,   41,  267,  270,   41,  299,
  300,  267,  302,   44,  304,   41,  306,  269,  269,   59,
   59,  311,  312,  313,  314,  315,  197,  270,   41,  200,
   59,  202,   59,  204,  205,  206,   59,  125,  125,  123,
  123,   59,   59,  123,  123,  267,  123,  123,  267,  270,
   61,  269,   61,  269,   61,  269,   61,  269,   44,   59,
  231,   59,  125,  234,  125,  236,  123,  123,   61,   44,
  241,  242,  243,  244,   61,  123,   61,   61,   61,   59,
  123,  259,   59,  125,  125,  123,   59,  263,  125,   59,
  125,  125,   59,  125,   59,  123,   59,   59,  123,  123,
   59,  123,  123,  123,  123,   59,   59,  123,  123,   59,
   59,  125,   59,  125,  259,  123,  259,  123,  259,  123,
  123,  123,  123,   59,  123,   59,  123,   59,   59,  125,
  125,  125,  123,  123,   59,   59,  125,  125,   59,   59,
   59,   59,  259,  125,  125,   59,  125,  125,   59,  125,
   59,  125,   59,  125,  125,   59,  125,  125,   59,  125,
   59,  125,   59,  259,  125,  125,  259,  125,  125,  125,
   59,   59,   59,   59,   59,    0,    0,   59,   41,   41,
   41,   41,   41,   41,  125,  125,   -1,   -1,   72,
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
"clausula_while : WHILE '(' condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' error ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' error LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE error condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' condicion ')' error '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' '(' condicion ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_while : WHILE '(' condicion ')' ')' LOOP '{' bloque_sentencias_control '}' ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' error '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
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
"sentencia_declaracion_procedimiento : PROC ID '(' error ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC error '(' lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion error NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID error lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' error '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI error cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' error '{' cuerpo_procedimiento '}' ';'",
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
"asignacion : ID '=' expresion error",
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

//#line 172 "gramatica.y"

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


public static void main(String args[]){
	Parser parser = new Parser();
	System.out.println(parser.yyparse());
	System.out.println(parser.getErrores());
	System.out.println(parser.getTokens());
	System.out.println(parser.getEstructuras());
	System.out.println(parser.getAnalizadorLexico().getDatosTabla_simbolos());
}
//#line 586 "Parser.java"
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
case 33:
//#line 54 "gramatica.y"
{addErrorSintactico("Error en la declaracion del bloque de sentencias.");}
break;
case 43:
//#line 70 "gramatica.y"
{addErrorSintactico("Syntax error");}
break;
case 44:
//#line 73 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia imprimir por pantalla.");}
break;
case 45:
//#line 74 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ) de mas en el lado derecho");}
break;
case 46:
//#line 75 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla: caracter ( de mas en el lado izquierdo");}
break;
case 47:
//#line 76 "gramatica.y"
{addErrorSintactico("Error al imprimir por pantalla");}
break;
case 52:
//#line 87 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion variables.");}
break;
case 53:
//#line 90 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento con parametros.");}
break;
case 54:
//#line 91 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Invocacion a procedimiento sin parametros.");}
break;
case 55:
//#line 92 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 56:
//#line 93 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 57:
//#line 94 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
break;
case 58:
//#line 95 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
break;
case 59:
//#line 96 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta )");}
break;
case 60:
//#line 97 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: error en la lista de parametros ");}
break;
case 61:
//#line 98 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: falta (");}
break;
case 62:
//#line 99 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento: hay un ( de mas del lado izquierdo");}
break;
case 63:
//#line 100 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay un ) de mas del lado derecho");}
break;
case 64:
//#line 103 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
break;
case 65:
//#line 104 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
break;
case 66:
//#line 105 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 67:
//#line 106 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 68:
//#line 107 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 69:
//#line 108 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 70:
//#line 109 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 71:
//#line 110 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 72:
//#line 111 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 73:
//#line 112 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 74:
//#line 113 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 75:
//#line 114 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 76:
//#line 115 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 77:
//#line 116 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 78:
//#line 117 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 92:
//#line 141 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 93:
//#line 142 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 94:
//#line 145 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 95:
//#line 146 "gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 96:
//#line 147 "gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 97:
//#line 148 "gramatica.y"
{addErrorSintactico("Error de asignación.");}
break;
case 107:
//#line 166 "gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 108:
//#line 168 "gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 989 "Parser.java"
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
