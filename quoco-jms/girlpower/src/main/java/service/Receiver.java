package service;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.GPQService;
import service.core.Quotation;
import service.message.*;

public class Receiver {

    // Static instance Variable Declarations
    static GPQService gpq_service_instance = new GPQService();

    public static void main(String[] args){

        // Variables for process
        String host;

        try{

            // Assigning host to host of the network
            host = args.length > 0 ? args[0] : "0.0.0.0";

            // Sets up Connection Factory on TCP port of ActiveMQ
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");

            Connection connection = factory.createConnection();
            connection.setClientID("girlpower");

            // Connection creates Session
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

            // Creating Consumer(s) and Producer(s) and their respective destinations
            Queue quotations_queue = session.createQueue("QUOTATIONS");
            Topic applications_topic = session.createTopic("APPLICATIONS");
            MessageConsumer applications_consumer = session.createConsumer(applications_topic);
            MessageProducer quotations_producer = session.createProducer(quotations_queue);

            // Start connection so messages can be transmitted across nodes
            connection.start();

            // Loop to constantly listen to Applications Topic
            while (true) {
                try{

                    // Get the next message from the APPLICATIONS topic
                    Message gpq_quotation_request_message = applications_consumer.receive();

                    // Check it is the right type of message
                    if (gpq_quotation_request_message instanceof ObjectMessage) {

                        // It’s an Object Message
                        Object gpq_quotation_request_message_content = ((ObjectMessage) gpq_quotation_request_message).getObject();

                        if (gpq_quotation_request_message_content instanceof QuotationRequestMessage) {

                            // It’s a Quotation Request Message
                            QuotationRequestMessage gpq_quotation_request = (QuotationRequestMessage) gpq_quotation_request_message_content;

                            // Generate a quotation and send a quotation response message
                            Quotation gpq_final_quotation = gpq_service_instance.generateQuotation(gpq_quotation_request.info);
                            Message gpq_final_quotation_response = session.createObjectMessage(new QuotationResponseMessage(gpq_quotation_request.id, gpq_final_quotation));
                            quotations_producer.send(gpq_final_quotation_response);

                        }
                    } else {

                        // Error Handling
                        System.out.println("Unknown message type: " +
                                gpq_quotation_request_message.getClass().getCanonicalName());

                    }
                } catch(JMSException e) {

                    // Error Handling
                    System.out.println(e.getMessage());
                }

            }


        } catch(Exception e) {

            // Error Handling
            e.printStackTrace();

        }
    }
}