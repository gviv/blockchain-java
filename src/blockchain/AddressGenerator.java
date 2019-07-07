package blockchain;

import org.bitcoinj.core.ECKey;

/**
 * Cette classe permet de g�n�rer al�atoirement des adresses
 * �metteur/destinataire.
 *
 * @author Guillaume Vivies
 *
 */
public class AddressGenerator {

  /**
   * Permet de g�n�rer des paires de cl� (adresses) al�atoires.
   *
   * @param n le nombre de paires � g�n�rer
   * @return les paires de cl� g�n�r�es al�atoirement
   */
  public static ECKey[] generateKeypairs(int n) {
    ECKey[] keypairs = new ECKey[n];

    for (int i = 0; i < n; ++i)
      keypairs[i] = new ECKey();
    return keypairs;
  }

}
