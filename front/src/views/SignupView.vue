<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { container } from 'tsyringe'
import MemberRepository from '@/repository/MemberRepository'
import HttpError from '@/http/HttpError'

const state = reactive({
  email: '',
  name: '',
  password: ''
})

const MEMBER_REPOSITORY = container.resolve(MemberRepository)

function doSignup() {
  const signupRequest = {
    email: state.email,
    name: state.name,
    password: state.password
  }

  MEMBER_REPOSITORY.signup(signupRequest)
    .then(() => {
      ElMessage({ type: 'success', message: '회원가입에 성공했습니다!' })
      setTimeout(() => {
        location.href = '/' // 로그인 페이지로 리디렉션
      }, 1000)
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
          <el-input v-model="state.email" type="email" placeholder="이메일을 입력하세요."></el-input>
        </el-form-item>

        <el-form-item label="이름">
          <el-input v-model="state.name" placeholder="이름을 입력하세요."></el-input>
        </el-form-item>

        <el-form-item label="비밀번호">
          <el-input v-model="state.password" type="password" placeholder="비밀번호를 입력하세요."></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="doSignup()">회원가입</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss"></style>
