<script setup lang="ts">
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import { onMounted, reactive } from 'vue'
import Paging from '@/entity/data/Paging'
import Post from '@/entity/post/Post'
import PostComponent from '@/components/PostComponent.vue'

const POST_REPOSITORY = container.resolve(PostRepository)

// 상태 관리
type StateType = {
  postList: Paging<Post>
  searchKeyword: string
  currentPage: number
}
const state = reactive<StateType>({
  postList: new Paging<Post>(),
  searchKeyword: '', // 검색어 상태
  currentPage: 1 // 현재 페이지 번호
})

// 게시글 목록 가져오기
function getList(page = 1) {
  state.currentPage = page // 페이지 갱신
  if (state.searchKeyword.trim() === '') {
    // 검색어가 없을 때 전체 게시글 가져오기
    POST_REPOSITORY.getList(page)
      .then((postList) => {
        state.postList = postList
      })
      .catch(() => {
        console.error('전체 게시글을 불러오는 중 문제가 발생했습니다.')
      })
  } else {
    // 검색어가 있을 때 검색 결과 가져오기
    POST_REPOSITORY.search(state.searchKeyword, state.currentPage, 3)
      .then((postList) => {
        state.postList = postList
      })
      .catch(() => {
        console.error('검색 결과를 불러오는 중 문제가 발생했습니다.')
      })
  }
}

// 검색 이벤트 처리
const handleSearch = () => {
  getList(1) // 검색 시 페이지를 1로 초기화
}

// 초기 데이터 로드
onMounted(() => {
  getList()
})
</script>

<template>
  <div class="content">
    <!-- 검색 바 -->
    <div class="search-bar">
      <input v-model="state.searchKeyword" type="text" placeholder="검색어를 입력하세요" />
      <button @click="handleSearch">검색</button>
    </div>

    <!-- 게시글 정보 -->
    <span class="totalCount">게시글 수 : {{ state.postList.totalCount }}</span>

    <!-- 게시글 목록 -->
    <ul class="posts">
      <li v-for="post in state.postList.items" :key="post.id">
        <PostComponent :post="post as any" />
      </li>
    </ul>

    <!-- 페이지네이션 -->
    <el-pagination
      :background="true"
      v-model:current-page="state.currentPage"
      layout="prev, pager, next"
      :total="state.postList.totalCount"
      :default-page-size="3"
      @current-change="(page: number) => getList(page)"
    />
  </div>
</template>

<style scoped lang="scss">
.content {
  padding: 0 1rem 0 1rem;
  margin-bottom: 2rem;
}

.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;

  input {
    flex: 1;
    padding: 0.5rem;
    margin-right: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
  }

  button {
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    &:hover {
      background-color: #0056b3;
    }
  }
}

.totalCount {
  font-size: 0.88rem;
  margin-bottom: 1rem;
}

.posts {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 1rem;
    &:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
