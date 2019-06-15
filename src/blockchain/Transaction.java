package blockchain;

import javax.xml.bind.DatatypeConverter;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.ECKey.ECDSASignature;
import org.bitcoinj.core.Sha256Hash;
import blockchain.utils.DateUtil;
import blockchain.utils.HashUtil;

/**
 * Cette classe représente une transaction.
 *
 * @author Guillaume Vivies
 *
 */
public class Transaction {

  /**
   * Indice de la transaction.
   */
  private int index;

  /**
   * Timestamp lors de la création.
   */
  private String timestamp;

  /**
   * Adresse de l'émetteur.
   */
  private String sender;

  /**
   * Adresse du destinataire.
   */
  private String receiver;

  /**
   * Montant de la transaction.
   */
  private int amount;

  /**
   * Signature numérique de la transaction.
   */
  private String signature;

  /**
   * Constructeur de Transaction.
   *
   * @param index    l'indice de la transaction
   * @param sender   l'adresse de l'émetteur
   * @param receiver l'adresse du destinataire
   * @param amount   le montant
   */
  public Transaction(int index, String sender, String receiver, int amount) {
    this.index = index;
    this.timestamp = DateUtil.getTimestamp();
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
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
   * Getter sender.
   *
   * @return sender
   */
  public String getSender() {
    return sender;
  }

  /**
   * Getter receiver.
   *
   * @return receiver
   */
  public String getReceiver() {
    return receiver;
  }

  /**
   * Getter amount.
   *
   * @return amount
   */
  public int getAmount() {
    return amount;
  }

  /**
   * Getter signature.
   *
   * @return signature
   */
  public String getSignature() {
    return signature;
  }

  /**
   * Setter signature.
   *
   * @param signature la signature
   */
  public void setSignature(String signature) {
    this.signature = signature;
  }

  /**
   * Permet de calculer le hash de la transaction.
   *
   * @return le hash
   */
  private String calculateHash() {
    String str = String.valueOf(index) + timestamp + sender + receiver + String.valueOf(amount);
    return HashUtil.applySha256(str);
  }

  /**
   * Permet de signer la transaction.
   *
   * @param k la paire de clé utilisée
   */
  public void sign(ECKey k) {
    // Calcul du hash de la transaction
    String hash = calculateHash();
    // Conversion du hash hexadécimal en byte[] de 32 octets
    byte[] hash32bytes = DatatypeConverter.parseHexBinary(hash);
    // Encapsulation de hash32bytes dans un Sha256Hash
    Sha256Hash h = Sha256Hash.wrap(hash32bytes);
    // Signature
    ECDSASignature s = k.sign(h);
    // Conversion de la ECDSASignature en byte[]
    byte[] sig = s.encodeToDER();
    // Conversion en String hexadécimale
    signature = DatatypeConverter.printHexBinary(sig);
  }

  /**
   * Vérifie si la signature numérique est valide.
   *
   * @return true si valide, false sinon
   */
  public boolean checkSignature() {
    // Calcul du hash de la transaction
    String hash = calculateHash();
    // Conversion du hash hexadécimal en byte[] de 32 octets
    byte[] hash32bytes = DatatypeConverter.parseHexBinary(hash);
    // Conversion de la signature hexadécimale en byte[]
    byte[] sig = DatatypeConverter.parseHexBinary(signature);
    // Conversion de l'adresse émetteur (clé publique) hexadécimale en byte[]
    byte[] pubKey = DatatypeConverter.parseHexBinary(sender);

    return ECKey.verify(hash32bytes, sig, pubKey);
  }

  public String toString() {
    return String.valueOf(index) + timestamp + sender + receiver + String.valueOf(amount) + signature;
  }

}
