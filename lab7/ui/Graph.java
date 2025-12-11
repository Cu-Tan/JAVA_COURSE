package com.example.lab7.ui;

import android.app.Activity;

import com.example.lab7.R;
import com.example.lab7.logic.Loan;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graph {

  public Graph(
    Activity _activity
  ) {

    graph = _activity.findViewById(R.id.graph);

    GridLabelRenderer glr = graph.getGridLabelRenderer();
    glr.setHorizontalAxisTitle("Month");
    glr.setVerticalAxisTitle("Loan payment");
    glr.setPadding(64);

  }

  public void update(
    Loan.MonthData[] monthData,
    int startMonth
  ) {

    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

    for(int i = 0; i < monthData.length; ++i){

      series.appendData(
        new DataPoint(
          i + startMonth,
          monthData[i].loanPayment
        ),
        false,
        monthData.length
      );

    }

    graph.removeAllSeries();
    graph.addSeries(series);

  }

  private final GraphView graph;

}
