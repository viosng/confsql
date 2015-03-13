package com.viosng.confsql.semantic.model.algebra.queries;

import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.algebra.special.expr.ValueExpression;
import com.viosng.confsql.semantic.model.other.ArithmeticType;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Notification;
import com.viosng.confsql.semantic.model.other.Parameter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:04
 */
public interface Query extends Expression {

    @NotNull
    @Override
    default ArithmeticType type() {
        return ArithmeticType.QUERY;
    }

    @Override
    default Expression findExpressionByType(ArithmeticType arithmeticType){
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    default Notification verify(@NotNull Context context){
        return new Notification();
    }

    @NotNull
    public QueryType queryType();
    
    @NotNull
    public List<Parameter> getParameters();

    /**
     * @return the list of query result attributes
     */
    @NotNull
    public List<ValueExpression.AttributeExpression> getQueryObjectAttributes();

    @NotNull
    public List<Expression> getRequiredSchemaAttributes();

    @NotNull
    public List<Query> getSubQueries();

    @NotNull
    public List<Expression> getArgumentExpressions();

    @NotNull
    public Context getContext();
    
    @NotNull
    public Notification verify();
    
    static enum QueryType {
        FICTIVE,
        PRIMARY,
        FILTER,
        FUSION,
        JOIN,
        AGGREGATION,
        NEST,
        UNNEST,
        GROUP_JOIN
    }

    interface UnaryQuery extends Query {
        @NotNull
        default Query getArg() {
            return getSubQueries().get(0);
        }
    }

    interface BinaryQuery extends Query {
        @NotNull
        default Query getLeftArg() {
            return getSubQueries().get(0);
        }

        @NotNull
        default Query getRightArg() {
            return getSubQueries().get(1);
        }
    }

    interface Primary extends Query {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.PRIMARY;}
    }
    
    interface Filter extends UnaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.FILTER;}
    }

    interface Fusion extends Query{
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.FUSION;}
    }

    interface Join extends BinaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.JOIN;}
    }
    
    interface Aggregation extends UnaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.AGGREGATION;}
    }

    interface Nest extends UnaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.NEST;}
    }

    interface UnNest extends UnaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.UNNEST;}
    }

    interface GroupJoin extends BinaryQuery {
        @NotNull
        @Override
        default QueryType queryType() {return QueryType.GROUP_JOIN;}
    }
}
