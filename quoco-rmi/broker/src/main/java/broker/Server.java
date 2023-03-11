package broker;

import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import service.BrokerService;
import service.Constants;

public class Server {
    public static void main(String[] args){
        LocalBrokerService localBrokerService = new LocalBrokerService();
        try{
            Registry registry = null;

            if (args.length == 0) {
                registry = LocateRegistry.createRegistry(1099);
            } else {
                registry = LocateRegistry.getRegistry();
            }

            //create the Remote object
            BrokerService brokerService = (BrokerService) UnicastRemoteObject.exportObject(localBrokerService, 0);

            // Register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brokerService);
            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch(Exception e) {
            System.out.println("Trouble in broker.Server:" + e);
        }
    }
}