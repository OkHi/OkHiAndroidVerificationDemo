package io.okhi.okhiandroidverificationdemo;

import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.okhi.android_core.models.OkCollectSuccessResponse;
import io.okhi.android_core.models.OkHiException;
import io.okhi.android_core.models.OkHiLocation;
import io.okhi.android_core.models.OkHiUsageType;
import io.okhi.android_core.models.OkHiUser;
import io.okhi.android_okcollect.OkCollect;
import io.okhi.android_okcollect.utilities.OkHiTheme;
import io.okhi.android_okcollect.callbacks.OkCollectCallback;
import io.okhi.android_okcollect.utilities.OkHiConfig;
import io.okhi.android_okverify.OkVerify;
import io.okhi.android_okverify.interfaces.OkVerifyCallback;
import io.okhi.android_okverify.models.OkHiNotification;

public class MainActivity extends AppCompatActivity {

  private OkCollect okCollect;
  private OkVerify okVerify;
  private OkHiTheme okHiTheme = new OkHiTheme.Builder("#333").build();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setUpView();
    setUpOkHi();
  }

  private void setUpOkHi() {
    int importance = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? NotificationManager.IMPORTANCE_DEFAULT : 3;
    OkVerify.init(getApplicationContext(), new OkHiNotification(
      "Verifying your address",
      "We're currently verifying your address. This won't take long",
      "OkHi_Test",
      "OkHi Address Verification",
      "Alerts related to any address verification updates",
      importance,
      1, // notificationId
      2 // notification request code
    ));
    OkHiConfig config = new OkHiConfig.Builder().withUsageTypes(new OkHiUsageType[]{OkHiUsageType.physicalVerification}).build();
    okVerify = new OkVerify.Builder(this).build();
    okCollect = new OkCollect.Builder(this).withConfig(config).withTheme(okHiTheme).build();
  }

  private void onVerifyAddressClick() {
    okCollect.launch(createOkHiUser(), new OkCollectCallback<OkCollectSuccessResponse>() {
      @Override
      public void onSuccess(OkCollectSuccessResponse response) {
        startAddressVerification(response);
      }

      @Override
      public void onClose() {
        showMessage("User closed.");
      }

      @Override
      public void onError(OkHiException e) {
        showMessage(e.getCode() + ":" + e.getMessage());
      }
    });
  }

  private void startAddressVerification(OkCollectSuccessResponse response) {
    okVerify.start(response, new OkVerifyCallback<String>() {
      @Override
      public void onSuccess(String locationId) {
        showMessage("Successfully started verification for: " + locationId);
      }

      @Override
      public void onError(OkHiException e) {
        showMessage(e.getCode() + ":" + e.getMessage());
      }
    });
  }

  private OkHiUser createOkHiUser() {
    return new OkHiUser.Builder("+254...")
      .withFirstName("Jane")
      .withLastName("Doe")
      .withEmail("jane@okhi.co")
      .withAppUserId("abcd1234")
      .build();
  }

  private void showMessage(String message) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
      }
    });
  }

  private void setUpView() {
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
    Button btnVerifyAddress = findViewById(R.id.btnVerifyAddress);
    btnVerifyAddress.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onVerifyAddressClick();
      }
    });
  }
}