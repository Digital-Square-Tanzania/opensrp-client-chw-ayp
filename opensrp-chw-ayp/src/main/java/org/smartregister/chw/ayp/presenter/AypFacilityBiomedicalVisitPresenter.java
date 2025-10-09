package org.smartregister.chw.ayp.presenter;

import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.domain.MemberObject;

public class AypFacilityBiomedicalVisitPresenter extends BaseAypVisitPresenter {

    public AypFacilityBiomedicalVisitPresenter(MemberObject memberObject, BaseAypVisitContract.View view,
                                               BaseAypVisitContract.Interactor interactor) {
        super(memberObject, view, interactor);
    }
}
