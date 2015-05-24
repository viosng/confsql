import com.viosng.confsql.semantic.model.algebra.Expression;
import com.viosng.confsql.semantic.model.other.Context;
import com.viosng.confsql.semantic.model.sql.*;
import com.viosng.confsql.semantic.model.thrift.ThriftExpression;
import com.viosng.confsql.semantic.model.thrift.ThriftExpressionConverter;
import com.viosng.confsql.semantic.model.thrift.Translator;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 19.12.2014
 * Time: 15:41
 */
public class Main {

    public static Translator.Processor processor;

    public static void startClient() {
        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            Translator.Client client = new Translator.Client(protocol);

            perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static ConfSQLVisitor<SQLExpression> visitor = new ConfSQLVisitorImpl();

    private static ConfSQLParser getParser(String input) {
        return new ConfSQLParser(new CommonTokenStream(new ConfSQLLexer(new ANTLRInputStream(input))));
    }

    public static void startServer() {
        try {
            processor = new Translator.Processor<>(query -> {
                try {
                    Expression exp = visitor.visit(getParser(query).stat()).convert();
                    System.out.println(exp.verify(Context.EMPTY_CONTEXT));
                    return ThriftExpressionConverter.getInstance().convert(exp);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            });

            new Thread(() -> {
                TServerTransport serverTransport = null;
                try {
                    serverTransport = new TServerSocket(9090);
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
                TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
                System.out.println("Starting the simple server...");
                server.serve();
            }).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static void main(String [] args) throws InterruptedException {
        startServer();
        Thread.sleep(200);
        startClient();
    }

    private static void perform(Translator.Client client) throws TException {
        Scanner in = new Scanner(System.in);
        String query;
        while (!(query = in.nextLine()).equals("0")) {
            ThriftExpression result = client.translate(query);
            TSerializer jsonSerializer = new TSerializer(new TSimpleJSONProtocol.Factory());
            System.out.println(jsonSerializer.toString(result));
        }
    }
}
