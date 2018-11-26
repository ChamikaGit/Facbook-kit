package android.arimaclanka.com.facebookacccountkit;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class SuccessActivty extends Activity {

    private TextView name1,name2,name3;
    private Button buttonLoutOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_activty);

        buttonLoutOut = findViewById(R.id.btnlogout);
        name1 = findViewById(R.id.name1);
        name2= findViewById(R.id.name2);
        name3 =findViewById(R.id.name3);

        btnListners();
    }

    private void btnListners() {
        buttonLoutOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountKit.logOut();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {

                name1.setText(String.format("User Id %s",account.getId()));
                name2.setText(String.format("Phone %s",account.getPhoneNumber() ==null ? "":account.getPhoneNumber().toString()));
                name3.setText(account.getEmail());
            }

            @Override
            public void onError(AccountKitError accountKitError) {

            }
        });
    }
}
