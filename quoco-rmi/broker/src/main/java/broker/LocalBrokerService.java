package broker;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.server.ExportException;
import java.util.LinkedList;
import java.util.List;

import service.BrokerService;
import service.ClientInfo;
import service.Quotation;
import service.QuotationService;
import java.rmi.registry.*;


public class LocalBrokerService implements BrokerService{

    public List<Quotation> getQuotations(ClientInfo info){
        List<Quotation> quotations = new LinkedList<Quotation>();

        try{
            Registry registry = null;
            registry = LocateRegistry.getRegistry(1099);

			for (String name : registry.list()) {
				if (name.startsWith("qs-")) {
					QuotationService service = (QuotationService) registry.lookup(name);
					quotations.add(service.generateQuotation(info));
				}
			}
        } catch(Exception e) {
            System.out.println(e);
        }

        return quotations;
    }

    @Override
    public void runBind (String name, Remote service) {
        try{
            Registry registry = null;
            registry = LocateRegistry.getRegistry();
            registry.bind(name, service);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
