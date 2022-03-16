package sharding.cli.viewcontroller.commands;

import java.util.Scanner;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public abstract class ACLIClientCommand implements ICLIClientCommand {

  protected final ICLIViewControllerClient vcClient;
  protected final Scanner sc = new Scanner(System.in);

  public ACLIClientCommand(ICLIViewControllerClient vcClient) {
    this.vcClient = vcClient;
  }
}
