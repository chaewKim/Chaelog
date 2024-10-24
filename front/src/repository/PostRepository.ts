import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HtttpRepository'
import PostWrite from '@/Entity/post/PostWrite'
import { plainToClass, plainToInstance } from 'class-transformer'
import Post from '@/Entity/post/Post'
import Paging from '@/Entity/data/Paging'

@singleton()
export default class PostRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: PostWrite) {
    //PostWrite타입의 변수를 받도록 함
    return this.httpRepository.post({
      path: '/api/posts', //앞에 api -> vue.js 페이지 주소가 아닌 api로 날아가야하는 네트워크 요청임을 알려주기위해
      //vite.config.ts에서 실직적으로 날리기 전에 앞에 붙은 /api는 생략해줌
      body: request
    })
  }

  public get(postId: number): Promise<Post> {
    return this.httpRepository
      .get({
        path: `/api/posts/${postId}`
      })
      .then((response) => {
        return plainToInstance(Post, response)
      })
  }

  public getList(page: number): Promise<Paging<Post>> {
    return this.httpRepository
      .get({
        path: `/api/posts?page=${page}&size=3`
      })
      .then((response) => {
        const paging = plainToInstance<Paging<Post>, any>(Paging, response)
        const items = plainToInstance<Post, any>(Post, response.items)
        paging.setItems(items)
        return paging
      })
  }
}
