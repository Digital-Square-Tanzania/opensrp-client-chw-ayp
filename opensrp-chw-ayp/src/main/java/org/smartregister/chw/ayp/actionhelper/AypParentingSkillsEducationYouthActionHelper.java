package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;

public class AypParentingSkillsEducationYouthActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingSkillsEducationYouthActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        List<String> selections = getTranslatedValueList(jsonObject, "skills_education_communication_youth");
        if (selections.isEmpty()) {
            return null;
        }
        return TextUtils.join(", ", selections);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        return getValueList(jsonObject, "skills_education_communication_youth").isEmpty()
                ? BaseAypVisitAction.Status.PENDING
                : BaseAypVisitAction.Status.COMPLETED;
    }
}
