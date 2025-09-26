package org.smartregister.chw.ayp.interactor.aypOutOfSchool;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolHealthBehaviourChangeServiceActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolMedicalServiceActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolNextAppointmentActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolServiceStatusActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolStructuralServiceActionHelper;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.interactor.BaseAypVisitInteractor;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class BaseAypOutSchoolClientVisitInteractor extends BaseAypVisitInteractor {

    protected final LinkedHashMap<String, BaseAypVisitAction> actionList = new LinkedHashMap<>();
    private final ECSyncHelper syncHelper;
    protected AppExecutors appExecutors;
    protected Map<String, List<VisitDetail>> details = null;
    protected String visitType;
    protected MemberObject memberObject;

    @VisibleForTesting
    public BaseAypOutSchoolClientVisitInteractor(AppExecutors appExecutors, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.syncHelper = syncHelper;

    }

    public BaseAypOutSchoolClientVisitInteractor() {
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
                evaluateServiceStatus(details);
                evaluateStructuralServices(details);
                evaluateMedicalServices(details);
                evaluateHealthAndBehaviourChangeService(details);
                fillNextAppointment(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }


    private void evaluateServiceStatus(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolServiceStatusActionHelper actionHelper = new AypOutSchoolServiceStatusActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_service_status))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_SERVICE_STATUS)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_service_status), action);
    }

    private void evaluateStructuralServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolStructuralServiceActionHelper actionHelper = new AypOutSchoolStructuralServiceActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_structural_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_STRUCTURAL_SERVICE)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_structural_services), action);
    }

    private void evaluateMedicalServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolMedicalServiceActionHelper actionHelper = new AypOutSchoolMedicalServiceActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_medical_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_MEDICAL_SERVICE)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_medical_services), action);
    }

    private void evaluateHealthAndBehaviourChangeService(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolHealthBehaviourChangeServiceActionHelper actionHelper = new AypOutSchoolHealthBehaviourChangeServiceActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_health_and_behaviour_change_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_HEALTH_AND_BEHAVIOUR_CHANGE_SERVICES)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_health_and_behaviour_change_services), action);
    }

    private void fillNextAppointment(Map<String, List<VisitDetail>> visitDetails) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolNextAppointmentActionHelper actionHelper = new AypOutSchoolNextAppointmentActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_next_appointment))
                .withOptional(false)
                .withDetails(visitDetails)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_NEXT_APPOINTMENT)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_next_appointment), action);
    }


    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_OUT_SCHOOL_FOLLOW_UP_VISIT;
    }

    protected String getTableName() {
        return Constants.TABLES.AYP_OUT_SCHOOL_CLIENT_FOLLOW_UP_VISIT;
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return AypDao.getOutSchoolMember(memberID);
    }
}
