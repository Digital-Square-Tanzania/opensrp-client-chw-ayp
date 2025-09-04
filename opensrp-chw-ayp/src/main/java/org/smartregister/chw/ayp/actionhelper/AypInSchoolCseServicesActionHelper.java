package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class AypInSchoolCseServicesActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected String cseServicesProvided;

    protected String jsonPayload;

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
            JSONObject jsonObject = new JSONObject(jsonPayload);

            String provided = JsonFormUtils.getValue(jsonObject, "cse_services_provided");
            String topics = JsonFormUtils.getValue(jsonObject, "cse_services_topics"); // comma-separated if multiple

            // keep a reference if needed elsewhere
            cseServicesProvided = provided;

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
        return null;
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {

        if (StringUtils.isNotBlank(cseServicesProvided)) {
            return BaseAypVisitAction.Status.COMPLETED;
        }
        return BaseAypVisitAction.Status.PENDING;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseaypVisitAction) {
        //overridden
    }

}
