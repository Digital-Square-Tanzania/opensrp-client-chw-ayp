package org.smartregister.chw.ayp_sample.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.vijay.jsonwizard.activities.JsonWizardFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;
import com.vijay.jsonwizard.factory.FileSourceFactoryHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.util.AypJsonFormUtils;
import org.smartregister.chw.ayp.util.DBConstants;
import org.smartregister.chw.ayp.util.JsonFormUtils;
import org.smartregister.chw.ayp.util.Constants;

import org.smartregister.chw.ayp_sample.R;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.FormUtils;
import org.smartregister.view.activity.SecuredActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class EntryActivity extends SecuredActivity implements View.OnClickListener, BaseAypVisitContract.VisitView {
    private static MemberObject aypMemberObject;

    public static MemberObject getSampleMember() {
        Map<String, String> details = new HashMap<>();
        details.put(DBConstants.KEY.FIRST_NAME, "Glory");
        details.put(DBConstants.KEY.LAST_NAME, "Juma");
        details.put(DBConstants.KEY.MIDDLE_NAME, "Wambui");
        details.put(DBConstants.KEY.DOB, "1982-01-18T03:00:00.000+03:00");
        details.put(DBConstants.KEY.LAST_HOME_VISIT, "");
        details.put(DBConstants.KEY.VILLAGE_TOWN, "Lavingtone #221");
        details.put(DBConstants.KEY.FAMILY_NAME, "Jumwa");
        details.put(DBConstants.KEY.UNIQUE_ID, "3503504");
        details.put(DBConstants.KEY.BASE_ENTITY_ID, "3503504");
        details.put(DBConstants.KEY.FAMILY_HEAD, "3503504");
        details.put(DBConstants.KEY.PHONE_NUMBER, "0934567543");
        CommonPersonObjectClient commonPersonObject = new CommonPersonObjectClient("", details, "Yo");
        commonPersonObject.setColumnmaps(details);

        if (aypMemberObject == null) {
            aypMemberObject = new MemberObject();
            aypMemberObject.setFirstName("Glory");
            aypMemberObject.setLastName("Juma");
            aypMemberObject.setMiddleName("Ali");
            aypMemberObject.setGender("Female");
            aypMemberObject.setMartialStatus("Married");
            aypMemberObject.setDob("1982-01-18T03:00:00.000+03:00");
            aypMemberObject.setAge("30");
            aypMemberObject.setUniqueId("3503504");
            aypMemberObject.setBaseEntityId("3503504");
            aypMemberObject.setFamilyBaseEntityId("3503504");
            aypMemberObject.setAddress("Njiro");
        }

        return aypMemberObject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.ayp_activity).setOnClickListener(this);
        findViewById(R.id.ayp_home_visit).setOnClickListener(this);
        findViewById(R.id.ayp_in_school_client_profile).setOnClickListener(this);
        findViewById(R.id.ayp_in_school_group_profile).setOnClickListener(this);

        /* out of school */
        findViewById(R.id.ayp_out_school_client_profile).setOnClickListener(this);
        findViewById(R.id.ayp_out_school_group_profile).setOnClickListener(this);
        findViewById(R.id.ayp_out_school_screening).setOnClickListener(this);
    }

    @Override
    protected void onCreation() {
        Timber.v("onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("onCreation");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ayp_activity:
                startActivity(new Intent(this, AypRegisterActivity.class));
                break;
            case R.id.ayp_home_visit:
                AypInSchoolClientVisitActivity.startAypInSchoolClientVisitActivity(this, "12345", true);
                break;
            case R.id.ayp_in_school_client_profile:
                AypInSchoolClientMemberProfileActivity.startMe(this, "12345");
                break;
            case R.id.ayp_in_school_group_profile:
                AypInSchoolGroupMemberProfileActivity.startMe(this, "12345");
                break;
            case R.id.ayp_out_school_client_profile:
                AypOutSchoolClientMemberProfileActivity.startMe(this, "12345");
                break;
            case R.id.ayp_out_school_group_profile:
                AypOutSchoolGroupMemberProfileActivity.startMe(this, "12345");
                break;
            case R.id.ayp_out_school_screening:
                try {
                    startForm("ayp_out_school_screening");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("TimberArgCount")
    private void startForm(String formName) throws Exception {
        JSONObject jsonForm = FileSourceFactoryHelper.getFileSource("").getFormFromFile(getApplicationContext(), formName);

        String currentLocationId = "Tanzania";
        if (jsonForm != null) {

            jsonForm.getJSONObject("metadata").put("encounter_location", currentLocationId);
            Intent intent = new Intent(this, JsonWizardFormActivity.class);
            intent.putExtra("json", jsonForm.toString());

            Form form = new Form();
            form.setWizard(true);
            form.setNextLabel("Next");
            form.setPreviousLabel("Previous");
            form.setSaveLabel("Save");
            form.setHideSaveLabel(true);

            intent.putExtra("form", form);
            startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
        }
    }



    @Override
    public void onDialogOptionUpdated(String jsonString) {
        Timber.v("onDialogOptionUpdated %s", jsonString);
    }

    @Override
    public Context getMyContext() {
        return this;
    }
}