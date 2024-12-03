<script setup lang="ts">
import { defineProps, onMounted, reactive, ref } from 'vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import Post from '@/entity/post/Post'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import CommentsComponents from '@/components/CommentsComponents.vue'
import ProfileRepository from '@/repository/ProfileRepository'
import MemberProfile from '@/entity/member/MemberProfile'

const props = defineProps<{
  postId: number
}>()

const POST_REPOSITORY = container.resolve(PostRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

type StateType = {
  post: Post
  profile: MemberProfile | null
}
const state = reactive<StateType>({
  post: new Post(),
  profile: null
})

function getPost() {
  POST_REPOSITORY.get(props.postId)
    .then((post: Post) => {
      //게시글 하나 조회
      state.post = post //서버에서 가져온 데이터
    })
    .catch((e) => {
      console.error(e)
    })
}

const router = useRouter()

onMounted(() => {
  getPost() //게시글 데이터 가져오기
  PROFILE_REPOSITORY.getProfile().then((profile) => {
    state.profile = profile
  })
})
//게시글 삭제
function remove() {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', 'Warning', {
    title: '삭제',
    confirmButtonText: '삭제',
    cancelButtonText: '취소',
    type: 'warning'
  }).then(() => {
    POST_REPOSITORY.delete(props.postId).then(() => {
      ElMessage({ type: 'success', message: '성공적으로 삭제되었습니다.' })
      router.back()
    })
  })
}
// 게시글 수정 페이지로 이동
function edit() {
  router.push({ name: 'edit', params: { postId: props.postId } })
}
</script>
<template>
  <el-row>
    <el-col :span="22" :offset="1">
      <div class="title">{{ state.post.title }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="10" :offset="7">
      <div class="title">
        <div class="regDate">Posted on {{ state.post.getDisplayRegDate() }}</div>
      </div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="content">
        {{ state.post.content }}
      </div>

      <div class="footer">
        <div v-if="state.post.authorName === state.profile?.name" class="edit" @click="edit()">수정</div>
        <div v-if="state.post.authorName === state.profile?.name" class="delete" @click="remove()">삭제</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="comments">
    <el-col>
      <CommentsComponents :postId="props.postId" />
    </el-col>
  </el-row>
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
hr {
  border-color: #f9f9f9;
  margin: 1.2rem 0;
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
