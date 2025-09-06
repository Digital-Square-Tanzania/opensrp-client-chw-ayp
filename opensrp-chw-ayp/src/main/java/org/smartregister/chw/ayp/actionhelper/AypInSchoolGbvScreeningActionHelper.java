package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypInSchoolGbvScreeningActionHelper implements BaseAypVisitAction.aypVisitActionHelper {
    private MemberObject memberObject;
    private String screeningOutcome; // Field for screening_for_gbv
    private String jsonPayload;
    private Context context;

    public AypInSchoolGbvScreeningActionHelper(Context context, MemberObject memberObject) {
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
    public BaseAypVisitAction.ScheduleStatus getPreProcessedStatus() {
        return null;
    }

    public String getPreProcessedSubTitle() {
        return null;
    }

    public String postProcess(String jsonPayload) {
        return "";
    }

    public void onPayloadReceived(String jsonPayload) {
        try {
            JSONObject payload = new JSONObject(jsonPayload);
            this.screeningOutcome = JsonFormUtils.getValue(payload, "screening_for_gbv");
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public String evaluateSubTitle() {
        if (StringUtils.isBlank(screeningOutcome)) {
             return context.getString(R.string.ayp_gbv_screening_outcome_prefix) + context.getString(R.string.ayp_gbv_not_screened); // Default to Not Screened
        }

        String statusText;
        switch (screeningOutcome) {
            case "encountered_gbv":
                statusText = context.getString(R.string.ayp_gbv_encountered);
                break;
            case "did_not_encounter_gbv":
                statusText = context.getString(R.string.ayp_gbv_not_encountered);
                break;
            case "not_screened":
            default:
                statusText = context.getString(R.string.ayp_gbv_not_screened);
                break;
        }
        return context.getString(R.string.ayp_gbv_screening_outcome_prefix) + statusText;
    }

    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isBlank(screeningOutcome)) {
            return BaseAypVisitAction.Status.PENDING;
        }
        // Any selection from the radio group means the screening step is considered addressed.
        // Specific follow-up actions would be handled by other form fields/relevance logic.
        if ("encountered_gbv".equalsIgnoreCase(screeningOutcome) ||
            "did_not_encounter_gbv".equalsIgnoreCase(screeningOutcome) ||
            "not_screened".equalsIgnoreCase(screeningOutcome)) {
            return BaseAypVisitAction.Status.COMPLETED;
        }
        return BaseAypVisitAction.Status.PENDING;
    }

    public void onPayloadReceived(BaseAypVisitAction action) {
        // Often empty in this pattern
    }
}
