package service.messages;

import service.core.ClientInfo;

public class ApplicationRequest implements MySerializable{
    private ClientInfo clientinfo;

    public ApplicationRequest(ClientInfo clientinfo){
        this.clientinfo = clientinfo;
    }

    public ApplicationRequest(){
    }

    public ClientInfo getClientinfo(){
        return clientinfo;
    }

    public void setClientinfo(ClientInfo clientinfo){
        this.clientinfo = clientinfo;
    }
}