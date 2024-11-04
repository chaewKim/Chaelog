<script setup lang="ts">
import { reactive } from 'vue'
import Login from '@/Entity/member/Login'
import { ElMessage } from 'element-plus'
import HttpError from '@/http/HttpError'
import MemberRepository from '@/repository/MemberRepository'
import { container } from 'tsyringe'
import router from '@/router'

const state = reactive({
  login: new Login()
})
const MEMBER_REPOSITORY = container.resolve(MemberRepository)
function doLogin() {
  // const HttpClient = new AxiosHttpClient()
  MEMBER_REPOSITORY.login(state.login)
    .then(() => {
      ElMessage({ type: 'success', message: '환영합니다. :) ' })
      location.href = '/'
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}
</script>

<template>
  <el-row>
    <el-col :span="10" :offset="7">
      <el-form label-position="top">
        <el-form-item label="이메일">
          <el-input v-model="state.login.email"></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input type="password" v-model="state.login.password"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doLogin()">로그인</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>
