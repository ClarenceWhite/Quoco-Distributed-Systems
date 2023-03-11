package service.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class ClientApplication implements Serializable {
    private long applicationId;
    private ClientInfo info;
    private ArrayList<Quotation> quotations;

    public ClientApplication(){};

    public ClientApplication(long applicationId, ClientInfo info){
        this.applicationId = applicationId; //applicationId in the instance
        this.info = info; //ClientInfo in the instance
        this.quotations = new ArrayList<>(); //create an empty ArrayList when new a class instance
    }

    public long getApplicationId(){
        return applicationId;
    }

    public void setApplicationId(long applicationId){
        this.applicationId = applicationId;
    }

    public ClientInfo getInfo(){
        return info;
    }

    public void setInfo(ClientInfo info){
        this.info = info;
    }

    public ArrayList<Quotation> getQuotations(){
        return quotations;
    }

    public void setQuotations(ArrayList<Quotation> quotations){
        this.quotations = quotations;
    }
}
