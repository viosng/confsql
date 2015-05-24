package com.viosng.confsql.semantic.model.other;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 9:12
 */
public enum ArithmeticType {
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
    FUNCTION_CALL("function"),
    CONSTANT("constant"),
    ATTRIBUTE("attribute"),
    CASE("case", "CASE"),
    QUERY("query"),
    ORDER("order"),
    PARAMETER("parameter"),
    OBJECT(),
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
