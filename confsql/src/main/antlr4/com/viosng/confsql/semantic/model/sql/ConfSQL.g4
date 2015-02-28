grammar ConfSQL;

stat : query EOF;

/*
===============================================================================
  Queries
===============================================================================
*/

query : ('select'|'SELECT') select_list table_expression?;

select_list : select_item (COMMA select_item)* ;
select_item
     : asterisk as_clause?                                                                       #all
     | expr as_clause?                                                                           #selectExpr
     ;

asterisk : (StringLiteral DOT)? MULTIPLY;

table_expression : from_clause where_clause? groupby_clause? having_clause? orderby_clause? limit_clause? ;

/*
===============================================================================
  From Clause
===============================================================================
*/

from_clause : ('FROM'|'from') paranthesized_param_list? table_reference_list;

table_reference_list :table_reference (COMMA table_reference)*;

table_reference : joined_table | table_primary;

joined_table : table_primary joined_table_primary+;

joined_table_primary : join_type? ('JOIN'|'join') paranthesized_param_list? table_primary join_condition?;

join_type
     : ('INNER' | 'inner')
     | ('FUZZY' | 'fuzzy')
     | ('LEFT' | 'left')
     | ('RIGHT' | 'right')
     | ('FULL' | 'full')
     ;

join_condition : ('ON'|'on') expr ;

table_primary
     : table_or_query_name as_clause? paranthesized_column_name_list?                #fromSource
     | LEFT_PAREN query RIGHT_PAREN as_clause paranthesized_column_name_list?        #fromSubQuery
     ;

table_or_query_name : StringLiteral | Field;

paranthesized_column_name_list : LEFT_PAREN column_name_list RIGHT_PAREN;
column_name_list : StringLiteral (COMMA StringLiteral)*;

/*
===============================================================================
  Where Clause
===============================================================================
*/

where_clause : ('where'|'WHERE') expr;

/*
===============================================================================
  Group by Clause
===============================================================================
*/

groupby_clause : ('group'|'GROUP') paranthesized_param_list? ('by'|'BY') expr_list;

/*
===============================================================================
  Having Clause
===============================================================================
*/

having_clause : ('having'|'HAVING') expr;

/*
===============================================================================
  Order by Clause
===============================================================================
*/

orderby_clause : ('order'|'ORDER') paranthesized_param_list? ('by'|'BY') expr_list Order_Type?;
Order_Type : 'ASC' | 'asc' | 'desc' | 'DESC';

/*
===============================================================================
  Limit Clause
===============================================================================
*/

limit_clause : ('limit'|'LIMIT') expr;

/*
===============================================================================
  Expressions
===============================================================================
*/

expr : BIT_NEG expr                                                                              #bitNeg
     | MINUS expr                                                                                #neg
     | <assoc=right> expr POWER expr                                                             #power
     | expr (MULTIPLY|DIVIDE|MODULAR) expr                                                       #arithmFirst
     | expr (PLUS|MINUS|BIT_AND|VERTICAL_BAR|BIT_XOR) expr                                       #arithmSecond
     | expr CONCATENATION_OPERATOR expr                                                          #concatination
     | ('NOT'|'not') expr                                                                        #not
     | expr (EQUAL|GTH|LTH|GEQ|LEQ|NOT_EQUAL) expr                                               #comparing
     | expr ('IS'|'is') expr                                                                     #is
     | expr ('AND'|'and') expr                                                                   #and
     | expr ('OR'|'or') expr                                                                     #or
     | ('CAST'|'cast') expr ('as'|'AS') StringLiteral                                            #cast
     | ('CASE'|'case') expr? case_when_clause+ case_else_clause? ('END'|'end')                   #case
     | query                                                                                     #subQueryExpr
     | (NumberLiteral | String)                                                                  #constant
     | Field                                                                                     #fieldExpr
     | StringLiteral (LEFT_PAREN exprs_and_params RIGHT_PAREN)?                                  #columnOrFunctionCall
     | LEFT_PAREN expr RIGHT_PAREN                                                               #brackets
     ;

as_clause : ('as'|'AS')? StringLiteral;

exprs_and_params
     : (expr_list SEMI_COLON)? param_list
     | expr_list?
     ;

paranthesized_param_list : LEFT_PAREN param_list RIGHT_PAREN;
param_list : param (COMMA param)*;
param : name=(String | StringLiteral) EQUAL expr;

expr_list : expr (COMMA expr)*;
case_when_clause : ('WHEN' | 'when') w=expr ('THEN' | 'then') t=expr;
case_else_clause : ('ELSE' | 'else') expr;

/*
===============================================================================
  Literals
===============================================================================
*/

NumberLiteral : Real
              | Int
              | Bool_Constant
              ;

Bool_Constant : 'FALSE' | 'false' | 'TRUE' | 'true' | 'NULL' | 'null' ;
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
