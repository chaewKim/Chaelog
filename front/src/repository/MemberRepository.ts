import HttpRepository from '@/repository/HtttpRepository'
import Login from '@/Entity/member/Login'
import { inject, singleton } from 'tsyringe'
import MemberProfile from '@/Entity/member/MemberProfile'

@singleton()
export default class MemberRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}
  public login(request: Login) {
    return this.httpRepository.post({
      path: '/api/auth/login',
      body: request
    })
  }

  public getProfile() {
    return this.httpRepository.get<MemberProfile>(
      {
        path: '/api/members/me'
      },
      MemberProfile
    )
  }
}
