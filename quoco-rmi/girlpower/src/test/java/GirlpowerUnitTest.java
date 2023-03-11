import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.ClientInfo;
import service.Constants;
import service.Quotation;
import service.QuotationService;
import girlpower.GPQService;
import org.junit.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class GirlpowerUnitTest {

    private static Registry registry;

    @BeforeClass
    public static void setup(){
        service.QuotationService gpqService = new GPQService();
        try{
            registry = LocateRegistry.createRegistry(1099);
            service.QuotationService quotationService = (service.QuotationService)
                    UnicastRemoteObject.exportObject(gpqService, 0);
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
        QuotationService service = new GPQService();

        //check if female only as a condition, price should be between 300 and 500
        ClientInfo femaleTest = new ClientInfo("Lily", 'F', 50, 5, 0, "licenseNumber");
        double femaleQuotation = service.generateQuotation(femaleTest).price;
        assertTrue(femaleQuotation >= 300 && femaleQuotation < 500);

        //check if points only as a condition, take points=0, price should be between 480 and 800
        ClientInfo pointsTest = new ClientInfo("Mac", 'M', 50, 0, 0, "licenseNumber");
        double pointsQuotation = service.generateQuotation(pointsTest).price;
        assertTrue(pointsQuotation >= 480 && pointsQuotation < 800);

        //check if claims as a condition, take claims=2, price should be between 540 and 900
        ClientInfo claimsTest = new ClientInfo("Mac", 'M', 50, 5, 2, "licenseNumber");
        double claimsQuotation = service.generateQuotation(claimsTest).price;
        assertTrue(claimsQuotation >= 540 && claimsQuotation < 900);

        //check whether the method returns a Quotation object
        ClientInfo returnTest = new ClientInfo();
        assertThat(service.generateQuotation(returnTest), instanceOf(Quotation.class));
    }

}