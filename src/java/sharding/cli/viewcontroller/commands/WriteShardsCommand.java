package sharding.cli.viewcontroller.commands;

import java.io.File;
import sharding.cli.model.KeyNotShardedException;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class WriteShardsCommand extends ACLIClientCommand {

  public WriteShardsCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    try {
      File savePath = new File("./Keys");
      if (! savePath.exists()) {
        savePath.mkdir();
      }
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
