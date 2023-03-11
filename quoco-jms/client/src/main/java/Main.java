import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientApplicationMessage;
import service.message.QuotationRequestMessage;

import javax.jms.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


public class Main {

    private static long SEED_ID = 0;
    private static Map<Long, ClientInfo> cache = new HashMap<>();

    public static void main(String[] args) throws Exception{

        String host = args.length > 0 ? args[0] : "localhost";
        System.out.println("ip address is:" + host);
        int port = 61616;

        // Sets up connection to MQ
        ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port);
        Connection connection = factory.createConnection();
        connection.setClientID("client");
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue requests_queue = session.createQueue("REQUESTS");
        Queue responses_queue = session.createQueue("RESPONSES");

        MessageProducer request_producer = session.createProducer(requests_queue);
        MessageConsumer response_consumer = session.createConsumer(responses_queue);

        connection.start();

        //listen to response queue
        new Thread(() -> {
            while (true) {
                try{
                    // Get message from response queue
                    Message final_quotation_message = response_consumer.receive();
                    if (final_quotation_message instanceof ObjectMessage) {
                        Object content = ((ObjectMessage) final_quotation_message).getObject();
                        if (content instanceof ClientApplicationMessage) {
                            ClientApplicationMessage final_quotation_response = (ClientApplicationMessage) content;
                            ClientInfo info = cache.get(final_quotation_response.id);
                            displayProfile(info);

                            for (Quotation quotation : final_quotation_response.quotations) {
                                displayQuotation(quotation);
                            }
                            System.out.println("\n");
                        }
                        // Acknowledge
                        final_quotation_message.acknowledge();
                    } else {
                        System.out.println("Unknown message type: " +
                                final_quotation_message.getClass().getCanonicalName());
                    }

                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        }).start();

        //send request
        for (ClientInfo info : clients) {
            try{
                QuotationRequestMessage initial_quotation_request = new QuotationRequestMessage(SEED_ID++, info);
                Message request = session.createObjectMessage(initial_quotation_request);
                cache.put(initial_quotation_request.id, initial_quotation_request.info);
                request_producer.send(request);
            } catch(Exception e) {
                System.out.println(e);
            }

        }
        connection.start();
    }


    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info){
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.name) +
                        " | Gender: " + String.format("%1$-27s", (info.gender == ClientInfo.MALE ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.age) + " |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.licenseNumber) +
                        " | No Claims: " + String.format("%1$-24s", info.noClaims + " years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.points) + " |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation){
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.company) +
                        " | Reference: " + String.format("%1$-24s", quotation.reference) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.price)) + " |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.MALE, 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.FEMALE, 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.MALE, 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.MALE, 35, 5, 2, "XYZ567/9")
    };
}