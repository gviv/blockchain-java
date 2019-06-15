package blockchain;

import java.util.ArrayList;
import java.util.Random;
import org.bitcoinj.core.ECKey;

/**
 * Cette classe permet de générer des transactions aléatoires.
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
   * Permet de générer des transactions.
   *
   * @param nbAddresses       le nombre d'adresses aléatoires à générer
   * @param nbMaxTransactions le nombre maximal de transactions
   * @return la liste des transactions
   */
  public static ArrayList<Transaction> generateTransactions(int nbAddresses, int nbMaxTransactions) {
    Random r = new Random();
    int nbTransactions = r.nextInt(nbMaxTransactions) + 1;

    ArrayList<Transaction> transactions = new ArrayList<Transaction>(nbTransactions);
    ECKey[] keypairs = AddressGenerator.generateKeypairs(nbAddresses);

    for (int i = 0; i < nbTransactions; ++i) {
      // Génération aléatoire d'un montant
      int amount = r.nextInt(MAX_VALUE) + 1;

      // Génération aléatoire d'un indice pour l'adresse émetteur et récepteur (à
      // piocher dans keypairs)
      int senderIndex = r.nextInt(nbAddresses);
      // Tant que l'adresse émetteur est la même que l'adresse destinataire, on
      // continue à tirer au sort un nombre
      int receiverIndex = senderIndex;
      while (senderIndex == receiverIndex) receiverIndex = r.nextInt(nbAddresses);

      // Récupération des adresses émetteur et récepteur sous forme hexadécimale
      String senderAddress = keypairs[senderIndex].getPublicKeyAsHex();
      String receiverAddress = keypairs[receiverIndex].getPublicKeyAsHex();

      // Création de la transaction
      Transaction t = new Transaction(i, senderAddress, receiverAddress, amount);
      t.sign(keypairs[senderIndex]);
      transactions.add(t);
    }

    return transactions;
  }

}
