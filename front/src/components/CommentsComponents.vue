<script setup lang="ts">
//댓글 리스트와 댓글 작성 기능
import CommentComponents from '@/components/CommentComponents.vue'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import { onMounted, reactive } from 'vue'
import CommentWrite from '@/entity/comment/CommentWrite'
import { ElMessage, ElMessageBox } from 'element-plus'
import CommentDeleteRequest from '@/entity/comment/CommentDeleteRequest'
import HttpError from '@/http/HttpError'
import Comment from '@/entity/comment/Comment'

const COMMENT_REPOSITORY = container.resolve(CommentRepository)
const props = defineProps<{
  postId: number
}>()
const state = reactive({
  comments: [] as Comment[],
  commentWrite: new CommentWrite(),
  deletePassword: '' //삭제 시 입력받을 비밀번호
})

//댓글 목록 불러오는 함수
function fetchComments() {
  COMMENT_REPOSITORY.getList(props.postId)
    .then((comments) => (state.comments = comments.items))
    .catch(() => ElMessage.error('댓글을 불러오는데 실패했습니다.'))
}
// 댓글 추가 함수
function addComment() {
  COMMENT_REPOSITORY.write(props.postId, state.commentWrite)
    .then(() => {
      ElMessage.success('댓글이 등록되었습니다.')
      state.commentWrite = new CommentWrite() // 입력란 초기화
      fetchComments()
    })
    .catch((e) => {
      // 서버에서 전송된 유효성 검사 오류 메시지를 처리하는 부분
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

// 댓글 삭제 시 비밀번호 입력 후 삭제하는 함수
function deleteComment(commentId: number) {
  ElMessageBox.prompt('비밀번호를 입력하세요', '댓글 삭제', {
    inputType: 'password',
    confirmButtonText: '확인',
    cancelButtonText: '취소'
  })
    .then(({ value }) => {
      // CommentDeleteRequest 객체를 생성하여 댓글 ID와 비밀번호를 저장합니다.
      const request = new CommentDeleteRequest(commentId, value)

      COMMENT_REPOSITORY.delete(request) // 비밀번호와 ID를 CommentDeleteRequest 객체로 전달
        .then(() => {
          ElMessage.success('댓글이 삭제되었습니다.')
          fetchComments() // 삭제 후 목록 갱신
        })
        .catch(() => ElMessage.error('댓글 삭제에 실패했습니다.'))
    })
    .catch(() => {
      ElMessage.info('삭제가 취소되었습니다.')
    })
}

//컴포넌트가 마운트될 때 댓글 목록 불러오기
onMounted(fetchComments)
</script>

<template>
  <div class="totalCount">댓글 {{ state.comments.length }}개</div>
  <div class="write">
    <div class="form">
      <div class="section">
        <label for="author">작성자</label>
        <el-input v-model="state.commentWrite.author" placeholder="이름을 입력하세요" />
        <label for="password">비밀번호</label>
        <el-input v-model="state.commentWrite.password" type="password" placeholder="비밀번호를 입력하세요" />
      </div>
      <div class="content">
        <label for="content">내용</label>
        <el-input v-model="state.commentWrite.content" type="textarea" rows="5" placeholder="댓글 내용을 입력하세요" />
      </div>
    </div>
    <el-button @click="addComment" type="primary">등록하기</el-button>
  </div>

  <ul class="comments">
    <li v-for="comment in state.comments" :key="comment.id">
      <CommentComponents :comment="comment as any" @deleteComment="deleteComment" />
    </li>
  </ul>
</template>

<style scoped lang="scss">
.totalCount {
  font-size: 1.4rem;
}

.write {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .form {
    display: flex;
    gap: 12px;
    margin-top: 10px;

    .section {
      width: 140px;
      display: flex;
      gap: 5px;
      flex-direction: column;
    }

    .content {
      flex-grow: 1;
    }
  }

  .button {
    width: 100px;
    align-self: flex-end;
  }
}

label {
  font-size: 0.78rem;
}

.comments {
  margin-top: 3rem;
  list-style: none;
  padding: 0;

  .comment {
    margin-bottom: 2.4rem;

    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
