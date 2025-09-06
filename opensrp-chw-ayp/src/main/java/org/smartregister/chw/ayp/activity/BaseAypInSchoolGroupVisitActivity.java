package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ayp.interactor.BaseAypInSchoolGroupVisitInteractor;
import org.smartregister.chw.ayp.presenter.AypInSchoolGroupVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypInSchoolGroupVisitActivity extends BaseAypVisitActivity {

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseAypInSchoolGroupVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, profileType);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void registerPresenter() {
        presenter = new AypInSchoolGroupVisitPresenter(memberObject, this, new BaseAypInSchoolGroupVisitInteractor());
    }
}

