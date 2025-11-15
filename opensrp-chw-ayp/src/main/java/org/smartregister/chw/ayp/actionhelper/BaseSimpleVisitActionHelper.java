package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;
import java.util.Map;

/**
 * Lightweight helper that marks actions complete when a payload is captured.
 * Extend this helper for forms that do not require custom pre-processing.
 */
public class BaseSimpleVisitActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    protected final Context context;
    protected final MemberObject memberObject;
    protected String jsonPayload;

    public BaseSimpleVisitActionHelper(Context context, MemberObject memberObject) {
        this.context = context;
        this.memberObject = memberObject;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        // No pre-processing required for simple helpers.
    }

    @Override
    public String getPreProcessed() {
        return null;
    }

    @Override
    public void onPayloadReceived(String jsonPayload) {
        this.jsonPayload = jsonPayload;
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
        return StringUtils.isBlank(jsonPayload)
                ? BaseAypVisitAction.Status.PENDING
                : BaseAypVisitAction.Status.COMPLETED;
    }

    @Override
    public void onPayloadReceived(BaseAypVisitAction baseAypVisitAction) {
        // No additional processing required.
    }
}
