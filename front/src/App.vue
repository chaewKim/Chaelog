<script setup lang="ts">
import Header from '@/components/Header.vue'
import Menu from '@/components/Menu.vue'
import Content from '@/components/Content.vue'
</script>

<template>
  <el-row>
    <el-col :span="22" :offset="1">
      <Header />
    </el-col>
  </el-row>

  <el-row>
    <el-col :span="22" :offset="1">
      <Menu />
    </el-col>
  </el-row>

  <el-row>
    <el-col
      :span="22"
      :offset="1"
      :sm="{ span: 20, offset: 2 }"
      :md="{ span: 16, offset: 4 }"
      :lg="{ span: 14, offset: 5 }"
    >
      <Content />
    </el-col>
  </el-row>
</template>

<style></style>

<style>
html {
  font-size: 16px;
  color: #383838;
}

body {
  background-color: #f9f9f9;
}
</style>
