package com.cliknfit.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.cliknfit.R;
import com.cliknfit.adapter.AdapterTimeList;
import com.cliknfit.database.DataBaseHelper;
import com.cliknfit.pojo.CardDBModel;
import com.cliknfit.util.Alerts;
import com.cliknfit.util.AppPreference;
import com.cliknfit.util.Constants;
import com.cliknfit.util.Validations;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;

import io.card.payment.CardIOActivity;
import io.card.payment.CardType;
import io.card.payment.CreditCard;

import static android.R.id.list;
import static com.cliknfit.R.id.amdivider;
import static com.cliknfit.R.id.pm;
import static com.cliknfit.R.id.thirty;
import static com.cliknfit.R.id.view;
import static com.cliknfit.R.id.zero;
import static com.paypal.android.sdk.onetouch.core.enums.ResultType.Success;
import static com.paypal.android.sdk.onetouch.core.metadata.ah.i;

public class PaymentMethods extends AppCompatActivity {

    private ImageView scan;
    private EditText card_number;
    private ImageView card_type;
    private EditText exp_date;
    private EditText cvv;
    private int MY_SCAN_REQUEST_CODE = 3698;
    private static String type = "";
    private String TAG = "tag";
    private TextView addcard;
    private DataBaseHelper database;
    private String selectedYear = "", selectedMonth = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        if (!getIntent().hasExtra("back")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        initViews();

    }


    private void initViews() {

        scan = (ImageView) findViewById(R.id.scan);
        card_type = (ImageView) findViewById(R.id.card_type);
        card_number = (EditText) findViewById(R.id.card_number);
        exp_date = (EditText) findViewById(R.id.exp_date);
        cvv = (EditText) findViewById(R.id.cvv);
        addcard = (TextView) findViewById(R.id.addcard);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent scanIntent = new Intent(PaymentMethods.this, CardIOActivity.class);

                // customize these values to suit your needs.
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

                // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
                // startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });
        textChangeListener();
        click();
    }

    private void click() {
        exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectdateDialog(exp_date);
            }
        });


        addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    adddatabase();
                }
            }
        });
    }

    private void adddatabase() {
        try {
            database = new DataBaseHelper(this);

            if (database.getAllCards().size() > 0) {
                for (CardDBModel m : database.getAllCards()) {
                    database.deleteContact(m);
                }
            }

            CardDBModel model = new CardDBModel();

            model.setCardnumber(card_number.getText().toString());
            model.setCardtype(type);

            String last = exp_date.getText().toString().substring(Math.max(exp_date.getText().toString().length() - 2, 0));
            String first = exp_date.getText().toString().substring(0, exp_date.getText().toString().length() - 2);
            model.setExpdate(first + "20" + last);
            model.setCvv(cvv.getText().toString());

            database.addCard(model);
            AppPreference.setBooleanPreference(this, Constants.PROFILEUPDATED, false);
            Alerts.okAlert(this, "Card added successfully", "Success", true);

        } catch (Exception e) {

        }
    }


    private void selectdateDialog(final EditText txt) {
        AlertDialog.Builder alert = new AlertDialog.Builder(PaymentMethods.this);
        View dialog = LayoutInflater.from(PaymentMethods.this).inflate(R.layout.alertexpdate, null, false);

        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mTillYear = mYear + 10;
        final int mMonth = c.get(Calendar.MONTH) + 1;
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final ArrayList<String> itemsMonths = new ArrayList<String>();

        for (int i = mMonth; i <= 12; i++) {
            if (i < 10)
                itemsMonths.add("0" + i);
            else
                itemsMonths.add("" + i);
        }

        final ArrayList<String> itemsYear = new ArrayList<String>();
        for (int i = mYear; i <= mTillYear; i++)
            itemsYear.add("" + i);

        final TextView value = (TextView) dialog.findViewById(R.id.value);
        final ListView monthlist = (ListView) dialog.findViewById(R.id.monthlist);
        final ListView yearlist = (ListView) dialog.findViewById(R.id.yearlist);

        final AdapterTimeList adapterMonths = new AdapterTimeList(this, itemsMonths, 0);
        final AdapterTimeList adapterYear = new AdapterTimeList(this, itemsYear, 0);

        monthlist.setAdapter(adapterMonths);
        yearlist.setAdapter(adapterYear);

        TextView tv_cancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        TextView tv_done = (TextView) dialog.findViewById(R.id.tv_done);

        selectedYear = itemsYear.get(0);
        selectedMonth = itemsMonths.get(0);
        alert.setView(dialog);
        alert.setCancelable(true);
        final AlertDialog alertDialog2 = alert.create();
        value.setText(selectedMonth + "/" + selectedYear);

        yearlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapterYear.changeposition(pos);
                if (pos == 0) {
                    itemsMonths.clear();
                    for (int i = mMonth; i <= 12; i++) {
                        if (i < 10)
                            itemsMonths.add("0" + i);
                        else
                            itemsMonths.add("" + i);
                    }
                } else {
                    itemsMonths.clear();
                    for (int i = 1; i <= 12; i++)
                        if (i < 10)
                            itemsMonths.add("0" + i);
                        else
                            itemsMonths.add("" + i);
                }
                adapterMonths.notifyDataSetChanged();

                selectedYear = itemsYear.get(pos);
                value.setText(selectedMonth + "/" + selectedYear);
            }
        });

        monthlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                adapterMonths.changeposition(pos);
                selectedMonth = itemsMonths.get(pos);
                value.setText(selectedMonth + "/" + selectedYear);

            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2.dismiss();

            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText(selectedMonth + "/" + selectedYear.substring(Math.max(selectedYear.length() - 2, 0)));
                alertDialog2.dismiss();
            }
        });
        alertDialog2.show();

    }


    private boolean isValid() {
        if (!Validations.isStringNotEmpty(type)) {
            Alerts.okAlert(this, Constants.REQUIRED_FIELD, "Card type!");
            return false;
        } else if (!Validations.isFieldNotEmpty(card_number)) {
            return false;
        } else if (!Validations.isFieldNotEmpty(exp_date))
            return false;
        else if (!Validations.isFieldNotEmpty(cvv))
            return false;
        else {
            return true;
        }
    }

    private void textChangeListener() {
        card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

               /* if (type.equals("american")) {
                    if (i == 3 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                    if (i == 10 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                } else if (type.equals("visa") || type.equals("master") || type.equals("discover")) {
                    if (i == 3 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());

                    }
                    if (i == 9 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                    if (i == 14 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                }*/
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i == 0) {
                    if (charSequence.toString().equals("4")) {
                        type = "visa";
                        card_type.setImageDrawable(getResources().getDrawable(R.drawable.visa_icon));
                    } else if (charSequence.toString().equals("5")) {
                        type = "master";
                        card_type.setImageDrawable(getResources().getDrawable(R.drawable.mastercard_icon));
                    } else if (charSequence.toString().equals("3")) {
                        type = "american";
                        card_type.setImageDrawable(getResources().getDrawable(R.drawable.american_express_logo));
                    } else if (charSequence.toString().equals("6")) {
                        type = "discover";
                        card_type.setImageDrawable(getResources().getDrawable(R.drawable.discover_icon));
                    }
                }
                if (type.equals("american")) {
                    setEditTextMaxLength(card_number, 17);
                } else
                    setEditTextMaxLength(card_number, 19);


                if (type.equals("american")) {
                    if (i == 3 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                    if (i == 10 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                } else if (type.equals("visa") || type.equals("master") || type.equals("discover")) {
                    if (i == 3 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());

                    }
                    if (i == 8 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                    if (i == 13 && i2 == 1) {
                        card_number.setText(charSequence + "-");
                        card_number.setSelection(card_number.getText().length());
                    }
                }


                card_number.setSelection(card_number.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


       /* exp_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (type.equals("")) {
                    card_number.setError(Constants.REQUIRED_FIELD);
                    card_number.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i == 1 && i2 == 1) {
                    exp_date.setText(charSequence + "/");
                }
                exp_date.setSelection(exp_date.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (type.equals("")) {
                    card_number.setError(Constants.REQUIRED_FIELD);
                    card_number.requestFocus();
                } else if (type.equals("american")) {
                    setEditTextMaxLength(cvv, 4);
                } else
                    setEditTextMaxLength(cvv, 3);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cvv.setSelection(cvv.getText().length());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                if (scanResult.getFormattedCardNumber().toString().charAt(0) == '4') {
                    type = "visa";
                    card_type.setImageDrawable(getResources().getDrawable(R.drawable.visa_icon));
                } else if (scanResult.getFormattedCardNumber().toString().charAt(0) == '5') {
                    type = "master";
                    card_type.setImageDrawable(getResources().getDrawable(R.drawable.mastercard_icon));
                } else if (scanResult.getFormattedCardNumber().toString().charAt(0) == '3') {
                    type = "american";
                    card_type.setImageDrawable(getResources().getDrawable(R.drawable.american_express_logo));
                } else if (scanResult.getFormattedCardNumber().toString().charAt(0) == '6') {
                    type = "discover";
                    card_type.setImageDrawable(getResources().getDrawable(R.drawable.discover_icon));
                }


                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
                card_number.setText("" + scanResult.getFormattedCardNumber().toString().replaceAll("\\s+", "-"));

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    exp_date.setText("" + scanResult.expiryMonth + "/" +
                            String.valueOf(scanResult.expiryYear).substring(Math.max(String.valueOf(scanResult.expiryYear).length() - 2, 0)));
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                    cvv.setText("" + scanResult.cvv.length());
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!getIntent().hasExtra("back")) {
                finish();
                super.onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!getIntent().hasExtra("back")) {
            super.onBackPressed();
        }
    }
}
