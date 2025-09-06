package org.smartregister.chw.ayp_sample.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONObject;
import org.smartregister.chw.ayp.activity.BaseAypGroupProfileActivity;
import org.smartregister.chw.ayp.activity.BaseAypProfileActivity;
import org.smartregister.chw.ayp.domain.GroupObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;
import org.smartregister.chw.ayp.util.Constants;

import timber.log.Timber;


public class AypInSchoolGroupMemberProfileActivity extends BaseAypGroupProfileActivity {
    private Visit serviceVisit = null;

    public static void startMe(Activity activity, String baseEntityID) {
        Intent intent = new Intent(activity, AypInSchoolGroupMemberProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityID);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Populate with sample group details and members for the sample app.
        GroupObject sampleGroup = new GroupObject("sample-group-1", "In-School Health Club", "Kata ya Mikocheni, Kinondoni", 3);
        setGroupViewWithData(sampleGroup);
        renderMembers(SampleDataFactory.sampleMembers());
    }

    @Override
    public void openGroupDetailsForm() {
        // Launch the sample group visit flow for a sample member
        AypInSchoolGroupVisitActivity.startAypInSchoolGroupVisitActivity(this, "entity-1", false);
    }

    @Override
    public void onAddMember() {
        // Also launch the sample group visit flow from Add Member for demo purposes
        AypInSchoolGroupVisitActivity.startAypInSchoolGroupVisitActivity(this, "entity-1", false);
    }

    @Override
    public void openMemberProfile(MemberObject member) {
        Toast.makeText(this, "Open member: " + member.getFullName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK) {
            try {
                String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                JSONObject form = new JSONObject(jsonString);
                String encounterType = form.getString(Constants.JSON_FORM_EXTRA.EVENT_TYPE);
                switch (encounterType) {
                    case Constants.EVENT_TYPE.ayp_SERVICES:
                        serviceVisit = new Visit();
                        serviceVisit.setProcessed(true);
                        serviceVisit.setJson(jsonString);
                        break;

                }
            } catch (Exception e) {
                Timber.e(e);
            }

        }

    }
}

class SampleDataFactory {
    static java.util.List<MemberObject> sampleMembers() {
        java.util.List<MemberObject> list = new java.util.ArrayList<>();

        MemberObject m1 = new MemberObject();
        m1.setFirstName("Asha");
        m1.setLastName("Mwajuma");
        m1.setRelationalId("family-1");
        m1.setAddress("Kata ya Mikocheni, Kinondoni");
        m1.setGender("Female");
        m1.setBaseEntityId("entity-1");
        list.add(m1);

        MemberObject m2 = new MemberObject();
        m2.setFirstName("Juma");
        m2.setLastName("Omary");
        m2.setRelationalId("family-2");
        m2.setAddress("Kata ya Mikocheni, Kinondoni");
        m2.setGender("Male");
        m2.setBaseEntityId("entity-2");
        list.add(m2);

        MemberObject m3 = new MemberObject();
        m3.setFirstName("Neema");
        m3.setLastName("Mushi");
        m3.setRelationalId("family-3");
        m3.setAddress("Kata ya Mikocheni, Kinondoni");
        m3.setGender("Female");
        m3.setBaseEntityId("entity-3");
        list.add(m3);

        return list;
    }
}
