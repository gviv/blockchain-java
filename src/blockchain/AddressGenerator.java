package blockchain;

import org.bitcoinj.core.ECKey;

/**
 * Cette classe permet de générer aléatoirement des adresses
 * émetteur/destinataire.
 *
 * @author Guillaume Vivies
 *
 */
public class AddressGenerator {

  /**
   * Permet de générer des paires de clé (adresses) aléatoires.
   *
   * @param n le nombre de paires à générer
   * @return les paires de clé générées aléatoirement
   */
  public static ECKey[] generateKeypairs(int n) {
    ECKey[] keypairs = new ECKey[n];

    for (int i = 0; i < n; ++i)
      keypairs[i] = new ECKey();
    return keypairs;
  }

}
