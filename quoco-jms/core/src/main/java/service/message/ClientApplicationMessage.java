package service.message;
import service.core.ClientInfo;
import service.core.Quotation;
import java.io.Serializable;
import java.util.ArrayList;

public class ClientApplicationMessage implements Serializable {
    public long id;
    public ClientInfo info;
    public ArrayList<Quotation> quotations;

    public ClientApplicationMessage(long id, ClientInfo info){
        this.id = id;
        this.info = info;
        this.quotations = new ArrayList<>();
    }

}