package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypFacilityBiomedicalFamilyPlanningActionHelper extends BaseSimpleVisitActionHelper {
    private static final String FAMILY_PLANNING_KEY = "family_planning_methods";
    private static final String OPTION_PILLS = "pills";
    private static final String OPTION_INJECTABLE = "injectable";
    private static final String OPTION_IMPLANTS = "implants";
    private static final String OPTION_IUCD = "iucd";
    private String preProcessedJson;

    public AypFacilityBiomedicalFamilyPlanningActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        preProcessedJson = jsonPayload;

        List<String> optionsToRemove = getOptionsToRemove();
        if (optionsToRemove.isEmpty()) {
            return;
        }

        try {
            JSONObject form = new JSONObject(jsonPayload);
            JSONArray fields = form.getJSONObject("step1").getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if (FAMILY_PLANNING_KEY.equals(field.optString("key"))) {
                    removeOptions(field, optionsToRemove);
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

    private List<String> getOptionsToRemove() {
        List<String> optionsToRemove = new ArrayList<>();
        if (memberObject == null || StringUtils.isBlank(memberObject.getGender())) {
            return optionsToRemove;
        }

        if (StringUtils.equalsIgnoreCase(memberObject.getGender(), Constants.MALE)) {
            optionsToRemove.add(OPTION_PILLS);
            optionsToRemove.add(OPTION_INJECTABLE);
            optionsToRemove.add(OPTION_IMPLANTS);
            optionsToRemove.add(OPTION_IUCD);
        }
        return optionsToRemove;
    }

    private void removeOptions(JSONObject field, List<String> optionKeys) {
        JSONArray options = field.optJSONArray("options");
        if (options == null) {
            return;
        }

        for (int i = options.length() - 1; i >= 0; i--) {
            JSONObject option = options.optJSONObject(i);
            if (option != null && optionKeys.contains(option.optString("key"))) {
                options.remove(i);
            }
        }
    }
}
