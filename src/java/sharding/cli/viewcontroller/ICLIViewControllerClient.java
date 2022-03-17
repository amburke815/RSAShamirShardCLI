package sharding.cli.viewcontroller;

import sharding.cli.model.IShardingCLIModel;
import sharding.cli.viewcontroller.commands.ICLIClientCommand;

/**
 * A view-controller interface that supports the ability to run a CLI client, execute commands,
 * render output of commands and CLI interaction to the user, and provides read-access for a
 * promised model object.
 * <br>
 * Defines the common operations needed to implement the actual CLI client frontend.
 */
public interface ICLIViewControllerClient {

  /**
   * Runs the Shamir/RSA CLI client until the user chooses to quit the program
   */
  void run();

  /**
   * Renders some output from the CLI program to whatever output stream data is to be written to.
   * One example would be <code>System.out</code>
   *
   * @param toRender the output data to render
   */
  void renderOutput(String toRender);

  /**
   * Returns the <code>IShardingCLIModel</code> that this view-controller is controlling and
   * displaying. Allows other controller and command methods to get data from the model.
   *
   * @return the <code>IShardingCLIModel</code> that this view-controller is controlling and
   * displaying.
   */
  IShardingCLIModel getShardingModel();

}
