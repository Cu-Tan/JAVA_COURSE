package com.example.lab7.ui;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lab7.R;
import com.example.lab7.logic.FilterData;

import java.util.function.Consumer;

public class FilterInputFields {

  public static void init(
    Activity _activity,
    Consumer<FilterData> _onConfirm,
    Runnable _onClear
  ) {

    LinearLayout inputFields = _activity.findViewById(R.id.filter_input_fields);

    LinearLayout startMonthRow = inputFields.findViewById(R.id.input_row1);
    LinearLayout endMonthRow = inputFields.findViewById(R.id.input_row2);

    ((TextView) startMonthRow.findViewById(R.id.label)).setText("Start month:");
    ((TextView) endMonthRow.findViewById(R.id.label)).setText("End month:");

    EditText startMonthField = startMonthRow.findViewById(R.id.input_field);
    EditText endMonthField = endMonthRow.findViewById(R.id.input_field);

    TextView errorText = inputFields.findViewById(R.id.error);

    Button confirmButton = inputFields.findViewById(R.id.confirm_button);
    confirmButton.setOnClickListener( view -> {

      try {

        int startMonth = Integer.parseInt( startMonthField.getText().toString() );
        int endMonth = Integer.parseInt( endMonthField.getText().toString() );

        _onConfirm.accept( new FilterData(
          startMonth,
          endMonth
        ));

        errorText.setVisibility(TextView.GONE);

      }
      catch (NumberFormatException e) {

        errorText.setVisibility(TextView.VISIBLE);

      }

    });

    Button clearButton = inputFields.findViewById(R.id.clear_button);
    clearButton.setOnClickListener( view -> _onClear.run() );

  }

}
