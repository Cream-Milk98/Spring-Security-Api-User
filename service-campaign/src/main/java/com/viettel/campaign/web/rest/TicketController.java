package com.viettel.campaign.web.rest;

import com.viettel.campaign.service.TicketService;
import com.viettel.campaign.service.impl.ApParamServiceImpl;
import com.viettel.campaign.web.dto.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ipcc/ticket")
@CrossOrigin
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);
    @Autowired
    TicketService ticketService;

    @GetMapping("/getHistory")
    @ResponseBody
    public ResponseEntity searchCampaignExecute(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize, @RequestParam("sort") String sort, @RequestParam("customerId") String customerId, @RequestParam("timezone") Long timezone) {
        ResultDTO result = ticketService.getHistory(page, pageSize, sort, customerId, timezone);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
