package service.actor;

import service.dodgydrivers.DDQService;

public class Init {
    //only one field which is an instance of DDQService
    private DDQService service;
    //constructor
    public Init(DDQService service){
        this.service = service;
    }
    //getter
    public DDQService getService(){
        return service;
    }
    //setter
    public void setService(DDQService service){
        this.service = service;
    }
}
