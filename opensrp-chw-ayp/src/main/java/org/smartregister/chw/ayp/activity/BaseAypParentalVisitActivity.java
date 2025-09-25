package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypParentalVisitInteractor;
import org.smartregister.chw.ayp.presenter.BaseAypParentalVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypParentalVisitActivity extends BaseAypVisitActivity {

    private static final String TAG = BaseAypParentalVisitActivity.class.getCanonicalName();

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseAypParentalVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, profileType);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAypParentalVisitPresenter(memberObject, this, new BaseAypParentalVisitInteractor());
    }

    @Override
    public void redrawHeader(MemberObject memberObject) {
        super.redrawHeader(memberObject);
        String scopeTitle = getString(R.string.ayp_parental_services_visit_title);
        CharSequence currentTitle = tvTitle.getText();
        if (StringUtils.isNotBlank(scopeTitle)) {
            if (currentTitle != null && StringUtils.isNotBlank(currentTitle.toString())) {
                tvTitle.setText(scopeTitle + " • " + currentTitle.toString());
            } else {
                tvTitle.setText(scopeTitle);
            }
        }
    }
}
