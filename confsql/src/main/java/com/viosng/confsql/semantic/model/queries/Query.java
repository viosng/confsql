package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.ModelElement;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.schema.Schema;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:04
 */
public interface Query extends ModelElement{
    public List<Parameter> getParameters();
    public Schema getSchema();
    public Context getContext();
}
