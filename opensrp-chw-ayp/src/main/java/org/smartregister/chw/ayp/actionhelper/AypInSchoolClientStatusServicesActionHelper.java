package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
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


public class AypInSchoolClientStatusServicesActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected String clientStatus;

    protected String jsonPayload;
    private String submittedPayload;

    protected Context context;

    protected MemberObject memberObject;


    public AypInSchoolClientStatusServicesActionHelper(Context context, MemberObject memberObject) {
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
            clientStatus = JsonFormUtils.getValue(jsonObject, "client_status");
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
        try {
            if (submittedPayload == null) return null;
            JSONObject json = new JSONObject(submittedPayload);
            String clientStatusValueKey = JsonFormUtils.getValue(json, "client_status");

            clientStatusValueKey = clientStatusValueKey.trim();

            if (clientStatusValueKey.isEmpty()) return null;

            String displayValue;
            switch (clientStatusValueKey) {
                case "continuing":
                    displayValue = context.getString(R.string.ayp_continuing);
                    break;
                case "dead":
                    displayValue = context.getString(R.string.ayp_dead);
                    break;
                case "transferred":
                    displayValue = context.getString(R.string.ayp_transferred);
                    break;
                case "dropped_from_school":
                    displayValue = context.getString(R.string.ayp_dropped_from_school);
                    break;
                case "opt_out":
                    displayValue = context.getString(R.string.ayp_opt_out);
                    break;
                default:
                    displayValue = clientStatusValueKey; // Fallback to the key if no match
            }
            return String.format(context.getString(R.string.ayp_client_status_subtitle), displayValue);
        } catch (Exception e) {
            Timber.e(e);
            return null;
        }
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {

        if (StringUtils.isNotBlank(clientStatus)) {
            return BaseAypVisitAction.Status.COMPLETED;
        }
        return BaseAypVisitAction.Status.PENDING;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseaypVisitAction) {
        //overridden
    }

}
