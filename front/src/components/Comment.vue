<script setup lang="ts"></script>

<template>
  <div class="comment">
    <div class="header">
      <div class="section">
        <div class="author">밥돌이</div>
        <div class="regDate">2023-05-01 23:59:59</div>
      </div>

      <div class="delete">삭제</div>
    </div>
    <div class="content">나 밥돌맨인데 개추했다.</div>
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
