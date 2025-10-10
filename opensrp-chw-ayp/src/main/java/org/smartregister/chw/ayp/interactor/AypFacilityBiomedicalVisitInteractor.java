package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalCondomDistributionActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalFamilyPlanningActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalGbvActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalHepatitisActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalHivstActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalHtsActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalPepServiceActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalStiScreeningActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalStiTreatmentClientActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalStiTreatmentPartnerActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityBiomedicalTbScreeningActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityNextAppointmentActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypFacilityReferralToOtherServicesActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypParentingServicesNextAppointmentAndReferralActionHelper;
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.chw.ayp.util.JsonFormUtils;
import org.smartregister.sync.helper.ECSyncHelper;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class AypFacilityBiomedicalVisitInteractor extends BaseAypVisitInteractor {

    @VisibleForTesting
    AypFacilityBiomedicalVisitInteractor(AppExecutors appExecutors, ECSyncHelper ecSyncHelper) {
        super(appExecutors, ecSyncHelper);
        this.visitType = Constants.EVENT_TYPE.AYP_SERVICES;
    }

    public AypFacilityBiomedicalVisitInteractor() {
        super(Constants.EVENT_TYPE.AYP_SERVICES);
    }

    @Override
    protected void populateActionList(BaseAypVisitContract.InteractorCallBack callBack) {
        final Runnable runnable = () -> {
            actionList.clear();
            try {
                evaluateHts(details);
                evaluateHivst(details);
                evaluateCondomDistribution(details);
                evaluateGbvScreening(details);
                evaluateStiScreening(details);
                evaluateClientStiTreatment(details);
                evaluatePartnerStiTreatment(details);
                evaluateTbScreening(details);
                evaluatePepService(details);
                evaluateHepatitis(details);
                evaluateFamilyPlanning(details);
                evaluateReferralToOtherServices(details);
                evaluateNextAppointment(details);
            } catch (BaseAypVisitAction.ValidationException e) {
                Timber.e(e);
            }

            appExecutors.mainThread().execute(() -> callBack.preloadActions(actionList));
        };

        appExecutors.diskIO().execute(runnable);
    }

    private void evaluateHts(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_hts))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalHtsActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_HTS)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_hts), action);
    }

    private void evaluateHivst(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_hivst))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalHivstActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_HIVST)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_hivst), action);
    }

    private void evaluateCondomDistribution(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_condom_distribution))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalCondomDistributionActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_CONDOM_DISTRIBUTION)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_condom_distribution), action);
    }

    private void evaluateGbvScreening(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_gbv))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalGbvActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_GBV)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_gbv), action);
    }

    private void evaluateStiScreening(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_sti_screening))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalStiScreeningActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_STI_SCREENING)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_sti_screening), action);
    }

    private void evaluateClientStiTreatment(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_sti_treatment_client))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalStiTreatmentClientActionHelper(context, memberObject))
                .withValidator(getStiPositiveValidator())
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_STI_TREATMENT_CLIENT)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_sti_treatment_client), action);
    }

    private void evaluatePartnerStiTreatment(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_sti_treatment_partner))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalStiTreatmentPartnerActionHelper(context, memberObject))
                .withValidator(getStiPositiveValidator())
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_STI_TREATMENT_PARTNER)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_sti_treatment_partner), action);
    }

    private void evaluateTbScreening(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_tb_screening))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalTbScreeningActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_TB_SCREENING)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_tb_screening), action);
    }

    private void evaluatePepService(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_pep_service))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalPepServiceActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_PEP_SERVICE)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_pep_service), action);
    }

    private void evaluateHepatitis(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_hepatitis))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalHepatitisActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_HEPATITIS)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_hepatitis), action);
    }

    private void evaluateFamilyPlanning(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_biomedical_family_planning))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityBiomedicalFamilyPlanningActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_BIOMEDICAL_FAMILY_PLANNING)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_biomedical_family_planning), action);
    }

    private void evaluateReferralToOtherServices(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_referral_to_other_services))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityReferralToOtherServicesActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_REFERRAL_TO_OTHER_SERVICES)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_referral_to_other_services), action);
    }

    private void evaluateNextAppointment(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_facility_next_appointment))
                .withOptional(false)
                .withDetails(details)
                .withHelper(new AypFacilityNextAppointmentActionHelper(context, memberObject))
                .withFormName(Constants.FORMS.AYP_FACILITY_NEXT_APPOINTMENT)
                .build();
        actionList.put(context.getString(R.string.ayp_facility_next_appointment), action);
    }

    private BaseAypVisitAction.Validator getStiPositiveValidator() {
        return new BaseAypVisitAction.Validator() {
            @Override
            public boolean isValid(String key) {
                return isStiPositive();
            }

            @Override
            public boolean isEnabled(String key) {
                return isStiPositive();
            }

            @Override
            public void onChanged(String key) {
                // No-op; UI refresh checks validity each time.
            }
        };
    }

    private boolean isStiPositive() {
        try {
            String screeningTitle = context.getString(R.string.ayp_facility_biomedical_sti_screening);
            BaseAypVisitAction screeningAction = actionList.get(screeningTitle);
            if (screeningAction == null) {
                return false;
            }

            String payload = screeningAction.getJsonPayload();
            if (StringUtils.isBlank(payload)) {
                return false;
            }

            JSONObject jsonObject = new JSONObject(payload);
            String rawValue = firstNonBlank(
                    JsonFormUtils.getValue(jsonObject, "sti_screening_result"),
                    JsonFormUtils.getValue(jsonObject, "sti_screening_results"),
                    JsonFormUtils.getValue(jsonObject, "ayp_sti_screening_result")
            );

            if (StringUtils.isBlank(rawValue)) {
                return false;
            }

            String sanitized = rawValue.replace("[", "")
                    .replace("]", "")
                    .replace("\"", "");

            String[] tokens = sanitized.split(",");
            for (String token : tokens) {
                if (StringUtils.containsIgnoreCase(token.trim(), "positive")) {
                    return true;
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return false;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    @Override
    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_FACILITY_SERVICES;
    }

    @Override
    protected String getTableName() {
        return Constants.TABLES.AYP_FACILITY_SERVICES;
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return AypDao.getFacilityMember(memberID);
    }
}
