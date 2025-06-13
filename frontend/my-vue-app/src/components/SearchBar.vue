<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted } from 'vue';

const props = defineProps<{
  modelValue: string;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void;
  (e: 'search', value: string): void;
}>();

const searchInput = ref(props.modelValue);
const suggestions = ref<string[]>([]);
const showSuggestions = ref(false);
const searchContainerRef = ref<HTMLElement | null>(null);
const isSelectingSuggestion = ref(false);
const debounceTimer = ref<number | null>(null);

// 防抖函数
const debounce = (fn: Function, delay: number) => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }
  debounceTimer.value = window.setTimeout(() => {
    fn();
    debounceTimer.value = null;
  }, delay);
};

// 获取搜索建议
const fetchSuggestions = async (value: string) => {
  if (!value.trim()) {
    suggestions.value = [];
    showSuggestions.value = false;
    return;
  }

  try {
    const response = await fetch(`/es/suggestions?keyword=${encodeURIComponent(value)}`);
    if (!response.ok) {
      throw new Error('获取搜索建议失败');
    }
    suggestions.value = await response.json();
    showSuggestions.value = suggestions.value.length > 0;
  } catch (error) {
    console.error('获取搜索建议失败:', error);
    suggestions.value = [];
    showSuggestions.value = false;
  }
};

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

// 监听输入变化
watch(() => searchInput.value, (newValue) => {
  if (isSelectingSuggestion.value) {
    isSelectingSuggestion.value = false;  // 重置标记
    return;  // 如果是选择建议项导致的变化，直接返回
  }

  // 使用防抖处理搜索建议请求
  debounce(() => fetchSuggestions(newValue), 300);
});

const handleSearch = () => {
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }
  emit('search', searchInput.value);
  showSuggestions.value = false;
};

const selectSuggestion = (suggestion: string) => {
  isSelectingSuggestion.value = true;
  // 移除高亮标签获取纯文本
  const plainText = suggestion.replace(/<\/?em>/g, '');
  searchInput.value = plainText;
  emit('update:modelValue', plainText);
  emit('search', plainText);
  showSuggestions.value = false;
};

// 添加和移除事件监听器
onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
  // 清理可能存在的定时器
  if (debounceTimer.value) {
    clearTimeout(debounceTimer.value);
  }
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
          v-for="(suggestion, index) in suggestions"
          :key="index"
          class="suggestion-item"
          @click="selectSuggestion(suggestion)"
          @mousedown.prevent
          v-html="suggestion"
        >
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

:deep(em) {
  color: #ff4d4f;
  font-style: normal;
  font-weight: bold;
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