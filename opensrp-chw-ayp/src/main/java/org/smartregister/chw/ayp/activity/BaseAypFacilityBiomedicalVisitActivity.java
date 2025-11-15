package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.AypFacilityBiomedicalVisitInteractor;
import org.smartregister.chw.ayp.presenter.AypFacilityBiomedicalVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypFacilityBiomedicalVisitActivity extends BaseAypVisitActivity {

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseAypFacilityBiomedicalVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, profileType);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void registerPresenter() {
        presenter = new AypFacilityBiomedicalVisitPresenter(memberObject, this, new AypFacilityBiomedicalVisitInteractor());
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        MemberObject member = AypDao.getFacilityMember(baseEntityId);
        return member != null ? member : super.getMemberObject(baseEntityId);
    }
}
