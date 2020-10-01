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
   10,   10,   10,   10,   10,   10,   10,   19,   19,   19,
   19,   16,   16,   16,   17,   17,   17,   21,   21,   20,
   20,   20,   11,   11,   11,    5,    5,    5,   22,   22,
   22,   23,   23,   23,   18,   18,
};
final static short yylen[] = {                            2,
    1,    1,    1,    2,    2,    3,    3,    3,    3,    3,
    3,    9,    9,    8,    9,    9,   10,   10,    9,   13,
    9,    9,    9,    9,   10,   10,   13,   13,   13,   13,
   14,   14,    1,    2,    1,    1,    1,    1,    1,    1,
    1,    2,    5,    6,    6,    3,    1,    3,    1,    1,
    3,    5,    4,    4,    4,    5,    5,    5,    5,    5,
    6,    6,   12,   11,   11,   11,   11,   11,   11,   11,
   12,   12,   12,   12,   12,   12,   12,    1,    1,    2,
    2,    1,    3,    5,    1,    3,    5,    2,    3,    3,
    3,    3,    4,    4,    4,    4,    4,    1,    3,    3,
    1,    1,    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   49,   50,    0,    0,    0,    0,    1,
    0,    0,   39,   38,   35,   36,   37,   40,   41,    0,
   42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    0,  102,  105,  104,
    0,    0,  103,    0,  101,    0,    0,    0,    0,    0,
   46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   51,  106,   95,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   55,
    0,    0,    0,   54,    0,    0,    0,   53,    0,    0,
   94,   93,   48,    0,    0,   99,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   88,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   92,   91,   90,   60,    0,   59,   56,    0,
   57,   58,    0,   52,   96,   97,    0,    0,    0,    0,
    0,    0,    0,   44,   45,    0,    0,    0,    0,    0,
    0,    0,   89,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   61,   62,   34,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   84,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   14,    0,    0,    0,    0,    0,    0,    0,   87,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   23,    0,   21,    0,    0,    0,   22,
    0,    0,    0,   24,   19,    0,   15,   13,    0,   16,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,    0,    0,   26,    0,    0,   17,   18,   80,
   81,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   65,    0,   67,    0,   66,    0,   68,   69,
   70,   64,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   72,   74,   71,   73,   75,   76,
   77,   63,   29,   27,    0,   28,    0,   30,   20,   31,
   32,
};
final static short yydgoto[] = {                          9,
   10,  293,  177,   46,   47,   13,  178,   14,   15,   16,
   17,   18,   19,   37,   20,   64,  101,   43,  295,   65,
  102,   44,   45,
};
final static short yysindex[] = {                       -96,
   54,   22,   24,    0,    0,   25, -219,  -33,    0,    0,
  -96,  -96,    0,    0,    0,    0,    0,    0,    0, -239,
    0,  -26,  -26,  -40,   67,  -34,  -26,  -37,   19,   26,
   -7,    4,  -36,    0,    0,  100,  112,    0,    0,    0,
 -185,   47,    0,  120,    0,  118,  -19,  137,  -26,   27,
    0,  140,  -75,  156,  -30,  -26,  157,    6,    8,  -39,
  143,  145,  147,  163,  164,   -2,    5,  -11,   28,  148,
   88, -239,    0,    0,    0,  -26,  -26,  -26,  -26,   86,
  -26,  -26,  -26,  -26,  -26,  -26,   89,  170,   90,  -20,
   20,  173,  -49,   99,  -43,  186,    9, -112,   43,  -42,
  259,  258,   45,  262,  -12, -151,   29,   46, -218,    0,
  245, -174,  246,    0,  260,  276,  263,    0,  264,   39,
    0,    0,    0,   78,  104,    0,    0,  -81,   -3,   -3,
   -3,   -3,   -3,   -3,  -81,  195,  -81,  198,  -81,  265,
    0,  266,  203,  -81,  204,   61,  206,  207,   64,   62,
  272,    0,   65, -155,  274,   68,  275,   69,  278,  -51,
   71, -145,    0,    0,    0,    0,  297,    0,    0,  283,
    0,    0,  284,    0,    0,    0,  -81,  219,  220,  -81,
  221,  -81,  222,    0,    0,  -81,  223,  -81,  226,  -81,
  -81,  227,    0,  -18,  290,  308,  -18,  293,  -18,  294,
  -18,  -18,  -41,  296,  298,  -46, -174,    0,    0,    0,
  -86,  -79,  235,  -77,  236, -196,  238,  306,  243,  -81,
  244,  247,  -81,  248,  -18, -155,  250,  -18,  251,  -18,
  252,  253,  254,  255,  -18,  -18,  -18,  -25,    0,  311,
  256,  321,  261,  -76,  322,  267,  -71,  323,  324,  268,
  326,    0,  327,  269,  328,  329,  270,  -96,  277,    0,
  -96,  279,  -96,  280,  -96,  -96,  -96,  -96,  281,  282,
  285,  286,  287,    0,  -81,    0,  -81,  330,  288,    0,
  -81,  333,  289,    0,    0,  -81,    0,    0,  334,    0,
    0,  338,  -96,  -96,  273,  -96,  291,  -96,  292,  -96,
  295,  299,  300,  301,  -96,  -96,  -96,  -96,  -96,  302,
  303,    0,  -81,  304,    0,  -81,  305,    0,    0,    0,
    0,  342,  307,  347,  309,  348,  310,  354,  355,  356,
  359,  312,  313,  314,  315,  316,  160,  162,  317,  172,
  318, -121,    0,  363,    0,  364,    0,  374,    0,    0,
    0,    0,  377,  385,  386,  387,  388,  389,  390,  191,
  393,  196,  395,  397,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  399,    0,  400,    0,    0,    0,
    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  460,  461,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  404,    0,    0,    0,    0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   30,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   31,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   32,   33,
   34,   35,   36,   37,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   40,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  339,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   42,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  340,  341,    0,    0,    0,    0,    0,    0,
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
  179,  154,    1,   80,    3,    0,  176,    0,    0,    0,
    0,    0,    0,  396,   -5,   -9,  134,  -80, -166,  -95,
 -132,  119,  121,
};
final static int YYTABLESIZE=492;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   12,  106,   56,   41,   41,   53,   32,   41,   41,  203,
   95,   12,   12,   98,  238,   98,  167,   98,   41,   41,
  138,  196,   69,   76,   42,   77,   41,   33,  158,  117,
   36,   98,   98,   63,   98,   71,   29,  164,  113,   76,
   86,   77,   85,   67,   68,  115,   99,  118,  103,  149,
   30,  165,  100,  100,  100,  108,  114,  116,   58,  248,
  140,   24,  249,   26,   28,   60,  250,   90,  120,  162,
   82,   85,    9,    6,    7,    8,   10,   11,  141,  173,
   83,   61,   86,  129,  130,  131,  132,  133,  134,   76,
   74,   77,  150,  260,  297,   62,  299,  174,  301,  302,
  303,  304,  139,   50,  159,   75,   54,   57,    4,    5,
  205,  239,   21,  224,   22,   98,  227,  160,  229,   78,
  231,  232,  234,  206,   79,   51,  320,  321,   88,  323,
   76,  325,   77,  327,  363,   96,  175,  364,  332,  333,
  334,  335,  336,   72,  259,   78,  122,  262,  100,  264,
   79,    4,    5,   11,  269,  270,  271,  273,   80,    1,
    2,   78,  176,    3,   11,   11,   79,    4,    5,    6,
   73,    7,  240,    8,    1,    2,  241,   87,    3,  242,
   91,  245,  278,  243,    6,  246,  279,  282,    8,   34,
   35,  283,  104,  107,  124,  125,   93,   97,  126,  127,
  108,   92,  109,  111,  202,  110,  121,  112,  128,  237,
  136,  135,  137,  142,  233,   48,  105,  143,   55,   70,
  100,  144,   31,  145,    4,    5,  146,  152,   98,   38,
  272,   98,   38,   38,   39,   39,   94,   40,   39,   39,
   40,   40,   52,   38,   98,   98,   98,   98,   61,   39,
   39,   40,   81,   82,   83,   84,  157,   39,  294,   66,
   61,  294,   62,  294,  147,  294,  294,  294,  294,    4,
    5,    4,    5,   62,   62,  148,   98,   23,   98,   25,
   27,   59,   89,  119,  161,   82,   85,    9,    6,    7,
    8,   10,   11,  294,  294,   83,  294,   86,  294,  153,
  294,  154,  156,  166,  168,  294,  294,  294,  294,  294,
  179,  151,  181,  155,  183,  163,  170,  180,  169,  187,
  182,  171,  172,  184,  185,  186,  188,  189,  190,  191,
  192,  193,  194,  195,  197,  199,  198,  200,  201,  204,
  207,  208,  209,  211,  212,  214,  216,  218,  220,  223,
  225,  226,  210,  228,  230,  213,  235,  215,  236,  244,
  247,  217,  251,  219,  252,  221,  222,  253,  255,  274,
  258,  256,  261,  263,  265,  266,  267,  268,  275,  276,
  280,  284,  285,  277,  287,  288,  290,  291,  312,  281,
  286,  315,  318,  289,  292,  254,  319,  322,  257,  296,
  343,  298,  300,  305,  306,  345,  347,  307,  308,  309,
  313,  316,  349,  350,  351,  324,  326,  352,  358,  328,
  359,  365,  366,  329,  330,  331,  337,  338,  340,  342,
  361,  344,  367,  346,  348,  368,  353,  354,  355,  356,
  357,  360,  362,  369,  370,  371,  372,  373,  374,  375,
  310,  376,  311,  378,  377,  379,  314,  380,  381,    2,
    3,  317,   47,   33,   78,   79,    0,  123,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  339,    0,
    0,  341,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   40,   45,   45,   40,   40,   45,   45,   61,
   41,   11,   12,   41,   61,   43,  112,   45,   45,   45,
   41,  154,   32,   43,   22,   45,   45,   61,   41,   41,
  270,   59,   60,   41,   62,   33,  256,  256,   41,   43,
   60,   45,   62,   40,   41,   41,   41,   59,   41,   41,
  270,  270,   58,   59,   60,   58,   59,   67,   40,  256,
   41,   40,  259,   40,   40,   40,  263,   41,   41,   41,
   41,   41,   41,   41,   41,   41,   41,   41,   59,   41,
   41,  256,   41,   81,   82,   83,   84,   85,   86,   43,
  276,   45,   98,  226,  261,  270,  263,   59,  265,  266,
  267,  268,  123,   24,  256,   59,   27,   28,  264,  265,
  256,  207,   59,  194,   61,  271,  197,  269,  199,   42,
  201,  202,  203,  269,   47,   59,  293,  294,   49,  296,
   43,  298,   45,  300,  256,   56,   59,  259,  305,  306,
  307,  308,  309,   44,  225,   42,   59,  228,  154,  230,
   47,  264,  265,    0,  235,  236,  237,  238,   41,  256,
  257,   42,   59,  260,   11,   12,   47,  264,  265,  266,
   59,  268,  259,  270,  256,  257,  263,   41,  260,  259,
   41,  259,  259,  263,  266,  263,  263,  259,  270,   11,
   12,  263,   59,   60,   76,   77,   41,   41,   78,   79,
   58,  277,   58,   41,  256,   59,   59,   44,  123,  256,
   41,  123,  123,   41,  256,  256,  256,  267,  256,  256,
  226,  123,  256,  267,  264,  265,   41,  270,  256,  270,
  256,  271,  270,  270,  276,  276,  267,  278,  276,  276,
  278,  278,  277,  270,  272,  273,  274,  275,  256,  276,
  276,  278,  272,  273,  274,  275,  269,  276,  258,  256,
  256,  261,  270,  263,  256,  265,  266,  267,  268,  264,
  265,  264,  265,  270,  270,  267,  271,  256,  271,  256,
  256,  256,  256,  256,  256,  256,  256,  256,  256,  256,
  256,  256,  256,  293,  294,  256,  296,  256,  298,   41,
  300,   44,   41,   59,   59,  305,  306,  307,  308,  309,
  135,  269,  137,  269,  139,  270,   41,  123,   59,  144,
  123,   59,   59,   59,   59,  123,  123,  267,  123,  123,
  267,  270,   61,  269,   61,   61,  269,  269,   61,  269,
   44,   59,   59,  125,  125,  125,  125,  125,  123,  123,
   61,   44,  177,   61,   61,  180,   61,  182,   61,  125,
  125,  186,  125,  188,   59,  190,  191,  125,  125,   59,
  123,  125,  123,  123,  123,  123,  123,  123,  123,   59,
   59,   59,   59,  123,   59,   59,   59,   59,   59,  123,
  123,   59,   59,  125,  125,  220,   59,  125,  223,  123,
   59,  123,  123,  123,  123,   59,   59,  123,  123,  123,
  123,  123,   59,   59,   59,  125,  125,   59,  259,  125,
  259,   59,   59,  125,  125,  125,  125,  125,  125,  125,
  259,  125,   59,  125,  125,   59,  125,  125,  125,  125,
  125,  125,  125,   59,   59,   59,   59,   59,   59,  259,
  275,   59,  277,   59,  259,   59,  281,   59,   59,    0,
    0,  286,   59,  125,  125,  125,   -1,   72,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  313,   -1,
   -1,  316,
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
"clausula_seleccion : IF '(' condicion error '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
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

//#line 170 "gramatica.y"

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
//#line 588 "Parser.java"
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
case 6:
//#line 23 "gramatica.y"
{System.out.println("SI ES ACA");}
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
case 71:
//#line 110 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 72:
//#line 111 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 73:
//#line 112 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 74:
//#line 113 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 75:
//#line 114 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 76:
//#line 115 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 77:
//#line 116 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 91:
//#line 140 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 92:
//#line 141 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 93:
//#line 144 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 94:
//#line 145 "gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 95:
//#line 146 "gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 105:
//#line 164 "gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 106:
//#line 166 "gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 987 "Parser.java"
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
