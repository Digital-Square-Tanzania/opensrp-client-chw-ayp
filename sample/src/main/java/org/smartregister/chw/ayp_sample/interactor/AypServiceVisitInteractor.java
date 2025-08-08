package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypServiceVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;


public class AypServiceVisitInteractor extends BaseAypServiceVisitInteractor {
    public AypServiceVisitInteractor(String visitType) {
        super(visitType);
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
