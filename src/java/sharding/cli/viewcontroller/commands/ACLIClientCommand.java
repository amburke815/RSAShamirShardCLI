package sharding.cli.viewcontroller.commands;

import sharding.Utils;
import sharding.cli.model.IShardingCLIModel;

public abstract class ACLIClientCommand implements ICLIClientCommand {
  protected final IShardingCLIModel shardingModel;

  public ACLIClientCommand(IShardingCLIModel model) {
    this.shardingModel = Utils.notNull(model);
  }
}
