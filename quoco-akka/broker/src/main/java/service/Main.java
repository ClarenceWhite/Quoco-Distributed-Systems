package service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Broker;

public class Main {
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create(); //create a system
        ActorRef ref = system.actorOf(Props.create(Broker.class), "broker"); //create actor ref
    }
}
