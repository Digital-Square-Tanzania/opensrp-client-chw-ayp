package org.smartregister.chw.ayp.util;

import static org.smartregister.util.Utils.getAllSharedPreferences;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
import org.smartregister.chw.ayp.AypLibrary;
import org.smartregister.chw.ayp.R;
import org.smartregister.chw.ayp.contract.BaseAypCallDialogContract;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.BaseRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;
import org.smartregister.util.PermissionUtils;

import java.util.Date;

import timber.log.Timber;

public class AypUtil {

    public static void processEvent(AllSharedPreferences allSharedPreferences, Event baseEvent) throws Exception {
        if (baseEvent != null) {
            AypJsonFormUtils.tagEvent(allSharedPreferences, baseEvent);
            JSONObject eventJson = new JSONObject(AypJsonFormUtils.gson.toJson(baseEvent));

            getSyncHelper().addEvent(baseEvent.getBaseEntityId(), eventJson, BaseRepository.TYPE_Unprocessed);
            startClientProcessing();
        }
    }

    public static void startClientProcessing() {
        try {
            long lastSyncTimeStamp = getAllSharedPreferences().fetchLastUpdatedAtDate(0);
            Date lastSyncDate = new Date(lastSyncTimeStamp);
            getClientProcessorForJava().processClient(getSyncHelper().getEvents(lastSyncDate, BaseRepository.TYPE_Unprocessed));
            getAllSharedPreferences().saveLastUpdatedAtDate(lastSyncDate.getTime());
        } catch (Exception e) {
            Timber.d(e);
        }
    }

    public static ECSyncHelper getSyncHelper() {
        return AypLibrary.getInstance().getEcSyncHelper();
    }

    public static ClientProcessorForJava getClientProcessorForJava() {
        return AypLibrary.getInstance().getClientProcessorForJava();
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(text);
        }
    }

    public static boolean launchDialer(final Activity activity, final BaseAypCallDialogContract.View callView, final String phoneNumber) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            // set a pending call execution request
            if (callView != null) {
                callView.setPendingCallRequest(() -> AypUtil.launchDialer(activity, callView, phoneNumber));
            }

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionUtils.PHONE_STATE_PERMISSION_REQUEST_CODE);

            return false;
        } else {
            if (((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number()
                    == null) {

                Timber.i("No dial application so we launch copy to clipboard...");

                ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(activity.getText(R.string.copied_phone_number), phoneNumber);
                clipboard.setPrimaryClip(clip);

                CopyToClipboardDialog copyToClipboardDialog = new CopyToClipboardDialog(activity, R.style.copy_clipboard_dialog);
                copyToClipboardDialog.setContent(phoneNumber);
                copyToClipboardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                copyToClipboardDialog.show();
                Toast.makeText(activity, activity.getText(R.string.copied_phone_number), Toast.LENGTH_SHORT).show();

            } else {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                activity.startActivity(intent);
            }
            return true;
        }
    }

    public static void saveFormEvent(final String jsonString) throws Exception {
        AllSharedPreferences allSharedPreferences = AypLibrary.getInstance().context().allSharedPreferences();
        Event baseEvent = AypJsonFormUtils.processJsonForm(allSharedPreferences, jsonString);
        AypUtil.processEvent(allSharedPreferences, baseEvent);
    }

    public static int getMemberProfileImageResourceIdentifier(String entityType) {
        return R.mipmap.ic_member;
    }

    public static String getGenderTranslated(Context context, String gender) {
        if (gender.equalsIgnoreCase(Gender.MALE.toString())) {
            return context.getResources().getString(R.string.male);
        } else if (gender.equalsIgnoreCase(Gender.FEMALE.toString())) {
            return context.getResources().getString(R.string.female);
        }
        return "";
    }

    protected static Event getCloseaypEvent(String jsonString,
                                             String baseEntityId) {

        Event closeaypEvent = new Gson().
                fromJson(jsonString, Event.class);

        closeaypEvent.setEntityType(Constants.TABLES.AYP_ENROLLMENT);
        closeaypEvent.setEventType(Constants.EVENT_TYPE.CLOSE_ayp_SERVICE);
        closeaypEvent.setBaseEntityId(baseEntityId);
        closeaypEvent.setFormSubmissionId(JsonFormUtils.
                generateRandomUUIDString());
        closeaypEvent.setEventDate(new Date());
        return closeaypEvent;
    }

    public static void closeaypService(String baseEntityId) {
        AllSharedPreferences allSharedPreferences = AypLibrary.
                getInstance().
                context().
                allSharedPreferences();
        Event closeaypEvent = getCloseaypEvent(new JSONObject().
                toString(),
                baseEntityId);

        try {
            NCUtils.addEvent(allSharedPreferences, closeaypEvent);
            NCUtils.startClientProcessing();
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}

