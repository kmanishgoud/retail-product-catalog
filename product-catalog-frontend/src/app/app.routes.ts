import { Routes } from '@angular/router';
import { ProductListComponent } from './product-list/product-list';
import { CreateProductComponent } from './create-product/create-product';
import { EditProductComponent } from './edit-product/edit-product';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { adminGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '',                 component: ProductListComponent },
  { path: 'login',            component: LoginComponent },
  { path: 'register',         component: RegisterComponent },
  { path: 'create-product',   component: CreateProductComponent, canActivate: [adminGuard] },
  { path: 'edit-product/:id', component: EditProductComponent,   canActivate: [adminGuard] },
  { path: '**',               redirectTo: '' }
];