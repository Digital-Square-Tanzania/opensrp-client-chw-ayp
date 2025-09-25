package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypParentingClientStatusActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingDeliveryModalityActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingHivStisReproductiveHealthActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingNextAppointmentReferralActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingPromoteHivServicesYouthActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingSexualReproductiveHealthYouthActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingSkillsEducationYouthActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingTrainingParentsActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingUnderstandingYouthActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingVisitCommentActionHelper;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypParentalVisitInteractor extends BaseAypVisitInteractor {

    @VisibleForTesting
    AypParentalVisitInteractor(AppExecutors appExecutors, ECSyncHelper ecSyncHelper) {
        super(appExecutors, ecSyncHelper);
        this.visitType = Constants.EVENT_TYPE.AYP_PARENTAL_SERVICES;
    }

    public AypParentalVisitInteractor() {
        super(Constants.EVENT_TYPE.AYP_PARENTAL_SERVICES);
    }

    @Override
    protected void populateActionList(BaseAypVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            actionList.clear();
            try {
                evaluateClientStatus(details);
                evaluateDeliveryModality(details);
                evaluateTrainingForParents(details);
                evaluateHivStisReproductiveHealth(details);
                evaluateUnderstandingYouth(details);
                evaluateSkillsEducation(details);
                evaluateSexualReproductiveHealth(details);
                evaluatePromoteHivServices(details);
                evaluateVisitComment(details);
                evaluateNextAppointment(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void evaluateClientStatus(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_client_status))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingClientStatusActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_CLIENT_STATUS)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_client_status), action);
    }

    private void evaluateDeliveryModality(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_delivery_modality))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingDeliveryModalityActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_DELIVERY_MODALITY)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_delivery_modality), action);
    }

    private void evaluateTrainingForParents(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_training_parents))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingTrainingParentsActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_TRAINING_PARENTS)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_training_parents), action);
    }

    private void evaluateHivStisReproductiveHealth(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_hiv_stis_reproductive_health))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingHivStisReproductiveHealthActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_HIV_STIS_REPRODUCTIVE_HEALTH)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_hiv_stis_reproductive_health), action);
    }

    private void evaluateUnderstandingYouth(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_understanding_youth))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingUnderstandingYouthActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_UNDERSTANDING_YOUTH)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_understanding_youth), action);
    }

    private void evaluateSkillsEducation(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_skills_education_youth))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingSkillsEducationYouthActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_SKILLS_EDUCATION_YOUTH)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_skills_education_youth), action);
    }

    private void evaluateSexualReproductiveHealth(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_sexual_reproductive_health_youth))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingSexualReproductiveHealthYouthActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_SEXUAL_REPRODUCTIVE_HEALTH_YOUTH)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_sexual_reproductive_health_youth), action);
    }

    private void evaluatePromoteHivServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_promote_hiv_services_youth))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingPromoteHivServicesYouthActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_PROMOTE_HIV_SERVICES_YOUTH)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_promote_hiv_services_youth), action);
    }

    private void evaluateVisitComment(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_visit_comment))
                .withOptional(true)
                .withDetails(details)
                .withHelper(new AypParentingVisitCommentActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_VISIT_COMMENT)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_visit_comment), action);
    }

    private void evaluateNextAppointment(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_parenting_next_appointment_referral))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypParentingNextAppointmentReferralActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_PARENTING_NEXT_APPOINTMENT_REFERRAL)
                .build();
        actionList.put(context.getString(R.string.ayp_parenting_next_appointment_referral), action);
    }

    @Override
    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_PARENTAL_SERVICES;
    }

    @Override
    protected String getTableName() {
        return Constants.TABLES.AYP_SERVICE;
    }
}
