package com.viosng.confsql.xml;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.ModelElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 25.12.2014
 * Time: 10:36
 */
public interface XMLConverter <T extends XMLConverter.XMLModelElement, E extends ModelElement>{
    
    public static interface XMLModelElement{}
    
    @NotNull
    public T convertToXML(@NotNull E modelElement);

    @NotNull
    public E convertFromXML(@NotNull T xmlElement);

    /**
     * Configures {@code XStream} object to work with {@code XMLConverter} implementation
     * @param xStream the object to configure
     */
    public void configure(@NotNull XStream xStream);
}
