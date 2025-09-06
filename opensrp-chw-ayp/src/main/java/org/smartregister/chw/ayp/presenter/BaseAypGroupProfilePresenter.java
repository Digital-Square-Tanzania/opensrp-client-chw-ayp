package org.smartregister.chw.ayp.presenter;

import androidx.annotation.Nullable;

import org.smartregister.chw.ayp.contract.AypGroupProfileContract;

public class BaseAypGroupProfilePresenter implements AypGroupProfileContract.Presenter {

    private final AypGroupProfileContract.View view;
    private final AypGroupProfileContract.Interactor interactor;

    public BaseAypGroupProfilePresenter(AypGroupProfileContract.View view, AypGroupProfileContract.Interactor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void load(@Nullable String groupId, @Nullable String groupName) {
        if (view != null) view.showProgressBar(true);
        interactor.fetchGroupAndMembers(groupId, groupName, new AypGroupProfileContract.InteractorCallback() {
            @Override
            public void onGroupLoaded(org.smartregister.chw.ayp.domain.GroupObject groupObject) {
                if (view != null) view.setGroupViewWithData(groupObject);
            }

            @Override
            public void onMembersLoaded(java.util.List<org.smartregister.chw.ayp.domain.MemberObject> members) {
                if (view != null) {
                    view.renderMembers(members);
                    view.showProgressBar(false);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (view != null) view.showProgressBar(false);
            }
        });
    }

    @Override
    public void saveGroupDetails(String jsonString) {
        interactor.saveGroupDetails(jsonString, new AypGroupProfileContract.InteractorCallback() {
            @Override
            public void onGroupLoaded(org.smartregister.chw.ayp.domain.GroupObject groupObject) {
                // no-op
            }

            @Override
            public void onMembersLoaded(java.util.List<org.smartregister.chw.ayp.domain.MemberObject> members) {
                // no-op
            }

            @Override
            public void onError(Throwable throwable) {
                // no-op
            }
        });
    }

    @Nullable
    @Override
    public AypGroupProfileContract.View getView() {
        return view;
    }
}

