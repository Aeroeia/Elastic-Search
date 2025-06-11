<script setup lang="ts">
import type { Product } from '../types/product';

defineProps<{
  products: Product[];
}>();
</script>

<template>
  <div class="product-grid">
    <div v-for="product in products" :key="product.id" class="product-card">
      <div class="product-image">
        <img :src="product.image" :alt="product.name" />
      </div>
      <div class="product-info">
        <h3 class="product-name">{{ product.name }}</h3>
        <p class="product-description">{{ product.description }}</p>
        <div class="product-price">
          <span class="price-symbol">¥</span>
          <span class="price-amount">{{ product.price.toLocaleString('zh-CN', { minimumFractionDigits: 2 }) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 32px;
  padding: 32px;
  max-width: 1400px;
  margin: 0 auto;
}

.product-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 20px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 280px;
  overflow: hidden;
  background: #f8f8f8;
  position: relative;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.product-info {
  padding: 24px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-name {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
}

.product-description {
  margin: 0;
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  flex: 1;
}

.product-price {
  margin-top: auto;
  font-size: 24px;
  color: #ff4d4f;
  font-weight: bold;
  display: flex;
  align-items: baseline;
}

.price-symbol {
  font-size: 16px;
  margin-right: 2px;
}

.price-amount {
  font-size: 24px;
}

@media (max-width: 768px) {
  .product-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 16px;
    padding: 16px;
  }

  .product-image {
    height: 200px;
  }

  .product-info {
    padding: 16px;
  }

  .product-name {
    font-size: 18px;
  }

  .price-amount {
    font-size: 20px;
  }
}
</style> 