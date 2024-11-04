import MemberProfile from '@/Entity/member/MemberProfile'
import { instanceToPlain } from 'class-transformer'
import { singleton } from 'tsyringe'

@singleton()
export default class ProfileRepository {
  public setProfile(profile: MemberProfile) {
    const json = instanceToPlain(profile)
    localStorage.setItem('profile', JSON.stringify(json))
    //profile data는 현재 클래스 인스턴스이기 때문에 localStorage에 바로 저장할 수 없음 -> JSON.stringify()를 사용해 문자열로 변환
  }

  public clear() {
    localStorage.clear()
  }
}
