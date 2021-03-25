package com.viettel.campaign.job;

import com.viettel.campaign.model.ccms_full.*;
import com.viettel.campaign.repository.ccms_full.ReceiveCustLogRepository;
import com.viettel.campaign.repository.ccms_full.ReceiveCustLogRepositoryCustom;
import com.viettel.campaign.repository.ccms_full.UserActionLogRepository;
import com.viettel.campaign.service.*;
import com.viettel.campaign.utils.DateTimeUtil;
import com.viettel.campaign.utils.TimeZoneUtils;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author hanv_itsol
 */

@Configuration
@EnableScheduling
public class CampaignJob {

    private static final Logger logger = LoggerFactory.getLogger(CampaignJob.class);


    //private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final String CUSTOMER_INACTIVE_DUARATION = "CUSTOMER_INACTIVE_DUARATION";
    private static final String CRON_EXPRESSION_CHECK_START = "CRON_EXPRESSION_CHECK_START";
    private static final String CRON_EXPRESSION_CHECK_END = "CRON_EXPRESSION_CHECK_END";
    private static final String CRON_EXPRESSION_UPDATE_RECEIVE_CUST_LOG = "CRON_EXPRESSION_UPDATE_RECEIVE_CUST_LOG";

    @Autowired
    private ProcessConfigService processConfigService;

    @Autowired
    private CampaignService campaignService;

    @Autowired
    private CustomerTimeService customerTimeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserActionLogService userActionLogService;

    @Autowired
    private UserActionLogRepository userActionLogRepository;

    @Autowired
    private ReceiveCustLogRepository receiveCustLogRepository;

    @Autowired
    private ReceiveCustLogRepositoryCustom receiveCustLogRepositoryCustom;

//    @Scheduled(fixedRate = 5000)
    //@Transactional( propagation = Propagation.REQUIRED)
    @SchedulerLock(name = "CampaignJob-New", lockAtLeastFor = 15, lockAtMostFor = 20)
    public void process() {
        //log.info(Thread.currentThread().getName() + " The Task executed at " + dateFormat.format(new Date()));
        List<ProcessConfig> list = processConfigService.findAll();
        list.forEach(p -> {
            boolean isExecute = false;
            if (!CRON_EXPRESSION_UPDATE_RECEIVE_CUST_LOG.equals(p.getConfigCode())) {
                isExecute = DateTimeUtil.isRun(p.getConfigValue(), p.getLastProcess());
            }
            Date dtNow = TimeZoneUtils.changeTimeZone(new Date(), 0l);
            switch (p.getConfigCode()) {
                case CUSTOMER_INACTIVE_DUARATION:
                    if (isExecute) {
                        List<Customer> customers = customerService.findAllByCondition(p.getSiteId(), dtNow);
                        updateCustomer(customers);
                        updateCustomerTime(customers);
                        logger.info("Cap nhat thoi gian thuc hien tien trinh cho siteId ... #{}", p.getSiteId());
                        p.setLastProcess(dtNow);
                        processConfigService.update(p);
                    }
                    break;
                case CRON_EXPRESSION_CHECK_START:
                    // process
                    if (isExecute) {
                        List<Long> status = new ArrayList<>();
                        status.add(1L);
                        status.add(1L);
                        List<Campaign> campaigns = campaignService.findCampaignByCompanySiteIdAndStartTimeIsLessThanEqualAndStatusIn(p.getSiteId(), dtNow, status);
                        campaigns.forEach(campaign -> {
                            logger.info("Chuyen trang thai chien dich ... #{} ... tu Du thao sang Trien khai", campaign.getCampaignId());
                            campaign.setProcessStatus(1);
                            campaign.setStatus(2L);
                            campaign.setCampaignStart(dtNow);
                            campaignService.updateProcess(campaign);
                            //write log
                            saveLog(campaign, 1);
                        });

                        logger.info("Cap nhat thoi gian thuc hien tien trinh cho siteId ... #{}", p.getSiteId());
                        p.setLastProcess(dtNow);
                        processConfigService.update(p);
                    }
                    break;
                case CRON_EXPRESSION_CHECK_END:
                    // process
                    if (isExecute) {
                        List<Long> status = new ArrayList<>();
                        status.add(2L);
                        status.add(3L);
                        List<Campaign> campaigns = campaignService.findCampaignByCompanySiteIdAndEndTimeIsLessThanEqualAndStatusIn(p.getSiteId(), dtNow, status);
                        campaigns.forEach(campaign -> {
                            logger.info("Chuyen trang thai chien dich ... #{} ... sang Ket thuc", campaign.getCampaignId());
                            campaign.setStatus(4L);
                            campaign.setCampaignEnd(dtNow);
                            campaignService.updateProcess(campaign);
                            //write log
                            saveLog(campaign, 2);
                        });

                        logger.info("Cap nhat thoi gian thuc hien tien trinh cho siteId ... #{}", p.getSiteId());
                        p.setLastProcess(dtNow);
                        processConfigService.update(p);
                    }
                    break;
                case CRON_EXPRESSION_UPDATE_RECEIVE_CUST_LOG:
                    // load all data receive cust logs where end_time = null
                    //log.info(p.getConfigValue());
//                    Date condition = DateTimeUtil.lastExecution2Long(p.getConfigValue());
                    List<ReceiveCustLog> receiveCustLogs = receiveCustLogRepositoryCustom.findTop100EndTimeIsNullAndCondition(p.getConfigValue());
                    if (!receiveCustLogs.isEmpty()) {
                        logger.info("Cập nhật RECEIVE_CUST_LOG count ... #{} ... ", receiveCustLogs.size());
                        updateReceiveCustLog(receiveCustLogs);
                        p.setLastProcess(dtNow);
                        processConfigService.update(p);
                    }
                default:
                    // update last check time
            }
        });


    }

    private void updateCustomer(List<Customer> customers) {
        customers.forEach(c -> {
            logger.info("Cap nhat trang thai khoa cua KH ... #{}", c.getCustomerId());
            c.setIpccStatus("active");
            customerService.update(c);
        });
    }

    private void updateCustomerTime(List<Customer> customers) {
        customers.forEach(customer -> {
            // find all customer_time by customerId
            List<CustomerTime> customerTimes = customerTimeService.findByCustomerId(customer.getCustomerId());
            customerTimes.parallelStream().forEach(customerTime -> {
                logger.info("Cap nhat Customer time cua KH ... #{}", customerTime.getCustomerId());
                customerTime.setStatus((short) 2);
                customerTime.setUpdateTime(new Date());
                customerTimeService.update(customerTime);
            });
        });
    }

    private void saveLog(Campaign c, int t) {
        UserActionLog logOld = userActionLogService.getByAgentId(1l);
        if (logOld != null) {
            userActionLogRepository.delete(logOld);
        }
        UserActionLog log = new UserActionLog();
        log.setAgentId(1L);
        log.setSessionId("-1");
        log.setStartTime(new Date());
        log.setActionType(t == 1 ? 13L : 14L);
        log.setObjectId(c.getCampaignId());
        log.setCompanySiteId(c.getCompanySiteId());
        userActionLogService.save(log);
    }

    private void updateReceiveCustLog(List<ReceiveCustLog> receiveCustLogs) {
        receiveCustLogs.parallelStream().forEach(receiveCustLog -> {
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
            receiveCustLog.setEndTime(new Date());
            receiveCustLogRepository.save(receiveCustLog);
        });
    }

}
