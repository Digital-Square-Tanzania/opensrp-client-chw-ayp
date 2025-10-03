package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

import java.util.List;

public class AypParentingUnderstandingYouthActionHelper extends BaseAypParentingVisitActionHelper {

    public AypParentingUnderstandingYouthActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        List<String> topics = getTranslatedValueList(jsonObject, "understanding_youth_challenges");
        if (topics.isEmpty()) {
            return null;
        }
        return TextUtils.join(", ", topics);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        return getValueList(jsonObject, "understanding_youth_challenges").isEmpty()
                ? BaseAypVisitAction.Status.PENDING
                : BaseAypVisitAction.Status.COMPLETED;
    }
}
