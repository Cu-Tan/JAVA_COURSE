package com.example.paskolos_skaiciuokle.logic;

/**
 * Holds all data about loan deferrals
 */
public class DeferralData {

  public DeferralData(
      int _deferralStart,
      int _deferralDuration,
      float _deferralInterest
  ){
    this.deferralStart = _deferralStart;
    this.deferralDuration = _deferralDuration;
    this.deferralInterest = _deferralInterest;
  }

  public int deferralStart;
  public int deferralDuration;
  public float deferralInterest;

}
