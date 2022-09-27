package com.coda.orchestrator.services;

/**
 * @author prasad
 */
public interface IRestService {
    String getPostCopyResponse(String url, String request);
    boolean isUp(String url);

}
