package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypGroupAttendanceActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolCseServicesActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolFinancialLiteracyActionHelper;
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

public class BaseAypInSchoolGroupVisitInteractor extends BaseAypVisitInteractor {

    protected final LinkedHashMap<String, BaseAypVisitAction> actionList = new LinkedHashMap<>();
    private final ECSyncHelper syncHelper;
    protected AppExecutors appExecutors;
    protected Map<String, List<VisitDetail>> details = null;
    protected String visitType;
    protected MemberObject memberObject;
    protected String groupId;

    @VisibleForTesting
    public BaseAypInSchoolGroupVisitInteractor(AppExecutors appExecutors, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.syncHelper = syncHelper;
    }

    public BaseAypInSchoolGroupVisitInteractor() {
        this(new AppExecutors(), AypLibrary.getInstance().getEcSyncHelper());
    }

    @Override
    protected String getCurrentVisitType() {
        if (StringUtils.isNotBlank(visitType)) {
            return visitType;
        }
        return Constants.EVENT_TYPE.ayp_ENROLLMENT;
    }

    @Override
    protected void populateActionList(BaseAypVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            try {
                evaluateGroupAttendance(details);
                evaluateCse(details);
                evaluateFinancialLiteracy(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void evaluateGroupAttendance(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypGroupAttendanceActionHelper actionHelper = createGroupAttendanceHelper();

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_group_attendance))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_GROUP_ATTENDANCE)
                .build();
        actionList.put(context.getString(R.string.ayp_group_attendance), action);
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    protected AypGroupAttendanceActionHelper createGroupAttendanceHelper() {
        return new AypGroupAttendanceActionHelper(context, memberObject, groupId);
    }

    private void evaluateCse(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolCseServicesActionHelper actionHelper = new AypInSchoolCseServicesActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_in_school_cse))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_CSE)
                .build();
        actionList.put(context.getString(R.string.ayp_in_school_cse), action);
    }

    private void evaluateFinancialLiteracy(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolFinancialLiteracyActionHelper actionHelper = new AypInSchoolFinancialLiteracyActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_in_school_financial_literacy))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_FINANCIAL_LITERACY)
                .build();
        actionList.put(context.getString(R.string.ayp_in_school_financial_literacy), action);
    }

    @Override
    protected String getEncounterType() {
        return Constants.EVENT_TYPE.ayp_SERVICES;
    }

    @Override
    protected String getTableName() {
        return Constants.TABLES.ayp_SERVICE;
    }
}
