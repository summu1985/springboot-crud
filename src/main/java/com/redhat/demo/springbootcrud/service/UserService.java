package com.redhat.demo.springbootcrud.service;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.demo.springbootcrud.model.User;
import com.redhat.demo.springbootcrud.model.Users;

@Component
public class UserService {
    @Value( "${backend.getAllAPI.url}" )
    private String getAllAPIURL;
    //http://localhost:8080/camel/opportunity

    public UserService() {
    }

    // public String addOpportunity(Opportunity opp) {
    //     String responseOppId = null;

    //     RestTemplate restTemplate = new RestTemplate();
    //     String result = restTemplate.postForObject(getAllAPIURL, opp, String.class);
    //     NewOpportunityResponse response = null;
    //     try {
    //         ObjectMapper mapper = new ObjectMapper();
    //         mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    //         response = mapper.readValue(result, NewOpportunityResponse.class);
    //         responseOppId = response.getId();
    //     } catch (Exception ex) {
    //         System.out.println(ex.toString());
    //     }

    //     System.out.println("Response from Fuse : " + response);;

    //     return responseOppId;
    // }
    public ArrayList<User> findAll() {
        RestTemplate restTemplate = new RestTemplate();
        String usersResponse = restTemplate.getForObject(getAllAPIURL,String.class);
        System.out.println("Recieved response from backend service : " + usersResponse);
        //Users users = null;
        User[] userList = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            userList = mapper.readValue(usersResponse, User[].class);
            //userList = users.getRecords();
           // ArrayList<User> users = userList;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return new ArrayList<User>(Arrays.asList(userList));
    }
}
