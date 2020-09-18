import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CustomersFormComponent } from './customers-form/customers-form.component';
import { CustomersListComponent } from './customers-list/customers-list.component';
import { LayoutComponent } from '../layout/layout.component';
import { AuthGuard } from '../auth.guard';

const routes: Routes = [
  { path : 'customers' , component: LayoutComponent , canActivate : [AuthGuard], children : [
    { path : '' , redirectTo : '/customers/list' , pathMatch : 'full' },
    { path : 'form' , component: CustomersFormComponent},
    { path : 'form/:id' , component: CustomersFormComponent},
    { path : 'list' , component: CustomersListComponent}
  ]},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomersRoutingModule { }
