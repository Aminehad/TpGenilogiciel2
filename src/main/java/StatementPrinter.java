import java.text.NumberFormat;
import java.util.*;


public class StatementPrinter {
  public static final String TRAGEDY = "tragedy";
  public static final String COMEDY = "comedy";
  

  public String print(Invoice invoice, HashMap<String, Play> plays,Customer customer) {
    int totalAmount = 0;
    int volumeCredits = 0;
    StringBuilder result = new StringBuilder();
    double cus=customer.getsoldepoints(); //get the solde points of the customer
    result.append(String.format("Statement for %s\n", invoice.customer.name));
    result.append(String.format("Your points %f\n", invoice.customer.soldepoints));

    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);

    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
       
      CalculClass calcul = new CalculClass();
      double thisAmount = calcul.calcul(perf, play,customer);

      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if (Play.playType.COMEDY.equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result.append(String.format("  %s: %s (%s seats)\n", play.name, frmt.format(thisAmount), perf.audience));
      totalAmount += thisAmount;

    }
    cus=cus+volumeCredits;
    //customer.setsoldepoints(cus); //set the solde points of the customer
    result.append(String.format("Amount owed is %s\n", frmt.format(totalAmount)));
    result.append(String.format("You earned %s credits\n", volumeCredits));
    result.append(String.format("Your point left %s credits\n", volumeCredits));
    return result.toString();
  }

  
  public String toHTML(Invoice invoice,HashMap<String, Play> plays,Customer customer) {

    int totalAmount = 0;
    int volumeCredits = 0;
    StringBuilder result = new StringBuilder();
    NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
    double cus=customer.getsoldepoints(); //get the solde points of the customer 


    for (Performance perf : invoice.performances) {
      Play play = plays.get(perf.playID);
      CalculClass calcul = new CalculClass();

      double thisAmount = calcul.calcul(perf, play,customer);
      // add volume credits
      volumeCredits += Math.max(perf.audience - 30, 0);
      // add extra credit for every ten comedy attendees
      if (Play.playType.COMEDY.equals(play.type)) volumeCredits += Math.floor(perf.audience / 5);

      // print line for this order
      result.append(String.format("  %s: %s (%s seats)\n", play.name, frmt.format(thisAmount), perf.audience));
      totalAmount += thisAmount;

     
    }
    cus=cus+volumeCredits;
    //customer.setsoldepoints(cus); //set the solde points of the customer
    // Génération du détail de la facture au format HTML
    result.append("<table>\n");
    result.append("<tr><th>Play</th><th>Seats</th><th>Amount</th></tr>\n");
    for (Performance perf : invoice.performances) {
        Play play = plays.get(perf.playID);
        result.append(String.format("  <tr><td>%s</td><td>%s</td><td>%s</td></tr>\n", play.name, perf.audience, frmt.format(totalAmount)));
    }
    result.append("</table>\n");
    result.append(String.format("<p>Amount owed is %s</p>\n", frmt.format(totalAmount)));
    result.append(String.format("<p>You earned %d credits</p>\n", volumeCredits));
    result.append(String.format("<p>Your points left %f credits</p>\n", cus));
    result.append("</body></html>\n");

    return result.toString();
}

 


}
