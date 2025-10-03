package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.BaseAypParentalVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;

public class AypParentalVisitInteractor extends BaseAypParentalVisitInteractor {
    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
