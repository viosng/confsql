grammar ConfigurableSQL;

expr : '~' expr                                                   #bitNeg
     | '-' expr                                                   #neg
     | <assoc=right> expr '**' expr                               #power
     | expr ('*'|'/'|'%') expr                                    #arithmFirst
     | expr ('+'|'-'|'&'|'|'|'^') expr                            #arithmSecond
     | expr ('='|'>'|'<'|'>='|'<='|'<>'|'!='|'!>'|'!<') expr      #comparing
     | 'NOT' NumberLiteral                                        #not
     | expr 'AND' expr                                            #and
     | expr ('ALL'|'ANY'|'BETWEEN'|'IN'|'LIKE'|'OR'|'SOME') expr  #arrayFunc
     | StringLiteral '(' (expr (',' expr)*)? ')'                  #functionCall
     | '(' expr ')'                                               #brackets
     | NumberLiteral                                              #constant
     ;

StringLiteral: [a-zA-z]+ [a-zA-z0-9_]*  ;

NumberLiteral : FloatWithExp
               | Int
               | BooleanConstant
               ;
           
BooleanConstant : 'TRUE' 
                | 'true'
                | 'FALSE'
                | 'false'
                ;

FloatWithExp : Float ([eE] Sign? Int )* ;

Float : Int+ FloatAfterDot
      | '0' '.' Digit*
      | '.' Digit+
      ;

FloatAfterDot : '.' Digit* ;

Int : NonZeroDigit+ Digit* ;

fragment Sign : [+-] ;
fragment NonZeroDigit : [1-9] ;
fragment Digit : [0-9] ;

WS : [\t \r\n] -> skip;