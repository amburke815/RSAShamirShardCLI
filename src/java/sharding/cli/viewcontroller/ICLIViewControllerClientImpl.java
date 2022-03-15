package sharding.cli.viewcontroller;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import sharding.cli.model.IShardingCLIModel;
import sharding.cli.model.ShardingCLIModelImpl;
import sharding.cli.viewcontroller.commands.GenerateKeyCommand;
import sharding.cli.viewcontroller.commands.ICLIClientCommand;

public class ICLIViewControllerClientImpl implements ICLIViewControllerClient {
  private final IShardingCLIModel shardingModel = new ShardingCLIModelImpl();
  private final Map<String, ICLIClientCommand> commandsMap = initCommandsMap();
  // TODO: see how to track key pair in model

  private final Map<String, ICLIClientCommand> initCommandsMap() {
    Map<String, ICLIClientCommand> commandsMap = new HashMap<>();

    commandsMap.putIfAbsent("gen-key", new GenerateKeyCommand(shardingModel));
    commandsMap.putIfAbsent("shard-key", new ShardKeyCommand(shardingModel));
    commandsMap.putIfAbsent("reassemble-key", new AssembleKeyCommand(shardingModel));
    commandsMap.putIfAbsent("encrypt", new EncryptCommand(shardingModel));
    commandsMap.putIfAbsent("decrypt", new DecryptCommand(shardingModel));

    return commandsMap;

  }

  @Override
  public void helpMenu() {

  }

  @Override
  public void execute(ICLIClientCommand cmd) {

  }
}
