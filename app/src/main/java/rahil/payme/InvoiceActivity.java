package rahil.payme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import rahil.payme.data.PayMeModel;

public class InvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    public static String EXTRA_NAME = "EXTRA_NAME";

    private static PayPalConfiguration config = new PayPalConfiguration();

    private ImageButton buttonPay;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = getIntent().getExtras().getString(EXTRA_NAME);
            getSupportActionBar().setTitle(getString(R.string.welcome) + name);
        }
        config.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
        config.clientId("AXXtS9n6ZQHFs_w2dNSk98tx3WELLtWAqvANTpTQ5LyogYAkYmAdl1owLtEZ-fRNPKniF4suV654Kili");
        config.defaultUserEmail("rahil123-buyer@gmail.com");

        buttonPay = (ImageButton) findViewById(R.id.btnPay);
        buttonPay.setOnClickListener(this);
        buttonLogout = (Button) findViewById(R.id.btnLogout);
        buttonLogout.setOnClickListener(this);
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPay:
                PayPalPayment payment = new PayPalPayment(new BigDecimal("117.80"), "USD", "K Software - Invoice #112",
                        PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(this, PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnLogout:
                PayMeModel payMeModel = PayMeModel.getInstance();
                payMeModel.setLoggedIn(false);
                payMeModel.setUserFirstName("");
                SharedPreferences sharedPref = getSharedPreferences(PayMeModel.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(PayMeModel.PREF_LOGGED_IN, payMeModel.isLoggedIn());
                editor.putString(PayMeModel.PREF_FIRST_NAME, payMeModel.getUserFirstName());
                editor.commit();

                Toast.makeText(this, "Logout successful", Toast.LENGTH_LONG).show();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
