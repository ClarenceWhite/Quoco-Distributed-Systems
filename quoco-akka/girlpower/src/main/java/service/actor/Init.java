package service.actor;

import service.girlpower.GPQService;

public class Init {
    //only one field which is an instance of GPQService
    private GPQService service;
    //constructor
    public Init(GPQService service){
        this.service = service;
    }
    //getter
    public GPQService getService(){
        return service;
    }
    //setter
    public void setService(GPQService service){
        this.service = service;
    }
}
