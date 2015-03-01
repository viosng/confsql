package com.viosng.confsql.semantic.model.sql.query;

import com.viosng.confsql.semantic.model.sql.SQLExpression;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 02.03.2015
 * Time: 3:21
 */
public class SQLAsteriskSelectItem implements SQLExpression {
    
    @Nullable
    private final String objectName;

    public SQLAsteriskSelectItem(@Nullable String objectName) {
        this.objectName = objectName;
    }

    @Nullable
    public String getObjectName() {
        return objectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SQLAsteriskSelectItem)) return false;

        SQLAsteriskSelectItem that = (SQLAsteriskSelectItem) o;

        return !(objectName != null ? !objectName.equals(that.objectName) : that.objectName != null);
    }

    @Override
    public int hashCode() {
        return objectName != null ? objectName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SQLAsteriskSelectItem{" +
                "objectName='" + objectName + '\'' +
                '}';
    }
}
