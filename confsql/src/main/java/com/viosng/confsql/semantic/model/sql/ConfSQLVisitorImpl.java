package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.impl.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.02.2015
 * Time: 12:59
 */
public class ConfSQLVisitorImpl extends ConfSQLBaseVisitor<SQLExpression> {

    @Override
    public SQLExpression visitExprsAndParams(ConfSQLParser.ExprsAndParamsContext ctx) {
        return new SQLExpressionsAndParamsList(
                ctx.exprList() != null 
                        ? (SQLExpressionList) visit(ctx.exprList()) 
                        : new SQLExpressionList(Collections.<SQLExpression>emptyList()),
                ctx.paramList() != null
                        ? (SQLExpressionList) visit(ctx.paramList())
                        : new SQLExpressionList(Collections.<SQLExpression>emptyList()));
    }

    @Override
    public SQLExpression visitParanthesizedParamList(ConfSQLParser.ParanthesizedParamListContext ctx) {
        return visit(ctx.paramList());
    }

    @Override
    public SQLExpression visitParamList(ConfSQLParser.ParamListContext ctx) {
        return new SQLExpressionList(ctx.param().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public SQLExpression visitParam(ConfSQLParser.ParamContext ctx) {
        String name = ctx.name.getText();
        name = name.charAt(0) == '"' ? name.substring(1, name.length() - 1) : name;
        return new SQLParameter(name, visit(ctx.expr()));
    }
    
    @Override
    public SQLExpression visitExprList(ConfSQLParser.ExprListContext ctx) {
        return new SQLExpressionList(ctx.expr().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public SQLExpression visitBitNeg(ConfSQLParser.BitNegContext ctx) {
        return new SQLUnaryExpression(SQLExpression.ArithmeticType.BIT_NEG, visit(ctx.expr()));
    }

    @Override
    public SQLExpression visitNeg(ConfSQLParser.NegContext ctx) {
        return new SQLUnaryExpression(SQLExpression.ArithmeticType.MINUS, visit(ctx.expr()));
    }

    @Override
    public SQLExpression visitPower(ConfSQLParser.PowerContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.POWER, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitArithmFirst(ConfSQLParser.ArithmFirstContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.resolveArithmeticType(ctx.op.getText()),
                visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitArithmSecond(ConfSQLParser.ArithmSecondContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.resolveArithmeticType(ctx.op.getText()),
                visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitConcatenation(ConfSQLParser.ConcatenationContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.CONCATENATION, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitNot(ConfSQLParser.NotContext ctx) {
        return new SQLUnaryExpression(SQLExpression.ArithmeticType.NOT, visit(ctx.expr()));
    }

    @Override
    public SQLExpression visitComparing(ConfSQLParser.ComparingContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.resolveArithmeticType(ctx.comp.getText()),
                visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitAnd(ConfSQLParser.AndContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.AND, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitOr(ConfSQLParser.OrContext ctx) {
        return new SQLBinaryExpression(SQLExpression.ArithmeticType.OR, visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public SQLExpression visitIs(ConfSQLParser.IsContext ctx) {
        return new SQLFunctionCall("is", new SQLExpressionsAndParamsList(
                new SQLExpressionList(Arrays.asList(visit(ctx.expr(0)), visit(ctx.expr(1)))),
                new SQLExpressionList(Collections.emptyList())));
    }

    @Override
    public SQLExpression visitCast(ConfSQLParser.CastContext ctx) {
        return new SQLFunctionCall("cast", new SQLExpressionsAndParamsList(
                new SQLExpressionList(Arrays.asList(visit(ctx.expr()), new SQLConstant(ctx.StringLiteral().getText()))),
                new SQLExpressionList(Collections.emptyList())));
    }

    @Override
    public SQLExpression visitCase(ConfSQLParser.CaseContext ctx) {
        return new SQLCase(ctx.expr() != null ? visit(ctx.expr()) : null,
                ctx.caseWhenClause().stream().map(e -> (SQLCase.SQLWhenThenClause) visit(e)).collect(Collectors.toList()),
                ctx.caseElseClause() != null ? visit(ctx.caseElseClause()) : null);
    }

    @Override
    public SQLExpression visitCaseWhenClause(ConfSQLParser.CaseWhenClauseContext ctx) {
        return new SQLCase.SQLWhenThenClause(visit(ctx.w), visit(ctx.t));
    }

    @Override
    public SQLExpression visitCaseElseClause(ConfSQLParser.CaseElseClauseContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public SQLExpression visitSubQueryExpr(ConfSQLParser.SubQueryExprContext ctx) {
        return visit(ctx.query());
    }

    @Override
    public SQLExpression visitConstant(ConfSQLParser.ConstantContext ctx) {
        return new SQLConstant(ctx.getText());
    }

    @Override
    public SQLExpression visitFieldExpr(ConfSQLParser.FieldExprContext ctx) {
        return new SQLField(ctx.getText());
    }

    @Override
    public SQLExpression visitColumnOrFunctionCall(ConfSQLParser.ColumnOrFunctionCallContext ctx) {
        return ctx.LEFT_PAREN() == null 
                ? new SQLField(ctx.StringLiteral().getText())
                : new SQLFunctionCall(ctx.StringLiteral().getText(), (SQLExpressionsAndParamsList) visit(ctx.exprsAndParams()));
    }

    @Override
    public SQLExpression visitBrackets(ConfSQLParser.BracketsContext ctx) {
        return visit(ctx.expr());
    }
}
