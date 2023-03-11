import broker.LocalBrokerService;
import org.junit.*;
import service.ClientInfo;
import service.Quotation;

import java.util.List;
import static org.junit.Assert.*;

public class BrokerUnitTest {
    @Test
    public void getQuotationsTest() throws Exception {
        //new a ClientInfo object
        ClientInfo info = new ClientInfo();
        //new a LocalBrokerService object
        LocalBrokerService localbrokerservice = new LocalBrokerService();
        //assign the result of method getQuotations to a variable
        List<Quotation> returnedList = localbrokerservice.getQuotations(info);

        //check if the result is an empty list
        assertTrue(returnedList.size() == 0);
    }
}