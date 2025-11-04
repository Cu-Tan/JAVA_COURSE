package com.example.paskolos_skaiciuokle.logic;

import java.io.FileWriter;

/**
 * Used for holding a function to save a loan to a file
 */
public class LoanSaver {

  /**
   * Saves a loan to a file
   * @param _loan - loan to save to file
   * @param _filePath - full path to file
   */
  public static void save(Loan _loan, String _filePath){

    // try with resources to close fileWriter automatically
    try(FileWriter fileWriter = new FileWriter(_filePath)){

      fileWriter.write("Month\t");
      fileWriter.write("Loan remainder\t");
      fileWriter.write("Monthly payment\t");
      fileWriter.write("Interest\t");
      fileWriter.write("Credit\n");

      int month = 1;

      double totalLoanPayment = 0;
      double totalLoanInterest = 0;

      for(Loan.MonthData loanMonthData: _loan.getUnfilteredMonthData()){

        totalLoanPayment += loanMonthData.loanPayment;
        totalLoanInterest += loanMonthData.loanInterest;

        fileWriter.write(month + "\t");
        fileWriter.write(String.format("%.2f", loanMonthData.loanRemainder) + '\t');
        fileWriter.write(String.format("%.2f", loanMonthData.loanPayment) + '\t');
        fileWriter.write(String.format("%.2f", loanMonthData.loanInterest) + '\t');
        fileWriter.write(String.format("%.2f", loanMonthData.loanCredit) + '\n');

        ++month;
      }
      fileWriter.write('\n');

      fileWriter.write("Total:\t\t");
      fileWriter.write(String.format("%.2f", totalLoanPayment) + '\t');
      fileWriter.write(String.format("%.2f", totalLoanInterest) + '\n');

      fileWriter.flush();
    }
    catch (Exception e){
      System.out.println("LOAN SAVER ERROR");
    }
  }
}
