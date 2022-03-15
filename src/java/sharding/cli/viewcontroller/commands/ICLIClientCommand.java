package sharding.cli.viewcontroller.commands;

import sharding.cli.viewcontroller.ICLIViewControllerClient;

public interface ICLIClientCommand {
  ICLIViewControllerClient execute();
}
