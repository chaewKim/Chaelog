<script setup lang="ts">
import { defineProps, onMounted, reactive, ref } from 'vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import Post from '@/Entity/post/Post'
import { useRouter } from 'vue-router'

const props = defineProps<{
  postId: number
}>()

const POST_REPOSITORY = container.resolve(PostRepository)

type StateType = {
  post: Post
}
const state = reactive<StateType>({
  post: new Post()
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
  getPost()
})
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
        <div class="edit">수정</div>
        <div class="delete">삭제</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="comments">
    <el-col>
      <Comments />
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
