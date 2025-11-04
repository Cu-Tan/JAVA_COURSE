package com.example.paskolos_skaiciuokle.logic;

import java.util.ArrayList;

/**
 * Loan that uses linear return type
 */
public class LoanLinear extends Loan{

  public LoanLinear(double _loanAmount, int _loanDuration, double _loanInterest) {
    super(_loanAmount, _loanDuration, _loanInterest);
  }

  @Override
  public void calculate() {

    startMonth = 1;

    baseMonthData = new ArrayList<>();

    double loanRemainder = this.loanAmount;
    double loanDuration = this.loanDuration;
    double loanCredit = this.loanAmount / this.loanDuration;

    double loanInterestMonthly = this.loanInterest / (100 * 12);

    while(loanDuration > 0) {
      double loanInterest = loanRemainder * loanInterestMonthly;
      double loanPayment = loanCredit + loanInterest;

      this.baseMonthData.add(new MonthData(
          loanRemainder,
          loanPayment,
          loanInterest,
          loanCredit
      ));

      loanRemainder -= loanCredit;
      --loanDuration;
    }
    fullMonthData = new ArrayList<>(baseMonthData);
  }
}
