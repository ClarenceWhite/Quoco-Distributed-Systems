package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Init;
import service.actor.Quoter;
import service.girlpower.GPQService;

public class Main {
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create(); //create a system
        ActorRef ref = system.actorOf(Props.create(Quoter.class), "girlpower"); //create an actor ref
        ref.tell(new Init(new GPQService()), null); //send a message asynchronously to initialize a service object
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker"); //select actor interested
        selection.tell("register", ref); //tell the actor "I want to register with you"
    }
}
