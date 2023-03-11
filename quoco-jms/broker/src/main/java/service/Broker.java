package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.message.*;

import javax.jms.*;
import java.util.HashMap;


public class Broker {

    static HashMap<Long, ClientApplicationMessage> cache = new HashMap<>();

    public static void main(String[] args){
        try{
            // Variables for process
            String host = args.length > 0 ? args[0] : "localhost";
            int port = 61616;

            //set up connection to MQ
            ConnectionFactory factory = new ActiveMQConnectionFactory("failover://tcp://" + host + ":" + port);
            Connection connection = factory.createConnection();
            connection.setClientID("broker");
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            //create queue and topics
            Queue RequestsQueue = session.createQueue("REQUESTS");
            Queue ResponsesQueue = session.createQueue("RESPONSES");
            Queue QuotationsQueue = session.createQueue("QUOTATIONS");
            Topic ApplicationsTopic = session.createTopic("APPLICATIONS");
            //create consumers and producers
            MessageConsumer requests_consumer = session.createConsumer(RequestsQueue);
            MessageProducer responses_producer = session.createProducer(ResponsesQueue);
            MessageProducer applications_producer = session.createProducer(ApplicationsTopic);
            MessageConsumer quotations_consumer = session.createConsumer(QuotationsQueue);

            connection.start();

            new Thread(() -> {
                while (true) {
                    try{
                        // Get message from Requests queue
                        Message received_request_message = requests_consumer.receive();

                        if (received_request_message instanceof ObjectMessage) {

                            Object content = ((ObjectMessage) received_request_message).getObject();

                            if (content instanceof QuotationRequestMessage) {

                                QuotationRequestMessage quotation_request = (QuotationRequestMessage) content;
                                Message quotation_response_message = session.createObjectMessage(quotation_request);

                                if (!cache.containsKey(quotation_request.id)) {
                                    cache.put(quotation_request.id, new ClientApplicationMessage(quotation_request.id, quotation_request.info));
                                }

                                // Send quotation request to applications topic
                                applications_producer.send(quotation_response_message);
//
                                // Wait for seconds
                                Thread.sleep(10000);

                                // Send Client Application Message back to Response Queue
                                Message final_quotation_response_sent = session.createObjectMessage(cache.get(quotation_request.id));
                                responses_producer.send(final_quotation_response_sent);
                            }

                            // Acknowledge
                            received_request_message.acknowledge();

                        } else {
                            System.out.println("Unknown received_quotation_response type: " + received_request_message.getClass().getCanonicalName());
                        }

                    } catch(Exception e) {
                        System.out.println(e);
                    }
                }
            }).start();

            //Thread for response
            new Thread(() -> {
                while (true) {
                    try{
                        // Get message from the Quotations queue
                        Message received_quotation_response = quotations_consumer.receive();
                        if (received_quotation_response instanceof ObjectMessage) {
                            Object content = ((ObjectMessage) received_quotation_response).getObject();
                            if (content instanceof QuotationResponseMessage) {
                                QuotationResponseMessage quotation_response_object = (QuotationResponseMessage) content;
                                if (cache.get(quotation_response_object.id) != null) {
                                    ClientApplicationMessage temporary_client_application_message = cache.get(quotation_response_object.id);
                                    temporary_client_application_message.quotations.add(quotation_response_object.quotation);
                                }
                            }
                            // Acknowledge
                            received_quotation_response.acknowledge();

                        } else {
                            System.out.println("Unknown received_quotation_response type: " +
                                    received_quotation_response.getClass().getCanonicalName());
                        }

                    } catch(Exception e) {
                        System.out.println(e);
                        System.out.println(e.getMessage());
                    }
                }

            }).start();

        } catch(Exception e) {
            System.out.println("ERROR: " + e);
        }
    }

}