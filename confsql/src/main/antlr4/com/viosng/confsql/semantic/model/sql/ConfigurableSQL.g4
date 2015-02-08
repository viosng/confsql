grammar ConfigurableSQL;

expr : <assoc=right> expr '^' expr
     | expr ('*'|'/') expr
     | expr ('+'|'-') expr
     | NUMBER
     ;

NUMBER : FLOAT_WITH_EXP
       | SIGNED_INT
       ;

FLOAT_WITH_EXP : FLOAT ([eE] SIGNED_INT)* ;

FLOAT : SIGN? INT+ FLOAT_AFTER_DOT
      | SIGN? '0' '.' DIGIT*
      | SIGN? '.' DIGIT+
      ;

FLOAT_AFTER_DOT : '.' DIGIT* ;

SIGNED_INT : SIGN? INT ;

INT :NON_ZERO_DIGIT+ DIGIT* ;

fragment SIGN : [+-] ;
fragment NON_ZERO_DIGIT : [1-9] ;
fragment DIGIT : [0-9] ;

WS : [\t \r\n] -> skip;