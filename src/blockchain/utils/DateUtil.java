package blockchain.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Cette classe permet de r�cup�rer le timestamp courant format�.
 *
 * @author Guillaume Vivies
 *
 */
public class DateUtil {

  /**
   * Le format du timestamp.
   */
  private static final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

  /**
   * Permet de r�cup�rer le timestamp actuel.
   *
   * @return le timestamp actuel
   */
  public static String getTimestamp() {
    return sdf.format(new Timestamp(System.currentTimeMillis()));
  }

}
