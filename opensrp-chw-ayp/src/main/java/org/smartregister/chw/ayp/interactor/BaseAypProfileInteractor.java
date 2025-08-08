package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ayp.contract.AypProfileContract;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.AypUtil;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BaseAypProfileInteractor implements AypProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BaseAypProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAypProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, AypProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("Ayp Visit", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final AypProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                AypUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
