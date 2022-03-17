package sharding.cli.viewcontroller.commands;

import java.io.File;
import sharding.cli.model.KeyNotShardedException;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Function object that supports the execution of the <code>ICLIClient.writeShards</code> method,
 * parses input from the user, and gives appropriate feedback to the user.
 */
public class WriteShardsCommand extends ACLIClientCommand {

  public WriteShardsCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    try {

      File savePath = new File("./Keys");
      // Utils.chmod777(savePath);

      vcClient.renderOutput("Writing private RSA key shards to ./Keys/Shard[k].TXT...");
      vcClient.getShardingModel().writeShards();
      vcClient.renderOutput("Successfully wrote private RSA key shards to ./Keys/Shard[k].TXT !");
    } catch (NoSuchKeyException e) {
      vcClient.renderOutput("No key pair generated yet. Use 'gen-key' to generate a key pair");
    } catch (KeyNotShardedException e) {
      vcClient.renderOutput("Key not sharded yet. Use 'shard-key' to shard the private RSA key");
    }
  }
}
