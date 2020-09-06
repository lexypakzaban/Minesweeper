// the "enumerator" is a type that has a certain number of public constants.
// thus, a variable of type MineStatus can be any one of MineStatus.ORIGINAL or
// MineStatus.FLAGGED or MineStatus.NUMBER_REVEALED, etc.
public enum MineStatus { ORIGINAL,FLAGGED,NUMBER_REVEALED,BOMB_REVEALED,EXPLODED};
