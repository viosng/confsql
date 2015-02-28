package com.viosng.confsql.semantic.model.sql;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 13:04
 */
public interface SQLExpression {
    
    static enum ArithmeticType {
        AND("and"),
        OR("or"),
        BIT_AND("&"),
        BIT_OR("|"),
        BIT_XOR("^"),
        BIT_NEG("~"),
        PLUS("plus", "+"), 
        MINUS("minus", "-"),
        MULTIPLY("multiply", "*"),
        DIVIDE("divide", "/"),
        MODULAR("modular", "%"),
        POWER("power", "**"),
        EQUAL("equal", "="), 
        GT("GT", ">"), 
        LT("LT", "<"), 
        GE("GE", ">="), 
        LE("LE", "<="),
        NOT_EQUAL("!=", "<>"),
        NOT("not", "!"),
        CONCATENATION("||"),
        UNDEFINED()
        ;
        
        @NotNull
        private final List<String> aliases;

        ArithmeticType(String... aliases) {
            this.aliases = Lists.newArrayList(aliases);
        }

        @NotNull
        public List<String> getAliases() {
            return aliases;
        }
        
        @NotNull
        public static ArithmeticType resolveArithmeticType(String alias) {
            String lowerCase = alias.toLowerCase();
            return Arrays.stream(ArithmeticType.values()).filter(t -> t.getAliases().contains(lowerCase)).findFirst().orElse(UNDEFINED);
        }
    }
}
