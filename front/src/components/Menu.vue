<script setup lang="ts">
import { onBeforeMount, reactive } from 'vue'
import { container } from 'tsyringe'
import MemberRepository from '@/repository/MemberRepository.js'
import ProfileRepository from '@/repository/ProfileRepository'
import MemberProfile from '@/entity/member/MemberProfile'
import { ElMessage } from 'element-plus'
const MEMBER_REPOSITORY = container.resolve(MemberRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

type StateType = {
  profile: MemberProfile | null
}
const state = reactive<StateType>({
  profile: null
})

onBeforeMount(() => {
  MEMBER_REPOSITORY.getProfile().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile) //저장
    state.profile = profile
  })
})
function logout() {
  PROFILE_REPOSITORY.clear()
  // location.href = '/api/auth/logout'
  fetch('/api/auth/logout', {
    method: 'POST',
    credentials: 'include' // include cookies
  }).then((response) => {
    if (response.ok) {
      ElMessage.success('로그아웃이 되었습니다.')
      setTimeout(() => {
        location.href = '/'
      }, 1000) //1초 후에 페이지 리디렉션
    } else {
      ElMessage.error('로그아웃에 실패하였습니다.')
    }
  })
}
</script>
<template>
  <ul class="menus">
    <li class="menu">
      <router-link to="/"> 처음으로 </router-link>
    </li>
    <li class="menu" v-if="state.profile === null">
      <router-link to="/signup"> 회원가입 </router-link>
      <!-- 회원가입 링크 추가 -->
    </li>
    <li class="menu" v-if="state.profile !== null">
      <router-link to="/write"> 글 작성 </router-link>
    </li>
    <li class="menu" v-if="state.profile === null">
      <router-link to="/login"> 로그인 </router-link>
    </li>

    <li class="menu" v-else>
      <a href="#" @click="logout()" to="/login">({{ state.profile.name }}) 로그아웃 </a>
    </li>
  </ul>
</template>
<style scoped lang="scss">
.menus {
  height: 20px;
  list-style: none;
  padding: 0;
  font-size: 0.88rem;
  font-weight: 300;
  text-align: center;
  margin: 0;
}

.menu {
  display: inline;
  margin-right: 1rem;

  &:last-child {
    margin-right: 0;
  }
  a {
    color: inherit;
  }
}
</style>
