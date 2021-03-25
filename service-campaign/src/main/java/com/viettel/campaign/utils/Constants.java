package com.viettel.campaign.utils;

/**
 * @author anhvd_itsol
 */
public class Constants {
    public interface ApiErrorCode {
        String ERROR = "01";
        String SUCCESS = "00";
        String DELETE_ERROR = "02";
    }
    public interface ApiErrorDesc {
        String ERROR = "ERROR";
        String SUCCESS = "SUCCESS";
        String DELETE_ERROR = "DELETE_ERROR";
    }
    public interface FileType {
        String pdf = "pdf";
        String xls = "xls";
        String xlsx = "xlsx";
    }
    public interface Status {
        Long ACTIVE = 1L;
        Long INACTIVE = 0L;

        String ACTIVE_STR = "Hoạt động";
        String INACTIVE_STR = "Không hoạt động";
    }
    public interface TypeStatus {
        Long ACTIVE = 1L;
        Long INACTIVE = 0L;

        String ACTIVE_STR = "Trạng thái kêt nối(Không thành công)";
        String INACTIVE_STR = "Trạng thái khảo sát";
    }

    public interface SQL_MODULES {
        String MODULE_EXECUTE = "campaign-execute";
    }

    public interface LANGUAGE {
        String LANGUAGE = "i18n/language";
    }

    public interface DATE_FORMAT {
        String FOMART_DATE_TYPE_1 = "DD/MM/YYYY";
    }

    public interface MIME_TYPE {
        String EXCEL_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    public interface FILE_UPLOAD_RESP_CODE {
        String SUCCESS = "00";
        String ERROR = "01";
        String INVALID_FORMAT = "02";
        String EMPTY = "03";
        String ROW_EMPTY = "04";
    }

    public interface PHONE_RANK {
        public static final int DEFAULT = 1;
        public static final int BLACK_LIST = 0;
        public static final int VIP = 5;
        public static final int NORMAL = 1;
    }

    public interface CHANNEL_ID {

        public static final int TICKET = 0;
        public static final int FACEBOOK = 1;
        public static final int EMAIL = 2;
        public static final int OTHER = 3;
        public static final int CHAT = 4;
        public static final int VOICE = 5;
        public static final int WEB = 6;
    }

    public interface PHONE_TYPE{
        public static final int MAIN_PHONE = 1;
        public static final int SUB_PHONE = 0;
    }
}
