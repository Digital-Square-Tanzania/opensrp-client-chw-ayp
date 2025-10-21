package org.smartregister.chw.ayp.actionhelper.aypOutOfSchool;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;
import org.smartregister.chw.ayp.util.VisitUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypOutSchoolServiceStatusActionHelper implements BaseAypVisitAction.aypVisitActionHelper {
    private MemberObject memberObject;
    protected String serviceStatus;
    protected String jsonPayload;
    private Context context;
    private HashMap<String, Boolean> checkObject = new HashMap<>();
    private String submittedPayload;


    public AypOutSchoolServiceStatusActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        this.jsonPayload = jsonPayload;
    }

    @Override
    public String getPreProcessed() {
        try {
            JSONObject jsonObject = new JSONObject(jsonPayload);
            return jsonObject.toString();
        } catch (JSONException e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public String getPreProcessedSubTitle() {
        return null;
    }

    @Override
    public String postProcess(String jsonPayload) {
        return jsonPayload;
    }

//    @Override
//    public void onPayloadReceived(String jsonPayload) {
//        try {
////            JSONObject jsonObject = new JSONObject(jsonPayload);
////            checkObject.clear();
////
////            checkObject.put("service_status", StringUtils.isNotBlank(JsonFormUtils.getValue(jsonObject, "service_status")));
//            this.submittedPayload = jsonPayload;
//            JSONObject jsonObject = new JSONObject(jsonPayload);
//            serviceStatus = JsonFormUtils.getValue(jsonObject, "service_status");
//        } catch (Exception e) {
//            Timber.e(e);
//        }
//    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        try {
            JSONObject payload = new JSONObject(jsonPayload);
            this.serviceStatus = JsonFormUtils.getValue(payload, "service_status");
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public BaseAypVisitAction.ScheduleStatus getPreProcessedStatus() {
        return null;
    }

    @Override
    public String evaluateSubTitle() {

        return "";
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isBlank(serviceStatus))
            return BaseAypVisitAction.Status.PENDING;
        else {
            return BaseAypVisitAction.Status.COMPLETED;
        }
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction aypVisitAction) {

    }
}

