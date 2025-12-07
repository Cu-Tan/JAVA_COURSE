package com.example.lab7.logic;

/**
 * Holds all data about loan deferrals
 */
public class DeferralData {

  public DeferralData(
      int _deferralStart,
      int _deferralDuration,
      double _deferralInterest
  ) {
    this.deferralStart = _deferralStart;
    this.deferralDuration = _deferralDuration;
    this.deferralInterest = _deferralInterest;
  }

  public int deferralStart;
  public int deferralDuration;
  public double deferralInterest;

}
