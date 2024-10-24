import axios, { AxiosError, type AxiosInstance, type AxiosResponse } from 'axios'
import HttpError from '@/http/HttpError'
import { injectable, singleton } from 'tsyringe'

export type HttpRequestConfig = {
  method?: 'GET' | 'POST' | 'PATCH' | 'DELETE' //default : GET
  path: string
  params?: any //URL에 쿼리 파라미터로 추가될 데이터 (?id=1)
  body?: any
}
//API와의 모든 통신 담당, 요청 보낸 후 응답 데이터 반환, 에러 발생시 처리
@singleton()
export default class AxiosHttpClient {
  private readonly client: AxiosInstance = axios.create({
    //axios 인스턴스 생성
    timeout: 3000,
    timeoutErrorMessage: 'TimeOut'
  })

  //API호출을 처리
  public async request(config: HttpRequestConfig) {
    return this.client
      .request({
        method: config.method,
        url: config.path,
        params: config.params,
        data: config.body
      })
      .then((response: AxiosResponse) => {
        return response.data
      })
      .catch((e: AxiosError) => {
        return Promise.reject(new HttpError(e))
      })
  }
}
