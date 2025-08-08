package org.smartregister.chw.ayp.interactor;


import android.content.Context;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypHtsActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypMedicalHistoryActionHelper;
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

public class BaseAypServiceVisitInteractor extends BaseAypVisitInteractor {

    protected BaseAypVisitContract.InteractorCallBack callBack;

    String visitType;
    private final AypLibrary aypLibrary;
    private final LinkedHashMap<String, BaseAypVisitAction> actionList;
    protected AppExecutors appExecutors;
    private ECSyncHelper syncHelper;
    private Context mContext;


    @VisibleForTesting
    public BaseAypServiceVisitInteractor(AppExecutors appExecutors, AypLibrary aypLibrary, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.aypLibrary = aypLibrary;
        this.syncHelper = syncHelper;
        this.actionList = new LinkedHashMap<>();
    }

    public BaseAypServiceVisitInteractor(String visitType) {
        this(new AppExecutors(), AypLibrary.getInstance(), AypLibrary.getInstance().getEcSyncHelper());
        this.visitType = visitType;
    }

    @Override
    protected String getCurrentVisitType() {
        if (StringUtils.isNotBlank(visitType)) {
            return visitType;
        }
        return super.getCurrentVisitType();
    }

    @Override
    protected void populateActionList(BaseAypVisitContract.InteractorCallBack callBack) {
        this.callBack = callBack;
        final Runnable runnable = () -> {
            try {
                evaluateaypMedicalHistory(details);
                evaluateaypPhysicalExam(details);
                evaluateaypHTS(details);

            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void evaluateaypMedicalHistory(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {

        AypMedicalHistoryActionHelper actionHelper = new aypMedicalHistory(mContext, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_medical_history))
                .withOptional(true)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.ayp_FOLLOWUP_FORMS.MEDICAL_HISTORY)
                .build();
        actionList.put(context.getString(R.string.ayp_medical_history), action);

    }

    private void evaluateaypPhysicalExam(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {

        aypPhysicalExamActionHelper actionHelper = new aypPhysicalExamActionHelper(mContext, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_physical_examination))
                .withOptional(true)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.ayp_FOLLOWUP_FORMS.PHYSICAL_EXAMINATION)
                .build();
        actionList.put(context.getString(R.string.ayp_physical_examination), action);
    }

    private void evaluateaypHTS(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {

        AypHtsActionHelper actionHelper = new AypHtsActionHelper(mContext, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_hts))
                .withOptional(true)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.ayp_FOLLOWUP_FORMS.HTS)
                .build();
        actionList.put(context.getString(R.string.ayp_hts), action);
    }

    @Override
    protected String getEncounterType() {
        return Constants.EVENT_TYPE.ayp_SERVICES;
    }

    @Override
    protected String getTableName() {
        return Constants.TABLES.ayp_SERVICE;
    }

    private class aypMedicalHistory extends org.smartregister.chw.ayp.actionhelper.AypMedicalHistoryActionHelper {


        public aypMedicalHistory(Context context, MemberObject memberObject) {
            super(context, memberObject);
        }

        @Override
        public String postProcess(String s) {
            if (StringUtils.isNotBlank(medical_history)) {
                try {
                    evaluateaypPhysicalExam(details);
                    evaluateaypHTS(details);
                } catch (BaseAypVisitAction.ValidationException e) {
                    e.printStackTrace();
                }
            }
            new AppExecutors().mainThread().execute(() -> callBack.preloadActions(actionList));
            return super.postProcess(s);
        }

    }

    private class aypPhysicalExamActionHelper extends org.smartregister.chw.ayp.actionhelper.AypPhysicalExamActionHelper {

        public aypPhysicalExamActionHelper(Context context, MemberObject memberObject) {
            super(context, memberObject);
        }

        @Override
        public String postProcess(String s) {
            if (StringUtils.isNotBlank(medical_history)) {
                try {
                    evaluateaypHTS(details);
                } catch (BaseAypVisitAction.ValidationException e) {
                    e.printStackTrace();
                }
            }
            new AppExecutors().mainThread().execute(() -> callBack.preloadActions(actionList));
            return super.postProcess(s);
        }

    }

}
