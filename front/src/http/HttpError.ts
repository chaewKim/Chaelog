import { AxiosError } from 'axios'

export default class HttpError {
  private readonly code: string
  private readonly message: string

  constructor(e: AxiosError) {
    this.code = e.response?.data.code ?? '500'
    this.message = e.response?.data.message ?? '네트워크 상태가 좋지 않습니다.'
  }

  public getMessage() {
    return this.message
  }
}
