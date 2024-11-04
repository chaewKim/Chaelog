<script setup lang="ts">
//댓글 항목 하나 보여주기 위한 컴포넌트
import Comment from '@/Entity/comment/Comment'

const props = defineProps<{
  comment: Comment
}>()
const emit = defineEmits(['deleteComment'])
function handleDelete() {
  emit('deleteComment', props.comment.id)
}
</script>

<template>
  <div class="comment">
    <div class="header">
      <div class="section">
        <div class="author">{{ props.comment.author }}</div>
        <div class="regDate">{{ props.comment.getDisplaySimpleRegDate() }}</div>
      </div>

      <div class="delete" @click="handleDelete">삭제</div>
    </div>
    <div class="content">{{ props.comment.content }}</div>
  </div>
</template>

<style scoped lang="scss">
.comment {
  width: 100%;
}

.header {
  display: flex;
  justify-content: space-between;
}

.section {
  display: flex;
  flex-direction: column;
}

.author {
  font-weight: 600;
  font-size: 1.2rem;
}

.regDate {
  margin-top: 5px;
  color: #797979;
  font-size: 0.88rem;
}

.content {
  margin-top: 0.8rem;
}

.delete {
  font-size: 0.78rem;
  color: red;
}
</style>
