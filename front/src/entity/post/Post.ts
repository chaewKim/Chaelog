import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import { Transform } from 'class-transformer'

export default class Post {
  public id = 0
  public title = ''
  public content = ''
  public memberId = 0
  public authorName = '' // String -> string으로 수정

  @Transform(({ value }) =>
    typeof value === 'string' ? LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : LocalDateTime.now()
  )
  public regDate = LocalDateTime.now()
  // @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)) //특정값 서버에서 내려왔을 때 지정한 타입으로 지정
  // public regDate = LocalDateTime.now()
  // public getShortenTitle() {
  //   if (this.title.length > 10) {
  //     return this.title.substring(0, 10) + '...'
  //   }
  //   return this.title
  // }

  public getDisplayRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy년 MM월 dd일 HH시'))
  }
  public getDisplaySimpleRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-dd-HH'))
  }
}
