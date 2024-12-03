import MemberProfile from '@/entity/member/MemberProfile'
import { instanceToPlain, plainToInstance } from 'class-transformer'
import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HtttpRepository'

@singleton()
export default class ProfileRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}
  /**
   * 서버에서 사용자 프로필 정보를 가져옴
   * @returns Promise<MemberProfile>
   */
  public getProfile(): Promise<MemberProfile> {
    return this.httpRepository
      .get<MemberProfile>(
        {
          path: '/api/members/me' // 서버에서 프로필 데이터를 가져올 API 경로
        },
        MemberProfile
      )
      .then((profile) => {
        this.setProfile(profile) // 로컬 스토리지에 프로필 저장
        return profile
      })
  }

  /**
   * 프로필 정보를 로컬 스토리지에 저장
   * @param profile 저장할 MemberProfile 객체
   */
  public setProfile(profile: MemberProfile) {
    const json = instanceToPlain(profile)
    localStorage.setItem('profile', JSON.stringify(json))
    //profile data는 현재 클래스 인스턴스이기 때문에 localStorage에 바로 저장할 수 없음 -> JSON.stringify()를 사용해 문자열로 변환
  }
  /**
   * 로컬 스토리지에서 프로필 정보를 가져옵니다.
   * @returns MemberProfile | null
   */
  public getStoredProfile(): MemberProfile | null {
    const stored = localStorage.getItem('profile')
    if (!stored) return null
    return plainToInstance(MemberProfile, JSON.parse(stored))
  }
  public clear() {
    localStorage.removeItem('profile')
  }
}
