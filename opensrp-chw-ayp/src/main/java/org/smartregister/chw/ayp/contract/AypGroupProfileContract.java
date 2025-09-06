package org.smartregister.chw.ayp.contract;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.ayp.domain.GroupObject;
import org.smartregister.chw.ayp.domain.MemberObject;

import java.util.List;

public interface AypGroupProfileContract {

    interface View extends InteractorCallback {
        void showProgressBar(boolean status);
        void setGroupViewWithData(GroupObject groupObject);
        void renderMembers(List<MemberObject> members);
        void openGroupDetailsForm();
        void onAddMember();
        void openMemberProfile(MemberObject member);
    }

    interface Presenter {
        void load(@Nullable String groupId, @Nullable String groupName);
        void saveGroupDetails(String jsonString);
        @Nullable
        View getView();
    }

    interface Interactor {
        void fetchGroupAndMembers(@Nullable String groupId, @Nullable String groupName, InteractorCallback callback);
        void saveGroupDetails(String jsonString, InteractorCallback callback);
    }

    interface InteractorCallback {
        void onGroupLoaded(GroupObject groupObject);
        void onMembersLoaded(List<MemberObject> members);
        void onError(Throwable throwable);
    }
}

