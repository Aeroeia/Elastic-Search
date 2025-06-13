<script setup lang="ts">
import { ref, onMounted } from 'vue';
import SearchBar from './components/SearchBar.vue';
import ProductGrid from './components/ProductGrid.vue';
import type { Product } from './types/product';
import logo from './assets/vue.svg';

const searchQuery = ref('');
const products = ref<Product[]>([]);

const fetchProducts = async (query: string = '') => {
  try {
    const response = await fetch(`/es/search?keyword=${encodeURIComponent(query)}`);
    if (!response.ok) {
      throw new Error('获取商品数据失败');
    }
    products.value = await response.json();
  } catch (error) {
    console.error('获取商品数据失败:', error);
    products.value = [];
  }
};

const handleSearch = (query: string) => {
  searchQuery.value = query;
  fetchProducts(query);
};

onMounted(() => {
  fetchProducts();
});
</script>

<template>
  <div class="app">
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <img :src="logo" alt="商城logo" />
        </div>
        <SearchBar
          v-model="searchQuery"
          @search="handleSearch"
        />
      </div>
    </header>
    <main class="main">
      <div class="content-wrapper">
        <h1 class="page-title">精选商品</h1>
        <ProductGrid :products="products" />
      </div>
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: #f5f5f5;
  color: #333;
  line-height: 1.6;
}

.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background-color: white;
  padding: 16px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  align-items: center;
  gap: 48px;
}

.logo {
  width: 120px;
  height: 40px;
  display: flex;
  align-items: center;
}

.logo img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.main {
  flex: 1;
  padding: 32px 0;
}

.content-wrapper {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 32px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  color: #1a1a1a;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
    gap: 24px;
  }
  
  .content-wrapper {
    padding: 0 16px;
  }
  
  .page-title {
    font-size: 20px;
    margin-bottom: 16px;
  }
}
</style>
