package sharding.cli.viewcontroller.commands;

import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Function object that exits the CLI program.
 */
public class QuitCommand extends ACLIClientCommand {

  public QuitCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Quitting Sharding CLI...");
    System.exit(1);
  }
}
