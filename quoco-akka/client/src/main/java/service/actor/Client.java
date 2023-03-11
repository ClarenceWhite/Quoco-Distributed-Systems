package service.actor;

import akka.actor.AbstractActor;
import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;

import java.text.NumberFormat;

public class Client extends AbstractActor {

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                //handler for sending requests to broker
                .match(String.class,
                        msg -> {
                            if (!msg.equals("please start")) {
                                return;
                            } else {
                                System.out.println("RECEIVED SIGN FROM BROKER!");
                                for (ClientInfo info : clients) {
                                    ApplicationRequest request = new ApplicationRequest(info);
                                    getSender().tell(request, getSelf());
                                    Thread.sleep(1000);
                                }
                            }
                        }
                )
                //handler for ApplicationResponse from broker
                .match(ApplicationResponse.class,
                        applicationResponse -> {
                            //display initial ClientInfo
                            displayProfile(applicationResponse.getClientInfo());
                            //display quotations
                            for (Quotation quotation : applicationResponse.getQuotations()) {
                                displayQuotation(quotation);
                            }
                            System.out.println("\n***************Divider***************\n\n\n");
                        }
                ).build();
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

