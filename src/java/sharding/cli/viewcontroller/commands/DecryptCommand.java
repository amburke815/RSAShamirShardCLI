package sharding.cli.viewcontroller.commands;

import java.nio.charset.StandardCharsets;
import sharding.cli.model.NoReassembledKeyException;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

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
    }
  }
}
