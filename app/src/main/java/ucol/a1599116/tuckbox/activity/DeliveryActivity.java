package ucol.a1599116.tuckbox.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ucol.a1599116.tuckbox.R;
import ucol.a1599116.tuckbox.util.ActivityHelper;
import ucol.a1599116.tuckbox.util.Debug;
import ucol.a1599116.tuckbox.util.LoginHelper;
import ucol.a1599116.tuckbox.util.Luhn;
import ucol.a1599116.tuckbox.util.TimeHelper;

/**
 * Delivery details activity
 * Handles user input for credit card and delivery details
 */
public class DeliveryActivity extends AbstractBaseActivity {

    //Credit card number, credit card CVV/CVC/CCV number, and address street input fields
    private EditText etCreditCard, etCreditCardCVV, etAddressStreet;

    //Credit card expiry, address region, and delivery time spinner/dropdown fields
    private LabelledSpinner spnCreditCardExpiryMonth, spnCreditCardExpiryYear, spnAddressRegion, spnDeliveryTime;

    //Calendar instance used for date and time gathering and validation
    private Calendar calendar;

    //Whether the autocomplete activity is currently being accessed
    private boolean autocomplete;

    /**
     * Activity OnCreate event
     * Called when the activity is created
     *
     * @param savedInstanceState The activities previously saved state (if it previously existed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.title_delivery);

        calendar = Calendar.getInstance(TimeZone.getTimeZone(getString(R.string.default_timezone)));

        etCreditCard = (EditText) findViewById(R.id.etCreditCard);
        etCreditCardCVV = (EditText) findViewById(R.id.etCreditCardCVV);
        etAddressStreet = (EditText) findViewById(R.id.etAddressStreet);

        spnCreditCardExpiryMonth = (LabelledSpinner) findViewById(R.id.spnCreditCardExpiryMonth);
        spnCreditCardExpiryYear = (LabelledSpinner) findViewById(R.id.spnCreditCardExpiryYear);
        spnAddressRegion = (LabelledSpinner) findViewById(R.id.spnAddressRegion);
        spnDeliveryTime = (LabelledSpinner) findViewById(R.id.spnDeliveryTime);

        //The mask to use for the credit card number input field, e.g. 0000-0000-0000-0000
        MaskedTextChangedListener creditCardMasker = new MaskedTextChangedListener(getString(R.string.default_credit_card_mask), true, etCreditCard, null, null);

        etCreditCard.addTextChangedListener(creditCardMasker);
        etCreditCard.setOnFocusChangeListener(creditCardMasker);
        etCreditCard.setHint(creditCardMasker.placeholder());

        //Populate lists used for spinner content population
        List<String> months = new ArrayList<>();
        List<String> years = new ArrayList<>();
        List<String> regions = Arrays.asList(getResources().getStringArray(R.array.default_region_select));
        List<String> deliveryTimes = Arrays.asList(getResources().getStringArray(R.array.default_delivery_times));

        int year = Calendar.getInstance(TimeZone.getTimeZone(getString(R.string.default_timezone))).get(Calendar.YEAR);
        for (int i = 1; i <= 12; i++)
            months.add(String.format(Locale.getDefault(), "%02d", i));
        for (int i = year; i < year + 10; i++)
            years.add(String.format(Locale.getDefault(), "%04d", i));

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, months);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, years);
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regions);
        ArrayAdapter<String> deliveryTimeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliveryTimes);

        spnCreditCardExpiryMonth.setCustomAdapter(monthAdapter);
        spnCreditCardExpiryYear.setCustomAdapter(yearAdapter);
        spnAddressRegion.setCustomAdapter(regionAdapter);
        spnDeliveryTime.setCustomAdapter(deliveryTimeAdapter);

        //Refill the fields with the persistent values from history re-ordering
        if (LoginHelper.fbUser.getAddressStreet() != null)
            etAddressStreet.setText(LoginHelper.fbUser.getAddressStreet());

        if (LoginHelper.fbUser.getAddressRegion() != null) {
            int regionIndex = regions.indexOf(LoginHelper.fbUser.getAddressRegion());
            spnAddressRegion.setSelection(regionIndex == -1 ? 0 : regionIndex);
        }

        if (LoginHelper.fbUser.getDeliveryTime() != null) {
            int timeIndex = deliveryTimes.indexOf(LoginHelper.fbUser.getDeliveryTime());
            spnDeliveryTime.setSelection(timeIndex == -1 ? 0 : timeIndex);
        }

        if (LoginHelper.fbUser.getCardNumber() != null)
            etCreditCard.setText(LoginHelper.fbUser.getCardNumber());

        if (LoginHelper.fbUser.getCardCVV() != null)
            etCreditCardCVV.setText(LoginHelper.fbUser.getCardCVV());

        if (LoginHelper.fbUser.getCardExpiryMonth() != null) {
            int expiryIndex = months.indexOf(LoginHelper.fbUser.getCardExpiryMonth());
            spnCreditCardExpiryMonth.setSelection(expiryIndex);
        }

        if (LoginHelper.fbUser.getCardExpiryYear() != null) {
            int expiryIndex = years.indexOf(LoginHelper.fbUser.getCardExpiryYear());
            spnCreditCardExpiryYear.setSelection(expiryIndex);
        }
    }

    /**
     * Option item selected event
     *
     * @param item The selected item
     * @return If the item was successfully selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Address street order edit text OnClick event
     *
     * @param view The view the method was invoked by
     */
    public void onClickAddressStreet(View view) {
        if (!autocomplete)
            try {
                //Create an autocomplete filter
                AutocompleteFilter filter = new AutocompleteFilter.Builder()
                        .setCountry("NZ")
                        .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                        .build();
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .setFilter(filter)
                        .build(this);
                startActivityForResult(intent, REQ_RES_GOOGLE_PLACES);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                // TODO: Handle the error.
            }
    }

    /**
     * Event called when a launched activity for results is finished
     *
     * @param requestCode The request code for the intent
     * @param resultCode  The result code for the intent
     * @param intent      The finished intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQ_RES_GOOGLE_PLACES) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, intent);
                updateFieldsWithAddress(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, intent);
                makeToast(status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            autocomplete = false;
        }
    }

    /**
     * Review order button OnClick event
     *
     * @param view The view the method was invoked by
     */
    public void onClickReviewOrder(View view) {
        final String cardNumber = etCreditCard.getText().toString().trim();
        final String cardCVV = etCreditCardCVV.getText().toString().trim();

        final int cardExpiryMonth = Integer.parseInt(spnCreditCardExpiryMonth.getSpinner().getSelectedItem().toString().trim());
        final int cardExpiryYear = Integer.parseInt(spnCreditCardExpiryYear.getSpinner().getSelectedItem().toString().trim());

        final String addressStreet = etAddressStreet.getText().toString().trim();
        final String addressRegion = spnAddressRegion.getSpinner().getSelectedItem().toString().trim();

        final String deliveryTime = spnDeliveryTime.getSpinner().getSelectedItem().toString().trim();

        if (!Debug.DEBUG) {
            //Check the credit card number matches xxxx-xxxx-xxxx-xxxx and also matches the Luhn algorithm
            //Regex matches anything with the following
            //  (4xxx || (51 to 55)xx)-xxxx-xxxx-xxxx
            //Where x is anything between 0 and 9 (inclusive)
            if (!cardNumber.matches("^((4[0-9]{3})|(5[1-5][0-9]{2}))(-[0-9]{4}){3}$") || !Luhn.check(cardNumber)) {
                Snackbar.make(view, R.string.credit_card_failed_number, Snackbar.LENGTH_SHORT).show();
                return;
            }

            //Check the credit card CVV/CVC/CCV matches xxx
            //Regex matches anything with the following
            //  xxx
            //Where x is anything between 0 and 9 (inclusive)
            if (!cardCVV.matches("^[0-9]{3}$")) {
                Snackbar.make(view, R.string.credit_card_failed_cvv, Snackbar.LENGTH_SHORT).show();
                return;
            }

            //Check if the expiry month and year are beyond the current month and year

            //Says there's an error, but it's not actually a compile or runtime error
            if (cardExpiryYear == calendar.get(Calendar.YEAR) && cardExpiryMonth <= calendar.get(Calendar.MONTH)) {
                Snackbar.make(view, R.string.credit_card_failed_expiry, Snackbar.LENGTH_SHORT).show();
                return;
            }

            //Check if the address provided is a valid address
            if (addressStreet.isEmpty()) {
                Snackbar.make(view, R.string.address_failed_street, Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (addressRegion.isEmpty()) {
                Snackbar.make(view, R.string.address_failed_region, Snackbar.LENGTH_SHORT).show();
                return;
            }

            //Check the order is being made before 10am
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 10) {
                DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    /**
                     * Date picker OnDateSet event
                     * @param datePicker    The date picker invoking the event
                     * @param i             The selected year
                     * @param i1            The selected month index
                     * @param i2            The selected day
                     */
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //Get the current month's name
                        Date temp = calendar.getTime();
                        calendar.set(i, i1, i2);
                        String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                        calendar.setTime(temp);

                        review(cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, String.format("%s %s, %s", monthName, i2, i), deliveryTime);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                //Set the minimum date for the order to be delivered by to the day after the current
                //and the maximum date to 2 weeks from the current
                dateDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() + TimeHelper.DAY_TO_MS);
                dateDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() + (TimeHelper.WEEK_TO_MS * 2));

                dateDialog.show();
                makeToast(R.string.delivery_after_10, Toast.LENGTH_LONG).show();
                return;
            }
        }

        review(cardNumber, cardCVV, cardExpiryMonth, cardExpiryYear, addressStreet, addressRegion, getString(R.string.delivery_today), deliveryTime);
    }

    /**
     * Launch the review activity with the provided extras
     *
     * @param cardNumber      The credit card number
     * @param cardCVV         The credit card CVV/CVC/CCV number
     * @param cardExpiryMonth The credit card expiry month
     * @param cardExpiryYear  The credit card expiry year
     * @param addressStreet   The address street
     * @param addressRegion   The address region
     * @param deliveryDate    The date to be delivered (always "Today" unless the order was made after 10am)
     * @param deliveryTime    The time to be delivered
     */
    private void review(String cardNumber, String cardCVV, int cardExpiryMonth, int cardExpiryYear, String addressStreet, String addressRegion, String deliveryDate, String deliveryTime) {
        LoginHelper.fbUser.setCardNumber(cardNumber);
        LoginHelper.fbUser.setCardCVV(cardCVV);
        LoginHelper.fbUser.setCardExpiryMonth(String.format(Locale.getDefault(), "%02d", cardExpiryMonth));
        LoginHelper.fbUser.setCardExpiryYear(String.valueOf(cardExpiryYear));
        LoginHelper.fbUser.setAddressStreet(addressStreet);
        LoginHelper.fbUser.setAddressRegion(addressRegion);
        LoginHelper.fbUser.setDeliveryDate(deliveryDate);
        LoginHelper.fbUser.setDeliveryTime(deliveryTime);

        ActivityHelper.launchActivity(DeliveryActivity.this, ReviewOrdersActivity.class);
    }

    /**
     * Update the address field and region spinner with the given data
     *
     * @param place The place returned from the Google Places API place selection activity
     */
    private void updateFieldsWithAddress(Place place) {
        List<String> regions = Arrays.asList(getResources().getStringArray(R.array.default_region_select));
        int regionIndex = -1;
        for (String region : regions)
            if (place.getAddress().toString().toLowerCase().contains(region.toLowerCase()))
                regionIndex = regions.indexOf(region);
        if (regionIndex != -1)
            spnAddressRegion.setSelection(regionIndex);
        etAddressStreet.setText(place.getName());
    }
}
