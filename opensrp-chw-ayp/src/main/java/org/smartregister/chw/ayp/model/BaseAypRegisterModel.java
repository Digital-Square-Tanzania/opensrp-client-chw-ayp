package org.smartregister.chw.ayp.model;

import org.json.JSONObject;
import org.smartregister.chw.ayp.contract.AypRegisterContract;
import org.smartregister.chw.ayp.util.AypJsonFormUtils;

public class BaseAypRegisterModel implements AypRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = AypJsonFormUtils.getFormAsJson(formName);
        AypJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
