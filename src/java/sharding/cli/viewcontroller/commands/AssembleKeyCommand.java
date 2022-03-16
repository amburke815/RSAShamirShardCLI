package sharding.cli.viewcontroller.commands;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class AssembleKeyCommand extends ACLIClientCommand {

  public AssembleKeyCommand(ICLIViewControllerClient vcClient) {
    super(vcClient);
  }

  @Override
  public void execute() {
    vcClient.renderOutput("Enter the 1-based indices of the shamir shards to assemble the"
        + " private RSA key with, separated by spaces. \nFor example, to assemble the key with "
        + "shards 2, 5, 7, and 12, enter '2 5 7 12'");
    List<Integer> enteredShardIndices = new ArrayList<>();
    String inp = "";
    if (sc.hasNextLine()) {
      inp = sc.nextLine();
      for(String s : inp.split(" ")) {
        try {
          enteredShardIndices.add(Integer.parseInt(s));
        } catch (NumberFormatException e) {
          vcClient.renderOutput("skipped entry " + s + " not a valid shard index");
        }
      }
    }

    try {
      vcClient.renderOutput("assembling the RSA private key from shards " +
          arrayListToArray(enteredShardIndices).toString());
      vcClient.getShardingModel().assembleShards(arrayListToArray(enteredShardIndices));
      vcClient.renderOutput("Successfully assembled private RSA key!");
    } catch (IllegalStateException e) {
      vcClient.renderOutput("Could not assemble RSA key from given shards. Try again.");
    }
  }

  private static int[] arrayListToArray(List<Integer> toConvert) {
    int[] arrayed = new int[toConvert.size()];
    for (int i = 0; i < toConvert.size(); i++) {
      arrayed[i] = toConvert.get(i);
    }

    return arrayed;
  }
}
