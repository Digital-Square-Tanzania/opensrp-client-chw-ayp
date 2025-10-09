package org.smartregister.chw.ayp.interactor.aypOutOfSchool;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutGroupAttendanceActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolGroupNextAppointActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolGroupStructuralActionHelper;
import org.smartregister.chw.ayp.actionhelper.aypOutOfSchool.AypOutSchoolSBCServiceActionHelper;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
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

public class BaseAypOutSchoolGroupVisitInteractor extends BaseAypVisitInteractor {

    protected final LinkedHashMap<String, BaseAypVisitAction> actionList = new LinkedHashMap<>();
    private final ECSyncHelper syncHelper;
    protected AppExecutors appExecutors;
    protected Map<String, List<VisitDetail>> details = null;
    protected String visitType;
    protected MemberObject memberObject;
    protected String groupId;

    @VisibleForTesting
    public BaseAypOutSchoolGroupVisitInteractor(AppExecutors appExecutors, ECSyncHelper syncHelper) {
        this.appExecutors = appExecutors;
        this.syncHelper = syncHelper;

    }

    public BaseAypOutSchoolGroupVisitInteractor() {
        this(new AppExecutors(), AypLibrary.getInstance().getEcSyncHelper());
    }


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
                evaluateStructuralServices(details);
                evaluateSBCServices(details);
                fillNextAppointment(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }


    private void evaluateGroupAttendance(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutGroupAttendanceActionHelper actionHelper = createGroupAttendanceHelper();

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

    protected AypOutGroupAttendanceActionHelper createGroupAttendanceHelper() {
        return new AypOutGroupAttendanceActionHelper(context, memberObject, groupId);
    }

    private void evaluateStructuralServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolGroupStructuralActionHelper actionHelper = new AypOutSchoolGroupStructuralActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_structural_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_GROUP_STRUCTURAL_SERVICE)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_structural_services), action);
    }

    private void evaluateSBCServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolSBCServiceActionHelper actionHelper = new AypOutSchoolSBCServiceActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_sbc_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_SBC_SERVICE)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_sbc_services), action);
    }

    private void fillNextAppointment(Map<String, List<VisitDetail>> visitDetails) throws BaseAypVisitAction.ValidationException {
        AypOutSchoolGroupNextAppointActionHelper actionHelper = new AypOutSchoolGroupNextAppointActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_out_school_next_appointment))
                .withOptional(false)
                .withDetails(visitDetails)
                .withHelper(actionHelper)
                .withFormName(Constants.FORMS.AYP_OUT_SCHOOL_GROUP_NEXT_APPOINTMENT)
                .build();
        actionList.put(context.getString(R.string.ayp_out_school_next_appointment), action);
    }


    @Override
    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_SERVICES;
    }

    @Override
    protected String getTableName() {
        return Constants.TABLES.AYP_SERVICE;
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        MemberObject memberObject = new MemberObject();
        memberObject.setBaseEntityId(memberID);

        return memberObject;
    }
}
