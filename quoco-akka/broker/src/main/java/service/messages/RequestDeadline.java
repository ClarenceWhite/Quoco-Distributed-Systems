package service.messages;

public class RequestDeadline {
    private int id;

    public RequestDeadline(int id){
        this.id = id;
    }

    public RequestDeadline(){
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
