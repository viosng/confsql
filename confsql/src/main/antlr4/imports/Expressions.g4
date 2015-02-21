grammar Expressions;

query : 'SELECT' selectList tableExpression ;

selectList : '*'
           | selectSubList (',' selectSubList)*
           ;
           
tableExpression : 'TABLE';
        
selectSubList : expr;

AsClause : 'AS' StringLiteral ;
expr : '~' expr                                                                           #bitNeg
     | '-' expr                                                                           #neg
     | <assoc=right> expr '**' expr                                                       #power
     | expr ('*'|'/'|'%') expr                                                            #arithmFirst
     | expr ('+'|'-'|'&'|'|'|'^') expr                                                    #arithmSecond
     | expr ('='|'>'|'<'|'>='|'<='|'<>'|'!='|'!>'|'!<') expr                              #comparing
     | 'NOT' NumberLiteral                                                                #not
     | expr 'AND' expr                                                                    #and
     | expr 'OR' expr                                                                     #or
     | expr 'LIKE' String                                                                 #like
     | expr 'BETWEEN' (Float 'AND' Float | Int 'AND' Int | String 'AND' String)           #between
     | expr ('ALL'|'ANY'|'IN') '(' query ')'                                              #subQuery
     | 'EXISTS' '(' query ')'                                                             #exists
     | StringLiteral '(' (expr (',' expr)*)? ')'                                          #functionCall
     | Field                                                                              #field
     | '(' expr ')'                                                                       #brackets
     | (NumberLiteral | String)                                                           #constant
     ;

Field : StringLiteral ('.' StringLiteral)? ;
StringLiteral: [a-zA-z]+ [a-zA-z0-9_]*  ;
String: '"' (Esc|.)*? '"' ;

NumberLiteral : Float
              | Int
              | BooleanConstant
              ;

BooleanConstant : 'TRUE'
                | 'true'
                | 'FALSE'
                | 'false'
                ;

Float : FloatWithoutExp ([eE] Sign? Int )* ;

FloatWithoutExp : Int+ FloatAfterDot
                | '0' '.' Digit*
                | '.' Digit+
                ;

FloatAfterDot : '.' Digit* ;

Int : NonZeroDigit+ Digit* ;

LineComment : MinusSign MinusSign .*? '\r'? '\n' -> skip ;

fragment Sign : [+-] ;
fragment MinusSign : '-' ;
fragment NonZeroDigit : [1-9] ;
fragment Digit : [0-9] ;
fragment Esc : '\\"' | '\\\\' ; // 2-char sequences \" and \\

WS : [\t \r\n] -> skip;