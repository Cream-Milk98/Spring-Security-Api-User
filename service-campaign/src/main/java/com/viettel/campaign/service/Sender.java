//package com.viettel.campaign.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//
///**
// * @author hanv_itsol
// * @project campaign
// */
//public class Sender {
//
//    @Autowired
//    private KafkaTemplate<String, String> simpleKafkaTemplate;
//
//    public void send(String topic, String payload) {
//        simpleKafkaTemplate.send(topic, payload);
//    }
//}
