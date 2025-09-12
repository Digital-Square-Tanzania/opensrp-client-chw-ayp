package org.smartregister.chw.ayp.interactor;

import androidx.annotation.VisibleForTesting;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolClientStatusServicesActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolCseServicesActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolEducationSubsidiesActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolFinancialLiteracyActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolSanitaryKitsActionHelper;
import org.smartregister.chw.ayp.actionhelper.AypInSchoolGbvScreeningActionHelper; // Added import
import org.smartregister.chw.ayp.contract.BaseAypVisitContract;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.VisitDetail;
import org.smartregister.chw.ayp.model.BaseAypVisitAction;
import org.smartregister.chw.ayp.util.AppExecutors;
import org.smartregister.chw.ayp.util.Constants;
import org.smartregister.sync.helper.ECSyncHelper;

import org.json.JSONObject;
import org.smartregister.chw.ayp.util.JsonFormUtils;
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
                // Gate all other actions behind Client Status = "continuing"
                evaluateCse(details);
                evaluateFinancialLiteracyEducation(details);
                evaluateEducationSubsidies(details);
                evaluateSanitaryKits(details);
                evaluateGbvScreening(details); // Added this line
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

    private void evaluateCse(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolCseServicesActionHelper actionHelper = new AypInSchoolCseServicesActionHelper(context, memberObject);

        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_inschool_cse_service))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withValidator(getClientStatusGatingValidator())
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_CSE)
                .build();
        actionList.put(context.getString(R.string.ayp_inschool_cse_service), action);
    }

    private void evaluateFinancialLiteracyEducation(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolFinancialLiteracyActionHelper actionHelper = new AypInSchoolFinancialLiteracyActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_financial_literacy_education_title))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withValidator(getClientStatusGatingValidator())
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_FINANCIAL_LITERACY)
                .build();
        actionList.put(context.getString(R.string.ayp_financial_literacy_education_title), action);
    }

    private void evaluateEducationSubsidies(Map<String, List<VisitDetail>> details) throws BaseAypVisitAction.ValidationException {
        AypInSchoolEducationSubsidiesActionHelper actionHelper = new AypInSchoolEducationSubsidiesActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_education_subsidies_title))
                .withOptional(false)
                .withDetails(details)
                .withHelper(actionHelper)
                .withValidator(getClientStatusGatingValidator())
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_EDUCATION_SUBSIDIES)
                .build();
        actionList.put(context.getString(R.string.ayp_education_subsidies_title), action);
    }

    private void evaluateSanitaryKits(Map<String, List<VisitDetail>> visitDetails) throws BaseAypVisitAction.ValidationException {
        AypInSchoolSanitaryKitsActionHelper actionHelper = new AypInSchoolSanitaryKitsActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_sanitary_kits_title))
                .withOptional(false)
                .withDetails(visitDetails)
                .withHelper(actionHelper)
                .withValidator(getClientStatusGatingValidator())
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_SANITARY_KITS)
                .build();
        actionList.put(context.getString(R.string.ayp_sanitary_kits_title), action);
    }

    private void evaluateGbvScreening(Map<String, List<VisitDetail>> visitDetails) throws BaseAypVisitAction.ValidationException {
        AypInSchoolGbvScreeningActionHelper actionHelper = new AypInSchoolGbvScreeningActionHelper(context, memberObject);
        BaseAypVisitAction action = getBuilder(context.getString(R.string.ayp_gbv_screening_title))
                .withOptional(false)
                .withDetails(visitDetails)
                .withHelper(actionHelper)
                .withValidator(getClientStatusGatingValidator())
                .withFormName(Constants.FORMS.AYP_IN_SCHOOL_GBV_SCREENING)
                .build();
        actionList.put(context.getString(R.string.ayp_gbv_screening_title), action);
    }


    protected String getEncounterType() {
        return Constants.EVENT_TYPE.AYP_IN_SCHOOL_FOLLOW_UP_VISIT;
    }

    protected String getTableName() {
        return Constants.TABLES.AYP_IN_SCHOOL_CLIENT_FOLLOW_UP_VISIT;
    }

    @Override
    public MemberObject getMemberClient(String memberID, String profileType) {
        return AypDao.getInSchoolMember(memberID);
    }

    private BaseAypVisitAction.Validator getClientStatusGatingValidator() {
        return new BaseAypVisitAction.Validator() {
            @Override
            public boolean isValid(String key) {
                // Client status action is always visible
                String clientStatusTitle = context.getString(R.string.ayp_inschool_services_client_status);
                if (clientStatusTitle.equals(key)) return true;
                // Other actions only visible when client status == continuing
                return isContinuingSelected();
            }

            @Override
            public boolean isEnabled(String key) {
                // Mirror visibility for enabled state
                return isValid(key);
            }

            @Override
            public void onChanged(String key) {
                // No-op; UI layer should re-query isValid/isEnabled when actions change
            }
        };
    }

    private boolean isContinuingSelected() {
        try {
            String clientStatusTitle = context.getString(R.string.ayp_inschool_services_client_status);
            BaseAypVisitAction statusAction = actionList.get(clientStatusTitle);
            if (statusAction != null) {
                String payload = statusAction.getJsonPayload();
                if (StringUtils.isNotBlank(payload)) {
                    JSONObject json = new JSONObject(payload);
                    String value = JsonFormUtils.getValue(json, "client_status");
                    return "continuing".equalsIgnoreCase(value != null ? value.trim() : null);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return false;
    }
}
