package com.coda.orchestrator.services.Impl;

import com.coda.orchestrator.exception.NoDownStreamFoundException;
import com.coda.orchestrator.services.IRestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Initializes routerURL and schedules heartbeat,
 * The downstream URLs are init during bootUp and subsequent heartBeat is scheduled based on cron Pattern
 *
 * @author prasad
 */
@Service
@Slf4j
@EnableScheduling
public class DemoRouterService {

    @Autowired
    IRestService demoRestService;

    @Value("${demoservice.url.prefix}")
    private String demoServicePrefixUrl;
    @Value("${demoservice.url.ports}")
    private String demoServicePorts;

    @Value("${demoservice.slowThreshold.times}")
    private Integer slowThresholdTimes;

    private volatile static Deque<String> routerUrls = new ArrayDeque<>();

    private static final Map<String, Integer> slowEndPoints = new HashMap<>();

    @PostConstruct
    private void initValidDownstream(){
        //fill the routerURL
        if(StringUtils.isNotBlank(demoServicePorts)){
            Arrays.stream(demoServicePorts.split("::")).forEach(currPort -> {
                String healthURLByPort = getdemoServiceHealthURLByPort(currPort);
                String demoServiceURL = getDemoServiceURL(currPort);
                try{
                    boolean isServiceUp = demoRestService.isUp(healthURLByPort);
                    if(isServiceUp){
                        routerUrls.add(demoServiceURL);
                    }
                }catch (Exception e){
                    log.error("URL is unreachable: {}", healthURLByPort);
                }
            });
        }
        log.debug("Demo Service URLs : {}", routerUrls);
    }

    public String getDemoServiceURL(String currPort) {
        return demoServicePrefixUrl + currPort;
    }

    //do a hearBeat with downstream based on cron pattern.
    @Scheduled(fixedDelay =  30000, initialDelay = 0)
    public void heartBeat(){
        log.info("HeartBeat started");
        Iterator<String> iterator = routerUrls.iterator();
        while (iterator.hasNext()){
            String currURLprefix = iterator.next();
            try{
                boolean isUp = demoRestService.isUp(currURLprefix + "/actuator/health");
                log.debug("URL {} status : {}", currURLprefix, isUp);
                if(!isUp){
                    iterator.remove();
                }
            }catch (Exception e){
                log.error("URL is unreachable : {}", currURLprefix);
                iterator.remove();
            }
        }
        log.info("HeartBeat ended with routerUrl {}", routerUrls);
    }

    private String getdemoServiceHealthURLByPort(String port){
        return demoServicePrefixUrl + port + "/actuator/health";
    }

    //method is synchronized so only single thread will be able to access it.
    public synchronized String getCurrentDemoServiceURL(){
        if(CollectionUtils.isEmpty(routerUrls)){
            throw new NoDownStreamFoundException("CODA005", new Throwable("No DemoServiceURL found"));
        }
        String currDemoServiceUrl = routerUrls.removeFirst();
        if(StringUtils.isBlank(currDemoServiceUrl)){
            throw new NoDownStreamFoundException("CODA005", new Throwable("No DemoServiceURL found"));
        }
        routerUrls.addLast(currDemoServiceUrl);
        return currDemoServiceUrl;
    }

    public synchronized void removeURL(String preFixURL) {
        routerUrls.removeFirstOccurrence(preFixURL);
    }

    public void thresholdBreached(String url){
        if(!slowEndPoints.containsKey(url))
            slowEndPoints.put(url, 1);
        else{
            slowEndPoints.put(url, slowEndPoints.get(url)+1);
        }
        if(slowEndPoints.get(url) > slowThresholdTimes){
            synchronized (routerUrls){
                routerUrls.removeFirstOccurrence(url);
                slowEndPoints.remove(url);
            }
        }


    }


}
