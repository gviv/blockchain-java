package blockchain;

import java.util.ArrayList;
import blockchain.utils.DateUtil;
import blockchain.utils.HashUtil;

/**
 * Cette classe représente un bloc de la blockchain.
 *
 * @author Guillaume Vivies
 *
 */
public class Block {

  /**
   * Indice du bloc dans la chaîne.
   */
  private int index;

  /**
   * Timestamp de la date de création du bloc.
   */
  private String timestamp;

  /**
   * Hash du bloc courant.
   */
  private String hash;

  /**
   * Hash du bloc précédent.
   */
  private String previousHash;

  /**
   * Nombre de transactions.
   */
  private int nbTransactions;

  /**
   * Liste des transactions.
   */
  private ArrayList<Transaction> transactions;

  /**
   * Racine de l'arbre de Merkle des transactions.
   */
  private String merkleRoot;

  /**
   * Nonce du bloc.
   */
  private int nonce;

  /**
   * Constructeur de Block. Le bloc créé n'est pas complet : il manque l'index, le
   * hash précédent et il faut le miner.
   *
   * @param list la liste des transactions
   */
  public Block(ArrayList<Transaction> list) {
    this.index = 0; // L'index sera défini lors de l'ajout à la blockchain
    this.timestamp = DateUtil.getTimestamp();
    this.transactions = list;
    this.nbTransactions = list == null ? 0 : list.size();
    this.previousHash = null; // Idem
    this.nonce = 0;
    this.merkleRoot = list == null ? "" : this.calculateMerkleRoot();
  }

  /**
   * Getter index.
   *
   * @return index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Getter timestamp.
   *
   * @return timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }

  /**
   * Getter hash.
   *
   * @return hash
   */
  public String getHash() {
    return hash;
  }

  /**
   * Getter previousHash.
   *
   * @return previousHash
   */
  public String getPreviousHash() {
    return previousHash;
  }

  /**
   * Getter nbTransactions.
   *
   * @return nbTransactions
   */
  public int getNbTransactions() {
    return nbTransactions;
  }

  /**
   * Getter transactions.
   *
   * @return transactions
   */
  public ArrayList<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Getter merkleRoot.
   *
   * @return merkleRoot
   */
  public String getMerkleRoot() {
    return merkleRoot;
  }

  /**
   * Getter nonce.
   *
   * @return nonce
   */
  public int getNonce() {
    return nonce;
  }

  /**
   * Setter index.
   *
   * @param index l'index
   */
  public void setIndex(int index) {
    this.index = index;
  }

  /**
   * Setter hash.
   *
   * @param hash le hash
   */
  public void setHash(String hash) {
    this.hash = hash;
  }

  /**
   * Setter previousHash.
   *
   * @param previousHash le hash du bloc précédent
   */
  public void setPreviousHash(String previousHash) {
    this.previousHash = previousHash;
  }

  /**
   * Calcule le hash du bloc courant.
   *
   * @return le hash du bloc
   */
  public String calculateHash() {
    String str = String.valueOf(index) + timestamp + previousHash + String.valueOf(nbTransactions);

    for (Transaction t : transactions)
      str += t.toString();
    str += merkleRoot + String.valueOf(nonce);

    return HashUtil.applySha256(str);
  }

  /**
   * Calcule le hash root de l'arbre de Merkle des transactions.
   *
   * @return le Merkle Root des transactions
   */
  public String calculateMerkleRoot() {
    return new MerkleTree(transactions, nbTransactions).getRoot();
  }

  /**
   * Mine le bloc et met à jour ses attributs.
   *
   * @param difficulty la difficulté du minage
   */
  public void mine(int difficulty) {
    String s = "";
    for (int i = 0; i < difficulty; ++i)
      s += "0";

    String hash = calculateHash();

    while (!hash.startsWith(s)) {
      this.nonce++;
      hash = this.calculateHash();
    }

    this.hash = hash;
  }

  /**
   * Vérifie si le Merkle Root est valide par rapport aux transactions.
   *
   * @return true si valide, false sinon
   */
  public boolean isValidMerkleRoot() {
    return this.calculateMerkleRoot().equals(this.merkleRoot);
  }

  /**
   * Vérifie si le hash est valide.
   *
   * @return true si valide, false sinon
   */
  public boolean isValidHash() {
    return this.calculateHash().equals(this.hash);
  }

  /**
   * Vérifie si la signature des transactions est valide.
   *
   * @return true si valide, false sinon
   */
  public boolean checkTransactions() {
    for (Transaction t : transactions) {
      if (!t.checkSignature())
        return false;
    }
    return true;
  }

}
