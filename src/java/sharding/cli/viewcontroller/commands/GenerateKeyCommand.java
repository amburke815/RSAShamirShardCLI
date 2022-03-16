package sharding.cli.viewcontroller.commands;

import java.util.Scanner;
import sharding.cli.model.IShardingCLIModel;
import sharding.cli.model.InsecureRSAKeySizeException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;
import sharding.cli.viewcontroller.CLIViewControllerClientImpl;

/**
 * Function object that calls <code>RSAKeyGen</code> method from the model to generate an RSA key
 * pair that the CLI controller-view can use for encryption, decryption, sharding, etc.
 */
public class GenerateKeyCommand extends ACLIClientCommand {

  public GenerateKeyCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Enter a key size as a multiple of 2048. Press enter to default to a key "
        + "size of 2048\n\n");
    int keySize = 2048;
    if (sc.hasNext()) {
      keySize = Integer.parseInt(sc.next());
    }


    try {
      vcClient.renderOutput("Generating RSA key pair...");
      vcClient.getShardingModel().setRSAKey(vcClient.getShardingModel().RSAKeyGen(keySize));
      vcClient.renderOutput("Generated RSA key pair of size " + keySize + " bits!");
    } catch (InsecureRSAKeySizeException e) {
      vcClient.renderOutput("Insecure/Illegal key size. Must be a multiple of 2048.");
    }
  }
}
