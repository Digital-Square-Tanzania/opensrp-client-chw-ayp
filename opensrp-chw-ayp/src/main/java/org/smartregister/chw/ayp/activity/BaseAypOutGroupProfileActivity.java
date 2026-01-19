package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.adapter.AypGroupMembersAdapter;
import org.smartregister.chw.ayp.contract.AypGroupProfileContract;
import org.smartregister.chw.ayp.contract.AypProfileContract;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.GroupObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;
import org.smartregister.chw.ayp.interactor.aypOutOfSchool.BaseAypOutGroupProfileInteractor;
import org.smartregister.chw.ayp.presenter.BaseAypGroupProfilePresenter;
import org.smartregister.chw.ayp.util.AypVisitsUtil;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.domain.AlertStatus;

import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import timber.log.Timber;

public class BaseAypOutGroupProfileActivity extends AppCompatActivity implements AypGroupProfileContract.View, View.OnClickListener, AypProfileContract.InteractorCallBack {

    protected TextView tvGroupName;
    protected TextView tvGroupType;
    protected TextView tvGroupAgeBand;
    protected TextView tvGroupMemberCount;
    protected TextView btnProvideDetails;
    protected TextView textviewProcessServiceVisit;
    protected TextView btnAddMember;
    protected RecyclerView rvMembers;
    protected ProgressBar progressBar;
    protected RelativeLayout rlLastVisit;

    protected AypGroupProfileContract.Presenter presenter;
    protected AypGroupMembersAdapter adapter;

    public static void startGroupProfileActivity(Activity activity, String groupId, @Nullable String groupName) {
        Intent intent = new Intent(activity, BaseAypOutGroupProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.GROUP_ID, groupId);
        if (groupName != null) intent.putExtra(Constants.ACTIVITY_PAYLOAD.GROUP_NAME, groupName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayp_group_profile);

        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseAypOutGroupProfileActivity.this.finish());
        View appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21 && appBarLayout != null) {
            appBarLayout.setOutlineProvider(null);
        }

        tvGroupName = findViewById(R.id.textview_group_name);
        tvGroupType = findViewById(R.id.textview_group_type);
        tvGroupAgeBand = findViewById(R.id.textview_group_age_band);
        tvGroupMemberCount = findViewById(R.id.textview_group_member_count);
        btnProvideDetails = findViewById(R.id.textview_provide_group_details);
        rlLastVisit = findViewById(R.id.rlLastVisit);
        textviewProcessServiceVisit = findViewById(R.id.textview_process_service_visit);
        btnAddMember = findViewById(R.id.textview_add_member);
        rvMembers = findViewById(R.id.recycler_members);
        progressBar = findViewById(R.id.progress_bar);

        btnProvideDetails.setOnClickListener(this);
        textviewProcessServiceVisit.setOnClickListener(this);
        btnAddMember.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);

        rvMembers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AypGroupMembersAdapter(this::openMemberProfile);
        rvMembers.setAdapter(adapter);

        presenter = new BaseAypGroupProfilePresenter(this, new BaseAypOutGroupProfileInteractor());
        String groupId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.GROUP_ID);
        String groupName = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.GROUP_NAME);
        presenter.load(groupId, groupName);
    }

    @Override
    public void showProgressBar(boolean status) {
        if (progressBar != null) progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setGroupViewWithData(GroupObject groupObject) {
        tvGroupName.setText(groupObject.getGroupName());
        // Type and Age-band can be set by the app module subclass when available
        if (tvGroupType != null) tvGroupType.setText("");
        if (tvGroupAgeBand != null) tvGroupAgeBand.setText("");
        if (tvGroupMemberCount != null) {
            tvGroupMemberCount.setText(getString(R.string.group_members_count, AypDao.getOutSchoolGroupMembers(groupObject.getGroupId()).size()));
        }
    }

    @Override
    public void renderMembers(List<MemberObject> members) {
        adapter.setItems(members);
        if (tvGroupMemberCount != null) {
            tvGroupMemberCount.setText(getString(R.string.group_members_count, members != null ? members.size() : 0));
        }
        if (btnProvideDetails != null) {
            boolean hasMembers = members != null && !members.isEmpty();
            btnProvideDetails.setVisibility(hasMembers ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void openGroupDetailsForm() {
        // Implement in app module: launch JSON form for group details
    }

    @Override
    public void onAddMember() {
        // Implement in app module: navigate to add member flow
    }

    @Override
    public void openMemberProfile(MemberObject member) {
        // Implement in app module: open member profile activity
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.textview_provide_group_details) {
            openGroupDetailsForm();
        } else if (id == R.id.textview_add_member) {
            onAddMember();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        }
        else if (id == R.id.textview_process_service_visit) {
            this.processGroupService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPage();
    }

    private void refreshPage() {
        processGroupService();   // reload UI logic
        // reload data from DB if needed
        if(getAypOutSchoolGroupVisit() != null){
            String date = getDateCreated(getAypOutSchoolGroupVisit().getJson());
            boolean isToday = isDateToday(date);
            if(isToday) {
                btnProvideDetails.setVisibility(View.GONE);
            }
        }
    }

    public static String getDateCreated(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.optString("dateCreated");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isDateToday(String isoDate) {
        try {
            SimpleDateFormat isoFormat =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date createdDate = isoFormat.parse(isoDate);

            SimpleDateFormat dayFormat =
                    new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

            String createdDay = dayFormat.format(createdDate);
            String todayDay = dayFormat.format(new Date());

            return createdDay.equals(todayDay);

        } catch (Exception e) {
            return false;
        }
    }


    public void processGroupService() {
        if(getAypOutSchoolGroupVisit() != null){
            if (!getAypOutSchoolGroupVisit().getProcessed() && AypVisitsUtil.getaypVisitStatus(getAypOutSchoolGroupVisit()).equalsIgnoreCase(AypVisitsUtil.Pending)) {
                textviewProcessServiceVisit.setVisibility(View.VISIBLE);
                btnProvideDetails.setVisibility(View.GONE);

//                textViewContinueaypService.setText(R.string.edit_visit);
                textviewProcessServiceVisit.setOnClickListener(view -> {
                    try {
                        AypVisitsUtil.manualProcessVisit(getAypOutSchoolGroupVisit());
                        Toast.makeText(this, R.string.ayp_out_school_service_conducted, Toast.LENGTH_SHORT).show();

                        // Refresh page
                        recreate();
                    } catch (Exception e) {
                        Timber.d(e);
                    }
                });
            } else {
                textviewProcessServiceVisit.setVisibility(View.GONE);
                rlLastVisit.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onGroupLoaded(GroupObject groupObject) {

    }

    @Override
    public void onMembersLoaded(List<MemberObject> members) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }

    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {

    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {

    }

    @Override
    public void startServiceForm() {

    }

    @Override
    public void graduateForm() {

    }

    @Override
    public void continueService() {

    }

    @Override
    public void continueDischarge() {

    }

    protected Visit getAypOutSchoolGroupVisit() {
        return  null;
    }
}
