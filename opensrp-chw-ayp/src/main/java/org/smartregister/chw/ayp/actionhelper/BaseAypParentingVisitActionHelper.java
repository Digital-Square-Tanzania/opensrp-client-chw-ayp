package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPTIONS_FIELD_NAME;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TEXT;
import static com.vijay.jsonwizard.utils.FormUtils.getFieldFromForm;

public abstract class BaseAypParentingVisitActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected final Context context;
    protected final MemberObject memberObject;
    protected String jsonPayload;
    protected JSONObject currentJsonState;

    protected BaseAypParentingVisitActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        this.jsonPayload = jsonPayload;
    }

    @Override
    public String getPreProcessed() {
        return null;
    }

    @Override
    public String getPreProcessedSubTitle() {
        return null;
    }

    @Override
    public BaseAypVisitAction.ScheduleStatus getPreProcessedStatus() {
        return null;
    }

    @Override
    public String postProcess(String jsonPayload) {
        return jsonPayload;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        try {
            currentJsonState = StringUtils.isNotBlank(jsonPayload) ? new JSONObject(jsonPayload) : null;
        } catch (JSONException e) {
            Timber.e(e);
            currentJsonState = null;
        }
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseAypVisitAction) {
        // No-op
    }

    @Override
    public String evaluateSubTitle() {
        if (currentJsonState == null) {
            return null;
        }
        try {
            return evaluateSubTitle(currentJsonState);
        } catch (JSONException e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (currentJsonState == null) {
            return BaseAypVisitAction.Status.PENDING;
        }
        try {
            return evaluateStatus(currentJsonState);
        } catch (JSONException e) {
            Timber.e(e);
            return BaseAypVisitAction.Status.PENDING;
        }
    }

    protected abstract String evaluateSubTitle(JSONObject jsonObject) throws JSONException;

    protected abstract BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException;

    protected String getValue(JSONObject jsonObject, String key) {
        if (jsonObject == null || StringUtils.isBlank(key)) {
            return "";
        }
        return JsonFormUtils.getValue(jsonObject, key);
    }

    protected List<String> getValueList(JSONObject jsonObject, String key) {
        return extractValues(getValue(jsonObject, key));
    }

    protected List<String> getTranslatedValueList(JSONObject jsonObject, String key) {
        List<String> rawValues = getValueList(jsonObject, key);
        if (rawValues.isEmpty() || jsonObject == null) {
            return rawValues;
        }
        try {
            JSONObject field = getFieldFromForm(jsonObject, key);
            if (field == null) {
                return rawValues;
            }
            JSONArray options = field.optJSONArray(OPTIONS_FIELD_NAME);
            if (options == null || options.length() == 0) {
                return rawValues;
            }

            Map<String, String> optionMap = new HashMap<>();
            for (int i = 0; i < options.length(); i++) {
                JSONObject option = options.getJSONObject(i);
                optionMap.put(option.optString(KEY), option.optString(TEXT));
            }

            List<String> displayValues = new ArrayList<>();
            for (String value : rawValues) {
                if (optionMap.containsKey(value)) {
                    displayValues.add(optionMap.get(value));
                } else {
                    displayValues.add(value);
                }
            }
            return displayValues;
        } catch (JSONException e) {
            Timber.e(e);
            return rawValues;
        }
    }

    protected String getTranslatedValue(JSONObject jsonObject, String key) {
        List<String> translated = getTranslatedValueList(jsonObject, key);
        if (translated.isEmpty()) {
            return "";
        }
        return TextUtils.join(", ", translated);
    }

    protected List<String> extractValues(String rawValue) {
        List<String> values = new ArrayList<>();
        if (StringUtils.isBlank(rawValue)) {
            return values;
        }
        String trimmed = rawValue.trim();
        try {
            JSONArray array = new JSONArray(trimmed);
            for (int i = 0; i < array.length(); i++) {
                String value = array.optString(i);
                if (StringUtils.isNotBlank(value)) {
                    values.add(value.trim());
                }
            }
            if (!values.isEmpty()) {
                return values;
            }
        } catch (JSONException e) {
            // Not a JSON array, continue parsing
        }

        if (trimmed.contains(",")) {
            String[] splitValues = trimmed.split(",");
            for (String value : splitValues) {
                if (StringUtils.isNotBlank(value)) {
                    values.add(value.trim());
                }
            }
        } else if (StringUtils.isNotBlank(trimmed)) {
            values.add(trimmed);
        }

        return values;
    }
}
