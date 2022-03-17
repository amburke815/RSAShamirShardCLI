package sharding.cli.viewcontroller.commands;

import java.nio.charset.StandardCharsets;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

@Deprecated
/**
 * Function object that supports the execution of the <code>IShardingCLIModel.encrypt</code> method,
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
