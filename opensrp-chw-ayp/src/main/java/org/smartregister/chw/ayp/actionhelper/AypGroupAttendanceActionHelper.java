package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypGroupAttendanceActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    private final Context context;
    private final MemberObject memberObject;
    private String submittedPayload;

    public AypGroupAttendanceActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        // no-op
    }

    @Override
    public String getPreProcessed() {
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        this.submittedPayload = jsonPayload;
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
        if (StringUtils.isBlank(submittedPayload)) return null;
        try {
            JSONObject payload = new JSONObject(submittedPayload);

            // Try common patterns to present a meaningful subtitle
            // 1) explicit count field
            String count = JsonFormUtils.getValue(payload, "present_members_count");
            if (StringUtils.isNotBlank(count)) {
                return context.getString(R.string.ayp_group_attendance) + ": " + count;
            }

            // 2) array-like multiselect field
            String members = JsonFormUtils.getValue(payload, "members_present");
            if (StringUtils.isNotBlank(members)) {
                try {
                    JSONArray arr = new JSONArray(members);
                    return context.getString(R.string.ayp_group_attendance) + ": " + arr.length();
                } catch (Exception e) {
                    // fall through
                }
            }

        } catch (Exception e) {
            Timber.e(e);
        }
        return null;
    }

    @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        return StringUtils.isNotBlank(submittedPayload) ? BaseAypVisitAction.Status.COMPLETED : BaseAypVisitAction.Status.PENDING;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseAypVisitAction) {
        // no-op
    }
}

