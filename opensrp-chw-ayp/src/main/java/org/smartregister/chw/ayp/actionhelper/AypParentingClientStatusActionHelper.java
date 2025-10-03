package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

public class AypParentingClientStatusActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingClientStatusActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        String clientStatus = getTranslatedValue(jsonObject, "client_status");
        if (StringUtils.isBlank(clientStatus)) {
            return null;
        }
        return context.getString(R.string.ayp_client_status_subtitle, clientStatus);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        String clientStatus = getValue(jsonObject, "client_status");
        if (StringUtils.isBlank(clientStatus)) {
            return BaseAypVisitAction.Status.PENDING;
        }
        return BaseAypVisitAction.Status.COMPLETED;
    }
}
