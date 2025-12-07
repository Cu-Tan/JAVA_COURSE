package com.example.lab7.logic;

/**
 * Holds loan data that was filled out by the user
 */
public class LoanInputData {

  public double loanAmount;
  public int loanDuration;
  public LoanType loanType;
  public double loanInterest;

  public LoanInputData(
      double _loanAmount,
      int _loanDuration,
      LoanType _loanType,
      double _loanInterest
  ){
    this.loanAmount = _loanAmount;
    this.loanDuration = _loanDuration;
    this.loanType = _loanType;
    this.loanInterest = _loanInterest;
  }

}
