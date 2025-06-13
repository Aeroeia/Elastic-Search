export interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  description: string;
  brand?: string;
  category?: string;
  highlight?: string;
}

export interface SearchSuggestion {
  text: string;
} 