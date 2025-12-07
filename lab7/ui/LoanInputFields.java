package com.example.lab7.ui;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lab7.R;
import com.example.lab7.logic.LoanInputData;
import com.example.lab7.logic.LoanType;

import java.util.function.Consumer;

public class LoanInputFields {

  public static void init(
    Activity _activity,
    Consumer<LoanInputData> onConfirm
  ) {
    LinearLayout inputFields = _activity.findViewById(R.id.loan_input_fields);

    LinearLayout loanAmountRow = inputFields.findViewById(R.id.input_row1);
    LinearLayout loanDurationRow = inputFields.findViewById(R.id.input_row2);
    LinearLayout loanTypeRow = inputFields.findViewById(R.id.input_row3);
    LinearLayout loanInterestRow = inputFields.findViewById(R.id.input_row4);

    TextView loanAmountLabel = loanAmountRow.findViewById(R.id.label);
    loanAmountLabel.setText("Loan amount:");

    TextView loanDurationLabel = loanDurationRow.findViewById(R.id.label);
    loanDurationLabel.setText("Loan duration:");

    TextView loanTypeLabel = loanTypeRow.findViewById(R.id.label);
    loanTypeLabel.setText("Loan type:");

    TextView loanInterestLabel = loanInterestRow.findViewById(R.id.label);
    loanInterestLabel.setText("Loan interest (%):");

    EditText loanAmountField = loanAmountRow.findViewById(R.id.input_field);
    EditText loanDurationField = loanDurationRow.findViewById(R.id.input_field);
    Spinner loanTypeField = loanTypeRow.findViewById(R.id.dropdown);
    EditText loanInterestField = loanInterestRow.findViewById(R.id.input_field);

    ArrayAdapter<LoanType> adapter = new ArrayAdapter<>(
      _activity,
      android.R.layout.simple_spinner_item,
      LoanType.values()
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    loanTypeField.setAdapter(adapter);

    TextView errorText = inputFields.findViewById(R.id.error);

    Button confirmButton = inputFields.findViewById(R.id.confirm_button);
    confirmButton.setOnClickListener(view -> {

      try {

        double loanAmount = Double.parseDouble(loanAmountField.getText().toString());
        int loanDuration = Integer.parseInt(loanDurationField.getText().toString());
        LoanType loanType = LoanType.valueOf(loanTypeField.getSelectedItem().toString());
        double loanInterest = Double.parseDouble(loanInterestField.getText().toString());

        onConfirm.accept( new LoanInputData(
          loanAmount, loanDuration, loanType, loanInterest
        ));

        errorText.setVisibility(TextView.GONE);

      }
      catch (IllegalArgumentException e) {

        errorText.setVisibility(TextView.VISIBLE);

      }

    });

  }

}
