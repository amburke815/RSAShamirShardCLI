package sharding.cli.viewcontroller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import sharding.cli.model.IShardingCLIModel;
import sharding.cli.model.ShardingCLIModelImpl;
import sharding.cli.viewcontroller.commands.AssembleKeyCommand;
import sharding.cli.viewcontroller.commands.GenerateKeyCommand;
import sharding.cli.viewcontroller.commands.HelpCommand;
import sharding.cli.viewcontroller.commands.ICLIClientCommand;
import sharding.cli.viewcontroller.commands.QuitCommand;
import sharding.cli.viewcontroller.commands.ShardKeyCommand;
import sharding.cli.viewcontroller.commands.WriteAllCommand;
import sharding.cli.viewcontroller.commands.WritePubCommand;
import sharding.cli.viewcontroller.commands.WriteShardsCommand;

/**
 * A View-Controller implementation that uses a model delegate object to call
 * and support model methods via model method function objects (see <code>ICLIClientCommand</code>)
 * and the command design pattern. Supports the ability to parse and handle input from the user, as
 * well as printing messages to the console for the user to read.
 */
public class CLIViewControllerClientImpl implements ICLIViewControllerClient {

  protected final IShardingCLIModel shardingModel;
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

  private final Map<String, ICLIClientCommand> initCommandsMap() {
    Map<String, ICLIClientCommand> commandsMap = new HashMap<>();

    commandsMap.putIfAbsent("gen-key", new GenerateKeyCommand(this));
    commandsMap.putIfAbsent("shard-key", new ShardKeyCommand(this));
    commandsMap.putIfAbsent("write-all", new WriteAllCommand(this));
    commandsMap.putIfAbsent("write-pub", new WritePubCommand(this));
    commandsMap.putIfAbsent("write-shards", new WriteShardsCommand(this));
    commandsMap.putIfAbsent("assemble-key", new AssembleKeyCommand(this));
    // commandsMap.putIfAbsent("encrypt", new EncryptCommand(this));
    // commandsMap.putIfAbsent("decrypt", new DecryptCommand(this));
    commandsMap.putIfAbsent("help", new HelpCommand(this));
    commandsMap.putIfAbsent("h", new HelpCommand(this));
    commandsMap.putIfAbsent("q", new QuitCommand(this));
    commandsMap.putIfAbsent("quit", new QuitCommand(this));

    return commandsMap;

  }

  @Override
  public void run() {
    renderOutput("Welcome to the RSA/Shamir Sharding CLI utility! \n" + HelpCommand.helpMenu);
    Scanner sc = new Scanner(System.in);
    String inp = "";
    while (sc.hasNext()) {
      inp = sc.next();
      if (commandsMap.containsKey(inp)) {
        commandsMap.get(inp).execute();
      } else {
        renderOutput("Unrecognized command. Enter 'h/help' to show help menu");
      }
    }
  }

  @Override
  public void renderOutput(String toRender) {
    System.out.println(toRender + "\n");
  }


}
