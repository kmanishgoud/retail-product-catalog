import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-create-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './create-product.html'
})
export class CreateProductComponent {

  productForm: FormGroup;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router,
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

    this.productService.createProduct(productData).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.successMessage = 'Product created successfully!';
        this.cdr.detectChanges();
        setTimeout(() => this.router.navigate(['/']), 2000);
      },
      
      error: (err) => {
  if (err.status === 400 && err.error?.errors) {
    const backendErrors = err.error.errors;
    this.errorMessage = Object.values(backendErrors).join(' • ');
  } else {
    this.errorMessage = 'Failed to create product. Please try again.';
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