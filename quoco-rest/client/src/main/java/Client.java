import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class Client {

    public static void main(String[] args) throws InterruptedException{
        //RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        //generate id for each client
        long application_id = 0;
        //a map to store all ClientApplication instances
        HashMap<Long, ClientApplication> application_pool = new HashMap<>();

        //loop over clients, new a ClientApplication instance for each client, store it in the Map, use id as key
        for(ClientInfo client : clients) {
            ClientApplication temp_app = new ClientApplication(application_id++, client);
            application_pool.put(temp_app.getApplicationId(), temp_app);
        }

        //loop over the map, send post request to broker, and get response
        for(ClientApplication application : application_pool.values()) {
            HttpEntity<ClientApplication> request = new HttpEntity<>(application);
            //send POST request and get feedback
            ClientApplication response =
                    restTemplate.postForObject("http://localhost:8080/applications/", request, ClientApplication.class);
            //get the quotations list from feedback object
            ArrayList<Quotation> quotations = response.getQuotations();
            //display ClientInfo
            displayProfile(response.getInfo());
            //display quotation related to the ClientInfo
            for (Quotation quotation : quotations) {
                displayQuotation(quotation);
            }
            //sleep for 2 seconds
            Thread.sleep(2000);
        }

    }



    public static void displayProfile(ClientInfo info){
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.getName()) +
                        " | Gender: " + String.format("%1$-27s", (info.getGender() == ClientInfo.MALE ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.getAge()) + " |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) +
                        " | No Claims: " + String.format("%1$-24s", info.getNoClaims() + " years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.getPoints()) + " |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    public static void displayQuotation(Quotation quotation){
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.getCompany()) +
                        " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice())) + " |");
        System.out.println("|=================================================================================================================|");
    }

    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };


}

