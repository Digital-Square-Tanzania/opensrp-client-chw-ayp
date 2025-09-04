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


public class AypInSchoolFinancialLiteracyServicesActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected String financialLiteracyProvided;
    protected String financialLiteracyTopic;

    protected String jsonPayload;

    protected Context context;

    protected MemberObject memberObject;


    public AypInSchoolFinancialLiteracyServicesActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        this.jsonPayload = jsonPayload;
    }

    @Override
    public String getPreProcessed() {
        return jsonPayload;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        try {
            JSONObject jsonObject = new JSONObject(jsonPayload);

            String provided = JsonFormUtils.getValue(jsonObject, "financial_literacy_provided");
            String topic = JsonFormUtils.getValue(jsonObject, "financial_literacy_topic");

            financialLiteracyProvided = provided;
            financialLiteracyTopic = topic;

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
        if (StringUtils.isBlank(financialLiteracyProvided)) return null;
        if ("yes".equalsIgnoreCase(financialLiteracyProvided)) {
            return "Provided";
        } else if ("no".equalsIgnoreCase(financialLiteracyProvided)) {
            return "Not provided";
        }
        return null;
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {

        if (StringUtils.isBlank(financialLiteracyProvided)) {
            return BaseAypVisitAction.Status.PENDING;
        }
        if ("yes".equalsIgnoreCase(financialLiteracyProvided)) {
            return StringUtils.isNotBlank(financialLiteracyTopic)
                    ? BaseAypVisitAction.Status.COMPLETED
                    : BaseAypVisitAction.Status.PENDING;
        }
        // If answered "no", the step is complete
        return BaseAypVisitAction.Status.COMPLETED;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseaypVisitAction) {
        //overridden
    }

}
