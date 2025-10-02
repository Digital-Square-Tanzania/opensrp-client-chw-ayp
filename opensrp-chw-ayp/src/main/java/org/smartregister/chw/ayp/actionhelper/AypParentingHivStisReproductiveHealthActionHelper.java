package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;

public class AypParentingHivStisReproductiveHealthActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingHivStisReproductiveHealthActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        String providedDisplay = getTranslatedValue(jsonObject, "rh_provided");
        if (StringUtils.isBlank(providedDisplay)) {
            return null;
        }

        String providedRaw = getValue(jsonObject, "rh_provided");
        if (StringUtils.equalsIgnoreCase(providedRaw, "no")) {
            return providedDisplay;
        }

        String sectionsDisplay = getTranslatedValue(jsonObject, "rh_sections_provided");
        if (StringUtils.isBlank(sectionsDisplay)) {
            return providedDisplay;
        }
        return providedDisplay + ": " + sectionsDisplay;
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        String providedRaw = getValue(jsonObject, "rh_provided");
        if (StringUtils.isBlank(providedRaw)) {
            return BaseAypVisitAction.Status.PENDING;
        }

        if (StringUtils.equalsIgnoreCase(providedRaw, "no")) {
            return BaseAypVisitAction.Status.COMPLETED;
        }

        List<String> sections = getValueList(jsonObject, "rh_sections_provided");
        if (sections.isEmpty()) {
            return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
        }

        boolean missingTopics = false;
        if (sections.contains("hiv_and_aids") && getValueList(jsonObject, "hiv_aids_topics").isEmpty()) {
            missingTopics = true;
        }
        if (sections.contains("stis_rtis") && getValueList(jsonObject, "stis_rtis_topics").isEmpty()) {
            missingTopics = true;
        }
        if (sections.contains("viral_hepatitis") && getValueList(jsonObject, "viral_hepatitis_topics").isEmpty()) {
            missingTopics = true;
        }
        if (sections.contains("protection_against_hiv") && getValueList(jsonObject, "protection_against_hiv_aids_stis_viral_hepatitis").isEmpty()) {
            missingTopics = true;
        }

        if (missingTopics) {
            return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
        }

        return BaseAypVisitAction.Status.COMPLETED;
    }
}
