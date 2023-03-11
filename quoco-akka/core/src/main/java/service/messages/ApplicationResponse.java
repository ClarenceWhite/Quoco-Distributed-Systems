package service.messages;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.ArrayList;

public class ApplicationResponse implements MySerializable{
    private ClientInfo clientInfo;
    private ArrayList<Quotation> quotations;

    public ApplicationResponse(ClientInfo clientInfo, ArrayList<Quotation> quotations){
        this.clientInfo = clientInfo;
        this.quotations = quotations;
    }

    public ApplicationResponse(){
    }

    public ClientInfo getClientInfo(){
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo){
        this.clientInfo = clientInfo;
    }

    public ArrayList<Quotation> getQuotations(){
        return quotations;
    }

    public void setQuotations(ArrayList<Quotation> quotations){
        this.quotations = quotations;
    }
}
