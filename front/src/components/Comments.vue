<script setup lang="ts">
import Comment from '@/components/Comment.vue'
</script>

<template>
  <div class="totalCount">댓글 0개</div>

  <div class="write">
    <div class="form">
      <div class="section">
        <div>
          <label for="author">작성자</label>
          <el-input id="author" placeholder="밥돌맨"></el-input>
        </div>

        <div>
          <label for="password">비밀번호</label>
          <el-input type="password" id="password" placeholder="비밀번호"></el-input>
        </div>
      </div>

      <div class="content">
        <label for="password">내용</label>
        <el-input id="content" type="textarea" :rows="5" :autosize="{ minRows: 5, maxRows: 4 }"></el-input>
      </div>
    </div>

    <el-button type="primary" class="button">등록하기</el-button>
  </div>

  <ul class="comments">
    <li class="comment">
      <Comment />
    </li>

    <li class="comment">
      <Comment />
    </li>

    <li class="comment">
      <Comment />
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
