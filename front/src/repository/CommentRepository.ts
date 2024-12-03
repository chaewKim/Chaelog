import HttpRepository from '@/repository/HtttpRepository'
import { inject, singleton } from 'tsyringe'
import CommentWrite from '@/entity/comment/CommentWrite'
import Comment from '@/entity/comment/Comment'
import Paging from '@/entity/data/Paging'
import CommentDeleteRequest from '@/entity/comment/CommentDeleteRequest'

@singleton()
export default class CommentRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  //댓글 생성
  public write(postId: number, request: CommentWrite): Promise<Comment> {
    // @ts-ignore
    return this.httpRepository.post(
      {
        path: `/api/posts/${postId}/comments`,
        body: request
      },
      Comment
    )
  }

  // 댓글 리스트 조회
  public getList(postId: number): Promise<Paging<Comment>> {
    return this.httpRepository.getList(
      {
        path: `/api/posts/${postId}/comments`
      },
      Comment
    )
  }

  // 댓글 삭제
  public delete(request: CommentDeleteRequest): Promise<void> {
    // @ts-ignore
    return this.httpRepository.delete({
      path: `/api/comments`,
      body: request
    })
  }
}
