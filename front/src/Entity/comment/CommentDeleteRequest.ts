export default class CommentDeleteRequest {
  public id = 0
  public password = ''

  constructor(id: number, password: string) {
    this.id = id
    this.password = password
  }
}
