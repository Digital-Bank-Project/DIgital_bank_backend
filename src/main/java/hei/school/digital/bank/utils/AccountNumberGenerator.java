package hei.school.digital.bank.utils;


import java.util.UUID;

public class AccountNumberGenerator {
  public static String generateAccountNumber() {
    UUID uuid = UUID.randomUUID();
    long mostSignificantBits = uuid.getMostSignificantBits() & 0x7FFFFFFF;
    String formattedNumber = String.format("%08d", mostSignificantBits % 100000000);
    return formattedNumber;
  }

}
