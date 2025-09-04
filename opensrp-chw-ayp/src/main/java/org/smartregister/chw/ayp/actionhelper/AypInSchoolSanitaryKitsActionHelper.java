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

public class AypInSchoolSanitaryKitsActionHelper implements BaseAypVisitAction.aypVisitActionHelper {
    private MemberObject memberObject;
    private String sanitaryKitsProvided; // Field to store if kits were provided
    private String jsonPayload;
    private Context context;

    public AypInSchoolSanitaryKitsActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        this.jsonPayload = jsonPayload;
        // this.context = context; // Already set in constructor
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
            this.sanitaryKitsProvided = JsonFormUtils.getValue(payload, "sanitary_kits_provided");
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public String evaluateSubTitle() {
        if (StringUtils.isBlank(sanitaryKitsProvided)) {
            return context.getString(R.string.ayp_sanitary_kits_provided_prefix) + context.getString(R.string.ayp_no); // Default to No if not set
        }

        String statusText = "";
        if ("yes".equalsIgnoreCase(sanitaryKitsProvided)) {
            statusText = context.getString(R.string.ayp_yes);
        } else if ("no".equalsIgnoreCase(sanitaryKitsProvided)) {
            statusText = context.getString(R.string.ayp_no);
        } else {
            statusText = sanitaryKitsProvided; // Should ideally not happen if form has yes/no
        }

        return context.getString(R.string.ayp_sanitary_kits_provided_prefix) + statusText;
    }

    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isBlank(sanitaryKitsProvided)) {
            return BaseAypVisitAction.Status.PENDING;
        }
        if ("yes".equalsIgnoreCase(sanitaryKitsProvided)) {
            return BaseAypVisitAction.Status.COMPLETED;
        }
        // Assuming if 'no' is selected, the action might still be considered complete for this step
        // Adjust if 'no' means it's pending or requires different handling
        if ("no".equalsIgnoreCase(sanitaryKitsProvided)) {
            return BaseAypVisitAction.Status.COMPLETED;
        }
        return BaseAypVisitAction.Status.PENDING;
    }

    public void onPayloadReceived(BaseAypVisitAction action) {
        // Often empty in this pattern
    }
}
