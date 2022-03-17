package sharding.cli.viewcontroller.commands;

import java.io.File;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Function object that supports the execution of the <code>ICLIClient.writePublic</code> method,
 * parses input from the user, and gives appropriate feedback to the user.
 * Writes public key to ./Keys directory
 */
public class WritePubCommand extends ACLIClientCommand {

  public WritePubCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    try {
      File savePath = new File("./Keys");
      // Utils.chmod777(savePath);
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
