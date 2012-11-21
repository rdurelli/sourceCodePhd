// $ANTLR 3.4 /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g 2012-11-15 16:12:39

  //packager of my parser
  package parser;
  //Collection
  import java.util.HashMap;
  import java.util.Set;
  import java.util.HashSet;
  //API to deal with Regular Expression
  import java.util.regex.Matcher;
  import java.util.regex.Pattern; 


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/** A Java 1.5 grammar for ANTLR v3 derived from the spec
 *
 *  This is a very close representation of the spec; the changes
 *  are comestic (remove left recursion) and also fixes (the spec
 *  isn't exactly perfect).  I have run this on the 1.4.2 source
 *  and some nasty looking enums from 1.5, but have not really
 *  tested for 1.5 compatibility.
 *
 *  I built this with: java -Xmx100M org.antlr.Tool java.g 
 *  and got two errors that are ok (for now):
 *  java.g:691:9: Decision can match input such as
 *    "'0'..'9'{'E', 'e'}{'+', '-'}'0'..'9'{'D', 'F', 'd', 'f'}"
 *    using multiple alternatives: 3, 4
 *  As a result, alternative(s) 4 were disabled for that input
 *  java.g:734:35: Decision can match input such as "{'$', 'A'..'Z',
 *    '_', 'a'..'z', '\u00C0'..'\u00D6', '\u00D8'..'\u00F6',
 *    '\u00F8'..'\u1FFF', '\u3040'..'\u318F', '\u3300'..'\u337F',
 *    '\u3400'..'\u3D2D', '\u4E00'..'\u9FFF', '\uF900'..'\uFAFF'}"
 *    using multiple alternatives: 1, 2
 *  As a result, alternative(s) 2 were disabled for that input
 *
 *  You can turn enum on/off as a keyword :)
 *
 *  Version 1.0 -- initial release July 5, 2006 (requires 3.0b2 or higher)
 *
 *  Primary author: Terence Parr, July 2006
 *
 *  Version 1.0.1 -- corrections by Koen Vanderkimpen & Marko van Dooren,
 *      October 25, 2006;
 *      fixed normalInterfaceDeclaration: now uses typeParameters instead
 *          of typeParameter (according to JLS, 3rd edition)
 *      fixed castExpression: no longer allows expression next to type
 *          (according to semantics in JLS, in contrast with syntax in JLS)
 *
 *  Version 1.0.2 -- Terence Parr, Nov 27, 2006
 *      java spec I built this from had some bizarre for-loop control.
 *          Looked weird and so I looked elsewhere...Yep, it's messed up.
 *          simplified.
 *
 *  Version 1.0.3 -- Chris Hogue, Feb 26, 2007
 *      Factored out an annotationName rule and used it in the annotation rule.
 *          Not sure why, but typeName wasn't recognizing references to inner
 *          annotations (e.g. @InterfaceName.InnerAnnotation())
 *      Factored out the elementValue section of an annotation reference.  Created 
 *          elementValuePair and elementValuePairs rules, then used them in the 
 *          annotation rule.  Allows it to recognize annotation references with 
 *          multiple, comma separated attributes.
 *      Updated elementValueArrayInitializer so that it allows multiple elements.
 *          (It was only allowing 0 or 1 element).
 *      Updated localVariableDeclaration to allow annotations.  Interestingly the JLS
 *          doesn't appear to indicate this is legal, but it does work as of at least
 *          JDK 1.5.0_06.
 *      Moved the Identifier portion of annotationTypeElementRest to annotationMethodRest.
 *          Because annotationConstantRest already references variableDeclarator which 
 *          has the Identifier portion in it, the parser would fail on constants in 
 *          annotation definitions because it expected two identifiers.  
 *      Added optional trailing ';' to the alternatives in annotationTypeElementRest.
 *          Wouldn't handle an inner interface that has a trailing ';'.
 *      Swapped the expression and type rule reference order in castExpression to 
 *          make it check for genericized casts first.  It was failing to recognize a
 *          statement like  "Class<Byte> TYPE = (Class<Byte>)...;" because it was seeing
 *          'Class<Byte' in the cast expression as a less than expression, then failing 
 *          on the '>'.
 *      Changed createdName to use typeArguments instead of nonWildcardTypeArguments.
 *          Again, JLS doesn't seem to allow this, but java.lang.Class has an example of
 *          of this construct.
 *      Changed the 'this' alternative in primary to allow 'identifierSuffix' rather than
 *          just 'arguments'.  The case it couldn't handle was a call to an explicit
 *          generic method invocation (e.g. this.<E>doSomething()).  Using identifierSuffix
 *          may be overly aggressive--perhaps should create a more constrained thisSuffix rule?
 *      
 *  Version 1.0.4 -- Hiroaki Nakamura, May 3, 2007
 *
 *  Fixed formalParameterDecls, localVariableDeclaration, forInit,
 *  and forVarControl to use variableModifier* not 'final'? (annotation)?
 *
 *  Version 1.0.5 -- Terence, June 21, 2007
 *  --a[i].foo didn't work. Fixed unaryExpression
 *
 *  Version 1.0.6 -- John Ridgway, March 17, 2008
 *      Made "assert" a switchable keyword like "enum".
 *      Fixed compilationUnit to disallow "annotation importDeclaration ...".
 *      Changed "Identifier ('.' Identifier)*" to "qualifiedName" in more 
 *          places.
 *      Changed modifier* and/or variableModifier* to classOrInterfaceModifiers,
 *          modifiers or variableModifiers, as appropriate.
 *      Renamed "bound" to "typeBound" to better match language in the JLS.
 *      Added "memberDeclaration" which rewrites to methodDeclaration or 
 *      fieldDeclaration and pulled type into memberDeclaration.  So we parse 
 *          type and then move on to decide whether we're dealing with a field
 *          or a method.
 *      Modified "constructorDeclaration" to use "constructorBody" instead of
 *          "methodBody".  constructorBody starts with explicitConstructorInvocation,
 *          then goes on to blockStatement*.  Pulling explicitConstructorInvocation
 *          out of expressions allowed me to simplify "primary".
 *      Changed variableDeclarator to simplify it.
 *      Changed type to use classOrInterfaceType, thus simplifying it; of course
 *          I then had to add classOrInterfaceType, but it is used in several 
 *          places.
 *      Fixed annotations, old version allowed "@X(y,z)", which is illegal.
 *      Added optional comma to end of "elementValueArrayInitializer"; as per JLS.
 *      Changed annotationTypeElementRest to use normalClassDeclaration and 
 *          normalInterfaceDeclaration rather than classDeclaration and 
 *          interfaceDeclaration, thus getting rid of a couple of grammar ambiguities.
 *      Split localVariableDeclaration into localVariableDeclarationStatement
 *          (includes the terminating semi-colon) and localVariableDeclaration.  
 *          This allowed me to use localVariableDeclaration in "forInit" clauses,
 *           simplifying them.
 *      Changed switchBlockStatementGroup to use multiple labels.  This adds an
 *          ambiguity, but if one uses appropriately greedy parsing it yields the
 *           parse that is closest to the meaning of the switch statement.
 *      Renamed "forVarControl" to "enhancedForControl" -- JLS language.
 *      Added semantic predicates to test for shift operations rather than other
 *          things.  Thus, for instance, the string "< <" will never be treated
 *          as a left-shift operator.
 *      In "creator" we rule out "nonWildcardTypeArguments" on arrayCreation, 
 *          which are illegal.
 *      Moved "nonWildcardTypeArguments into innerCreator.
 *      Removed 'super' superSuffix from explicitGenericInvocation, since that
 *          is only used in explicitConstructorInvocation at the beginning of a
 *           constructorBody.  (This is part of the simplification of expressions
 *           mentioned earlier.)
 *      Simplified primary (got rid of those things that are only used in
 *          explicitConstructorInvocation).
 *      Lexer -- removed "Exponent?" from FloatingPointLiteral choice 4, since it
 *          led to an ambiguity.
 *
 *      This grammar successfully parses every .java file in the JDK 1.5 source 
 *          tree (excluding those whose file names include '-', which are not
 *          valid Java compilation units).
 *
 *  Known remaining problems:
 *      "Letter" and "JavaIDDigit" are wrong.  The actual specification of
 *      "Letter" should be "a character for which the method
 *      Character.isJavaIdentifierStart(int) returns true."  A "Java 
 *      letter-or-digit is a character for which the method 
 *      Character.isJavaIdentifierPart(int) returns true."
 */
@SuppressWarnings({"all", "warnings", "unchecked"})
public class JavaParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSERT", "COMMENT", "CharacterLiteral", "DecimalLiteral", "ENUM", "EscapeSequence", "Exponent", "FloatTypeSuffix", "FloatingPointLiteral", "HexDigit", "HexLiteral", "Identifier", "IntegerTypeSuffix", "JavaIDDigit", "LINE_COMMENT", "Letter", "OctalEscape", "OctalLiteral", "StringLiteral", "UnicodeEscape", "WS", "'!'", "'!='", "'%'", "'%='", "'&&'", "'&'", "'&='", "'('", "')'", "'*'", "'*='", "'+'", "'++'", "'+='", "','", "'-'", "'--'", "'-='", "'.'", "'...'", "'/'", "'/='", "':'", "';'", "'<'", "'='", "'=='", "'>'", "'?'", "'@'", "'['", "']'", "'^'", "'^='", "'abstract'", "'boolean'", "'break'", "'byte'", "'case'", "'catch'", "'char'", "'class'", "'continue'", "'default'", "'do'", "'double'", "'else'", "'extends'", "'false'", "'final'", "'finally'", "'float'", "'for'", "'if'", "'implements'", "'import'", "'instanceof'", "'int'", "'interface'", "'long'", "'native'", "'new'", "'null'", "'package'", "'private'", "'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", "'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", "'transient'", "'true'", "'try'", "'void'", "'volatile'", "'while'", "'{'", "'|'", "'|='", "'||'", "'}'", "'~'"
    };

    public static final int EOF=-1;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int ASSERT=4;
    public static final int COMMENT=5;
    public static final int CharacterLiteral=6;
    public static final int DecimalLiteral=7;
    public static final int ENUM=8;
    public static final int EscapeSequence=9;
    public static final int Exponent=10;
    public static final int FloatTypeSuffix=11;
    public static final int FloatingPointLiteral=12;
    public static final int HexDigit=13;
    public static final int HexLiteral=14;
    public static final int Identifier=15;
    public static final int IntegerTypeSuffix=16;
    public static final int JavaIDDigit=17;
    public static final int LINE_COMMENT=18;
    public static final int Letter=19;
    public static final int OctalEscape=20;
    public static final int OctalLiteral=21;
    public static final int StringLiteral=22;
    public static final int UnicodeEscape=23;
    public static final int WS=24;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public JavaParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public JavaParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[407+1];
         

    }

    public String[] getTokenNames() { return JavaParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g"; }


    	
       private Set<String> selects = new HashSet<String>();
       
       private Set<String> updates = new HashSet<String>();
       
       private Set<String> deletes = new HashSet<String>();
       
       private Set<String> inserts = new HashSet<String>();
       
       private HashMap<String, String> names = new HashMap<String, String>();
       
       public HashMap<String, String> getNames() {
          return this.names;
       }
       
       public Set<String> getSelects () {
          return this.selects;
       }
       
       public Set<String> getUpdates () {
          return this.updates;
       }
       
       public Set<String> getDeletes () {
          return this.deletes;
       }
       
       public Set<String> getInserts () {
          return this.inserts;
       }
        



    // $ANTLR start "compilationUnit"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:229:1: compilationUnit : ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* );
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:230:5: ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) | ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==54) ) {
                int LA8_1 = input.LA(2);

                if ( (synpred5_Java()) ) {
                    alt8=1;
                }
                else if ( (true) ) {
                    alt8=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA8_0==EOF||LA8_0==ENUM||LA8_0==48||LA8_0==59||LA8_0==66||LA8_0==74||LA8_0==80||LA8_0==83||(LA8_0 >= 88 && LA8_0 <= 91)||(LA8_0 >= 94 && LA8_0 <= 95)) ) {
                alt8=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:230:9: annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                    {
                    pushFollow(FOLLOW_annotations_in_compilationUnit66);
                    annotations();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==88) ) {
                        alt4=1;
                    }
                    else if ( (LA4_0==ENUM||LA4_0==54||LA4_0==59||LA4_0==66||LA4_0==74||LA4_0==83||(LA4_0 >= 89 && LA4_0 <= 91)||(LA4_0 >= 94 && LA4_0 <= 95)) ) {
                        alt4=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;

                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:13: packageDeclaration ( importDeclaration )* ( typeDeclaration )*
                            {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit80);
                            packageDeclaration();

                            state._fsp--;
                            if (state.failed) return ;

                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:32: ( importDeclaration )*
                            loop1:
                            do {
                                int alt1=2;
                                int LA1_0 = input.LA(1);

                                if ( (LA1_0==80) ) {
                                    alt1=1;
                                }


                                switch (alt1) {
                            	case 1 :
                            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:32: importDeclaration
                            	    {
                            	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit82);
                            	    importDeclaration();

                            	    state._fsp--;
                            	    if (state.failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop1;
                                }
                            } while (true);


                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:51: ( typeDeclaration )*
                            loop2:
                            do {
                                int alt2=2;
                                int LA2_0 = input.LA(1);

                                if ( (LA2_0==ENUM||LA2_0==48||LA2_0==54||LA2_0==59||LA2_0==66||LA2_0==74||LA2_0==83||(LA2_0 >= 89 && LA2_0 <= 91)||(LA2_0 >= 94 && LA2_0 <= 95)) ) {
                                    alt2=1;
                                }


                                switch (alt2) {
                            	case 1 :
                            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:51: typeDeclaration
                            	    {
                            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit85);
                            	    typeDeclaration();

                            	    state._fsp--;
                            	    if (state.failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop2;
                                }
                            } while (true);


                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:13: classOrInterfaceDeclaration ( typeDeclaration )*
                            {
                            pushFollow(FOLLOW_classOrInterfaceDeclaration_in_compilationUnit100);
                            classOrInterfaceDeclaration();

                            state._fsp--;
                            if (state.failed) return ;

                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:41: ( typeDeclaration )*
                            loop3:
                            do {
                                int alt3=2;
                                int LA3_0 = input.LA(1);

                                if ( (LA3_0==ENUM||LA3_0==48||LA3_0==54||LA3_0==59||LA3_0==66||LA3_0==74||LA3_0==83||(LA3_0 >= 89 && LA3_0 <= 91)||(LA3_0 >= 94 && LA3_0 <= 95)) ) {
                                    alt3=1;
                                }


                                switch (alt3) {
                            	case 1 :
                            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:41: typeDeclaration
                            	    {
                            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit102);
                            	    typeDeclaration();

                            	    state._fsp--;
                            	    if (state.failed) return ;

                            	    }
                            	    break;

                            	default :
                            	    break loop3;
                                }
                            } while (true);


                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:9: ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:9: ( packageDeclaration )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==88) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:9: packageDeclaration
                            {
                            pushFollow(FOLLOW_packageDeclaration_in_compilationUnit123);
                            packageDeclaration();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:29: ( importDeclaration )*
                    loop6:
                    do {
                        int alt6=2;
                        int LA6_0 = input.LA(1);

                        if ( (LA6_0==80) ) {
                            alt6=1;
                        }


                        switch (alt6) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:29: importDeclaration
                    	    {
                    	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit126);
                    	    importDeclaration();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop6;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:48: ( typeDeclaration )*
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==ENUM||LA7_0==48||LA7_0==54||LA7_0==59||LA7_0==66||LA7_0==74||LA7_0==83||(LA7_0 >= 89 && LA7_0 <= 91)||(LA7_0 >= 94 && LA7_0 <= 95)) ) {
                            alt7=1;
                        }


                        switch (alt7) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:234:48: typeDeclaration
                    	    {
                    	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit129);
                    	    typeDeclaration();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop7;
                        }
                    } while (true);


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "compilationUnit"



    // $ANTLR start "packageDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:237:1: packageDeclaration : 'package' qualifiedName ';' ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:238:5: ( 'package' qualifiedName ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:238:9: 'package' qualifiedName ';'
            {
            match(input,88,FOLLOW_88_in_packageDeclaration149); if (state.failed) return ;

            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration151);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;

            match(input,48,FOLLOW_48_in_packageDeclaration153); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 2, packageDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "packageDeclaration"



    // $ANTLR start "importDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:241:1: importDeclaration : 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' ;
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:5: ( 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:9: 'import' ( 'static' )? qualifiedName ( '.' '*' )? ';'
            {
            match(input,80,FOLLOW_80_in_importDeclaration176); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:18: ( 'static' )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==94) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:18: 'static'
                    {
                    match(input,94,FOLLOW_94_in_importDeclaration178); if (state.failed) return ;

                    }
                    break;

            }


            pushFollow(FOLLOW_qualifiedName_in_importDeclaration181);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:42: ( '.' '*' )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==43) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:242:43: '.' '*'
                    {
                    match(input,43,FOLLOW_43_in_importDeclaration184); if (state.failed) return ;

                    match(input,34,FOLLOW_34_in_importDeclaration186); if (state.failed) return ;

                    }
                    break;

            }


            match(input,48,FOLLOW_48_in_importDeclaration190); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 3, importDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "importDeclaration"


    public static class typeDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "typeDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:245:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final JavaParser.typeDeclaration_return typeDeclaration() throws RecognitionException {
        JavaParser.typeDeclaration_return retval = new JavaParser.typeDeclaration_return();
        retval.start = input.LT(1);

        int typeDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:246:5: ( classOrInterfaceDeclaration | ';' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==ENUM||LA11_0==54||LA11_0==59||LA11_0==66||LA11_0==74||LA11_0==83||(LA11_0 >= 89 && LA11_0 <= 91)||(LA11_0 >= 94 && LA11_0 <= 95)) ) {
                alt11=1;
            }
            else if ( (LA11_0==48) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }
            switch (alt11) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:246:9: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration213);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    if ( state.backtracking==0 ) {System.out.println( " Nome da Classe " +input.toString(retval.start,input.LT(-1)));}

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:247:9: ';'
                    {
                    match(input,48,FOLLOW_48_in_typeDeclaration225); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 4, typeDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "typeDeclaration"



    // $ANTLR start "classOrInterfaceDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:250:1: classOrInterfaceDeclaration : classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) ;
    public final void classOrInterfaceDeclaration() throws RecognitionException {
        int classOrInterfaceDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:251:5: ( classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration ) )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:251:9: classOrInterfaceModifiers ( classDeclaration | interfaceDeclaration )
            {
            pushFollow(FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration248);
            classOrInterfaceModifiers();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:251:35: ( classDeclaration | interfaceDeclaration )
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==ENUM||LA12_0==66) ) {
                alt12=1;
            }
            else if ( (LA12_0==54||LA12_0==83) ) {
                alt12=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }
            switch (alt12) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:251:36: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration251);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:251:55: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration255);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 5, classOrInterfaceDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classOrInterfaceDeclaration"



    // $ANTLR start "classOrInterfaceModifiers"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:254:1: classOrInterfaceModifiers : ( classOrInterfaceModifier )* ;
    public final void classOrInterfaceModifiers() throws RecognitionException {
        int classOrInterfaceModifiers_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:255:5: ( ( classOrInterfaceModifier )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:255:9: ( classOrInterfaceModifier )*
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:255:9: ( classOrInterfaceModifier )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==54) ) {
                    int LA13_2 = input.LA(2);

                    if ( (LA13_2==Identifier) ) {
                        alt13=1;
                    }


                }
                else if ( (LA13_0==59||LA13_0==74||(LA13_0 >= 89 && LA13_0 <= 91)||(LA13_0 >= 94 && LA13_0 <= 95)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:255:9: classOrInterfaceModifier
            	    {
            	    pushFollow(FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers279);
            	    classOrInterfaceModifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 6, classOrInterfaceModifiers_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classOrInterfaceModifiers"



    // $ANTLR start "classOrInterfaceModifier"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:258:1: classOrInterfaceModifier : ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' );
    public final void classOrInterfaceModifier() throws RecognitionException {
        int classOrInterfaceModifier_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:259:5: ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'strictfp' )
            int alt14=8;
            switch ( input.LA(1) ) {
            case 54:
                {
                alt14=1;
                }
                break;
            case 91:
                {
                alt14=2;
                }
                break;
            case 90:
                {
                alt14=3;
                }
                break;
            case 89:
                {
                alt14=4;
                }
                break;
            case 59:
                {
                alt14=5;
                }
                break;
            case 94:
                {
                alt14=6;
                }
                break;
            case 74:
                {
                alt14=7;
                }
                break;
            case 95:
                {
                alt14=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:259:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_classOrInterfaceModifier299);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:260:9: 'public'
                    {
                    match(input,91,FOLLOW_91_in_classOrInterfaceModifier312); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:261:9: 'protected'
                    {
                    match(input,90,FOLLOW_90_in_classOrInterfaceModifier327); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:262:9: 'private'
                    {
                    match(input,89,FOLLOW_89_in_classOrInterfaceModifier339); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:263:9: 'abstract'
                    {
                    match(input,59,FOLLOW_59_in_classOrInterfaceModifier353); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:264:9: 'static'
                    {
                    match(input,94,FOLLOW_94_in_classOrInterfaceModifier366); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:265:9: 'final'
                    {
                    match(input,74,FOLLOW_74_in_classOrInterfaceModifier381); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:266:9: 'strictfp'
                    {
                    match(input,95,FOLLOW_95_in_classOrInterfaceModifier397); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 7, classOrInterfaceModifier_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classOrInterfaceModifier"



    // $ANTLR start "modifiers"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:269:1: modifiers : ( modifier )* ;
    public final void modifiers() throws RecognitionException {
        int modifiers_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:270:5: ( ( modifier )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:270:9: ( modifier )*
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:270:9: ( modifier )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==54) ) {
                    int LA15_2 = input.LA(2);

                    if ( (LA15_2==Identifier) ) {
                        alt15=1;
                    }


                }
                else if ( (LA15_0==59||LA15_0==74||LA15_0==85||(LA15_0 >= 89 && LA15_0 <= 91)||(LA15_0 >= 94 && LA15_0 <= 95)||LA15_0==98||LA15_0==102||LA15_0==106) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:270:9: modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_modifiers419);
            	    modifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 8, modifiers_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "modifiers"



    // $ANTLR start "classDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:273:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final void classDeclaration() throws RecognitionException {
        int classDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:274:5: ( normalClassDeclaration | enumDeclaration )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==66) ) {
                alt16=1;
            }
            else if ( (LA16_0==ENUM) ) {
                alt16=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }
            switch (alt16) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:274:9: normalClassDeclaration
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration439);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:275:9: enumDeclaration
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration449);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 9, classDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classDeclaration"



    // $ANTLR start "normalClassDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:278:1: normalClassDeclaration : 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody ;
    public final void normalClassDeclaration() throws RecognitionException {
        int normalClassDeclaration_StartIndex = input.index();

        Token Identifier1=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:279:5: ( 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:279:9: 'class' Identifier ( typeParameters )? ( 'extends' type )? ( 'implements' typeList )? classBody
            {
            match(input,66,FOLLOW_66_in_normalClassDeclaration472); if (state.failed) return ;

            Identifier1=(Token)match(input,Identifier,FOLLOW_Identifier_in_normalClassDeclaration474); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:279:28: ( typeParameters )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==49) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:279:28: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration476);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            if ( state.backtracking==0 ) {names.put("class_name", (Identifier1!=null?Identifier1.getText():null));}

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:280:9: ( 'extends' type )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==72) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:280:10: 'extends' type
                    {
                    match(input,72,FOLLOW_72_in_normalClassDeclaration490); if (state.failed) return ;

                    pushFollow(FOLLOW_type_in_normalClassDeclaration492);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:281:9: ( 'implements' typeList )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==79) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:281:10: 'implements' typeList
                    {
                    match(input,79,FOLLOW_79_in_normalClassDeclaration505); if (state.failed) return ;

                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration507);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            pushFollow(FOLLOW_classBody_in_normalClassDeclaration519);
            classBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 10, normalClassDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "normalClassDeclaration"



    // $ANTLR start "typeParameters"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:285:1: typeParameters : '<' typeParameter ( ',' typeParameter )* '>' ;
    public final void typeParameters() throws RecognitionException {
        int typeParameters_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:286:5: ( '<' typeParameter ( ',' typeParameter )* '>' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:286:9: '<' typeParameter ( ',' typeParameter )* '>'
            {
            match(input,49,FOLLOW_49_in_typeParameters542); if (state.failed) return ;

            pushFollow(FOLLOW_typeParameter_in_typeParameters544);
            typeParameter();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:286:27: ( ',' typeParameter )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==39) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:286:28: ',' typeParameter
            	    {
            	    match(input,39,FOLLOW_39_in_typeParameters547); if (state.failed) return ;

            	    pushFollow(FOLLOW_typeParameter_in_typeParameters549);
            	    typeParameter();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            match(input,52,FOLLOW_52_in_typeParameters553); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 11, typeParameters_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeParameters"



    // $ANTLR start "typeParameter"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:289:1: typeParameter : Identifier ( 'extends' typeBound )? ;
    public final void typeParameter() throws RecognitionException {
        int typeParameter_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:290:5: ( Identifier ( 'extends' typeBound )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:290:9: Identifier ( 'extends' typeBound )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_typeParameter572); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:290:20: ( 'extends' typeBound )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==72) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:290:21: 'extends' typeBound
                    {
                    match(input,72,FOLLOW_72_in_typeParameter575); if (state.failed) return ;

                    pushFollow(FOLLOW_typeBound_in_typeParameter577);
                    typeBound();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 12, typeParameter_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeParameter"



    // $ANTLR start "typeBound"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:293:1: typeBound : type ( '&' type )* ;
    public final void typeBound() throws RecognitionException {
        int typeBound_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:294:5: ( type ( '&' type )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:294:9: type ( '&' type )*
            {
            pushFollow(FOLLOW_type_in_typeBound606);
            type();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:294:14: ( '&' type )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==30) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:294:15: '&' type
            	    {
            	    match(input,30,FOLLOW_30_in_typeBound609); if (state.failed) return ;

            	    pushFollow(FOLLOW_type_in_typeBound611);
            	    type();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 13, typeBound_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeBound"



    // $ANTLR start "enumDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:297:1: enumDeclaration : ENUM Identifier ( 'implements' typeList )? enumBody ;
    public final void enumDeclaration() throws RecognitionException {
        int enumDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:298:5: ( ENUM Identifier ( 'implements' typeList )? enumBody )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:298:9: ENUM Identifier ( 'implements' typeList )? enumBody
            {
            match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration632); if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_enumDeclaration634); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:298:25: ( 'implements' typeList )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==79) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:298:26: 'implements' typeList
                    {
                    match(input,79,FOLLOW_79_in_enumDeclaration637); if (state.failed) return ;

                    pushFollow(FOLLOW_typeList_in_enumDeclaration639);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            pushFollow(FOLLOW_enumBody_in_enumDeclaration643);
            enumBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 14, enumDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumDeclaration"



    // $ANTLR start "enumBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:301:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
    public final void enumBody() throws RecognitionException {
        int enumBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:5: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:9: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
            match(input,108,FOLLOW_108_in_enumBody662); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:13: ( enumConstants )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==Identifier||LA24_0==54) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:13: enumConstants
                    {
                    pushFollow(FOLLOW_enumConstants_in_enumBody664);
                    enumConstants();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:28: ( ',' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==39) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:28: ','
                    {
                    match(input,39,FOLLOW_39_in_enumBody667); if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:33: ( enumBodyDeclarations )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==48) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:302:33: enumBodyDeclarations
                    {
                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody670);
                    enumBodyDeclarations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,112,FOLLOW_112_in_enumBody673); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 15, enumBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumBody"



    // $ANTLR start "enumConstants"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:305:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final void enumConstants() throws RecognitionException {
        int enumConstants_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:306:5: ( enumConstant ( ',' enumConstant )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:306:9: enumConstant ( ',' enumConstant )*
            {
            pushFollow(FOLLOW_enumConstant_in_enumConstants692);
            enumConstant();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:306:22: ( ',' enumConstant )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==39) ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1==Identifier||LA27_1==54) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:306:23: ',' enumConstant
            	    {
            	    match(input,39,FOLLOW_39_in_enumConstants695); if (state.failed) return ;

            	    pushFollow(FOLLOW_enumConstant_in_enumConstants697);
            	    enumConstant();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 16, enumConstants_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumConstants"



    // $ANTLR start "enumConstant"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:309:1: enumConstant : ( annotations )? Identifier ( arguments )? ( classBody )? ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:5: ( ( annotations )? Identifier ( arguments )? ( classBody )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:9: ( annotations )? Identifier ( arguments )? ( classBody )?
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:9: ( annotations )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==54) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:9: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_enumConstant722);
                    annotations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,Identifier,FOLLOW_Identifier_in_enumConstant725); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:33: ( arguments )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==32) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:33: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_enumConstant727);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:44: ( classBody )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==108) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:310:44: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_enumConstant730);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 17, enumConstant_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumConstant"



    // $ANTLR start "enumBodyDeclarations"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:313:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final void enumBodyDeclarations() throws RecognitionException {
        int enumBodyDeclarations_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:314:5: ( ';' ( classBodyDeclaration )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:314:9: ';' ( classBodyDeclaration )*
            {
            match(input,48,FOLLOW_48_in_enumBodyDeclarations754); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:314:13: ( classBodyDeclaration )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==ENUM||LA31_0==Identifier||(LA31_0 >= 48 && LA31_0 <= 49)||LA31_0==54||(LA31_0 >= 59 && LA31_0 <= 60)||LA31_0==62||(LA31_0 >= 65 && LA31_0 <= 66)||LA31_0==70||LA31_0==74||LA31_0==76||(LA31_0 >= 82 && LA31_0 <= 85)||(LA31_0 >= 89 && LA31_0 <= 91)||(LA31_0 >= 93 && LA31_0 <= 95)||LA31_0==98||LA31_0==102||(LA31_0 >= 105 && LA31_0 <= 106)||LA31_0==108) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:314:14: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations757);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 18, enumBodyDeclarations_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumBodyDeclarations"



    // $ANTLR start "interfaceDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:317:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final void interfaceDeclaration() throws RecognitionException {
        int interfaceDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:318:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt32=2;
            int LA32_0 = input.LA(1);

            if ( (LA32_0==83) ) {
                alt32=1;
            }
            else if ( (LA32_0==54) ) {
                alt32=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 32, 0, input);

                throw nvae;

            }
            switch (alt32) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:318:9: normalInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration782);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:319:9: annotationTypeDeclaration
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration792);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 19, interfaceDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceDeclaration"



    // $ANTLR start "normalInterfaceDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:322:1: normalInterfaceDeclaration : 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody ;
    public final void normalInterfaceDeclaration() throws RecognitionException {
        int normalInterfaceDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:5: ( 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:9: 'interface' Identifier ( typeParameters )? ( 'extends' typeList )? interfaceBody
            {
            match(input,83,FOLLOW_83_in_normalInterfaceDeclaration815); if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_normalInterfaceDeclaration817); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:32: ( typeParameters )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==49) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:32: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration819);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:48: ( 'extends' typeList )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==72) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:323:49: 'extends' typeList
                    {
                    match(input,72,FOLLOW_72_in_normalInterfaceDeclaration823); if (state.failed) return ;

                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration825);
                    typeList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration829);
            interfaceBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 20, normalInterfaceDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "normalInterfaceDeclaration"



    // $ANTLR start "typeList"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:326:1: typeList : type ( ',' type )* ;
    public final void typeList() throws RecognitionException {
        int typeList_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:327:5: ( type ( ',' type )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:327:9: type ( ',' type )*
            {
            pushFollow(FOLLOW_type_in_typeList852);
            type();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:327:14: ( ',' type )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==39) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:327:15: ',' type
            	    {
            	    match(input,39,FOLLOW_39_in_typeList855); if (state.failed) return ;

            	    pushFollow(FOLLOW_type_in_typeList857);
            	    type();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 21, typeList_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeList"



    // $ANTLR start "classBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:330:1: classBody : '{' ( classBodyDeclaration )* '}' ;
    public final void classBody() throws RecognitionException {
        int classBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:331:5: ( '{' ( classBodyDeclaration )* '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:331:9: '{' ( classBodyDeclaration )* '}'
            {
            match(input,108,FOLLOW_108_in_classBody882); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:331:13: ( classBodyDeclaration )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==ENUM||LA36_0==Identifier||(LA36_0 >= 48 && LA36_0 <= 49)||LA36_0==54||(LA36_0 >= 59 && LA36_0 <= 60)||LA36_0==62||(LA36_0 >= 65 && LA36_0 <= 66)||LA36_0==70||LA36_0==74||LA36_0==76||(LA36_0 >= 82 && LA36_0 <= 85)||(LA36_0 >= 89 && LA36_0 <= 91)||(LA36_0 >= 93 && LA36_0 <= 95)||LA36_0==98||LA36_0==102||(LA36_0 >= 105 && LA36_0 <= 106)||LA36_0==108) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:331:13: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody884);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);


            match(input,112,FOLLOW_112_in_classBody887); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 22, classBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classBody"



    // $ANTLR start "interfaceBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:334:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
    public final void interfaceBody() throws RecognitionException {
        int interfaceBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:335:5: ( '{' ( interfaceBodyDeclaration )* '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:335:9: '{' ( interfaceBodyDeclaration )* '}'
            {
            match(input,108,FOLLOW_108_in_interfaceBody910); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:335:13: ( interfaceBodyDeclaration )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( (LA37_0==ENUM||LA37_0==Identifier||(LA37_0 >= 48 && LA37_0 <= 49)||LA37_0==54||(LA37_0 >= 59 && LA37_0 <= 60)||LA37_0==62||(LA37_0 >= 65 && LA37_0 <= 66)||LA37_0==70||LA37_0==74||LA37_0==76||(LA37_0 >= 82 && LA37_0 <= 85)||(LA37_0 >= 89 && LA37_0 <= 91)||(LA37_0 >= 93 && LA37_0 <= 95)||LA37_0==98||LA37_0==102||(LA37_0 >= 105 && LA37_0 <= 106)) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:335:13: interfaceBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody912);
            	    interfaceBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);


            match(input,112,FOLLOW_112_in_interfaceBody915); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 23, interfaceBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceBody"



    // $ANTLR start "classBodyDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:338:1: classBodyDeclaration : ( ';' | ( 'static' )? block | modifiers memberDecl );
    public final void classBodyDeclaration() throws RecognitionException {
        int classBodyDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:339:5: ( ';' | ( 'static' )? block | modifiers memberDecl )
            int alt39=3;
            switch ( input.LA(1) ) {
            case 48:
                {
                alt39=1;
                }
                break;
            case 94:
                {
                int LA39_2 = input.LA(2);

                if ( (LA39_2==108) ) {
                    alt39=2;
                }
                else if ( (LA39_2==ENUM||LA39_2==Identifier||LA39_2==49||LA39_2==54||(LA39_2 >= 59 && LA39_2 <= 60)||LA39_2==62||(LA39_2 >= 65 && LA39_2 <= 66)||LA39_2==70||LA39_2==74||LA39_2==76||(LA39_2 >= 82 && LA39_2 <= 85)||(LA39_2 >= 89 && LA39_2 <= 91)||(LA39_2 >= 93 && LA39_2 <= 95)||LA39_2==98||LA39_2==102||(LA39_2 >= 105 && LA39_2 <= 106)) ) {
                    alt39=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 2, input);

                    throw nvae;

                }
                }
                break;
            case 108:
                {
                alt39=2;
                }
                break;
            case ENUM:
            case Identifier:
            case 49:
            case 54:
            case 59:
            case 60:
            case 62:
            case 65:
            case 66:
            case 70:
            case 74:
            case 76:
            case 82:
            case 83:
            case 84:
            case 85:
            case 89:
            case 90:
            case 91:
            case 93:
            case 95:
            case 98:
            case 102:
            case 105:
            case 106:
                {
                alt39=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;

            }

            switch (alt39) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:339:9: ';'
                    {
                    match(input,48,FOLLOW_48_in_classBodyDeclaration934); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:340:9: ( 'static' )? block
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:340:9: ( 'static' )?
                    int alt38=2;
                    int LA38_0 = input.LA(1);

                    if ( (LA38_0==94) ) {
                        alt38=1;
                    }
                    switch (alt38) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:340:9: 'static'
                            {
                            match(input,94,FOLLOW_94_in_classBodyDeclaration944); if (state.failed) return ;

                            }
                            break;

                    }


                    pushFollow(FOLLOW_block_in_classBodyDeclaration947);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:341:9: modifiers memberDecl
                    {
                    pushFollow(FOLLOW_modifiers_in_classBodyDeclaration957);
                    modifiers();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration959);
                    memberDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 24, classBodyDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classBodyDeclaration"



    // $ANTLR start "memberDecl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:344:1: memberDecl : ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void memberDecl() throws RecognitionException {
        int memberDecl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:345:5: ( genericMethodOrConstructorDecl | memberDeclaration | 'void' Identifier voidMethodDeclaratorRest | Identifier constructorDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt40=6;
            switch ( input.LA(1) ) {
            case 49:
                {
                alt40=1;
                }
                break;
            case Identifier:
                {
                int LA40_2 = input.LA(2);

                if ( (LA40_2==Identifier||LA40_2==43||LA40_2==49||LA40_2==55) ) {
                    alt40=2;
                }
                else if ( (LA40_2==32) ) {
                    alt40=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 40, 2, input);

                    throw nvae;

                }
                }
                break;
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                alt40=2;
                }
                break;
            case 105:
                {
                alt40=3;
                }
                break;
            case 54:
            case 83:
                {
                alt40=5;
                }
                break;
            case ENUM:
            case 66:
                {
                alt40=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;

            }

            switch (alt40) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:345:9: genericMethodOrConstructorDecl
                    {
                    pushFollow(FOLLOW_genericMethodOrConstructorDecl_in_memberDecl982);
                    genericMethodOrConstructorDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:346:9: memberDeclaration
                    {
                    pushFollow(FOLLOW_memberDeclaration_in_memberDecl992);
                    memberDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:347:9: 'void' Identifier voidMethodDeclaratorRest
                    {
                    match(input,105,FOLLOW_105_in_memberDecl1002); if (state.failed) return ;

                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl1004); if (state.failed) return ;

                    pushFollow(FOLLOW_voidMethodDeclaratorRest_in_memberDecl1006);
                    voidMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:348:9: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_memberDecl1016); if (state.failed) return ;

                    pushFollow(FOLLOW_constructorDeclaratorRest_in_memberDecl1018);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:349:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl1028);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:350:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_memberDecl1038);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 25, memberDecl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "memberDecl"



    // $ANTLR start "memberDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:353:1: memberDeclaration : type ( methodDeclaration | fieldDeclaration ) ;
    public final void memberDeclaration() throws RecognitionException {
        int memberDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:354:5: ( type ( methodDeclaration | fieldDeclaration ) )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:354:9: type ( methodDeclaration | fieldDeclaration )
            {
            pushFollow(FOLLOW_type_in_memberDeclaration1061);
            type();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:354:14: ( methodDeclaration | fieldDeclaration )
            int alt41=2;
            int LA41_0 = input.LA(1);

            if ( (LA41_0==Identifier) ) {
                int LA41_1 = input.LA(2);

                if ( (LA41_1==32) ) {
                    alt41=1;
                }
                else if ( (LA41_1==39||LA41_1==48||LA41_1==50||LA41_1==55) ) {
                    alt41=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 41, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;

            }
            switch (alt41) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:354:15: methodDeclaration
                    {
                    pushFollow(FOLLOW_methodDeclaration_in_memberDeclaration1064);
                    methodDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:354:35: fieldDeclaration
                    {
                    pushFollow(FOLLOW_fieldDeclaration_in_memberDeclaration1068);
                    fieldDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 26, memberDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "memberDeclaration"



    // $ANTLR start "genericMethodOrConstructorDecl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:357:1: genericMethodOrConstructorDecl : typeParameters genericMethodOrConstructorRest ;
    public final void genericMethodOrConstructorDecl() throws RecognitionException {
        int genericMethodOrConstructorDecl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:358:5: ( typeParameters genericMethodOrConstructorRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:358:9: typeParameters genericMethodOrConstructorRest
            {
            pushFollow(FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1088);
            typeParameters();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1090);
            genericMethodOrConstructorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 27, genericMethodOrConstructorDecl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "genericMethodOrConstructorDecl"



    // $ANTLR start "genericMethodOrConstructorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:361:1: genericMethodOrConstructorRest : ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest );
    public final void genericMethodOrConstructorRest() throws RecognitionException {
        int genericMethodOrConstructorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:362:5: ( ( type | 'void' ) Identifier methodDeclaratorRest | Identifier constructorDeclaratorRest )
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==Identifier) ) {
                int LA43_1 = input.LA(2);

                if ( (LA43_1==Identifier||LA43_1==43||LA43_1==49||LA43_1==55) ) {
                    alt43=1;
                }
                else if ( (LA43_1==32) ) {
                    alt43=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 43, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA43_0==60||LA43_0==62||LA43_0==65||LA43_0==70||LA43_0==76||LA43_0==82||LA43_0==84||LA43_0==93||LA43_0==105) ) {
                alt43=1;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 43, 0, input);

                throw nvae;

            }
            switch (alt43) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:362:9: ( type | 'void' ) Identifier methodDeclaratorRest
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:362:9: ( type | 'void' )
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==Identifier||LA42_0==60||LA42_0==62||LA42_0==65||LA42_0==70||LA42_0==76||LA42_0==82||LA42_0==84||LA42_0==93) ) {
                        alt42=1;
                    }
                    else if ( (LA42_0==105) ) {
                        alt42=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 42, 0, input);

                        throw nvae;

                    }
                    switch (alt42) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:362:10: type
                            {
                            pushFollow(FOLLOW_type_in_genericMethodOrConstructorRest1114);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:362:17: 'void'
                            {
                            match(input,105,FOLLOW_105_in_genericMethodOrConstructorRest1118); if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest1121); if (state.failed) return ;

                    pushFollow(FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1123);
                    methodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:363:9: Identifier constructorDeclaratorRest
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_genericMethodOrConstructorRest1133); if (state.failed) return ;

                    pushFollow(FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1135);
                    constructorDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 28, genericMethodOrConstructorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "genericMethodOrConstructorRest"



    // $ANTLR start "methodDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:366:1: methodDeclaration : Identifier methodDeclaratorRest ;
    public final void methodDeclaration() throws RecognitionException {
        int methodDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:367:5: ( Identifier methodDeclaratorRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:367:9: Identifier methodDeclaratorRest
            {
            match(input,Identifier,FOLLOW_Identifier_in_methodDeclaration1154); if (state.failed) return ;

            pushFollow(FOLLOW_methodDeclaratorRest_in_methodDeclaration1156);
            methodDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 29, methodDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "methodDeclaration"



    // $ANTLR start "fieldDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:370:1: fieldDeclaration : variableDeclarators ';' ;
    public final void fieldDeclaration() throws RecognitionException {
        int fieldDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:371:5: ( variableDeclarators ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:371:9: variableDeclarators ';'
            {
            pushFollow(FOLLOW_variableDeclarators_in_fieldDeclaration1175);
            variableDeclarators();

            state._fsp--;
            if (state.failed) return ;

            match(input,48,FOLLOW_48_in_fieldDeclaration1177); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 30, fieldDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "fieldDeclaration"



    // $ANTLR start "interfaceBodyDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:374:1: interfaceBodyDeclaration : ( modifiers interfaceMemberDecl | ';' );
    public final void interfaceBodyDeclaration() throws RecognitionException {
        int interfaceBodyDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:375:5: ( modifiers interfaceMemberDecl | ';' )
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==ENUM||LA44_0==Identifier||LA44_0==49||LA44_0==54||(LA44_0 >= 59 && LA44_0 <= 60)||LA44_0==62||(LA44_0 >= 65 && LA44_0 <= 66)||LA44_0==70||LA44_0==74||LA44_0==76||(LA44_0 >= 82 && LA44_0 <= 85)||(LA44_0 >= 89 && LA44_0 <= 91)||(LA44_0 >= 93 && LA44_0 <= 95)||LA44_0==98||LA44_0==102||(LA44_0 >= 105 && LA44_0 <= 106)) ) {
                alt44=1;
            }
            else if ( (LA44_0==48) ) {
                alt44=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 44, 0, input);

                throw nvae;

            }
            switch (alt44) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:375:9: modifiers interfaceMemberDecl
                    {
                    pushFollow(FOLLOW_modifiers_in_interfaceBodyDeclaration1204);
                    modifiers();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1206);
                    interfaceMemberDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:376:9: ';'
                    {
                    match(input,48,FOLLOW_48_in_interfaceBodyDeclaration1216); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 31, interfaceBodyDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceBodyDeclaration"



    // $ANTLR start "interfaceMemberDecl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:379:1: interfaceMemberDecl : ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration );
    public final void interfaceMemberDecl() throws RecognitionException {
        int interfaceMemberDecl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:380:5: ( interfaceMethodOrFieldDecl | interfaceGenericMethodDecl | 'void' Identifier voidInterfaceMethodDeclaratorRest | interfaceDeclaration | classDeclaration )
            int alt45=5;
            switch ( input.LA(1) ) {
            case Identifier:
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                alt45=1;
                }
                break;
            case 49:
                {
                alt45=2;
                }
                break;
            case 105:
                {
                alt45=3;
                }
                break;
            case 54:
            case 83:
                {
                alt45=4;
                }
                break;
            case ENUM:
            case 66:
                {
                alt45=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 45, 0, input);

                throw nvae;

            }

            switch (alt45) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:380:9: interfaceMethodOrFieldDecl
                    {
                    pushFollow(FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1235);
                    interfaceMethodOrFieldDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:381:9: interfaceGenericMethodDecl
                    {
                    pushFollow(FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1245);
                    interfaceGenericMethodDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:382:9: 'void' Identifier voidInterfaceMethodDeclaratorRest
                    {
                    match(input,105,FOLLOW_105_in_interfaceMemberDecl1255); if (state.failed) return ;

                    match(input,Identifier,FOLLOW_Identifier_in_interfaceMemberDecl1257); if (state.failed) return ;

                    pushFollow(FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1259);
                    voidInterfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:383:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1269);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:384:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_interfaceMemberDecl1279);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 32, interfaceMemberDecl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceMemberDecl"



    // $ANTLR start "interfaceMethodOrFieldDecl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:387:1: interfaceMethodOrFieldDecl : type Identifier interfaceMethodOrFieldRest ;
    public final void interfaceMethodOrFieldDecl() throws RecognitionException {
        int interfaceMethodOrFieldDecl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:388:5: ( type Identifier interfaceMethodOrFieldRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:388:9: type Identifier interfaceMethodOrFieldRest
            {
            pushFollow(FOLLOW_type_in_interfaceMethodOrFieldDecl1302);
            type();

            state._fsp--;
            if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1304); if (state.failed) return ;

            pushFollow(FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1306);
            interfaceMethodOrFieldRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 33, interfaceMethodOrFieldDecl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceMethodOrFieldDecl"



    // $ANTLR start "interfaceMethodOrFieldRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:391:1: interfaceMethodOrFieldRest : ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest );
    public final void interfaceMethodOrFieldRest() throws RecognitionException {
        int interfaceMethodOrFieldRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:392:5: ( constantDeclaratorsRest ';' | interfaceMethodDeclaratorRest )
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==50||LA46_0==55) ) {
                alt46=1;
            }
            else if ( (LA46_0==32) ) {
                alt46=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 46, 0, input);

                throw nvae;

            }
            switch (alt46) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:392:9: constantDeclaratorsRest ';'
                    {
                    pushFollow(FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1329);
                    constantDeclaratorsRest();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_interfaceMethodOrFieldRest1331); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:393:9: interfaceMethodDeclaratorRest
                    {
                    pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1341);
                    interfaceMethodDeclaratorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 34, interfaceMethodOrFieldRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceMethodOrFieldRest"



    // $ANTLR start "methodDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:396:1: methodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void methodDeclaratorRest() throws RecognitionException {
        int methodDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:397:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:397:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_methodDeclaratorRest1364);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:397:26: ( '[' ']' )*
            loop47:
            do {
                int alt47=2;
                int LA47_0 = input.LA(1);

                if ( (LA47_0==55) ) {
                    alt47=1;
                }


                switch (alt47) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:397:27: '[' ']'
            	    {
            	    match(input,55,FOLLOW_55_in_methodDeclaratorRest1367); if (state.failed) return ;

            	    match(input,56,FOLLOW_56_in_methodDeclaratorRest1369); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop47;
                }
            } while (true);


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:398:9: ( 'throws' qualifiedNameList )?
            int alt48=2;
            int LA48_0 = input.LA(1);

            if ( (LA48_0==101) ) {
                alt48=1;
            }
            switch (alt48) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:398:10: 'throws' qualifiedNameList
                    {
                    match(input,101,FOLLOW_101_in_methodDeclaratorRest1382); if (state.failed) return ;

                    pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaratorRest1384);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:399:9: ( methodBody | ';' )
            int alt49=2;
            int LA49_0 = input.LA(1);

            if ( (LA49_0==108) ) {
                alt49=1;
            }
            else if ( (LA49_0==48) ) {
                alt49=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 49, 0, input);

                throw nvae;

            }
            switch (alt49) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:399:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_methodDeclaratorRest1400);
                    methodBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:400:13: ';'
                    {
                    match(input,48,FOLLOW_48_in_methodDeclaratorRest1414); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 35, methodDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "methodDeclaratorRest"



    // $ANTLR start "voidMethodDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:404:1: voidMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) ;
    public final void voidMethodDeclaratorRest() throws RecognitionException {
        int voidMethodDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:405:5: ( formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' ) )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:405:9: formalParameters ( 'throws' qualifiedNameList )? ( methodBody | ';' )
            {
            pushFollow(FOLLOW_formalParameters_in_voidMethodDeclaratorRest1447);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:405:26: ( 'throws' qualifiedNameList )?
            int alt50=2;
            int LA50_0 = input.LA(1);

            if ( (LA50_0==101) ) {
                alt50=1;
            }
            switch (alt50) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:405:27: 'throws' qualifiedNameList
                    {
                    match(input,101,FOLLOW_101_in_voidMethodDeclaratorRest1450); if (state.failed) return ;

                    pushFollow(FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1452);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:406:9: ( methodBody | ';' )
            int alt51=2;
            int LA51_0 = input.LA(1);

            if ( (LA51_0==108) ) {
                alt51=1;
            }
            else if ( (LA51_0==48) ) {
                alt51=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 51, 0, input);

                throw nvae;

            }
            switch (alt51) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:406:13: methodBody
                    {
                    pushFollow(FOLLOW_methodBody_in_voidMethodDeclaratorRest1468);
                    methodBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:407:13: ';'
                    {
                    match(input,48,FOLLOW_48_in_voidMethodDeclaratorRest1482); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 36, voidMethodDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "voidMethodDeclaratorRest"



    // $ANTLR start "interfaceMethodDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:411:1: interfaceMethodDeclaratorRest : formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' ;
    public final void interfaceMethodDeclaratorRest() throws RecognitionException {
        int interfaceMethodDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:5: ( formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:9: formalParameters ( '[' ']' )* ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1515);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:26: ( '[' ']' )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==55) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:27: '[' ']'
            	    {
            	    match(input,55,FOLLOW_55_in_interfaceMethodDeclaratorRest1518); if (state.failed) return ;

            	    match(input,56,FOLLOW_56_in_interfaceMethodDeclaratorRest1520); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:37: ( 'throws' qualifiedNameList )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==101) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:412:38: 'throws' qualifiedNameList
                    {
                    match(input,101,FOLLOW_101_in_interfaceMethodDeclaratorRest1525); if (state.failed) return ;

                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1527);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,48,FOLLOW_48_in_interfaceMethodDeclaratorRest1531); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 37, interfaceMethodDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceMethodDeclaratorRest"



    // $ANTLR start "interfaceGenericMethodDecl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:415:1: interfaceGenericMethodDecl : typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest ;
    public final void interfaceGenericMethodDecl() throws RecognitionException {
        int interfaceGenericMethodDecl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:416:5: ( typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:416:9: typeParameters ( type | 'void' ) Identifier interfaceMethodDeclaratorRest
            {
            pushFollow(FOLLOW_typeParameters_in_interfaceGenericMethodDecl1554);
            typeParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:416:24: ( type | 'void' )
            int alt54=2;
            int LA54_0 = input.LA(1);

            if ( (LA54_0==Identifier||LA54_0==60||LA54_0==62||LA54_0==65||LA54_0==70||LA54_0==76||LA54_0==82||LA54_0==84||LA54_0==93) ) {
                alt54=1;
            }
            else if ( (LA54_0==105) ) {
                alt54=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 54, 0, input);

                throw nvae;

            }
            switch (alt54) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:416:25: type
                    {
                    pushFollow(FOLLOW_type_in_interfaceGenericMethodDecl1557);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:416:32: 'void'
                    {
                    match(input,105,FOLLOW_105_in_interfaceGenericMethodDecl1561); if (state.failed) return ;

                    }
                    break;

            }


            match(input,Identifier,FOLLOW_Identifier_in_interfaceGenericMethodDecl1564); if (state.failed) return ;

            pushFollow(FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1574);
            interfaceMethodDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 38, interfaceGenericMethodDecl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "interfaceGenericMethodDecl"



    // $ANTLR start "voidInterfaceMethodDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:420:1: voidInterfaceMethodDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? ';' ;
    public final void voidInterfaceMethodDeclaratorRest() throws RecognitionException {
        int voidInterfaceMethodDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:421:5: ( formalParameters ( 'throws' qualifiedNameList )? ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:421:9: formalParameters ( 'throws' qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1597);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:421:26: ( 'throws' qualifiedNameList )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==101) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:421:27: 'throws' qualifiedNameList
                    {
                    match(input,101,FOLLOW_101_in_voidInterfaceMethodDeclaratorRest1600); if (state.failed) return ;

                    pushFollow(FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1602);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,48,FOLLOW_48_in_voidInterfaceMethodDeclaratorRest1606); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 39, voidInterfaceMethodDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "voidInterfaceMethodDeclaratorRest"



    // $ANTLR start "constructorDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:424:1: constructorDeclaratorRest : formalParameters ( 'throws' qualifiedNameList )? constructorBody ;
    public final void constructorDeclaratorRest() throws RecognitionException {
        int constructorDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:425:5: ( formalParameters ( 'throws' qualifiedNameList )? constructorBody )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:425:9: formalParameters ( 'throws' qualifiedNameList )? constructorBody
            {
            pushFollow(FOLLOW_formalParameters_in_constructorDeclaratorRest1629);
            formalParameters();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:425:26: ( 'throws' qualifiedNameList )?
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==101) ) {
                alt56=1;
            }
            switch (alt56) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:425:27: 'throws' qualifiedNameList
                    {
                    match(input,101,FOLLOW_101_in_constructorDeclaratorRest1632); if (state.failed) return ;

                    pushFollow(FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1634);
                    qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            pushFollow(FOLLOW_constructorBody_in_constructorDeclaratorRest1638);
            constructorBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 40, constructorDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constructorDeclaratorRest"



    // $ANTLR start "constantDeclarator"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:428:1: constantDeclarator : Identifier constantDeclaratorRest ;
    public final void constantDeclarator() throws RecognitionException {
        int constantDeclarator_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:429:5: ( Identifier constantDeclaratorRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:429:9: Identifier constantDeclaratorRest
            {
            match(input,Identifier,FOLLOW_Identifier_in_constantDeclarator1657); if (state.failed) return ;

            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclarator1659);
            constantDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 41, constantDeclarator_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constantDeclarator"


    public static class variableDeclarators_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "variableDeclarators"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:432:1: variableDeclarators : variableDeclarator ( ',' variableDeclarator )* ;
    public final JavaParser.variableDeclarators_return variableDeclarators() throws RecognitionException {
        JavaParser.variableDeclarators_return retval = new JavaParser.variableDeclarators_return();
        retval.start = input.LT(1);

        int variableDeclarators_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:433:5: ( variableDeclarator ( ',' variableDeclarator )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:433:9: variableDeclarator ( ',' variableDeclarator )*
            {
            pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1682);
            variableDeclarator();

            state._fsp--;
            if (state.failed) return retval;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:433:28: ( ',' variableDeclarator )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==39) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:433:29: ',' variableDeclarator
            	    {
            	    match(input,39,FOLLOW_39_in_variableDeclarators1685); if (state.failed) return retval;

            	    pushFollow(FOLLOW_variableDeclarator_in_variableDeclarators1687);
            	    variableDeclarator();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 42, variableDeclarators_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "variableDeclarators"



    // $ANTLR start "variableDeclarator"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:436:1: variableDeclarator : variableDeclaratorId ( '=' variableInitializer )? ;
    public final void variableDeclarator() throws RecognitionException {
        int variableDeclarator_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:437:5: ( variableDeclaratorId ( '=' variableInitializer )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:437:9: variableDeclaratorId ( '=' variableInitializer )?
            {
            pushFollow(FOLLOW_variableDeclaratorId_in_variableDeclarator1708);
            variableDeclaratorId();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:437:30: ( '=' variableInitializer )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==50) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:437:31: '=' variableInitializer
                    {
                    match(input,50,FOLLOW_50_in_variableDeclarator1711); if (state.failed) return ;

                    pushFollow(FOLLOW_variableInitializer_in_variableDeclarator1713);
                    variableInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 43, variableDeclarator_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "variableDeclarator"



    // $ANTLR start "constantDeclaratorsRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:440:1: constantDeclaratorsRest : constantDeclaratorRest ( ',' constantDeclarator )* ;
    public final void constantDeclaratorsRest() throws RecognitionException {
        int constantDeclaratorsRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:441:5: ( constantDeclaratorRest ( ',' constantDeclarator )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:441:9: constantDeclaratorRest ( ',' constantDeclarator )*
            {
            pushFollow(FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1739);
            constantDeclaratorRest();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:441:32: ( ',' constantDeclarator )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==39) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:441:33: ',' constantDeclarator
            	    {
            	    match(input,39,FOLLOW_39_in_constantDeclaratorsRest1742); if (state.failed) return ;

            	    pushFollow(FOLLOW_constantDeclarator_in_constantDeclaratorsRest1744);
            	    constantDeclarator();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 44, constantDeclaratorsRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constantDeclaratorsRest"



    // $ANTLR start "constantDeclaratorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:444:1: constantDeclaratorRest : ( '[' ']' )* '=' variableInitializer ;
    public final void constantDeclaratorRest() throws RecognitionException {
        int constantDeclaratorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:445:5: ( ( '[' ']' )* '=' variableInitializer )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:445:9: ( '[' ']' )* '=' variableInitializer
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:445:9: ( '[' ']' )*
            loop60:
            do {
                int alt60=2;
                int LA60_0 = input.LA(1);

                if ( (LA60_0==55) ) {
                    alt60=1;
                }


                switch (alt60) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:445:10: '[' ']'
            	    {
            	    match(input,55,FOLLOW_55_in_constantDeclaratorRest1766); if (state.failed) return ;

            	    match(input,56,FOLLOW_56_in_constantDeclaratorRest1768); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop60;
                }
            } while (true);


            match(input,50,FOLLOW_50_in_constantDeclaratorRest1772); if (state.failed) return ;

            pushFollow(FOLLOW_variableInitializer_in_constantDeclaratorRest1774);
            variableInitializer();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 45, constantDeclaratorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constantDeclaratorRest"



    // $ANTLR start "variableDeclaratorId"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:448:1: variableDeclaratorId : Identifier ( '[' ']' )* ;
    public final void variableDeclaratorId() throws RecognitionException {
        int variableDeclaratorId_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:449:5: ( Identifier ( '[' ']' )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:449:9: Identifier ( '[' ']' )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_variableDeclaratorId1797); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:449:20: ( '[' ']' )*
            loop61:
            do {
                int alt61=2;
                int LA61_0 = input.LA(1);

                if ( (LA61_0==55) ) {
                    alt61=1;
                }


                switch (alt61) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:449:21: '[' ']'
            	    {
            	    match(input,55,FOLLOW_55_in_variableDeclaratorId1800); if (state.failed) return ;

            	    match(input,56,FOLLOW_56_in_variableDeclaratorId1802); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop61;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 46, variableDeclaratorId_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "variableDeclaratorId"



    // $ANTLR start "variableInitializer"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:452:1: variableInitializer : ( arrayInitializer | expression );
    public final void variableInitializer() throws RecognitionException {
        int variableInitializer_StartIndex = input.index();

        JavaParser.expression_return expression2 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:453:5: ( arrayInitializer | expression )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==108) ) {
                alt62=1;
            }
            else if ( ((LA62_0 >= CharacterLiteral && LA62_0 <= DecimalLiteral)||LA62_0==FloatingPointLiteral||(LA62_0 >= HexLiteral && LA62_0 <= Identifier)||(LA62_0 >= OctalLiteral && LA62_0 <= StringLiteral)||LA62_0==25||LA62_0==32||(LA62_0 >= 36 && LA62_0 <= 37)||(LA62_0 >= 40 && LA62_0 <= 41)||LA62_0==60||LA62_0==62||LA62_0==65||LA62_0==70||LA62_0==73||LA62_0==76||LA62_0==82||LA62_0==84||(LA62_0 >= 86 && LA62_0 <= 87)||LA62_0==93||LA62_0==96||LA62_0==99||LA62_0==103||LA62_0==105||LA62_0==113) ) {
                alt62=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;

            }
            switch (alt62) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:453:9: arrayInitializer
                    {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer1823);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:454:9: expression
                    {
                    pushFollow(FOLLOW_expression_in_variableInitializer1833);
                    expression2=expression();

                    state._fsp--;
                    if (state.failed) return ;

                    if ( state.backtracking==0 ) {System.out.println("Expression Mother "+ (expression2!=null?input.toString(expression2.start,expression2.stop):null));}

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 47, variableInitializer_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "variableInitializer"



    // $ANTLR start "arrayInitializer"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:457:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:5: ( '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:9: '{' ( variableInitializer ( ',' variableInitializer )* ( ',' )? )? '}'
            {
            match(input,108,FOLLOW_108_in_arrayInitializer1862); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:13: ( variableInitializer ( ',' variableInitializer )* ( ',' )? )?
            int alt65=2;
            int LA65_0 = input.LA(1);

            if ( ((LA65_0 >= CharacterLiteral && LA65_0 <= DecimalLiteral)||LA65_0==FloatingPointLiteral||(LA65_0 >= HexLiteral && LA65_0 <= Identifier)||(LA65_0 >= OctalLiteral && LA65_0 <= StringLiteral)||LA65_0==25||LA65_0==32||(LA65_0 >= 36 && LA65_0 <= 37)||(LA65_0 >= 40 && LA65_0 <= 41)||LA65_0==60||LA65_0==62||LA65_0==65||LA65_0==70||LA65_0==73||LA65_0==76||LA65_0==82||LA65_0==84||(LA65_0 >= 86 && LA65_0 <= 87)||LA65_0==93||LA65_0==96||LA65_0==99||LA65_0==103||LA65_0==105||LA65_0==108||LA65_0==113) ) {
                alt65=1;
            }
            switch (alt65) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:14: variableInitializer ( ',' variableInitializer )* ( ',' )?
                    {
                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1865);
                    variableInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:34: ( ',' variableInitializer )*
                    loop63:
                    do {
                        int alt63=2;
                        int LA63_0 = input.LA(1);

                        if ( (LA63_0==39) ) {
                            int LA63_1 = input.LA(2);

                            if ( ((LA63_1 >= CharacterLiteral && LA63_1 <= DecimalLiteral)||LA63_1==FloatingPointLiteral||(LA63_1 >= HexLiteral && LA63_1 <= Identifier)||(LA63_1 >= OctalLiteral && LA63_1 <= StringLiteral)||LA63_1==25||LA63_1==32||(LA63_1 >= 36 && LA63_1 <= 37)||(LA63_1 >= 40 && LA63_1 <= 41)||LA63_1==60||LA63_1==62||LA63_1==65||LA63_1==70||LA63_1==73||LA63_1==76||LA63_1==82||LA63_1==84||(LA63_1 >= 86 && LA63_1 <= 87)||LA63_1==93||LA63_1==96||LA63_1==99||LA63_1==103||LA63_1==105||LA63_1==108||LA63_1==113) ) {
                                alt63=1;
                            }


                        }


                        switch (alt63) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:35: ',' variableInitializer
                    	    {
                    	    match(input,39,FOLLOW_39_in_arrayInitializer1868); if (state.failed) return ;

                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1870);
                    	    variableInitializer();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop63;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:61: ( ',' )?
                    int alt64=2;
                    int LA64_0 = input.LA(1);

                    if ( (LA64_0==39) ) {
                        alt64=1;
                    }
                    switch (alt64) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:458:62: ','
                            {
                            match(input,39,FOLLOW_39_in_arrayInitializer1875); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }


            match(input,112,FOLLOW_112_in_arrayInitializer1882); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 48, arrayInitializer_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "arrayInitializer"



    // $ANTLR start "modifier"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:461:1: modifier : ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );
    public final void modifier() throws RecognitionException {
        int modifier_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:462:5: ( annotation | 'public' | 'protected' | 'private' | 'static' | 'abstract' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )
            int alt66=12;
            switch ( input.LA(1) ) {
            case 54:
                {
                alt66=1;
                }
                break;
            case 91:
                {
                alt66=2;
                }
                break;
            case 90:
                {
                alt66=3;
                }
                break;
            case 89:
                {
                alt66=4;
                }
                break;
            case 94:
                {
                alt66=5;
                }
                break;
            case 59:
                {
                alt66=6;
                }
                break;
            case 74:
                {
                alt66=7;
                }
                break;
            case 85:
                {
                alt66=8;
                }
                break;
            case 98:
                {
                alt66=9;
                }
                break;
            case 102:
                {
                alt66=10;
                }
                break;
            case 106:
                {
                alt66=11;
                }
                break;
            case 95:
                {
                alt66=12;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 66, 0, input);

                throw nvae;

            }

            switch (alt66) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:462:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_modifier1901);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:463:9: 'public'
                    {
                    match(input,91,FOLLOW_91_in_modifier1911); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:464:9: 'protected'
                    {
                    match(input,90,FOLLOW_90_in_modifier1921); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:465:9: 'private'
                    {
                    match(input,89,FOLLOW_89_in_modifier1931); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:466:9: 'static'
                    {
                    match(input,94,FOLLOW_94_in_modifier1941); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:467:9: 'abstract'
                    {
                    match(input,59,FOLLOW_59_in_modifier1951); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:468:9: 'final'
                    {
                    match(input,74,FOLLOW_74_in_modifier1961); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:469:9: 'native'
                    {
                    match(input,85,FOLLOW_85_in_modifier1971); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:470:9: 'synchronized'
                    {
                    match(input,98,FOLLOW_98_in_modifier1981); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:471:9: 'transient'
                    {
                    match(input,102,FOLLOW_102_in_modifier1991); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:472:9: 'volatile'
                    {
                    match(input,106,FOLLOW_106_in_modifier2001); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:473:9: 'strictfp'
                    {
                    match(input,95,FOLLOW_95_in_modifier2011); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 49, modifier_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "modifier"



    // $ANTLR start "packageOrTypeName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:476:1: packageOrTypeName : qualifiedName ;
    public final void packageOrTypeName() throws RecognitionException {
        int packageOrTypeName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:477:5: ( qualifiedName )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:477:9: qualifiedName
            {
            pushFollow(FOLLOW_qualifiedName_in_packageOrTypeName2030);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 50, packageOrTypeName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "packageOrTypeName"



    // $ANTLR start "enumConstantName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:480:1: enumConstantName : Identifier ;
    public final void enumConstantName() throws RecognitionException {
        int enumConstantName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:481:5: ( Identifier )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:481:9: Identifier
            {
            match(input,Identifier,FOLLOW_Identifier_in_enumConstantName2049); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 51, enumConstantName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enumConstantName"



    // $ANTLR start "typeName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:484:1: typeName : qualifiedName ;
    public final void typeName() throws RecognitionException {
        int typeName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:485:5: ( qualifiedName )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:485:9: qualifiedName
            {
            pushFollow(FOLLOW_qualifiedName_in_typeName2068);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 52, typeName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeName"


    public static class type_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "type"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:488:1: type : ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* );
    public final JavaParser.type_return type() throws RecognitionException {
        JavaParser.type_return retval = new JavaParser.type_return();
        retval.start = input.LT(1);

        int type_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:489:2: ( classOrInterfaceType ( '[' ']' )* | primitiveType ( '[' ']' )* )
            int alt69=2;
            int LA69_0 = input.LA(1);

            if ( (LA69_0==Identifier) ) {
                alt69=1;
            }
            else if ( (LA69_0==60||LA69_0==62||LA69_0==65||LA69_0==70||LA69_0==76||LA69_0==82||LA69_0==84||LA69_0==93) ) {
                alt69=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 69, 0, input);

                throw nvae;

            }
            switch (alt69) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:489:4: classOrInterfaceType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_type2082);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return retval;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:489:25: ( '[' ']' )*
                    loop67:
                    do {
                        int alt67=2;
                        int LA67_0 = input.LA(1);

                        if ( (LA67_0==55) ) {
                            alt67=1;
                        }


                        switch (alt67) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:489:26: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_type2085); if (state.failed) return retval;

                    	    match(input,56,FOLLOW_56_in_type2087); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop67;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:490:4: primitiveType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_primitiveType_in_type2094);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return retval;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:490:18: ( '[' ']' )*
                    loop68:
                    do {
                        int alt68=2;
                        int LA68_0 = input.LA(1);

                        if ( (LA68_0==55) ) {
                            alt68=1;
                        }


                        switch (alt68) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:490:19: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_type2097); if (state.failed) return retval;

                    	    match(input,56,FOLLOW_56_in_type2099); if (state.failed) return retval;

                    	    }
                    	    break;

                    	default :
                    	    break loop68;
                        }
                    } while (true);


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 53, type_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "type"



    // $ANTLR start "classOrInterfaceType"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:493:1: classOrInterfaceType : Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* ;
    public final void classOrInterfaceType() throws RecognitionException {
        int classOrInterfaceType_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:2: ( Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:4: Identifier ( typeArguments )? ( '.' Identifier ( typeArguments )? )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_classOrInterfaceType2112); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:15: ( typeArguments )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==49) ) {
                int LA70_1 = input.LA(2);

                if ( (LA70_1==Identifier||LA70_1==53||LA70_1==60||LA70_1==62||LA70_1==65||LA70_1==70||LA70_1==76||LA70_1==82||LA70_1==84||LA70_1==93) ) {
                    alt70=1;
                }
            }
            switch (alt70) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:15: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2114);
                    typeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:30: ( '.' Identifier ( typeArguments )? )*
            loop72:
            do {
                int alt72=2;
                int LA72_0 = input.LA(1);

                if ( (LA72_0==43) ) {
                    alt72=1;
                }


                switch (alt72) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:31: '.' Identifier ( typeArguments )?
            	    {
            	    match(input,43,FOLLOW_43_in_classOrInterfaceType2118); if (state.failed) return ;

            	    match(input,Identifier,FOLLOW_Identifier_in_classOrInterfaceType2120); if (state.failed) return ;

            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:46: ( typeArguments )?
            	    int alt71=2;
            	    int LA71_0 = input.LA(1);

            	    if ( (LA71_0==49) ) {
            	        int LA71_1 = input.LA(2);

            	        if ( (LA71_1==Identifier||LA71_1==53||LA71_1==60||LA71_1==62||LA71_1==65||LA71_1==70||LA71_1==76||LA71_1==82||LA71_1==84||LA71_1==93) ) {
            	            alt71=1;
            	        }
            	    }
            	    switch (alt71) {
            	        case 1 :
            	            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:494:46: typeArguments
            	            {
            	            pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2122);
            	            typeArguments();

            	            state._fsp--;
            	            if (state.failed) return ;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop72;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 54, classOrInterfaceType_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classOrInterfaceType"



    // $ANTLR start "primitiveType"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:497:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final void primitiveType() throws RecognitionException {
        int primitiveType_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:498:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:
            {
            if ( input.LA(1)==60||input.LA(1)==62||input.LA(1)==65||input.LA(1)==70||input.LA(1)==76||input.LA(1)==82||input.LA(1)==84||input.LA(1)==93 ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 55, primitiveType_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "primitiveType"



    // $ANTLR start "variableModifier"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:508:1: variableModifier : ( 'final' | annotation );
    public final void variableModifier() throws RecognitionException {
        int variableModifier_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:509:5: ( 'final' | annotation )
            int alt73=2;
            int LA73_0 = input.LA(1);

            if ( (LA73_0==74) ) {
                alt73=1;
            }
            else if ( (LA73_0==54) ) {
                alt73=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;

            }
            switch (alt73) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:509:9: 'final'
                    {
                    match(input,74,FOLLOW_74_in_variableModifier2231); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:510:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_variableModifier2241);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 56, variableModifier_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "variableModifier"



    // $ANTLR start "typeArguments"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:513:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final void typeArguments() throws RecognitionException {
        int typeArguments_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:514:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:514:9: '<' typeArgument ( ',' typeArgument )* '>'
            {
            match(input,49,FOLLOW_49_in_typeArguments2260); if (state.failed) return ;

            pushFollow(FOLLOW_typeArgument_in_typeArguments2262);
            typeArgument();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:514:26: ( ',' typeArgument )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==39) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:514:27: ',' typeArgument
            	    {
            	    match(input,39,FOLLOW_39_in_typeArguments2265); if (state.failed) return ;

            	    pushFollow(FOLLOW_typeArgument_in_typeArguments2267);
            	    typeArgument();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);


            match(input,52,FOLLOW_52_in_typeArguments2271); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 57, typeArguments_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeArguments"



    // $ANTLR start "typeArgument"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:517:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final void typeArgument() throws RecognitionException {
        int typeArgument_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:518:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt76=2;
            int LA76_0 = input.LA(1);

            if ( (LA76_0==Identifier||LA76_0==60||LA76_0==62||LA76_0==65||LA76_0==70||LA76_0==76||LA76_0==82||LA76_0==84||LA76_0==93) ) {
                alt76=1;
            }
            else if ( (LA76_0==53) ) {
                alt76=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 76, 0, input);

                throw nvae;

            }
            switch (alt76) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:518:9: type
                    {
                    pushFollow(FOLLOW_type_in_typeArgument2294);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:519:9: '?' ( ( 'extends' | 'super' ) type )?
                    {
                    match(input,53,FOLLOW_53_in_typeArgument2304); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:519:13: ( ( 'extends' | 'super' ) type )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==72||LA75_0==96) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:519:14: ( 'extends' | 'super' ) type
                            {
                            if ( input.LA(1)==72||input.LA(1)==96 ) {
                                input.consume();
                                state.errorRecovery=false;
                                state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            pushFollow(FOLLOW_type_in_typeArgument2315);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 58, typeArgument_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "typeArgument"



    // $ANTLR start "qualifiedNameList"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:522:1: qualifiedNameList : qualifiedName ( ',' qualifiedName )* ;
    public final void qualifiedNameList() throws RecognitionException {
        int qualifiedNameList_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:523:5: ( qualifiedName ( ',' qualifiedName )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:523:9: qualifiedName ( ',' qualifiedName )*
            {
            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2340);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:523:23: ( ',' qualifiedName )*
            loop77:
            do {
                int alt77=2;
                int LA77_0 = input.LA(1);

                if ( (LA77_0==39) ) {
                    alt77=1;
                }


                switch (alt77) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:523:24: ',' qualifiedName
            	    {
            	    match(input,39,FOLLOW_39_in_qualifiedNameList2343); if (state.failed) return ;

            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2345);
            	    qualifiedName();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop77;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 59, qualifiedNameList_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "qualifiedNameList"



    // $ANTLR start "formalParameters"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:526:1: formalParameters : '(' ( formalParameterDecls )? ')' ;
    public final void formalParameters() throws RecognitionException {
        int formalParameters_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:527:5: ( '(' ( formalParameterDecls )? ')' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:527:9: '(' ( formalParameterDecls )? ')'
            {
            match(input,32,FOLLOW_32_in_formalParameters2366); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:527:13: ( formalParameterDecls )?
            int alt78=2;
            int LA78_0 = input.LA(1);

            if ( (LA78_0==Identifier||LA78_0==54||LA78_0==60||LA78_0==62||LA78_0==65||LA78_0==70||LA78_0==74||LA78_0==76||LA78_0==82||LA78_0==84||LA78_0==93) ) {
                alt78=1;
            }
            switch (alt78) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:527:13: formalParameterDecls
                    {
                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters2368);
                    formalParameterDecls();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,33,FOLLOW_33_in_formalParameters2371); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 60, formalParameters_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "formalParameters"



    // $ANTLR start "formalParameterDecls"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:530:1: formalParameterDecls : variableModifiers type formalParameterDeclsRest ;
    public final void formalParameterDecls() throws RecognitionException {
        int formalParameterDecls_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:531:5: ( variableModifiers type formalParameterDeclsRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:531:9: variableModifiers type formalParameterDeclsRest
            {
            pushFollow(FOLLOW_variableModifiers_in_formalParameterDecls2394);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_type_in_formalParameterDecls2396);
            type();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2398);
            formalParameterDeclsRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 61, formalParameterDecls_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "formalParameterDecls"



    // $ANTLR start "formalParameterDeclsRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:534:1: formalParameterDeclsRest : ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId );
    public final void formalParameterDeclsRest() throws RecognitionException {
        int formalParameterDeclsRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:535:5: ( variableDeclaratorId ( ',' formalParameterDecls )? | '...' variableDeclaratorId )
            int alt80=2;
            int LA80_0 = input.LA(1);

            if ( (LA80_0==Identifier) ) {
                alt80=1;
            }
            else if ( (LA80_0==44) ) {
                alt80=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 80, 0, input);

                throw nvae;

            }
            switch (alt80) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:535:9: variableDeclaratorId ( ',' formalParameterDecls )?
                    {
                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2421);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:535:30: ( ',' formalParameterDecls )?
                    int alt79=2;
                    int LA79_0 = input.LA(1);

                    if ( (LA79_0==39) ) {
                        alt79=1;
                    }
                    switch (alt79) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:535:31: ',' formalParameterDecls
                            {
                            match(input,39,FOLLOW_39_in_formalParameterDeclsRest2424); if (state.failed) return ;

                            pushFollow(FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2426);
                            formalParameterDecls();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:536:9: '...' variableDeclaratorId
                    {
                    match(input,44,FOLLOW_44_in_formalParameterDeclsRest2438); if (state.failed) return ;

                    pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2440);
                    variableDeclaratorId();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 62, formalParameterDeclsRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "formalParameterDeclsRest"



    // $ANTLR start "methodBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:539:1: methodBody : block ;
    public final void methodBody() throws RecognitionException {
        int methodBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:540:5: ( block )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:540:9: block
            {
            pushFollow(FOLLOW_block_in_methodBody2463);
            block();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 63, methodBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "methodBody"



    // $ANTLR start "constructorBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:543:1: constructorBody : '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' ;
    public final void constructorBody() throws RecognitionException {
        int constructorBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:5: ( '{' ( explicitConstructorInvocation )? ( blockStatement )* '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:9: '{' ( explicitConstructorInvocation )? ( blockStatement )* '}'
            {
            match(input,108,FOLLOW_108_in_constructorBody2482); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:13: ( explicitConstructorInvocation )?
            int alt81=2;
            switch ( input.LA(1) ) {
                case 49:
                    {
                    alt81=1;
                    }
                    break;
                case 99:
                    {
                    int LA81_2 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 32:
                    {
                    int LA81_3 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 96:
                    {
                    int LA81_4 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case DecimalLiteral:
                case HexLiteral:
                case OctalLiteral:
                    {
                    int LA81_5 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case FloatingPointLiteral:
                    {
                    int LA81_6 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case CharacterLiteral:
                    {
                    int LA81_7 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case StringLiteral:
                    {
                    int LA81_8 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 73:
                case 103:
                    {
                    int LA81_9 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 87:
                    {
                    int LA81_10 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 86:
                    {
                    int LA81_11 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case Identifier:
                    {
                    int LA81_12 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 60:
                case 62:
                case 65:
                case 70:
                case 76:
                case 82:
                case 84:
                case 93:
                    {
                    int LA81_13 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
                case 105:
                    {
                    int LA81_14 = input.LA(2);

                    if ( (synpred113_Java()) ) {
                        alt81=1;
                    }
                    }
                    break;
            }

            switch (alt81) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:13: explicitConstructorInvocation
                    {
                    pushFollow(FOLLOW_explicitConstructorInvocation_in_constructorBody2484);
                    explicitConstructorInvocation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:44: ( blockStatement )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( (LA82_0==ASSERT||(LA82_0 >= CharacterLiteral && LA82_0 <= ENUM)||LA82_0==FloatingPointLiteral||(LA82_0 >= HexLiteral && LA82_0 <= Identifier)||(LA82_0 >= OctalLiteral && LA82_0 <= StringLiteral)||LA82_0==25||LA82_0==32||(LA82_0 >= 36 && LA82_0 <= 37)||(LA82_0 >= 40 && LA82_0 <= 41)||LA82_0==48||LA82_0==54||(LA82_0 >= 59 && LA82_0 <= 62)||(LA82_0 >= 65 && LA82_0 <= 67)||(LA82_0 >= 69 && LA82_0 <= 70)||(LA82_0 >= 73 && LA82_0 <= 74)||(LA82_0 >= 76 && LA82_0 <= 78)||(LA82_0 >= 82 && LA82_0 <= 84)||(LA82_0 >= 86 && LA82_0 <= 87)||(LA82_0 >= 89 && LA82_0 <= 100)||(LA82_0 >= 103 && LA82_0 <= 105)||(LA82_0 >= 107 && LA82_0 <= 108)||LA82_0==113) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:44: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_constructorBody2487);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop82;
                }
            } while (true);


            match(input,112,FOLLOW_112_in_constructorBody2490); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 64, constructorBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constructorBody"



    // $ANTLR start "explicitConstructorInvocation"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:547:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );
    public final void explicitConstructorInvocation() throws RecognitionException {
        int explicitConstructorInvocation_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:5: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' )
            int alt85=2;
            switch ( input.LA(1) ) {
            case 49:
                {
                alt85=1;
                }
                break;
            case 99:
                {
                int LA85_2 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt85=1;
                }
                else if ( (true) ) {
                    alt85=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 85, 2, input);

                    throw nvae;

                }
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case Identifier:
            case OctalLiteral:
            case StringLiteral:
            case 32:
            case 60:
            case 62:
            case 65:
            case 70:
            case 73:
            case 76:
            case 82:
            case 84:
            case 86:
            case 87:
            case 93:
            case 103:
            case 105:
                {
                alt85=2;
                }
                break;
            case 96:
                {
                int LA85_4 = input.LA(2);

                if ( (synpred117_Java()) ) {
                    alt85=1;
                }
                else if ( (true) ) {
                    alt85=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 85, 4, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 85, 0, input);

                throw nvae;

            }

            switch (alt85) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: ( nonWildcardTypeArguments )?
                    int alt83=2;
                    int LA83_0 = input.LA(1);

                    if ( (LA83_0==49) ) {
                        alt83=1;
                    }
                    switch (alt83) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2509);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    if ( input.LA(1)==96||input.LA(1)==99 ) {
                        input.consume();
                        state.errorRecovery=false;
                        state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2520);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_explicitConstructorInvocation2522); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:549:9: primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';'
                    {
                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation2532);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,43,FOLLOW_43_in_explicitConstructorInvocation2534); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:549:21: ( nonWildcardTypeArguments )?
                    int alt84=2;
                    int LA84_0 = input.LA(1);

                    if ( (LA84_0==49) ) {
                        alt84=1;
                    }
                    switch (alt84) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:549:21: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2536);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,96,FOLLOW_96_in_explicitConstructorInvocation2539); if (state.failed) return ;

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation2541);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_explicitConstructorInvocation2543); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 65, explicitConstructorInvocation_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "explicitConstructorInvocation"



    // $ANTLR start "qualifiedName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:553:1: qualifiedName : Identifier ( '.' Identifier )* ;
    public final void qualifiedName() throws RecognitionException {
        int qualifiedName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:554:5: ( Identifier ( '.' Identifier )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:554:9: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_qualifiedName2563); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:554:20: ( '.' Identifier )*
            loop86:
            do {
                int alt86=2;
                int LA86_0 = input.LA(1);

                if ( (LA86_0==43) ) {
                    int LA86_2 = input.LA(2);

                    if ( (LA86_2==Identifier) ) {
                        alt86=1;
                    }


                }


                switch (alt86) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:554:21: '.' Identifier
            	    {
            	    match(input,43,FOLLOW_43_in_qualifiedName2566); if (state.failed) return ;

            	    match(input,Identifier,FOLLOW_Identifier_in_qualifiedName2568); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop86;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 66, qualifiedName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "qualifiedName"



    // $ANTLR start "literal"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:557:1: literal : ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 67) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:558:5: ( integerLiteral | FloatingPointLiteral | CharacterLiteral | StringLiteral | booleanLiteral | 'null' )
            int alt87=6;
            switch ( input.LA(1) ) {
            case DecimalLiteral:
            case HexLiteral:
            case OctalLiteral:
                {
                alt87=1;
                }
                break;
            case FloatingPointLiteral:
                {
                alt87=2;
                }
                break;
            case CharacterLiteral:
                {
                alt87=3;
                }
                break;
            case StringLiteral:
                {
                alt87=4;
                }
                break;
            case 73:
            case 103:
                {
                alt87=5;
                }
                break;
            case 87:
                {
                alt87=6;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 87, 0, input);

                throw nvae;

            }

            switch (alt87) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:558:9: integerLiteral
                    {
                    pushFollow(FOLLOW_integerLiteral_in_literal2594);
                    integerLiteral();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:559:9: FloatingPointLiteral
                    {
                    match(input,FloatingPointLiteral,FOLLOW_FloatingPointLiteral_in_literal2604); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:560:9: CharacterLiteral
                    {
                    match(input,CharacterLiteral,FOLLOW_CharacterLiteral_in_literal2614); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:561:9: StringLiteral
                    {
                    match(input,StringLiteral,FOLLOW_StringLiteral_in_literal2624); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:562:9: booleanLiteral
                    {
                    pushFollow(FOLLOW_booleanLiteral_in_literal2634);
                    booleanLiteral();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:563:9: 'null'
                    {
                    match(input,87,FOLLOW_87_in_literal2644); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 67, literal_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "literal"



    // $ANTLR start "integerLiteral"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:566:1: integerLiteral : ( HexLiteral | OctalLiteral | DecimalLiteral );
    public final void integerLiteral() throws RecognitionException {
        int integerLiteral_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 68) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:567:5: ( HexLiteral | OctalLiteral | DecimalLiteral )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:
            {
            if ( input.LA(1)==DecimalLiteral||input.LA(1)==HexLiteral||input.LA(1)==OctalLiteral ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 68, integerLiteral_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "integerLiteral"



    // $ANTLR start "booleanLiteral"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:572:1: booleanLiteral : ( 'true' | 'false' );
    public final void booleanLiteral() throws RecognitionException {
        int booleanLiteral_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 69) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:573:5: ( 'true' | 'false' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:
            {
            if ( input.LA(1)==73||input.LA(1)==103 ) {
                input.consume();
                state.errorRecovery=false;
                state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 69, booleanLiteral_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "booleanLiteral"



    // $ANTLR start "annotations"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:579:1: annotations : ( annotation )+ ;
    public final void annotations() throws RecognitionException {
        int annotations_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 70) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:5: ( ( annotation )+ )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:9: ( annotation )+
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:9: ( annotation )+
            int cnt88=0;
            loop88:
            do {
                int alt88=2;
                int LA88_0 = input.LA(1);

                if ( (LA88_0==54) ) {
                    int LA88_2 = input.LA(2);

                    if ( (LA88_2==Identifier) ) {
                        int LA88_3 = input.LA(3);

                        if ( (synpred128_Java()) ) {
                            alt88=1;
                        }


                    }


                }


                switch (alt88) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:9: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_annotations2733);
            	    annotation();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt88 >= 1 ) break loop88;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(88, input);
                        throw eee;
                }
                cnt88++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 70, annotations_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotations"



    // $ANTLR start "annotation"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:583:1: annotation : '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 71) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:5: ( '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:9: '@' annotationName ( '(' ( elementValuePairs | elementValue )? ')' )?
            {
            match(input,54,FOLLOW_54_in_annotation2753); if (state.failed) return ;

            pushFollow(FOLLOW_annotationName_in_annotation2755);
            annotationName();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:28: ( '(' ( elementValuePairs | elementValue )? ')' )?
            int alt90=2;
            int LA90_0 = input.LA(1);

            if ( (LA90_0==32) ) {
                alt90=1;
            }
            switch (alt90) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:30: '(' ( elementValuePairs | elementValue )? ')'
                    {
                    match(input,32,FOLLOW_32_in_annotation2759); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:34: ( elementValuePairs | elementValue )?
                    int alt89=3;
                    int LA89_0 = input.LA(1);

                    if ( (LA89_0==Identifier) ) {
                        int LA89_1 = input.LA(2);

                        if ( (LA89_1==50) ) {
                            alt89=1;
                        }
                        else if ( ((LA89_1 >= 26 && LA89_1 <= 27)||(LA89_1 >= 29 && LA89_1 <= 30)||(LA89_1 >= 32 && LA89_1 <= 34)||(LA89_1 >= 36 && LA89_1 <= 37)||(LA89_1 >= 40 && LA89_1 <= 41)||LA89_1==43||LA89_1==45||LA89_1==49||(LA89_1 >= 51 && LA89_1 <= 53)||LA89_1==55||LA89_1==57||LA89_1==81||LA89_1==109||LA89_1==111) ) {
                            alt89=2;
                        }
                    }
                    else if ( ((LA89_0 >= CharacterLiteral && LA89_0 <= DecimalLiteral)||LA89_0==FloatingPointLiteral||LA89_0==HexLiteral||(LA89_0 >= OctalLiteral && LA89_0 <= StringLiteral)||LA89_0==25||LA89_0==32||(LA89_0 >= 36 && LA89_0 <= 37)||(LA89_0 >= 40 && LA89_0 <= 41)||LA89_0==54||LA89_0==60||LA89_0==62||LA89_0==65||LA89_0==70||LA89_0==73||LA89_0==76||LA89_0==82||LA89_0==84||(LA89_0 >= 86 && LA89_0 <= 87)||LA89_0==93||LA89_0==96||LA89_0==99||LA89_0==103||LA89_0==105||LA89_0==108||LA89_0==113) ) {
                        alt89=2;
                    }
                    switch (alt89) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:36: elementValuePairs
                            {
                            pushFollow(FOLLOW_elementValuePairs_in_annotation2763);
                            elementValuePairs();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:584:56: elementValue
                            {
                            pushFollow(FOLLOW_elementValue_in_annotation2767);
                            elementValue();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,33,FOLLOW_33_in_annotation2772); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 71, annotation_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotation"



    // $ANTLR start "annotationName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:587:1: annotationName : Identifier ( '.' Identifier )* ;
    public final void annotationName() throws RecognitionException {
        int annotationName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 72) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:588:5: ( Identifier ( '.' Identifier )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:588:7: Identifier ( '.' Identifier )*
            {
            match(input,Identifier,FOLLOW_Identifier_in_annotationName2796); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:588:18: ( '.' Identifier )*
            loop91:
            do {
                int alt91=2;
                int LA91_0 = input.LA(1);

                if ( (LA91_0==43) ) {
                    alt91=1;
                }


                switch (alt91) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:588:19: '.' Identifier
            	    {
            	    match(input,43,FOLLOW_43_in_annotationName2799); if (state.failed) return ;

            	    match(input,Identifier,FOLLOW_Identifier_in_annotationName2801); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop91;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 72, annotationName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationName"



    // $ANTLR start "elementValuePairs"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:591:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final void elementValuePairs() throws RecognitionException {
        int elementValuePairs_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 73) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:592:5: ( elementValuePair ( ',' elementValuePair )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:592:9: elementValuePair ( ',' elementValuePair )*
            {
            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2822);
            elementValuePair();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:592:26: ( ',' elementValuePair )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==39) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:592:27: ',' elementValuePair
            	    {
            	    match(input,39,FOLLOW_39_in_elementValuePairs2825); if (state.failed) return ;

            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs2827);
            	    elementValuePair();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 73, elementValuePairs_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "elementValuePairs"



    // $ANTLR start "elementValuePair"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:595:1: elementValuePair : Identifier '=' elementValue ;
    public final void elementValuePair() throws RecognitionException {
        int elementValuePair_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 74) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:596:5: ( Identifier '=' elementValue )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:596:9: Identifier '=' elementValue
            {
            match(input,Identifier,FOLLOW_Identifier_in_elementValuePair2848); if (state.failed) return ;

            match(input,50,FOLLOW_50_in_elementValuePair2850); if (state.failed) return ;

            pushFollow(FOLLOW_elementValue_in_elementValuePair2852);
            elementValue();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 74, elementValuePair_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "elementValuePair"



    // $ANTLR start "elementValue"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:599:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final void elementValue() throws RecognitionException {
        int elementValue_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 75) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:600:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt93=3;
            switch ( input.LA(1) ) {
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case Identifier:
            case OctalLiteral:
            case StringLiteral:
            case 25:
            case 32:
            case 36:
            case 37:
            case 40:
            case 41:
            case 60:
            case 62:
            case 65:
            case 70:
            case 73:
            case 76:
            case 82:
            case 84:
            case 86:
            case 87:
            case 93:
            case 96:
            case 99:
            case 103:
            case 105:
            case 113:
                {
                alt93=1;
                }
                break;
            case 54:
                {
                alt93=2;
                }
                break;
            case 108:
                {
                alt93=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 93, 0, input);

                throw nvae;

            }

            switch (alt93) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:600:9: conditionalExpression
                    {
                    pushFollow(FOLLOW_conditionalExpression_in_elementValue2875);
                    conditionalExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:601:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_elementValue2885);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:602:9: elementValueArrayInitializer
                    {
                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue2895);
                    elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 75, elementValue_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "elementValue"



    // $ANTLR start "elementValueArrayInitializer"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:605:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
    public final void elementValueArrayInitializer() throws RecognitionException {
        int elementValueArrayInitializer_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 76) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
            {
            match(input,108,FOLLOW_108_in_elementValueArrayInitializer2918); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:13: ( elementValue ( ',' elementValue )* )?
            int alt95=2;
            int LA95_0 = input.LA(1);

            if ( ((LA95_0 >= CharacterLiteral && LA95_0 <= DecimalLiteral)||LA95_0==FloatingPointLiteral||(LA95_0 >= HexLiteral && LA95_0 <= Identifier)||(LA95_0 >= OctalLiteral && LA95_0 <= StringLiteral)||LA95_0==25||LA95_0==32||(LA95_0 >= 36 && LA95_0 <= 37)||(LA95_0 >= 40 && LA95_0 <= 41)||LA95_0==54||LA95_0==60||LA95_0==62||LA95_0==65||LA95_0==70||LA95_0==73||LA95_0==76||LA95_0==82||LA95_0==84||(LA95_0 >= 86 && LA95_0 <= 87)||LA95_0==93||LA95_0==96||LA95_0==99||LA95_0==103||LA95_0==105||LA95_0==108||LA95_0==113) ) {
                alt95=1;
            }
            switch (alt95) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:14: elementValue ( ',' elementValue )*
                    {
                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2921);
                    elementValue();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:27: ( ',' elementValue )*
                    loop94:
                    do {
                        int alt94=2;
                        int LA94_0 = input.LA(1);

                        if ( (LA94_0==39) ) {
                            int LA94_1 = input.LA(2);

                            if ( ((LA94_1 >= CharacterLiteral && LA94_1 <= DecimalLiteral)||LA94_1==FloatingPointLiteral||(LA94_1 >= HexLiteral && LA94_1 <= Identifier)||(LA94_1 >= OctalLiteral && LA94_1 <= StringLiteral)||LA94_1==25||LA94_1==32||(LA94_1 >= 36 && LA94_1 <= 37)||(LA94_1 >= 40 && LA94_1 <= 41)||LA94_1==54||LA94_1==60||LA94_1==62||LA94_1==65||LA94_1==70||LA94_1==73||LA94_1==76||LA94_1==82||LA94_1==84||(LA94_1 >= 86 && LA94_1 <= 87)||LA94_1==93||LA94_1==96||LA94_1==99||LA94_1==103||LA94_1==105||LA94_1==108||LA94_1==113) ) {
                                alt94=1;
                            }


                        }


                        switch (alt94) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:28: ',' elementValue
                    	    {
                    	    match(input,39,FOLLOW_39_in_elementValueArrayInitializer2924); if (state.failed) return ;

                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer2926);
                    	    elementValue();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop94;
                        }
                    } while (true);


                    }
                    break;

            }


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:49: ( ',' )?
            int alt96=2;
            int LA96_0 = input.LA(1);

            if ( (LA96_0==39) ) {
                alt96=1;
            }
            switch (alt96) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:606:50: ','
                    {
                    match(input,39,FOLLOW_39_in_elementValueArrayInitializer2933); if (state.failed) return ;

                    }
                    break;

            }


            match(input,112,FOLLOW_112_in_elementValueArrayInitializer2937); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 76, elementValueArrayInitializer_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "elementValueArrayInitializer"



    // $ANTLR start "annotationTypeDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:609:1: annotationTypeDeclaration : '@' 'interface' Identifier annotationTypeBody ;
    public final void annotationTypeDeclaration() throws RecognitionException {
        int annotationTypeDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 77) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:610:5: ( '@' 'interface' Identifier annotationTypeBody )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:610:9: '@' 'interface' Identifier annotationTypeBody
            {
            match(input,54,FOLLOW_54_in_annotationTypeDeclaration2960); if (state.failed) return ;

            match(input,83,FOLLOW_83_in_annotationTypeDeclaration2962); if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_annotationTypeDeclaration2964); if (state.failed) return ;

            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2966);
            annotationTypeBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 77, annotationTypeDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationTypeDeclaration"



    // $ANTLR start "annotationTypeBody"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:613:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
    public final void annotationTypeBody() throws RecognitionException {
        int annotationTypeBody_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 78) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:614:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:614:9: '{' ( annotationTypeElementDeclaration )* '}'
            {
            match(input,108,FOLLOW_108_in_annotationTypeBody2989); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:614:13: ( annotationTypeElementDeclaration )*
            loop97:
            do {
                int alt97=2;
                int LA97_0 = input.LA(1);

                if ( (LA97_0==ENUM||LA97_0==Identifier||LA97_0==49||LA97_0==54||(LA97_0 >= 59 && LA97_0 <= 60)||LA97_0==62||(LA97_0 >= 65 && LA97_0 <= 66)||LA97_0==70||LA97_0==74||LA97_0==76||(LA97_0 >= 82 && LA97_0 <= 85)||(LA97_0 >= 89 && LA97_0 <= 91)||(LA97_0 >= 93 && LA97_0 <= 95)||LA97_0==98||LA97_0==102||(LA97_0 >= 105 && LA97_0 <= 106)) ) {
                    alt97=1;
                }


                switch (alt97) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:614:14: annotationTypeElementDeclaration
            	    {
            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody2992);
            	    annotationTypeElementDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop97;
                }
            } while (true);


            match(input,112,FOLLOW_112_in_annotationTypeBody2996); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 78, annotationTypeBody_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationTypeBody"



    // $ANTLR start "annotationTypeElementDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:617:1: annotationTypeElementDeclaration : modifiers annotationTypeElementRest ;
    public final void annotationTypeElementDeclaration() throws RecognitionException {
        int annotationTypeElementDeclaration_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 79) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:618:5: ( modifiers annotationTypeElementRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:618:9: modifiers annotationTypeElementRest
            {
            pushFollow(FOLLOW_modifiers_in_annotationTypeElementDeclaration3019);
            modifiers();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3021);
            annotationTypeElementRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 79, annotationTypeElementDeclaration_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationTypeElementDeclaration"



    // $ANTLR start "annotationTypeElementRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:621:1: annotationTypeElementRest : ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? );
    public final void annotationTypeElementRest() throws RecognitionException {
        int annotationTypeElementRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 80) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:622:5: ( type annotationMethodOrConstantRest ';' | normalClassDeclaration ( ';' )? | normalInterfaceDeclaration ( ';' )? | enumDeclaration ( ';' )? | annotationTypeDeclaration ( ';' )? )
            int alt102=5;
            switch ( input.LA(1) ) {
            case Identifier:
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                alt102=1;
                }
                break;
            case 66:
                {
                alt102=2;
                }
                break;
            case 83:
                {
                alt102=3;
                }
                break;
            case ENUM:
                {
                alt102=4;
                }
                break;
            case 54:
                {
                alt102=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;

            }

            switch (alt102) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:622:9: type annotationMethodOrConstantRest ';'
                    {
                    pushFollow(FOLLOW_type_in_annotationTypeElementRest3044);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3046);
                    annotationMethodOrConstantRest();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_annotationTypeElementRest3048); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:623:9: normalClassDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3058);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:623:32: ( ';' )?
                    int alt98=2;
                    int LA98_0 = input.LA(1);

                    if ( (LA98_0==48) ) {
                        alt98=1;
                    }
                    switch (alt98) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:623:32: ';'
                            {
                            match(input,48,FOLLOW_48_in_annotationTypeElementRest3060); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:624:9: normalInterfaceDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3071);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:624:36: ( ';' )?
                    int alt99=2;
                    int LA99_0 = input.LA(1);

                    if ( (LA99_0==48) ) {
                        alt99=1;
                    }
                    switch (alt99) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:624:36: ';'
                            {
                            match(input,48,FOLLOW_48_in_annotationTypeElementRest3073); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:625:9: enumDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementRest3084);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:625:25: ( ';' )?
                    int alt100=2;
                    int LA100_0 = input.LA(1);

                    if ( (LA100_0==48) ) {
                        alt100=1;
                    }
                    switch (alt100) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:625:25: ';'
                            {
                            match(input,48,FOLLOW_48_in_annotationTypeElementRest3086); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:626:9: annotationTypeDeclaration ( ';' )?
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3097);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:626:35: ( ';' )?
                    int alt101=2;
                    int LA101_0 = input.LA(1);

                    if ( (LA101_0==48) ) {
                        alt101=1;
                    }
                    switch (alt101) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:626:35: ';'
                            {
                            match(input,48,FOLLOW_48_in_annotationTypeElementRest3099); if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 80, annotationTypeElementRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationTypeElementRest"



    // $ANTLR start "annotationMethodOrConstantRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:629:1: annotationMethodOrConstantRest : ( annotationMethodRest | annotationConstantRest );
    public final void annotationMethodOrConstantRest() throws RecognitionException {
        int annotationMethodOrConstantRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 81) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:630:5: ( annotationMethodRest | annotationConstantRest )
            int alt103=2;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==Identifier) ) {
                int LA103_1 = input.LA(2);

                if ( (LA103_1==32) ) {
                    alt103=1;
                }
                else if ( (LA103_1==39||LA103_1==48||LA103_1==50||LA103_1==55) ) {
                    alt103=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 103, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 103, 0, input);

                throw nvae;

            }
            switch (alt103) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:630:9: annotationMethodRest
                    {
                    pushFollow(FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3123);
                    annotationMethodRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:631:9: annotationConstantRest
                    {
                    pushFollow(FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3133);
                    annotationConstantRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 81, annotationMethodOrConstantRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationMethodOrConstantRest"



    // $ANTLR start "annotationMethodRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:634:1: annotationMethodRest : Identifier '(' ')' ( defaultValue )? ;
    public final void annotationMethodRest() throws RecognitionException {
        int annotationMethodRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 82) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:635:5: ( Identifier '(' ')' ( defaultValue )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:635:9: Identifier '(' ')' ( defaultValue )?
            {
            match(input,Identifier,FOLLOW_Identifier_in_annotationMethodRest3156); if (state.failed) return ;

            match(input,32,FOLLOW_32_in_annotationMethodRest3158); if (state.failed) return ;

            match(input,33,FOLLOW_33_in_annotationMethodRest3160); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:635:28: ( defaultValue )?
            int alt104=2;
            int LA104_0 = input.LA(1);

            if ( (LA104_0==68) ) {
                alt104=1;
            }
            switch (alt104) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:635:28: defaultValue
                    {
                    pushFollow(FOLLOW_defaultValue_in_annotationMethodRest3162);
                    defaultValue();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 82, annotationMethodRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationMethodRest"



    // $ANTLR start "annotationConstantRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:638:1: annotationConstantRest : variableDeclarators ;
    public final void annotationConstantRest() throws RecognitionException {
        int annotationConstantRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 83) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:639:5: ( variableDeclarators )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:639:9: variableDeclarators
            {
            pushFollow(FOLLOW_variableDeclarators_in_annotationConstantRest3186);
            variableDeclarators();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 83, annotationConstantRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "annotationConstantRest"



    // $ANTLR start "defaultValue"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:642:1: defaultValue : 'default' elementValue ;
    public final void defaultValue() throws RecognitionException {
        int defaultValue_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 84) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:643:5: ( 'default' elementValue )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:643:9: 'default' elementValue
            {
            match(input,68,FOLLOW_68_in_defaultValue3209); if (state.failed) return ;

            pushFollow(FOLLOW_elementValue_in_defaultValue3211);
            elementValue();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 84, defaultValue_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "defaultValue"



    // $ANTLR start "block"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:648:1: block : '{' ( blockStatement )* '}' ;
    public final void block() throws RecognitionException {
        int block_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 85) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:649:5: ( '{' ( blockStatement )* '}' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:649:9: '{' ( blockStatement )* '}'
            {
            match(input,108,FOLLOW_108_in_block3232); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:649:13: ( blockStatement )*
            loop105:
            do {
                int alt105=2;
                int LA105_0 = input.LA(1);

                if ( (LA105_0==ASSERT||(LA105_0 >= CharacterLiteral && LA105_0 <= ENUM)||LA105_0==FloatingPointLiteral||(LA105_0 >= HexLiteral && LA105_0 <= Identifier)||(LA105_0 >= OctalLiteral && LA105_0 <= StringLiteral)||LA105_0==25||LA105_0==32||(LA105_0 >= 36 && LA105_0 <= 37)||(LA105_0 >= 40 && LA105_0 <= 41)||LA105_0==48||LA105_0==54||(LA105_0 >= 59 && LA105_0 <= 62)||(LA105_0 >= 65 && LA105_0 <= 67)||(LA105_0 >= 69 && LA105_0 <= 70)||(LA105_0 >= 73 && LA105_0 <= 74)||(LA105_0 >= 76 && LA105_0 <= 78)||(LA105_0 >= 82 && LA105_0 <= 84)||(LA105_0 >= 86 && LA105_0 <= 87)||(LA105_0 >= 89 && LA105_0 <= 100)||(LA105_0 >= 103 && LA105_0 <= 105)||(LA105_0 >= 107 && LA105_0 <= 108)||LA105_0==113) ) {
                    alt105=1;
                }


                switch (alt105) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:649:13: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_block3234);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop105;
                }
            } while (true);


            match(input,112,FOLLOW_112_in_block3237); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 85, block_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "block"



    // $ANTLR start "blockStatement"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:652:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );
    public final void blockStatement() throws RecognitionException {
        int blockStatement_StartIndex = input.index();

        JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement3 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 86) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:653:5: ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement )
            int alt106=3;
            switch ( input.LA(1) ) {
            case 74:
                {
                int LA106_1 = input.LA(2);

                if ( (synpred151_Java()) ) {
                    alt106=1;
                }
                else if ( (synpred152_Java()) ) {
                    alt106=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 106, 1, input);

                    throw nvae;

                }
                }
                break;
            case 54:
                {
                int LA106_2 = input.LA(2);

                if ( (synpred151_Java()) ) {
                    alt106=1;
                }
                else if ( (synpred152_Java()) ) {
                    alt106=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 106, 2, input);

                    throw nvae;

                }
                }
                break;
            case Identifier:
                {
                int LA106_3 = input.LA(2);

                if ( (synpred151_Java()) ) {
                    alt106=1;
                }
                else if ( (true) ) {
                    alt106=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 106, 3, input);

                    throw nvae;

                }
                }
                break;
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                int LA106_4 = input.LA(2);

                if ( (synpred151_Java()) ) {
                    alt106=1;
                }
                else if ( (true) ) {
                    alt106=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 106, 4, input);

                    throw nvae;

                }
                }
                break;
            case ENUM:
            case 59:
            case 66:
            case 83:
            case 89:
            case 90:
            case 91:
            case 94:
            case 95:
                {
                alt106=2;
                }
                break;
            case ASSERT:
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case OctalLiteral:
            case StringLiteral:
            case 25:
            case 32:
            case 36:
            case 37:
            case 40:
            case 41:
            case 48:
            case 61:
            case 67:
            case 69:
            case 73:
            case 77:
            case 78:
            case 86:
            case 87:
            case 92:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 103:
            case 104:
            case 105:
            case 107:
            case 108:
            case 113:
                {
                alt106=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 106, 0, input);

                throw nvae;

            }

            switch (alt106) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:653:9: localVariableDeclarationStatement
                    {
                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement3260);
                    localVariableDeclarationStatement3=localVariableDeclarationStatement();

                    state._fsp--;
                    if (state.failed) return ;

                    if ( state.backtracking==0 ) {System.out.println((localVariableDeclarationStatement3!=null?input.toString(localVariableDeclarationStatement3.start,localVariableDeclarationStatement3.stop):null));}

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:654:9: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement3272);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:655:9: statement
                    {
                    pushFollow(FOLLOW_statement_in_blockStatement3282);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 86, blockStatement_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "blockStatement"


    public static class localVariableDeclarationStatement_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "localVariableDeclarationStatement"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:658:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
    public final JavaParser.localVariableDeclarationStatement_return localVariableDeclarationStatement() throws RecognitionException {
        JavaParser.localVariableDeclarationStatement_return retval = new JavaParser.localVariableDeclarationStatement_return();
        retval.start = input.LT(1);

        int localVariableDeclarationStatement_StartIndex = input.index();

        JavaParser.localVariableDeclaration_return localVariableDeclaration4 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 87) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:659:5: ( localVariableDeclaration ';' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:659:10: localVariableDeclaration ';'
            {
            pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3306);
            localVariableDeclaration4=localVariableDeclaration();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {System.out.println("LocalVariableDeclaration " + (localVariableDeclaration4!=null?input.toString(localVariableDeclaration4.start,localVariableDeclaration4.stop):null));}

            match(input,48,FOLLOW_48_in_localVariableDeclarationStatement3309); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 87, localVariableDeclarationStatement_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclarationStatement"


    public static class localVariableDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "localVariableDeclaration"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:662:1: localVariableDeclaration : variableModifiers type variableDeclarators ;
    public final JavaParser.localVariableDeclaration_return localVariableDeclaration() throws RecognitionException {
        JavaParser.localVariableDeclaration_return retval = new JavaParser.localVariableDeclaration_return();
        retval.start = input.LT(1);

        int localVariableDeclaration_StartIndex = input.index();

        JavaParser.variableDeclarators_return variableDeclarators5 =null;

        JavaParser.type_return type6 =null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 88) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:663:5: ( variableModifiers type variableDeclarators )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:663:9: variableModifiers type variableDeclarators
            {
            pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration3328);
            variableModifiers();

            state._fsp--;
            if (state.failed) return retval;

            pushFollow(FOLLOW_type_in_localVariableDeclaration3330);
            type6=type();

            state._fsp--;
            if (state.failed) return retval;

            pushFollow(FOLLOW_variableDeclarators_in_localVariableDeclaration3332);
            variableDeclarators5=variableDeclarators();

            state._fsp--;
            if (state.failed) return retval;

            if ( state.backtracking==0 ) {System.out.println("O declarator Ž "+ (variableDeclarators5!=null?input.toString(variableDeclarators5.start,variableDeclarators5.stop):null));}

            if ( state.backtracking==0 ) {System.out.println("O type Ž "+ (type6!=null?input.toString(type6.start,type6.stop):null));}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 88, localVariableDeclaration_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "localVariableDeclaration"



    // $ANTLR start "variableModifiers"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:667:1: variableModifiers : ( variableModifier )* ;
    public final void variableModifiers() throws RecognitionException {
        int variableModifiers_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 89) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:668:5: ( ( variableModifier )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:668:9: ( variableModifier )*
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:668:9: ( variableModifier )*
            loop107:
            do {
                int alt107=2;
                int LA107_0 = input.LA(1);

                if ( (LA107_0==54||LA107_0==74) ) {
                    alt107=1;
                }


                switch (alt107) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:668:9: variableModifier
            	    {
            	    pushFollow(FOLLOW_variableModifier_in_variableModifiers3363);
            	    variableModifier();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop107;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 89, variableModifiers_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "variableModifiers"



    // $ANTLR start "statement"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:671:1: statement : ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 90) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:672:5: ( block | ASSERT expression ( ':' expression )? ';' | 'if' parExpression statement ( options {k=1; } : 'else' statement )? | 'for' '(' forControl ')' statement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | 'try' block ( catches 'finally' block | catches | 'finally' block ) | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( Identifier )? ';' | 'continue' ( Identifier )? ';' | ';' | statementExpression ';' | Identifier ':' statement )
            int alt114=16;
            switch ( input.LA(1) ) {
            case 108:
                {
                alt114=1;
                }
                break;
            case ASSERT:
                {
                alt114=2;
                }
                break;
            case 78:
                {
                alt114=3;
                }
                break;
            case 77:
                {
                alt114=4;
                }
                break;
            case 107:
                {
                alt114=5;
                }
                break;
            case 69:
                {
                alt114=6;
                }
                break;
            case 104:
                {
                alt114=7;
                }
                break;
            case 97:
                {
                alt114=8;
                }
                break;
            case 98:
                {
                alt114=9;
                }
                break;
            case 92:
                {
                alt114=10;
                }
                break;
            case 100:
                {
                alt114=11;
                }
                break;
            case 61:
                {
                alt114=12;
                }
                break;
            case 67:
                {
                alt114=13;
                }
                break;
            case 48:
                {
                alt114=14;
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case OctalLiteral:
            case StringLiteral:
            case 25:
            case 32:
            case 36:
            case 37:
            case 40:
            case 41:
            case 60:
            case 62:
            case 65:
            case 70:
            case 73:
            case 76:
            case 82:
            case 84:
            case 86:
            case 87:
            case 93:
            case 96:
            case 99:
            case 103:
            case 105:
            case 113:
                {
                alt114=15;
                }
                break;
            case Identifier:
                {
                int LA114_16 = input.LA(2);

                if ( (LA114_16==47) ) {
                    alt114=16;
                }
                else if ( ((LA114_16 >= 26 && LA114_16 <= 32)||(LA114_16 >= 34 && LA114_16 <= 38)||(LA114_16 >= 40 && LA114_16 <= 43)||(LA114_16 >= 45 && LA114_16 <= 46)||(LA114_16 >= 48 && LA114_16 <= 53)||LA114_16==55||(LA114_16 >= 57 && LA114_16 <= 58)||LA114_16==81||(LA114_16 >= 109 && LA114_16 <= 111)) ) {
                    alt114=15;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 114, 16, input);

                    throw nvae;

                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 114, 0, input);

                throw nvae;

            }

            switch (alt114) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:672:7: block
                    {
                    pushFollow(FOLLOW_block_in_statement3381);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:673:9: ASSERT expression ( ':' expression )? ';'
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement3391); if (state.failed) return ;

                    pushFollow(FOLLOW_expression_in_statement3393);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:673:27: ( ':' expression )?
                    int alt108=2;
                    int LA108_0 = input.LA(1);

                    if ( (LA108_0==47) ) {
                        alt108=1;
                    }
                    switch (alt108) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:673:28: ':' expression
                            {
                            match(input,47,FOLLOW_47_in_statement3396); if (state.failed) return ;

                            pushFollow(FOLLOW_expression_in_statement3398);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_statement3402); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:674:9: 'if' parExpression statement ( options {k=1; } : 'else' statement )?
                    {
                    match(input,78,FOLLOW_78_in_statement3412); if (state.failed) return ;

                    pushFollow(FOLLOW_parExpression_in_statement3414);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_statement_in_statement3416);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:674:38: ( options {k=1; } : 'else' statement )?
                    int alt109=2;
                    int LA109_0 = input.LA(1);

                    if ( (LA109_0==71) ) {
                        int LA109_1 = input.LA(2);

                        if ( (synpred157_Java()) ) {
                            alt109=1;
                        }
                    }
                    switch (alt109) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:674:54: 'else' statement
                            {
                            match(input,71,FOLLOW_71_in_statement3426); if (state.failed) return ;

                            pushFollow(FOLLOW_statement_in_statement3428);
                            statement();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:675:9: 'for' '(' forControl ')' statement
                    {
                    match(input,77,FOLLOW_77_in_statement3440); if (state.failed) return ;

                    match(input,32,FOLLOW_32_in_statement3442); if (state.failed) return ;

                    pushFollow(FOLLOW_forControl_in_statement3444);
                    forControl();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,33,FOLLOW_33_in_statement3446); if (state.failed) return ;

                    pushFollow(FOLLOW_statement_in_statement3448);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:676:9: 'while' parExpression statement
                    {
                    match(input,107,FOLLOW_107_in_statement3458); if (state.failed) return ;

                    pushFollow(FOLLOW_parExpression_in_statement3460);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_statement_in_statement3462);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:677:9: 'do' statement 'while' parExpression ';'
                    {
                    match(input,69,FOLLOW_69_in_statement3472); if (state.failed) return ;

                    pushFollow(FOLLOW_statement_in_statement3474);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,107,FOLLOW_107_in_statement3476); if (state.failed) return ;

                    pushFollow(FOLLOW_parExpression_in_statement3478);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_statement3480); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:678:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
                    {
                    match(input,104,FOLLOW_104_in_statement3490); if (state.failed) return ;

                    pushFollow(FOLLOW_block_in_statement3492);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:679:9: ( catches 'finally' block | catches | 'finally' block )
                    int alt110=3;
                    int LA110_0 = input.LA(1);

                    if ( (LA110_0==64) ) {
                        int LA110_1 = input.LA(2);

                        if ( (synpred162_Java()) ) {
                            alt110=1;
                        }
                        else if ( (synpred163_Java()) ) {
                            alt110=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 110, 1, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA110_0==75) ) {
                        alt110=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 110, 0, input);

                        throw nvae;

                    }
                    switch (alt110) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:679:11: catches 'finally' block
                            {
                            pushFollow(FOLLOW_catches_in_statement3504);
                            catches();

                            state._fsp--;
                            if (state.failed) return ;

                            match(input,75,FOLLOW_75_in_statement3506); if (state.failed) return ;

                            pushFollow(FOLLOW_block_in_statement3508);
                            block();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:680:11: catches
                            {
                            pushFollow(FOLLOW_catches_in_statement3520);
                            catches();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 3 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:681:13: 'finally' block
                            {
                            match(input,75,FOLLOW_75_in_statement3534); if (state.failed) return ;

                            pushFollow(FOLLOW_block_in_statement3536);
                            block();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:683:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
                    {
                    match(input,97,FOLLOW_97_in_statement3556); if (state.failed) return ;

                    pushFollow(FOLLOW_parExpression_in_statement3558);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,108,FOLLOW_108_in_statement3560); if (state.failed) return ;

                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement3562);
                    switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,112,FOLLOW_112_in_statement3564); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:684:9: 'synchronized' parExpression block
                    {
                    match(input,98,FOLLOW_98_in_statement3574); if (state.failed) return ;

                    pushFollow(FOLLOW_parExpression_in_statement3576);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_block_in_statement3578);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:685:9: 'return' ( expression )? ';'
                    {
                    match(input,92,FOLLOW_92_in_statement3588); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:685:18: ( expression )?
                    int alt111=2;
                    int LA111_0 = input.LA(1);

                    if ( ((LA111_0 >= CharacterLiteral && LA111_0 <= DecimalLiteral)||LA111_0==FloatingPointLiteral||(LA111_0 >= HexLiteral && LA111_0 <= Identifier)||(LA111_0 >= OctalLiteral && LA111_0 <= StringLiteral)||LA111_0==25||LA111_0==32||(LA111_0 >= 36 && LA111_0 <= 37)||(LA111_0 >= 40 && LA111_0 <= 41)||LA111_0==60||LA111_0==62||LA111_0==65||LA111_0==70||LA111_0==73||LA111_0==76||LA111_0==82||LA111_0==84||(LA111_0 >= 86 && LA111_0 <= 87)||LA111_0==93||LA111_0==96||LA111_0==99||LA111_0==103||LA111_0==105||LA111_0==113) ) {
                        alt111=1;
                    }
                    switch (alt111) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:685:18: expression
                            {
                            pushFollow(FOLLOW_expression_in_statement3590);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_statement3593); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:686:9: 'throw' expression ';'
                    {
                    match(input,100,FOLLOW_100_in_statement3603); if (state.failed) return ;

                    pushFollow(FOLLOW_expression_in_statement3605);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_statement3607); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:687:9: 'break' ( Identifier )? ';'
                    {
                    match(input,61,FOLLOW_61_in_statement3617); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:687:17: ( Identifier )?
                    int alt112=2;
                    int LA112_0 = input.LA(1);

                    if ( (LA112_0==Identifier) ) {
                        alt112=1;
                    }
                    switch (alt112) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:687:17: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement3619); if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_statement3622); if (state.failed) return ;

                    }
                    break;
                case 13 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:688:9: 'continue' ( Identifier )? ';'
                    {
                    match(input,67,FOLLOW_67_in_statement3632); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:688:20: ( Identifier )?
                    int alt113=2;
                    int LA113_0 = input.LA(1);

                    if ( (LA113_0==Identifier) ) {
                        alt113=1;
                    }
                    switch (alt113) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:688:20: Identifier
                            {
                            match(input,Identifier,FOLLOW_Identifier_in_statement3634); if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_statement3637); if (state.failed) return ;

                    }
                    break;
                case 14 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:689:9: ';'
                    {
                    match(input,48,FOLLOW_48_in_statement3647); if (state.failed) return ;

                    }
                    break;
                case 15 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:690:9: statementExpression ';'
                    {
                    pushFollow(FOLLOW_statementExpression_in_statement3658);
                    statementExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,48,FOLLOW_48_in_statement3660); if (state.failed) return ;

                    }
                    break;
                case 16 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:691:9: Identifier ':' statement
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_statement3670); if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_statement3672); if (state.failed) return ;

                    pushFollow(FOLLOW_statement_in_statement3674);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 90, statement_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "statement"



    // $ANTLR start "catches"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:694:1: catches : catchClause ( catchClause )* ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 91) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:695:5: ( catchClause ( catchClause )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:695:9: catchClause ( catchClause )*
            {
            pushFollow(FOLLOW_catchClause_in_catches3697);
            catchClause();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:695:21: ( catchClause )*
            loop115:
            do {
                int alt115=2;
                int LA115_0 = input.LA(1);

                if ( (LA115_0==64) ) {
                    alt115=1;
                }


                switch (alt115) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:695:22: catchClause
            	    {
            	    pushFollow(FOLLOW_catchClause_in_catches3700);
            	    catchClause();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop115;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 91, catches_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "catches"



    // $ANTLR start "catchClause"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:698:1: catchClause : 'catch' '(' formalParameter ')' block ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 92) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:699:5: ( 'catch' '(' formalParameter ')' block )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:699:9: 'catch' '(' formalParameter ')' block
            {
            match(input,64,FOLLOW_64_in_catchClause3725); if (state.failed) return ;

            match(input,32,FOLLOW_32_in_catchClause3727); if (state.failed) return ;

            pushFollow(FOLLOW_formalParameter_in_catchClause3729);
            formalParameter();

            state._fsp--;
            if (state.failed) return ;

            match(input,33,FOLLOW_33_in_catchClause3731); if (state.failed) return ;

            pushFollow(FOLLOW_block_in_catchClause3733);
            block();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 92, catchClause_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "catchClause"



    // $ANTLR start "formalParameter"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:702:1: formalParameter : variableModifiers type variableDeclaratorId ;
    public final void formalParameter() throws RecognitionException {
        int formalParameter_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 93) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:703:5: ( variableModifiers type variableDeclaratorId )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:703:9: variableModifiers type variableDeclaratorId
            {
            pushFollow(FOLLOW_variableModifiers_in_formalParameter3752);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_type_in_formalParameter3754);
            type();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_variableDeclaratorId_in_formalParameter3756);
            variableDeclaratorId();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 93, formalParameter_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "formalParameter"



    // $ANTLR start "switchBlockStatementGroups"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:706:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final void switchBlockStatementGroups() throws RecognitionException {
        int switchBlockStatementGroups_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 94) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:707:5: ( ( switchBlockStatementGroup )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:707:9: ( switchBlockStatementGroup )*
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:707:9: ( switchBlockStatementGroup )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==63||LA116_0==68) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:707:10: switchBlockStatementGroup
            	    {
            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3784);
            	    switchBlockStatementGroup();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 94, switchBlockStatementGroups_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroups"



    // $ANTLR start "switchBlockStatementGroup"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:714:1: switchBlockStatementGroup : ( switchLabel )+ ( blockStatement )* ;
    public final void switchBlockStatementGroup() throws RecognitionException {
        int switchBlockStatementGroup_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 95) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:5: ( ( switchLabel )+ ( blockStatement )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:9: ( switchLabel )+ ( blockStatement )*
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:9: ( switchLabel )+
            int cnt117=0;
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==63) ) {
                    int LA117_2 = input.LA(2);

                    if ( (synpred178_Java()) ) {
                        alt117=1;
                    }


                }
                else if ( (LA117_0==68) ) {
                    int LA117_3 = input.LA(2);

                    if ( (synpred178_Java()) ) {
                        alt117=1;
                    }


                }


                switch (alt117) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:9: switchLabel
            	    {
            	    pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup3811);
            	    switchLabel();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt117 >= 1 ) break loop117;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(117, input);
                        throw eee;
                }
                cnt117++;
            } while (true);


            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:22: ( blockStatement )*
            loop118:
            do {
                int alt118=2;
                int LA118_0 = input.LA(1);

                if ( (LA118_0==ASSERT||(LA118_0 >= CharacterLiteral && LA118_0 <= ENUM)||LA118_0==FloatingPointLiteral||(LA118_0 >= HexLiteral && LA118_0 <= Identifier)||(LA118_0 >= OctalLiteral && LA118_0 <= StringLiteral)||LA118_0==25||LA118_0==32||(LA118_0 >= 36 && LA118_0 <= 37)||(LA118_0 >= 40 && LA118_0 <= 41)||LA118_0==48||LA118_0==54||(LA118_0 >= 59 && LA118_0 <= 62)||(LA118_0 >= 65 && LA118_0 <= 67)||(LA118_0 >= 69 && LA118_0 <= 70)||(LA118_0 >= 73 && LA118_0 <= 74)||(LA118_0 >= 76 && LA118_0 <= 78)||(LA118_0 >= 82 && LA118_0 <= 84)||(LA118_0 >= 86 && LA118_0 <= 87)||(LA118_0 >= 89 && LA118_0 <= 100)||(LA118_0 >= 103 && LA118_0 <= 105)||(LA118_0 >= 107 && LA118_0 <= 108)||LA118_0==113) ) {
                    alt118=1;
                }


                switch (alt118) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:22: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup3814);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop118;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 95, switchBlockStatementGroup_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroup"



    // $ANTLR start "switchLabel"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:718:1: switchLabel : ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' );
    public final void switchLabel() throws RecognitionException {
        int switchLabel_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 96) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:719:5: ( 'case' constantExpression ':' | 'case' enumConstantName ':' | 'default' ':' )
            int alt119=3;
            int LA119_0 = input.LA(1);

            if ( (LA119_0==63) ) {
                int LA119_1 = input.LA(2);

                if ( ((LA119_1 >= CharacterLiteral && LA119_1 <= DecimalLiteral)||LA119_1==FloatingPointLiteral||LA119_1==HexLiteral||(LA119_1 >= OctalLiteral && LA119_1 <= StringLiteral)||LA119_1==25||LA119_1==32||(LA119_1 >= 36 && LA119_1 <= 37)||(LA119_1 >= 40 && LA119_1 <= 41)||LA119_1==60||LA119_1==62||LA119_1==65||LA119_1==70||LA119_1==73||LA119_1==76||LA119_1==82||LA119_1==84||(LA119_1 >= 86 && LA119_1 <= 87)||LA119_1==93||LA119_1==96||LA119_1==99||LA119_1==103||LA119_1==105||LA119_1==113) ) {
                    alt119=1;
                }
                else if ( (LA119_1==Identifier) ) {
                    int LA119_4 = input.LA(3);

                    if ( ((LA119_4 >= 26 && LA119_4 <= 32)||(LA119_4 >= 34 && LA119_4 <= 38)||(LA119_4 >= 40 && LA119_4 <= 43)||(LA119_4 >= 45 && LA119_4 <= 46)||(LA119_4 >= 49 && LA119_4 <= 53)||LA119_4==55||(LA119_4 >= 57 && LA119_4 <= 58)||LA119_4==81||(LA119_4 >= 109 && LA119_4 <= 111)) ) {
                        alt119=1;
                    }
                    else if ( (LA119_4==47) ) {
                        int LA119_5 = input.LA(4);

                        if ( (synpred180_Java()) ) {
                            alt119=1;
                        }
                        else if ( (synpred181_Java()) ) {
                            alt119=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 119, 5, input);

                            throw nvae;

                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 119, 4, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 119, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA119_0==68) ) {
                alt119=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 119, 0, input);

                throw nvae;

            }
            switch (alt119) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:719:9: 'case' constantExpression ':'
                    {
                    match(input,63,FOLLOW_63_in_switchLabel3838); if (state.failed) return ;

                    pushFollow(FOLLOW_constantExpression_in_switchLabel3840);
                    constantExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_switchLabel3842); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:720:9: 'case' enumConstantName ':'
                    {
                    match(input,63,FOLLOW_63_in_switchLabel3852); if (state.failed) return ;

                    pushFollow(FOLLOW_enumConstantName_in_switchLabel3854);
                    enumConstantName();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_switchLabel3856); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:721:9: 'default' ':'
                    {
                    match(input,68,FOLLOW_68_in_switchLabel3866); if (state.failed) return ;

                    match(input,47,FOLLOW_47_in_switchLabel3868); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 96, switchLabel_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "switchLabel"



    // $ANTLR start "forControl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:724:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );
    public final void forControl() throws RecognitionException {
        int forControl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 97) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:726:5: ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? )
            int alt123=2;
            alt123 = dfa123.predict(input);
            switch (alt123) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:726:9: enhancedForControl
                    {
                    pushFollow(FOLLOW_enhancedForControl_in_forControl3899);
                    enhancedForControl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:9: ( forInit )? ';' ( expression )? ';' ( forUpdate )?
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:9: ( forInit )?
                    int alt120=2;
                    int LA120_0 = input.LA(1);

                    if ( ((LA120_0 >= CharacterLiteral && LA120_0 <= DecimalLiteral)||LA120_0==FloatingPointLiteral||(LA120_0 >= HexLiteral && LA120_0 <= Identifier)||(LA120_0 >= OctalLiteral && LA120_0 <= StringLiteral)||LA120_0==25||LA120_0==32||(LA120_0 >= 36 && LA120_0 <= 37)||(LA120_0 >= 40 && LA120_0 <= 41)||LA120_0==54||LA120_0==60||LA120_0==62||LA120_0==65||LA120_0==70||(LA120_0 >= 73 && LA120_0 <= 74)||LA120_0==76||LA120_0==82||LA120_0==84||(LA120_0 >= 86 && LA120_0 <= 87)||LA120_0==93||LA120_0==96||LA120_0==99||LA120_0==103||LA120_0==105||LA120_0==113) ) {
                        alt120=1;
                    }
                    switch (alt120) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:9: forInit
                            {
                            pushFollow(FOLLOW_forInit_in_forControl3909);
                            forInit();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_forControl3912); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:22: ( expression )?
                    int alt121=2;
                    int LA121_0 = input.LA(1);

                    if ( ((LA121_0 >= CharacterLiteral && LA121_0 <= DecimalLiteral)||LA121_0==FloatingPointLiteral||(LA121_0 >= HexLiteral && LA121_0 <= Identifier)||(LA121_0 >= OctalLiteral && LA121_0 <= StringLiteral)||LA121_0==25||LA121_0==32||(LA121_0 >= 36 && LA121_0 <= 37)||(LA121_0 >= 40 && LA121_0 <= 41)||LA121_0==60||LA121_0==62||LA121_0==65||LA121_0==70||LA121_0==73||LA121_0==76||LA121_0==82||LA121_0==84||(LA121_0 >= 86 && LA121_0 <= 87)||LA121_0==93||LA121_0==96||LA121_0==99||LA121_0==103||LA121_0==105||LA121_0==113) ) {
                        alt121=1;
                    }
                    switch (alt121) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:22: expression
                            {
                            pushFollow(FOLLOW_expression_in_forControl3914);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,48,FOLLOW_48_in_forControl3917); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:38: ( forUpdate )?
                    int alt122=2;
                    int LA122_0 = input.LA(1);

                    if ( ((LA122_0 >= CharacterLiteral && LA122_0 <= DecimalLiteral)||LA122_0==FloatingPointLiteral||(LA122_0 >= HexLiteral && LA122_0 <= Identifier)||(LA122_0 >= OctalLiteral && LA122_0 <= StringLiteral)||LA122_0==25||LA122_0==32||(LA122_0 >= 36 && LA122_0 <= 37)||(LA122_0 >= 40 && LA122_0 <= 41)||LA122_0==60||LA122_0==62||LA122_0==65||LA122_0==70||LA122_0==73||LA122_0==76||LA122_0==82||LA122_0==84||(LA122_0 >= 86 && LA122_0 <= 87)||LA122_0==93||LA122_0==96||LA122_0==99||LA122_0==103||LA122_0==105||LA122_0==113) ) {
                        alt122=1;
                    }
                    switch (alt122) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:727:38: forUpdate
                            {
                            pushFollow(FOLLOW_forUpdate_in_forControl3919);
                            forUpdate();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 97, forControl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "forControl"



    // $ANTLR start "forInit"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:730:1: forInit : ( localVariableDeclaration | expressionList );
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 98) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:731:5: ( localVariableDeclaration | expressionList )
            int alt124=2;
            switch ( input.LA(1) ) {
            case 54:
            case 74:
                {
                alt124=1;
                }
                break;
            case Identifier:
                {
                int LA124_3 = input.LA(2);

                if ( (synpred186_Java()) ) {
                    alt124=1;
                }
                else if ( (true) ) {
                    alt124=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 124, 3, input);

                    throw nvae;

                }
                }
                break;
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                int LA124_4 = input.LA(2);

                if ( (synpred186_Java()) ) {
                    alt124=1;
                }
                else if ( (true) ) {
                    alt124=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 124, 4, input);

                    throw nvae;

                }
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case OctalLiteral:
            case StringLiteral:
            case 25:
            case 32:
            case 36:
            case 37:
            case 40:
            case 41:
            case 73:
            case 86:
            case 87:
            case 96:
            case 99:
            case 103:
            case 105:
            case 113:
                {
                alt124=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 124, 0, input);

                throw nvae;

            }

            switch (alt124) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:731:9: localVariableDeclaration
                    {
                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit3939);
                    localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:732:9: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_forInit3949);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 98, forInit_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "forInit"



    // $ANTLR start "enhancedForControl"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:735:1: enhancedForControl : variableModifiers type Identifier ':' expression ;
    public final void enhancedForControl() throws RecognitionException {
        int enhancedForControl_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 99) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:736:5: ( variableModifiers type Identifier ':' expression )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:736:9: variableModifiers type Identifier ':' expression
            {
            pushFollow(FOLLOW_variableModifiers_in_enhancedForControl3972);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;

            pushFollow(FOLLOW_type_in_enhancedForControl3974);
            type();

            state._fsp--;
            if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_enhancedForControl3976); if (state.failed) return ;

            match(input,47,FOLLOW_47_in_enhancedForControl3978); if (state.failed) return ;

            pushFollow(FOLLOW_expression_in_enhancedForControl3980);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 99, enhancedForControl_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "enhancedForControl"



    // $ANTLR start "forUpdate"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:739:1: forUpdate : expressionList ;
    public final void forUpdate() throws RecognitionException {
        int forUpdate_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 100) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:740:5: ( expressionList )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:740:9: expressionList
            {
            pushFollow(FOLLOW_expressionList_in_forUpdate3999);
            expressionList();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 100, forUpdate_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "forUpdate"



    // $ANTLR start "parExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:745:1: parExpression : '(' expression ')' ;
    public final void parExpression() throws RecognitionException {
        int parExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 101) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:746:5: ( '(' expression ')' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:746:9: '(' expression ')'
            {
            match(input,32,FOLLOW_32_in_parExpression4020); if (state.failed) return ;

            pushFollow(FOLLOW_expression_in_parExpression4022);
            expression();

            state._fsp--;
            if (state.failed) return ;

            match(input,33,FOLLOW_33_in_parExpression4024); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 101, parExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "parExpression"



    // $ANTLR start "expressionList"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:749:1: expressionList : expression ( ',' expression )* ;
    public final void expressionList() throws RecognitionException {
        int expressionList_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 102) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:750:5: ( expression ( ',' expression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:750:9: expression ( ',' expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionList4047);
            expression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:750:20: ( ',' expression )*
            loop125:
            do {
                int alt125=2;
                int LA125_0 = input.LA(1);

                if ( (LA125_0==39) ) {
                    alt125=1;
                }


                switch (alt125) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:750:21: ',' expression
            	    {
            	    match(input,39,FOLLOW_39_in_expressionList4050); if (state.failed) return ;

            	    pushFollow(FOLLOW_expression_in_expressionList4052);
            	    expression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop125;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 102, expressionList_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "expressionList"



    // $ANTLR start "statementExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:753:1: statementExpression : expression ;
    public final void statementExpression() throws RecognitionException {
        int statementExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 103) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:754:5: ( expression )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:754:9: expression
            {
            pushFollow(FOLLOW_expression_in_statementExpression4073);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 103, statementExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "statementExpression"



    // $ANTLR start "constantExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:757:1: constantExpression : expression ;
    public final void constantExpression() throws RecognitionException {
        int constantExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 104) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:758:5: ( expression )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:758:9: expression
            {
            pushFollow(FOLLOW_expression_in_constantExpression4096);
            expression();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 104, constantExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "constantExpression"


    public static class expression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "expression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:761:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final JavaParser.expression_return expression() throws RecognitionException {
        JavaParser.expression_return retval = new JavaParser.expression_return();
        retval.start = input.LT(1);

        int expression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 105) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:5: ( conditionalExpression ( assignmentOperator expression )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:9: conditionalExpression ( assignmentOperator expression )?
            {
            pushFollow(FOLLOW_conditionalExpression_in_expression4119);
            conditionalExpression();

            state._fsp--;
            if (state.failed) return retval;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:31: ( assignmentOperator expression )?
            int alt126=2;
            switch ( input.LA(1) ) {
                case 50:
                    {
                    int LA126_1 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 38:
                    {
                    int LA126_2 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 42:
                    {
                    int LA126_3 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 35:
                    {
                    int LA126_4 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 46:
                    {
                    int LA126_5 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 31:
                    {
                    int LA126_6 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 110:
                    {
                    int LA126_7 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 58:
                    {
                    int LA126_8 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 28:
                    {
                    int LA126_9 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 49:
                    {
                    int LA126_10 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
                case 52:
                    {
                    int LA126_11 = input.LA(2);

                    if ( (synpred188_Java()) ) {
                        alt126=1;
                    }
                    }
                    break;
            }

            switch (alt126) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:32: assignmentOperator expression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_expression4122);
                    assignmentOperator();

                    state._fsp--;
                    if (state.failed) return retval;

                    pushFollow(FOLLOW_expression_in_expression4124);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 105, expression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "expression"



    // $ANTLR start "assignmentOperator"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:765:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?| ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?| ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?);
    public final void assignmentOperator() throws RecognitionException {
        int assignmentOperator_StartIndex = input.index();

        Token t1=null;
        Token t2=null;
        Token t3=null;
        Token t4=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 106) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:766:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?| ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?| ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?)
            int alt127=12;
            int LA127_0 = input.LA(1);

            if ( (LA127_0==50) ) {
                alt127=1;
            }
            else if ( (LA127_0==38) ) {
                alt127=2;
            }
            else if ( (LA127_0==42) ) {
                alt127=3;
            }
            else if ( (LA127_0==35) ) {
                alt127=4;
            }
            else if ( (LA127_0==46) ) {
                alt127=5;
            }
            else if ( (LA127_0==31) ) {
                alt127=6;
            }
            else if ( (LA127_0==110) ) {
                alt127=7;
            }
            else if ( (LA127_0==58) ) {
                alt127=8;
            }
            else if ( (LA127_0==28) ) {
                alt127=9;
            }
            else if ( (LA127_0==49) && (synpred198_Java())) {
                alt127=10;
            }
            else if ( (LA127_0==52) ) {
                int LA127_11 = input.LA(2);

                if ( (LA127_11==52) ) {
                    int LA127_12 = input.LA(3);

                    if ( (LA127_12==52) && (synpred199_Java())) {
                        alt127=11;
                    }
                    else if ( (LA127_12==50) && (synpred200_Java())) {
                        alt127=12;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 127, 12, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 127, 11, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 127, 0, input);

                throw nvae;

            }
            switch (alt127) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:766:9: '='
                    {
                    match(input,50,FOLLOW_50_in_assignmentOperator4150); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:767:9: '+='
                    {
                    match(input,38,FOLLOW_38_in_assignmentOperator4160); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:768:9: '-='
                    {
                    match(input,42,FOLLOW_42_in_assignmentOperator4170); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:769:9: '*='
                    {
                    match(input,35,FOLLOW_35_in_assignmentOperator4180); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:770:9: '/='
                    {
                    match(input,46,FOLLOW_46_in_assignmentOperator4190); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:771:9: '&='
                    {
                    match(input,31,FOLLOW_31_in_assignmentOperator4200); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:772:9: '|='
                    {
                    match(input,110,FOLLOW_110_in_assignmentOperator4210); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:773:9: '^='
                    {
                    match(input,58,FOLLOW_58_in_assignmentOperator4220); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:774:9: '%='
                    {
                    match(input,28,FOLLOW_28_in_assignmentOperator4230); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:775:9: ( '<' '<' '=' )=>t1= '<' t2= '<' t3= '=' {...}?
                    {
                    t1=(Token)match(input,49,FOLLOW_49_in_assignmentOperator4251); if (state.failed) return ;

                    t2=(Token)match(input,49,FOLLOW_49_in_assignmentOperator4255); if (state.failed) return ;

                    t3=(Token)match(input,50,FOLLOW_50_in_assignmentOperator4259); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() &&
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() && 
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() &&\n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 11 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:780:9: ( '>' '>' '>' '=' )=>t1= '>' t2= '>' t3= '>' t4= '=' {...}?
                    {
                    t1=(Token)match(input,52,FOLLOW_52_in_assignmentOperator4293); if (state.failed) return ;

                    t2=(Token)match(input,52,FOLLOW_52_in_assignmentOperator4297); if (state.failed) return ;

                    t3=(Token)match(input,52,FOLLOW_52_in_assignmentOperator4301); if (state.failed) return ;

                    t4=(Token)match(input,50,FOLLOW_50_in_assignmentOperator4305); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() &&
                              t3.getLine() == t4.getLine() && 
                              t3.getCharPositionInLine() + 1 == t4.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() &&\n          $t3.getLine() == $t4.getLine() && \n          $t3.getCharPositionInLine() + 1 == $t4.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 12 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:787:9: ( '>' '>' '=' )=>t1= '>' t2= '>' t3= '=' {...}?
                    {
                    t1=(Token)match(input,52,FOLLOW_52_in_assignmentOperator4336); if (state.failed) return ;

                    t2=(Token)match(input,52,FOLLOW_52_in_assignmentOperator4340); if (state.failed) return ;

                    t3=(Token)match(input,50,FOLLOW_50_in_assignmentOperator4344); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() && 
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "assignmentOperator", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() && \n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 106, assignmentOperator_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "assignmentOperator"


    public static class conditionalExpression_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "conditionalExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:794:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' expression )? ;
    public final JavaParser.conditionalExpression_return conditionalExpression() throws RecognitionException {
        JavaParser.conditionalExpression_return retval = new JavaParser.conditionalExpression_return();
        retval.start = input.LT(1);

        int conditionalExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 107) ) { return retval; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:795:5: ( conditionalOrExpression ( '?' expression ':' expression )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:795:9: conditionalOrExpression ( '?' expression ':' expression )?
            {
            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression4373);
            conditionalOrExpression();

            state._fsp--;
            if (state.failed) return retval;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:795:33: ( '?' expression ':' expression )?
            int alt128=2;
            int LA128_0 = input.LA(1);

            if ( (LA128_0==53) ) {
                alt128=1;
            }
            switch (alt128) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:795:35: '?' expression ':' expression
                    {
                    match(input,53,FOLLOW_53_in_conditionalExpression4377); if (state.failed) return retval;

                    pushFollow(FOLLOW_expression_in_conditionalExpression4379);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    match(input,47,FOLLOW_47_in_conditionalExpression4381); if (state.failed) return retval;

                    pushFollow(FOLLOW_expression_in_conditionalExpression4383);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }


            if ( state.backtracking==0 ) {
                	
                	
                	//Utilizar aqui depois para fazer um parser do HTML...
                	//System.err.println("Dentro do ACTION Poderoso HTML"+input.toString(retval.start,input.LT(-1)));
                	
                	String lineSeparator = System.getProperty("line.separator") + " ";
                	
                	//regex para obter somente select
                	Pattern pattern = Pattern.compile("^\\W\\s*select", Pattern.CASE_INSENSITIVE);
            		
            		Matcher matcher = pattern.matcher(input.toString(retval.start,input.LT(-1)));
            		
            		if(matcher.find()) {
            		
            			this.getSelects().add(input.toString(retval.start,input.LT(-1)).trim().replaceFirst("\\s*select", "select").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";");
            			//System.err.println("ConditionalExpression AQUI "+ input.toString(retval.start,input.LT(-1)));
            		
            		}
            		
            		
            		//regex para obter somente update statement
            		Pattern patternUpdate = Pattern.compile("^\\W\\s*update", Pattern.CASE_INSENSITIVE);
            		
            		Matcher matcherUpdate = patternUpdate.matcher(input.toString(retval.start,input.LT(-1)));
            		
            		if(matcherUpdate.find()) {
            		
            			this.getUpdates().add(input.toString(retval.start,input.LT(-1)).trim().replaceFirst("\\s*update", "update").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";");
            			//System.err.println("ConditionalExpression AQUI "+ input.toString(retval.start,input.LT(-1)));
            		
            		}
            		
            		//regex para obter somente insert statement
            		Pattern patternInsert = Pattern.compile("^\\W\\s*insert", Pattern.CASE_INSENSITIVE);
            		
            		Matcher matcherInsert = patternInsert.matcher(input.toString(retval.start,input.LT(-1)));
            		
            		if(matcherInsert.find()) {
            		
            			this.getInserts().add(input.toString(retval.start,input.LT(-1)).trim().replaceFirst("\\s*insert", "insert").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";");
            			//System.err.println("ConditionalExpression AQUI "+ input.toString(retval.start,input.LT(-1)));
            		
            		}
            		
            		//regex para obter somente delele statement
            		Pattern patternDelete = Pattern.compile("^\\W\\s*delete", Pattern.CASE_INSENSITIVE);
            		
            		Matcher matcherDelete = patternDelete.matcher(input.toString(retval.start,input.LT(-1)));
            		
            		if(matcherDelete.find()) {
            		
            			this.getDeletes().add(input.toString(retval.start,input.LT(-1)).trim().replaceFirst("\\s*delete", "delete").replace('"', ' ').replaceAll("\\+"+lineSeparator, "").replaceAll("\\s+", " ").replaceAll("\\+\\D*?\\+", " ")+";");
            			//System.err.println("ConditionalExpression AQUI "+ input.toString(retval.start,input.LT(-1)));
            		
            		} 
            		
            		
            		
            		
            		
                
            //    	if(input.toString(retval.start,input.LT(-1)).contains("select")){
            //    	
            //    	    System.err.println("ConditionalExpression AQUI "+ input.toString(retval.start,input.LT(-1)));
            //    	}else {
            //    		
            //    		//System.err.println("Estou no ELSE");
            //    	
            //    	}
                	
                	}

            }

            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 107, conditionalExpression_StartIndex); }

        }
        return retval;
    }
    // $ANTLR end "conditionalExpression"



    // $ANTLR start "conditionalOrExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:872:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final void conditionalOrExpression() throws RecognitionException {
        int conditionalOrExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 108) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:873:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:873:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4421);
            conditionalAndExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:873:34: ( '||' conditionalAndExpression )*
            loop129:
            do {
                int alt129=2;
                int LA129_0 = input.LA(1);

                if ( (LA129_0==111) ) {
                    alt129=1;
                }


                switch (alt129) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:873:36: '||' conditionalAndExpression
            	    {
            	    match(input,111,FOLLOW_111_in_conditionalOrExpression4425); if (state.failed) return ;

            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression4427);
            	    conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop129;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 108, conditionalOrExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "conditionalOrExpression"



    // $ANTLR start "conditionalAndExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:876:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final void conditionalAndExpression() throws RecognitionException {
        int conditionalAndExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 109) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:877:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:877:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4449);
            inclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:877:31: ( '&&' inclusiveOrExpression )*
            loop130:
            do {
                int alt130=2;
                int LA130_0 = input.LA(1);

                if ( (LA130_0==29) ) {
                    alt130=1;
                }


                switch (alt130) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:877:33: '&&' inclusiveOrExpression
            	    {
            	    match(input,29,FOLLOW_29_in_conditionalAndExpression4453); if (state.failed) return ;

            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4455);
            	    inclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop130;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 109, conditionalAndExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "conditionalAndExpression"



    // $ANTLR start "inclusiveOrExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:880:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final void inclusiveOrExpression() throws RecognitionException {
        int inclusiveOrExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 110) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:881:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:881:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4477);
            exclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:881:31: ( '|' exclusiveOrExpression )*
            loop131:
            do {
                int alt131=2;
                int LA131_0 = input.LA(1);

                if ( (LA131_0==109) ) {
                    alt131=1;
                }


                switch (alt131) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:881:33: '|' exclusiveOrExpression
            	    {
            	    match(input,109,FOLLOW_109_in_inclusiveOrExpression4481); if (state.failed) return ;

            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4483);
            	    exclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop131;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 110, inclusiveOrExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "inclusiveOrExpression"



    // $ANTLR start "exclusiveOrExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:884:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final void exclusiveOrExpression() throws RecognitionException {
        int exclusiveOrExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 111) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:885:5: ( andExpression ( '^' andExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:885:9: andExpression ( '^' andExpression )*
            {
            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4505);
            andExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:885:23: ( '^' andExpression )*
            loop132:
            do {
                int alt132=2;
                int LA132_0 = input.LA(1);

                if ( (LA132_0==57) ) {
                    alt132=1;
                }


                switch (alt132) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:885:25: '^' andExpression
            	    {
            	    match(input,57,FOLLOW_57_in_exclusiveOrExpression4509); if (state.failed) return ;

            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression4511);
            	    andExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop132;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 111, exclusiveOrExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "exclusiveOrExpression"



    // $ANTLR start "andExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:888:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final void andExpression() throws RecognitionException {
        int andExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 112) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:889:5: ( equalityExpression ( '&' equalityExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:889:9: equalityExpression ( '&' equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_andExpression4533);
            equalityExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:889:28: ( '&' equalityExpression )*
            loop133:
            do {
                int alt133=2;
                int LA133_0 = input.LA(1);

                if ( (LA133_0==30) ) {
                    alt133=1;
                }


                switch (alt133) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:889:30: '&' equalityExpression
            	    {
            	    match(input,30,FOLLOW_30_in_andExpression4537); if (state.failed) return ;

            	    pushFollow(FOLLOW_equalityExpression_in_andExpression4539);
            	    equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop133;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 112, andExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "andExpression"



    // $ANTLR start "equalityExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:892:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final void equalityExpression() throws RecognitionException {
        int equalityExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 113) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:893:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:893:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4561);
            instanceOfExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:893:30: ( ( '==' | '!=' ) instanceOfExpression )*
            loop134:
            do {
                int alt134=2;
                int LA134_0 = input.LA(1);

                if ( (LA134_0==26||LA134_0==51) ) {
                    alt134=1;
                }


                switch (alt134) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:893:32: ( '==' | '!=' ) instanceOfExpression
            	    {
            	    if ( input.LA(1)==26||input.LA(1)==51 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression4573);
            	    instanceOfExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop134;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 113, equalityExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "equalityExpression"



    // $ANTLR start "instanceOfExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:896:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final void instanceOfExpression() throws RecognitionException {
        int instanceOfExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 114) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:897:5: ( relationalExpression ( 'instanceof' type )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:897:9: relationalExpression ( 'instanceof' type )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression4595);
            relationalExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:897:30: ( 'instanceof' type )?
            int alt135=2;
            int LA135_0 = input.LA(1);

            if ( (LA135_0==81) ) {
                alt135=1;
            }
            switch (alt135) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:897:31: 'instanceof' type
                    {
                    match(input,81,FOLLOW_81_in_instanceOfExpression4598); if (state.failed) return ;

                    pushFollow(FOLLOW_type_in_instanceOfExpression4600);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 114, instanceOfExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "instanceOfExpression"



    // $ANTLR start "relationalExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:900:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final void relationalExpression() throws RecognitionException {
        int relationalExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 115) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:901:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:901:9: shiftExpression ( relationalOp shiftExpression )*
            {
            pushFollow(FOLLOW_shiftExpression_in_relationalExpression4621);
            shiftExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:901:25: ( relationalOp shiftExpression )*
            loop136:
            do {
                int alt136=2;
                int LA136_0 = input.LA(1);

                if ( (LA136_0==49) ) {
                    int LA136_2 = input.LA(2);

                    if ( ((LA136_2 >= CharacterLiteral && LA136_2 <= DecimalLiteral)||LA136_2==FloatingPointLiteral||(LA136_2 >= HexLiteral && LA136_2 <= Identifier)||(LA136_2 >= OctalLiteral && LA136_2 <= StringLiteral)||LA136_2==25||LA136_2==32||(LA136_2 >= 36 && LA136_2 <= 37)||(LA136_2 >= 40 && LA136_2 <= 41)||LA136_2==50||LA136_2==60||LA136_2==62||LA136_2==65||LA136_2==70||LA136_2==73||LA136_2==76||LA136_2==82||LA136_2==84||(LA136_2 >= 86 && LA136_2 <= 87)||LA136_2==93||LA136_2==96||LA136_2==99||LA136_2==103||LA136_2==105||LA136_2==113) ) {
                        alt136=1;
                    }


                }
                else if ( (LA136_0==52) ) {
                    int LA136_3 = input.LA(2);

                    if ( ((LA136_3 >= CharacterLiteral && LA136_3 <= DecimalLiteral)||LA136_3==FloatingPointLiteral||(LA136_3 >= HexLiteral && LA136_3 <= Identifier)||(LA136_3 >= OctalLiteral && LA136_3 <= StringLiteral)||LA136_3==25||LA136_3==32||(LA136_3 >= 36 && LA136_3 <= 37)||(LA136_3 >= 40 && LA136_3 <= 41)||LA136_3==50||LA136_3==60||LA136_3==62||LA136_3==65||LA136_3==70||LA136_3==73||LA136_3==76||LA136_3==82||LA136_3==84||(LA136_3 >= 86 && LA136_3 <= 87)||LA136_3==93||LA136_3==96||LA136_3==99||LA136_3==103||LA136_3==105||LA136_3==113) ) {
                        alt136=1;
                    }


                }


                switch (alt136) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:901:27: relationalOp shiftExpression
            	    {
            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression4625);
            	    relationalOp();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression4627);
            	    shiftExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop136;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 115, relationalExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "relationalExpression"



    // $ANTLR start "relationalOp"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:904:1: relationalOp : ( ( '<' '=' )=>t1= '<' t2= '=' {...}?| ( '>' '=' )=>t1= '>' t2= '=' {...}?| '<' | '>' );
    public final void relationalOp() throws RecognitionException {
        int relationalOp_StartIndex = input.index();

        Token t1=null;
        Token t2=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 116) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:905:5: ( ( '<' '=' )=>t1= '<' t2= '=' {...}?| ( '>' '=' )=>t1= '>' t2= '=' {...}?| '<' | '>' )
            int alt137=4;
            int LA137_0 = input.LA(1);

            if ( (LA137_0==49) ) {
                int LA137_1 = input.LA(2);

                if ( (LA137_1==50) && (synpred211_Java())) {
                    alt137=1;
                }
                else if ( ((LA137_1 >= CharacterLiteral && LA137_1 <= DecimalLiteral)||LA137_1==FloatingPointLiteral||(LA137_1 >= HexLiteral && LA137_1 <= Identifier)||(LA137_1 >= OctalLiteral && LA137_1 <= StringLiteral)||LA137_1==25||LA137_1==32||(LA137_1 >= 36 && LA137_1 <= 37)||(LA137_1 >= 40 && LA137_1 <= 41)||LA137_1==60||LA137_1==62||LA137_1==65||LA137_1==70||LA137_1==73||LA137_1==76||LA137_1==82||LA137_1==84||(LA137_1 >= 86 && LA137_1 <= 87)||LA137_1==93||LA137_1==96||LA137_1==99||LA137_1==103||LA137_1==105||LA137_1==113) ) {
                    alt137=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 137, 1, input);

                    throw nvae;

                }
            }
            else if ( (LA137_0==52) ) {
                int LA137_2 = input.LA(2);

                if ( (LA137_2==50) && (synpred212_Java())) {
                    alt137=2;
                }
                else if ( ((LA137_2 >= CharacterLiteral && LA137_2 <= DecimalLiteral)||LA137_2==FloatingPointLiteral||(LA137_2 >= HexLiteral && LA137_2 <= Identifier)||(LA137_2 >= OctalLiteral && LA137_2 <= StringLiteral)||LA137_2==25||LA137_2==32||(LA137_2 >= 36 && LA137_2 <= 37)||(LA137_2 >= 40 && LA137_2 <= 41)||LA137_2==60||LA137_2==62||LA137_2==65||LA137_2==70||LA137_2==73||LA137_2==76||LA137_2==82||LA137_2==84||(LA137_2 >= 86 && LA137_2 <= 87)||LA137_2==93||LA137_2==96||LA137_2==99||LA137_2==103||LA137_2==105||LA137_2==113) ) {
                    alt137=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 137, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 137, 0, input);

                throw nvae;

            }
            switch (alt137) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:905:9: ( '<' '=' )=>t1= '<' t2= '=' {...}?
                    {
                    t1=(Token)match(input,49,FOLLOW_49_in_relationalOp4662); if (state.failed) return ;

                    t2=(Token)match(input,50,FOLLOW_50_in_relationalOp4666); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:908:9: ( '>' '=' )=>t1= '>' t2= '=' {...}?
                    {
                    t1=(Token)match(input,52,FOLLOW_52_in_relationalOp4696); if (state.failed) return ;

                    t2=(Token)match(input,50,FOLLOW_50_in_relationalOp4700); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "relationalOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:911:9: '<'
                    {
                    match(input,49,FOLLOW_49_in_relationalOp4721); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:912:9: '>'
                    {
                    match(input,52,FOLLOW_52_in_relationalOp4732); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 116, relationalOp_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "relationalOp"



    // $ANTLR start "shiftExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:915:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final void shiftExpression() throws RecognitionException {
        int shiftExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 117) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:916:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:916:9: additiveExpression ( shiftOp additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_shiftExpression4752);
            additiveExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:916:28: ( shiftOp additiveExpression )*
            loop138:
            do {
                int alt138=2;
                int LA138_0 = input.LA(1);

                if ( (LA138_0==49) ) {
                    int LA138_1 = input.LA(2);

                    if ( (LA138_1==49) ) {
                        int LA138_4 = input.LA(3);

                        if ( ((LA138_4 >= CharacterLiteral && LA138_4 <= DecimalLiteral)||LA138_4==FloatingPointLiteral||(LA138_4 >= HexLiteral && LA138_4 <= Identifier)||(LA138_4 >= OctalLiteral && LA138_4 <= StringLiteral)||LA138_4==25||LA138_4==32||(LA138_4 >= 36 && LA138_4 <= 37)||(LA138_4 >= 40 && LA138_4 <= 41)||LA138_4==60||LA138_4==62||LA138_4==65||LA138_4==70||LA138_4==73||LA138_4==76||LA138_4==82||LA138_4==84||(LA138_4 >= 86 && LA138_4 <= 87)||LA138_4==93||LA138_4==96||LA138_4==99||LA138_4==103||LA138_4==105||LA138_4==113) ) {
                            alt138=1;
                        }


                    }


                }
                else if ( (LA138_0==52) ) {
                    int LA138_2 = input.LA(2);

                    if ( (LA138_2==52) ) {
                        int LA138_5 = input.LA(3);

                        if ( (LA138_5==52) ) {
                            int LA138_7 = input.LA(4);

                            if ( ((LA138_7 >= CharacterLiteral && LA138_7 <= DecimalLiteral)||LA138_7==FloatingPointLiteral||(LA138_7 >= HexLiteral && LA138_7 <= Identifier)||(LA138_7 >= OctalLiteral && LA138_7 <= StringLiteral)||LA138_7==25||LA138_7==32||(LA138_7 >= 36 && LA138_7 <= 37)||(LA138_7 >= 40 && LA138_7 <= 41)||LA138_7==60||LA138_7==62||LA138_7==65||LA138_7==70||LA138_7==73||LA138_7==76||LA138_7==82||LA138_7==84||(LA138_7 >= 86 && LA138_7 <= 87)||LA138_7==93||LA138_7==96||LA138_7==99||LA138_7==103||LA138_7==105||LA138_7==113) ) {
                                alt138=1;
                            }


                        }
                        else if ( ((LA138_5 >= CharacterLiteral && LA138_5 <= DecimalLiteral)||LA138_5==FloatingPointLiteral||(LA138_5 >= HexLiteral && LA138_5 <= Identifier)||(LA138_5 >= OctalLiteral && LA138_5 <= StringLiteral)||LA138_5==25||LA138_5==32||(LA138_5 >= 36 && LA138_5 <= 37)||(LA138_5 >= 40 && LA138_5 <= 41)||LA138_5==60||LA138_5==62||LA138_5==65||LA138_5==70||LA138_5==73||LA138_5==76||LA138_5==82||LA138_5==84||(LA138_5 >= 86 && LA138_5 <= 87)||LA138_5==93||LA138_5==96||LA138_5==99||LA138_5==103||LA138_5==105||LA138_5==113) ) {
                            alt138=1;
                        }


                    }


                }


                switch (alt138) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:916:30: shiftOp additiveExpression
            	    {
            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression4756);
            	    shiftOp();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression4758);
            	    additiveExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop138;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 117, shiftExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "shiftExpression"



    // $ANTLR start "shiftOp"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:919:1: shiftOp : ( ( '<' '<' )=>t1= '<' t2= '<' {...}?| ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?| ( '>' '>' )=>t1= '>' t2= '>' {...}?);
    public final void shiftOp() throws RecognitionException {
        int shiftOp_StartIndex = input.index();

        Token t1=null;
        Token t2=null;
        Token t3=null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 118) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:920:5: ( ( '<' '<' )=>t1= '<' t2= '<' {...}?| ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?| ( '>' '>' )=>t1= '>' t2= '>' {...}?)
            int alt139=3;
            int LA139_0 = input.LA(1);

            if ( (LA139_0==49) && (synpred215_Java())) {
                alt139=1;
            }
            else if ( (LA139_0==52) ) {
                int LA139_2 = input.LA(2);

                if ( (LA139_2==52) ) {
                    int LA139_3 = input.LA(3);

                    if ( (LA139_3==52) && (synpred216_Java())) {
                        alt139=2;
                    }
                    else if ( (LA139_3==36) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==40) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==37) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==41) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==113) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==25) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==32) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==99) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==96) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==DecimalLiteral||LA139_3==HexLiteral||LA139_3==OctalLiteral) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==FloatingPointLiteral) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==CharacterLiteral) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==StringLiteral) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==73||LA139_3==103) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==87) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==86) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==Identifier) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==60||LA139_3==62||LA139_3==65||LA139_3==70||LA139_3==76||LA139_3==82||LA139_3==84||LA139_3==93) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else if ( (LA139_3==105) && (synpred217_Java())) {
                        alt139=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 139, 3, input);

                        throw nvae;

                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 139, 2, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 139, 0, input);

                throw nvae;

            }
            switch (alt139) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:920:9: ( '<' '<' )=>t1= '<' t2= '<' {...}?
                    {
                    t1=(Token)match(input,49,FOLLOW_49_in_shiftOp4789); if (state.failed) return ;

                    t2=(Token)match(input,49,FOLLOW_49_in_shiftOp4793); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:923:9: ( '>' '>' '>' )=>t1= '>' t2= '>' t3= '>' {...}?
                    {
                    t1=(Token)match(input,52,FOLLOW_52_in_shiftOp4825); if (state.failed) return ;

                    t2=(Token)match(input,52,FOLLOW_52_in_shiftOp4829); if (state.failed) return ;

                    t3=(Token)match(input,52,FOLLOW_52_in_shiftOp4833); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() &&
                              t2.getLine() == t3.getLine() && 
                              t2.getCharPositionInLine() + 1 == t3.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() &&\n          $t2.getLine() == $t3.getLine() && \n          $t2.getCharPositionInLine() + 1 == $t3.getCharPositionInLine() ");
                    }

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:928:9: ( '>' '>' )=>t1= '>' t2= '>' {...}?
                    {
                    t1=(Token)match(input,52,FOLLOW_52_in_shiftOp4863); if (state.failed) return ;

                    t2=(Token)match(input,52,FOLLOW_52_in_shiftOp4867); if (state.failed) return ;

                    if ( !(( t1.getLine() == t2.getLine() && 
                              t1.getCharPositionInLine() + 1 == t2.getCharPositionInLine() )) ) {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        throw new FailedPredicateException(input, "shiftOp", " $t1.getLine() == $t2.getLine() && \n          $t1.getCharPositionInLine() + 1 == $t2.getCharPositionInLine() ");
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 118, shiftOp_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "shiftOp"



    // $ANTLR start "additiveExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:934:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final void additiveExpression() throws RecognitionException {
        int additiveExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 119) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:935:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:935:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4897);
            multiplicativeExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:935:34: ( ( '+' | '-' ) multiplicativeExpression )*
            loop140:
            do {
                int alt140=2;
                int LA140_0 = input.LA(1);

                if ( (LA140_0==36||LA140_0==40) ) {
                    alt140=1;
                }


                switch (alt140) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:935:36: ( '+' | '-' ) multiplicativeExpression
            	    {
            	    if ( input.LA(1)==36||input.LA(1)==40 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression4909);
            	    multiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop140;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 119, additiveExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "additiveExpression"



    // $ANTLR start "multiplicativeExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:938:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final void multiplicativeExpression() throws RecognitionException {
        int multiplicativeExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 120) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:939:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:939:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4931);
            unaryExpression();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:939:25: ( ( '*' | '/' | '%' ) unaryExpression )*
            loop141:
            do {
                int alt141=2;
                int LA141_0 = input.LA(1);

                if ( (LA141_0==27||LA141_0==34||LA141_0==45) ) {
                    alt141=1;
                }


                switch (alt141) {
            	case 1 :
            	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:939:27: ( '*' | '/' | '%' ) unaryExpression
            	    {
            	    if ( input.LA(1)==27||input.LA(1)==34||input.LA(1)==45 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	        state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }


            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression4949);
            	    unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop141;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 120, multiplicativeExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "multiplicativeExpression"



    // $ANTLR start "unaryExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:942:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
    public final void unaryExpression() throws RecognitionException {
        int unaryExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 121) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:943:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
            int alt142=5;
            switch ( input.LA(1) ) {
            case 36:
                {
                alt142=1;
                }
                break;
            case 40:
                {
                alt142=2;
                }
                break;
            case 37:
                {
                alt142=3;
                }
                break;
            case 41:
                {
                alt142=4;
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case Identifier:
            case OctalLiteral:
            case StringLiteral:
            case 25:
            case 32:
            case 60:
            case 62:
            case 65:
            case 70:
            case 73:
            case 76:
            case 82:
            case 84:
            case 86:
            case 87:
            case 93:
            case 96:
            case 99:
            case 103:
            case 105:
            case 113:
                {
                alt142=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 142, 0, input);

                throw nvae;

            }

            switch (alt142) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:943:9: '+' unaryExpression
                    {
                    match(input,36,FOLLOW_36_in_unaryExpression4975); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4977);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:944:9: '-' unaryExpression
                    {
                    match(input,40,FOLLOW_40_in_unaryExpression4987); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression4989);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:945:9: '++' unaryExpression
                    {
                    match(input,37,FOLLOW_37_in_unaryExpression4999); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5001);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:946:9: '--' unaryExpression
                    {
                    match(input,41,FOLLOW_41_in_unaryExpression5011); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression5013);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:947:9: unaryExpressionNotPlusMinus
                    {
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression5023);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 121, unaryExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "unaryExpression"



    // $ANTLR start "unaryExpressionNotPlusMinus"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:950:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final void unaryExpressionNotPlusMinus() throws RecognitionException {
        int unaryExpressionNotPlusMinus_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 122) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:951:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt145=4;
            switch ( input.LA(1) ) {
            case 113:
                {
                alt145=1;
                }
                break;
            case 25:
                {
                alt145=2;
                }
                break;
            case 32:
                {
                int LA145_3 = input.LA(2);

                if ( (synpred229_Java()) ) {
                    alt145=3;
                }
                else if ( (true) ) {
                    alt145=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 145, 3, input);

                    throw nvae;

                }
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case Identifier:
            case OctalLiteral:
            case StringLiteral:
            case 60:
            case 62:
            case 65:
            case 70:
            case 73:
            case 76:
            case 82:
            case 84:
            case 86:
            case 87:
            case 93:
            case 96:
            case 99:
            case 103:
            case 105:
                {
                alt145=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 145, 0, input);

                throw nvae;

            }

            switch (alt145) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:951:9: '~' unaryExpression
                    {
                    match(input,113,FOLLOW_113_in_unaryExpressionNotPlusMinus5042); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5044);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:952:9: '!' unaryExpression
                    {
                    match(input,25,FOLLOW_25_in_unaryExpressionNotPlusMinus5054); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5056);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:953:9: castExpression
                    {
                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5066);
                    castExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:954:9: primary ( selector )* ( '++' | '--' )?
                    {
                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus5076);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:954:17: ( selector )*
                    loop143:
                    do {
                        int alt143=2;
                        int LA143_0 = input.LA(1);

                        if ( (LA143_0==43||LA143_0==55) ) {
                            alt143=1;
                        }


                        switch (alt143) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:954:17: selector
                    	    {
                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus5078);
                    	    selector();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop143;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:954:27: ( '++' | '--' )?
                    int alt144=2;
                    int LA144_0 = input.LA(1);

                    if ( (LA144_0==37||LA144_0==41) ) {
                        alt144=1;
                    }
                    switch (alt144) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:
                            {
                            if ( input.LA(1)==37||input.LA(1)==41 ) {
                                input.consume();
                                state.errorRecovery=false;
                                state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 122, unaryExpressionNotPlusMinus_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"



    // $ANTLR start "castExpression"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:957:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus );
    public final void castExpression() throws RecognitionException {
        int castExpression_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 123) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:958:5: ( '(' primitiveType ')' unaryExpression | '(' ( type | expression ) ')' unaryExpressionNotPlusMinus )
            int alt147=2;
            int LA147_0 = input.LA(1);

            if ( (LA147_0==32) ) {
                int LA147_1 = input.LA(2);

                if ( (synpred233_Java()) ) {
                    alt147=1;
                }
                else if ( (true) ) {
                    alt147=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 147, 1, input);

                    throw nvae;

                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 147, 0, input);

                throw nvae;

            }
            switch (alt147) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:958:8: '(' primitiveType ')' unaryExpression
                    {
                    match(input,32,FOLLOW_32_in_castExpression5104); if (state.failed) return ;

                    pushFollow(FOLLOW_primitiveType_in_castExpression5106);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,33,FOLLOW_33_in_castExpression5108); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpression_in_castExpression5110);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:8: '(' ( type | expression ) ')' unaryExpressionNotPlusMinus
                    {
                    match(input,32,FOLLOW_32_in_castExpression5119); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:12: ( type | expression )
                    int alt146=2;
                    alt146 = dfa146.predict(input);
                    switch (alt146) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:13: type
                            {
                            pushFollow(FOLLOW_type_in_castExpression5122);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:20: expression
                            {
                            pushFollow(FOLLOW_expression_in_castExpression5126);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    match(input,33,FOLLOW_33_in_castExpression5129); if (state.failed) return ;

                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5131);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 123, castExpression_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "castExpression"



    // $ANTLR start "primary"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:962:1: primary : ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final void primary() throws RecognitionException {
        int primary_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 124) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:963:5: ( parExpression | 'this' ( '.' Identifier )* ( identifierSuffix )? | 'super' superSuffix | literal | 'new' creator | Identifier ( '.' Identifier )* ( identifierSuffix )? | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt153=8;
            switch ( input.LA(1) ) {
            case 32:
                {
                alt153=1;
                }
                break;
            case 99:
                {
                alt153=2;
                }
                break;
            case 96:
                {
                alt153=3;
                }
                break;
            case CharacterLiteral:
            case DecimalLiteral:
            case FloatingPointLiteral:
            case HexLiteral:
            case OctalLiteral:
            case StringLiteral:
            case 73:
            case 87:
            case 103:
                {
                alt153=4;
                }
                break;
            case 86:
                {
                alt153=5;
                }
                break;
            case Identifier:
                {
                alt153=6;
                }
                break;
            case 60:
            case 62:
            case 65:
            case 70:
            case 76:
            case 82:
            case 84:
            case 93:
                {
                alt153=7;
                }
                break;
            case 105:
                {
                alt153=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 153, 0, input);

                throw nvae;

            }

            switch (alt153) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:963:9: parExpression
                    {
                    pushFollow(FOLLOW_parExpression_in_primary5150);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:9: 'this' ( '.' Identifier )* ( identifierSuffix )?
                    {
                    match(input,99,FOLLOW_99_in_primary5160); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:16: ( '.' Identifier )*
                    loop148:
                    do {
                        int alt148=2;
                        int LA148_0 = input.LA(1);

                        if ( (LA148_0==43) ) {
                            int LA148_2 = input.LA(2);

                            if ( (LA148_2==Identifier) ) {
                                int LA148_3 = input.LA(3);

                                if ( (synpred236_Java()) ) {
                                    alt148=1;
                                }


                            }


                        }


                        switch (alt148) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:17: '.' Identifier
                    	    {
                    	    match(input,43,FOLLOW_43_in_primary5163); if (state.failed) return ;

                    	    match(input,Identifier,FOLLOW_Identifier_in_primary5165); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop148;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:34: ( identifierSuffix )?
                    int alt149=2;
                    switch ( input.LA(1) ) {
                        case 55:
                            {
                            int LA149_1 = input.LA(2);

                            if ( (synpred237_Java()) ) {
                                alt149=1;
                            }
                            }
                            break;
                        case 32:
                            {
                            alt149=1;
                            }
                            break;
                        case 43:
                            {
                            int LA149_3 = input.LA(2);

                            if ( (synpred237_Java()) ) {
                                alt149=1;
                            }
                            }
                            break;
                    }

                    switch (alt149) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:34: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5169);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:965:9: 'super' superSuffix
                    {
                    match(input,96,FOLLOW_96_in_primary5180); if (state.failed) return ;

                    pushFollow(FOLLOW_superSuffix_in_primary5182);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:966:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_primary5192);
                    literal();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:967:9: 'new' creator
                    {
                    match(input,86,FOLLOW_86_in_primary5202); if (state.failed) return ;

                    pushFollow(FOLLOW_creator_in_primary5204);
                    creator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:9: Identifier ( '.' Identifier )* ( identifierSuffix )?
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_primary5214); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:20: ( '.' Identifier )*
                    loop150:
                    do {
                        int alt150=2;
                        int LA150_0 = input.LA(1);

                        if ( (LA150_0==43) ) {
                            int LA150_2 = input.LA(2);

                            if ( (LA150_2==Identifier) ) {
                                int LA150_3 = input.LA(3);

                                if ( (synpred242_Java()) ) {
                                    alt150=1;
                                }


                            }


                        }


                        switch (alt150) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:21: '.' Identifier
                    	    {
                    	    match(input,43,FOLLOW_43_in_primary5217); if (state.failed) return ;

                    	    match(input,Identifier,FOLLOW_Identifier_in_primary5219); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop150;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:38: ( identifierSuffix )?
                    int alt151=2;
                    switch ( input.LA(1) ) {
                        case 55:
                            {
                            int LA151_1 = input.LA(2);

                            if ( (synpred243_Java()) ) {
                                alt151=1;
                            }
                            }
                            break;
                        case 32:
                            {
                            alt151=1;
                            }
                            break;
                        case 43:
                            {
                            int LA151_3 = input.LA(2);

                            if ( (synpred243_Java()) ) {
                                alt151=1;
                            }
                            }
                            break;
                    }

                    switch (alt151) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:38: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary5223);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:969:9: primitiveType ( '[' ']' )* '.' 'class'
                    {
                    pushFollow(FOLLOW_primitiveType_in_primary5234);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:969:23: ( '[' ']' )*
                    loop152:
                    do {
                        int alt152=2;
                        int LA152_0 = input.LA(1);

                        if ( (LA152_0==55) ) {
                            alt152=1;
                        }


                        switch (alt152) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:969:24: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_primary5237); if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_primary5239); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop152;
                        }
                    } while (true);


                    match(input,43,FOLLOW_43_in_primary5243); if (state.failed) return ;

                    match(input,66,FOLLOW_66_in_primary5245); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:970:9: 'void' '.' 'class'
                    {
                    match(input,105,FOLLOW_105_in_primary5255); if (state.failed) return ;

                    match(input,43,FOLLOW_43_in_primary5257); if (state.failed) return ;

                    match(input,66,FOLLOW_66_in_primary5259); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 124, primary_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "primary"



    // $ANTLR start "identifierSuffix"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:973:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator );
    public final void identifierSuffix() throws RecognitionException {
        int identifierSuffix_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 125) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:974:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' explicitGenericInvocation | '.' 'this' | '.' 'super' arguments | '.' 'new' innerCreator )
            int alt156=8;
            switch ( input.LA(1) ) {
            case 55:
                {
                int LA156_1 = input.LA(2);

                if ( (LA156_1==56) ) {
                    alt156=1;
                }
                else if ( ((LA156_1 >= CharacterLiteral && LA156_1 <= DecimalLiteral)||LA156_1==FloatingPointLiteral||(LA156_1 >= HexLiteral && LA156_1 <= Identifier)||(LA156_1 >= OctalLiteral && LA156_1 <= StringLiteral)||LA156_1==25||LA156_1==32||(LA156_1 >= 36 && LA156_1 <= 37)||(LA156_1 >= 40 && LA156_1 <= 41)||LA156_1==60||LA156_1==62||LA156_1==65||LA156_1==70||LA156_1==73||LA156_1==76||LA156_1==82||LA156_1==84||(LA156_1 >= 86 && LA156_1 <= 87)||LA156_1==93||LA156_1==96||LA156_1==99||LA156_1==103||LA156_1==105||LA156_1==113) ) {
                    alt156=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 156, 1, input);

                    throw nvae;

                }
                }
                break;
            case 32:
                {
                alt156=3;
                }
                break;
            case 43:
                {
                switch ( input.LA(2) ) {
                case 66:
                    {
                    alt156=4;
                    }
                    break;
                case 99:
                    {
                    alt156=6;
                    }
                    break;
                case 96:
                    {
                    alt156=7;
                    }
                    break;
                case 86:
                    {
                    alt156=8;
                    }
                    break;
                case 49:
                    {
                    alt156=5;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 156, 3, input);

                    throw nvae;

                }

                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 156, 0, input);

                throw nvae;

            }

            switch (alt156) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:974:9: ( '[' ']' )+ '.' 'class'
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:974:9: ( '[' ']' )+
                    int cnt154=0;
                    loop154:
                    do {
                        int alt154=2;
                        int LA154_0 = input.LA(1);

                        if ( (LA154_0==55) ) {
                            alt154=1;
                        }


                        switch (alt154) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:974:10: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_identifierSuffix5279); if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_identifierSuffix5281); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt154 >= 1 ) break loop154;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(154, input);
                                throw eee;
                        }
                        cnt154++;
                    } while (true);


                    match(input,43,FOLLOW_43_in_identifierSuffix5285); if (state.failed) return ;

                    match(input,66,FOLLOW_66_in_identifierSuffix5287); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:975:9: ( '[' expression ']' )+
                    {
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:975:9: ( '[' expression ']' )+
                    int cnt155=0;
                    loop155:
                    do {
                        int alt155=2;
                        int LA155_0 = input.LA(1);

                        if ( (LA155_0==55) ) {
                            int LA155_2 = input.LA(2);

                            if ( (synpred249_Java()) ) {
                                alt155=1;
                            }


                        }


                        switch (alt155) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:975:10: '[' expression ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_identifierSuffix5298); if (state.failed) return ;

                    	    pushFollow(FOLLOW_expression_in_identifierSuffix5300);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_identifierSuffix5302); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt155 >= 1 ) break loop155;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(155, input);
                                throw eee;
                        }
                        cnt155++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:976:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_identifierSuffix5315);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:977:9: '.' 'class'
                    {
                    match(input,43,FOLLOW_43_in_identifierSuffix5325); if (state.failed) return ;

                    match(input,66,FOLLOW_66_in_identifierSuffix5327); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:978:9: '.' explicitGenericInvocation
                    {
                    match(input,43,FOLLOW_43_in_identifierSuffix5337); if (state.failed) return ;

                    pushFollow(FOLLOW_explicitGenericInvocation_in_identifierSuffix5339);
                    explicitGenericInvocation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:979:9: '.' 'this'
                    {
                    match(input,43,FOLLOW_43_in_identifierSuffix5349); if (state.failed) return ;

                    match(input,99,FOLLOW_99_in_identifierSuffix5351); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:980:9: '.' 'super' arguments
                    {
                    match(input,43,FOLLOW_43_in_identifierSuffix5361); if (state.failed) return ;

                    match(input,96,FOLLOW_96_in_identifierSuffix5363); if (state.failed) return ;

                    pushFollow(FOLLOW_arguments_in_identifierSuffix5365);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:981:9: '.' 'new' innerCreator
                    {
                    match(input,43,FOLLOW_43_in_identifierSuffix5375); if (state.failed) return ;

                    match(input,86,FOLLOW_86_in_identifierSuffix5377); if (state.failed) return ;

                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix5379);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 125, identifierSuffix_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "identifierSuffix"



    // $ANTLR start "creator"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:984:1: creator : ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) );
    public final void creator() throws RecognitionException {
        int creator_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 126) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:985:5: ( nonWildcardTypeArguments createdName classCreatorRest | createdName ( arrayCreatorRest | classCreatorRest ) )
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==49) ) {
                alt158=1;
            }
            else if ( (LA158_0==Identifier||LA158_0==60||LA158_0==62||LA158_0==65||LA158_0==70||LA158_0==76||LA158_0==82||LA158_0==84||LA158_0==93) ) {
                alt158=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 158, 0, input);

                throw nvae;

            }
            switch (alt158) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:985:9: nonWildcardTypeArguments createdName classCreatorRest
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator5398);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_createdName_in_creator5400);
                    createdName();

                    state._fsp--;
                    if (state.failed) return ;

                    pushFollow(FOLLOW_classCreatorRest_in_creator5402);
                    classCreatorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:986:9: createdName ( arrayCreatorRest | classCreatorRest )
                    {
                    pushFollow(FOLLOW_createdName_in_creator5412);
                    createdName();

                    state._fsp--;
                    if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:986:21: ( arrayCreatorRest | classCreatorRest )
                    int alt157=2;
                    int LA157_0 = input.LA(1);

                    if ( (LA157_0==55) ) {
                        alt157=1;
                    }
                    else if ( (LA157_0==32) ) {
                        alt157=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 157, 0, input);

                        throw nvae;

                    }
                    switch (alt157) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:986:22: arrayCreatorRest
                            {
                            pushFollow(FOLLOW_arrayCreatorRest_in_creator5415);
                            arrayCreatorRest();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:986:41: classCreatorRest
                            {
                            pushFollow(FOLLOW_classCreatorRest_in_creator5419);
                            classCreatorRest();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 126, creator_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "creator"



    // $ANTLR start "createdName"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:989:1: createdName : ( classOrInterfaceType | primitiveType );
    public final void createdName() throws RecognitionException {
        int createdName_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 127) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:990:5: ( classOrInterfaceType | primitiveType )
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==Identifier) ) {
                alt159=1;
            }
            else if ( (LA159_0==60||LA159_0==62||LA159_0==65||LA159_0==70||LA159_0==76||LA159_0==82||LA159_0==84||LA159_0==93) ) {
                alt159=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 159, 0, input);

                throw nvae;

            }
            switch (alt159) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:990:9: classOrInterfaceType
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName5439);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:991:9: primitiveType
                    {
                    pushFollow(FOLLOW_primitiveType_in_createdName5449);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 127, createdName_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "createdName"



    // $ANTLR start "innerCreator"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:994:1: innerCreator : ( nonWildcardTypeArguments )? Identifier classCreatorRest ;
    public final void innerCreator() throws RecognitionException {
        int innerCreator_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 128) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:995:5: ( ( nonWildcardTypeArguments )? Identifier classCreatorRest )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:995:9: ( nonWildcardTypeArguments )? Identifier classCreatorRest
            {
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:995:9: ( nonWildcardTypeArguments )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( (LA160_0==49) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:995:9: nonWildcardTypeArguments
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator5472);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,Identifier,FOLLOW_Identifier_in_innerCreator5475); if (state.failed) return ;

            pushFollow(FOLLOW_classCreatorRest_in_innerCreator5477);
            classCreatorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 128, innerCreator_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "innerCreator"



    // $ANTLR start "arrayCreatorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:998:1: arrayCreatorRest : '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) ;
    public final void arrayCreatorRest() throws RecognitionException {
        int arrayCreatorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 129) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:999:5: ( '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* ) )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:999:9: '[' ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            {
            match(input,55,FOLLOW_55_in_arrayCreatorRest5496); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1000:9: ( ']' ( '[' ']' )* arrayInitializer | expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            int alt164=2;
            int LA164_0 = input.LA(1);

            if ( (LA164_0==56) ) {
                alt164=1;
            }
            else if ( ((LA164_0 >= CharacterLiteral && LA164_0 <= DecimalLiteral)||LA164_0==FloatingPointLiteral||(LA164_0 >= HexLiteral && LA164_0 <= Identifier)||(LA164_0 >= OctalLiteral && LA164_0 <= StringLiteral)||LA164_0==25||LA164_0==32||(LA164_0 >= 36 && LA164_0 <= 37)||(LA164_0 >= 40 && LA164_0 <= 41)||LA164_0==60||LA164_0==62||LA164_0==65||LA164_0==70||LA164_0==73||LA164_0==76||LA164_0==82||LA164_0==84||(LA164_0 >= 86 && LA164_0 <= 87)||LA164_0==93||LA164_0==96||LA164_0==99||LA164_0==103||LA164_0==105||LA164_0==113) ) {
                alt164=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 164, 0, input);

                throw nvae;

            }
            switch (alt164) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1000:13: ']' ( '[' ']' )* arrayInitializer
                    {
                    match(input,56,FOLLOW_56_in_arrayCreatorRest5510); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1000:17: ( '[' ']' )*
                    loop161:
                    do {
                        int alt161=2;
                        int LA161_0 = input.LA(1);

                        if ( (LA161_0==55) ) {
                            alt161=1;
                        }


                        switch (alt161) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1000:18: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_arrayCreatorRest5513); if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_arrayCreatorRest5515); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop161;
                        }
                    } while (true);


                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreatorRest5519);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:13: expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_expression_in_arrayCreatorRest5533);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,56,FOLLOW_56_in_arrayCreatorRest5535); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:28: ( '[' expression ']' )*
                    loop162:
                    do {
                        int alt162=2;
                        int LA162_0 = input.LA(1);

                        if ( (LA162_0==55) ) {
                            int LA162_1 = input.LA(2);

                            if ( (synpred262_Java()) ) {
                                alt162=1;
                            }


                        }


                        switch (alt162) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:29: '[' expression ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_arrayCreatorRest5538); if (state.failed) return ;

                    	    pushFollow(FOLLOW_expression_in_arrayCreatorRest5540);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_arrayCreatorRest5542); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop162;
                        }
                    } while (true);


                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:50: ( '[' ']' )*
                    loop163:
                    do {
                        int alt163=2;
                        int LA163_0 = input.LA(1);

                        if ( (LA163_0==55) ) {
                            int LA163_2 = input.LA(2);

                            if ( (LA163_2==56) ) {
                                alt163=1;
                            }


                        }


                        switch (alt163) {
                    	case 1 :
                    	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:51: '[' ']'
                    	    {
                    	    match(input,55,FOLLOW_55_in_arrayCreatorRest5547); if (state.failed) return ;

                    	    match(input,56,FOLLOW_56_in_arrayCreatorRest5549); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop163;
                        }
                    } while (true);


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 129, arrayCreatorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "arrayCreatorRest"



    // $ANTLR start "classCreatorRest"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1005:1: classCreatorRest : arguments ( classBody )? ;
    public final void classCreatorRest() throws RecognitionException {
        int classCreatorRest_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 130) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1006:5: ( arguments ( classBody )? )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1006:9: arguments ( classBody )?
            {
            pushFollow(FOLLOW_arguments_in_classCreatorRest5580);
            arguments();

            state._fsp--;
            if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1006:19: ( classBody )?
            int alt165=2;
            int LA165_0 = input.LA(1);

            if ( (LA165_0==108) ) {
                alt165=1;
            }
            switch (alt165) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1006:19: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_classCreatorRest5582);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 130, classCreatorRest_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "classCreatorRest"



    // $ANTLR start "explicitGenericInvocation"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1009:1: explicitGenericInvocation : nonWildcardTypeArguments Identifier arguments ;
    public final void explicitGenericInvocation() throws RecognitionException {
        int explicitGenericInvocation_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 131) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1010:5: ( nonWildcardTypeArguments Identifier arguments )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1010:9: nonWildcardTypeArguments Identifier arguments
            {
            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5606);
            nonWildcardTypeArguments();

            state._fsp--;
            if (state.failed) return ;

            match(input,Identifier,FOLLOW_Identifier_in_explicitGenericInvocation5608); if (state.failed) return ;

            pushFollow(FOLLOW_arguments_in_explicitGenericInvocation5610);
            arguments();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 131, explicitGenericInvocation_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "explicitGenericInvocation"



    // $ANTLR start "nonWildcardTypeArguments"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1013:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final void nonWildcardTypeArguments() throws RecognitionException {
        int nonWildcardTypeArguments_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 132) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1014:5: ( '<' typeList '>' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1014:9: '<' typeList '>'
            {
            match(input,49,FOLLOW_49_in_nonWildcardTypeArguments5633); if (state.failed) return ;

            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments5635);
            typeList();

            state._fsp--;
            if (state.failed) return ;

            match(input,52,FOLLOW_52_in_nonWildcardTypeArguments5637); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 132, nonWildcardTypeArguments_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "nonWildcardTypeArguments"



    // $ANTLR start "selector"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1017:1: selector : ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' );
    public final void selector() throws RecognitionException {
        int selector_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 133) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1018:5: ( '.' Identifier ( arguments )? | '.' 'this' | '.' 'super' superSuffix | '.' 'new' innerCreator | '[' expression ']' )
            int alt167=5;
            int LA167_0 = input.LA(1);

            if ( (LA167_0==43) ) {
                switch ( input.LA(2) ) {
                case Identifier:
                    {
                    alt167=1;
                    }
                    break;
                case 99:
                    {
                    alt167=2;
                    }
                    break;
                case 96:
                    {
                    alt167=3;
                    }
                    break;
                case 86:
                    {
                    alt167=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 167, 1, input);

                    throw nvae;

                }

            }
            else if ( (LA167_0==55) ) {
                alt167=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 167, 0, input);

                throw nvae;

            }
            switch (alt167) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1018:9: '.' Identifier ( arguments )?
                    {
                    match(input,43,FOLLOW_43_in_selector5660); if (state.failed) return ;

                    match(input,Identifier,FOLLOW_Identifier_in_selector5662); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1018:24: ( arguments )?
                    int alt166=2;
                    int LA166_0 = input.LA(1);

                    if ( (LA166_0==32) ) {
                        alt166=1;
                    }
                    switch (alt166) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1018:24: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_selector5664);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1019:9: '.' 'this'
                    {
                    match(input,43,FOLLOW_43_in_selector5675); if (state.failed) return ;

                    match(input,99,FOLLOW_99_in_selector5677); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1020:9: '.' 'super' superSuffix
                    {
                    match(input,43,FOLLOW_43_in_selector5687); if (state.failed) return ;

                    match(input,96,FOLLOW_96_in_selector5689); if (state.failed) return ;

                    pushFollow(FOLLOW_superSuffix_in_selector5691);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1021:9: '.' 'new' innerCreator
                    {
                    match(input,43,FOLLOW_43_in_selector5701); if (state.failed) return ;

                    match(input,86,FOLLOW_86_in_selector5703); if (state.failed) return ;

                    pushFollow(FOLLOW_innerCreator_in_selector5705);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1022:9: '[' expression ']'
                    {
                    match(input,55,FOLLOW_55_in_selector5715); if (state.failed) return ;

                    pushFollow(FOLLOW_expression_in_selector5717);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    match(input,56,FOLLOW_56_in_selector5719); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 133, selector_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "selector"



    // $ANTLR start "superSuffix"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1025:1: superSuffix : ( arguments | '.' Identifier ( arguments )? );
    public final void superSuffix() throws RecognitionException {
        int superSuffix_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 134) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1026:5: ( arguments | '.' Identifier ( arguments )? )
            int alt169=2;
            int LA169_0 = input.LA(1);

            if ( (LA169_0==32) ) {
                alt169=1;
            }
            else if ( (LA169_0==43) ) {
                alt169=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 169, 0, input);

                throw nvae;

            }
            switch (alt169) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1026:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_superSuffix5742);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1027:9: '.' Identifier ( arguments )?
                    {
                    match(input,43,FOLLOW_43_in_superSuffix5752); if (state.failed) return ;

                    match(input,Identifier,FOLLOW_Identifier_in_superSuffix5754); if (state.failed) return ;

                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1027:24: ( arguments )?
                    int alt168=2;
                    int LA168_0 = input.LA(1);

                    if ( (LA168_0==32) ) {
                        alt168=1;
                    }
                    switch (alt168) {
                        case 1 :
                            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1027:24: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_superSuffix5756);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 134, superSuffix_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "superSuffix"



    // $ANTLR start "arguments"
    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1030:1: arguments : '(' ( expressionList )? ')' ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 135) ) { return ; }

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1031:5: ( '(' ( expressionList )? ')' )
            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1031:9: '(' ( expressionList )? ')'
            {
            match(input,32,FOLLOW_32_in_arguments5776); if (state.failed) return ;

            // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1031:13: ( expressionList )?
            int alt170=2;
            int LA170_0 = input.LA(1);

            if ( ((LA170_0 >= CharacterLiteral && LA170_0 <= DecimalLiteral)||LA170_0==FloatingPointLiteral||(LA170_0 >= HexLiteral && LA170_0 <= Identifier)||(LA170_0 >= OctalLiteral && LA170_0 <= StringLiteral)||LA170_0==25||LA170_0==32||(LA170_0 >= 36 && LA170_0 <= 37)||(LA170_0 >= 40 && LA170_0 <= 41)||LA170_0==60||LA170_0==62||LA170_0==65||LA170_0==70||LA170_0==73||LA170_0==76||LA170_0==82||LA170_0==84||(LA170_0 >= 86 && LA170_0 <= 87)||LA170_0==93||LA170_0==96||LA170_0==99||LA170_0==103||LA170_0==105||LA170_0==113) ) {
                alt170=1;
            }
            switch (alt170) {
                case 1 :
                    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1031:13: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_arguments5778);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            match(input,33,FOLLOW_33_in_arguments5781); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
            if ( state.backtracking>0 ) { memoize(input, 135, arguments_StartIndex); }

        }
        return ;
    }
    // $ANTLR end "arguments"

    // $ANTLR start synpred5_Java
    public final void synpred5_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:230:9: ( annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* ) )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:230:9: annotations ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
        {
        pushFollow(FOLLOW_annotations_in_synpred5_Java66);
        annotations();

        state._fsp--;
        if (state.failed) return ;

        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:9: ( packageDeclaration ( importDeclaration )* ( typeDeclaration )* | classOrInterfaceDeclaration ( typeDeclaration )* )
        int alt176=2;
        int LA176_0 = input.LA(1);

        if ( (LA176_0==88) ) {
            alt176=1;
        }
        else if ( (LA176_0==ENUM||LA176_0==54||LA176_0==59||LA176_0==66||LA176_0==74||LA176_0==83||(LA176_0 >= 89 && LA176_0 <= 91)||(LA176_0 >= 94 && LA176_0 <= 95)) ) {
            alt176=2;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            NoViableAltException nvae =
                new NoViableAltException("", 176, 0, input);

            throw nvae;

        }
        switch (alt176) {
            case 1 :
                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:13: packageDeclaration ( importDeclaration )* ( typeDeclaration )*
                {
                pushFollow(FOLLOW_packageDeclaration_in_synpred5_Java80);
                packageDeclaration();

                state._fsp--;
                if (state.failed) return ;

                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:32: ( importDeclaration )*
                loop173:
                do {
                    int alt173=2;
                    int LA173_0 = input.LA(1);

                    if ( (LA173_0==80) ) {
                        alt173=1;
                    }


                    switch (alt173) {
                	case 1 :
                	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:32: importDeclaration
                	    {
                	    pushFollow(FOLLOW_importDeclaration_in_synpred5_Java82);
                	    importDeclaration();

                	    state._fsp--;
                	    if (state.failed) return ;

                	    }
                	    break;

                	default :
                	    break loop173;
                    }
                } while (true);


                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:51: ( typeDeclaration )*
                loop174:
                do {
                    int alt174=2;
                    int LA174_0 = input.LA(1);

                    if ( (LA174_0==ENUM||LA174_0==48||LA174_0==54||LA174_0==59||LA174_0==66||LA174_0==74||LA174_0==83||(LA174_0 >= 89 && LA174_0 <= 91)||(LA174_0 >= 94 && LA174_0 <= 95)) ) {
                        alt174=1;
                    }


                    switch (alt174) {
                	case 1 :
                	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:231:51: typeDeclaration
                	    {
                	    pushFollow(FOLLOW_typeDeclaration_in_synpred5_Java85);
                	    typeDeclaration();

                	    state._fsp--;
                	    if (state.failed) return ;

                	    }
                	    break;

                	default :
                	    break loop174;
                    }
                } while (true);


                }
                break;
            case 2 :
                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:13: classOrInterfaceDeclaration ( typeDeclaration )*
                {
                pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred5_Java100);
                classOrInterfaceDeclaration();

                state._fsp--;
                if (state.failed) return ;

                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:41: ( typeDeclaration )*
                loop175:
                do {
                    int alt175=2;
                    int LA175_0 = input.LA(1);

                    if ( (LA175_0==ENUM||LA175_0==48||LA175_0==54||LA175_0==59||LA175_0==66||LA175_0==74||LA175_0==83||(LA175_0 >= 89 && LA175_0 <= 91)||(LA175_0 >= 94 && LA175_0 <= 95)) ) {
                        alt175=1;
                    }


                    switch (alt175) {
                	case 1 :
                	    // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:232:41: typeDeclaration
                	    {
                	    pushFollow(FOLLOW_typeDeclaration_in_synpred5_Java102);
                	    typeDeclaration();

                	    state._fsp--;
                	    if (state.failed) return ;

                	    }
                	    break;

                	default :
                	    break loop175;
                    }
                } while (true);


                }
                break;

        }


        }

    }
    // $ANTLR end synpred5_Java

    // $ANTLR start synpred113_Java
    public final void synpred113_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:13: ( explicitConstructorInvocation )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:544:13: explicitConstructorInvocation
        {
        pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred113_Java2484);
        explicitConstructorInvocation();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred113_Java

    // $ANTLR start synpred117_Java
    public final void synpred117_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
        {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: ( nonWildcardTypeArguments )?
        int alt184=2;
        int LA184_0 = input.LA(1);

        if ( (LA184_0==49) ) {
            alt184=1;
        }
        switch (alt184) {
            case 1 :
                // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:548:9: nonWildcardTypeArguments
                {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred117_Java2509);
                nonWildcardTypeArguments();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }


        if ( input.LA(1)==96||input.LA(1)==99 ) {
            input.consume();
            state.errorRecovery=false;
            state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }


        pushFollow(FOLLOW_arguments_in_synpred117_Java2520);
        arguments();

        state._fsp--;
        if (state.failed) return ;

        match(input,48,FOLLOW_48_in_synpred117_Java2522); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred117_Java

    // $ANTLR start synpred128_Java
    public final void synpred128_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:9: ( annotation )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:580:9: annotation
        {
        pushFollow(FOLLOW_annotation_in_synpred128_Java2733);
        annotation();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred128_Java

    // $ANTLR start synpred151_Java
    public final void synpred151_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:653:9: ( localVariableDeclarationStatement )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:653:9: localVariableDeclarationStatement
        {
        pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred151_Java3260);
        localVariableDeclarationStatement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred151_Java

    // $ANTLR start synpred152_Java
    public final void synpred152_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:654:9: ( classOrInterfaceDeclaration )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:654:9: classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred152_Java3272);
        classOrInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred152_Java

    // $ANTLR start synpred157_Java
    public final void synpred157_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:674:54: ( 'else' statement )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:674:54: 'else' statement
        {
        match(input,71,FOLLOW_71_in_synpred157_Java3426); if (state.failed) return ;

        pushFollow(FOLLOW_statement_in_synpred157_Java3428);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred157_Java

    // $ANTLR start synpred162_Java
    public final void synpred162_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:679:11: ( catches 'finally' block )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:679:11: catches 'finally' block
        {
        pushFollow(FOLLOW_catches_in_synpred162_Java3504);
        catches();

        state._fsp--;
        if (state.failed) return ;

        match(input,75,FOLLOW_75_in_synpred162_Java3506); if (state.failed) return ;

        pushFollow(FOLLOW_block_in_synpred162_Java3508);
        block();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred162_Java

    // $ANTLR start synpred163_Java
    public final void synpred163_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:680:11: ( catches )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:680:11: catches
        {
        pushFollow(FOLLOW_catches_in_synpred163_Java3520);
        catches();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred163_Java

    // $ANTLR start synpred178_Java
    public final void synpred178_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:9: ( switchLabel )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:715:9: switchLabel
        {
        pushFollow(FOLLOW_switchLabel_in_synpred178_Java3811);
        switchLabel();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred178_Java

    // $ANTLR start synpred180_Java
    public final void synpred180_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:719:9: ( 'case' constantExpression ':' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:719:9: 'case' constantExpression ':'
        {
        match(input,63,FOLLOW_63_in_synpred180_Java3838); if (state.failed) return ;

        pushFollow(FOLLOW_constantExpression_in_synpred180_Java3840);
        constantExpression();

        state._fsp--;
        if (state.failed) return ;

        match(input,47,FOLLOW_47_in_synpred180_Java3842); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred180_Java

    // $ANTLR start synpred181_Java
    public final void synpred181_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:720:9: ( 'case' enumConstantName ':' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:720:9: 'case' enumConstantName ':'
        {
        match(input,63,FOLLOW_63_in_synpred181_Java3852); if (state.failed) return ;

        pushFollow(FOLLOW_enumConstantName_in_synpred181_Java3854);
        enumConstantName();

        state._fsp--;
        if (state.failed) return ;

        match(input,47,FOLLOW_47_in_synpred181_Java3856); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred181_Java

    // $ANTLR start synpred182_Java
    public final void synpred182_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:726:9: ( enhancedForControl )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:726:9: enhancedForControl
        {
        pushFollow(FOLLOW_enhancedForControl_in_synpred182_Java3899);
        enhancedForControl();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred182_Java

    // $ANTLR start synpred186_Java
    public final void synpred186_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:731:9: ( localVariableDeclaration )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:731:9: localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred186_Java3939);
        localVariableDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred186_Java

    // $ANTLR start synpred188_Java
    public final void synpred188_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:32: ( assignmentOperator expression )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:762:32: assignmentOperator expression
        {
        pushFollow(FOLLOW_assignmentOperator_in_synpred188_Java4122);
        assignmentOperator();

        state._fsp--;
        if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred188_Java4124);
        expression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred188_Java

    // $ANTLR start synpred198_Java
    public final void synpred198_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:775:9: ( '<' '<' '=' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:775:10: '<' '<' '='
        {
        match(input,49,FOLLOW_49_in_synpred198_Java4241); if (state.failed) return ;

        match(input,49,FOLLOW_49_in_synpred198_Java4243); if (state.failed) return ;

        match(input,50,FOLLOW_50_in_synpred198_Java4245); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred198_Java

    // $ANTLR start synpred199_Java
    public final void synpred199_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:780:9: ( '>' '>' '>' '=' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:780:10: '>' '>' '>' '='
        {
        match(input,52,FOLLOW_52_in_synpred199_Java4281); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred199_Java4283); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred199_Java4285); if (state.failed) return ;

        match(input,50,FOLLOW_50_in_synpred199_Java4287); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred199_Java

    // $ANTLR start synpred200_Java
    public final void synpred200_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:787:9: ( '>' '>' '=' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:787:10: '>' '>' '='
        {
        match(input,52,FOLLOW_52_in_synpred200_Java4326); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred200_Java4328); if (state.failed) return ;

        match(input,50,FOLLOW_50_in_synpred200_Java4330); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred200_Java

    // $ANTLR start synpred211_Java
    public final void synpred211_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:905:9: ( '<' '=' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:905:10: '<' '='
        {
        match(input,49,FOLLOW_49_in_synpred211_Java4654); if (state.failed) return ;

        match(input,50,FOLLOW_50_in_synpred211_Java4656); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred211_Java

    // $ANTLR start synpred212_Java
    public final void synpred212_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:908:9: ( '>' '=' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:908:10: '>' '='
        {
        match(input,52,FOLLOW_52_in_synpred212_Java4688); if (state.failed) return ;

        match(input,50,FOLLOW_50_in_synpred212_Java4690); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred212_Java

    // $ANTLR start synpred215_Java
    public final void synpred215_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:920:9: ( '<' '<' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:920:10: '<' '<'
        {
        match(input,49,FOLLOW_49_in_synpred215_Java4781); if (state.failed) return ;

        match(input,49,FOLLOW_49_in_synpred215_Java4783); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred215_Java

    // $ANTLR start synpred216_Java
    public final void synpred216_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:923:9: ( '>' '>' '>' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:923:10: '>' '>' '>'
        {
        match(input,52,FOLLOW_52_in_synpred216_Java4815); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred216_Java4817); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred216_Java4819); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred216_Java

    // $ANTLR start synpred217_Java
    public final void synpred217_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:928:9: ( '>' '>' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:928:10: '>' '>'
        {
        match(input,52,FOLLOW_52_in_synpred217_Java4855); if (state.failed) return ;

        match(input,52,FOLLOW_52_in_synpred217_Java4857); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred217_Java

    // $ANTLR start synpred229_Java
    public final void synpred229_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:953:9: ( castExpression )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:953:9: castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred229_Java5066);
        castExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred229_Java

    // $ANTLR start synpred233_Java
    public final void synpred233_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:958:8: ( '(' primitiveType ')' unaryExpression )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:958:8: '(' primitiveType ')' unaryExpression
        {
        match(input,32,FOLLOW_32_in_synpred233_Java5104); if (state.failed) return ;

        pushFollow(FOLLOW_primitiveType_in_synpred233_Java5106);
        primitiveType();

        state._fsp--;
        if (state.failed) return ;

        match(input,33,FOLLOW_33_in_synpred233_Java5108); if (state.failed) return ;

        pushFollow(FOLLOW_unaryExpression_in_synpred233_Java5110);
        unaryExpression();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred233_Java

    // $ANTLR start synpred234_Java
    public final void synpred234_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:13: ( type )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:959:13: type
        {
        pushFollow(FOLLOW_type_in_synpred234_Java5122);
        type();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred234_Java

    // $ANTLR start synpred236_Java
    public final void synpred236_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:17: ( '.' Identifier )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:17: '.' Identifier
        {
        match(input,43,FOLLOW_43_in_synpred236_Java5163); if (state.failed) return ;

        match(input,Identifier,FOLLOW_Identifier_in_synpred236_Java5165); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred236_Java

    // $ANTLR start synpred237_Java
    public final void synpred237_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:34: ( identifierSuffix )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:964:34: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred237_Java5169);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred237_Java

    // $ANTLR start synpred242_Java
    public final void synpred242_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:21: ( '.' Identifier )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:21: '.' Identifier
        {
        match(input,43,FOLLOW_43_in_synpred242_Java5217); if (state.failed) return ;

        match(input,Identifier,FOLLOW_Identifier_in_synpred242_Java5219); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred242_Java

    // $ANTLR start synpred243_Java
    public final void synpred243_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:38: ( identifierSuffix )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:968:38: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred243_Java5223);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }

    }
    // $ANTLR end synpred243_Java

    // $ANTLR start synpred249_Java
    public final void synpred249_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:975:10: ( '[' expression ']' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:975:10: '[' expression ']'
        {
        match(input,55,FOLLOW_55_in_synpred249_Java5298); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred249_Java5300);
        expression();

        state._fsp--;
        if (state.failed) return ;

        match(input,56,FOLLOW_56_in_synpred249_Java5302); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred249_Java

    // $ANTLR start synpred262_Java
    public final void synpred262_Java_fragment() throws RecognitionException {
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:29: ( '[' expression ']' )
        // /Users/rafaeldurelli/Documents/workspace/ANTLR Java Tester/src/a/b/c/Java.g:1001:29: '[' expression ']'
        {
        match(input,55,FOLLOW_55_in_synpred262_Java5538); if (state.failed) return ;

        pushFollow(FOLLOW_expression_in_synpred262_Java5540);
        expression();

        state._fsp--;
        if (state.failed) return ;

        match(input,56,FOLLOW_56_in_synpred262_Java5542); if (state.failed) return ;

        }

    }
    // $ANTLR end synpred262_Java

    // Delegated rules

    public final boolean synpred157_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred157_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred211_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred211_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred249_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred249_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred243_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred243_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred5_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred5_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred229_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred229_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred178_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred178_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred215_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred215_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred113_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred113_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred151_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred151_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred117_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred117_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred162_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred162_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred217_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred217_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred186_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred186_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred188_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred188_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred212_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred212_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred163_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred163_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred152_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred152_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred242_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred242_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred199_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred199_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred216_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred216_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred236_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred236_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred262_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred262_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred198_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred198_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred233_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred233_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred180_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred180_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred128_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred128_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred200_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred200_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred234_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred234_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred182_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred182_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred181_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred181_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred237_Java() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred237_Java_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA123 dfa123 = new DFA123(this);
    protected DFA146 dfa146 = new DFA146(this);
    static final String DFA123_eotS =
        "\u0087\uffff";
    static final String DFA123_eofS =
        "\u0087\uffff";
    static final String DFA123_minS =
        "\1\6\4\17\22\uffff\5\17\1\6\1\17\1\6\1\47\30\uffff\1\70\1\47\1\uffff"+
        "\21\0\2\uffff\3\0\21\uffff\1\0\5\uffff\1\0\30\uffff\1\0\5\uffff";
    static final String DFA123_maxS =
        "\1\161\1\135\1\17\1\157\1\67\22\uffff\2\67\1\135\1\17\1\135\1\161"+
        "\1\143\1\161\1\67\30\uffff\1\70\1\67\1\uffff\21\0\2\uffff\3\0\21"+
        "\uffff\1\0\5\uffff\1\0\30\uffff\1\0\5\uffff";
    static final String DFA123_acceptS =
        "\5\uffff\1\2\166\uffff\1\1\12\uffff";
    static final String DFA123_specialS =
        "\73\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\1\16\1\17\1\20\2\uffff\1\21\1\22\1\23\21\uffff\1\24\5\uffff"+
        "\1\25\30\uffff\1\26\5\uffff}>";
    static final String[] DFA123_transitionS = {
            "\2\5\4\uffff\1\5\1\uffff\1\5\1\3\5\uffff\2\5\2\uffff\1\5\6\uffff"+
            "\1\5\3\uffff\2\5\2\uffff\2\5\6\uffff\1\5\5\uffff\1\2\5\uffff"+
            "\1\4\1\uffff\1\4\2\uffff\1\4\4\uffff\1\4\2\uffff\1\5\1\1\1\uffff"+
            "\1\4\5\uffff\1\4\1\uffff\1\4\1\uffff\2\5\5\uffff\1\4\2\uffff"+
            "\1\5\2\uffff\1\5\3\uffff\1\5\1\uffff\1\5\7\uffff\1\5",
            "\1\27\46\uffff\1\32\5\uffff\1\30\1\uffff\1\30\2\uffff\1\30"+
            "\4\uffff\1\30\3\uffff\1\31\1\uffff\1\30\5\uffff\1\30\1\uffff"+
            "\1\30\10\uffff\1\30",
            "\1\33",
            "\1\37\12\uffff\7\5\1\uffff\11\5\1\35\1\uffff\2\5\1\uffff\1"+
            "\5\1\34\4\5\1\uffff\1\36\1\uffff\2\5\26\uffff\1\5\33\uffff\3"+
            "\5",
            "\1\71\33\uffff\1\5\13\uffff\1\70",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\76\33\uffff\1\74\5\uffff\1\73\5\uffff\1\75",
            "\1\100\47\uffff\1\77",
            "\1\101\46\uffff\1\104\5\uffff\1\102\1\uffff\1\102\2\uffff\1"+
            "\102\4\uffff\1\102\3\uffff\1\103\1\uffff\1\102\5\uffff\1\102"+
            "\1\uffff\1\102\10\uffff\1\102",
            "\1\105",
            "\1\110\20\uffff\1\107\12\uffff\1\106\12\uffff\1\113\5\uffff"+
            "\1\111\1\uffff\1\111\2\uffff\1\111\4\uffff\1\111\3\uffff\1\112"+
            "\1\uffff\1\111\5\uffff\1\111\1\uffff\1\111\10\uffff\1\111",
            "\2\5\4\uffff\1\5\1\uffff\1\5\1\116\5\uffff\2\5\2\uffff\1\5"+
            "\6\uffff\1\5\3\uffff\2\5\2\uffff\2\5\7\uffff\2\5\2\uffff\1\120"+
            "\6\uffff\1\117\1\uffff\1\117\2\uffff\1\117\4\uffff\1\117\2\uffff"+
            "\1\5\2\uffff\1\117\5\uffff\1\117\1\uffff\1\117\1\uffff\2\5\5"+
            "\uffff\1\117\2\uffff\1\5\2\uffff\1\5\3\uffff\1\5\1\uffff\1\5"+
            "\7\uffff\1\5",
            "\1\142\41\uffff\1\5\20\uffff\1\5\23\uffff\1\5\11\uffff\1\5"+
            "\2\uffff\1\5",
            "\2\5\4\uffff\1\5\1\uffff\2\5\5\uffff\2\5\2\uffff\1\5\6\uffff"+
            "\1\5\3\uffff\2\5\2\uffff\2\5\16\uffff\1\150\3\uffff\1\5\1\uffff"+
            "\1\5\2\uffff\1\5\4\uffff\1\5\2\uffff\1\5\2\uffff\1\5\5\uffff"+
            "\1\5\1\uffff\1\5\1\uffff\2\5\5\uffff\1\5\2\uffff\1\5\2\uffff"+
            "\1\5\3\uffff\1\5\1\uffff\1\5\7\uffff\1\5",
            "\1\5\7\uffff\1\174\1\5\1\uffff\1\5\4\uffff\1\5",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0081",
            "\1\5\7\uffff\1\174\1\5\1\uffff\1\5\4\uffff\1\5",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA123_eot = DFA.unpackEncodedString(DFA123_eotS);
    static final short[] DFA123_eof = DFA.unpackEncodedString(DFA123_eofS);
    static final char[] DFA123_min = DFA.unpackEncodedStringToUnsignedChars(DFA123_minS);
    static final char[] DFA123_max = DFA.unpackEncodedStringToUnsignedChars(DFA123_maxS);
    static final short[] DFA123_accept = DFA.unpackEncodedString(DFA123_acceptS);
    static final short[] DFA123_special = DFA.unpackEncodedString(DFA123_specialS);
    static final short[][] DFA123_transition;

    static {
        int numStates = DFA123_transitionS.length;
        DFA123_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA123_transition[i] = DFA.unpackEncodedString(DFA123_transitionS[i]);
        }
    }

    class DFA123 extends DFA {

        public DFA123(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 123;
            this.eot = DFA123_eot;
            this.eof = DFA123_eof;
            this.min = DFA123_min;
            this.max = DFA123_max;
            this.accept = DFA123_accept;
            this.special = DFA123_special;
            this.transition = DFA123_transition;
        }
        public String getDescription() {
            return "724:1: forControl options {k=3; } : ( enhancedForControl | ( forInit )? ';' ( expression )? ';' ( forUpdate )? );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA123_59 = input.LA(1);

                         
                        int index123_59 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_59);

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA123_60 = input.LA(1);

                         
                        int index123_60 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_60);

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA123_61 = input.LA(1);

                         
                        int index123_61 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_61);

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA123_62 = input.LA(1);

                         
                        int index123_62 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_62);

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA123_63 = input.LA(1);

                         
                        int index123_63 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_63);

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA123_64 = input.LA(1);

                         
                        int index123_64 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_64);

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA123_65 = input.LA(1);

                         
                        int index123_65 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_65);

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA123_66 = input.LA(1);

                         
                        int index123_66 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_66);

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA123_67 = input.LA(1);

                         
                        int index123_67 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_67);

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA123_68 = input.LA(1);

                         
                        int index123_68 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_68);

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA123_69 = input.LA(1);

                         
                        int index123_69 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_69);

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA123_70 = input.LA(1);

                         
                        int index123_70 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_70);

                        if ( s>=0 ) return s;
                        break;

                    case 12 : 
                        int LA123_71 = input.LA(1);

                         
                        int index123_71 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_71);

                        if ( s>=0 ) return s;
                        break;

                    case 13 : 
                        int LA123_72 = input.LA(1);

                         
                        int index123_72 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_72);

                        if ( s>=0 ) return s;
                        break;

                    case 14 : 
                        int LA123_73 = input.LA(1);

                         
                        int index123_73 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_73);

                        if ( s>=0 ) return s;
                        break;

                    case 15 : 
                        int LA123_74 = input.LA(1);

                         
                        int index123_74 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_74);

                        if ( s>=0 ) return s;
                        break;

                    case 16 : 
                        int LA123_75 = input.LA(1);

                         
                        int index123_75 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_75);

                        if ( s>=0 ) return s;
                        break;

                    case 17 : 
                        int LA123_78 = input.LA(1);

                         
                        int index123_78 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_78);

                        if ( s>=0 ) return s;
                        break;

                    case 18 : 
                        int LA123_79 = input.LA(1);

                         
                        int index123_79 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_79);

                        if ( s>=0 ) return s;
                        break;

                    case 19 : 
                        int LA123_80 = input.LA(1);

                         
                        int index123_80 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_80);

                        if ( s>=0 ) return s;
                        break;

                    case 20 : 
                        int LA123_98 = input.LA(1);

                         
                        int index123_98 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_98);

                        if ( s>=0 ) return s;
                        break;

                    case 21 : 
                        int LA123_104 = input.LA(1);

                         
                        int index123_104 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_104);

                        if ( s>=0 ) return s;
                        break;

                    case 22 : 
                        int LA123_129 = input.LA(1);

                         
                        int index123_129 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred182_Java()) ) {s = 124;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index123_129);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 123, _s, input);
            error(nvae);
            throw nvae;
        }

    }
    static final String DFA146_eotS =
        "\7\uffff";
    static final String DFA146_eofS =
        "\7\uffff";
    static final String DFA146_minS =
        "\1\6\1\0\1\41\2\uffff\1\70\1\41";
    static final String DFA146_maxS =
        "\1\161\1\0\1\67\2\uffff\1\70\1\67";
    static final String DFA146_acceptS =
        "\3\uffff\1\2\1\1\2\uffff";
    static final String DFA146_specialS =
        "\1\uffff\1\0\5\uffff}>";
    static final String[] DFA146_transitionS = {
            "\2\3\4\uffff\1\3\1\uffff\1\3\1\1\5\uffff\2\3\2\uffff\1\3\6\uffff"+
            "\1\3\3\uffff\2\3\2\uffff\2\3\22\uffff\1\2\1\uffff\1\2\2\uffff"+
            "\1\2\4\uffff\1\2\2\uffff\1\3\2\uffff\1\2\5\uffff\1\2\1\uffff"+
            "\1\2\1\uffff\2\3\5\uffff\1\2\2\uffff\1\3\2\uffff\1\3\3\uffff"+
            "\1\3\1\uffff\1\3\7\uffff\1\3",
            "\1\uffff",
            "\1\4\11\uffff\1\3\13\uffff\1\5",
            "",
            "",
            "\1\6",
            "\1\4\11\uffff\1\3\13\uffff\1\5"
    };

    static final short[] DFA146_eot = DFA.unpackEncodedString(DFA146_eotS);
    static final short[] DFA146_eof = DFA.unpackEncodedString(DFA146_eofS);
    static final char[] DFA146_min = DFA.unpackEncodedStringToUnsignedChars(DFA146_minS);
    static final char[] DFA146_max = DFA.unpackEncodedStringToUnsignedChars(DFA146_maxS);
    static final short[] DFA146_accept = DFA.unpackEncodedString(DFA146_acceptS);
    static final short[] DFA146_special = DFA.unpackEncodedString(DFA146_specialS);
    static final short[][] DFA146_transition;

    static {
        int numStates = DFA146_transitionS.length;
        DFA146_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA146_transition[i] = DFA.unpackEncodedString(DFA146_transitionS[i]);
        }
    }

    class DFA146 extends DFA {

        public DFA146(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 146;
            this.eot = DFA146_eot;
            this.eof = DFA146_eof;
            this.min = DFA146_min;
            this.max = DFA146_max;
            this.accept = DFA146_accept;
            this.special = DFA146_special;
            this.transition = DFA146_transition;
        }
        public String getDescription() {
            return "959:12: ( type | expression )";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA146_1 = input.LA(1);

                         
                        int index146_1 = input.index();
                        input.rewind();

                        s = -1;
                        if ( (synpred234_Java()) ) {s = 4;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index146_1);

                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}

            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 146, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

    public static final BitSet FOLLOW_annotations_in_compilationUnit66 = new BitSet(new long[]{0x0840000000000100L,0x00000000CF080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit80 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit82 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit85 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_compilationUnit100 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit102 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit123 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit126 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit129 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_88_in_packageDeclaration149 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration151 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_packageDeclaration153 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_80_in_importDeclaration176 = new BitSet(new long[]{0x0000000000008000L,0x0000000040000000L});
    public static final BitSet FOLLOW_94_in_importDeclaration178 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_importDeclaration181 = new BitSet(new long[]{0x0001080000000000L});
    public static final BitSet FOLLOW_43_in_importDeclaration184 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_34_in_importDeclaration186 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_importDeclaration190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_typeDeclaration225 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifiers_in_classOrInterfaceDeclaration248 = new BitSet(new long[]{0x0040000000000100L,0x0000000000080004L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceModifier_in_classOrInterfaceModifiers279 = new BitSet(new long[]{0x0840000000000002L,0x00000000CE000400L});
    public static final BitSet FOLLOW_annotation_in_classOrInterfaceModifier299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_classOrInterfaceModifier312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_classOrInterfaceModifier327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_classOrInterfaceModifier339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_classOrInterfaceModifier353 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_classOrInterfaceModifier366 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_classOrInterfaceModifier381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_classOrInterfaceModifier397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_modifiers419 = new BitSet(new long[]{0x0840000000000002L,0x00000444CE200400L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_normalClassDeclaration472 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_normalClassDeclaration474 = new BitSet(new long[]{0x0002000000000000L,0x0000100000008100L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration476 = new BitSet(new long[]{0x0000000000000000L,0x0000100000008100L});
    public static final BitSet FOLLOW_72_in_normalClassDeclaration490 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration492 = new BitSet(new long[]{0x0000000000000000L,0x0000100000008000L});
    public static final BitSet FOLLOW_79_in_normalClassDeclaration505 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration507 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_typeParameters542 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters544 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_39_in_typeParameters547 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters549 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_52_in_typeParameters553 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_typeParameter572 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000100L});
    public static final BitSet FOLLOW_72_in_typeParameter575 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter577 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound606 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_typeBound609 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeBound611 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration632 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enumDeclaration634 = new BitSet(new long[]{0x0000000000000000L,0x0000100000008000L});
    public static final BitSet FOLLOW_79_in_enumDeclaration637 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration639 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_enumBody662 = new BitSet(new long[]{0x0041008000008000L,0x0001000000000000L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody664 = new BitSet(new long[]{0x0001008000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_enumBody667 = new BitSet(new long[]{0x0001000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody670 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_enumBody673 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants692 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_enumConstants695 = new BitSet(new long[]{0x0040000000008000L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants697 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_annotations_in_enumConstant722 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enumConstant725 = new BitSet(new long[]{0x0000000100000002L,0x0000100000000000L});
    public static final BitSet FOLLOW_arguments_in_enumConstant727 = new BitSet(new long[]{0x0000000000000002L,0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_enumConstant730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_enumBodyDeclarations754 = new BitSet(new long[]{0x5843000000008102L,0x00001644EE3C1446L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations757 = new BitSet(new long[]{0x5843000000008102L,0x00001644EE3C1446L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_83_in_normalInterfaceDeclaration815 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_normalInterfaceDeclaration817 = new BitSet(new long[]{0x0002000000000000L,0x0000100000000100L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration819 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000100L});
    public static final BitSet FOLLOW_72_in_normalInterfaceDeclaration823 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration825 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration829 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList852 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_typeList855 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeList857 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_108_in_classBody882 = new BitSet(new long[]{0x5843000000008100L,0x00011644EE3C1446L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody884 = new BitSet(new long[]{0x5843000000008100L,0x00011644EE3C1446L});
    public static final BitSet FOLLOW_112_in_classBody887 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_interfaceBody910 = new BitSet(new long[]{0x5843000000008100L,0x00010644EE3C1446L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody912 = new BitSet(new long[]{0x5843000000008100L,0x00010644EE3C1446L});
    public static final BitSet FOLLOW_112_in_interfaceBody915 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_classBodyDeclaration934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_classBodyDeclaration944 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_classBodyDeclaration947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_classBodyDeclaration957 = new BitSet(new long[]{0x5042000000008100L,0x00000200201C1046L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_genericMethodOrConstructorDecl_in_memberDecl982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDeclaration_in_memberDecl992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_memberDecl1002 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl1004 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_voidMethodDeclaratorRest_in_memberDecl1006 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_memberDecl1016 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_memberDecl1018 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl1028 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl1038 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_memberDeclaration1061 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDeclaration1064 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDeclaration1068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_genericMethodOrConstructorDecl1088 = new BitSet(new long[]{0x5000000000008000L,0x0000020020141042L});
    public static final BitSet FOLLOW_genericMethodOrConstructorRest_in_genericMethodOrConstructorDecl1090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_genericMethodOrConstructorRest1114 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_105_in_genericMethodOrConstructorRest1118 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1121 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_genericMethodOrConstructorRest1123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_genericMethodOrConstructorRest1133 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_constructorDeclaratorRest_in_genericMethodOrConstructorRest1135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_methodDeclaration1154 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_methodDeclaratorRest_in_methodDeclaration1156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_fieldDeclaration1175 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_fieldDeclaration1177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceBodyDeclaration1204 = new BitSet(new long[]{0x5042000000008100L,0x00000200201C1046L});
    public static final BitSet FOLLOW_interfaceMemberDecl_in_interfaceBodyDeclaration1206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_interfaceBodyDeclaration1216 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldDecl_in_interfaceMemberDecl1235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceGenericMethodDecl_in_interfaceMemberDecl1245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_interfaceMemberDecl1255 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMemberDecl1257 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_voidInterfaceMethodDeclaratorRest_in_interfaceMemberDecl1259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceMemberDecl1269 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceMemberDecl1279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_interfaceMethodOrFieldDecl1302 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceMethodOrFieldDecl1304 = new BitSet(new long[]{0x0084000100000000L});
    public static final BitSet FOLLOW_interfaceMethodOrFieldRest_in_interfaceMethodOrFieldDecl1306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorsRest_in_interfaceMethodOrFieldRest1329 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_interfaceMethodOrFieldRest1331 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceMethodOrFieldRest1341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaratorRest1364 = new BitSet(new long[]{0x0081000000000000L,0x0000102000000000L});
    public static final BitSet FOLLOW_55_in_methodDeclaratorRest1367 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_methodDeclaratorRest1369 = new BitSet(new long[]{0x0081000000000000L,0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_methodDeclaratorRest1382 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaratorRest1384 = new BitSet(new long[]{0x0001000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_methodBody_in_methodDeclaratorRest1400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_methodDeclaratorRest1414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidMethodDeclaratorRest1447 = new BitSet(new long[]{0x0001000000000000L,0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_voidMethodDeclaratorRest1450 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidMethodDeclaratorRest1452 = new BitSet(new long[]{0x0001000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_methodBody_in_voidMethodDeclaratorRest1468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_voidMethodDeclaratorRest1482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaratorRest1515 = new BitSet(new long[]{0x0081000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_55_in_interfaceMethodDeclaratorRest1518 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_interfaceMethodDeclaratorRest1520 = new BitSet(new long[]{0x0081000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_interfaceMethodDeclaratorRest1525 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaratorRest1527 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_interfaceMethodDeclaratorRest1531 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceGenericMethodDecl1554 = new BitSet(new long[]{0x5000000000008000L,0x0000020020141042L});
    public static final BitSet FOLLOW_type_in_interfaceGenericMethodDecl1557 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_105_in_interfaceGenericMethodDecl1561 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_interfaceGenericMethodDecl1564 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_interfaceMethodDeclaratorRest_in_interfaceGenericMethodDecl1574 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_voidInterfaceMethodDeclaratorRest1597 = new BitSet(new long[]{0x0001000000000000L,0x0000002000000000L});
    public static final BitSet FOLLOW_101_in_voidInterfaceMethodDeclaratorRest1600 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_voidInterfaceMethodDeclaratorRest1602 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_voidInterfaceMethodDeclaratorRest1606 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_formalParameters_in_constructorDeclaratorRest1629 = new BitSet(new long[]{0x0000000000000000L,0x0000102000000000L});
    public static final BitSet FOLLOW_101_in_constructorDeclaratorRest1632 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedNameList_in_constructorDeclaratorRest1634 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_constructorBody_in_constructorDeclaratorRest1638 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_constantDeclarator1657 = new BitSet(new long[]{0x0084000000000000L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclarator1659 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1682 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_variableDeclarators1685 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclarators1687 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_variableDeclarator1708 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_50_in_variableDeclarator1711 = new BitSet(new long[]{0x500003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator1713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_constantDeclaratorRest_in_constantDeclaratorsRest1739 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_constantDeclaratorsRest1742 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_constantDeclarator_in_constantDeclaratorsRest1744 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_55_in_constantDeclaratorRest1766 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_constantDeclaratorRest1768 = new BitSet(new long[]{0x0084000000000000L});
    public static final BitSet FOLLOW_50_in_constantDeclaratorRest1772 = new BitSet(new long[]{0x500003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_constantDeclaratorRest1774 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_variableDeclaratorId1797 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_variableDeclaratorId1800 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_variableDeclaratorId1802 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer1823 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer1833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_arrayInitializer1862 = new BitSet(new long[]{0x500003310260D0C0L,0x0003128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1865 = new BitSet(new long[]{0x0000008000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_arrayInitializer1868 = new BitSet(new long[]{0x500003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1870 = new BitSet(new long[]{0x0000008000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_arrayInitializer1875 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_arrayInitializer1882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifier1901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_modifier1911 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_90_in_modifier1921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_89_in_modifier1931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_94_in_modifier1941 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_modifier1951 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_modifier1961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_85_in_modifier1971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_modifier1981 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_102_in_modifier1991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_106_in_modifier2001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_95_in_modifier2011 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_packageOrTypeName2030 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_enumConstantName2049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_typeName2068 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type2082 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_type2085 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_type2087 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_type2094 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_type2097 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_type2099 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2112 = new BitSet(new long[]{0x0002080000000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2114 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_classOrInterfaceType2118 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_classOrInterfaceType2120 = new BitSet(new long[]{0x0002080000000002L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2122 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_74_in_variableModifier2231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_variableModifier2241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_typeArguments2260 = new BitSet(new long[]{0x5020000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2262 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_39_in_typeArguments2265 = new BitSet(new long[]{0x5020000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2267 = new BitSet(new long[]{0x0010008000000000L});
    public static final BitSet FOLLOW_52_in_typeArguments2271 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument2294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_53_in_typeArgument2304 = new BitSet(new long[]{0x0000000000000002L,0x0000000100000100L});
    public static final BitSet FOLLOW_set_in_typeArgument2307 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_typeArgument2315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2340 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_qualifiedNameList2343 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2345 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_32_in_formalParameters2366 = new BitSet(new long[]{0x5040000200008000L,0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters2368 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_formalParameters2371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameterDecls2394 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_formalParameterDecls2396 = new BitSet(new long[]{0x0000100000008000L});
    public static final BitSet FOLLOW_formalParameterDeclsRest_in_formalParameterDecls2398 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2421 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_formalParameterDeclsRest2424 = new BitSet(new long[]{0x5040000000008000L,0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameterDeclsRest2426 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_formalParameterDeclsRest2438 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterDeclsRest2440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_block_in_methodBody2463 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_constructorBody2482 = new BitSet(new long[]{0x784303310260D1D0L,0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_constructorBody2484 = new BitSet(new long[]{0x784103310260D1D0L,0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_blockStatement_in_constructorBody2487 = new BitSet(new long[]{0x784103310260D1D0L,0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_112_in_constructorBody2490 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2509 = new BitSet(new long[]{0x0000000000000000L,0x0000000900000000L});
    public static final BitSet FOLLOW_set_in_explicitConstructorInvocation2512 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2520 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_explicitConstructorInvocation2522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation2532 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_explicitConstructorInvocation2534 = new BitSet(new long[]{0x0002000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2536 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_explicitConstructorInvocation2539 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation2541 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_explicitConstructorInvocation2543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2563 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_qualifiedName2566 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_qualifiedName2568 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_integerLiteral_in_literal2594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FloatingPointLiteral_in_literal2604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CharacterLiteral_in_literal2614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_StringLiteral_in_literal2624 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanLiteral_in_literal2634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_87_in_literal2644 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_annotations2733 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_annotation2753 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_annotationName_in_annotation2755 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_32_in_annotation2759 = new BitSet(new long[]{0x504003330260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation2763 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_elementValue_in_annotation2767 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_annotation2772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2796 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_43_in_annotationName2799 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_annotationName2801 = new BitSet(new long[]{0x0000080000000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2822 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_elementValuePairs2825 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs2827 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_Identifier_in_elementValuePair2848 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_elementValuePair2850 = new BitSet(new long[]{0x504003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair2852 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue2875 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue2885 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue2895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_elementValueArrayInitializer2918 = new BitSet(new long[]{0x504003B10260D0C0L,0x0003128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2921 = new BitSet(new long[]{0x0000008000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_elementValueArrayInitializer2924 = new BitSet(new long[]{0x504003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer2926 = new BitSet(new long[]{0x0000008000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_39_in_elementValueArrayInitializer2933 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_elementValueArrayInitializer2937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_annotationTypeDeclaration2960 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_83_in_annotationTypeDeclaration2962 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_annotationTypeDeclaration2964 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration2966 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_annotationTypeBody2989 = new BitSet(new long[]{0x5840000000008100L,0x00010444EE3C1446L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody2992 = new BitSet(new long[]{0x5840000000008100L,0x00010444EE3C1446L});
    public static final BitSet FOLLOW_112_in_annotationTypeBody2996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeElementDeclaration3019 = new BitSet(new long[]{0x5040000000008100L,0x00000000201C1046L});
    public static final BitSet FOLLOW_annotationTypeElementRest_in_annotationTypeElementDeclaration3021 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_annotationTypeElementRest3044 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_annotationMethodOrConstantRest_in_annotationTypeElementRest3046 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementRest3058 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementRest3071 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementRest3084 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3086 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementRest3097 = new BitSet(new long[]{0x0001000000000002L});
    public static final BitSet FOLLOW_48_in_annotationTypeElementRest3099 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodRest_in_annotationMethodOrConstantRest3123 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationConstantRest_in_annotationMethodOrConstantRest3133 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_annotationMethodRest3156 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_annotationMethodRest3158 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_annotationMethodRest3160 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_defaultValue_in_annotationMethodRest3162 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableDeclarators_in_annotationConstantRest3186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_defaultValue3209 = new BitSet(new long[]{0x504003310260D0C0L,0x0002128920D41242L});
    public static final BitSet FOLLOW_elementValue_in_defaultValue3211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_108_in_block3232 = new BitSet(new long[]{0x784103310260D1D0L,0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_blockStatement_in_block3234 = new BitSet(new long[]{0x784103310260D1D0L,0x00031B9FFEDC766EL});
    public static final BitSet FOLLOW_112_in_block3237 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement3260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement3272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement3282 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3306 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_localVariableDeclarationStatement3309 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration3328 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration3330 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclarators_in_localVariableDeclaration3332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifier_in_variableModifiers3363 = new BitSet(new long[]{0x0040000000000002L,0x0000000000000400L});
    public static final BitSet FOLLOW_block_in_statement3381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement3391 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3393 = new BitSet(new long[]{0x0001800000000000L});
    public static final BitSet FOLLOW_47_in_statement3396 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3398 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_78_in_statement3412 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3414 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3416 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000080L});
    public static final BitSet FOLLOW_71_in_statement3426 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_statement3440 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_statement3442 = new BitSet(new long[]{0x504103310260D0C0L,0x0002028920D41642L});
    public static final BitSet FOLLOW_forControl_in_statement3444 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_statement3446 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3448 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_107_in_statement3458 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3460 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3462 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_statement3472 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3474 = new BitSet(new long[]{0x0000000000000000L,0x0000080000000000L});
    public static final BitSet FOLLOW_107_in_statement3476 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3478 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3480 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_104_in_statement3490 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3492 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000801L});
    public static final BitSet FOLLOW_catches_in_statement3504 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_statement3506 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_statement3520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_statement3534 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3536 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_97_in_statement3556 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3558 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_108_in_statement3560 = new BitSet(new long[]{0x8000000000000000L,0x0001000000000010L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement3562 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_112_in_statement3564 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_98_in_statement3574 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_parExpression_in_statement3576 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_statement3578 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_statement3588 = new BitSet(new long[]{0x500103310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3590 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3593 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_100_in_statement3603 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_statement3605 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3607 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_61_in_statement3617 = new BitSet(new long[]{0x0001000000008000L});
    public static final BitSet FOLLOW_Identifier_in_statement3619 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_statement3632 = new BitSet(new long[]{0x0001000000008000L});
    public static final BitSet FOLLOW_Identifier_in_statement3634 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_statement3647 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statementExpression_in_statement3658 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_statement3660 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_statement3670 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_statement3672 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_statement3674 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches3697 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_catchClause_in_catches3700 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_catchClause3725 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_32_in_catchClause3727 = new BitSet(new long[]{0x5040000000008000L,0x0000000020141442L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause3729 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_catchClause3731 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_catchClause3733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter3752 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_formalParameter3754 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameter3756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups3784 = new BitSet(new long[]{0x8000000000000002L,0x0000000000000010L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup3811 = new BitSet(new long[]{0xF84103310260D1D2L,0x00021B9FFEDC767EL});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup3814 = new BitSet(new long[]{0x784103310260D1D2L,0x00021B9FFEDC766EL});
    public static final BitSet FOLLOW_63_in_switchLabel3838 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_constantExpression_in_switchLabel3840 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_switchLabel3852 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_enumConstantName_in_switchLabel3854 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_switchLabel3866 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_switchLabel3868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_forControl3899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forInit_in_forControl3909 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_forControl3912 = new BitSet(new long[]{0x500103310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_forControl3914 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_forControl3917 = new BitSet(new long[]{0x500003310260D0C2L,0x0002028920D41242L});
    public static final BitSet FOLLOW_forUpdate_in_forControl3919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit3939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit3949 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_enhancedForControl3972 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_enhancedForControl3974 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_enhancedForControl3976 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_enhancedForControl3978 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_enhancedForControl3980 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forUpdate3999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_parExpression4020 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_parExpression4022 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_parExpression4024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList4047 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_39_in_expressionList4050 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_expressionList4052 = new BitSet(new long[]{0x0000008000000002L});
    public static final BitSet FOLLOW_expression_in_statementExpression4073 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_constantExpression4096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression4119 = new BitSet(new long[]{0x0416444890000002L,0x0000400000000000L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression4122 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_expression4124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_38_in_assignmentOperator4160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_42_in_assignmentOperator4170 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_35_in_assignmentOperator4180 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_46_in_assignmentOperator4190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_assignmentOperator4200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_110_in_assignmentOperator4210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_assignmentOperator4220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_assignmentOperator4230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_assignmentOperator4251 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_assignmentOperator4255 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4293 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4297 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4301 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4305 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4336 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_assignmentOperator4340 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_assignmentOperator4344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression4373 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_conditionalExpression4377 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression4379 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_conditionalExpression4381 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression4383 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4421 = new BitSet(new long[]{0x0000000000000002L,0x0000800000000000L});
    public static final BitSet FOLLOW_111_in_conditionalOrExpression4425 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression4427 = new BitSet(new long[]{0x0000000000000002L,0x0000800000000000L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4449 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_29_in_conditionalAndExpression4453 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression4455 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4477 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L});
    public static final BitSet FOLLOW_109_in_inclusiveOrExpression4481 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression4483 = new BitSet(new long[]{0x0000000000000002L,0x0000200000000000L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4505 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_57_in_exclusiveOrExpression4509 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression4511 = new BitSet(new long[]{0x0200000000000002L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4533 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_30_in_andExpression4537 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression4539 = new BitSet(new long[]{0x0000000040000002L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4561 = new BitSet(new long[]{0x0008000004000002L});
    public static final BitSet FOLLOW_set_in_equalityExpression4565 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression4573 = new BitSet(new long[]{0x0008000004000002L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression4595 = new BitSet(new long[]{0x0000000000000002L,0x0000000000020000L});
    public static final BitSet FOLLOW_81_in_instanceOfExpression4598 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression4600 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4621 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression4625 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression4627 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_49_in_relationalOp4662 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_relationalOp4666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_relationalOp4696 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_relationalOp4700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_relationalOp4721 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_relationalOp4732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4752 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression4756 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression4758 = new BitSet(new long[]{0x0012000000000002L});
    public static final BitSet FOLLOW_49_in_shiftOp4789 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_shiftOp4793 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_shiftOp4825 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4829 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4833 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_shiftOp4863 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_shiftOp4867 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4897 = new BitSet(new long[]{0x0000011000000002L});
    public static final BitSet FOLLOW_set_in_additiveExpression4901 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression4909 = new BitSet(new long[]{0x0000011000000002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4931 = new BitSet(new long[]{0x0000200408000002L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression4935 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression4949 = new BitSet(new long[]{0x0000200408000002L});
    public static final BitSet FOLLOW_36_in_unaryExpression4975 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4977 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_unaryExpression4987 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression4989 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_unaryExpression4999 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_41_in_unaryExpression5011 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression5013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression5023 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_113_in_unaryExpressionNotPlusMinus5042 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5044 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_unaryExpressionNotPlusMinus5054 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus5056 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus5066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus5076 = new BitSet(new long[]{0x00800A2000000002L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus5078 = new BitSet(new long[]{0x00800A2000000002L});
    public static final BitSet FOLLOW_32_in_castExpression5104 = new BitSet(new long[]{0x5000000000000000L,0x0000000020141042L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression5106 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_castExpression5108 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression5110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_castExpression5119 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_type_in_castExpression5122 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_expression_in_castExpression5126 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_castExpression5129 = new BitSet(new long[]{0x500000010260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression5131 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary5150 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_99_in_primary5160 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_43_in_primary5163 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_primary5165 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_96_in_primary5180 = new BitSet(new long[]{0x0000080100000000L});
    public static final BitSet FOLLOW_superSuffix_in_primary5182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary5192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_86_in_primary5202 = new BitSet(new long[]{0x5002000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_creator_in_primary5204 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_primary5214 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_43_in_primary5217 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_primary5219 = new BitSet(new long[]{0x0080080100000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary5223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary5234 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_55_in_primary5237 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_primary5239 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_43_in_primary5243 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_primary5245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_105_in_primary5255 = new BitSet(new long[]{0x0000080000000000L});
    public static final BitSet FOLLOW_43_in_primary5257 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_primary5259 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_identifierSuffix5279 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_identifierSuffix5281 = new BitSet(new long[]{0x0080080000000000L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5285 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_identifierSuffix5287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_identifierSuffix5298 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix5300 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_identifierSuffix5302 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5315 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5325 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_identifierSuffix5327 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5337 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_explicitGenericInvocation_in_identifierSuffix5339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5349 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_identifierSuffix5351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5361 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_identifierSuffix5363 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix5365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_identifierSuffix5375 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_identifierSuffix5377 = new BitSet(new long[]{0x0002000000008000L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix5379 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator5398 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_createdName_in_creator5400 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_createdName_in_creator5412 = new BitSet(new long[]{0x0080000100000000L});
    public static final BitSet FOLLOW_arrayCreatorRest_in_creator5415 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator5419 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName5439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName5449 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator5472 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_innerCreator5475 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator5477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5496 = new BitSet(new long[]{0x510003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5510 = new BitSet(new long[]{0x0080000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5513 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5515 = new BitSet(new long[]{0x0080000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreatorRest5519 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5533 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5535 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5538 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_arrayCreatorRest5540 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5542 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_arrayCreatorRest5547 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_arrayCreatorRest5549 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest5580 = new BitSet(new long[]{0x0000000000000002L,0x0000100000000000L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest5582 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitGenericInvocation5606 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_explicitGenericInvocation5608 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_explicitGenericInvocation5610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_nonWildcardTypeArguments5633 = new BitSet(new long[]{0x5000000000008000L,0x0000000020141042L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments5635 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_nonWildcardTypeArguments5637 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5660 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_selector5662 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_arguments_in_selector5664 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5675 = new BitSet(new long[]{0x0000000000000000L,0x0000000800000000L});
    public static final BitSet FOLLOW_99_in_selector5677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5687 = new BitSet(new long[]{0x0000000000000000L,0x0000000100000000L});
    public static final BitSet FOLLOW_96_in_selector5689 = new BitSet(new long[]{0x0000080100000000L});
    public static final BitSet FOLLOW_superSuffix_in_selector5691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_selector5701 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_86_in_selector5703 = new BitSet(new long[]{0x0002000000008000L});
    public static final BitSet FOLLOW_innerCreator_in_selector5705 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_selector5715 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_selector5717 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_selector5719 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_superSuffix5752 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_superSuffix5754 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix5756 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_arguments5776 = new BitSet(new long[]{0x500003330260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expressionList_in_arguments5778 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_arguments5781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred5_Java66 = new BitSet(new long[]{0x0840000000000100L,0x00000000CF080404L});
    public static final BitSet FOLLOW_packageDeclaration_in_synpred5_Java80 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_importDeclaration_in_synpred5_Java82 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE090404L});
    public static final BitSet FOLLOW_typeDeclaration_in_synpred5_Java85 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred5_Java100 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_typeDeclaration_in_synpred5_Java102 = new BitSet(new long[]{0x0841000000000102L,0x00000000CE080404L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred113_Java2484 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred117_Java2509 = new BitSet(new long[]{0x0000000000000000L,0x0000000900000000L});
    public static final BitSet FOLLOW_set_in_synpred117_Java2512 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_arguments_in_synpred117_Java2520 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_synpred117_Java2522 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_synpred128_Java2733 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred151_Java3260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred152_Java3272 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_71_in_synpred157_Java3426 = new BitSet(new long[]{0x700103310260D0D0L,0x00021B9F30D4726AL});
    public static final BitSet FOLLOW_statement_in_synpred157_Java3428 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred162_Java3504 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_75_in_synpred162_Java3506 = new BitSet(new long[]{0x0000000000000000L,0x0000100000000000L});
    public static final BitSet FOLLOW_block_in_synpred162_Java3508 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred163_Java3520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchLabel_in_synpred178_Java3811 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_synpred180_Java3838 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_constantExpression_in_synpred180_Java3840 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_synpred180_Java3842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_synpred181_Java3852 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_enumConstantName_in_synpred181_Java3854 = new BitSet(new long[]{0x0000800000000000L});
    public static final BitSet FOLLOW_47_in_synpred181_Java3856 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enhancedForControl_in_synpred182_Java3899 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred186_Java3939 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_assignmentOperator_in_synpred188_Java4122 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_synpred188_Java4124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred198_Java4241 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred198_Java4243 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred198_Java4245 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4281 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4283 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred199_Java4285 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred199_Java4287 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred200_Java4326 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred200_Java4328 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred200_Java4330 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred211_Java4654 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred211_Java4656 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred212_Java4688 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_50_in_synpred212_Java4690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_49_in_synpred215_Java4781 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_49_in_synpred215_Java4783 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4815 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4817 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred216_Java4819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_52_in_synpred217_Java4855 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_52_in_synpred217_Java4857 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred229_Java5066 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_synpred233_Java5104 = new BitSet(new long[]{0x5000000000000000L,0x0000000020141042L});
    public static final BitSet FOLLOW_primitiveType_in_synpred233_Java5106 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_synpred233_Java5108 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred233_Java5110 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_synpred234_Java5122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_synpred236_Java5163 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_synpred236_Java5165 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred237_Java5169 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_synpred242_Java5217 = new BitSet(new long[]{0x0000000000008000L});
    public static final BitSet FOLLOW_Identifier_in_synpred242_Java5219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred243_Java5223 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred249_Java5298 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_synpred249_Java5300 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_synpred249_Java5302 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_55_in_synpred262_Java5538 = new BitSet(new long[]{0x500003310260D0C0L,0x0002028920D41242L});
    public static final BitSet FOLLOW_expression_in_synpred262_Java5540 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_56_in_synpred262_Java5542 = new BitSet(new long[]{0x0000000000000002L});

}