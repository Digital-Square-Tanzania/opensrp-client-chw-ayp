package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;

public class AypParentingTrainingParentsActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingTrainingParentsActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        List<String> sections = getTranslatedValueList(jsonObject, "section_selection");
        if (sections.isEmpty()) {
            return null;
        }
        return TextUtils.join(", ", sections);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        List<String> sections = getValueList(jsonObject, "section_selection");
        if (sections.isEmpty()) {
            return BaseAypVisitAction.Status.PENDING;
        }

        if (sections.contains("hiv_problem_among_young_people") && getValueList(jsonObject, "hiv_problem_topics").isEmpty()) {
            return BaseAypVisitAction.Status.PARTIALLY_COMPLETED;
        }

        return BaseAypVisitAction.Status.COMPLETED;
    }
}
