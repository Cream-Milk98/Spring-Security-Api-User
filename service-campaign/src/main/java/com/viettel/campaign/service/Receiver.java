//package com.viettel.campaign.service;
//
//import org.springframework.kafka.annotation.KafkaListener;
//
//import java.util.concurrent.CountDownLatch;
//
///**
// * @author hanv_itsol
// * @project campaign
// */
//public class Receiver {
//
//    private CountDownLatch latch = new CountDownLatch(1);
//
//    @KafkaListener(topics = "hanv")
//    public void receive(String payload) {
//        latch.countDown();
//    }
//}
