package blockchain;

import java.util.ArrayList;

/**
 * Cette classe repr�sente une blockchain.
 *
 * @author Guillaume Vivies
 *
 */
public class Blockchain {

  /**
   * Difficult� du minage des blocs.
   */
  private int difficulty;

  /**
   * Nombre de blocs de la cha�ne.
   */
  private int nbBlocks;

  /**
   * Liste des blocs composant la cha�ne.
   */
  private ArrayList<Block> blocks;

  /**
   * Constructeur de Blockchain.
   *
   * @param difficulty la difficult� de la blockchain
   */
  public Blockchain(int difficulty) {
    this.blocks = new ArrayList<Block>();
    this.difficulty = difficulty;

    // Cr�ation d'une transaction bidon
    Transaction t = new Transaction(0, "", "", 0);
    t.setSignature("");
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    transactions.add(t);

    // Cr�ation du premier bloc
    Block b = new Block(transactions);
    b.setIndex(0);
    b.setPreviousHash("0");
    b.setHash(b.calculateHash());
    blocks.add(b);
    this.nbBlocks = 1;
  }

  /**
   * Getter nbBlocks.
   *
   * @return nbBlocks
   */
  public int getNbBlocks() {
    return nbBlocks;
  }

  /**
   * Getter blocks.
   *
   * @return blocks
   */
  public ArrayList<Block> getBlocks() {
    return blocks;
  }

  /**
   * Permet de r�cup�rer le dernier bloc de la cha�ne.
   *
   * @return le dernier bloc de la cha�ne
   */
  public Block getLastBlock() {
    return blocks.get(blocks.size() - 1);
  }

  /**
   * Permet de r�cup�rer le bloc d'indice i.
   *
   * @param i l'indice du bloc
   * @return le bloc correspondant
   */
  public Block getBlock(int i) {
    if (i >= 0 && i < this.nbBlocks)
      return this.blocks.get(i);
    System.err.println("getBlock(" + i + ") : indice invalide.");
    return null;
  }

  /**
   * Ajoute un bloc � la blockchain.
   *
   * @param b le bloc � ajouter
   */
  public void addBlock(Block b) {
    b.setIndex(nbBlocks);
    b.setPreviousHash(getLastBlock().getHash());
    b.mine(difficulty);
    nbBlocks++;
    blocks.add(b);
  }

  /**
   * V�rifie si la blockchain est valide.
   *
   * @return true si valide, false sinon
   */
  public boolean isValid() {
    Block genesisBlock = blocks.get(0);
    // V�rification que le premier bloc est le bloc Genesis
    if (!genesisBlock.getPreviousHash().equals("0")) return false;
    // V�rification du merkle root
    if (!genesisBlock.isValidMerkleRoot()) return false;
    // V�rification du hash
    if (!genesisBlock.isValidHash()) return false;

    Block curBlock;
    for (int i = 1; i < this.nbBlocks; ++i) {
      curBlock = blocks.get(i);
      // V�rification du merkle root
      if (!curBlock.isValidMerkleRoot()) return false;
      // V�rification du hash
      if (!curBlock.isValidHash()) return false;
      // V�rification du hash pr�c�dent
      if (!curBlock.getPreviousHash().equals(blocks.get(i - 1).getHash())) return false;
      // V�rification des transactions
      if (!curBlock.checkTransactions()) return false;
    }

    return true;
  }

}
