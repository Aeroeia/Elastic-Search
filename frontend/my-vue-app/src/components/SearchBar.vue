<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue';
import type { SearchSuggestion } from '../types/product';

const props = defineProps<{
  modelValue: string;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'search', value: string): void;
}>();

const searchInput = ref(props.modelValue);
const suggestions = ref<SearchSuggestion[]>([]);
const showSuggestions = ref(false);
const searchContainerRef = ref<HTMLElement | null>(null);
const isSelectingSuggestion = ref(false);  // 新增：标记是否正在选择建议项

// 模拟搜索建议数据
const mockSuggestions: SearchSuggestion[] = [
  { text: 'MacBook Pro M3', id: 1 },
  { text: 'MacBook Air M2', id: 2 },
  { text: 'MacBook Pro 16寸', id: 3 },
  { text: 'MacBook Air M1', id: 4 },
  { text: 'MacBook Pro M2', id: 5 },
  { text: 'MacBook Air 13寸', id: 6 },
  { text: 'MacBook Pro M1', id: 7 }
];

// 处理点击外部区域
const handleClickOutside = (event: MouseEvent) => {
  if (searchContainerRef.value && !searchContainerRef.value.contains(event.target as Node)) {
    showSuggestions.value = false;
  }
};

// 处理输入框聚焦
const handleFocus = () => {
  if (searchInput.value.trim() && suggestions.value.length > 0) {
    showSuggestions.value = true;
  }
};

watch(() => searchInput.value, async (newValue) => {
  if (isSelectingSuggestion.value) {
    isSelectingSuggestion.value = false;  // 重置标记
    return;  // 如果是选择建议项导致的变化，直接返回
  }

  if (newValue.trim()) {
    try {
      // 模拟API调用延迟
      await new Promise(resolve => setTimeout(resolve, 100));
      
      // 根据输入过滤建议
      suggestions.value = mockSuggestions.filter(suggestion => 
        suggestion.text.toLowerCase().includes(newValue.toLowerCase())
      );
      showSuggestions.value = suggestions.value.length > 0;
    } catch (error) {
      console.error('获取搜索建议失败:', error);
    }
  } else {
    suggestions.value = [];
    showSuggestions.value = false;
  }
});

const handleSearch = () => {
  emit('search', searchInput.value);
  showSuggestions.value = false;
};

const selectSuggestion = (suggestion: SearchSuggestion) => {
  isSelectingSuggestion.value = true;  // 设置标记，表示正在选择建议项
  searchInput.value = suggestion.text;
  emit('update:modelValue', suggestion.text);
  emit('search', suggestion.text);
  showSuggestions.value = false;
};

// 添加和移除事件监听器
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<template>
  <div class="search-container" ref="searchContainerRef">
    <div class="search-input-wrapper">
      <input
        v-model="searchInput"
        type="text"
        class="search-input"
        placeholder="搜索商品..."
        @keyup.enter="handleSearch"
        @focus="handleFocus"
      />
      <button class="search-button" @click="handleSearch">
        <span class="search-icon">🔍</span>
        <span class="search-text">搜索</span>
      </button>
    </div>
    
    <div v-if="showSuggestions && suggestions.length > 0" class="suggestions-container">
      <ul class="suggestions-list">
        <li
          v-for="suggestion in suggestions"
          :key="suggestion.id"
          class="suggestion-item"
          @click="selectSuggestion(suggestion)"
          @mousedown.prevent
        >
          <span class="suggestion-icon">🔍</span>
          {{ suggestion.text }}
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
.search-container {
  position: relative;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.search-input-wrapper {
  display: flex;
  gap: 12px;
  background: white;
  border-radius: 8px;
  padding: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  font-size: 16px;
  color: black;
  border: 1px solid black;
  border-radius: 6px;
  outline: none;
  transition: all 0.3s ease;
  background: #f8f8f8;
}

.search-input:focus {
  background: white;
  box-shadow: 0 0 0 2px rgba(255, 77, 79, 0.2);
}

.search-button {
  padding: 0 24px;
  font-size: 16px;
  color: white;
  background-color: #ff4d4f;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
  outline: none;
}

.search-button:focus {
  outline: none;
}

.search-button:hover {
  transform: translateY(-1px);
}

.search-button:active {
  transform: translateY(1px);
}

.search-icon {
  font-size: 18px;
}

.suggestions-container {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  right: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  overflow: hidden;
}

.suggestions-list {
  list-style: none;
  padding: 8px 0;
  margin: 0;
  max-height: 300px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 12px 16px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: background-color 0.2s;
}

.suggestion-item:hover {
  background-color: #f5f5f5;
}

.suggestion-icon {
  color: #999;
  font-size: 14px;
}

@media (max-width: 768px) {
  .search-container {
    max-width: 100%;
  }

  .search-text {
    display: none;
  }

  .search-button {
    padding: 0 16px;
  }

  .search-input {
    font-size: 14px;
    padding: 10px 12px;
  }
}
</style> 