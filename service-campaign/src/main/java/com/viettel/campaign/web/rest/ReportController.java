package com.viettel.campaign.web.rest;

import com.viettel.campaign.birt.BirtReportGenerator;
import com.viettel.campaign.birt.ReportParameter;
import com.viettel.campaign.utils.FnCommon;
import com.viettel.campaign.utils.RedisUtil;
import com.viettel.campaign.web.dto.ReportResponseDTO;
import com.viettel.campaign.web.dto.ReportStatusDTO;
import com.viettel.campaign.web.dto.ResultDTO;
import com.viettel.campaign.web.dto.request_dto.CampaignRequestDTO;
import com.viettel.econtact.filter.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/ipcc/campaign/report")
public class ReportController {

    private Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    BirtReportGenerator birtReportGenerator;

    @RequestMapping(value = "/{reportName}/{type}/{hasPaging}", method = RequestMethod.POST)
    public ResponseEntity<Resource> report(
                                           @PathVariable("hasPaging") Boolean hasPaging,
                                           @PathVariable("type") String type,
                                           @PathVariable("reportName") String reportName,
                                           @RequestBody Map<String, Object> maps,
                                           final HttpServletRequest request) throws Exception {

        try {
            String sourceFile = reportName +".rptdesign";
            String tempFilePath = System.getProperty("user.dir") + File.separator + "templates" + File.separator + sourceFile;
            ReportParameter rm = new ReportParameter(tempFilePath, type);
            setParameterInMap(reportName, maps, request);
            if (maps != null) {
                Set set = maps.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    rm.setParameter(mentry.getKey().toString(), mentry.getValue());
                }
            }
            if ("html".equalsIgnoreCase(type)) {
                //rm.setParameter("isHideTableTitle", true);
            }

            ReportResponseDTO responseEntity = new ReportResponseDTO();
            ReportStatusDTO messEntity = new ReportStatusDTO();
            messEntity.setCode(10);
            messEntity.setDescription("");

            if ("html".equalsIgnoreCase(type) && hasPaging) {
                int count = birtReportGenerator.getCountData(rm);
                responseEntity.setData(count);
            }

            messEntity.setCode(1);
            messEntity.setDescription("OK");
            ByteArrayOutputStream outputStream = birtReportGenerator.generate(rm);
            ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
            responseEntity.setStatus(messEntity);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment;filename=" + "report_" + System.currentTimeMillis() + "." + type);
            headers.set("Content-Response", (FnCommon.convertObjectToStringJson(responseEntity)));
            headers.add("Access-Control-Expose-Headers", "Content-Response");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(byteArrayResource);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    private void setParameterInMap(String reportName, Map<String, Object> maps, HttpServletRequest request) {
        if ("connect_voice_report".equals(reportName)) {
            String authToken = request.getHeader("X-Auth-Token");
            UserSession userSession = (UserSession) RedisUtil.getInstance().get(authToken);
            maps.put("accountId", userSession.getAccountId());
        }

        if ("total_interaction_by_day".equals(reportName)) {
            String authToken = request.getHeader("X-Auth-Token");
            UserSession userSession = (UserSession) RedisUtil.getInstance().get(authToken);
            maps.put("accountId", userSession.getAccountId());
        }
    }
    @RequestMapping(value = "/manh", method = RequestMethod.POST)
    public ResultDTO findByCampaignCode(@RequestBody CampaignRequestDTO dto) {
        ResultDTO a = new ResultDTO();
        a.setData("àdsdf");
        return a;
    }
}
