package blockchain;

import java.util.ArrayList;
import blockchain.utils.HashUtil;

/**
 * Cette classe représente un arbre de Merkle.
 *
 * @author Guillaume Vivies
 *
 */
public class MerkleTree {

  /**
   * La valeur de l'arbre.
   */
  private String root;

  /**
   * Le sous-arbre gauche.
   */
  private MerkleTree left;

  /**
   * Le sous-arbre droit.
   */
  private MerkleTree right;

  /**
   * Constructeur de MerkleTree.
   *
   * @param transactions   la liste des transactions
   * @param nbTransactions le nombre de transactions
   */
  public MerkleTree(ArrayList<Transaction> transactions, int nbTransactions) {
    MerkleTree[] trees = new MerkleTree[nbTransactions + 1];

    // On met dans trees le hash initial des transactions
    for (int i = 0; i < nbTransactions; ++i) {
      trees[i] = new MerkleTree(null, HashUtil.applySha256(transactions.get(i).toString()), null);
    }

    // L'arbre voulu est le premier de la liste renvoyée par hashList()
    MerkleTree t = hashList(trees, nbTransactions)[0];

    this.root = t.root;
    this.left = t.left;
    this.right = t.right;
  }

  /**
   * Getter root.
   *
   * @return root
   */
  public String getRoot() {
    return root;
  }

  /**
   * Remplace chaque élément de la liste par le hash du root de list[i] et list[i
   * + 1].
   *
   * @param list la liste à modifier
   * @param nb   le nombre d'éléments de la liste
   * @return la liste modifiée
   */
  private MerkleTree[] hashList(MerkleTree[] list, int nb) {
    if (nb == 1)
      return list;

    if (nb % 2 == 1) {
      list[nb] = list[nb - 1];
      nb++;
    }

    int k = 0;
    for (int i = 0; i < nb - 1; i += 2) {
      list[k++] = new MerkleTree(list[i], HashUtil.applySha256(list[i].root + list[i + 1].root), list[i + 1]);
    }

    return hashList(list, nb / 2);
  }

  /**
   * Constructeur privé utilisé dans la fonction hashList.
   *
   * @param left  l'arbre de gauche
   * @param root  la valeur du noeud
   * @param right l'arbre de droite
   */
  private MerkleTree(MerkleTree left, String root, MerkleTree right) {
    this.left = left;
    this.root = root;
    this.right = right;
  }

}
