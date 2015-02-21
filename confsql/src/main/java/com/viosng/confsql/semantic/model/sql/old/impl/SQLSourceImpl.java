package com.viosng.confsql.semantic.model.sql.old.impl;

import com.google.common.collect.Lists;
import com.viosng.confsql.semantic.model.algebra.Query;
import com.viosng.confsql.semantic.model.algebra.QueryBuilder;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.old.SQLSource;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 31.01.2015
 * Time: 17:53
 */
public class SQLSourceImpl extends AbstractSQLSingleQuery implements SQLSource {
    
    private static final String SOURCE_NAME_PARAMETER = "sourceName";
    
    private static List<Parameter> addSourceNameParameter(List<Parameter> parameters, String sourceName) {
        List<Parameter> newParameters = Lists.newArrayList(parameters);
        newParameters.add(new Parameter(SOURCE_NAME_PARAMETER, sourceName));
        return newParameters;
    }

    @NotNull
    private final String sourceName;


    public SQLSourceImpl(@NotNull String alias, @NotNull List<Parameter> parameters, @NotNull String sourceName) {
        super(alias, addSourceNameParameter(parameters, sourceName));
        this.sourceName = sourceName;
    }

    public SQLSourceImpl(@NotNull List<Parameter> parameters, @NotNull String sourceName) {
        super(sourceName, parameters);
        this.sourceName = sourceName;
    }

    @NotNull
    @Override
    public String sourceName() {
        return sourceName;
    }

    @NotNull
    @Override
    public Query convertToQuery() {
        return new QueryBuilder()
                .queryType(Query.QueryType.PRIMARY)
                .parameters(parameters())
                .id(alias())
                .create();
    }
}
