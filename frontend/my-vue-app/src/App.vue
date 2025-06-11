<script setup lang="ts">
import { ref, onMounted } from 'vue';
import SearchBar from './components/SearchBar.vue';
import ProductGrid from './components/ProductGrid.vue';
import type { Product } from './types/product';
import logo from './assets/vue.svg';

const searchQuery = ref('');
const products = ref<Product[]>([]);

// 模拟数据
const mockProducts: Product[] = [
  {
    id: 1,
    name: 'MacBook Pro M3',
    price: 14999,
    image: 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mbp14-m3-max-pro-spaceblack-select-202310?wid=452&hei=420&fmt=jpeg&qlt=95&.v=1697311054290',
    description: '搭载 M3 芯片的 MacBook Pro，性能强劲，续航持久'
  },
  {
    id: 2,
    name: 'MacBook Air M2',
    price: 9999,
    image: 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mba15-midnight-select-202306?wid=452&hei=420&fmt=jpeg&qlt=95&.v=1684518479433',
    description: '轻薄便携的 MacBook Air，采用 M2 芯片，完美平衡性能与便携性'
  },
  {
    id: 3,
    name: 'MacBook Pro 16"',
    price: 18999,
    image: 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/mbp16-m3-max-pro-spaceblack-select-202310?wid=452&hei=420&fmt=jpeg&qlt=95&.v=1697311054293',
    description: '16 英寸大屏幕 MacBook Pro，专业人士的理想之选'
  },
  {
    id: 4,
    name: 'MacBook Air M1',
    price: 7999,
    image: 'https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/macbook-air-space-gray-select-201810?wid=452&hei=420&fmt=jpeg&qlt=95&.v=1633027804000',
    description: '入门级 MacBook Air，M1 芯片带来的强大性能'
  }
];

const fetchProducts = async (query: string = '') => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500)); // 添加延迟以模拟网络请求
    
    if (query) {
      // 如果有搜索关键词，进行过滤
      products.value = mockProducts.filter(product => 
        product.name.toLowerCase().includes(query.toLowerCase()) ||
        product.description.toLowerCase().includes(query.toLowerCase())
      );
    } else {
      // 没有搜索关键词时显示所有商品
      products.value = mockProducts;
    }
  } catch (error) {
    console.error('获取商品数据失败:', error);
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
  max-width: 100%;
  max-height: 100%;
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
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 24px;
  text-align: center;
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
    gap: 24px;
  }

  .logo {
    width: 80px;
    height: 32px;
  }

  .content-wrapper {
    padding: 0 16px;
  }

  .page-title {
    font-size: 24px;
    margin-bottom: 16px;
  }
}
</style>
