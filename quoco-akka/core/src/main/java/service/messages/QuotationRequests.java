package service.messages;

import service.core.ClientInfo;

public class QuotationRequests implements MySerializable{
    private int id;
    private ClientInfo clientInfo;

    public QuotationRequests(int id, ClientInfo clientInfo){
        this.id = id;
        this.clientInfo = clientInfo;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public ClientInfo getClientInfo(){
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo){
        this.clientInfo = clientInfo;
    }
}
