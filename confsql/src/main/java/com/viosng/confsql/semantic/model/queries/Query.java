package com.viosng.confsql.semantic.model.queries;

import com.viosng.confsql.semantic.model.Parameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 2:04
 */
public interface Query {
    public List<Parameter> getParameters();
    
}
