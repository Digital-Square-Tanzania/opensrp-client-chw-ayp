package org.smartregister.chw.ayp_sample.interactor;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.interactor.AypFacilityBiomedicalVisitInteractor;
import org.smartregister.chw.ayp_sample.activity.EntryActivity;

public class AypFacilityBiomedicalVisitSampleInteractor extends AypFacilityBiomedicalVisitInteractor {

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return EntryActivity.getSampleMember();
    }
}
