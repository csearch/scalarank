package scalarank.ranker

import scala.reflect.ClassTag
import scalarank.datapoint.{Datapoint, Query, Relevance}

/**
  * An abstract ranker interface
  *
  * @tparam TrainType Type to train on which needs to be at least Datapoint with Relevance
  * @tparam RankType Type to rank with which needs to be at least Datapoint
  */
trait Ranker[TrainType <: Datapoint with Relevance, RankType <: Datapoint] {

  /**
    * Trains the ranker on a set of labeled data points
    *
    * @param data The set of labeled data points
    */
  def train(data: Iterable[Query[TrainType]]): Unit

  /**
    * Scores the given set of query-document pairs
    *
    * @param data The data set
    * @return The scores
    */
  def score(data: IndexedSeq[RankType]): IndexedSeq[Double]

  /**
    * Ranks given set of data points
    *
    * @param data The set of data points
    * @return An ordered list of data points
    */
  def rank[R <: RankType : ClassTag](data: IndexedSeq[R]): IndexedSeq[R] = {
    sort(data, score(data))
  }

  /**
    * Sorts given data using given set of scores
    *
    * @param data The data
    * @param scores The computed scores
    * @return A sorted array of ranks
    */
  protected def sort[R <: RankType : ClassTag](data: IndexedSeq[R], scores: IndexedSeq[Double]): IndexedSeq[R] = {
    data.zip(scores).sortBy(_._2).map(_._1)
  }

}

