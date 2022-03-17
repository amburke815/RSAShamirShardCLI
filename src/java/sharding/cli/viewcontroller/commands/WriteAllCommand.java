package sharding.cli.viewcontroller.commands;

import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Function object that supports the execution of the <code>IShardingCLIModel.writePublic</code> and
 * <code>IShardingCLIModel.writeShards</code> methods,
 * parses input from the user, and gives appropriate feedback to the user.
 */
public class WriteAllCommand extends ACLIClientCommand {

  public WriteAllCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Writing all private key shards and public key to ./Keys directory...");
    new WritePubCommand(vcClient).execute();
    new WriteShardsCommand(vcClient).execute();
  }
}
