package sharding.cli.viewcontroller;

import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import sharding.cli.model.IShardingCLIModel;
import sharding.cli.model.ShardingCLIModelImpl;
import sharding.cli.viewcontroller.commands.GenerateKeyCommand;
import sharding.cli.viewcontroller.commands.ICLIClientCommand;
import sharding.cli.viewcontroller.commands.ShardKeyCommand;

public class CLIViewControllerClientImpl implements ICLIViewControllerClient {

  protected IShardingCLIModel shardingModel;
  protected final Map<String, ICLIClientCommand> commandsMap = initCommandsMap();

  // public getter for model field
  @Override
  public IShardingCLIModel getShardingModel() { return shardingModel; }




  /**
   * nullary ctor that sets the model field to a model containing no generated keys or shards
   */
  public CLIViewControllerClientImpl() {
    this.shardingModel = new ShardingCLIModelImpl();
  }

  /**
   * unary ctor that sets the model to a specific model
   *
   * @param shardingModel
   */
  public CLIViewControllerClientImpl(IShardingCLIModel shardingModel) {
    this.shardingModel = shardingModel;
  }

  /**
   * binary ctor that sets the model to a model with a given key and shards map
   *
   * @param keyPair   the keyPair to give the model field
   * @param shardsMap the shard map to give the model field
   */
  public CLIViewControllerClientImpl(KeyPair keyPair, Map<Integer, byte[]> shardsMap) {
    this.shardingModel
        = new ShardingCLIModelImpl(keyPair, shardsMap);
  }

  private final Map<String, ICLIClientCommand> initCommandsMap() {
    Map<String, ICLIClientCommand> commandsMap = new HashMap<>();

    commandsMap.putIfAbsent("gen-key", new GenerateKeyCommand(this));
    commandsMap.putIfAbsent("shard-key", new ShardKeyCommand(this));
    commandsMap.putIfAbsent("write-all", new WriteAllCommand(this));
    commandsMap.putIfAbsent("write-pub", new WritePubCommand(this));
    commandsMap.putIfAbsent("write-shards", new WriteShardsCommand(this));
    //commandsMap.putIfAbsent("reassemble-key", new AssembleKeyCommand());
    //commandsMap.putIfAbsent("encrypt", new EncryptCommand());
    //commandsMap.putIfAbsent("decrypt", new DecryptCommand());

    return commandsMap;

  }

  @Override
  public void run() {
    renderOutput("Welcome");
    Scanner sc = new Scanner(System.in);
    String inp = "";
    while (sc.hasNext()) {
      inp = sc.next();
      if (commandsMap.containsKey(inp)) {
        commandsMap.get(inp).execute();
      }
    }
  }

  @Override
  public void helpMenu() {

  }

  @Override
  public void execute(ICLIClientCommand cmd) {
    if (commandsMap.containsKey(cmd)) {
      commandsMap.get(cmd).execute();
    }
  }

  @Override
  public void renderOutput(String toRender) {
    System.out.println(toRender + "\n\n");
  }


}
