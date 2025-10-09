package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ayp.interactor.AypFacilityBiomedicalVisitInteractor;
import org.smartregister.chw.ayp.presenter.AypFacilityBiomedicalVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class AypFacilityBiomedicalVisitActivity extends BaseAypVisitActivity {

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, AypFacilityBiomedicalVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, profileType);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void registerPresenter() {
        presenter = new AypFacilityBiomedicalVisitPresenter(memberObject, this, new AypFacilityBiomedicalVisitInteractor());
    }
}
