package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.util.JsonFormUtils;
import org.smartregister.chw.ayp.domain.VisitDetail;


import java.util.List;
import java.util.Map;

import timber.log.Timber;

// Changed to implements BaseAypVisitAction.aypVisitActionHelper
public class AypInSchoolEducationSubsidiesActionHelper implements BaseAypVisitAction.aypVisitActionHelper {
    private MemberObject memberObject;
    private String educationSubsidiesProvided;
    private String jsonPayload;
    private Context context;

    // Constructor might need adjustment if BaseAypVisitAction.aypVisitActionHelper is an interface
    // and no super() call is appropriate.
    public AypInSchoolEducationSubsidiesActionHelper(Context context, MemberObject memberObject) {
        // super(context, memberObject); // Removed if not extending a class that needs it
        this.context = context; 
        this.memberObject = memberObject; 
    }

    // This signature comes from BaseAypVisitActionHelper, may need adjustment
    // if aypVisitActionHelper interface defines it differently or not at all.
    // @Override 
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

    // @Override
    public String getPreProcessedSubTitle() {
        return null;
    }

    // @Override
    public String postProcess(String jsonPayload) {
        return "";
    }

    // @Override
    public void onPayloadReceived(String jsonPayload) {
        try {
            JSONObject payload = new JSONObject(jsonPayload);
            educationSubsidiesProvided = JsonFormUtils.getValue(payload, "education_subsidies_provided");
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    // @Override
    public String evaluateSubTitle() {
        if (StringUtils.isBlank(educationSubsidiesProvided)) {
            return "";
        }

        String providedString = "";
        if ("yes".equalsIgnoreCase(educationSubsidiesProvided)) {
            providedString = context.getString(R.string.ayp_yes);
        } else if ("no".equalsIgnoreCase(educationSubsidiesProvided)) {
            providedString = context.getString(R.string.ayp_no);
        }

        if (StringUtils.isNotBlank(providedString)) {
            return context.getString(R.string.ayp_education_subsidies_provided_prefix) + providedString;
        }
        return "";
    }

    // @Override
    public BaseAypVisitAction.Status evaluateStatusOnPayload() {
        if (StringUtils.isBlank(educationSubsidiesProvided)) {
            return BaseAypVisitAction.Status.PENDING;
        }
        return BaseAypVisitAction.Status.COMPLETED;
    }

    // @Override
    // Corrected typo in previous versions if any
    public void onPayloadReceived(BaseAypVisitAction action) { 
        // Empty as per AypInSchoolFinancialLiteracyActionHelper
    }
}
