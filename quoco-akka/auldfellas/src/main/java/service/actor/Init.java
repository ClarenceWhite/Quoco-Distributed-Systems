package service.actor;

import service.auldfellas.AFQService;

public class Init {
    //only one field which is an instance of AFQService
    private AFQService service;
    //constructor
    public Init(AFQService service){
        this.service = service;
    }
    //getter
    public AFQService getService(){
        return service;
    }
    //setter
    public void setService(AFQService service){
        this.service = service;
    }
}
