package org.smartregister.chw.ayp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.domain.MemberObject;
import org.smartregister.chw.ayp.domain.Visit;
import org.smartregister.chw.ayp.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class BaseAypParentalMedicalHistoryActivity extends AppCompatActivity {

    private static MemberObject memberProfile;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    private LinearLayout visitContainer;
    private ProgressBar progressBar;
    private TextView titleView;

    public static void startMe(Activity activity, MemberObject memberObject) {
        memberProfile = memberObject;
        Intent intent = new Intent(activity, BaseAypParentalMedicalHistoryActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayp_parental_medical_history);

        visitContainer = findViewById(R.id.visit_container);
        progressBar = findViewById(R.id.progress_bar);
        titleView = findViewById(R.id.tvTitle);

        if (memberProfile == null) {
            finishWithMessage(R.string.ayp_no_visit_history);
            return;
        }

        String displayName = memberProfile.getFullName();
        if (StringUtils.isBlank(displayName)) {
            displayName = memberProfile.getFirstName();
        }
        titleView.setText(getString(R.string.ayp_parental_medical_history_title, StringUtils.defaultIfBlank(displayName, "")));
        loadVisitsAsync();
    }

    private void loadVisitsAsync() {
        showProgress(true);
        new Thread(() -> {
            List<Visit> visits = fetchVisits();
            mainHandler.post(() -> {
                showProgress(false);
                renderVisits(visits);
            });
        }).start();
    }

    private List<Visit> fetchVisits() {
        List<Visit> visits = new ArrayList<>();
        try {
            AypLibrary library = AypLibrary.getInstance();
            if (library != null && memberProfile != null) {
                List<Visit> parentalVisits = library.visitRepository()
                        .getVisits(memberProfile.getBaseEntityId(), Constants.EVENT_TYPE.AYP_PARENTAL_SERVICES);
                if (parentalVisits != null) {
                    visits.addAll(parentalVisits);
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return visits;
    }

    private void renderVisits(List<Visit> visits) {
        visitContainer.removeAllViews();
        if (visits == null || visits.isEmpty()) {
            addMessageView(R.string.ayp_no_visit_history);
            return;
        }

        for (Visit visit : visits) {
            View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, visitContainer, false);
            TextView title = view.findViewById(android.R.id.text1);
            TextView subtitle = view.findViewById(android.R.id.text2);

            String visitType = StringUtils.defaultIfBlank(visit.getVisitType(), getString(R.string.ayp_parental_services_visit_title));
            Date visitDate = visit.getDate();
            String formattedDate = visitDate != null ? dateFormat.format(visitDate) : getString(R.string.ayp_visit_date_unknown);
            title.setText(visitType);
            subtitle.setText(formattedDate);

            visitContainer.addView(view);
        }
    }

    private void addMessageView(@StringRes int messageRes) {
        TextView messageView = new TextView(this);
        messageView.setText(messageRes);
        messageView.setPadding(0, 16, 0, 0);
        visitContainer.addView(messageView);
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void finishWithMessage(@StringRes int messageRes) {
        Timber.w(getString(messageRes));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        memberProfile = null;
    }
}
