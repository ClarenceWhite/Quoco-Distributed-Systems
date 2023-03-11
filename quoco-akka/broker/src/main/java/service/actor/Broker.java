package service.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;
import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Broker extends AbstractActor {
    //create an id generator for each request from client
    static int id = 0;
    //create a list for maintaining actorRefs, this list contains all services refs
    private ArrayList<ActorRef> actorRefs = new ArrayList<>();
    //response cache to store responses
    private HashMap<Integer, ArrayList<Quotation>> response_cache = new HashMap<>();
    //request cache to keep track of requests
    private HashMap<Integer, ClientInfo> request_cache = new HashMap<>();
    //client Ref
    private ActorRef clientRef;



    @Override
    public Receive createReceive(){
        return receiveBuilder()
                //handler for service registration
                .match(String.class,
                        msg -> {
                            //if the msg is not used to register a service with broker, return
                            if (msg.equals("register")) {
                                // else find the sender(service), add it to the Ref list
                                actorRefs.add(getSender());
                                //print the registered service name out
                                System.out.println("\n==========\n" + "SERVICE REGISTERED:" + getSender() + "\n==========\n");
                                //send a quotation request to the service (testing purpose)
//                        getSender().tell(new QuotationRequests(1, new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1")), getSelf());
                            } else if (msg.equals("I am client")) {
                                clientRef = getSender();
                                System.out.println("\n==========\n" + "CLIENT REGISTERED:" + getSender() + "\n==========\n");
                                //tell the client actor "please start sending requests to me"
                                clientRef.tell("please start", getSelf());
                            } else {
                                return;
                            }
                        })
                // handler for ApplicationRequest from client
                .match(ApplicationRequest.class,
                        applicationRequest -> {
                            //increment id
                            id = id + 1;
                            //new a QuotationRequests instance
                            QuotationRequests quotationRequest = new QuotationRequests(id, applicationRequest.getClientinfo());
                            //put this request into cache pool to make it trackable
                            if (!response_cache.containsKey(id)) {
                                response_cache.put(id, new ArrayList<Quotation>());
                                System.out.println("RESPONSE CACHE: " + response_cache);
                                request_cache.put(id, applicationRequest.getClientinfo());
                                System.out.println("REQUEST CACHE: " + response_cache);
                            }
                            //loop over three services, send request to them respectively
                            for (ActorRef serviceRef : actorRefs) {
                                //use broker itself as the sender, associate an id with the request from client,send QuotationRequests to services
                                serviceRef.tell(quotationRequest, getSelf());
                            }
                            //RequestDeadline for the request
                            getContext().system().scheduler().scheduleOnce(
                                    Duration.create(2, TimeUnit.SECONDS),
                                    getSelf(),
                                    new RequestDeadline(id),
                                    getContext().dispatcher(), null
                            );
                        }
                )
                //handler for QuotationResponse from services
                .match(QuotationResponse.class,
                        quotationResponse -> {
                            //print out id received
                            System.out.println("GOT QUOTATION FOR ID:" + quotationResponse.getId());
                            //update the QuotationResponse List in cache under a single request id
                            response_cache.get(quotationResponse.getId()).add(quotationResponse.getQuotation());
                            System.out.println("UPDATING QUOTATION LIST.... " + response_cache);
                        }
                )
                //handler for RequestDeadline
                .match(RequestDeadline.class,
                        requestDeadline -> {
                            if (!response_cache.containsKey(requestDeadline.getId())) {
                                return;
                            } else {
                                ApplicationResponse responseToClient =
                                        new ApplicationResponse(request_cache.get(requestDeadline.getId()), response_cache.get(requestDeadline.getId()));
                                //send applicationResponse to client
                                clientRef.tell(responseToClient, getSelf());
                            }
                        }
                ).build();
    }
}
