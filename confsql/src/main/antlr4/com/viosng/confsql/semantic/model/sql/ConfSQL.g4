grammar ConfSQL;

stat : query EOF;

/*
===============================================================================
  Queries
===============================================================================
*/

query : ('select'|'SELECT') selectList tableExpression?;

selectList : selectItem (COMMA selectItem)* ;
selectItem
     : asterisk asClause?                                                                       #all
     | expr asClause?                                                                           #selectExpr
     ;

asterisk : (StringLiteral DOT)? MULTIPLY;

tableExpression : fromClause whereClause? groupByClause? havingClause? orderByClause? limitClause? ;

/*
===============================================================================
  From Clause
===============================================================================
*/

fromClause : ('FROM'|'from') paranthesizedParamList? tableReferenceList;

tableReferenceList :tableReference (COMMA tableReference)*;

tableReference : tablePrimary joinedTablePrimary*;

joinedTablePrimary : JoinType? ('JOIN'|'join') paranthesizedParamList? tablePrimary (('ON'|'on') expr)?;

JoinType
     : ('INNER' | 'inner')
     | ('FUZZY' | 'fuzzy')
     | ('LEFT' | 'left')
     | ('RIGHT' | 'right')
     | ('FULL' | 'full')
     ;

tablePrimary
     : tableOrQueryName asClause? paranthesizedColumnNameList?                #fromSource
     | LEFT_PAREN query RIGHT_PAREN asClause paranthesizedColumnNameList?     #fromSubQuery
     ;

tableOrQueryName : StringLiteral | Field;

paranthesizedColumnNameList : LEFT_PAREN columnNameList RIGHT_PAREN;
columnNameList : StringLiteral (COMMA StringLiteral)*;

/*
===============================================================================
  Where Clause
===============================================================================
*/

whereClause : ('where'|'WHERE') expr;

/*
===============================================================================
  Group by Clause
===============================================================================
*/

groupByClause : ('group'|'GROUP') paranthesizedParamList? ('by'|'BY') exprList;

/*
===============================================================================
  Having Clause
===============================================================================
*/

havingClause : ('having'|'HAVING') expr;

/*
===============================================================================
  Order by Clause
===============================================================================
*/

orderByClause : ('order'|'ORDER') paranthesizedParamList? ('by'|'BY') exprList OrderType?;
OrderType : 'ASC' | 'asc' | 'desc' | 'DESC';

/*
===============================================================================
  Limit Clause
===============================================================================
*/

limitClause : ('limit'|'LIMIT') expr;

/*
===============================================================================
  Expressions
===============================================================================
*/

expr : BIT_NEG expr                                                                              #bitNeg
     | MINUS expr                                                                                #neg
     | <assoc=right> expr POWER expr                                                             #power
     | expr op=(MULTIPLY|DIVIDE|MODULAR) expr                                                    #arithmFirst
     | expr op=(PLUS|MINUS|BIT_AND|VERTICAL_BAR|BIT_XOR) expr                                    #arithmSecond
     | expr CONCATENATION_OPERATOR expr                                                          #concatenation
     | ('NOT'|'not') expr                                                                        #not
     | expr comp=(EQUAL|GTH|LTH|GEQ|LEQ|NOT_EQUAL) expr                                          #comparing
     | expr ('IS'|'is') expr                                                                     #is
     | expr ('AND'|'and') expr                                                                   #and
     | expr ('OR'|'or') expr                                                                     #or
     | ('CAST'|'cast') expr ('as'|'AS') StringLiteral                                            #cast
     | ('CASE'|'case') expr? caseWhenClause+ caseElseClause? ('END'|'end')                       #case
     | LEFT_PAREN query RIGHT_PAREN                                                              #subQueryExpr
     | (NumberLiteral | String)                                                                  #constant
     | Field                                                                                     #fieldExpr
     | StringLiteral (LEFT_PAREN exprsAndParams RIGHT_PAREN)?                                    #columnOrFunctionCall
     | LEFT_PAREN expr RIGHT_PAREN                                                               #brackets
     ;

asClause : ('as'|'AS')? StringLiteral;

exprsAndParams
     : (exprList SEMI_COLON)? paramList
     | exprList?
     ;

paranthesizedParamList : LEFT_PAREN paramList RIGHT_PAREN;
paramList : param (COMMA param)*;
param : name=(String | StringLiteral) EQUAL expr;

exprList : expr (COMMA expr)*;
caseWhenClause : ('WHEN' | 'when') w=expr ('THEN' | 'then') t=expr;
caseElseClause : ('ELSE' | 'else') expr;

/*
===============================================================================
  Literals
===============================================================================
*/

NumberLiteral : Real
              | Int
              | BoolConstant
              ;

BoolConstant : 'FALSE' | 'false' | 'TRUE' | 'true' | 'NULL' | 'null' ;
Real : (RealWithoutExp | Int) ([eE] Sign? Int)* ;
RealWithoutExp : ('0' | (Int+)) '.' Digit*
               | '.' Digit+
               ;

Int : (NonZeroDigit+ Digit*) | Digit ;

Field : StringLiteral (DOT StringLiteral)+;
StringLiteral: [a-zA-Z] [a-zA-Z0-9_]*;
String: '"' (Esc|.)*? '"' ;

LineComment : MinusSign MinusSign .*? '\r'? '\n' -> skip ;

ASSIGN  : ':=';
EQUAL  : '=';
COLON :  ':';
SEMI_COLON :  ';';
COMMA : ',';
CONCATENATION_OPERATOR : VERTICAL_BAR VERTICAL_BAR;
NOT_EQUAL  : '<>' | '!=';
LTH : '<' ;
LEQ : '<=';
GTH   : '>';
GEQ   : '>=';
LEFT_PAREN :  '(';
RIGHT_PAREN : ')';
PLUS  : '+';
MINUS : '-';
MULTIPLY: '*';
POWER : '**';
DIVIDE  : '/';
MODULAR : '%';
DOT : '.';
UNDERLINE : '_';
BIT_AND : '&';
BIT_XOR : '^';
BIT_NEG : '~';
VERTICAL_BAR : '|';
QUOTE : '\'';
DOUBLE_QUOTE : '"';

Digit : [0-9] ;
fragment Sign : [+-] ;
fragment MinusSign : '-' ;
fragment NonZeroDigit : [1-9] ;
fragment Esc : '\\"' | '\\\\' ; // 2-char sequences \" and \\


WS : [\t \r\n] -> skip;
