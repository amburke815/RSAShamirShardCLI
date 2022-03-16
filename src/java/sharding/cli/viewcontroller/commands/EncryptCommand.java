package sharding.cli.viewcontroller.commands;

import java.nio.charset.StandardCharsets;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class EncryptCommand extends ACLIClientCommand {

  public EncryptCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    try {
      vcClient.renderOutput("Please enter a message to encrypt:");
      String msg = "";
      if(sc.hasNextLine()) {
        msg = sc.nextLine();
      }
      byte[] encryptedMessageBytes = vcClient.getShardingModel().encrypt(msg);
      String encryptedMessage = new String(encryptedMessageBytes, StandardCharsets.UTF_8);

      vcClient.renderOutput("Successfully encrypted the message!\n\nENCRYPTED MESSAGE:");
      vcClient.renderOutput(encryptedMessage);
    } catch (NoSuchKeyException e) {
      vcClient.renderOutput("No key generated yet. Use 'gen-key' to generate the public "
          + "RSA key for encryption");
    } catch (IllegalStateException e) {
      vcClient.renderOutput("Message is too long. Please encrypt a message of length less than 245 bytes");
    }
  }
}
