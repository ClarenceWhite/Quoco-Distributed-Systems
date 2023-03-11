package service.broker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
public class LocalBrokerService {
    //get urls from application.properties file
    @Value("#{'${server.provider_urls}'.split(',\\s*')}")
    List<String> provider_urls;

    //set a cache variable so that we can retrieve applications with 'GET' method
    HashMap<Long, Object> cache = new HashMap<>();

    //method to handle POST requests to get quotations
    @RequestMapping(value = "/applications", method = RequestMethod.POST)
    public ResponseEntity<ClientApplication> getQuotations(@RequestBody ClientApplication customer) throws URISyntaxException{
        //new a RestTemplate class instance
        RestTemplate restTemplate = new RestTemplate();

        //set the ClientApplication to http post request body
        HttpEntity<ClientInfo> request = new HttpEntity<>(customer.getInfo());

        //create a temp quotations ArrayList
        ArrayList<Quotation> temp_quotations = new ArrayList<>();

        //loop over three services and get their quotations, change the quotation attribute of customer from
        // empty ArrayList to an ArrayList with three lines of quotation results
        for (String provider_url: provider_urls) {
            //send a request to one of the three services, get its quotation
            Quotation quotation = restTemplate.postForObject(provider_url, request, Quotation.class);
            //add quotation to a temp quotations ArrayList
            temp_quotations.add(quotation);
        }
        //update 'quotations' attribute of customer
        customer.setQuotations(temp_quotations);
        //check if the id is in cache, if not, put the id along with its response to cache
        if(!cache.containsKey(customer.getApplicationId())) {
            cache.put(customer.getApplicationId(), customer);
        }

        //return
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/applications/";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(path));
        return new ResponseEntity<>(customer, headers, HttpStatus.CREATED);
    }

    //method to handle GET request with specified application ids
    @RequestMapping(value = "/applications/{application_id}", method = RequestMethod.GET)
    public ResponseEntity<ClientApplication> getApplicationById(@PathVariable("application_id") long application_id) throws URISyntaxException{
        ClientApplication response = new ClientApplication();

        //check if the id in url exists in cache, if yes, retrieve the record from cache
        if(!cache.containsKey(application_id)) {
            System.out.println("ID NOT FOUND!");
            return null;
        }else {
            System.out.println("ID FOUND!");
            response = (ClientApplication) cache.get(application_id);
        }

        //return
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/applications/";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(path));
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    //method to handle GET request which returns all the applications
    @RequestMapping(value = "/applications/", method = RequestMethod.GET)
    public ResponseEntity<ArrayList> getAllApplications() throws URISyntaxException{
        ArrayList<ClientApplication> response = new ArrayList<>();

        //loop over cache, put all stuff from cache to the ArrayList
        for(long key : cache.keySet()) {
            response.add((ClientApplication) cache.get(key));
        }

        //return
        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/applications/";
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(path));
        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }


}

