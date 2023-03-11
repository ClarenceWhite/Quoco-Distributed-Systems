package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Client;

public class Main {
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create(); //create a system
        ActorRef ref = system.actorOf(Props.create(Client.class), "client"); //create an actor ref for client
        ActorSelection selection = system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker"); //select actor interested
        selection.tell("I am client", ref); //tell the broker actor "I am client"
    }
}
