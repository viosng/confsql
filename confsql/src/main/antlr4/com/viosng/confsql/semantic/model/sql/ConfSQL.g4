grammar ConfSQL;

stat : query EOF;

query : expr;

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
     | expr ('LIKE'|'like') String                                                               #like
     | expr ('BETWEEN'|'between') expr ('AND'|'and') expr                                        #between
     | expr ('ALL'|'all'|'ANY'|'any'|'IN'|'in') LEFT_PAREN (query | expr_list) RIGHT_PAREN       #subQuery
     | ('EXISTS'|'exists') LEFT_PAREN query RIGHT_PAREN                                          #exists
     | ('CAST'|'cast') expr ('as'|'AS') StringLiteral                                            #cast
     | ('CASE'|'case') expr? case_when_clause+ case_else_clause ('END'|'end')                    #case
     | Field                                                                                     #field
     | StringLiteral (LEFT_PAREN expr_list? RIGHT_PAREN)?                                        #columnOrFunctionCall
     | LEFT_PAREN expr RIGHT_PAREN                                                               #brackets
     | (NumberLiteral | String)                                                                  #constant
     ;

expr_list : expr (',' expr)*;
case_when_clause : ('WHEN' | 'when') w=expr ('THEN' | 'then') t=expr;
case_else_clause : ('ELSE' | 'else') e=expr;

Field : StringLiteral ('.' StringLiteral)+;
StringLiteral: [a-zA-z]+? [a-zA-z0-9_]*;
String: '"' (Esc|.)*? '"' ;


NumberLiteral : Float
              | Int
              | Bool_Constant
              ;
              
Bool_Constant : TRUE | FALSE | NULL ;
Float : (FloatWithoutExp | Int) ([eE] Sign? Int )* ;

FloatWithoutExp : Int+ FloatAfterDot
                | '0' '.' Digit*
                | '.' Digit+
                ;

FloatAfterDot : '.' Digit* ;

Int : NonZeroDigit+ Digit* ;


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

fragment Sign : [+-] ;
fragment MinusSign : '-' ;
fragment NonZeroDigit : [1-9] ;
fragment Digit : [0-9] ;
fragment Esc : '\\"' | '\\\\' ; // 2-char sequences \" and \\

/*
===============================================================================
  Reserved Keywords
===============================================================================
*/
ALL : 'ALL' | 'all';
AND : 'AND' | 'and';
ANY : 'ANY' | 'any';
ASYMMETRIC : 'ASYMMETRIC' | 'asymmetric';
ASC : 'ASC' | 'asc';
BOTH : 'BOTH' | 'both';
CASE : 'CASE' | 'case';
CAST : 'CAST' | 'cast';
CREATE : 'CREATE' | 'create';
CROSS : 'CROSS' | 'cross';
DESC : 'DESC' | 'desc';
DISTINCT : 'DISTINCT' | 'distinct';
END : 'END' | 'end';
ELSE : 'ELSE' | 'else';
EXCEPT : 'EXCEPT' | 'except';
EXISTS : 'EXISTS' | 'exists';
FALSE : 'FALSE' | 'false';
FULL : 'FULL' | 'full';
FROM : 'FROM' | 'from';
FUZZY : 'FUZZY' | 'fuzzy';
GROUP : 'GROUP' | 'group';
HAVING : 'HAVING' | 'having';
ILIKE : 'ILIKE' | 'ilike';
IN : 'IN' | 'in';
INNER : 'INNER' | 'inner';
INTERSECT : 'INTERSECT' | 'intersect';
INTO : 'INTO' | 'into';
IS : 'IS' | 'is';
JOIN : 'JOIN' | 'join';
LEADING : 'LEADING' | 'leading';
LEFT : 'LEFT' | 'left';
LIKE : 'LIKE' | 'like';
BETWEEN : 'BETWEEN' | 'between';
LIMIT : 'LIMIT' | 'limit';
NATURAL : 'NATURAL' | 'natural';
NOT : 'NOT' | 'not';
NULL : 'NULL' | 'null';
ON : 'ON' | 'on';
OUTER : 'OUTER' | 'outer';
OR : 'OR' | 'or';
ORDER : 'ORDER' | 'order';
RIGHT : 'RIGHT' | 'right';
SELECT : 'SELECT' | 'select';
SOME : 'SOME' | 'some';
SYMMETRIC : 'SYMMETRIC' | 'symmetric';
TABLE : 'TABLE' | 'table';
THEN : 'THEN' | 'then';
TRAILING : 'TRAILING' | 'trailing';
TRUE : 'TRUE' | 'true';
UNION : 'UNION' | 'union';
UNIQUE : 'UNIQUE' | 'unique';
UNKNOWN : 'UNKNOWN' | 'unknown';
USING : 'USING' | 'using';
WHEN : 'WHEN' | 'when';
WHERE : 'WHERE' | 'where';
WITH : 'WITH' | 'with';

/*
===============================================================================
  Tokens for Case Insensitive Keywords
===============================================================================
*/
fragment A
	:	'A' | 'a';

fragment B
	:	'B' | 'b';

fragment C
	:	'C' | 'c';

fragment D
	:	'D' | 'd';

fragment E
	:	'E' | 'e';

fragment F
	:	'F' | 'f';

fragment G
	:	'G' | 'g';

fragment H
	:	'H' | 'h';

fragment I
	:	'I' | 'i';

fragment J
	:	'J' | 'j';

fragment K
	:	'K' | 'k';

fragment L
	:	'L' | 'l';

fragment M
	:	'M' | 'm';

fragment N
	:	'N' | 'n';

fragment O
	:	'O' | 'o';

fragment P
	:	'P' | 'p';

fragment Q
	:	'Q' | 'q';

fragment R
	:	'R' | 'r';

fragment S
	:	'S' | 's';

fragment T
	:	'T' | 't';

fragment U
	:	'U' | 'u';

fragment V
	:	'V' | 'v';

fragment W
	:	'W' | 'w';

fragment X
	:	'X' | 'x';

fragment Y
	:	'Y' | 'y';

fragment Z
	:	'Z' | 'z';


WS : [\t \r\n] -> skip;
