package sharding.cli.viewcontroller.commands;

import java.io.File;
import sharding.cli.model.NoSuchKeyException;

public class WriteShardsCommand extends ACLIClientCommand {

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
    }
  }
}
