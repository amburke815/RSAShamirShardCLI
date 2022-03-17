package sharding.cli.viewcontroller.commands;

import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Function object that prints a help menu to the console when requested by the user
 */
public class HelpCommand extends ACLIClientCommand {

  public static final String helpMenu = "(1) gen-key.....Generates an RSA key pair of a size (in bits) that is a multiple of 2048\n"
      + "(2) shard-key.....Shards the current private RSA key into n shards using Shamir's Secret Sharing"
      + "Algorithm, requiring k shards to recreate the key\n"
      + "(3) write-all.....writes the public RSA key to ./Keys/Public.TXT and each k'th shard of the private key to ./Keys"
      + "/Shard[k].TXT\n"
      + "(4) write-pub.....writes the public RSA key to ./Keys/Public.TXT\n"
      + "(5) write-shards.....writes all k of n private key shards to ./Keys/Shard[k].TXT\n"
      + "(6) assemble-key.....assemble the private RSA key from a valid number of shamir shards\n"
      + "(7) help/h.....displays this help menu\n"
      + "(8) quit/q.....quits the Sharding CLI\n";
  //+ "(9) encrypt.....enter a message to be encrypted using the generated RSA key\n"
  //+ "(10) decrypt.....enter a ciphertext to be decrypted using the private RSA key reassembled from Shamir shards\n"

  public HelpCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput(helpMenu);
  }
}
