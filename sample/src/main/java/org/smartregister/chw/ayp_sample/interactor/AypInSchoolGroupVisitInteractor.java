package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.actionhelper.AypGroupAttendanceActionHelper;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolGroupVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;
import org.smartregister.chw.ayp_sample.activity.SampleGroupAttendanceActionHelper;

public class AypInSchoolGroupVisitInteractor extends BaseAypInSchoolGroupVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }

    @Override
    protected AypGroupAttendanceActionHelper createGroupAttendanceHelper() {
        // Use the sample helper that returns the in-memory sample members
        return new SampleGroupAttendanceActionHelper(context, memberObject, groupId);
    }
}
