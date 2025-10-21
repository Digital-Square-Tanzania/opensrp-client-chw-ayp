package org.smartregister.chw.ayp_sample.interactor.aypOutOfSchool;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.aypOutOfSchool.BaseAypOutSchoolClientVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;


public class AypOutSchoolClientServiceVisitInteractor extends BaseAypOutSchoolClientVisitInteractor {

    public AypOutSchoolClientServiceVisitInteractor(String visitType) {
        super(visitType);
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
