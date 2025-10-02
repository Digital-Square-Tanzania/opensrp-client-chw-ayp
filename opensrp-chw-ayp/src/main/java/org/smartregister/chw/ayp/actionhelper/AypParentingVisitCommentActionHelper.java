package org.smartregister.chw.ayp.actionhelper;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;

public class AypParentingVisitCommentActionHelper extends BaseAypParentingVisitActionHelper {

    private static final int MAX_PREVIEW_LENGTH = 60;

    public AypParentingVisitCommentActionHelper(Context context, MemberObject memberObject) {
        super(context, memberObject);
    }

    @Override
    protected String evaluateSubTitle(JSONObject jsonObject) throws JSONException {
        String comment = getValue(jsonObject, "visit_comment");
        if (StringUtils.isBlank(comment)) {
            return null;
        }
        return StringUtils.abbreviate(comment, MAX_PREVIEW_LENGTH);
    }

    @Override
    protected BaseAypVisitAction.Status evaluateStatus(JSONObject jsonObject) throws JSONException {
        return StringUtils.isBlank(getValue(jsonObject, "visit_comment"))
                ? BaseAypVisitAction.Status.PENDING
                : BaseAypVisitAction.Status.COMPLETED;
    }
}
