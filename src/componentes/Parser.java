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
    4,    4,    6,    6,    6,    6,    6,    6,    6,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    7,    7,    2,    2,    3,    3,    3,
    3,    3,    3,   12,   12,   12,   12,   14,   14,   15,
   15,    9,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   10,   10,   10,   10,   10,   10,   10,
   10,   10,   10,   19,   19,   19,   19,   16,   16,   16,
   16,   17,   17,   17,   17,   21,   21,   20,   20,   20,
   11,   11,   11,    5,    5,    5,   22,   22,   22,   23,
   23,   23,   18,   18,
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
   12,   14,   14,    1,    1,    2,    2,    1,    3,    5,
    7,    1,    3,    5,    7,    2,    3,    3,    3,    3,
    4,    4,    4,    3,    3,    1,    3,    3,    1,    1,
    1,    1,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   50,   51,    0,    0,    0,    0,    1,
    0,    0,   40,   39,   36,   37,   38,   41,   42,    0,
   43,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    4,    5,    0,    0,  110,  113,  112,
    0,    0,  111,    0,  109,    0,    0,    0,    0,    0,
   47,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   52,  114,  103,    0,    0,    0,    0,
    0,   12,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   56,    0,    0,    0,   55,    0,    0,   54,
    0,    0,  102,  101,   49,    0,    0,  107,  108,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   44,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   96,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  100,   99,   98,
   61,    0,   60,   57,    0,    0,   59,    0,   53,    0,
    0,   35,    0,    0,    0,    0,   45,   46,    0,    0,
    0,    0,    0,    0,    0,   97,    0,    0,    0,    0,
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
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   91,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   86,   87,    0,    0,   95,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,    0,   27,    0,
    0,   18,   19,   67,    0,   69,    0,   68,    0,    0,
    0,   72,   73,   74,   66,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   29,    0,    0,    0,    0,   76,
   78,   75,    0,    0,    0,   77,   79,   80,   81,   65,
    0,   30,   28,    0,    0,   31,   21,   70,    0,   71,
    0,    0,    0,   82,   83,   32,   33,
};
final static short yydgoto[] = {                          9,
   10,  309,  310,   46,   47,   13,  139,   14,   15,   16,
   17,   18,   19,   37,   20,   65,  103,   43,  311,   66,
  104,   44,   45,
};
final static short yysindex[] = {                       -79,
  148,  -23,   40,    0,    0,   48, -186,   18,    0,    0,
  -79,  -79,    0,    0,    0,    0,    0,    0,    0, -245,
    0,  -26,  -26,  -40,   -9,  -38,  -26,  -36,    4,   50,
   24,   37,    3,    0,    0,   38,   55,    0,    0,    0,
 -153,   68,    0,  119,    0,  129,   14,   -4,  -43,  177,
    0,  184,  -50,  185,  -10,  -28,  190,   34,   44,   20,
  205,  217,  231,    0,  251,  256,   13,   70,   22,   15,
  243,  112, -245,    0,    0,    0,  -26,  -26,  -26,  -26,
  201,    0,  -26,  -26,  -26,  -26,  -26,  -26,  204,  -98,
  -26,   -2,   53,  288,   66,  208,   71,  -26,  -35, -100,
   72,   67,  291,  295,   73,  299,  -18,   87,  -34,   27,
   74, -152,    0,  286, -110,  287,    0,  -51,   91,    0,
  289,   69,    0,    0,    0,  119,  119,    0,    0,  -98,
  165,  165,  165,  165,  165,  165,  -98,  -98,  224,  309,
   95,  -98,  293,    0,  294,  232,  -98,  233,  313,  234,
  235,  103,   90,  301,    0,   96,  -91,  306,   99,  311,
  101,   46,  312,   63,  122,  110,    8,    0,    0,    0,
    0,  336,    0,    0,  340,  323,    0,  127,    0,  259,
  260,    0, -217,  264,  265,  261,    0,    0,  -98,  266,
  -98,  123,  -98,  -98,  125,    0,  -15,  328,  349,  -15,
  333,  -15,  334,  130,  355,  -15,  -15,    2,  131,  337,
  341,   64,  141, -110,  342,    0,  344, -190, -114,  345,
  282,  -98,  -98, -130,  281,  350,  283,  292,  285,  296,
  300,  303,  -15,  -91,  304,  -15,  305,  -15,  351,  142,
  307,  308,  310,  314,  353,  -15,  -15,  -15,    6,  147,
  373,    0,    0,  359,  315,  360,  316,    0,  -98,  297,
  317,  365,  370,  318,  375,    0,  376,  -98,  377,  381,
  -98,  -79,  320,  388,  -79,  321,  -79,  322,  -15,  385,
  -79,  -79,  -79,  -79,  -15,  325,  326,  327,  329,  330,
  390,  198,    0,  -98,    0,  -98,  331,  -80,  -75,    0,
    0,  -98,    0,    0,  332,    0,    0,  335,  -79,  -79,
  338,  -79,  199,  339,  -79,  343,  -79,  346,  -15,  347,
  348,  352,  354,  357,  -79,  -79,  -79,  -79,  -79,  -15,
    0,  356,  358,  200,  402,  361,  403,  362,  363,  406,
  407,    0,    0,  408,  364,    0,  411,  366,  412,  367,
  -79,  371,  415,  416,  417,  419,  -79,  368,  374,  378,
  379,  380,  372,  223,  227,  428,    0,  -98,    0,  -98,
  -63,    0,    0,    0,  431,    0,  437,    0,  439,  382,
  -79,    0,    0,    0,    0,  383,  441,  442,  443,  447,
  450,  -79,  451,  452,    0,  387,  389,  454,  456,    0,
    0,    0,  457,  392,  459,    0,    0,    0,    0,    0,
  394,    0,    0,  262,  267,    0,    0,    0,  461,    0,
  463,  464,  465,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  525,  527,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  469,    0,    0,    0,    0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   45,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   56,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -19,   -7,    0,    0,    0,
  488,  489,  490,  491,  492,  493,    0,  410,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   57,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   58,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   60,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  413,  414,
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
  192,  105,    1,   65,  114,    0, -127,    0,    0,    0,
    0,    0,    0,  467,  263,   -3,  -32,  128, -175,  -94,
 -112,  134,  135,
};
final static int YYTABLESIZE=540;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   12,   53,  180,   56,   41,  152,  165,  174,   41,  181,
  182,   12,   12,  106,  186,  106,   24,  106,   41,  190,
  172,  104,  161,  104,   36,  104,  106,  110,   70,   41,
   97,  106,  106,  105,  106,  105,   89,  105,  141,  104,
  104,  220,  104,   58,  199,  221,   41,   41,  213,   51,
   41,  105,  105,  116,  105,  122,   77,   32,   78,  108,
  109,  225,  119,  227,   63,  229,  230,  167,  254,   29,
  111,  117,  255,   88,  101,   87,   68,   69,   33,   26,
  120,   73,   64,   30,  105,   88,  204,   28,   50,   60,
  138,   54,   57,  143,  260,  261,   92,   89,   93,  314,
   90,  316,   94,  169,   11,  320,  321,  322,  323,  178,
   77,  144,   78,   74,  175,   11,   11,  170,   90,  251,
  142,  274,   75,  208,  249,  262,   76,  179,  263,  205,
  138,  297,  264,  342,  343,   42,  345,  138,  138,  348,
  305,  350,  138,  308,  256,   61,   72,  138,  257,  358,
  359,  360,  361,  362,   77,  140,   78,    1,    2,   62,
   79,    3,  149,    4,    5,   80,  332,    6,  333,   81,
  124,    8,    4,    5,  339,  380,    1,    2,  335,  100,
    3,  386,  336,  337,    4,    5,    6,  338,    7,  138,
    8,  138,  398,  138,  138,  399,  131,  132,  133,  134,
  135,  136,   34,   35,   61,  404,   21,   77,   22,   78,
  126,  127,   91,  128,  129,   48,  411,   92,   62,   55,
  150,  163,  138,  138,   93,   95,   94,   98,  106,   38,
   99,  151,   23,   38,  164,   39,  104,   40,   52,   39,
  396,   40,  397,   38,  106,  106,  106,  106,  105,   39,
  160,   40,  104,  104,  104,  104,   96,  243,   71,  138,
   39,  289,  111,  211,  105,  105,  105,  105,  138,   82,
  121,  138,   38,   31,  112,  107,  212,   39,   39,   61,
   40,   39,  166,    4,    5,   83,   84,   85,   86,  113,
  100,  114,   67,   62,  138,   25,  138,    4,    5,  115,
   88,  123,  138,   27,  100,   59,   62,    4,    5,    4,
    5,   92,   89,   93,  100,   90,  100,   94,  207,  248,
  102,  102,  102,  130,  232,  118,  137,  235,  145,  237,
  147,  156,  146,  241,  242,  244,  155,  148,  157,  159,
  154,  158,  162,  168,  171,  173,  176,  177,  183,  184,
  185,  187,  188,  192,  189,  191,  193,  194,  195,  196,
  273,  197,  153,  276,  198,  278,  200,  201,  138,  203,
  138,  202,  206,  286,  287,  288,  290,  209,  210,  214,
  215,  216,  217,  218,  219,  224,  222,  223,  233,  228,
  226,  231,  234,  236,  238,  240,  250,  246,  239,  245,
  252,  247,  253,  258,  259,  265,  318,  267,  266,  269,
  280,  279,  324,  285,  268,  291,  292,  293,  295,  102,
  270,  298,  271,  300,  102,  272,  275,  277,  301,  281,
  282,  313,  283,  303,  304,  306,  284,  294,  296,  307,
  302,  299,  312,  315,  317,  319,  352,  325,  326,  327,
  330,  328,  329,  331,  346,  334,  340,  363,  366,  341,
  367,  369,  344,  347,  372,  373,  374,  349,  351,  376,
  378,  353,  354,  382,  383,  384,  355,  385,  356,  357,
  364,  393,  365,  368,  370,  394,  395,  371,  375,  400,
  377,  379,  387,  381,  392,  401,  102,  402,  388,  406,
  407,  408,  389,  390,  391,  409,  403,  405,  410,  412,
  413,  414,  416,  415,  417,  418,  419,  420,  421,  424,
  422,  425,  426,  427,    2,  423,    3,   48,    9,    6,
    7,    8,   10,   11,   34,    0,    0,   84,   85,  125,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,  130,   40,   45,   41,   41,   59,   45,  137,
  138,   11,   12,   41,  142,   43,   40,   45,   45,  147,
  115,   41,   41,   43,  270,   45,   59,   60,   32,   45,
   41,   59,   60,   41,   62,   43,   41,   45,   41,   59,
   60,  259,   62,   40,  157,  263,   45,   45,   41,   59,
   45,   59,   60,   41,   62,   41,   43,   40,   45,   40,
   41,  189,   41,  191,   41,  193,  194,   41,  259,  256,
   58,   59,  263,   60,   41,   62,   40,   41,   61,   40,
   59,   44,   59,  270,   41,   41,   41,   40,   24,   40,
   90,   27,   28,   41,  222,  223,   41,   41,   41,  275,
   41,  277,   41,  256,    0,  281,  282,  283,  284,   41,
   43,   59,   45,   59,  118,   11,   12,  270,  123,  214,
  123,  234,  276,   61,   61,  256,   59,   59,  259,  162,
  130,  259,  263,  309,  310,   22,  312,  137,  138,  315,
  268,  317,  142,  271,  259,  256,   33,  147,  263,  325,
  326,  327,  328,  329,   43,   91,   45,  256,  257,  270,
   42,  260,   98,  264,  265,   47,  294,  266,  296,   41,
   59,  270,  264,  265,  302,  351,  256,  257,  259,  271,
  260,  357,  263,  259,  264,  265,  266,  263,  268,  189,
  270,  191,  256,  193,  194,  259,   83,   84,   85,   86,
   87,   88,   11,   12,  256,  381,   59,   43,   61,   45,
   77,   78,  256,   79,   80,  256,  392,   41,  270,  256,
  256,  256,  222,  223,   41,   41,  277,  256,  256,  270,
   41,  267,  256,  270,  269,  276,  256,  278,  277,  276,
  368,  278,  370,  270,  272,  273,  274,  275,  256,  276,
  269,  278,  272,  273,  274,  275,  267,  256,  256,  259,
  276,  256,   58,  256,  272,  273,  274,  275,  268,  256,
  256,  271,  270,  256,   58,  256,  269,  276,  276,  256,
  278,  276,  256,  264,  265,  272,  273,  274,  275,   59,
  271,   41,  256,  270,  294,  256,  296,  264,  265,   44,
  256,   59,  302,  256,  271,  256,  270,  264,  265,  264,
  265,  256,  256,  256,  271,  256,  271,  256,  256,  256,
   58,   59,   60,  123,  197,  256,  123,  200,   41,  202,
  123,   41,  267,  206,  207,  208,  270,  267,   44,   41,
  269,  269,  256,  270,   59,   59,  256,   59,  125,   41,
  256,   59,   59,   41,  123,  123,  123,  123,  256,  270,
  233,   61,  100,  236,  269,  238,   61,  269,  368,  269,
  370,   61,   61,  246,  247,  248,  249,  256,  269,   44,
   41,   59,  256,  125,  125,  125,  123,  123,   61,  267,
  125,  267,   44,   61,   61,   41,  256,   61,  269,  269,
   59,   61,   59,   59,  123,  125,  279,  125,   59,  125,
  269,   61,  285,   61,  123,  269,   44,   59,   59,  157,
  125,  125,  123,   59,  162,  123,  123,  123,   59,  123,
  123,   44,  123,   59,   59,   59,  123,  123,  123,   59,
  123,  125,  123,  123,  123,   61,  319,  123,  123,  123,
   61,  123,  123,  256,  256,  125,  125,  330,  259,  125,
   59,   59,  125,  125,   59,   59,   59,  125,  123,   59,
   59,  125,  125,   59,   59,   59,  125,   59,  125,  123,
  125,  259,  125,  123,  123,  259,   59,  125,  125,   59,
  125,  125,  125,  123,  123,   59,  234,   59,  125,   59,
   59,   59,  125,  125,  125,   59,  125,  125,   59,   59,
   59,  125,   59,  125,   59,   59,  125,   59,  125,   59,
  259,   59,   59,   59,    0,  259,    0,   59,   41,   41,
   41,   41,   41,   41,  125,   -1,   -1,  125,  125,   73,
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
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC error '(' ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' error NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID error ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' '(' error ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' ')' ')' error NI '=' cte '{' cuerpo_procedimiento '}' ';'",
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
"sentencia_declaracion_procedimiento : PROC ID '(' '(' error lista_parametros_declaracion ')' NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"sentencia_declaracion_procedimiento : PROC ID '(' lista_parametros_declaracion ')' ')' error NI '=' cte '{' cuerpo_procedimiento '}' ';'",
"cuerpo_procedimiento : sentencias_declarativas",
"cuerpo_procedimiento : sentencias_ejecutables",
"cuerpo_procedimiento : sentencias_declarativas cuerpo_procedimiento",
"cuerpo_procedimiento : sentencias_ejecutables cuerpo_procedimiento",
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


public static void main(String args[]){
	Parser parser = new Parser();
	System.out.println(parser.yyparse());
	System.out.println(parser.getErrores());
	ArrayList<String> tokens = parser.getTokens();
	for (int i = 0; i<tokens.size();i++) {
		System.out.println(tokens.get(i));
	}
	ArrayList<String> estructuras = parser.getEstructuras();
	for (int i = 0; i<estructuras.size();i++) {
		System.out.println(estructuras.get(i));
	}
	System.out.println(parser.getAnalizadorLexico().getDatosTabla_simbolos());
}
//#line 618 "Parser.java"
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
//#line 29 "gramatica.y"
{addErrorSintactico("Error en la condicion");}
break;
case 13:
//#line 32 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia WHILE LOOP.");}
break;
case 14:
//#line 33 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE.");}
break;
case 15:
//#line 34 "gramatica.y"
{addErrorSintactico("Error en la definicion del WHILE: falta el )");}
break;
case 16:
//#line 35 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta el (.");}
break;
case 17:
//#line 36 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: falta LOOP luego del ).");}
break;
case 18:
//#line 37 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ( de mas del lado izquierdo.");}
break;
case 19:
//#line 38 "gramatica.y"
{addErrorSintactico("Error en la condicion del WHILE: hay uno o mas ) de mas del lado derecho.");}
break;
case 20:
//#line 41 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF sin ELSE");}
break;
case 21:
//#line 42 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia IF con bloque ELSE.");}
break;
case 22:
//#line 43 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF.");}
break;
case 23:
//#line 44 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el )");}
break;
case 24:
//#line 45 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el (");}
break;
case 25:
//#line 46 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: falta el END_IF");}
break;
case 26:
//#line 47 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ( de mas del lado izquierdo");}
break;
case 27:
//#line 48 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF: hay uno o mas ) de mas del lado derecho");}
break;
case 28:
//#line 49 "gramatica.y"
{addErrorSintactico("Error en la condicion del IF ELSE.");}
break;
case 29:
//#line 50 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el )");}
break;
case 30:
//#line 51 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el (");}
break;
case 31:
//#line 52 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: falta el END_IF");}
break;
case 32:
//#line 53 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ( de mas del lado izquierdo");}
break;
case 33:
//#line 54 "gramatica.y"
{addErrorSintactico("Error en la definicion del IF ELSE: hay uno o mas ) de mas del lado derecho");}
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
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 58:
//#line 95 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
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
{addErrorSintactico("Error al invocar procedimiento: hay uno o mas ( de mas del lado izquierdo");}
break;
case 63:
//#line 100 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento:hay uno o mas ) de mas del lado derecho");}
break;
case 64:
//#line 101 "gramatica.y"
{addErrorSintactico("Error al invocar procedimiento.");}
break;
case 65:
//#line 104 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento con parametros.");}
break;
case 66:
//#line 105 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia declaracion procedimiento sin parametros.");}
break;
case 67:
//#line 106 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 68:
//#line 107 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 69:
//#line 108 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 70:
//#line 109 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 71:
//#line 110 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 72:
//#line 111 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 73:
//#line 112 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 74:
//#line 113 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 75:
//#line 114 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: error en la lista de parametros");}
break;
case 76:
//#line 115 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta ID");}
break;
case 77:
//#line 116 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta )");}
break;
case 78:
//#line 117 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta (");}
break;
case 79:
//#line 118 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta NI");}
break;
case 80:
//#line 119 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta =");}
break;
case 81:
//#line 120 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: falta cte");}
break;
case 82:
//#line 121 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ( de  mas. ");}
break;
case 83:
//#line 122 "gramatica.y"
{addErrorSintactico("Error al declarar procedimiento: tiene uno o mas ) de mas. ");}
break;
case 91:
//#line 134 "gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 95:
//#line 141 "gramatica.y"
{addErrorSintactico("Error. El numero maximo de parametros permitido es 3.");}
break;
case 99:
//#line 149 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametro del lado derecho");}
break;
case 100:
//#line 150 "gramatica.y"
{ addErrorSintactico("Error en la definicion de parametros del lado izquierdo");}
break;
case 101:
//#line 153 "gramatica.y"
{estructuras.add("Linea numero: "+(analizadorLexico.getFilaActual()+1) + " --Sentencia asignacion variable.");}
break;
case 102:
//#line 154 "gramatica.y"
{addErrorSintactico("Error de asignación a la derecha.");}
break;
case 103:
//#line 155 "gramatica.y"
{addErrorSintactico("Error de asignación a la izquierda.");}
break;
case 113:
//#line 173 "gramatica.y"
{if (!analizadorLexico.check_rango_longint(val_peek(0).sval)){
                addErrorSintactico("Error longint fuera de rango");}}
break;
case 114:
//#line 175 "gramatica.y"
{analizadorLexico.updateTablaSimbolos(val_peek(0).sval);
               yyval = new ParserVal("-"+val_peek(0).sval);}
break;
//#line 1045 "Parser.java"
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
