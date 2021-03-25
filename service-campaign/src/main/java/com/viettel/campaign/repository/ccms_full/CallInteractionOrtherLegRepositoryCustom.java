package com.viettel.campaign.repository.ccms_full;

import com.viettel.campaign.model.ccms_full.CallInteractionOtherLeg;
import java.util.List;

public interface CallInteractionOrtherLegRepositoryCustom {
    List<CallInteractionOtherLeg> getCallInteractionByPhoneNumber(String phoneNumber);

    List<CallInteractionOtherLeg> getCallInteractionByCallID(String callID);
}
