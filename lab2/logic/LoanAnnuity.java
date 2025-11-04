package com.example.paskolos_skaiciuokle.logic;

import java.util.ArrayList;

/**
 * Loan that uses annuity return type
 */
public class LoanAnnuity extends Loan{

  public LoanAnnuity(double _loanAmount, int _loanDuration, double _loanInterest) {
    super(_loanAmount, _loanDuration, _loanInterest);
  }

  @Override
  public void calculate() {

    startMonth = 1;

    this.baseMonthData = new ArrayList<>();

    double loanRemainder = this.loanAmount;
    double loanDuration = this.loanDuration;

    double loanInterestMonthly = this.loanInterest / (100 * 12);

    double tempNum = Math.pow(loanInterestMonthly + 1, this.loanDuration); // (i + 1)^n
    double loanPayment = this.loanAmount * (loanInterestMonthly * tempNum) / (tempNum - 1); // SUM * (i * (i + 1)^n) / ((i + 1)^n - 1)

    while(loanDuration > 0) {
      double loanInterest = loanRemainder * loanInterestMonthly;
      double loanCredit = loanPayment - loanInterest;

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
