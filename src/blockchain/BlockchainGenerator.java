package blockchain;

import java.util.ArrayList;

/**
 * Cette classe permet de g�n�rer une blockchain.
 *
 * @author Guillaume Vivies
 *
 */
public class BlockchainGenerator {

  /**
   * Nombre d'adresses diff�rentes � g�n�rer.
   */
  private static final int NB_ADDRESSES = 10;

  /**
   * Permet de g�n�rer un bloc.
   *
   * @param nbMaxTransactions le nombre maximal de transactions
   * @return le bloc g�n�r�
   */
  public static Block generateBlock(int nbMaxTransactions) {
    ArrayList<Transaction> transactions = TransactionGenerator.generateTransactions(NB_ADDRESSES, nbMaxTransactions);
    return new Block(transactions);
  }

  /**
   * Permet de g�n�rer une blockchain.
   *
   * @param nbBlocks          le nombre de blocs
   * @param difficulty        la difficult�
   * @param nbMaxTransactions le nombre maximal de transactions par bloc
   * @return la blockchain g�n�r�e
   */
  public static Blockchain generateBlockchain(int nbBlocks, int difficulty, int nbMaxTransactions) {
    Blockchain blockchain = new Blockchain(difficulty);
    for (int i = 0; i < nbBlocks - 1; ++i) {
      Block b = generateBlock(nbMaxTransactions);
      blockchain.addBlock(b);
    }
    return blockchain;
  }

}
