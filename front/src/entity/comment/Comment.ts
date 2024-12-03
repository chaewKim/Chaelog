import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import { Transform } from 'class-transformer'

export default class Comment {
  public id = 0
  public author = ''
  public password = ''
  public content = ''

  @Transform(({ value }) =>
    typeof value === 'string' ? LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : value
  )
  public regDate = LocalDateTime.now()

  public getDisplaySimpleRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd HH:mm'))
  }
}
