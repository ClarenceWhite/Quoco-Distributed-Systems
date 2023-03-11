package dodgydrivers;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import service.BrokerService;
import service.Constants;
import service.QuotationService;

public class Server {
    public static void main(String[] args){
        QuotationService ddqService = new DDQService();
        try{
            // Connect to the RMI Registry - creating the registry will be the responsibility of the broker.

            Registry registry = null;
            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                String host = args[0];
                registry = LocateRegistry.getRegistry(host, 1099);
            }

            // Create the Remote Object
            QuotationService quotationService = (QuotationService) UnicastRemoteObject.exportObject(ddqService, 0);

            // Register the object with the RMI Registry
            BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
            brokerService.runBind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            System.out.println("Trouble in dodgydrivers.Server" + e);
        }
    }
}