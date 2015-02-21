package com.viosng.confsql.semantic.model.sql;

import com.thoughtworks.xstream.XStream;
import com.viosng.confsql.semantic.model.other.Parameter;
import com.viosng.confsql.semantic.model.sql.old.impl.SQLSourceImpl;
import com.viosng.confsql.semantic.model.sql.old.SQLSource;
import com.viosng.confsql.xml.XMLQueryConverter;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 31.01.2015
 * Time: 18:40
 */
public class SQLConvertionTest {
    private XStream xstream = new XStream();
    
    @Test
    public void testSourceConvert() throws Exception {
        SQLSource source = new SQLSourceImpl("source", Arrays.asList(new Parameter("size", "20")), "alias");
        XMLQueryConverter.XMLQuery xmlQuery = XMLQueryConverter.getInstance().convertToXML(source.convertToQuery());
        XMLQueryConverter.getInstance().configure(xstream);
        System.out.println(xstream.toXML(xmlQuery));

    }
}
