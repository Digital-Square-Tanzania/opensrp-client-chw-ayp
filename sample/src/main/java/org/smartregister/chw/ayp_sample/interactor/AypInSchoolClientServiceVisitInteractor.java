package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolClientVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;


public class AypInSchoolClientServiceVisitInteractor extends BaseAypInSchoolClientVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
