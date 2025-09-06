package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolClientVisitInteractor;
import org.smartregister.chw.ayp.interactor.BaseAypInSchoolGroupVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;


public class AypInSchoolGroupVisitInteractor extends BaseAypInSchoolGroupVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
