package com.viosng.confsql.semantic.model.sql;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 12:59
 */
public class ConfSQLVisitorImpl extends ConfSQLBaseVisitor<SQLExpression> {

    @Override
    public SQLExpression visitQuery(ConfSQLParser.QueryContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public SQLExpression visitAnd(ConfSQLParser.AndContext ctx) {
        return new SQLConstant(visit(ctx.expr(0)).toString()  + " and " + visit(ctx.expr(1)).toString());
    }

    @Override
    public SQLExpression visitOr(ConfSQLParser.OrContext ctx) {
        return new SQLConstant(visit(ctx.expr(0)).toString() + " or " + visit(ctx.expr(1)).toString());
    }

    @Override
    public SQLExpression visitConstant(ConfSQLParser.ConstantContext ctx) {
        return new SQLConstant(ctx.getText());
    }
}
