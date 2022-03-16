package sharding.cli.viewcontroller.commands;

import sharding.cli.model.IShardingCLIModel;
import sharding.cli.model.IllegalShamirShardingParametersException;
import sharding.cli.model.NoSuchKeyException;
import sharding.cli.viewcontroller.CLIViewControllerClientImpl;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class ShardKeyCommand extends ACLIClientCommand {

  public ShardKeyCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Please enter a positive integer n representing the number of "
        + "total Shamir shards");
    int n = 0;
    if (sc.hasNext()) {
      try {
        n = Integer.parseInt(sc.next());
      } catch (NumberFormatException e) {
        vcClient.renderOutput("invalid shard number n. Please enter a positive integer");
      }
    }
    vcClient.renderOutput("Now enter a positive integer k <= n representing the minimum"
        + " number of Shamir shards required to recreate the private key");
    int k = 0;
    if (sc.hasNext()) {
      try {
        k = Integer.parseInt(sc.next());
      } catch (NumberFormatException e) {
        vcClient.renderOutput("invalid shard number k. Please enter a positive integer");
      }
    }
    try {
      vcClient.renderOutput("Sharding the private key into " + n + " total shards of which "
          + k + " are required to recreate the key...");
      vcClient.getShardingModel().setShardsMap(vcClient.getShardingModel().shamirShardKey(n, k));
      vcClient.renderOutput("Successfully used Shamir's Secret Sharing Algorithm to break "
          + "the private key into " + n + " total shards of which " + k + " are required to recreate "
          + "the key!");
    } catch (NoSuchKeyException e) {
      vcClient.renderOutput("No key generated yet. Please use 'gen-key' to generate a key pair");
    } catch (IllegalShamirShardingParametersException e) {
      vcClient.renderOutput("Bad Shamir Sharding paramters n and k. Make sure that n > 0 and k <= n");
    }

  }
}
