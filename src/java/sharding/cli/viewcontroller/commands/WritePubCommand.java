package sharding.cli.viewcontroller.commands;

import java.io.File;
import java.nio.file.Path;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class WritePubCommand extends ACLIClientCommand {

  public WritePubCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    try {
      File savePath = new File("./Keys");
      if (! savePath.exists()) {
        savePath.mkdir();
      }
      vcClient.renderOutput("Writing public RSA key to ./Keys/Public.TXT...");
      vcClient.getShardingModel().writePublicKey();
      vcClient.renderOutput("Successfully wrote public key to ./Keys/Public.TXT !");
    } catch (NoSuchKeyException e) {
      vcClient.renderOutput("No key pair generated yet. Use 'gen-key' to generate a key pair");
    }
  }
}
