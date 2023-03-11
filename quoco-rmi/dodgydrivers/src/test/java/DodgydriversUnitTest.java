import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.ClientInfo;
import service.Constants;
import service.Quotation;
import service.QuotationService;
import dodgydrivers.DDQService;
import org.junit.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class DodgydriversUnitTest {

    private static Registry registry;

    @BeforeClass
    public static void setup(){
        service.QuotationService ddqService = new DDQService();
        try{
            registry = LocateRegistry.createRegistry(1099);
            service.QuotationService quotationService = (service.QuotationService)
                    UnicastRemoteObject.exportObject(ddqService, 0);
            registry.bind(service.Constants.AULD_FELLAS_SERVICE, quotationService);
        } catch(Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception{
        QuotationService service = (QuotationService)
                registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void genereteQuotationTest() throws Exception{
        QuotationService service = new DDQService();

        //check if points only as a condition, take points=4, price should be between 640 and 800
        ClientInfo pointsTest = new ClientInfo("Lily", 'F', 50, 4, 0, "licenseNumber");
        double pointsQuotation = service.generateQuotation(pointsTest).price;
        assertTrue(pointsQuotation >= 640 && pointsQuotation < 800);

        //check if claims and points as a condition, take claims=1 and points=4, price should be between 560 and 700
        ClientInfo claimsTest = new ClientInfo("Lily", 'F', 50, 4, 1, "licenseNumber");
        double claimsQuotation = service.generateQuotation(claimsTest).price;
        assertTrue(claimsQuotation >= 560 && claimsQuotation < 700);

        //check whether the method returns a Quotation object
        ClientInfo returnTest = new ClientInfo();
        assertThat(service.generateQuotation(returnTest), instanceOf(Quotation.class));
        ;
    }

}