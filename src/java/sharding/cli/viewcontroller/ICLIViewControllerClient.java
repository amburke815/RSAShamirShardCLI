package sharding.cli.viewcontroller;

import sharding.cli.viewcontroller.commands.ICLIClientCommand;

public interface ICLIViewControllerClient {

  void helpMenu();

  void execute(ICLIClientCommand cmd);
}
