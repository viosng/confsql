package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.InternalQueryElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 25.12.2014
 * Time: 10:36
 */
public interface XMLConverter <T extends XMLConverter.XMLModelElement, E extends InternalQueryElement>{
    
    public static interface XMLModelElement{}
    
    @NotNull
    public T convertToXMLExpression(@NotNull E expression);

    @NotNull
    public E convertFromXMLExpression(@NotNull T xmlExpression);

    /**
     * Configures {@code XStream} object to work with {@code XMLConverter} implementation
     * @param xStream the object to configure
     */
    public void configure(@NotNull XStream xStream);
}
