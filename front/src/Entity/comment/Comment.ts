import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import { Transform } from 'class-transformer'

export default class Comment {
  public id = 0
  public author = ''
  public password = ''
  public content = ''

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)) //특정값 서버에서 내려왔을 때 지정한 타입으로 지정
  public regDate = LocalDateTime.now()

  public getDisplaySimpleRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-dd-HH'))
  }
}
