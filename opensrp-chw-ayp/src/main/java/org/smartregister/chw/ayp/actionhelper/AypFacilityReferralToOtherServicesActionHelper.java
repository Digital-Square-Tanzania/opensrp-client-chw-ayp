package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.util.Constants;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypFacilityReferralToOtherServicesActionHelper extends BaseSimpleVisitActionHelper {
    private static final String REFERRAL_KEY = "refer_other_services";
    private static final String OPTION_VMMC = "vmmc";
    private String preProcessedJson;

    public AypFacilityReferralToOtherServicesActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        preProcessedJson = jsonPayload;
        if (!isFemaleClient()) {
            return;
        }

        try {
            JSONObject form = new JSONObject(jsonPayload);
            JSONArray fields = form.getJSONObject("step1").getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if (REFERRAL_KEY.equals(field.optString("key"))) {
                    removeOption(field, OPTION_VMMC);
                    break;
                }
            }
            preProcessedJson = form.toString();
        } catch (Exception e) {
            Timber.e(e);
            preProcessedJson = jsonPayload;
        }
    }

    @Override
    public String getPreProcessed() {
        return preProcessedJson;
    }

    private boolean isFemaleClient() {
        if (memberObject == null || StringUtils.isBlank(memberObject.getGender())) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(memberObject.getGender(), Constants.FEMALE);
    }

    private void removeOption(JSONObject field, String optionKey) {
        JSONArray options = field.optJSONArray("options");
        if (options == null) {
            return;
        }

        for (int i = options.length() - 1; i >= 0; i--) {
            JSONObject option = options.optJSONObject(i);
            if (option != null && optionKey.equals(option.optString("key"))) {
                options.remove(i);
            }
        }
    }
}
