package sharding.cli.viewcontroller.commands;


/**
 * Function object interface that helps to implement the command design pattern in
 * <code>CLIViewControllerImpl</code>. Each derived class has access to the <code>execute/code> method
 * that is intended to execute the function of that derived class.
 */
public interface ICLIClientCommand {
  void execute();
}
