package blockchain.utils;

import com.google.gson.Gson;
import blockchain.Blockchain;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class BCJsonUtils {

  public static Blockchain BCJsonReader(String filename) {

    Gson gson = new Gson();

    try (Reader reader = new FileReader(filename)) {

      // Convert JSON to Java Object
      Blockchain bc = gson.fromJson(reader, Blockchain.class);
      return bc;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void BCJsonWriter(Blockchain bc, String filename) {
    // JSON Parser
    // 1. Convert object to JSON string
    Gson gson = new Gson();

    // 2. Convert object to JSON string and save into a file directly
    try (FileWriter writer = new FileWriter(filename)) {
      gson.toJson(bc, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
