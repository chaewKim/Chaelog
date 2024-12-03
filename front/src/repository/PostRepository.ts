import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HtttpRepository'
import PostWrite from '@/entity/post/PostWrite'
import Post from '@/entity/post/Post'

@singleton()
export default class PostRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}
  // 검색 기능 (페이지네이션 지원)
  public search(keyword: string, page: number, size: number = 3) {
    const params: any = { page, size }
    if (keyword.trim()) {
      params.keyword = keyword
    }
    return this.httpRepository.getList<Post>(
      {
        path: '/api/posts/search',
        params: params
      },
      Post
    )
  }
  public write(request: PostWrite) {
    //PostWrite타입의 변수를 받도록 함
    return this.httpRepository.post({
      path: '/api/posts', //앞에 api -> vue.js 페이지 주소가 아닌 api로 날아가야하는 네트워크 요청임을 알려주기위해
      //vite.config.ts에서 실직적으로 날리기 전에 앞에 붙은 /api는 생략해줌
      body: request
    })
  }

  public get(postId: number) {
    return this.httpRepository.get<Post>(
      {
        path: `/api/posts/${postId}`
      },
      Post
    )
  }

  public getList(page: number) {
    return this.httpRepository.getList<Post>(
      {
        path: `/api/posts?page=${page}&size=3`
      },
      Post
    )
  }
  public delete(postId: number) {
    return this.httpRepository.delete({
      path: `/api/posts/${postId}`
    })
  }

  // update 메서드 추가
  public update(postId: number, request: PostWrite) {
    return this.httpRepository.patch({
      path: `/api/posts/${postId}`,
      body: request
    })
  }
}
