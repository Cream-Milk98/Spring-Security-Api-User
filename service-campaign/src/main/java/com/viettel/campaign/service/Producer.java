//package com.viettel.campaign.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
///**
// * @author hanv_itsol
// * @project service-campaign
// */
//
//@Service
//public class Producer {
//
//    private static final String TOPIC = "TestTopic";
//    private static final String TOPIC2 = "TestTopic2";
//
//
//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    public void sendMessage(String message){
//        this.kafkaTemplate.send(TOPIC, "key1", message);
//    }
//
//    public void sendMessageTopic2(String message){
//        this.kafkaTemplate.send(TOPIC2, "key2", message);
//    }
//
//}
