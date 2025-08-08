package org.smartregister.chw.ayp.custom_views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.fragment.BaseAypCallDialogFragment;
import org.smartregister.chw.ayp.R;

public class BaseAypFloatingMenu extends LinearLayout implements View.OnClickListener {
    private MemberObject MEMBER_OBJECT;

    public BaseAypFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_ayp_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.ayp_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ayp_fab) {
            Activity activity = (Activity) getContext();
            BaseAypCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }  else if (view.getId() == R.id.refer_to_facility_layout) {
            Activity activity = (Activity) getContext();
            BaseAypCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}