package com.viettel.campaign.web.rest.testconnection;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;
import com.viettel.campaign.utils.DataUtil;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

//import com.viettel.campaign.web.rest.CustomerController;
//import org.apache.log4j.Logger;

@Controller
@RequestMapping("/checkConnection/")
public class ConnectionController implements Discriminator<ILoggingEvent> {
//    private static final Logger logger = Logger.getLogger(ConnectionController.class);

//    private static final Logger logger = LogManager.getLogger(ConnectionController.class);

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConnectionController.class);
    private String userName;

    @GetMapping(path = "/test")
    @ResponseBody
    public ResponseEntity listAllCustomer(@RequestParam("userName") String userName, HttpServletRequest request) {
        Map result = new HashMap();
        String ip = DataUtil.getClientIpAddr(request).getHostAddress();
        Thread.currentThread().setName(ip);
        logger.info("user check Connection: " + userName + " request " + DataUtil.getClientIpAddr(request));
//        logger.error("Test Log Error");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    private static final String KEY = "threadName";

    private boolean started;

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return Thread.currentThread().getName();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    public boolean isStarted() {
        return started;
    }
}
