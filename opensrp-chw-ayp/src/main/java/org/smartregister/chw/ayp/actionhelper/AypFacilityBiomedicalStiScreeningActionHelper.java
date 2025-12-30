package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import timber.log.Timber;

public class AypFacilityBiomedicalStiScreeningActionHelper extends BaseSimpleVisitActionHelper {

    public AypFacilityBiomedicalStiScreeningActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    public String evaluateSubTitle() {
        if (StringUtils.isBlank(jsonPayload)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonPayload);
            String rawValue = firstNonBlank(
                    JsonFormUtils.getValue(jsonObject, "sti_screening_result"),
                    JsonFormUtils.getValue(jsonObject, "sti_screening_results"),
                    JsonFormUtils.getValue(jsonObject, "ayp_sti_screening_result")
            );

            if (StringUtils.isBlank(rawValue)) {
                return null;
            }

            return sanitizeDisplayValue(rawValue);
        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    private String sanitizeDisplayValue(String rawValue) {
        String sanitized = rawValue.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .trim();

        if (StringUtils.isBlank(sanitized)) {
            return null;
        }

        sanitized = sanitized.replace("_", " ");
        return StringUtils.capitalize(sanitized);
    }
}
