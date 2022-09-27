package com.coda.orchestrator.services.Impl;

import com.coda.orchestrator.exception.InvalidValueException;
import com.coda.orchestrator.exception.NoContentFoundException;
import com.coda.orchestrator.services.IRestService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * @author prasad
 */
@Component
@Slf4j
public class DemoRestService implements IRestService {

    @Autowired
    private RestTemplate restTemplate;

    public String getPostCopyResponse(String url, String request) {
        HttpEntity<String> httpEntity = new HttpEntity<>(request, getHeaders());
        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if(responseEntity == null){
                throw new NoContentFoundException("CODA001", new Throwable("No content found"));
            }
            log.info("Response received from downstream");
            return responseEntity.getBody();
        }catch (RestClientException e){
            if(e instanceof HttpClientErrorException){
                if( HttpStatus.BAD_REQUEST ==((HttpClientErrorException) e).getStatusCode() ){
                    throw new InvalidValueException("CODA002", new Throwable("Client end error"));
                }
            }
            //server end exception
            throw new InvalidValueException("CODA003", new Throwable("Server end error"));
        }
    }

    public boolean isUp(String url){
        try{
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if(responseEntity == null){
                throw new NoContentFoundException("CODA001", new Throwable("No content found"));
            }
            log.info("Response received from downstream");
            JsonObject jsonObject = new Gson().fromJson(responseEntity.getBody(), JsonObject.class);
            return jsonObject.get("status").getAsString().equals("UP");
        }catch (RestClientException e){
            if(e instanceof HttpClientErrorException){
                if( HttpStatus.BAD_REQUEST ==((HttpClientErrorException) e).getStatusCode() ){
                    throw new InvalidValueException("CODA002", new Throwable("Client end error"));
                }
            }
            throw new InvalidValueException("CODA003", new Throwable("Server end error"));
        }
    }
    private HttpHeaders getHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}
