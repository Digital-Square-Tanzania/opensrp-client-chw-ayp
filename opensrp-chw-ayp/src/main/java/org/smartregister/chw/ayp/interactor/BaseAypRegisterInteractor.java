package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ayp.contract.AypRegisterContract;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.AypUtil;

public class BaseAypRegisterInteractor implements AypRegisterContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BaseAypRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BaseAypRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final AypRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                AypUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
