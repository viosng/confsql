package com.viosng.confsql.semantic.model.thrift;

import com.google.common.base.Joiner;
import com.viosng.confsql.semantic.model.sql.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.junit.Test;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class ThriftConvertionTest {

    private ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    @Test
    public void testFull() throws Exception {
        String query = Joiner.on("").join(Files.readAllLines(Paths.get(
                "src/test/java/com/viosng/confsql/semantic/model/thrift/query.sql"), StandardCharsets.UTF_8));
        ThriftExpression thriftQuery = ThriftExpressionConverter.getInstance().convert(query);

        TSerializer serializer = new TSerializer(new TJSONProtocol.Factory());
        String json = serializer.toString(thriftQuery);
        TDeserializer deserializer = new TDeserializer(new TJSONProtocol.Factory());
        ThriftExpression deserializedThriftQuery = new ThriftExpression();
        deserializer.deserialize(deserializedThriftQuery, json, "UTF-8");
        assertEquals(thriftQuery, deserializedThriftQuery);

        TSerializer jsonSerializer = new TSerializer(new TSimpleJSONProtocol.Factory());
        json = jsonSerializer.toString(thriftQuery);

        FileWriter out = new FileWriter("jsonOutput.json");
        out.write(json);
        out.close();
        System.out.println(query);
        System.out.println(json);
        //System.out.println(exp.verify(Context.EMPTY_CONTEXT));
    }
}
