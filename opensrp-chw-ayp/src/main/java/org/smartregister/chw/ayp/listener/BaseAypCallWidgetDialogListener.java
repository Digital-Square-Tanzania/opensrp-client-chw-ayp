package org.smartregister.chw.ayp.listener;


import android.view.View;

import org.smartregister.chw.ayp.fragment.BaseAypCallDialogFragment;
import org.smartregister.chw.ayp.R;

public class BaseAypCallWidgetDialogListener implements View.OnClickListener {

    private BaseAypCallDialogFragment callDialogFragment;

    public BaseAypCallWidgetDialogListener(BaseAypCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ayp_call_close) {
            callDialogFragment.dismiss();
        }
    }
}
