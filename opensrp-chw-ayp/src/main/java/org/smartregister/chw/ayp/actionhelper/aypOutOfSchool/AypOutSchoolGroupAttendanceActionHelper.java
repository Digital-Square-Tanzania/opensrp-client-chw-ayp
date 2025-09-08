package org.smartregister.chw.ayp.actionhelper.aypOutOfSchool;

import android.content.Context;

import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypOutSchoolGroupAttendanceActionHelper implements BaseAypVisitAction.aypVisitActionHelper {
    private MemberObject memberObject;
    private String financialLiteracyConducted;
    protected String jsonPayload;
    private Context context;

    public AypOutSchoolGroupAttendanceActionHelper(Context context, MemberObject memberObject) {
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
    public String postProcess(String jsonPayload) {
        return "";
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        try {
            JSONObject payload = new JSONObject(jsonPayload);
//            financialLiteracyConducted = JsonFormUtils.getValue(payload, "financial_literacy_conducted");
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
//        if (StringUtils.isBlank(financialLiteracyConducted)) {
//            return "";
//        }
//
//        String conductedString = "";
//        if ("yes".equalsIgnoreCase(financialLiteracyConducted)) {
//            conductedString = context.getString(R.string.ayp_yes);
//        } else if ("no".equalsIgnoreCase(financialLiteracyConducted)) {
//            conductedString = context.getString(R.string.ayp_no);
//        }
//
//        if (StringUtils.isNotBlank(conductedString)) {
//            return context.getString(R.string.ayp_financial_literacy_conducted_prefix) + conductedString;
//        }
        return "";
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
//        if (StringUtils.isBlank(financialLiteracyConducted)) {
//            return BaseAypVisitAction.Status.PENDING;
//        }

        return BaseAypVisitAction.Status.COMPLETED;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction aypVisitAction) {

    }
}
