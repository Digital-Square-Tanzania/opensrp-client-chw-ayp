package org.smartregister.chw.ayp.contract;

import android.content.Context;

public interface BaseAypCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
