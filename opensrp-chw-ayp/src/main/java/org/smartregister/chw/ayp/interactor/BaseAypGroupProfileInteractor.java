package org.smartregister.chw.ayp.interactor;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.contract.AypGroupProfileContract;
import org.smartregister.chw.ayp.dao.AypDao;
import org.smartregister.chw.ayp.domain.GroupObject;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BaseAypGroupProfileInteractor implements AypGroupProfileContract.Interactor {

    @Override
    public void fetchGroupAndMembers(String groupId, String groupName, AypGroupProfileContract.InteractorCallback callback) {
        try {
            if (StringUtils.isBlank(groupId)) {
                callback.onError(new IllegalArgumentException("groupId is required"));
                return;
            }

            List<MemberObject> members = AypDao.getInSchoolGroupMembers(groupId);

            String location = !members.isEmpty() ? members.get(0).getAddress() : null;
            GroupObject group = new GroupObject(groupId, StringUtils.defaultIfBlank(groupName, "Group"), location, members.size());

            callback.onGroupLoaded(group);
            callback.onMembersLoaded(members);

        } catch (Exception e) {
            callback.onError(e);
        }
    }

    @Override
    public void saveGroupDetails(String jsonString, AypGroupProfileContract.InteractorCallback callback) {
        // Placeholder for app-specific persistence if needed
        // Simply signal success for now by re-calling fetch from presenter after save
    }
}

