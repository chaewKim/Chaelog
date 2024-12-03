import { AxiosError } from 'axios'

export default class HttpError {
  private readonly code: string
  private readonly message: string
  private readonly validation: object

  //응답 정보가 있을 경우 그 내용 반영, 없을 경우 기본 값
  constructor(e: AxiosError) {
    const data =
      (e.response?.data as {
        code?: string
        message?: string
        validation?: object
      }) || {}
    this.code = data.code ?? '500'
    this.message = data.message ?? '네트워크 상태가 좋지 않습니다.'
    this.validation = data.validation || {}
  }

  public getMessage() {
    return this.message
  }
  public getValidationErrors() {
    return this.validation
  }
}
