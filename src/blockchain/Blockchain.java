package blockchain;

import java.util.ArrayList;

/**
 * Cette classe représente une blockchain.
 *
 * @author Guillaume Vivies
 *
 */
public class Blockchain {

  /**
   * Difficulté du minage des blocs.
   */
  private int difficulty;

  /**
   * Nombre de blocs de la chaîne.
   */
  private int nbBlocks;

  /**
   * Liste des blocs composant la chaîne.
   */
  private ArrayList<Block> blocks;

  /**
   * Constructeur de Blockchain.
   *
   * @param difficulty la difficulté de la blockchain
   */
  public Blockchain(int difficulty) {
    this.blocks = new ArrayList<Block>();
    this.difficulty = difficulty;

    // Création d'une transaction bidon
    Transaction t = new Transaction(0, "", "", 0);
    t.setSignature("");
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    transactions.add(t);

    // Création du premier bloc
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
   * Permet de récupérer le dernier bloc de la chaîne.
   *
   * @return le dernier bloc de la chaîne
   */
  public Block getLastBlock() {
    return blocks.get(blocks.size() - 1);
  }

  /**
   * Permet de récupérer le bloc d'indice i.
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
   * Ajoute un bloc à la blockchain.
   *
   * @param b le bloc à ajouter
   */
  public void addBlock(Block b) {
    b.setIndex(nbBlocks);
    b.setPreviousHash(getLastBlock().getHash());
    b.mine(difficulty);
    nbBlocks++;
    blocks.add(b);
  }

  /**
   * Vérifie si la blockchain est valide.
   *
   * @return true si valide, false sinon
   */
  public boolean isValid() {
    Block genesisBlock = blocks.get(0);
    // Vérification que le premier bloc est le bloc Genesis
    if (!genesisBlock.getPreviousHash().equals("0")) return false;
    // Vérification du merkle root
    if (!genesisBlock.isValidMerkleRoot()) return false;
    // Vérification du hash
    if (!genesisBlock.isValidHash()) return false;

    Block curBlock;
    for (int i = 1; i < this.nbBlocks; ++i) {
      curBlock = blocks.get(i);
      // Vérification du merkle root
      if (!curBlock.isValidMerkleRoot()) return false;
      // Vérification du hash
      if (!curBlock.isValidHash()) return false;
      // Vérification du hash précédent
      if (!curBlock.getPreviousHash().equals(blocks.get(i - 1).getHash())) return false;
      // Vérification des transactions
      if (!curBlock.checkTransactions()) return false;
    }

    return true;
  }

}
