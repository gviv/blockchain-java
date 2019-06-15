package blockchain;

import java.util.ArrayList;

/**
 * Cette classe permet de générer une blockchain.
 *
 * @author Guillaume Vivies
 *
 */
public class BlockchainGenerator {

  /**
   * Nombre d'adresses différentes à générer.
   */
  private static final int NB_ADDRESSES = 10;

  /**
   * Permet de générer un bloc.
   *
   * @param nbMaxTransactions le nombre maximal de transactions
   * @return le bloc généré
   */
  public static Block generateBlock(int nbMaxTransactions) {
    ArrayList<Transaction> transactions = TransactionGenerator.generateTransactions(NB_ADDRESSES, nbMaxTransactions);
    return new Block(transactions);
  }

  /**
   * Permet de générer une blockchain.
   *
   * @param nbBlocks          le nombre de blocs
   * @param difficulty        la difficulté
   * @param nbMaxTransactions le nombre maximal de transactions par bloc
   * @return la blockchain générée
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
