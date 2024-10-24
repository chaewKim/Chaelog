export default class Paging<T> {
  public page: number = 0
  public size: number = 0
  public totalCount: number = 0
  public items: T[] = []

  public setItems(items: T[]) {
    this.items = items
  }
}
