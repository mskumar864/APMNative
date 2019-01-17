package com.herokuapp.apmanative.apmnative;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.LocalPayment;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreeListener;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCallback;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.LocalPaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.models.PostalAddress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements PaymentMethodNonceCreatedListener,BraintreeCancelListener,BraintreeErrorListener {
    private String clientToken="";
    private Button idealButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //APM CODE FOR BT WITH CUSTOM UI
        final Activity activity=this;
        try {
            // mBraintreeFragment is ready to use!


            AsyncHttpClient client = new AsyncHttpClient();
            client.get("https://ppbraintree.herokuapp.com/client_token_sb", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    Log.i("Suresh******Token",responseString+"failure");
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    Log.i("Suresh******Token",response);
                    clientToken = response;

                }
            });

            idealButton=(Button)findViewById(R.id.iDEAL);

        idealButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {


                // String mAuthorization="eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI0YjFkMmJjYWYzOTgyYWIzYmFmMWFjY2Q2Y2VkYTU0MjgzMTFiMGRkZTZiMjlkMzNhM2VjYzUwMDVkOTI3MDYxfGNsaWVudF9pZD1jbGllbnRfaWQkc2FuZGJveCQ0ZHByYmZjNnBoNTk1Y2NqXHUwMDI2Y3JlYXRlZF9hdD0yMDE5LTAxLTAyVDA0OjUzOjQ1LjE5MDYwODU0MiswMDAwXHUwMDI2bWVyY2hhbnRfaWQ9Y21zanJ4cWpyanpiY3oyciIsImNvbmZpZ1VybCI6Imh0dHBzOi8vYXBpLnNhbmRib3guYnJhaW50cmVlZ2F0ZXdheS5jb206NDQzL21lcmNoYW50cy9jbXNqcnhxanJqemJjejJyL2NsaWVudF9hcGkvdjEvY29uZmlndXJhdGlvbiIsImdyYXBoUUwiOnsidXJsIjoiaHR0cHM6Ly9wYXltZW50cy5zYW5kYm94LmJyYWludHJlZS1hcGkuY29tL2dyYXBocWwiLCJkYXRlIjoiMjAxOC0wNS0wOCJ9LCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL2Ntc2pyeHFqcmp6YmN6MnIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vb3JpZ2luLWFuYWx5dGljcy1zYW5kLnNhbmRib3guYnJhaW50cmVlLWFwaS5jb20vY21zanJ4cWpyanpiY3oyciJ9LCJ0aHJlZURTZWN1cmVFbmFibGVkIjpmYWxzZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiR1BTIHRlc3RpbmcncyBUZXN0IFN0b3JlIiwiY2xpZW50SWQiOiJBUmFQeEZiV0NSTWRxbGYxS0N0VkFDdk4xdVFhNXViZ3YwaU9UWWxGZVhLeldsNXlMMlh3c1UxcWFEdF9VRHo3V21VUkl1LTFTRlFqZFNMNSIsInByaXZhY3lVcmwiOiJodHRwczovL2V4YW1wbGUuY29tIiwidXNlckFncmVlbWVudFVybCI6Imh0dHBzOi8vZXhhbXBsZS5jb20iLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjpmYWxzZSwiZW52aXJvbm1lbnQiOiJvZmZsaW5lIiwidW52ZXR0ZWRNZXJjaGFudCI6ZmFsc2UsImJyYWludHJlZUNsaWVudElkIjoibWFzdGVyY2xpZW50MyIsImJpbGxpbmdBZ3JlZW1lbnRzRW5hYmxlZCI6dHJ1ZSwibWVyY2hhbnRBY2NvdW50SWQiOiJVU0QiLCJjdXJyZW5jeUlzb0NvZGUiOiJVU0QifSwibWVyY2hhbnRJZCI6ImNtc2pyeHFqcmp6YmN6MnIiLCJ2ZW5tbyI6Im9mZiJ9";
                try {
                    final BraintreeFragment mBraintreeFragment = BraintreeFragment.newInstance(activity, clientToken);

                    Log.i("Suresh******Token", clientToken);

                    PostalAddress address = new PostalAddress()
                            .streetAddress("836486 of 22321 Park Lake")
                            .countryCodeAlpha2("NL")
                            .locality("Den Haag")
                            .postalCode("2585 GJ");
                    LocalPaymentRequest request = new LocalPaymentRequest()
                            .paymentType("ideal")
                            .amount("1.01")
                            .address(address)
                            .phone("639847934")
                            .email("vada864@gmail.com")
                            .givenName("Jon")
                            .surname("Doe")
                            .shippingAddressRequired(true)
                            .currencyCode("EUR");
                    LocalPayment.startPayment(mBraintreeFragment, request, new BraintreeResponseListener<LocalPaymentRequest>() {
                        @Override
                        public void onResponse(LocalPaymentRequest localPaymentRequest) {
                            // Do any preprocessing localPaymentRequest.getPaymentId()
                            Log.i("suresh", localPaymentRequest.getPaymentId());
                            LocalPayment.approvePayment(mBraintreeFragment, localPaymentRequest);

                        }


                    });


                } catch (Exception e) {
                }
            }
        });


        } catch (Exception e) {
            // There was an issue with your authorization string.
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Send this nonce to your server
            String nonce = paymentMethodNonce.getNonce();
            Log.i("suresh nonce",nonce);

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
     /*   params.setForceMultipartEntityContentType(true);
        params.setContentEncoding("application/x-www-form-urlencoded");*/
        params.put("nonce", nonce);
        params.put("amount", "1.01");
        Log.d("nonce  ",nonce);
        client.post("https://ppbraintree.herokuapp.com/checkout_sb", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String body = new String(responseBody);
                        Log.d("payment  ",body);
                        Toast.makeText(getApplicationContext(),"payment sucessfull",Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("checkout  ",error.getMessage());
                    }
                    // Your implementation here
                }
        );
        Log.d("Suresh******  exit  ","postNonceToServer");
        }

        @Override

        public void onCancel(int requestCode) {
            // Use this to handle a canceled activity, if the given requestCode is important.
            // You may want to use this callback to hide loading indicators, and prepare your UI for input
            Log.i("suresh request code","oncancel");
        }

@Override
    public void onError(Exception error) {
            if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
                Log.i("suresh onerror",error.getMessage());
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    Log.e("suresh",expirationMonthError.getMessage());
                }
            }
        }
    }

}