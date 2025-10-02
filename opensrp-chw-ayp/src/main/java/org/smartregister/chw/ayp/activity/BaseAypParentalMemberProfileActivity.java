package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;
import org.smartregister.chw.ayp.util.Constants;

public class BaseAypParentalMemberProfileActivity extends BaseAypProfileActivity {

    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseAypParentalMemberProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        super.onCreation();
        refreshMedicalHistory(false);
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        boolean showLastVisit = hasHistory || getLatestParentalVisit() != null;
        rlLastVisit.setVisibility(showLastVisit ? View.VISIBLE : View.GONE);
    }

    private Visit getLatestParentalVisit() {
        AypLibrary library = AypLibrary.getInstance();
        if (library == null || memberObject == null) {
            return null;
        }
        return library.visitRepository().getLatestVisit(memberObject.getBaseEntityId(), Constants.EVENT_TYPE.AYP_PARENTAL_SERVICES);
    }

    @Override
    protected Visit getServiceVisit() {
        Visit visit = getLatestParentalVisit();
        return visit != null ? visit : super.getServiceVisit();
    }

    @Override
    public void continueService() {
        // No in-progress service continuation logic defined for parental profile.
    }

    @Override
    public void continueDischarge() {
        // No discharge flow for parental profile currently.
    }

    @Override
    public void openFollowupVisit() {
        BaseAypParentalVisitActivity.startMe(this, memberObject.getBaseEntityId(), false);
    }

    @Override
    public void startServiceForm() {
        BaseAypParentalVisitActivity.startMe(this, memberObject.getBaseEntityId(), false);
    }

    @Override
    public void openMedicalHistory() {
        BaseAypParentalMedicalHistoryActivity.startMe(this, memberObject);
    }

    @Override
    protected MemberObject getMemberObject(String baseEntityId) {
        return AypDao.getParentalMember(baseEntityId);
    }
}
