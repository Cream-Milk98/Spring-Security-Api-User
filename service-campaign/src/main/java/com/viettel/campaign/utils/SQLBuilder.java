package com.viettel.campaign.utils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

import com.viettel.campaign.web.dto.BaseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author anhvd_itsol
 */
public class SQLBuilder {
    protected static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);
    public static final String SQL_MODULE_CAMPAIGN_MNG = "campaign-mng";
    public static final String SQL_MODULE_CAMPAIGN_STATUS_MNG = "campaign-status-mng";
    public static final String SQL_MODULE_CAMPAIGN_CUSTOMER_MNG = "campaign-customer-mng";

    public static String getSqlQueryById(String module,
                                         String queryId) {
        File folder = null;
        try {
            folder = new ClassPathResource(
                    "sql" + File.separator + module + File.separator + queryId + ".sql").getFile();

            // Read file
            if (folder.isFile()) {
                String sql = new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath())));
                return sql;
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return null;
    }

    public static Pageable buildPageable(BaseDTO obj) {
        Pageable pageable = null;
        try {
            if (DataUtil.isNullOrEmpty(obj.getSort())) {
                pageable = PageRequest.of(obj.getPage(), obj.getPageSize());
            } else {
                String[] sorts = obj.getSort().split(",");
                Sort sort = new Sort(Sort.Direction.fromString(sorts[1]), sorts[0]);
                pageable = PageRequest.of(obj.getPage(), obj.getPageSize(), sort);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return pageable;
    }
}
