package org.smartregister.chw.ayp.listener;

import android.app.Activity;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.chw.ayp.R;

public class AypBottomNavigationListener extends BottomNavigationListener {
    private Activity context;

    public AypBottomNavigationListener(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        super.onNavigationItemSelected(item);

        BaseRegisterActivity baseRegisterActivity = (BaseRegisterActivity) context;

        // Switch fragments by position based on selected item
        int itemId = item.getItemId();
        if (itemId == R.id.action_home) {
            baseRegisterActivity.switchToFragment(0);
        } else if (itemId == R.id.action_groups) {
            baseRegisterActivity.switchToFragment(1);
        } else {
            // Fallback to base fragment for any unrecognized item
            baseRegisterActivity.switchToBaseFragment();
        }

        return true;
    }
}
