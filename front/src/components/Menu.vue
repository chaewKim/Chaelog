<template>
  <ul class="menus">
    <li class="menu">
      <router-link to="/"> 처음으로 </router-link>
    </li>
    <li class="menu">
      <router-link to="/login"> 로그인 </router-link>
    </li>
    <li class="menu">
      <router-link to="/write"> 글 작성 </router-link>
    </li>
  </ul>
</template>

<script setup lang="ts"></script>
<style scoped lang="scss">
.menus {
  height: 20px;
  list-style: none;
  padding: 0;
  font-size: 0.88rem;
  font-weight: 300;
  text-align: center;
  margin: 0;
}

.menu {
  display: inline;
  margin-right: 1rem;

  &:last-child {
    margin-right: 0;
  }
  a {
    color: inherit;
  }
}
</style>
