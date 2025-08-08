package org.smartregister.chw.ayp_sample.presenter;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.chw.ayp.contract.aypProfileContract;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.presenter.BaseaypProfilePresenter;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class BaseAypProfilePresenterTest {

    @Mock
    private aypProfileContract.View view = Mockito.mock(aypProfileContract.View.class);

    @Mock
    private aypProfileContract.Interactor interactor = Mockito.mock(aypProfileContract.Interactor.class);

    @Mock
    private MemberObject aypMemberObject = new MemberObject();

    private BaseaypProfilePresenter profilePresenter = new BaseaypProfilePresenter(view, interactor, aypMemberObject);


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
