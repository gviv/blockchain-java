package blockchain;

import java.util.ArrayList;
import java.util.Random;
import org.bitcoinj.core.ECKey;

/**
 * Cette classe permet de g�n�rer des transactions al�atoires.
 *
 * @author Guillaume Vivies
 *
 */
public class TransactionGenerator {

  /**
   * Le montant maximal d'une transaction.
   */
  public static final int MAX_VALUE = 100;

  /**
   * Permet de g�n�rer des transactions.
   *
   * @param nbAddresses       le nombre d'adresses al�atoires � g�n�rer
   * @param nbMaxTransactions le nombre maximal de transactions
   * @return la liste des transactions
   */
  public static ArrayList<Transaction> generateTransactions(int nbAddresses, int nbMaxTransactions) {
    Random r = new Random();
    int nbTransactions = r.nextInt(nbMaxTransactions) + 1;

    ArrayList<Transaction> transactions = new ArrayList<Transaction>(nbTransactions);
    ECKey[] keypairs = AddressGenerator.generateKeypairs(nbAddresses);

    for (int i = 0; i < nbTransactions; ++i) {
      // G�n�ration al�atoire d'un montant
      int amount = r.nextInt(MAX_VALUE) + 1;

      // G�n�ration al�atoire d'un indice pour l'adresse �metteur et r�cepteur (�
      // piocher dans keypairs)
      int senderIndex = r.nextInt(nbAddresses);
      // Tant que l'adresse �metteur est la m�me que l'adresse destinataire, on
      // continue � tirer au sort un nombre
      int receiverIndex = senderIndex;
      while (senderIndex == receiverIndex) receiverIndex = r.nextInt(nbAddresses);

      // R�cup�ration des adresses �metteur et r�cepteur sous forme hexad�cimale
      String senderAddress = keypairs[senderIndex].getPublicKeyAsHex();
      String receiverAddress = keypairs[receiverIndex].getPublicKeyAsHex();

      // Cr�ation de la transaction
      Transaction t = new Transaction(i, senderAddress, receiverAddress, amount);
      t.sign(keypairs[senderIndex]);
      transactions.add(t);
    }

    return transactions;
  }

}
