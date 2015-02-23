package com.viosng.confsql.semantic.model.sql;

import com.google.common.base.Splitter;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 18:10
 */

@RunWith(Theories.class)
public class ConfSQLParserTest {
    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    private void printVisit(String input) {
        System.out.println(visitor.visit(getParser(input).query()));

    }

    @Test
    public void testBool() throws Exception {
        printVisit("TRUE AND null OR false AND true");
    }

    @Test
    public void testName() throws Exception {
        String input = "1 + f(3)";
        ConfSQLLexer l = new ConfSQLLexer(new ANTLRInputStream(input));
        List<? extends Token> list = l.getAllTokens();
        System.out.println(list);
    }

    @Test
    public void testGenRules() throws Exception {
        String rules = "AS : A S;\n" +
                "ALL : A L L;\n" +
                "AND : A N D;\n" +
                "ANY : A N Y;\n" +
                "ASYMMETRIC : A S Y M M E T R I C;\n" +
                "ASC : A S C;\n" +
                "\n" +
                "BOTH : B O T H;\n" +
                "\n" +
                "CASE : C A S E;\n" +
                "CAST : C A S T;\n" +
                "CREATE : C R E A T E;\n" +
                "CROSS : C R O S S;\n" +
                "\n" +
                "DESC : D E S C;\n" +
                "DISTINCT : D I S T I N C T;\n" +
                "\n" +
                "END : E N D;\n" +
                "ELSE : E L S E;\n" +
                "EXCEPT : E X C E P T;\n" +
                "\n" +
                "FALSE : F A L S E;\n" +
                "FULL : F U L L;\n" +
                "FROM : F R O M;\n" +
                "FUZZY : F U Z Z Y;\n" +
                "\n" +
                "GROUP : G R O U P;\n" +
                "\n" +
                "HAVING : H A V I N G;\n" +
                "\n" +
                "ILIKE : I L I K E;\n" +
                "IN : I N;\n" +
                "INNER : I N N E R;\n" +
                "INTERSECT : I N T E R S E C T;\n" +
                "INTO : I N T O;\n" +
                "IS : I S;\n" +
                "\n" +
                "JOIN : J O I N;\n" +
                "\n" +
                "LEADING : L E A D I N G;\n" +
                "LEFT : L E F T;\n" +
                "LIKE : L I K E;\n" +
                "LIMIT : L I M I T;\n" +
                "\n" +
                "NATURAL : N A T U R A L;\n" +
                "NOT : N O T;\n" +
                "NULL : N U L L;\n" +
                "\n" +
                "ON : O N;\n" +
                "OUTER : O U T E R;\n" +
                "OR : O R;\n" +
                "ORDER : O R D E R;\n" +
                "RIGHT : R I G H T;\n" +
                "SELECT : S E L E C T;\n" +
                "SOME : S O M E;\n" +
                "SYMMETRIC : S Y M M E T R I C;\n" +
                "\n" +
                "TABLE : T A B L E;\n" +
                "THEN : T H E N;\n" +
                "TRAILING : T R A I L I N G;\n" +
                "TRUE : TRUE;\n" +
                "\n" +
                "UNION : U N I O N;\n" +
                "UNIQUE : U N I Q U E;\n" +
                "USING : U S I N G;\n" +
                "\n" +
                "WHEN : W H E N;\n" +
                "WHERE : W H E R E;\n" +
                "WITH : W I T H;";
        Iterable<String> rule = Splitter.on("\n").omitEmptyStrings().split(rules);

        for (String s : rule) {
            String ruleName = s.substring(0, s.indexOf(" "));
            System.out.println(ruleName.toLowerCase() + s.substring(s.indexOf(" ")));
        }

    }
}
