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
    9,    8,    9,    9,   10,   10,   13,   12,   13,   13,
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
    0,   43,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   88,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   92,   91,   90,   60,    0,   59,   56,
    0,   57,   58,    0,   52,   96,   97,    0,    0,   34,
    0,    0,    0,    0,   44,   45,    0,    0,    0,    0,
    0,    0,    0,   89,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   61,   62,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   84,
    0,    0,    0,    0,   22,    0,    0,    0,    0,    0,
    0,    0,   14,    0,    0,    0,    0,    0,    0,    0,
   87,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   23,    0,   21,    0,    0,    0,
    0,    0,    0,   24,   19,    0,   15,   13,    0,   16,
   12,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   25,    0,   26,    0,    0,   17,   18,   80,
   81,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   65,    0,   67,    0,   66,    0,   68,   69,
   70,   64,    0,    0,    0,    0,    0,    0,    0,   28,
    0,    0,    0,    0,   72,   74,   71,   73,   75,   76,
   77,   63,   29,   27,    0,    0,   30,   20,   31,   32,
};
final static short yydgoto[] = {                          9,
   10,  293,  136,   46,   47,   13,  137,   14,   15,   16,
   17,   18,   19,   37,   20,   64,  101,   43,  295,   65,
  102,   44,   45,
};
final static short yysindex[] = {                       -94,
   83,  -33,  -29,    0,    0,    5, -193,  -25,    0,    0,
  -94,  -94,    0,    0,    0,    0,    0,    0,    0, -243,
    0,  -36,  -36,  -40,   -9,  -34,  -36,  -37,   34,    6,
   10,  -12,  -41,    0,    0,   -6,   20,    0,    0,    0,
 -177,   41,    0,   69,    0,   73,  -23,  -20,  -36,   78,
    0,   85, -134,  110,  -11,  -36,  126,    7,   11,  -39,
  123,  127,  120,  146,  155,   -1,   21,  -15,    8,  143,
   51, -243,    0,    0,    0,  -36,  -36,  -36,  -36,   80,
  -36,  -36,  -36,  -36,  -36,  -36,   81, -129,  165,  -18,
   24,  166,  -59,   86,  -57,  170,   23, -100,  -56,  -52,
  173,  177,  -49,  183,  -10, -222,   25,  -42, -169,    0,
  198, -158,  228,    0,  229,  248,  233,    0,  234,   26,
    0,    0,    0,   31,   33,    0,    0, -129,   46,   46,
   46,   46,   46,   46, -129, -129,  171,  175,  179, -129,
  241,    0,  244,  182, -129,  190,   47,  192,  194,   52,
   48,  259,    0,   53,  -96,  262,   55,  264,   57,  266,
  -51,   59, -174,    0,    0,    0,    0,  285,    0,    0,
  271,    0,    0,  272,    0,    0,    0,  207,  208,    0,
 -146, -129, -129,  209,    0,    0, -129,  210, -129,  213,
 -129, -129,  214,    0,  -21,  277,  295,  -21,  279,  -21,
  280,  -21,  -21,   -3,  281,  282,  -44, -158,    0,    0,
 -105,  -86,  286,  221,  222,  223, -124,  224,  287,  225,
 -129,  226,  227, -129,  230,  -21,  -96,  231,  -21,  232,
  -21,  237,  238,  239,  243,  -21,  -21,  -21,   -2,    0,
  297,  246,  298,  247,    0, -129,  -81,  -79,  305,  312,
  249,  314,    0,  315,  250,  317,  318,  253,  -94,  256,
    0,  -94,  257,  -94,  258,  -94,  -94,  -94,  -94,  260,
  261,  263,  265,  267,    0, -129,    0, -129,  268,  323,
  269,  326,  273,    0,    0, -129,    0,    0,  328,    0,
    0,  330,  -94,  -94,  270,  -94,  274,  -94,  276,  -94,
  278,  283,  284,  288,  -94,  -94,  -94,  -94,  -94,  289,
  290,  135,    0, -129,    0, -129,  291,    0,    0,    0,
    0,  332,  292,  339,  293,  343,  294,  345,  346,  347,
  348,  296,  299,  300,  301,  302,  151,  152,  353,  303,
  304, -103,    0,  361,    0,  364,    0,  371,    0,    0,
    0,    0,  372,  373,  374,  375,  376,  377,  378,    0,
  180,  181,  379,  382,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  383,  384,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  444,  445,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  387,    0,    0,    0,    0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   28,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  406,  407,
  408,  409,  410,  412,    0,  331,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   29,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   30,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  333,  334,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  178,  118,    1,   32,   39,    0,  176,    0,    0,    0,
    0,    0,    0,  385,   -5,    9,  132,  -43, -160,  -93,
 -130,  121,  122,
};
final static int YYTABLESIZE=492;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   12,  106,   56,   41,   41,   53,   24,   41,   41,  204,
   26,   12,   12,   98,   32,   98,  239,   98,  168,   76,
   87,   77,  139,   41,  197,  117,   36,   67,   68,   95,
  159,   98,   98,  160,   98,   33,   86,   72,   85,  113,
   69,   41,   41,  118,   28,   60,  161,   99,  120,   51,
   63,  103,  100,  100,  100,   50,  108,  114,   54,   57,
   42,  115,   29,  150,  141,  163,  174,   82,   85,   83,
   86,   71,   78,   58,   78,  116,   30,   79,   73,   79,
   89,  206,  142,   76,  175,   77,  165,   96,   76,  176,
   77,  177,  151,   76,  207,   77,  261,   61,   74,   75,
  166,  297,   88,  299,  140,  301,  302,  303,  304,  122,
   78,   62,  213,   80,  240,   79,  214,   11,   90,  129,
  130,  131,  132,  133,  134,   91,    1,    2,   11,   11,
    3,  249,  320,  321,  250,  323,    6,  325,  251,  327,
    8,   21,   92,   22,  332,  333,  334,  335,  336,  100,
   93,  225,  363,  241,  228,  364,  230,  242,  232,  233,
  235,    1,    2,    4,    5,    3,   97,    4,    5,    4,
    5,    6,  243,    7,   98,    8,  244,  280,  110,  282,
  108,  281,  260,  283,  109,  263,  111,  265,   34,   35,
  104,  107,  270,  271,  272,  274,  124,  125,  112,  126,
  127,  121,  128,  135,  203,  138,  143,  144,  145,  146,
  147,  238,  152,  154,   70,   48,  105,  153,   55,  156,
  155,  100,   23,  157,    4,    5,   25,  164,   38,   38,
   31,   98,   38,   38,   39,   39,   40,   40,   39,   39,
   40,   40,   52,   66,   98,   98,   98,   98,   81,   82,
   83,   84,  234,  273,   39,   94,  167,   62,  158,  294,
   27,   59,  294,  119,  294,   61,  294,  294,  294,  294,
    4,    5,   39,   39,    4,    5,   61,   98,  148,   62,
  162,   98,   82,   85,   83,   86,  169,  170,  171,  149,
   62,  172,  173,  294,  294,  181,  294,  182,  294,  185,
  294,  183,  186,  178,  187,  294,  294,  294,  294,  294,
  179,  180,  189,  190,  191,  184,  192,  194,  193,  195,
  188,  196,  198,  199,  200,  201,  202,  205,  208,  209,
  210,  211,  212,  217,  219,  221,  224,  226,  227,  229,
  231,  236,  237,  246,  245,  253,  247,  248,  252,  254,
  256,  257,  259,  262,  264,  275,  277,  215,  216,  266,
  267,  268,  218,  284,  220,  269,  222,  223,  276,  278,
  285,  286,  287,  288,  289,  290,  291,  292,  296,  298,
  300,  313,  305,  306,  315,  307,  318,  308,  319,  309,
  343,  314,  312,  339,  322,  316,  255,  345,  324,  258,
  326,  347,  328,  349,  350,  351,  352,  329,  330,  358,
  359,  360,  331,  337,  338,  342,  344,  346,  348,  365,
  353,  279,  366,  354,  355,  356,  357,  361,  362,  367,
  368,  369,  370,  371,  372,  373,  374,  377,  375,  376,
  378,  379,  380,    2,    3,   47,    9,    6,    7,    8,
   10,  310,   11,  311,    0,   33,  123,   78,   79,    0,
    0,  317,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  340,
    0,  341,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   41,   40,   45,   45,   40,   40,   45,   45,   61,
   40,   11,   12,   41,   40,   43,   61,   45,  112,   43,
   41,   45,   41,   45,  155,   41,  270,   40,   41,   41,
   41,   59,   60,  256,   62,   61,   60,   44,   62,   41,
   32,   45,   45,   59,   40,   40,  269,   41,   41,   59,
   41,   41,   58,   59,   60,   24,   58,   59,   27,   28,
   22,   41,  256,   41,   41,   41,   41,   41,   41,   41,
   41,   33,   42,   40,   42,   67,  270,   47,   59,   47,
   49,  256,   59,   43,   59,   45,  256,   56,   43,   59,
   45,   59,   98,   43,  269,   45,  227,  256,  276,   59,
  270,  262,  123,  264,  123,  266,  267,  268,  269,   59,
   42,  270,  259,   41,  208,   47,  263,    0,   41,   81,
   82,   83,   84,   85,   86,   41,  256,  257,   11,   12,
  260,  256,  293,  294,  259,  296,  266,  298,  263,  300,
  270,   59,  277,   61,  305,  306,  307,  308,  309,  155,
   41,  195,  256,  259,  198,  259,  200,  263,  202,  203,
  204,  256,  257,  264,  265,  260,   41,  264,  265,  264,
  265,  266,  259,  268,  271,  270,  263,  259,   59,  259,
   58,  263,  226,  263,   58,  229,   41,  231,   11,   12,
   59,   60,  236,  237,  238,  239,   76,   77,   44,   78,
   79,   59,  123,  123,  256,   41,   41,  267,  123,  267,
   41,  256,  269,   41,  256,  256,  256,  270,  256,  269,
   44,  227,  256,   41,  264,  265,  256,  270,  270,  270,
  256,  271,  270,  270,  276,  276,  278,  278,  276,  276,
  278,  278,  277,  256,  272,  273,  274,  275,  272,  273,
  274,  275,  256,  256,  276,  267,   59,  270,  269,  259,
  256,  256,  262,  256,  264,  256,  266,  267,  268,  269,
  264,  265,  276,  276,  264,  265,  256,  271,  256,  270,
  256,  271,  256,  256,  256,  256,   59,   59,   41,  267,
  270,   59,   59,  293,  294,  125,  296,  123,  298,   59,
  300,  123,   59,  128,  123,  305,  306,  307,  308,  309,
  135,  136,  123,  267,  123,  140,  123,  270,  267,   61,
  145,  269,   61,  269,   61,  269,   61,  269,   44,   59,
   59,  125,  125,  125,  125,  123,  123,   61,   44,   61,
   61,   61,   61,  123,   59,   59,  125,  125,  125,  125,
  125,  125,  123,  123,  123,   59,   59,  182,  183,  123,
  123,  123,  187,   59,  189,  123,  191,  192,  123,  123,
   59,  123,   59,   59,  125,   59,   59,  125,  123,  123,
  123,   59,  123,  123,   59,  123,   59,  123,   59,  123,
   59,  123,  125,  259,  125,  123,  221,   59,  125,  224,
  125,   59,  125,   59,   59,   59,   59,  125,  125,  259,
  259,   59,  125,  125,  125,  125,  125,  125,  125,   59,
  125,  246,   59,  125,  125,  125,  125,  125,  125,   59,
   59,   59,   59,   59,   59,   59,   59,   59,  259,  259,
   59,   59,   59,    0,    0,   59,   41,   41,   41,   41,
   41,  276,   41,  278,   -1,  125,   72,  125,  125,   -1,
   -1,  286,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  314,
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
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF error condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' '{' bloque_sentencias_control '}' error ';'",
"clausula_seleccion : IF '(' '(' condicion ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' condicion ')' ')' '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error ')' '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
"clausula_seleccion : IF '(' error '{' bloque_sentencias_control '}' ELSE '{' bloque_sentencias_control '}' END_IF ';'",
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
//#line 585 "Parser.java"
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
{estructuras.add("ERRROOOR");}
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
//#line 984 "Parser.java"
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
