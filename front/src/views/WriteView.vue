<script setup lang="ts">
import { reactive } from 'vue'
import PostWrite from '@/entity/post/PostWrite'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import { ElMessage } from 'element-plus'
import HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'

const state = reactive({
  postWrite: new PostWrite() //초기값은 빈 객체
})
const router = useRouter()
const POST_REPOSITORY = container.resolve(PostRepository) //의존성 주입
const validationErrors: { [key: string]: string } = {}

function write() {
  POST_REPOSITORY.write(state.postWrite)
    .then(() => {
      //글 등록 완료
      ElMessage({ type: 'success', message: '글 등록이 완료되었습니다. :) ' })
      router.replace('/')
    })
    .catch((e) => {
      const validationErrors = e.getValidationErrors()
      if (Object.keys(validationErrors).length > 0) {
        Object.keys(validationErrors).forEach((field) => {
          ElMessage({
            type: 'error',
            message: validationErrors[field]
          })
        })
      } else {
        ElMessage({
          type: 'error',
          message: e.getMessage()
        })
      }
    })
}
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input v-model="state.postWrite.title" size="large" placeholder="제목을 입력해주세요" />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="state.postWrite.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="write()">등록완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style></style>
