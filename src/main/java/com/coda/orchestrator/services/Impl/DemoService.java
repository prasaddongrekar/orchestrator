package com.coda.orchestrator.services.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author prasad
 */
@Service
@Slf4j
public class DemoService {

    @Autowired
    private DemoRestService demoRestService;

    @Autowired
    private DemoRouterService demoRouterService;

    private static final long thresholdResponseLimit = 100;

    public String postToDemoService(String request){


        //get round robin endPoint
        String preFixURL = demoRouterService.getCurrentDemoServiceURL();
        log.info("*************DemoService URL****************** : {}",preFixURL);
        try{
            long startTime = System.currentTimeMillis();
            String postCopyResponse = demoRestService.getPostCopyResponse(preFixURL + "/v1", request);
            //mark url as threshold breached if response is delayed
            if( System.currentTimeMillis() - startTime > 0){
                demoRouterService.thresholdBreached(preFixURL);
            }
            return postCopyResponse;
        }catch (Exception e){
            log.error("Exception caught, removing url from rotation : ", preFixURL, e);
            demoRouterService.removeURL(preFixURL);
            throw e;

        }
    }

    public String test(){
        return "test good";
    }
}
