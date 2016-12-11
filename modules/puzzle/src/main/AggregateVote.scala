package lila.puzzle

case class AggregateVote(up: Int, down: Int, nb: Int, ratio: Int) {

  def add(v: Boolean) = copy(
    up = up + v.fold(1, 0),
    down = down + v.fold(0, 1)
  ).computeNbAndRatio

  def change(from: Boolean, to: Boolean) = if (from == to) this else copy(
    up = up + to.fold(1, -1),
    down = down + to.fold(-1, 1)
  ).computeNbAndRatio

  def count = up + down

  def sum = up - down

  def computeNbAndRatio = copy(
    ratio = 100*(up - down)/(up + down),
    nb = up + down)
}

object AggregateVote {

  val default = AggregateVote(1, 0, 1, 100)
  val disable = AggregateVote(0, 9000, 9000, -100).computeNbAndRatio

  val minRatio = -50
  val minVotes = 30

  import reactivemongo.bson.Macros
  implicit val aggregatevoteBSONHandler = Macros.handler[AggregateVote]
}
