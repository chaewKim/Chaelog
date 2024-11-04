<script setup lang="ts">
import { defineProps, onMounted, reactive } from 'vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import PostWrite from '@/Entity/post/PostWrite'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const props = defineProps<{
  postId: number
}>()

const POST_REPOSITORY = container.resolve(PostRepository)

const state = reactive({
  postWrite: new PostWrite()
})

const router = useRouter()

// 기존 게시글 불러오기
function getPost() {
  POST_REPOSITORY.get(props.postId)
    .then((post) => {
      state.postWrite.title = post.title
      state.postWrite.content = post.content
    })
    .catch((e) => {
      console.error(e)
    })
}

// 게시글 업데이트 요청
function updatePost() {
  POST_REPOSITORY.update(props.postId, state.postWrite)
    .then(() => {
      ElMessage({ type: 'success', message: '게시글이 수정되었습니다.' })
      router.replace(`/post/${props.postId}`)
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
        ElMessage({ type: 'error', message: e.getMessage() })
      }
    })
}

// 초기화 시 기존 게시글 가져오기
onMounted(() => {
  getPost()
})
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input v-model="state.postWrite.title" size="large" placeholder="제목을 입력해주세요" />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="state.postWrite.content" type="textarea" rows="15" placeholder="내용을 입력해주세요" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="updatePost()">수정 완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.8rem;
  font-weight: 400;
  text-align: center;
}

.regDate {
  margin-top: 0.5rem;
  font-size: 0.78rem;
  font-weight: 300;
}

.content {
  margin-top: 1.88rem;
  font-weight: 300;
  word-break: break-all;
  white-space: break-spaces;
  line-height: 1.4;
  min-height: 5rem;
}

.footer {
  margin-top: 1rem;
  display: flex;
  font-size: 0.78rem;
  justify-content: flex-end;
  gap: 0.8rem;

  .delete {
    color: red;
  }
}

.comments {
  margin-top: 4.8rem;
}
</style>
