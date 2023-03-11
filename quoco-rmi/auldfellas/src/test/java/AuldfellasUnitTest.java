import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import service.ClientInfo;
import service.Constants;
import service.Quotation;
import service.QuotationService;
import auldfellas.AFQService;
import org.junit.*;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class AuldfellasUnitTest {
    private static Registry registry;

    @BeforeClass
    public static void setup(){
        service.QuotationService afqService = new AFQService();
        try{
            registry = LocateRegistry.createRegistry(1099);
            service.QuotationService quotationService = (service.QuotationService)
                    UnicastRemoteObject.exportObject(afqService, 0);
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
        QuotationService service = new AFQService();

        //check if male only as a condition, price should be between 420 and 840
        ClientInfo maleTest = new ClientInfo("Tom", 'M', 50, 6, 0, "licenseNumber");
        double maleQuotation = service.generateQuotation(maleTest).price;
        assertTrue(maleQuotation >= 420 && maleQuotation < 840);

        //check if age only as a condition, take 70 years old, price should be between 480 and 960
        ClientInfo ageTest = new ClientInfo("Amy", 'F', 70, 6, 0, "licenseNumber");
        double ageQuotation = service.generateQuotation(ageTest).price;
        assertTrue(ageQuotation >= 480 && ageQuotation < 960);

        //check if points only as a condition, take points=8, price should be between 900 and 1800
        ClientInfo pointsTest = new ClientInfo("Lily", 'F', 50, 8, 0, "licenseNumber");
        double pointsQuotation = service.generateQuotation(pointsTest).price;
        assertTrue(pointsQuotation >= 900 && pointsQuotation < 1800);

        //check whether the method returns a Quotation object
        ClientInfo returnTest = new ClientInfo();
        assertThat(service.generateQuotation(returnTest), instanceOf(Quotation.class));;
    }

}