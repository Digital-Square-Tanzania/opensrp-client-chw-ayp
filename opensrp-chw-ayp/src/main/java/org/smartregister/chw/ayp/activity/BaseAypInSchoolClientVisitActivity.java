package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolClientVisitInteractor;
import org.smartregister.chw.ayp.presenter.BaseAypVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypInSchoolClientVisitActivity extends BaseAypVisitActivity {

    private static final String TAG = BaseAypInSchoolClientVisitActivity.class.getCanonicalName();

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseAypInSchoolClientVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, isEditMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, profileType);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    protected MemberObject getMemberObject(String baseEntityId) {
        return AypDao.getMember(baseEntityId);
    }

    @Override
    protected void registerPresenter() {
        presenter = new BaseAypVisitPresenter(memberObject, this, new BaseAypInSchoolClientVisitInteractor());
    }
}
