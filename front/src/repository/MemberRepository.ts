import HttpRepository from '@/repository/HtttpRepository'
import Login from '@/entity/member/Login'
import { inject, singleton } from 'tsyringe'
import MemberProfile from '@/entity/member/MemberProfile'

@singleton()
export default class MemberRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}
  public signup(request: { email: string; name: string; password: string }) {
    return this.httpRepository.post({
      path: '/api/auth/signup',
      body: request
    })
  }
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
