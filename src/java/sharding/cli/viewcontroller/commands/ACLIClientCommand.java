package sharding.cli.viewcontroller.commands;

import java.util.Scanner;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

/**
 * Abstract class used to aggregate common data among all command function objects including
 * a controller-view field to reflect executions to the user and scanner to process inputs from the user.
 * <br>
 * Note that this common implementation uses <code>System.in</code> to read inputs from the user,
 * but we could just as well have used an <code>Appendable</code> to support reading from other
 * streams such as files.
 */
public abstract class ACLIClientCommand implements ICLIClientCommand {

  protected final ICLIViewControllerClient vcClient;
  protected final Scanner sc = new Scanner(System.in);

  /**
   * trivial ctor
   * @param vcClient the controller-view client that will reflect command executions to the user
   */
  public ACLIClientCommand(ICLIViewControllerClient vcClient) {
    this.vcClient = vcClient;
  }
}
