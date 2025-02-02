import AxiosHttpClient, { type HttpRequestConfig } from '@/http/AxiosHttpClient'
import { inject, singleton } from 'tsyringe'
import { plainToInstance } from 'class-transformer'
import Null from '@/entity/data/Null'
import Paging from '@/entity/data/Paging'
@singleton()
export default class HttpRepository {
  constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {}

  public get<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T }): Promise<T> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => plainToInstance(clazz, response))
  }
  public getList<T>(config: HttpRequestConfig, clazz: { new (...args: any[]): T }): Promise<Paging<T>> {
    //Paging에 속해있는 items의 type
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => {
      const paging = plainToInstance<Paging<T>, any>(Paging, response)
      const items = plainToInstance<T, any>(clazz, response.items)
      paging.setItems(items as T[]) //배열로 캐스팅
      return paging
    })
  }

  public post<T extends object | Null>(
    config: HttpRequestConfig,
    clazz: { new (...args: any[]): T } | null = null
  ): Promise<T | Null> {
    return this.httpClient
      .request({ ...config, method: 'POST' })
      .then((response) => plainToInstance(clazz || Null, response as object[])) //null이 아니면 clazz쓰고 null이면 Null
  }
  public patch<T extends object | Null>(
    config: HttpRequestConfig,
    clazz: { new (...args: any[]): T } | null = null
  ): Promise<T | Null> {
    return this.httpClient
      .request({ ...config, method: 'PATCH' })
      .then((response) => plainToInstance(clazz || Null, response as object[]))
  }
  public delete<T extends object | Null>(
    config: HttpRequestConfig,
    clazz: { new (...args: any[]): T } | null = null
  ): Promise<T | Null> {
    return this.httpClient
      .request({ ...config, method: 'DELETE' })
      .then((response) => plainToInstance(clazz !== null ? clazz : Null, response))
  }
}
