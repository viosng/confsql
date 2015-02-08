package com.viosng.confsql.semantic.model.sql;

import com.viosng.confsql.semantic.model.algebra.Query;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 06.02.2015
 * Time: 20:27
 */
public interface SQLQuery {
    static double a = .0e0;
    @NotNull
    public Query convertToQuery();
}
