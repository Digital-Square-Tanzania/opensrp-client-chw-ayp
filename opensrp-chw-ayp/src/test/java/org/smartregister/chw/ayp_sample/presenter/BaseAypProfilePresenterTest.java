package org.smartregister.chw.ayp_sample.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.ayp.contract.AypProfileContract;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.presenter.BaseAypProfilePresenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseAypProfilePresenterTest {

    @Mock
    private AypProfileContract.View view = Mockito.mock(AypProfileContract.View.class);

    @Mock
    private AypProfileContract.Interactor interactor = Mockito.mock(AypProfileContract.Interactor.class);

    @Mock
    private MemberObject aypMemberObject = new MemberObject();

    private BaseAypProfilePresenter profilePresenter = new BaseAypProfilePresenter(view, interactor, aypMemberObject);


    @Test
    public void fillProfileDataCallsSetProfileViewWithDataWhenPassedMemberObject() {
        profilePresenter.fillProfileData(aypMemberObject);
        verify(view).setProfileViewWithData();
    }

    @Test
    public void fillProfileDataDoesntCallsSetProfileViewWithDataIfMemberObjectEmpty() {
        profilePresenter.fillProfileData(null);
        verify(view, never()).setProfileViewWithData();
    }

    @Test
    public void malariaTestDatePeriodIsLessThanSeven() {
        profilePresenter.recordaypButton("");
        verify(view).hideView();
    }

    @Test
    public void malariaTestDatePeriodIsMoreThanFourteen() {
        profilePresenter.recordaypButton("EXPIRED");
        verify(view).hideView();
    }

    @Test
    public void refreshProfileBottom() {
        profilePresenter.refreshProfileBottom();
        verify(interactor).refreshProfileInfo(aypMemberObject, profilePresenter.getView());
    }

    @Test
    public void saveForm() {
        profilePresenter.saveForm(null);
        verify(interactor).saveRegistration(null, view);
    }
}
