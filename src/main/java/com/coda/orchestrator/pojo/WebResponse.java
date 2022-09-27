package com.coda.orchestrator.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author prasad
 */
@Data
@AllArgsConstructor
@Builder
public class WebResponse {

    private Date date;
    private String message;
    private String details;
    private String errorCode;
}
