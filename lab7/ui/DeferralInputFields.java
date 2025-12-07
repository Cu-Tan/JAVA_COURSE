package com.example.lab7.ui;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lab7.R;
import com.example.lab7.logic.DeferralData;

import java.util.function.Consumer;

public class DeferralInputFields {

  public static void init(
    Activity _activity,
    Consumer<DeferralData> _onConfirm,
    Runnable _onClear
  ) {

    LinearLayout inputFields = _activity.findViewById(R.id.deferral_input_fields);

    LinearLayout startMonthRow = inputFields.findViewById(R.id.input_row1);
    LinearLayout durationRow = inputFields.findViewById(R.id.input_row2);
    LinearLayout interestRow = inputFields.findViewById(R.id.input_row3);

    ((TextView) startMonthRow.findViewById(R.id.label)).setText("Defferal start month:");
    ((TextView) durationRow.findViewById(R.id.label)).setText("Defferal duration:");
    ((TextView) interestRow.findViewById(R.id.label)).setText("Defferal interest (%):");

    EditText startMonthField = startMonthRow.findViewById(R.id.input_field);
    EditText durationField = durationRow.findViewById(R.id.input_field);
    EditText interestField = interestRow.findViewById(R.id.input_field);

    TextView errorText = inputFields.findViewById(R.id.error);

    Button confirmButton = inputFields.findViewById(R.id.confirm_button);
    confirmButton.setOnClickListener( view -> {

      try {

        int startMonth = Integer.parseInt(startMonthField.getText().toString());
        int duration = Integer.parseInt(durationField.getText().toString());
        double interest = Double.parseDouble(interestField.getText().toString());

        _onConfirm.accept( new DeferralData(
          startMonth,
          duration,
          interest
        ));

        errorText.setVisibility(TextView.GONE);

      }
      catch (NumberFormatException e) {

        errorText.setVisibility(TextView.VISIBLE);

      }

    });

    Button clearButton = inputFields.findViewById(R.id.clear_button);
    clearButton.setOnClickListener( view -> {
      _onClear.run();
    });

  }

}
