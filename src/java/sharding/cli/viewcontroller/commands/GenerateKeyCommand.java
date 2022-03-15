package sharding.cli.viewcontroller.commands;

import sharding.cli.model.IShardingCLIModel;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class GenerateKeyCommand extends ACLIClientCommand {

  public GenerateKeyCommand(IShardingCLIModel model) {
    super(model);
  }

  @Override
  public ICLIViewControllerClient execute() {
    shardingModel.RSAKeyGen();
  }
}
