package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

public class AypParentingDeliveryModalityActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingDeliveryModalityActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        String modalityDisplay = getTranslatedValue(jsonObject, "service_delivery_modality");
        if (StringUtils.isBlank(modalityDisplay)) {
            return null;
        }

        String modalityRaw = getValue(jsonObject, "service_delivery_modality");
        String groupTypes = getTranslatedValue(jsonObject, "group_modality_types");
        if (StringUtils.equalsIgnoreCase(modalityRaw, "group") && StringUtils.isNotBlank(groupTypes)) {
            return modalityDisplay + ": " + groupTypes;
        }
        return modalityDisplay;
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        String modalityRaw = getValue(jsonObject, "service_delivery_modality");
        if (StringUtils.isBlank(modalityRaw)) {
            return BaseAypVisitAction.Status.PENDING;
        }

        if (StringUtils.equalsIgnoreCase(modalityRaw, "group") && getValueList(jsonObject, "group_modality_types").isEmpty()) {
            return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
        }

        return BaseAypVisitAction.Status.COMPLETED;
    }
}
