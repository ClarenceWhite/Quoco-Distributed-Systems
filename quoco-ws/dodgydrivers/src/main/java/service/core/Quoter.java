package service.core;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
import java.net.InetAddress;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, use = SOAPBinding.Use.LITERAL)
public class Quoter extends AbstractQuotationService {
    // All references are to be prefixed with an AF (e.g. AF001000)
    public static final String PREFIX = "DD";
    public static final String COMPANY = "Dodgy Drivers Corp.";

    /**
     * Quote generation:
     * 30% discount for being male
     * 2% discount per year over 60
     * 20% discount for less than 3 penalty points
     * 50% penalty (i.e. reduction in discount) for more than 60 penalty points
     */

    @WebMethod
    public Quotation generateQuotation(ClientInfo info){
        // Create an initial quotation between 800 and 1000
        double price = generatePrice(800, 200);

        // 5% discount per penalty point (3 points required for qualification)
        int discount = (info.points > 3) ? 5 * info.points : -50;

        // Add a no claims discount
        discount += getNoClaimsDiscount(info);

        // Generate the quotation and send it back
        return new Quotation(COMPANY, generateReference(PREFIX), (price * (100 - discount)) / 100);
    }

    private int getNoClaimsDiscount(ClientInfo info){
        return 10 * info.noClaims;
    }

    public static void main(String[] args){
        String host = "dodgydrivers";
        if (args.length > 0) {
            host = args[0];
        }
        Endpoint.publish("http://0.0.0.0:9002/quotation", new Quoter());
        jmDNSAdvert(host);
    }


    private static void jmDNSAdvert(String host){
        try{
            String path = "path=http://" + host + ":9002/quotation?wsdl";
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "ws-service", 9002, path);
            jmdns.registerService(serviceInfo);

            //we need to let this thread sleep for a long time, so that the registered service will not get offline
            Thread.sleep(100000000);
            Thread.sleep(100000000);
            jmdns.unregisterAllServices();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}