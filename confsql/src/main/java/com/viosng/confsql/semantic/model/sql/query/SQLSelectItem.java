package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import com.viosng.confsql.semantic.model.sql.expr.impl.SQLField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 3:16
 */
public class SQLSelectItem implements SQLExpression {
    @NotNull
    private final SQLExpression item;
    
    @Nullable
    private final SQLField as;

    public SQLSelectItem(@NotNull SQLExpression item, @Nullable SQLField as) {
        this.item = item;
        this.as = as;
    }

    @NotNull
    public SQLExpression getItem() {
        return item;
    }

    @Nullable
    public SQLField getAs() {
        return as;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLSelectItem)) return false;

        SQLSelectItem that = (SQLSelectItem) o;

        return !(as != null ? !as.equals(that.as) : that.as != null) && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        int result = item.hashCode();
        result = 31 * result + (as != null ? as.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SQLSelectItem{" +
                "item=" + item +
                ", as=" + as +
                '}';
    }
}
