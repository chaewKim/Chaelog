import { AxiosError } from 'axios'

export default class HttpError {
  private readonly code: string
  private readonly message: string
  private readonly validation: Record<string, string>

  //응답 정보가 있을 경우 그 내용 반영, 없을 경우 기본 값
  constructor(e: AxiosError) {
    this.code = e.response?.data.code ?? '500'
    this.message = e.response?.data.message ?? '네트워크 상태가 좋지 않습니다.'
    this.validation = e.response?.data.validation || {}
  }

  public getMessage() {
    return this.message
  }
  public getValidationErrors() {
    return this.validation
  }
}
