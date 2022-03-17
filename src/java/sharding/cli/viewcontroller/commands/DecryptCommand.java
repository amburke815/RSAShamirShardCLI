package sharding.cli.viewcontroller.commands;

import java.nio.charset.StandardCharsets;
import sharding.cli.model.NoReassembledKeyException;
import sharding.cli.model.ShardingCLIModelImpl;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

@Deprecated
/**
 * Function object that supports the execution of the <code>IShardingCLIModel.decrypt</code> method,
 * parses input from the user, and gives appropriate feedback to the user.
 *
 * In the future I'd like to add support for encrypting and decrypting strings. All of the
 * architecture for doing this is already present in this project, but because of the padding
 * I use, I can only encrypt and decrypt very small strings. If I were to implement this feature,
 * I would use a symmetric AES key to encrypt and decrypt strings and use the preexistent asymmetric
 * RSA key to encrypt that AES key in order to strike a balance between efficiency and security.
 *
 * Because the current implementation can only encrypt and decrypt very short strings, I have marked
 * these classes that would expose that functionality to the client as depracated
 */
public class DecryptCommand extends ACLIClientCommand {

  public DecryptCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Enter a message to decrypt:");
    String inp = "";
    if (sc.hasNextLine()) {
      inp = sc.nextLine();
    }
    try {
      vcClient.renderOutput("Decrypted message with private key reassembled from Shamir shards...");
      vcClient.renderOutput("DECRYPTED MESSAGE:");
      vcClient
          .renderOutput(vcClient.getShardingModel().decrypt(inp.getBytes(StandardCharsets.UTF_8)));
    } catch (NoReassembledKeyException e) {
      vcClient.renderOutput("Cannot decrypt. You have not assembled valid Shamir shares to "
          + "recover the private key yet. Use shard-key to create shards and reassemble-key to "
          + "reassemble the private key.");
      System.out.println(
          "shardsmap: " + (((ShardingCLIModelImpl) vcClient.getShardingModel()).getShardsMap()
              == null));
      System.out.println("reass: " + (
          ((ShardingCLIModelImpl) vcClient.getShardingModel()).getReassembledPrivateKey() == null));
    }
  }
}
