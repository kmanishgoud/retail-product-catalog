import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProductService } from '../services/product.service';
import { Product } from '../services/product';
import { PageResponse } from '../services/page-response';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './product-list.html'
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 0;
  isLoading: boolean = false;

  searchKeyword: string = '';
  selectedCategory: string = '';
  categories: string[] = ['Electronics', 'Clothing', 'Books', 'Home & Kitchen', 'Sports', 'Toys', 'Beauty', 'Automotive'];

  deletingId: number | null = null;
  showDeleteConfirm: boolean = false;
  productToDelete: Product | null = null;

  constructor(
  private productService: ProductService,
  private authService: AuthService,
  private cdr: ChangeDetectorRef
) {}

  isAdmin = false;

ngOnInit(): void {
  this.isAdmin = this.authService.isAdmin();
  this.loadProducts();
}

  loadProducts(page: number = 0): void {
    this.isLoading = true;
    let request;

    if (this.searchKeyword.trim()) {
      request = this.productService.searchProducts(this.searchKeyword.trim(), page);
    } else if (this.selectedCategory) {
      request = this.productService.getProductsByCategory(this.selectedCategory, page);
    } else {
      request = this.productService.getProducts(page);
    }

    request.subscribe((data: PageResponse<Product>) => {
      this.products = data.content;
      this.totalElements = data.totalElements;
      this.totalPages = data.totalPages;
      this.currentPage = data.number;
      this.isLoading = false;
      this.cdr.detectChanges();
    });
  }

  onSearch(): void {
    this.selectedCategory = '';
    this.loadProducts(0);
  }

  onCategoryChange(): void {
    this.searchKeyword = '';
    this.loadProducts(0);
  }

  onClearFilters(): void {
    this.searchKeyword = '';
    this.selectedCategory = '';
    this.loadProducts(0);
  }

  confirmDelete(product: Product): void {
    this.productToDelete = product;
    this.showDeleteConfirm = true;
    this.cdr.detectChanges();
  }

  cancelDelete(): void {
    this.productToDelete = null;
    this.showDeleteConfirm = false;
    this.cdr.detectChanges();
  }

  executeDelete(): void {
    if (!this.productToDelete?.id) return;
    this.deletingId = this.productToDelete.id;

    this.productService.deleteProduct(this.productToDelete.id).subscribe({
      next: () => {
        this.deletingId = null;
        this.showDeleteConfirm = false;
        this.productToDelete = null;
        this.loadProducts(this.currentPage);
      },
      error: (err) => {
        this.deletingId = null;
        this.cdr.detectChanges();
      }
    });
  }
}