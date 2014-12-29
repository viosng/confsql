package com.viosng.confsql.semantic.model;

import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 21.12.2014
 * Time: 11:40
 */
public interface ModelElement {
    
    @NotNull
    public final static String UNDEFINED_ID = "";
    
    @NotNull
    public String id();
    
}
