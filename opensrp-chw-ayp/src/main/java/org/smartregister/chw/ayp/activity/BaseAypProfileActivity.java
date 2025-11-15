package org.smartregister.chw.ayp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.contract.AypProfileContract;
import org.smartregister.chw.ayp.custom_views.BaseAypFloatingMenu;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;
import org.smartregister.chw.ayp.interactor.BaseAypProfileInteractor;
import org.smartregister.chw.ayp.presenter.BaseAypProfilePresenter;
import org.smartregister.chw.ayp.util.AypUtil;
import org.smartregister.chw.ayp.util.AypVisitsUtil;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.domain.AlertStatus;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public abstract class BaseAypProfileActivity extends BaseProfileActivity implements AypProfileContract.View, AypProfileContract.InteractorCallBack {

    protected MemberObject memberObject;
    protected AypProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected TextView textViewRecordayp;
    protected TextView textViewGraduate;
    protected TextView textViewRecordAnc;
    protected TextView textViewContinueayp;
    protected TextView textViewContinueaypService;
    protected TextView manualProcessVisit;
    protected TextView textview_positive_date;
    protected View view_last_visit_row;
    protected View view_most_due_overdue_row;
    protected View view_family_row;
    protected View view_positive_date_row;
    protected RelativeLayout rlLastVisit;
    protected RelativeLayout rlUpcomingServices;
    protected RelativeLayout rlFamilyServicesDue;
    protected RelativeLayout visitStatus;
    protected RelativeLayout visitInProgress;
    protected RelativeLayout aypServiceInProgress;
    protected ImageView imageViewCross;
    protected TextView textViewUndo;
    protected RelativeLayout rlaypPositiveDate;
    protected TextView textViewVisitDone;
    protected TextView textViewId;
    protected RelativeLayout visitDone;
    protected LinearLayout recordVisits;
    protected TextView textViewVisitDoneEdit;
    protected TextView textViewRecordAncNotDone;
    protected String profileType;
    protected BaseAypFloatingMenu baseaypFloatingMenu;
    private TextView tvUpComingServices;
    private TextView tvFamilyStatus;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private ProgressBar progressBar;
    protected TextView aypRiskLabel;


    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BaseAypProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_ayp_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        String baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);
        profileType = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.PROFILE_TYPE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BaseAypProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_last_visit_row = findViewById(R.id.view_last_visit_row);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        view_family_row = findViewById(R.id.view_family_row);
        view_positive_date_row = findViewById(R.id.view_positive_date_row);
        imageViewCross = findViewById(R.id.tick_image);
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);
        textview_positive_date = findViewById(R.id.textview_positive_date);
        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);
        rlaypPositiveDate = findViewById(R.id.rlaypPositiveDate);
        textViewVisitDone = findViewById(R.id.textview_visit_done);
        textViewId = findViewById(R.id.textview_uic_id);
        visitStatus = findViewById(R.id.record_visit_not_done_bar);
        visitDone = findViewById(R.id.visit_done_bar);
        visitInProgress = findViewById(R.id.record_visit_in_progress);
        aypServiceInProgress = findViewById(R.id.record_ayp_service_visit_in_progress);
        recordVisits = findViewById(R.id.record_visits);
        progressBar = findViewById(R.id.progress_bar);
        textViewRecordAncNotDone = findViewById(R.id.textview_record_anc_not_done);
        textViewVisitDoneEdit = findViewById(R.id.textview_edit);
        textViewRecordayp = findViewById(R.id.textview_record_ayp);
        textViewGraduate = findViewById(R.id.textview_graduate);
        textViewContinueayp = findViewById(R.id.textview_continue);
        textViewContinueaypService = findViewById(R.id.continue_ayp_service);
        manualProcessVisit = findViewById(R.id.textview_manual_process);
        textViewRecordAnc = findViewById(R.id.textview_record_anc);
        textViewUndo = findViewById(R.id.textview_undo);
        imageView = findViewById(R.id.imageview_profile);

        textViewRecordAncNotDone.setOnClickListener(this);
        textViewVisitDoneEdit.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        rlUpcomingServices.setOnClickListener(this);
        rlFamilyServicesDue.setOnClickListener(this);
        rlaypPositiveDate.setOnClickListener(this);
        textViewRecordayp.setOnClickListener(this);
        textViewGraduate.setOnClickListener(this);
        textViewContinueayp.setOnClickListener(this);
        textViewContinueaypService.setOnClickListener(this);
        manualProcessVisit.setOnClickListener(this);
        textViewRecordAnc.setOnClickListener(this);
        textViewUndo.setOnClickListener(this);
        aypRiskLabel = findViewById(R.id.risk_label);

        imageRenderHelper = new ImageRenderHelper(this);
        memberObject = getMemberObject(baseEntityId);
        initializePresenter();
        profilePresenter.fillProfileData(memberObject);
        setupViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupViews();
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        setupViews();
    }

    @Override
    protected void setupViews() {
        initializeFloatingMenu();
        setupButtons();
        showUICID(memberObject.getBaseEntityId());
        showLabel(memberObject.getBaseEntityId());
    }

    protected void showUICID(String baseEntityId) {
        if (profileType.equalsIgnoreCase(Constants.PROFILE_TYPES.ayp_PROFILE)) {
//            String tableName = profileType.equalsIgnoreCase(Constants.PROFILE_TYPES.ayp_PROFILE) ? Constants.TABLES.AYP_OUT_SCHOOL_ENROLLMENT : Constants.TABLES.AYP_ENROLLMENT;
            String tableName = Constants.TABLES.AYP_OUT_SCHOOL_ENROLLMENT;
            String UIC_ID = AypDao.getUIC_ID(baseEntityId, tableName);
            if (StringUtils.isNotBlank(UIC_ID)) {
                textViewId.setVisibility(View.VISIBLE);
                textViewId.setText(getString(R.string.uic_id, UIC_ID.toUpperCase(Locale.ROOT)));
            } else {
                textViewId.setVisibility(View.GONE);
            }
        } else {
            textViewId.setVisibility(View.GONE);
        }
    }

    protected void showLabel(String baseEntityId) {
        if (profileType.equalsIgnoreCase(Constants.PROFILE_TYPES.ayp_PROFILE)) {
            String tableName = Constants.TABLES.AYP_OUT_SCHOOL_ENROLLMENT;
            String score = AypDao.getScore(baseEntityId, tableName);

            int scoreValue = StringUtils.isNotEmpty(score) ? Integer.parseInt(score) : 0;

            int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
            String gender = memberObject.getGender();

            boolean high_agyw_less_than_19 = (gender.equalsIgnoreCase("Female") &&  age < 19 && scoreValue > 10 && scoreValue < 16);
            boolean high_abym_less_than_19 = (gender.equalsIgnoreCase("Male") && age < 19 && scoreValue > 9 && scoreValue < 14);
            boolean high_agyw_less_than_19_24 = (gender.equalsIgnoreCase("Female") && age > 18 && scoreValue > 8 && scoreValue < 12);
            boolean high_abym_less_than_19_24 = (gender.equalsIgnoreCase("Male") && age >= 18 && scoreValue > 7 && scoreValue < 11);

            boolean medium_agyw_less_than_19 = (gender.equalsIgnoreCase("Female") &&  age < 19 && scoreValue > 5 && scoreValue < 10);
            boolean medium_abym_less_than_19 = (gender.equalsIgnoreCase("Male") && age < 19 && scoreValue > 4 && scoreValue < 9);
            boolean medium_agyw_less_than_19_24 = (gender.equalsIgnoreCase("Female") && age >= 19 && scoreValue > 4 && scoreValue < 8);
            boolean medium_abym_less_than_19_24 = (gender.equalsIgnoreCase("Male") && age >= 19 && scoreValue > 3 && scoreValue < 7);

            boolean low_agyw_less_than_19 = (gender.equalsIgnoreCase("Female") &&  age < 19 && scoreValue > 0 && scoreValue < 5);
            boolean low_abym_less_than_19 = (gender.equalsIgnoreCase("Male") && age < 19 && scoreValue > 0 && scoreValue < 4);
            boolean low_agyw_less_than_19_24 = (gender.equalsIgnoreCase("Female") && age >= 19 && scoreValue > 0 && scoreValue < 4);
            boolean low_abym_less_than_19_24 = (gender.equalsIgnoreCase("Male") && age >= 19 && scoreValue > 0 && scoreValue < 3);

            if (StringUtils.isNotBlank(score)) {
                aypRiskLabel.setVisibility(View.VISIBLE);
                if(high_agyw_less_than_19 || high_abym_less_than_19 || high_agyw_less_than_19_24 || high_abym_less_than_19_24){
                    aypRiskLabel.setText("HIGH RISK");
                    aypRiskLabel.setTextColor(Color.WHITE);
                    aypRiskLabel.setBackgroundColor(Color.RED);
                }
                if(medium_agyw_less_than_19 || medium_abym_less_than_19 || medium_agyw_less_than_19_24 || medium_abym_less_than_19_24) {
                    aypRiskLabel.setText("MODERATE RISK");
                    aypRiskLabel.setTextColor(Color.WHITE);
                    aypRiskLabel.setBackgroundColor(Color.parseColor("#FFA500")); // Standard orange
                }
                if(low_agyw_less_than_19 || low_abym_less_than_19 || low_agyw_less_than_19_24 || low_abym_less_than_19_24){
                    aypRiskLabel.setText("LOW RISK");
                    aypRiskLabel.setTextColor(Color.WHITE);
                    aypRiskLabel.setBackgroundColor(Color.GRAY);
                }
            } else {
                aypRiskLabel.setVisibility(View.GONE);
            }
        } else {
            aypRiskLabel.setVisibility(View.GONE);
        }
    }

    protected void setupButtons() {
        if(getAypOutSchoolVisit() != null){
            if (!getAypOutSchoolVisit().getProcessed() && AypVisitsUtil.getaypVisitStatus(getAypOutSchoolVisit()).equalsIgnoreCase(AypVisitsUtil.Pending)) {
                manualProcessVisit.setVisibility(View.VISIBLE);
                textViewContinueaypService.setText(R.string.edit_visit);
                manualProcessVisit.setOnClickListener(view -> {
                    try {
                        AypVisitsUtil.manualProcessVisit(getAypOutSchoolVisit());
                        displayToast(R.string.ayp_out_school_service_conducted);
                        setupViews();
                    } catch (Exception e) {
                        Timber.d(e);
                    }
                });
            } else {
                manualProcessVisit.setVisibility(View.GONE);
                textViewGraduate.setVisibility(View.GONE);
            }
        }

        if (isVisitOnProgress(getAypOutSchoolVisit())) {
//            textViewProcedureVmmc.setVisibility(View.GONE);
            aypServiceInProgress.setVisibility(View.VISIBLE);
        } else {
//            textViewProcedureVmmc.setVisibility(View.VISIBLE);
            aypServiceInProgress.setVisibility(View.GONE);
        }
    }

    protected Visit getAypOutSchoolVisit() {
        return AypLibrary.getInstance().visitRepository().getLatestVisit(memberObject.getBaseEntityId(), Constants.EVENT_TYPE.AYP_OUT_SCHOOL_FOLLOW_UP_VISIT);
    }

    protected Visit getServiceVisit() {
        return AypLibrary.getInstance().visitRepository().getLatestVisit(memberObject.getBaseEntityId(), Constants.EVENT_TYPE.AYP_SERVICES);
    }


    protected void processAypService() {
        rlLastVisit.setVisibility(View.VISIBLE);
        findViewById(R.id.family_ayp_head).setVisibility(View.VISIBLE);
    }


    protected MemberObject getMemberObject(String baseEntityId) {
        return AypDao.getMember(baseEntityId);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (id == R.id.rlUpcomingServices) {
            this.openUpcomingService();
        } else if (id == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        } else if (id == R.id.textview_record_ayp) {
            this.openFollowupVisit();
        } else if (id == R.id.textview_graduate) {
            this.graduateForm();
        } else if (id == R.id.continue_ayp_service) {
            this.continueService();
        } else if (id == R.id.textview_continue) {
            this.continueDischarge();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BaseAypProfilePresenter(this, new BaseAypProfileInteractor(), memberObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(memberObject.getPhoneNumber())) {
            baseaypFloatingMenu = new BaseAypFloatingMenu(this, memberObject);
            baseaypFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(baseaypFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    public void hideView() {
        textViewRecordayp.setVisibility(View.GONE);
    }

    @Override
    public void openFollowupVisit() {
        //Implement in application
    }

    @Override
    public void graduateForm() {
        //Implement in application
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        int age = new Period(new DateTime(memberObject.getAge()), new DateTime()).getYears();
        textViewName.setText(String.format("%s %s %s, %d", memberObject.getFirstName(), memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewGender.setText(AypUtil.getGenderTranslated(this, memberObject.getGender()));
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());


        if (StringUtils.isNotBlank(memberObject.getPrimaryCareGiver()) && memberObject.getPrimaryCareGiver().equals(memberObject.getBaseEntityId())) {
            findViewById(R.id.primary_ayp_caregiver).setVisibility(View.GONE);
        }
        if (memberObject.getaypTestDate() != null) {
            textview_positive_date.setText(getString(R.string.ayp_positive) + " " + formatTime(memberObject.getaypTestDate()));
        }
    }

    @Override
    public void setOverDueColor() {
        textViewRecordayp.setBackground(getResources().getDrawable(R.drawable.record_btn_selector_overdue));

    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {
        showProgressBar(false);
        if (status == AlertStatus.complete) return;
        view_most_due_overdue_row.setVisibility(View.GONE);
        rlUpcomingServices.setVisibility(View.GONE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(AypUtil.fromHtml(getString(R.string.vaccine_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(AypUtil.fromHtml(getString(R.string.vaccine_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {
        showProgressBar(false);
        if (status == AlertStatus.complete) {
            setFamilyStatus(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            setFamilyStatus(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(AypUtil.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    private void setFamilyStatus(String familyStatus) {
        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.GONE);
        tvFamilyStatus.setText(familyStatus);
    }

    @Override
    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void openUpcomingService() {
        //implement
    }

    @Override
    public void openFamilyDueServices() {
        //implement
    }

    @Nullable
    private String formatTime(Date dateTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(dateTime);
        } catch (Exception e) {
            Timber.d(e);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
//            finish();
        }
    }

    protected boolean isVisitOnProgress(Visit visit) {

        return visit != null && !visit.getProcessed();
    }
}
