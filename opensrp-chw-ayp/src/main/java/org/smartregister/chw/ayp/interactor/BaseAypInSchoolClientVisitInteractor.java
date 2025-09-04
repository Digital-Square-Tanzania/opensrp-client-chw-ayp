package org.smartregister.chw.ayp.interactor;

import android.content.Context;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolClientStatusServicesActionHelper;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class BaseAypInSchoolClientVisitInteractor extends BaseAypVisitInteractor {

    protected final LinkedHashMap<String, BaseAypVisitAction> actionList = new LinkedHashMap<>();
    private final ECSyncHelper syncHelper;
    protected AppExecutors appExecutors;
    protected Map<String, List<VisitDetail>> details = null;
    protected String visitType;
    protected MemberObject memberObject;

    @VisibleForTesting
    public BaseAypInSchoolClientVisitInteractor(AppExecutors appExecutors, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.syncHelper = syncHelper;

    }

    public BaseAypInSchoolClientVisitInteractor() {
        this(new AppExecutors(), AypLibrary.getInstance().getEcSyncHelper());
    }


    protected String getCurrentVisitType() {
        if (StringUtils.isNotBlank(visitType)) {
            return visitType;
        }
        return Constants.EVENT_TYPE.ayp_ENROLLMENT;
    }

    protected void populateActionList(BaseAypVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            try {
                evaluateClientStatus(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }


    private void evaluateClientStatus(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolClientStatusServicesActionHelper actionHelper = new AypInSchoolClientStatusServicesActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_inschool_services_client_status))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_CLIENT_STATUS)
                .build();
        actionList.put(context.getString(R.string.ayp_inschool_services_client_status), action);
    }


    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_IN_SCHOOL_FOLLOW_UP_VISIT;
    }

    protected String getTableName() {
        return Constants.TABLES.AYP_IN_SCHOOL_CLIENT_FOLLOW_UP_VISIT;
    }
}
