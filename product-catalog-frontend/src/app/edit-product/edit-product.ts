import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-edit-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-product.html'
})
export class EditProductComponent implements OnInit {

  productForm: FormGroup;
  productId!: number;
  isLoading = true;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {
    this.productForm = this.fb.group({
      name:          ['', [Validators.required, Validators.minLength(2)]],
      description:   ['', [Validators.required, Validators.minLength(10)]],
      price:         [null, [Validators.required, Validators.min(0.01)]],
      currency:      ['GBP'],
      category:      ['', Validators.required],
      stockQuantity: [null, [Validators.required, Validators.min(0)]],
      imageUrl:      ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(this.productId).subscribe({
      next: (product) => {
        this.productForm.patchValue({
          name:          product.name,
          description:   product.description,
          price:         product.price,
          currency:      'GBP',
          category:      product.category,
          stockQuantity: product.stockQuantity,
          imageUrl:      product.imageUrl
        });
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.errorMessage = 'Failed to load product.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  get f() { return this.productForm.controls; }

  onSubmit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    const { currency, ...productData } = this.productForm.value;

    this.productService.updateProduct(this.productId, productData).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Product updated successfully!';
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/']), 2000);
      },
      error: (err) => {
        if (err.status === 400 && err.error?.errors) {
          const backendErrors = err.error.errors;
          this.errorMessage = Object.values(backendErrors).join(' • ');
        } else {
          this.errorMessage = 'Failed to update product. Please try again.';
        }
        this.isSubmitting = false;
        this.cdr.detectChanges();
        console.error(err);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}