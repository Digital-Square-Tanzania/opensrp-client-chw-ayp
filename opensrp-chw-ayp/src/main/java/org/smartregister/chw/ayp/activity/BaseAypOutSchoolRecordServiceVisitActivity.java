package org.smartregister.chw.ayp.activity;

import static org.smartregister.chw.ayp.util.Constants.EVENT_TYPE.AYP_OUT_SCHOOL_FOLLOW_UP_VISIT;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolClientVisitInteractor;
import org.smartregister.chw.ayp.interactor.aypOutOfSchool.BaseAypOutSchoolClientVisitInteractor;
import org.smartregister.chw.ayp.presenter.BaseAypVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypOutSchoolRecordServiceVisitActivity extends BaseAypVisitActivity {

    private static final String TAG = BaseAypOutSchoolRecordServiceVisitActivity.class.getCanonicalName();

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, BaseAypOutSchoolRecordServiceVisitActivity.class);
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
        presenter = new BaseAypVisitPresenter(memberObject, this, new BaseAypOutSchoolClientVisitInteractor(AYP_OUT_SCHOOL_FOLLOW_UP_VISIT));
    }
}
