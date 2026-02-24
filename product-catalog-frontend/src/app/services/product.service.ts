import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from './product';
import { PageResponse } from './page-response';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = '/api/products';

  constructor(private http: HttpClient) {}

  getProducts(page: number = 0, size: number = 8, sortBy: string = 'id'): Observable<PageResponse<Product>> {
    return this.http.get<PageResponse<Product>>(
      `${this.apiUrl}?page=${page}&size=${size}&sortBy=${sortBy}`
    );
  }

  searchProducts(keyword: string, page: number = 0, size: number = 8): Observable<PageResponse<Product>> {
    return this.http.get<PageResponse<Product>>(
      `${this.apiUrl}/search?keyword=${keyword}&page=${page}&size=${size}`
    );
  }

  getProductById(id: number): Observable<Product> {
  return this.http.get<Product>(`${this.apiUrl}/${id}`);
}
  getProductsByCategory(category: string, page: number = 0, size: number = 8): Observable<PageResponse<Product>> {
    return this.http.get<PageResponse<Product>>(
      `${this.apiUrl}/category/${category}?page=${page}&size=${size}`
    );
  }

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, product);
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}