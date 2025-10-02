package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import android.text.TextUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.ArrayList;
import java.util.List;

public class AypParentingNextAppointmentReferralActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingNextAppointmentReferralActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        String appointmentDate = getValue(jsonObject, "next_appointment_date");
        List<String> referralDisplay = getTranslatedValueList(jsonObject, "referral_services");
        List<String> referralRaw = getValueList(jsonObject, "referral_services");
        String otherSpecify = getValue(jsonObject, "referral_other_specify");

        List<String> subtitleParts = new ArrayList<>();
        if (StringUtils.isNotBlank(appointmentDate)) {
            subtitleParts.add(context.getString(R.string.ayp_out_school_next_appointment) + ": " + appointmentDate);
        }

        if (!referralDisplay.isEmpty()) {
            String displayText = TextUtils.join(", ", referralDisplay);
            if (referralRaw.contains("others_specify") && StringUtils.isNotBlank(otherSpecify)) {
                displayText = displayText + " (" + otherSpecify + ")";
            }
            subtitleParts.add(displayText);
        } else if (StringUtils.isNotBlank(otherSpecify)) {
            subtitleParts.add(otherSpecify);
        }

        if (subtitleParts.isEmpty()) {
            return null;
        }

        return TextUtils.join("; ", subtitleParts);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        String appointmentDate = getValue(jsonObject, "next_appointment_date");
        List<String> referrals = getValueList(jsonObject, "referral_services");
        String otherSpecify = getValue(jsonObject, "referral_other_specify");

        if (StringUtils.isBlank(appointmentDate) && referrals.isEmpty()) {
            return BaseAypVisitAction.Status.PENDING;
        }

        if (referrals.contains("others_specify") && StringUtils.isBlank(otherSpecify)) {
            return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
        }

        return BaseAypVisitAction.Status.COMPLETED;
    }
}
