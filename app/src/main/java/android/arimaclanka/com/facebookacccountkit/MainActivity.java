package android.arimaclanka.com.facebookacccountkit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {


    private Button btn_phone;
    private Button btn_email;
    private final static int REQUEST_CODE = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_phone = findViewById(R.id.btn_phone);
        btn_email = findViewById(R.id.btn_email);
        printkeyHash();

        btnClickListner();
    }

    private void btnClickListner() {

        btn_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEmailPage(LoginType.PHONE);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startEmailPage(LoginType.EMAIL);
            }
        });
    }

    private void startEmailPage(LoginType loginType) {

        if (loginType == LoginType.EMAIL) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.EMAIL,
                            AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);
        } else if (loginType == LoginType.PHONE) {


            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN);

            intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
            startActivityForResult(intent, REQUEST_CODE);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {

            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (result.getError() != null) {

                Toast.makeText(this, "" + result.getError().getErrorType().getMessage(), Toast.LENGTH_LONG).show();
                return;
            } else if (result.wasCancelled()) {

                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (result.getAccessToken() != null) {
                    Toast.makeText(this, "Success ! %s" + result.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "Success ! %s" + result.getAuthorizationCode().substring(0, 10), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, SuccessActivty.class));
            }
        }
    }

    private void printkeyHash() {

        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("android.arimaclanka.com.facebookacccountkit",
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
