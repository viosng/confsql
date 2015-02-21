package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.expressions.SQLConstant;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 12:59
 */
public class ConfSQLVisitorImpl extends ConfSQLBaseVisitor<SQLElement> {

    @Override
    public SQLElement visitQuery(ConfSQLParser.QueryContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public SQLElement visitAnd(ConfSQLParser.AndContext ctx) {
        return new SQLConstant(visit(ctx.expr(0)).toString()  + " and " + visit(ctx.expr(1)).toString());
    }

    @Override
    public SQLElement visitOr(ConfSQLParser.OrContext ctx) {
        return new SQLConstant(visit(ctx.expr(0)).toString() + " or " + visit(ctx.expr(1)).toString());
    }

    @Override
    public SQLElement visitConstant(ConfSQLParser.ConstantContext ctx) {
        return new SQLConstant(ctx.getText());
    }
}
