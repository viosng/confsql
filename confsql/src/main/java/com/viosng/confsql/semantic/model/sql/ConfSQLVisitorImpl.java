package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.sql.impl.*;

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
    public SQLExpression visitExprs_and_params(ConfSQLParser.Exprs_and_paramsContext ctx) {
        return new SQLExpressionsAndParamsList(
                ctx.expr_list() != null 
                        ? (SQLExpressionList) visit(ctx.expr_list()) 
                        : new SQLExpressionList(Collections.<SQLExpression>emptyList()),
                ctx.param_list() != null
                        ? (SQLExpressionList) visit(ctx.param_list())
                        : new SQLExpressionList(Collections.<SQLExpression>emptyList()));
    }

    @Override
    public SQLExpression visitParam_list(ConfSQLParser.Param_listContext ctx) {
        return new SQLExpressionList(ctx.param().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public SQLExpression visitParam(ConfSQLParser.ParamContext ctx) {
        String name = ctx.name.getText();
        name = name.charAt(0) == '"' ? name.substring(1, name.length() - 1) : name;
        return new SQLParameter(name, visit(ctx.expr()));
    }
    
    @Override
    public SQLExpression visitExpr_list(ConfSQLParser.Expr_listContext ctx) {
        return new SQLExpressionList(ctx.expr().stream().map(this::visit).collect(Collectors.toList()));
    }

    @Override
    public SQLExpression visitCase(ConfSQLParser.CaseContext ctx) {
        return new SQLCaseExpr(ctx.expr() != null ? visit(ctx.expr()) : null,
                ctx.case_when_clause().stream().map(e -> (SQLCaseExpr.SQLWhenThenClause) visit(e)).collect(Collectors.toList()),
                ctx.case_else_clause() != null ? visit(ctx.case_else_clause()) : null);
    }

    @Override
    public SQLExpression visitCase_when_clause(ConfSQLParser.Case_when_clauseContext ctx) {
        return new SQLCaseExpr.SQLWhenThenClause(visit(ctx.w), visit(ctx.t));
    }

    @Override
    public SQLExpression visitCase_else_clause(ConfSQLParser.Case_else_clauseContext ctx) {
        return visit(ctx.expr());
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
                : new SQLFunctionCall(ctx.StringLiteral().getText(), (SQLExpressionsAndParamsList) visit(ctx.exprs_and_params()));
    }

    @Override
    public SQLExpression visitBrackets(ConfSQLParser.BracketsContext ctx) {
        return visit(ctx.expr());
    }
}
