package blockchain.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Cette classe permet de récupérer le timestamp courant formaté.
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
   * Permet de récupérer le timestamp actuel.
   *
   * @return le timestamp actuel
   */
  public static String getTimestamp() {
    return sdf.format(new Timestamp(System.currentTimeMillis()));
  }

}
