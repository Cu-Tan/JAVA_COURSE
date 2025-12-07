package com.example.lab7.logic;

import java.util.ArrayList;

/**
 * Used to create specific loan types
 */
public abstract class Loan {

  public Loan(
    double _loanAmount,
    int _loanDuration,
    double _loanInterest
  ) {
    this.loanAmount = _loanAmount;
    this.loanDuration = _loanDuration;
    this.loanInterest = _loanInterest;
  }

  /**
   * Used to hold all the data relating to a specific month in the full loan
   */
  public class MonthData{

    public double loanRemainder;
    public double loanPayment;
    public double loanInterest;
    public double loanCredit;

    public MonthData(
      double _loanRemainder,
      double _loanPayment,
      double _loanInterest,
      double _loanCredit
    ){
      this.loanRemainder = _loanRemainder;
      this.loanPayment = _loanPayment;
      this.loanInterest = _loanInterest;
      this.loanCredit = _loanCredit;
    }

  }
  /**
   * Calculates loan data
   */
  abstract public void calculate();
  /**
   * @return loan duration without deferrals
   */
  public int getBaseDuration(){
    return loanDuration;
  }
  /**
   * @return loan duration including deferrals
   */
  public int getDuration(){
    return fullMonthData.size();
  }
  /**
   * @return start month of the loan (a.k.a. start of filter)
   */
  public int getStartMonth(){
    return startMonth;
  }
  public MonthData[] getUnfilteredMonthData() { return fullMonthData.toArray(new MonthData[0]); }
  public MonthData[] getMonthData(){
    return getFilteredData();
  }

  public void setFilter(FilterData _filterData){
    filterData = _filterData;
    if(filterData == null){
      startMonth = 1;
    }
    else {
      startMonth = filterData.start;
    }
  }
  public void setDeferral(DeferralData _deferralData) {

    if(_deferralData == null){
      fullMonthData = new ArrayList<>(baseMonthData);
    }
    else {
      addDeferral(_deferralData);
    }

  }

  protected final int DECIMAL_POINTS = 10;

  protected ArrayList<MonthData> baseMonthData;
  protected ArrayList<MonthData> fullMonthData;
  protected FilterData filterData;
  protected double loanAmount;
  protected int loanDuration;
  protected double loanInterest;
  protected int startMonth;

  /**
   * Adds deferral data to a loan and sets fullMonthData
   * @param _deferralData
   */
  protected void addDeferral(DeferralData _deferralData) {

    if(_deferralData == null || baseMonthData == null){
      return;
    }

    fullMonthData = new ArrayList<>(baseMonthData);

    int deferralStartIndex = _deferralData.deferralStart - 1;

    if(deferralStartIndex > this.fullMonthData.size() || deferralStartIndex < 0){
      System.out.println("ERROR WITH CALC WITH DEFERRAL");
      return;
    }

    double loanRemainder = this.fullMonthData.get(deferralStartIndex).loanRemainder;
    double deferralInterestMonthly = _deferralData.deferralInterest / (100 * 12);
    double loanPayment = loanRemainder * deferralInterestMonthly;

    MonthData deferralMonthData = new MonthData(
        loanRemainder,
        loanPayment,
        loanPayment,
        0
    );

    int deferralDuration = _deferralData.deferralDuration;

    while(deferralDuration > 0){
      this.fullMonthData.add(deferralStartIndex, deferralMonthData);
      ++deferralStartIndex;
      --deferralDuration;
    }
  }

  /**
   * @return data that meets filter requirements or the data if filter is null
   */
  protected MonthData[] getFilteredData(){

    if(filterData == null){
      return fullMonthData.toArray(new MonthData[0]);
    }

    ArrayList<MonthData> filteredData = new ArrayList<>();

    for(int i = filterData.start - 1; i <= filterData.end - 1; ++i){
      filteredData.add(fullMonthData.get(i));
    }

    return filteredData.toArray(new MonthData[0]);
  }
}
