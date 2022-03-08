package sharding.shards;

public interface IKeyShard {

  int getIndex();

  void write();

  @Override
  String toString();

  @Override
  int hashCode();

  @Override
  boolean equals(Object o);

}
