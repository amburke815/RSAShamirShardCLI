package sharding.cli.viewcontroller;

import java.security.KeyPair;
import java.util.Map;
import sharding.cli.model.IShardingCLIModel;
import sharding.cli.viewcontroller.commands.ICLIClientCommand;

public interface ICLIViewControllerClient {

  void run();

  void execute(ICLIClientCommand cmd);

  void renderOutput(String toRender);

  IShardingCLIModel getShardingModel();

}
