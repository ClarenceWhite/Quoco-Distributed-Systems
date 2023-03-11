package service.actor;

import akka.actor.AbstractActor;

import service.core.Quotation;
import service.core.QuotationService;
import service.messages.QuotationRequests;
import service.messages.QuotationResponse;

public class Quoter extends AbstractActor {
    private QuotationService service;

    //auldfellas will receive the requests from a sender, and then send response back to the sender.
    @Override
    public Receive createReceive(){
        return receiveBuilder()
            //first handler
            .match(QuotationRequests.class, //check if the msg is an instance of QuotationRequests
            msg -> {
                //generate quotation
                Quotation quotation = service.generateQuotation(msg.getClientInfo());
//                System.out.println(quotation.getReference());
                //get request sender, which may be the broker or someone else, tell the sender the response
                getSender().tell(new QuotationResponse(msg.getId(), quotation), getSelf());
                }
            )
            //second handler
            .match(Init.class, //check if the service is an instance of Init class
            msg -> {
                //initialize service to AFQService
                service = msg.getService();
            }
            ).build();
    }

}

