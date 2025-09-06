package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;
import org.smartregister.chw.ayp.R;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypInSchoolCseServicesActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected String cseConducted;
    protected String topicsCovered;
    protected String jsonPayload;
    private String submittedPayload;
    protected Context context;
    protected MemberObject memberObject;

    public AypInSchoolCseServicesActionHelper(Context context, MemberObject memberObject) {
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
    public void onPayloadReceived(String jsonPayload) {
        try {
            this.submittedPayload = jsonPayload;
            JSONObject jsonObject = new JSONObject(jsonPayload);
            cseConducted = JsonFormUtils.getValue(jsonObject, "cse_conducted");
            topicsCovered = JsonFormUtils.getValue(jsonObject, "topics_covered");
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Override
    public String postProcess(String jsonPayload) {
        return jsonPayload;
    }

    @Override
    public BaseAypVisitAction.ScheduleStatus getPreProcessedStatus() {
        return null;
    }

    @Override
    public String getPreProcessedSubTitle() {
        return null;
    }

    @Override
    public String evaluateSubTitle() {
        if (submittedPayload == null) return null;
        try {
            JSONObject json = new JSONObject(submittedPayload);
            String cseConductedValue = JsonFormUtils.getValue(json, "cse_conducted");

            if ("yes".equalsIgnoreCase(cseConductedValue)) {
                String topicsCoveredValue = JsonFormUtils.getValue(json, "topics_covered");
                if (StringUtils.isNotBlank(topicsCoveredValue)) {
                    JSONArray topicsArray = new JSONArray(topicsCoveredValue);
                    if (topicsArray.length() > 0) {
                        return String.format(context.getString(R.string.ayp_cse_conducted_subtitle_with_topics), topicsArray.length());
                    }
                }
                return context.getString(R.string.ayp_cse_conducted_yes);
            } else if ("no".equalsIgnoreCase(cseConductedValue)) {
                return context.getString(R.string.ayp_cse_conducted_no);
            }
        } catch (JSONException e) {
            Timber.e(e);
            return null;
        }
        return null;
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isNotBlank(cseConducted)) {
            if ("yes".equalsIgnoreCase(cseConducted)) {
                if (StringUtils.isNotBlank(topicsCovered) && !"[]".equals(topicsCovered)) {
                     return BaseAypVisitAction.Status.COMPLETED;
                }
                return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
            }
            return BaseAypVisitAction.Status.COMPLETED;
        }
        return BaseAypVisitAction.Status.PENDING;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseAypVisitAction) {
        //overridden
    }
}
