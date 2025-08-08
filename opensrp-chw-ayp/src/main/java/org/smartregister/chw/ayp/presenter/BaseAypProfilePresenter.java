package org.smartregister.chw.ayp.presenter;

import android.content.Context;

import androidx.annotation.Nullable;

import org.smartregister.chw.ayp.contract.AypProfileContract;
import org.smartregister.chw.ayp.domain.MemberObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BaseAypProfilePresenter implements AypProfileContract.Presenter {
    protected WeakReference<AypProfileContract.View> view;
    protected MemberObject memberObject;
    protected AypProfileContract.Interactor interactor;
    protected Context context;

    public BaseAypProfilePresenter(AypProfileContract.View view, AypProfileContract.Interactor interactor, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.memberObject = memberObject;
        this.interactor = interactor;
    }

    @Override
    public void fillProfileData(MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void recordaypButton(@Nullable String visitState) {
        if (getView() == null) {
            return;
        }

        if (("OVERDUE").equals(visitState) || ("DUE").equals(visitState)) {
            if (("OVERDUE").equals(visitState)) {
                getView().setOverDueColor();
            }
        } else {
            getView().hideView();
        }
    }

    @Override
    @Nullable
    public AypProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo(memberObject, getView());
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, getView());
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
