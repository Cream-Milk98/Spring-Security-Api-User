package com.viettel.campaign.web.dto.request_dto;

import com.viettel.campaign.web.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerQueryDTO extends BaseDTO {
    String join ;
    Long field;
    String operator;
    String condition;
    String type;

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public Long getField() {
        return field;
    }

    public void setField(Long field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
