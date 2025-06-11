export interface Product {
  id: number;
  name: string;
  price: number;
  image: string;
  description: string;
}

export interface SearchSuggestion {
  text: string;
  id: number;
} 