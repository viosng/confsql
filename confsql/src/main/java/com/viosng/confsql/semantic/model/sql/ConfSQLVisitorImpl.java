package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.expr.impl.*;
import com.viosng.confsql.semantic.model.sql.query.SQLGroupByClause;
import com.viosng.confsql.semantic.model.sql.query.SQLOrderByClause;

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
    public SQLExpression visitGroupByClause(ConfSQLParser.GroupByClauseContext ctx) {
        return new SQLGroupByClause(
                ctx.paranthesizedParamList() != null
                        ? ((SQLParameterList) visit(ctx.paranthesizedParamList())).getParameterList()
                        : Collections.<SQLParameter>emptyList(),
                ((SQLExpressionList) visit(ctx.exprList())).getExpressionList());
    }

    @Override
    public SQLExpression visitHavingClause(ConfSQLParser.HavingClauseContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public SQLExpression visitOrderByClause(ConfSQLParser.OrderByClauseContext ctx) {
        return new SQLOrderByClause(
                ctx.paranthesizedParamList() != null 
                        ? ((SQLParameterList) visit(ctx.paranthesizedParamList())).getParameterList() 
                        : Collections.<SQLParameter>emptyList(),
                ((SQLExpressionList) visit(ctx.exprList())).getExpressionList(),
                ctx.OrderType() != null ? ctx.OrderType().getText() : "asc");
    }

    @Override
    public SQLExpression visitLimitClause(ConfSQLParser.LimitClauseContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public SQLExpression visitAsClause(ConfSQLParser.AsClauseContext ctx) {
        return new SQLConstant(ctx.StringLiteral().getText());
    }

    @Override
    public SQLExpression visitExprsAndParams(ConfSQLParser.ExprsAndParamsContext ctx) {
        return new SQLExpressionsAndParamsList(
                ctx.exprList() != null 
                        ? (SQLExpressionList) visit(ctx.exprList())
                        : new SQLExpressionList(Collections.<SQLExpression>emptyList()),
                ctx.paramList() != null
                        ? (SQLParameterList) visit(ctx.paramList())
                        : new SQLParameterList(Collections.<SQLParameter>emptyList()));
    }

    @Override
    public SQLExpression visitParanthesizedParamList(ConfSQLParser.ParanthesizedParamListContext ctx) {
        return visit(ctx.paramList());
    }

    @Override
    public SQLExpression visitParamList(ConfSQLParser.ParamListContext ctx) {
        return new SQLParameterList(ctx.param().stream().map(p -> (SQLParameter) visit(p)).collect(Collectors.toList()));
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
        return new SQLFunctionCall("is", Arrays.asList(visit(ctx.expr(0)), visit(ctx.expr(1))), Collections.emptyList());
    }

    @Override
    public SQLExpression visitCast(ConfSQLParser.CastContext ctx) {
        return new SQLFunctionCall("cast", Arrays.asList(visit(ctx.expr()), new SQLConstant(ctx.StringLiteral().getText())), 
                Collections.emptyList());
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
        if (ctx.LEFT_PAREN() == null) {
            return new SQLField(ctx.StringLiteral().getText());
        } else {
            SQLExpressionsAndParamsList epList = (SQLExpressionsAndParamsList) visit(ctx.exprsAndParams());
            return new SQLFunctionCall(ctx.StringLiteral().getText(), epList.getExpressionList().getExpressionList(), 
                    epList.getParameterList().getParameterList());
        }
    }

    @Override
    public SQLExpression visitBrackets(ConfSQLParser.BracketsContext ctx) {
        return visit(ctx.expr());
    }
}
