package org.smartregister.chw.ayp.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";
    String ayp_VISIT_GROUP = "ayp_visit_group";


    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
        String EVENT_TYPE = "eventType";
    }

    interface EVENT_TYPE {
        String ayp_ENROLLMENT = "Ayp Enrollment";
        String AYP_IN_SCHOOL_ENROLLMENT = "AYP In-school Enrollment";
        String AYP_SERVICES = "Ayp Services";
        String AYP_FOLLOW_UP_VISIT = "Ayp Follow-up Visit";
        String AYP_IN_SCHOOL_FOLLOW_UP_VISIT = "Ayp In School Client Followup Visit";
        String AYP_OUT_SCHOOL_FOLLOW_UP_VISIT = "Ayp Out School Client Followup Visit";
        String VOID_EVENT = "Void Event";
        String CLOSE_ayp_SERVICE = "Close Ayp Service";
        String AYP_GROUP_DETAILS = "group_details";
        String AYP_GROUP_MEMBERSHIP = "ayp_group_membership";

    }

    interface FORMS {
        String AYP_REGISTRATION = "ayp_enrollment";
        String AYP_FOLLOW_UP_VISIT = "ayp_followup_visit";
        String AYP_IN_SCHOOL_CLIENT_STATUS = "ayp_in_school_client_status";
        String AYP_IN_SCHOOL_ENROLLMENT = "ayp_in_school_enrollment";
        String AYP_IN_SCHOOL_CSE = "ayp_in_school_cse";
        String AYP_IN_SCHOOL_FINANCIAL_LITERACY = "ayp_in_school_financial_literacy";
        String AYP_IN_SCHOOL_EDUCATION_SUBSIDIES = "ayp_in_school_education_subsidies";
        String AYP_IN_SCHOOL_SANITARY_KITS = "ayp_in_school_sanitary_kits";
        String AYP_IN_SCHOOL_GBV_SCREENING = "ayp_in_school_gbv_screening";
        String AYP_GROUP_ATTENDANCE = "ayp_in_school_group_members_attendance";

        String AYP_OUT_SCHOOL_SERVICE_STATUS = "ayp_out_school_service_status";
        String AYP_OUT_SCHOOL_STRUCTURAL_SERVICE = "ayp_out_school_structural_services";
        String AYP_OUT_SCHOOL_MEDICAL_SERVICE = "ayp_out_school_medical_services";
        String AYP_OUT_SCHOOL_HEALTH_AND_BEHAVIOUR_CHANGE_SERVICES= "ayp_out_school_health_and_behaviour_change_services";
        String AYP_OUT_SCHOOL_NEXT_APPOINTMENT = "ayp_out_school_next_appointment";

        String AYP_OUT_SCHOOL_GROUP_ATTENDANCE = "ayp_out_school_group_attendance";
        String AYP_OUT_SCHOOL_GROUP_STRUCTURAL_SERVICE = "ayp_out_school_group_structural_services";
        String AYP_OUT_SCHOOL_SBC_SERVICE = "ayp_out_school_sbc_services";
        String AYP_OUT_SCHOOL_GROUP_NEXT_APPOINTMENT = "ayp_out_school_group_next_appointment";
    }

    interface ayp_FOLLOWUP_FORMS {
        String MEDICAL_HISTORY = "ayp_service_medical_history";
        String PHYSICAL_EXAMINATION = "ayp_service_physical_examination";
        String HTS = "ayp_service_hts";
    }

    interface TABLES {
        String AYP_ENROLLMENT = "ec_ayp_enrollment";
        String AYP_IN_SCHOOL_ENROLLMENT = "ec_ayp_in_school_enrollment";
        String AYP_IN_SCHOOL_GROUP_DETAILS = "ec_ayp_in_school_group_details";
        String AYP_IN_SCHOOL_CLIENT_FOLLOW_UP_VISIT = "ec_ayp_in_school_client_followup_visits";
        String AYP_SERVICE = "ec_ayp_services";
        String AYP_OUT_SCHOOL_CLIENT_FOLLOW_UP_VISIT = "ec_ayp_out_school_client_followup_visits";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String AYP_FORM_NAME = "AYP_FORM_NAME";
        String MEMBER_PROFILE_OBJECT = "MemberObject";
        String EDIT_MODE = "editMode";
        String PROFILE_TYPE = "profile_type";
        String GROUP_ID = "GROUP_ID";
        String GROUP_NAME = "GROUP_NAME";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String ayp_ENROLLMENT = "ayp_enrollment";
    }

    interface ayp_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

    interface PROFILE_TYPES {
        String ayp_PROFILE = "ayp_profile";
    }

    interface VALUES {
        String NONE = "none";
        String CHORDAE = "chordae";
        String HIV = "hiv";
        String RBG = "random_blood_glucose_test";
        String FBG = "fast_blood_glucose_test";
        String HYPERTENSION = "hypertension";
        String SILICON_OR_LEXAN = "silicon_or_lexan";
        String NEGATIVE = "negative";
        String SATISFACTORY = "satisfactory";
        String NEEDS_FOLLOWUP = "needs_followup";
        String YES = "yes";
    }

    interface TABLE_COLUMN {
        String GENITAL_EXAMINATION = "genital_examination";
        String SYSTOLIC = "systolic";
        String DIASTOLIC = "diastolic";
        String ANY_COMPLAINTS = "any_complaints";
        String CLIENT_DIAGNONISED_WITH = "is_client_diagnosed_with_any";
        String COMPLICATION_TYPE = "type_complication";
        String HEMATOLOGICAL_DISEASE_SYMPTOMS = "any_hematological_disease_symptoms";
        String KNOWN_ALLEGIES = "known_allergies";
        String HIV_RESULTS = "hiv_result";
        String HIV_VIRAL_LOAD = "hiv_viral_load_text";
        String TYPE_OF_BLOOD_FOR_GLUCOSE_TEST = "type_of_blood_for_glucose_test";
        String BLOOD_FOR_GLUCOSE = "blood_for_glucose";
        String DISCHARGE_CONDITION = "discharge_condition";
        String IS_MALE_PROCEDURE_CIRCUMCISION_CONDUCTED = "is_male_procedure_circumcision_conducted";
    }

}
