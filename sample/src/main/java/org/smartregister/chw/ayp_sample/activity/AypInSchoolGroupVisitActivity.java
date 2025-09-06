package org.smartregister.chw.ayp_sample.activity;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONObject;
import org.smartregister.chw.ayp.activity.BaseAypInSchoolGroupVisitActivity;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.presenter.BaseAypVisitPresenter;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.chw.ayp_sample.interactor.AypInSchoolGroupVisitInteractor;


public class AypInSchoolGroupVisitActivity extends BaseAypInSchoolGroupVisitActivity {
    public static void startAypInSchoolGroupVisitActivity(Activity activity, String baseEntityId, Boolean editMode) {
        startAypInSchoolGroupVisitActivity(activity, baseEntityId, editMode, null, null);
    }

    public static void startAypInSchoolGroupVisitActivity(Activity activity, String baseEntityId, Boolean editMode, String groupId, String groupName) {
        Intent intent = new Intent(activity, AypInSchoolGroupVisitActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.EDIT_MODE, editMode);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE, Constants.PROFILE_TYPES.ayp_PROFILE);
        if (groupId != null) intent.putExtra(Constants.ACTIVITY_PAYLOAD.GROUP_ID, groupId);
        if (groupName != null) intent.putExtra(Constants.ACTIVITY_PAYLOAD.GROUP_NAME, groupName);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return EntryActivity.getSampleMember();
    }

    @Override
    protected void registerPresenter() {
        AypInSchoolGroupVisitInteractor interactor = new AypInSchoolGroupVisitInteractor();
        String groupId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.GROUP_ID);
        interactor.setGroupId(groupId);
        presenter = new BaseAypVisitPresenter(memberObject, this, interactor);
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, SampleJsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }


    @Override
    public void submittedAndClose(String results) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.JSON_FORM_EXTRA.JSON, results);
        setResult(Activity.RESULT_OK, returnIntent);
        close();
    }

}
