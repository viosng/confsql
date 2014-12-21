import com.viosng.confsql.xml.Parameter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 19.12.2014
 * Time: 15:41
 */
public class Main {

    public static void main(String[] args) {
        Parameter parameter = new Parameter("a", "b");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Parameter.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(parameter, System.out);
            OutputStream out = new BufferedOutputStream(System.out);
            jaxbMarshaller.marshal(parameter, System.out);
            
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader("xml string here");
            Parameter person = (Parameter) unmarshaller.unmarshal(
                    new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<parameter name=\"a\" value=\"b\"/>"));
            System.out.println(person);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
