package sharding.cli.viewcontroller.commands;

import sharding.cli.viewcontroller.ICLIViewControllerClient;

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
