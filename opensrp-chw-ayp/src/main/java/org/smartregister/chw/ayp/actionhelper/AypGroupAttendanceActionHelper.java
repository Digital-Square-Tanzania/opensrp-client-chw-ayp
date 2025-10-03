package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.JsonFormUtils;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import timber.log.Timber;

public class AypGroupAttendanceActionHelper implements BaseAypVisitAction.aypVisitActionHelper {

    private final Context context;
    private final MemberObject memberObject;
    private final String groupId;
    private String submittedPayload;
    private String loadedJson;
    private String preProcessedJson;

    public AypGroupAttendanceActionHelper(Context context, MemberObject memberObject, String groupId) {
        this.context = context;
        this.memberObject = memberObject;
        this.groupId = groupId;
    }

    @Override
    public void onJsonFormLoaded(String jsonPayload, Context context, Map<String, List<VisitDetail>> map) {
        this.loadedJson = jsonPayload;
        try {
            JSONObject form = new JSONObject(jsonPayload);

            // Build dynamic options for members_present
            JSONArray fields = form.getJSONObject("step1").getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                if ("members_present".equals(field.optString("key"))) {
                    JSONArray options = buildMemberOptions();
                    field.put("options", options);
                    // ensure combined option values is true
                    field.put("combine_checkbox_option_values", "true");
                    break;
                }
            }

            this.preProcessedJson = form.toString();
        } catch (Exception e) {
            Timber.e(e);
            this.preProcessedJson = this.loadedJson; // fallback
        }
    }

    @Override
    public String getPreProcessed() {
        return preProcessedJson;
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

    private JSONArray buildMemberOptions() {
        JSONArray options = new JSONArray();
        try {
            List<MemberObject> members = getMembersForGroup();
            for (MemberObject m : members) {
                if (m == null || StringUtils.isBlank(m.getBaseEntityId())) continue;
                JSONObject opt = new JSONObject();
                opt.put("key", m.getBaseEntityId());
                opt.put("text", m.getFullName());
                opt.put("openmrs_entity_parent", "");
                opt.put("openmrs_entity", "concept");
                opt.put("openmrs_entity_id", m.getBaseEntityId());
                options.put(opt);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return options;
    }

    protected List<MemberObject> getMembersForGroup() {
        List<MemberObject> members = new ArrayList<>();
        try {
            if (members.isEmpty()) {
                members = AypDao.getInSchoolGroupMembers(groupId);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return members;
    }
}
