package org.smartregister.chw.ayp_sample.activity;

import android.content.Context;

import org.smartregister.chw.ayp.actionhelper.AypGroupAttendanceActionHelper;
import org.smartregister.chw.ayp.domain.MemberObject;

import java.util.List;

/**
 * Sample override to supply group members from in-memory sample data
 */
public class SampleGroupAttendanceActionHelper extends AypGroupAttendanceActionHelper {

    public SampleGroupAttendanceActionHelper(Context context, MemberObject memberObject, String groupId) {
        super(context, memberObject, groupId);
    }

    @Override
    protected List<MemberObject> getMembersForGroup() {
        // Use the sample member list defined in the sample profile activity
        return SampleDataFactory.sampleMembers();
    }
}

