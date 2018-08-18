package utils

object DynamicSortBySupport {

  import slick.ast.Ordering
  import slick.ast.Ordering.Direction
  import slick.lifted.{ColumnOrdered, Ordered, Query, Rep}

  type ColumnOrdering = (String, Direction) // Just a type alias
  trait ColumnSelector {
    val select: Map[String, Rep[_]] // The runtime map between string names and table columns
  }

  implicit class MultiSortableQuery[A <: ColumnSelector, B, C[_]](query: Query[A, B, C]) {
    def dynamicSortBy(sortBy: Seq[ColumnOrdering]): Query[A, B, C] =
      sortBy.foldRight(query) { // Fold right is reversing order
        case ((sortColumn, sortOrder), queryToSort) =>
          val sortOrderRep: Rep[_] => Ordered = ColumnOrdered(_, Ordering(sortOrder))
          val sortColumnRep: A => Rep[_] = _.select(sortColumn)
          queryToSort.sortBy(sortColumnRep)(sortOrderRep)
      }
  }

}